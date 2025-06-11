package com.iyuba.biaori.simplified.ui.dubbing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.analytics.AnalyticsListener;
import com.iyuba.biaori.simplified.Constant;
import com.iyuba.biaori.simplified.R;
import com.iyuba.biaori.simplified.adapter.MyFragmentAdapter2;
import com.iyuba.biaori.simplified.databinding.ActivityDubbingVideoBinding;
import com.iyuba.biaori.simplified.entity.MediaPauseEventbus;
import com.iyuba.biaori.simplified.ui.fragment.dubbing.DubbingRankFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 配音秀 播放界面
 */
public class DubbingVideoActivity extends AppCompatActivity {

    private ActivityDubbingVideoBinding activityDubbingVideoBinding;

    private String id;
    private String category;
    private String bg_sound;
    private String pic;
    private String title;

    private static String CATEGORY = "CATEGORY";
    private static String ID = "ID";
    private static String PIC = "PIC";
    private static String TITLE = "TITLE";
    //背景音频
    private static String BG_SOUND = "BG_SOUND";

    private String tab[] = {"排行"};

    private MyFragmentAdapter2 myFragmentAdapter2;

    private ExoPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDubbingVideoBinding = ActivityDubbingVideoBinding.inflate(getLayoutInflater());
        setContentView(activityDubbingVideoBinding.getRoot());

        getBundle();
        initOperation();

        String url = null;
        if (Constant.userinfo == null) {

            url = "http://static0.iyuba.cn" + "/video/voa/" + category + "/" + id + ".mp4";
        } else {

            if (Constant.userinfo.isVip()) {

                url = "http://staticvip.iyuba.cn" + "/video/voa/" + category + "/" + id + ".mp4";
            } else {

                url = "http://static0.iyuba.cn" + "/video/voa/" + category + "/" + id + ".mp4";
            }
        }
        MediaItem mediaItem = MediaItem.fromUri(url);

        player = new ExoPlayer.Builder(this).build();
        player.addAnalyticsListener(new AnalyticsListener() {
            @Override
            public void onIsPlayingChanged(EventTime eventTime, boolean isPlaying) {

                if (isPlaying) {

                    EventBus.getDefault().post(new MediaPauseEventbus());
                }
            }
        });
        activityDubbingVideoBinding.dvSpv.setPlayer(player);
        player.setMediaItem(mediaItem);
        player.prepare();
        player.play();


        initTab();
    }

    private void initOperation() {

        activityDubbingVideoBinding.dvButDubbing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DubbingActivity.startActivity(DubbingVideoActivity.this, id, category, bg_sound, pic, title);
            }
        });
    }


    private void initTab() {

        List<Fragment> fragmentList = new ArrayList<>();

        fragmentList.add(DubbingRankFragment.newInstance(id));

        myFragmentAdapter2 = new MyFragmentAdapter2(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, fragmentList, tab);
        activityDubbingVideoBinding.dvVp.setAdapter(myFragmentAdapter2);

        activityDubbingVideoBinding.dvTl.setupWithViewPager(activityDubbingVideoBinding.dvVp);
    }


    private void getBundle() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            id = bundle.getString(ID);
            category = bundle.getString(CATEGORY);
            bg_sound = bundle.getString(BG_SOUND);
            pic = bundle.getString(PIC);
            title = bundle.getString(TITLE);
        }
    }

    /**
     * @param activity
     * @param videoUrl 链接地址
     * @param id       id
     */
    public static void startActivity(Activity activity, String videoUrl, String id, String category, String bg_sound,
                                     String pic, String title) {

        Intent intent = new Intent(activity, DubbingVideoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ID, id);
        bundle.putString(CATEGORY, category);
        bundle.putString(BG_SOUND, bg_sound);
        bundle.putString(PIC, pic);
        bundle.putString(TITLE, title);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.pause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (player != null) {

            player.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (player != null) {

            player.release();
        }
    }
}