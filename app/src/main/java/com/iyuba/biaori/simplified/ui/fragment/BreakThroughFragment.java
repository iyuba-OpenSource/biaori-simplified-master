package com.iyuba.biaori.simplified.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.iyuba.biaori.simplified.Constant;
import com.iyuba.biaori.simplified.MyApplication;
import com.iyuba.biaori.simplified.R;
import com.iyuba.biaori.simplified.adapter.break_through.BreakThroughAdapter;
import com.iyuba.biaori.simplified.databinding.FragmentBreakThroughBinding;
import com.iyuba.biaori.simplified.db.JpWord;
import com.iyuba.biaori.simplified.entity.Checkpoint;
import com.iyuba.biaori.simplified.model.bean.textbook.JpWordSentenceBean;
import com.iyuba.biaori.simplified.presenter.break_through.BreakThroughPresenter;
import com.iyuba.biaori.simplified.ui.BaseFragment;
import com.iyuba.biaori.simplified.ui.break_through.BTWordsActivity;
import com.iyuba.biaori.simplified.ui.login.WxLoginActivity;
import com.iyuba.biaori.simplified.ui.vip.VipActivity;
import com.iyuba.biaori.simplified.util.GridSpacingItemDecoration;
import com.iyuba.biaori.simplified.util.popup.BTMorePopup;
import com.iyuba.biaori.simplified.util.popup.LoadingPopup;
import com.iyuba.biaori.simplified.util.popup.WordLoadingPopup;
import com.iyuba.biaori.simplified.view.break_through.BreakThroughContract;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

/**
 * 单词闯关
 */
public class BreakThroughFragment extends BaseFragment<BreakThroughContract.BreakThroughView, BreakThroughContract.BreakThroughPresenter>
        implements BreakThroughContract.BreakThroughView {

    private static final String LEVEL = "LEVEL";

    private int level;

    private FragmentBreakThroughBinding fragmentBreakThroughBinding;

    private BTMorePopup btMorePopup;

    private WordLoadingPopup loadingPopup;

    private int progress = 0;

    private BreakThroughAdapter breakThroughAdapter;

    private SharedPreferences sp;

    /**
     * 单词数量
     */
    private int wordNum = 30;

    private GridSpacingItemDecoration gridSpacingItemDecoration;

    public BreakThroughFragment() {

    }

    public static BreakThroughFragment newInstance(int level) {
        BreakThroughFragment fragment = new BreakThroughFragment();
        Bundle args = new Bundle();
        args.putInt(LEVEL, level);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 更新bundle
     */
    public void updateBundle() {

        if (getArguments() != null) {
            level = getArguments().getInt(LEVEL, 6);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            level = getArguments().getInt(LEVEL, 6);
        }

        breakThroughAdapter = new BreakThroughAdapter(R.layout.item_break_through, new ArrayList<>());
        sp = getContext().getSharedPreferences(Constant.SP_BREAK_THROUGH, Context.MODE_PRIVATE);
        wordNum = sp.getInt(Constant.SP_KEY_WORD_NUM, 30);
        breakThroughAdapter.setWordNum(wordNum);
        breakThroughAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {


                Checkpoint checkpoint = breakThroughAdapter.getItem(position);

                if (Constant.userinfo != null) {


                    if(checkpoint.isPass()){

                        if (!Constant.userinfo.isVip() && position != 0) {

                            showBuyVipDialog();
                            return;
                        }
                    }else{

                        toast("此关未解锁");
                        return;
                    }
                } else {

                    startActivity(new Intent(requireActivity(), WxLoginActivity.class));
                    return;
                }

                if (checkpoint.isPass()) {

                    BTWordsActivity.startActivity(getActivity(), position, level);
                } else {

                    toast("此关未解锁");
                }

            }
        });

        gridSpacingItemDecoration = new GridSpacingItemDecoration(3, 40, true);
    }


    /**
     * 是否购买会员
     */
    private void showBuyVipDialog() {

        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setMessage("非VIP用户单词闯关体验闯1关，VIP会员无限单词闯关,是否购买会员？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        VipActivity.startActivity(getActivity(), 0);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                }).create();
        alertDialog.show();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initOperation();
//        initData();
    }

    @Override
    public void onResume() {
        super.onResume();

        List<JpWord> jpWordList = LitePal.where("level = ?", level + "").find(JpWord.class);
        if (jpWordList.size() == 0) {

            progress = 0;
            showLoading("第一次加载较慢，\n请不要退出\n更新进度：" + progress + "%");
            presenter.getJpWordSentenceAll(level);
        } else {
            initData();
        }
    }

    private void initOperation() {

        fragmentBreakThroughBinding.toolbar.toolbarIvBack.setVisibility(View.GONE);
        if (Constant.bookDataDTO == null) {

            fragmentBreakThroughBinding.toolbar.toolbarIvTitle.setText("单词闯关");
        } else {

            fragmentBreakThroughBinding.toolbar.toolbarIvTitle.setText(Constant.bookDataDTO.getBook());
        }
        fragmentBreakThroughBinding.toolbar.toolbarIvRight.setImageResource(R.mipmap.icon_getyun);
        fragmentBreakThroughBinding.toolbar.toolbarIbRight2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                initMorePopup();
            }
        });
        fragmentBreakThroughBinding.toolbar.toolbarIvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progress = 0;
                showLoading("第一次加载较慢，\n请不要退出\n更新进度：" + progress + "%");
                presenter.getJpWordSentenceAll(level);
            }
        });
        fragmentBreakThroughBinding.btRv.setLayoutManager(new GridLayoutManager(getContext(), 3));
        fragmentBreakThroughBinding.btRv.setAdapter(breakThroughAdapter);
        if (fragmentBreakThroughBinding.btRv.getItemDecorationCount() == 0) {

            fragmentBreakThroughBinding.btRv.addItemDecoration(gridSpacingItemDecoration);
        }
    }

    private void initData() {
        //处理单词库变更，title也要变
        if (Constant.bookDataDTO == null) {

            fragmentBreakThroughBinding.toolbar.toolbarIvTitle.setText("单词闯关");
        } else {

            fragmentBreakThroughBinding.toolbar.toolbarIvTitle.setText(Constant.bookDataDTO.getBook());
        }

        //此level的数量
        int size = LitePal.where("level = ?", level + "").count(JpWord.class);
        int checkpointNuM = size / wordNum;

        //已通过的关卡
        List<Checkpoint> checkpointList = new ArrayList<>();
        for (int i = 0; i < checkpointNuM; i++) {

            List<JpWord> jpWordList = LitePal
                    .where("level = ? ", level + "")
                    .order("wordid")
                    .limit(wordNum)
                    .offset(i * wordNum)
                    .find(JpWord.class);
            Checkpoint checkpoint = dealPass(jpWordList);
            checkpointList.add(checkpoint);
        }

        //设置可进行的最高关卡,将已通过的关卡设置成通过状态(已通过的关卡可能再次闯关会变成未通过状态)
        int position = 0;
        for (int i = (checkpointList.size() - 1); i >= 0; i--) {

            Checkpoint checkpoint = checkpointList.get(i);
            if (position != 0) {

                checkpoint.setPass(true);
            }
            if (checkpoint.isPass()) {

                position++;
            }
        }
        if (position < checkpointList.size()) {

            checkpointList.get(position).setPass(true);
        }
        breakThroughAdapter.setNewData(checkpointList);
    }


    /**
     * 计算这一关的正确率，大于等于80%算通过
     *
     * @param jpWordList
     * @return
     */
    private Checkpoint dealPass(List<JpWord> jpWordList) {

        int tnum = 0;
        for (int i = 0; i < jpWordList.size(); i++) {

            JpWord word = jpWordList.get(i);
            if (word.getAnswer_status() == 1) {

                tnum++;
            }
        }

        int accuracy = (int) (100.0 * tnum / jpWordList.size());
        Checkpoint checkpoint = new Checkpoint();
        checkpoint.settNum(tnum);
        if (accuracy >= 80) {

            checkpoint.setPass(true);
        } else {

            checkpoint.setPass(false);
        }
        return checkpoint;
    }


    @Override
    protected View initLayout(LayoutInflater inflater, ViewGroup container) {
        fragmentBreakThroughBinding = FragmentBreakThroughBinding.inflate(inflater, container, false);
        return fragmentBreakThroughBinding.getRoot();
    }

    @Override
    protected BreakThroughContract.BreakThroughPresenter initPresenter() {
        return new BreakThroughPresenter();
    }

    @Override
    public void showLoading(String msg) {

        if (loadingPopup == null) {

            loadingPopup = new WordLoadingPopup(getContext());
        }
        loadingPopup.setContent(msg);
        loadingPopup.showPopupWindow();
    }

    @Override
    public void hideLoading() {

        if (loadingPopup != null) {

            loadingPopup.dismiss();
        }
    }

    /**
     * 选择单词个数弹窗
     */
    private void initMorePopup() {

        if (btMorePopup == null) {

            List<String> strings = new ArrayList<>();
            strings.add("每关30个单词");
            strings.add("每关50个单词");
            strings.add("每关70个单词");
            strings.add("每关90个单词");
            strings.add("每关100个单词");

            btMorePopup = new BTMorePopup(getContext());
            btMorePopup.initOperation(strings);
            btMorePopup.setCallback(new BTMorePopup.Callback() {
                @Override
                public void getString(int position) {

                    if (position == 0) {

                        sp.edit().putInt(Constant.SP_KEY_WORD_NUM, 30).apply();
                        wordNum = 30;
                        initData();
                    } else if (position == 1) {

                        sp.edit().putInt(Constant.SP_KEY_WORD_NUM, 50).apply();
                        wordNum = 50;
                        initData();
                    } else if (position == 2) {

                        sp.edit().putInt(Constant.SP_KEY_WORD_NUM, 70).apply();
                        wordNum = 70;
                        initData();
                    } else if (position == 3) {

                        sp.edit().putInt(Constant.SP_KEY_WORD_NUM, 90).apply();
                        wordNum = 90;
                        initData();
                    } else {

                        sp.edit().putInt(Constant.SP_KEY_WORD_NUM, 100).apply();
                        wordNum = 100;
                        initData();
                    }
                    breakThroughAdapter.setWordNum(wordNum);
                    btMorePopup.dismiss();
                }
            });
        }
        btMorePopup.showPopupWindow(fragmentBreakThroughBinding.toolbar.toolbarIbRight2);
    }


    @Override
    public void toast(String msg) {

        Toast.makeText(MyApplication.getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getJpWordSentenceAllComplete(JpWordSentenceBean jpWordSentenceBean) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                List<JpWord> jpWordList = jpWordSentenceBean.getData();
                for (int i = 0; i < jpWordList.size(); i++) {

                    JpWord word = jpWordList.get(i);
                    List<JpWord> words = LitePal.where("wordid = ?", word.getId() + "").find(JpWord.class);
                    if (words.size() == 0) {

                        Message message = handler.obtainMessage();
                        message.obj = word;
                        message.what = 2;
                        handler.sendMessage(message);
                    } else {

                        Message message = handler.obtainMessage();
                        message.obj = word;
                        message.what = 3;
                        message.arg1 = word.getId();
                        handler.sendMessage(message);
//                        word.updateAll("wordid = ?", word.getId() + "");
                    }
                    progress = (int) (100.0 * (i + 1) / jpWordSentenceBean.getSize());
                    Log.d("qweewq", progress + "");


                    if (loadingPopup != null) {//过滤重复的
                        if (loadingPopup.getProgress() != progress) {

                            loadingPopup.setProgress(progress);
                            handler.sendEmptyMessage(1);
                        }
                    }
                }
            }
        }).start();

    }

    Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {

            if (msg.what == 1) {

                if (progress == 100) {
                    initData();
                    hideLoading();
                } else {

                    loadingPopup.setContent("第一次加载较慢，\n请不要退出\n更新进度：" + progress + "%");
//                        showLoading("第一次加载较慢，\n请不要退出\n更新进度：" + progress + "%");
                }
            } else if (msg.what == 2) {

                JpWord word = (JpWord) msg.obj;
                word.save();
            } else {

                JpWord word = (JpWord) msg.obj;
                int id = msg.arg1;
                word.updateAll("wordid = ?", id + "");
            }
            return false;
        }
    });
}