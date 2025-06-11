package com.iyuba.biaori.simplified.ui.me;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.iyuba.biaori.simplified.Constant;
import com.iyuba.biaori.simplified.MyApplication;
import com.iyuba.biaori.simplified.R;
import com.iyuba.biaori.simplified.adapter.break_through.CPWordAdapter;
import com.iyuba.biaori.simplified.databinding.ActivityWordCollectBinding;
import com.iyuba.biaori.simplified.db.JpWord;
import com.iyuba.biaori.simplified.model.bean.me.WordPdfBean;
import com.iyuba.biaori.simplified.model.bean.textbook.JpWordCollectBean;
import com.iyuba.biaori.simplified.presenter.BasePresenter;
import com.iyuba.biaori.simplified.presenter.me.WordCollectPresenter;
import com.iyuba.biaori.simplified.ui.BaseActivity;
import com.iyuba.biaori.simplified.ui.break_through.BTWordsActivity;
import com.iyuba.biaori.simplified.ui.textbook.WordDetailsActivity;
import com.iyuba.biaori.simplified.view.me.WordCollectContract;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的 - 单词收藏
 */
public class WordCollectActivity extends BaseActivity<WordCollectContract.WordCollectView, WordCollectContract.WordCollectPresenter>
        implements WordCollectContract.WordCollectView {

    private ActivityWordCollectBinding activityWordCollectBinding;

    private CPWordAdapter cpWordAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initOperation();
    }

    private void initOperation() {

        activityWordCollectBinding.toolbar.toolbarIvPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                presenter.getWordToPDF(Constant.userinfo.getUid(), "query", "");
            }
        });

        activityWordCollectBinding.toolbar.toolbarIvTitle.setText("单词本");
        activityWordCollectBinding.toolbar.toolbarIvRight.setVisibility(View.VISIBLE);
        activityWordCollectBinding.toolbar.toolbarIvRight.setImageResource(R.mipmap.icon_getyun);
        activityWordCollectBinding.toolbar.toolbarIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        activityWordCollectBinding.toolbar.toolbarIvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cpWordAdapter.setNewData(new ArrayList<>());
                showLoading(null);

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        presenter.getWordCollect(Constant.userinfo.getUid() + "", "query", Constant.APPID);
                    }
                }, 1000);
            }
        });

        activityWordCollectBinding.wcRv.setLayoutManager(new LinearLayoutManager(this));
        cpWordAdapter = new CPWordAdapter(R.layout.item_cp_word, new ArrayList<>());
        activityWordCollectBinding.wcRv.setAdapter(cpWordAdapter);
        cpWordAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                JpWord word = cpWordAdapter.getItem(position);
                if (word.isShow()) {

                    word.setShow(false);
                } else {

                    word.setShow(true);
                }
                cpWordAdapter.notifyItemChanged(position);
            }
        });
        cpWordAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                WordDetailsActivity.startActivity(WordCollectActivity.this, cpWordAdapter.getData(), position);
            }
        });
    }

    private void initData() {


        List<JpWord> jpWordList = LitePal.where("collect = ?", "1").find(JpWord.class);
        if (jpWordList.size() == 0) {
            showLoading(null);
            presenter.getWordCollect(Constant.userinfo.getUid() + "", "query", Constant.APPID);
        } else {

            hideLoading();
            cpWordAdapter.setNewData(jpWordList);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        initData();
    }

    @Override
    public View initLayout() {
        activityWordCollectBinding = ActivityWordCollectBinding.inflate(getLayoutInflater());
        return activityWordCollectBinding.getRoot();
    }

    @Override
    public WordCollectContract.WordCollectPresenter initPresenter() {
        return new WordCollectPresenter();
    }

    @Override
    public void showLoading(String msg) {


        activityWordCollectBinding.wcPb.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {

        activityWordCollectBinding.wcPb.setVisibility(View.GONE);
    }

    @Override
    public void toast(String msg) {

        Toast.makeText(MyApplication.getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getWordToPDF(WordPdfBean wordPdfBean) {


        AlertDialog alertDialog = new AlertDialog.Builder(WordCollectActivity.this)
                .setTitle("pdf")
                .setMessage(wordPdfBean.getFilePath())
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                })
                .setPositiveButton("打开", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(wordPdfBean.getFilePath())));
                    }
                })
                .show();
    }

    @Override
    public void getWordCollected(JpWordCollectBean wordCollectBean) {

        List<JpWord> jpWordList = wordCollectBean.getData();
        for (int i = 0; i < jpWordList.size(); i++) {

            JpWord word = jpWordList.get(i);
            List<JpWord> cWords = LitePal.where("wordid = ?", word.getId() + "").find(JpWord.class);

            if (cWords.size() == 0) {//没有此数据

                word.setCollect(1);
                word.save();
            } else {//有此数据更新

                JpWord cw = cWords.get(0);
                word.setCollect(1);
                word.setAnswer_status(cw.getAnswer_status());
                word.updateAll("wordid = ?", word.getId() + "");
            }
        }
        List<JpWord> wordList = LitePal.where("collect = 1").find(JpWord.class);
        cpWordAdapter.setNewData(wordList);
    }
}