package com.iyuba.biaori.simplified;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.iyuba.biaori.simplified.adapter.MyFragmentAdapter;
import com.iyuba.biaori.simplified.databinding.ActivityMainBinding;
import com.iyuba.biaori.simplified.entity.BookEventBus;
import com.iyuba.biaori.simplified.entity.MediaPauseEventbus;
import com.iyuba.biaori.simplified.entity.RewardEventbus;
import com.iyuba.biaori.simplified.model.bean.BookListBean;
import com.iyuba.biaori.simplified.ui.AlertActivity;
import com.iyuba.biaori.simplified.ui.fragment.BreakThroughFragment;
import com.iyuba.biaori.simplified.ui.fragment.DubbingFragment;
import com.iyuba.biaori.simplified.ui.fragment.MeFragment;
import com.iyuba.biaori.simplified.ui.fragment.TextbookFragment;
import com.iyuba.biaori.simplified.ui.vip.VipActivity;
import com.iyuba.biaori.simplified.ui.welcome.MyWebActivity;
import com.iyuba.biaori.simplified.util.popup.PrivacyPopup;
import com.iyuba.headlinelibrary.HeadlineType;
import com.iyuba.headlinelibrary.event.HeadlinePlayEvent;
import com.iyuba.headlinelibrary.ui.content.AudioContentActivity;
import com.iyuba.headlinelibrary.ui.content.AudioContentActivityNew;
import com.iyuba.headlinelibrary.ui.content.TextContentActivity;
import com.iyuba.headlinelibrary.ui.content.VideoContentActivityNew;
import com.iyuba.headlinelibrary.ui.video.VideoMiniContentActivity;
import com.iyuba.imooclib.data.local.IMoocDBManager;
import com.iyuba.imooclib.event.ImoocBuyVIPEvent;
import com.iyuba.imooclib.event.ImoocPlayEvent;
import com.iyuba.imooclib.ui.mobclass.MobClassFragment;
import com.iyuba.module.dl.BasicDLPart;
import com.iyuba.module.dl.DLItemEvent;
import com.iyuba.module.favor.data.model.BasicFavorPart;
import com.iyuba.module.favor.event.FavorItemEvent;
import com.iyuba.module.movies.ui.series.SeriesActivity;
import com.iyuba.module.privacy.PrivacyInfoHelper;
import com.iyuba.share.ShareExecutor;
import com.iyuba.share.mob.MobShareExecutor;
import com.mob.MobSDK;
import com.youdao.sdk.common.YoudaoSDK;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import personal.iyuba.personalhomelibrary.event.ArtDataSkipEvent;
import personal.iyuba.personalhomelibrary.event.HomePauseEvent;
import personal.iyuba.personalhomelibrary.event.PersonalSkipEvent;


/**
 * 主界面
 */
public class MainActivity extends AppCompatActivity {


    private ActivityMainBinding activityMainBinding;

    private MyFragmentAdapter myFragmentAdapter;
    private List<Fragment> fragmentList;

    private String[] navTitle = {"教材", "闯关", "配音秀", "微课", "我的"};
    //    private String[] navTitle = {"教材", "闯关", "我的"};
    private int[] navImg = {R.mipmap.nav_textbook, R.mipmap.nav_bt, R.mipmap.nav_dubbing, R.mipmap.nav_imooc, R.mipmap.nav_personal};

    private int[] cNavImg = {R.mipmap.nav_textbook_c, R.mipmap.nav_bt_c, R.mipmap.nav_dubbing_c, R.mipmap.nav_imooc_c, R.mipmap.nav_personal_c};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        EventBus.getDefault().register(this);


        initFragment();

        //设置缓存数，如果不设置缓存数则微课崩溃
        activityMainBinding.mainVp.setOffscreenPageLimit(3);
        myFragmentAdapter = new MyFragmentAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                fragmentList);
        activityMainBinding.mainVp.setAdapter(myFragmentAdapter);
        activityMainBinding.mainVp.setOffscreenPageLimit(5);

        activityMainBinding.mainTl.setupWithViewPager(activityMainBinding.mainVp);

        initNav();
    }

    /**
     * 微课视频播放事件
     *
     * @param imoocPlayEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ImoocPlayEvent imoocPlayEvent) {

        EventBus.getDefault().post(new MediaPauseEventbus());
    }

    /**
     * 口语圈
     *
     * @param homePauseEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HomePauseEvent homePauseEvent) {

        EventBus.getDefault().post(new MediaPauseEventbus());
    }

    /**
     * 视频播放
     *
     * @param headlinePlayEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HeadlinePlayEvent headlinePlayEvent) {

        EventBus.getDefault().post(new MediaPauseEventbus());
    }

    /**
     * 奖励弹窗
     *
     * @param rewardEventbus
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RewardEventbus rewardEventbus) {

        AlertActivity.startActivity(MainActivity.this, rewardEventbus.getReward());
    }

    /**
     * 从口语圈点击后进入的页面
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ArtDataSkipEvent event) {
        if (event.voa != null) {
            BasicFavorPart part = new BasicFavorPart();
            part.setType(event.voa.categoryString);
            part.setCategoryName(event.voa.categoryString);
            part.setTitle(event.voa.title);
            part.setTitleCn(event.voa.title_cn);
            part.setPic(event.voa.pic);
            part.setId(event.voa.voaid + "");
            part.setSound(event.voa.sound + "");
            jumpToCorrectFavorActivityByCate(this, part);
        }
    }


    public void jumpToCorrectFavorActivityByCate(Context context, BasicFavorPart item) {
        switch (item.getType()) {
            case "news":
                Intent intent1 = TextContentActivity.buildIntent(context, item.getId(), item.getTitle(), item.getTitleCn(), item.getType(),
                        item.getCategoryName(), item.getCreateTime(), item.getPic(), item.getSource());
                startActivity(intent1);
                break;
            case "headnews":

                Intent intent2 = TextContentActivity.buildIntent(context, item.getId(), item.getTitle(), item.getTitleCn(), "news",
                        item.getCategoryName(), item.getCreateTime(), item.getPic(), item.getSource());
                startActivity(intent2);
                break;
            case "voa":
            case "csvoa":
            case "bbc":
            case "song":
                Intent intent3 = AudioContentActivityNew.buildIntent(context, item.getCategoryName(), item.getTitle(),
                        item.getTitleCn(), item.getPic(), item.getType(), item.getId(), item.getSound());
                startActivity(intent3);
                break;
            case "voavideo":
            case "meiyu":
            case "ted":
            case "japanvideos":
            case "bbcwordvideo":
            case "topvideos":

                Intent intent = VideoContentActivityNew.buildIntent(context, item.getCategoryName(), item.getTitle(),
                        item.getTitleCn(), item.getPic(), item.getType(), item.getId(), item.getSound());
                startActivity(intent);
                break;
            case "series":
//                startActivity(new Intent(context, OneMvSerisesView.class)
//                        .putExtra("serisesid", item.getSeriseId())
//                        .putExtra("voaid", item.getId()));
                break;
            case "smallvideo":
                startActivity(VideoMiniContentActivity.buildIntentForOne(this, item.getId(), 0, 1, 1));
                break;
            default:
                break;
        }
    }

    /**
     * 教材切换后触发,用来更新breakThroughFragment的level值
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void toEvent(BookEventBus bookEventBus) {

        BreakThroughFragment breakThroughFragment = null;
        for (int i = 0; i < fragmentList.size(); i++) {

            Fragment fragment = fragmentList.get(i);
            if (fragment instanceof BreakThroughFragment) {

                breakThroughFragment = (BreakThroughFragment) fragment;
            }
        }
        if (breakThroughFragment != null) {

            Bundle bundle = breakThroughFragment.getArguments();
            if (bundle != null) {

                bundle.putInt("LEVEL", Integer.parseInt(Constant.bookDataDTO.getLevel()));
            }
            breakThroughFragment.updateBundle();
        }
    }

    /**
     * 微课点击购买会员
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ImoocBuyVIPEvent event) {

        VipActivity.startActivity(this, 10);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DLItemEvent event) {
        BasicDLPart item = event.items.get(event.position);
        jumpToCorrectDLActivityByCate(this, item);
    }


    /**
     * 暂停后台播放(在某些手机上，音频焦点不能)
     *
     * @param mediaPauseEventbus
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MediaPauseEventbus mediaPauseEventbus) {

        TextbookFragment textbookFragment = null;
        for (int i = 0; i < fragmentList.size(); i++) {

            Fragment fragment = fragmentList.get(i);
            if (fragment instanceof TextbookFragment) {

                textbookFragment = (TextbookFragment) fragment;
                break;
            }
        }
        if (textbookFragment != null) {

            textbookFragment.pauseMedia();
        }
    }

    /**
     * 共通模块  我的收藏 点击事件
     *
     * @param fEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FavorItemEvent fEvent) {
        //收藏页面点击
        BasicFavorPart fPart = fEvent.items.get(fEvent.position);
        goFavorItem(fPart);
    }


    private void goFavorItem(BasicFavorPart part) {

        switch (part.getType()) {
            case HeadlineType.NEWS:
                startActivity(TextContentActivity.buildIntent(this, part.getId(), part.getTitle(), part.getTitleCn(), part.getType()
                        , part.getCategoryName(), part.getCreateTime(), part.getPic(), part.getSource()));
                break;
            case HeadlineType.VOA:
            case HeadlineType.CSVOA:
            case HeadlineType.BBC:
                startActivity(AudioContentActivityNew.buildIntent(
                        this, part.getCategoryName(), part.getTitle(), part.getTitleCn(),
                        part.getPic(), part.getType(), part.getId(), part.getSound()));
                break;

            case HeadlineType.SONG:
                startActivity(AudioContentActivity.buildIntent(
                        this, part.getCategoryName(), part.getTitle(), part.getTitleCn(),
                        part.getPic(), part.getType(), part.getId(), part.getSound()));
                break;
            case HeadlineType.VOAVIDEO:
            case HeadlineType.MEIYU:
            case HeadlineType.TED:
            case HeadlineType.BBCWORDVIDEO:
            case HeadlineType.TOPVIDEOS:
            case HeadlineType.JAPANVIDEOS:
                startActivity(VideoContentActivityNew.buildIntent(this,
                        part.getCategoryName(), part.getTitle(), part.getTitleCn(), part.getPic(),
                        part.getType(), part.getId(), part.getSound()));
                break;
            //小视频
            case HeadlineType.SMALLVIDEO_JP:
            case HeadlineType.SMALLVIDEO:

                startActivity(VideoMiniContentActivity.buildIntentForOne(this, part.getId(), 0, 1, 1));
                break;
            case "series":
                Intent intent = SeriesActivity.buildIntent(this, part.getSeriesId(), part.getId());
                startActivity(intent);
                break;
        }
    }

    public void jumpToCorrectDLActivityByCate(Context context, BasicDLPart item) {
        switch (item.getType()) {
            case HeadlineType.VOA:
            case HeadlineType.CSVOA:
            case HeadlineType.BBC:
            case HeadlineType.SONG:
                startActivity(AudioContentActivityNew.buildIntent(context, item.getCategoryName(), item.getTitle()
                        , item.getTitleCn(), item.getPic(), item.getType(), item.getId()));
                break;
            case HeadlineType.VOAVIDEO:
            case HeadlineType.MEIYU:
            case HeadlineType.TED:
            case HeadlineType.BBCWORDVIDEO:
            case HeadlineType.JAPANVIDEOS:
            case HeadlineType.TOPVIDEOS:
            case HeadlineType.SMALLVIDEO_JP:
                startActivity(VideoContentActivityNew.buildIntent(context, item.getCategoryName(), item.getTitle(),
                        item.getTitleCn(), item.getPic(), item.getType(), item.getId(), ""));
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);
    }


/*    private Intent buildIntent(Context context, String url, String title) {
        Intent intent = new Intent(context, Web.class);
        intent.putExtra(URL_KEY, url);
        intent.putExtra(TITLE_KEY, title);
        return intent;
    }*/

    private void initNav() {

        activityMainBinding.mainTl.setTabMode(TabLayout.MODE_FIXED);
        for (int i = 0; i < navTitle.length; i++) {

            TabLayout.Tab tab = activityMainBinding.mainTl.getTabAt(i);
            View navView = LayoutInflater.from(this).inflate(R.layout.nav, null);
            tab.setCustomView(navView);
            TextView nav_name = navView.findViewById(R.id.nav_name);
            ImageView nav_iv_pic = navView.findViewById(R.id.nav_iv_pic);
            nav_name.setText(navTitle[i]);
            nav_iv_pic.setImageResource(navImg[i]);

            if (i == 0) {

                nav_name.setTextColor(Color.parseColor("#00a490"));
                nav_iv_pic.setImageResource(cNavImg[0]);
            } else {

                nav_name.setTextColor(Color.GRAY);
            }
        }
        activityMainBinding.mainTl.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                View view = tab.getCustomView();
                ImageView nav_iv_pic = view.findViewById(R.id.nav_iv_pic);
                TextView nav_name = view.findViewById(R.id.nav_name);
                nav_name.setTextColor(Color.parseColor("#00a490"));
                nav_iv_pic.setImageResource(cNavImg[tab.getPosition()]);

                if (tab.getPosition() == 3) {//微课

                    EventBus.getDefault().post(new MediaPauseEventbus());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                View view = tab.getCustomView();
                TextView nav_name = view.findViewById(R.id.nav_name);
                ImageView nav_iv_pic = view.findViewById(R.id.nav_iv_pic);
                nav_name.setTextColor(Color.GRAY);
                nav_iv_pic.setImageResource(navImg[tab.getPosition()]);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initFragment() {

        fragmentList = new ArrayList<>();
        fragmentList.add(TextbookFragment.newInstance());

        fragmentList.add(BreakThroughFragment.newInstance(Integer.parseInt(Constant.bookDataDTO.getLevel())));
        ArrayList<Integer> imoocList = new ArrayList<>();
        imoocList.add(1);//n1微课id
        imoocList.add(5);//n2微课id
        imoocList.add(6);//n3微课id

        //配音秀
        fragmentList.add(DubbingFragment.newInstance());

        Bundle bundle = MobClassFragment.buildArguments(Constant.OWERID, false, imoocList);
        MobClassFragment mobClassFragment = MobClassFragment.newInstance(bundle);
        fragmentList.add(mobClassFragment);

        fragmentList.add(MeFragment.newInstance());
    }


    /**
     * 切换到某页
     *
     * @param position
     */
    public void jumpPosition(int position) {

        if (myFragmentAdapter == null) {

            return;
        }
        activityMainBinding.mainVp.setCurrentItem(position);

    }
}