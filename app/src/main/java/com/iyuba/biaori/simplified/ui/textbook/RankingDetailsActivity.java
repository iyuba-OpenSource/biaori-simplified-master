package com.iyuba.biaori.simplified.ui.textbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.iyuba.biaori.simplified.Constant;
import com.iyuba.biaori.simplified.MyApplication;
import com.iyuba.biaori.simplified.R;
import com.iyuba.biaori.simplified.adapter.textbook.RankingDetailsAdapter;
import com.iyuba.biaori.simplified.databinding.ActivityRankingDetailsBinding;
import com.iyuba.biaori.simplified.db.Book;
import com.iyuba.biaori.simplified.db.Sentence;
import com.iyuba.biaori.simplified.entity.MediaPauseEventbus;
import com.iyuba.biaori.simplified.model.bean.textbook.MergeBean;
import com.iyuba.biaori.simplified.model.textbook.RankingDetailsBean;
import com.iyuba.biaori.simplified.presenter.textbook.RankingDetailsPresenter;
import com.iyuba.biaori.simplified.ui.BaseActivity;
import com.iyuba.biaori.simplified.util.DateUtil;
import com.iyuba.biaori.simplified.view.textbook.RankingDetailsContract;
import com.iyuba.module.toolbox.MD5;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 排行榜-用户音频信息
 */
public class RankingDetailsActivity extends BaseActivity<RankingDetailsContract.RankingDetailsView, RankingDetailsContract.RankingDetailsPresenter>
        implements RankingDetailsContract.RankingDetailsView {

    private ActivityRankingDetailsBinding binding;

    private RankingDetailsAdapter rankingDetailsAdapter;

    private String bookName;

    private int source;

    private int lessonid;

    private int uid = 0;

    private String portrait;

    private String name;

    private MediaPlayer mediaPlayer;

    public static void startActivity(Activity activity, int source, int lessonid, int uid, String portrait, String name) {

        Intent intent = new Intent(activity, RankingDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("SOURCE", source);
        bundle.putInt("LESSONID", lessonid);
        bundle.putInt("UID", uid);
        bundle.putString("PORTRAIT", portrait);
        bundle.putString("NAME", name);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    private void getBundle() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            source = bundle.getInt("SOURCE");
            lessonid = bundle.getInt("LESSONID");
            uid = bundle.getInt("UID");
            portrait = bundle.getString("PORTRAIT");
            name = bundle.getString("NAME");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getBundle();

        binding.toolbar.toolbarIvTitle.setText(name + "的评测");
        binding.toolbar.toolbarIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        //获取课本的name
        List<Book> bookList = LitePal.where("source = ?", source + "").find(Book.class);
        if (bookList.size() != 0) {

            bookName = bookList.get(0).getName();
        }


        rankingDetailsAdapter = new RankingDetailsAdapter(R.layout.item_ranking_details, new ArrayList<>());
        binding.rdRv.setLayoutManager(new LinearLayoutManager(this));
        binding.rdRv.setAdapter(rankingDetailsAdapter);

        rankingDetailsAdapter.setName(name);
        rankingDetailsAdapter.setPortrait(portrait);

        rankingDetailsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                RankingDetailsBean.DataDTO dataDTO = rankingDetailsAdapter.getItem(position);
                initMediaPlayer(dataDTO.getShuoShuo());
            }
        });

        String sign = uid + "getWorksByUserId" + DateUtil.getCurDate();
        presenter.getWorksByUserId(uid, bookName, lessonid, "2,4", MD5.getMD5ofStr(sign));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {

            mediaPlayer.release();
        }
    }

    @Override
    public View initLayout() {

        binding = ActivityRankingDetailsBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public RankingDetailsContract.RankingDetailsPresenter initPresenter() {
        return new RankingDetailsPresenter();
    }

    @Override
    public void showLoading(String msg) {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void toast(String msg) {

        Toast.makeText(MyApplication.getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getMergeData(RankingDetailsBean rankingDetailsBean) {

        rankingDetailsAdapter.setNewData(rankingDetailsBean.getData());
    }


    /**
     * 播放句子
     */
    private void initMediaPlayer(String path) {

        if (mediaPlayer == null) {

            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {

                    EventBus.getDefault().post(new MediaPauseEventbus());
                    mediaPlayer.start();
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {

                }
            });
        }

        mediaPlayer.reset();
        try {
            //http://static2.iyuba.cn/Japan/jp3/talk/001/001_01.mp3

            String url = Constant.URL_IUSERSPEECH + "/voa/" + path;
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}