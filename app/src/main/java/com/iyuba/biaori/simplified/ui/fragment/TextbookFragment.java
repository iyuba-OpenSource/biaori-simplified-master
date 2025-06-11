package com.iyuba.biaori.simplified.ui.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.iyuba.biaori.simplified.Constant;
import com.iyuba.biaori.simplified.MainActivity;
import com.iyuba.biaori.simplified.MyApplication;
import com.iyuba.biaori.simplified.R;
import com.iyuba.biaori.simplified.adapter.TextbookAdapter;
import com.iyuba.biaori.simplified.databinding.FragmentTextbookBinding;
import com.iyuba.biaori.simplified.db.JpLesson;
import com.iyuba.biaori.simplified.entity.BookEventBus;
import com.iyuba.biaori.simplified.entity.MediaPauseEventbus;
import com.iyuba.biaori.simplified.model.bean.AdEntryBean;
import com.iyuba.biaori.simplified.model.bean.JpLessonBean;
import com.iyuba.biaori.simplified.model.bean.textbook.CollectBean;
import com.iyuba.biaori.simplified.presenter.TextbookPresenter;
import com.iyuba.biaori.simplified.service.MediaService;
import com.iyuba.biaori.simplified.ui.BaseFragment;
import com.iyuba.biaori.simplified.ui.textbook.BookListActivity;
import com.iyuba.biaori.simplified.ui.textbook.TextbookDetailsActivity;
import com.iyuba.biaori.simplified.ui.video.HeadlineVideoActivity;
import com.iyuba.biaori.simplified.util.LineItemDecoration;
import com.iyuba.biaori.simplified.util.MD5Util;
import com.iyuba.biaori.simplified.util.TimeUtils;
import com.iyuba.biaori.simplified.view.TextbookContract;
import com.iyuba.headlinelibrary.data.local.HeadlineInfoHelper;
import com.iyuba.headlinelibrary.ui.common.ss.StreamMixStrategy;
import com.iyuba.headlinelibrary.ui.common.ss.StreamNonVipStrategy;
import com.iyuba.headlinelibrary.ui.common.ss.StreamStrategy;
import com.iyuba.headlinelibrary.ui.common.ss.StreamVipStrategy;
import com.iyuba.sdk.data.iyu.IyuNative;
import com.iyuba.sdk.data.ydsdk.YDSDKTemplateNative;
import com.iyuba.sdk.data.youdao.YDNative;
import com.iyuba.sdk.mixnative.MixAdRenderer;
import com.iyuba.sdk.mixnative.MixNative;
import com.iyuba.sdk.mixnative.MixViewBinder;
import com.iyuba.sdk.mixnative.PositionLoadWay;
import com.iyuba.sdk.mixnative.StreamType;
import com.iyuba.sdk.nativeads.NativeAdPositioning;
import com.iyuba.sdk.nativeads.NativeEventListener;
import com.iyuba.sdk.nativeads.NativeRecyclerAdapter;
import com.iyuba.sdk.nativeads.NativeResponse;
import com.youdao.sdk.nativeads.RequestParameters;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * 教材fragment
 */
public class TextbookFragment extends BaseFragment<TextbookContract.TextbookView, TextbookContract.TextbookPresenter>
        implements TextbookContract.TextbookView {


    private FragmentTextbookBinding fragmentTextbookBinding;
    private TextbookAdapter textbookAdapter;

    private View emptyView;

    private TextView empty_tv_content;

    private LineItemDecoration lineItemDecoration;

    private DecimalFormat decimalFormat;

    /**
     * mediaservice的binder
     */
    private MediaService.MusicController musicController;

    private int mStrategyCode = -1;

    /**
     * 广告类型
     */
    private final int[] mStreamTypes = new int[3];

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            musicController = (MediaService.MusicController) service;
            JpLesson jpLesson = musicController.getCurData();
            fragmentTextbookBinding.textbookTvTitle.setText(jpLesson.getLesson());

            fragmentTextbookBinding.textbookLlSound.setVisibility(View.VISIBLE);
            fragmentTextbookBinding.textbookIvSound.setImageResource(R.mipmap.icon_home_pause);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    public TextbookFragment() {

    }

    public static TextbookFragment newInstance() {
        TextbookFragment fragment = new TextbookFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);

        emptyView = LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.empty_view, null);
        empty_tv_content = emptyView.findViewById(R.id.empty_tv_content);
        empty_tv_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                presenter.getJpLesson(Constant.bookDataDTO.getSource() + "");
            }
        });

        decimalFormat = new DecimalFormat("#000");


        lineItemDecoration = new LineItemDecoration(getActivity(), LinearLayoutManager.VERTICAL);
        lineItemDecoration.setDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.decoration_10dp, null));
        textbookAdapter = new TextbookAdapter(R.layout.item_textbook, new ArrayList<>());
        textbookAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                TextbookAdapter.TextbookViewHolder textbookViewHolder = (TextbookAdapter.TextbookViewHolder) fragmentTextbookBinding.textbookRvList.getChildViewHolder(view);
                JpLesson jpLesson = textbookViewHolder.getJpLesson();

                int pos = 0;
                for (int i = 0; i < textbookAdapter.getData().size(); i++) {

                    JpLesson jl = textbookAdapter.getItem(i);
                    if (jl == jpLesson) {

                        pos = i;
                        break;
                    }
                }
                TextbookDetailsActivity.startActivity(requireActivity(), pos, textbookAdapter.getData());
            }
        });
        //绑定service
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MediaService.FLAG_MEDIA_PLAY);
        intentFilter.addAction(MediaService.FLAG_MEDIA_BROADCAST);
        intentFilter.addAction(MediaService.FLAG_SWITCH_DATA);
        MyApplication.getContext().registerReceiver(new MusicBroadcastReceiver(), intentFilter);
    }


    /**
     * 绑定数据
     */
    private void bindServer() {

        Intent intent = new Intent(MyApplication.getContext(), MediaService.class);
        MyApplication.getContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 获取后台播放的状态来更新当前界面的播放器
     */
    private void updateMediaStatus() {

        if (musicController != null) {

            fragmentTextbookBinding.textbookTvTitle.setText(musicController.getCurData().getLesson());
            if (musicController.isPlaying()) {//判断是否正在播放来设置按钮的图标

                fragmentTextbookBinding.textbookIvSound.setImageResource(R.mipmap.icon_home_pause);
            } else {

                fragmentTextbookBinding.textbookIvSound.setImageResource(R.mipmap.icon_home_play);
            }
            //模式更新
            int mode = musicController.getMode();
            if (mode == MediaService.MODE_RANDOM) {

                fragmentTextbookBinding.textbookIvPlaytype.setImageResource(R.mipmap.icon_home_random);
            } else if (mode == MediaService.MODE_LIST) {

                fragmentTextbookBinding.textbookIvPlaytype.setImageResource(R.mipmap.icon_home_list);
            } else {

                fragmentTextbookBinding.textbookIvPlaytype.setImageResource(R.mipmap.icon_home_single);
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        updateMediaStatus();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BookEventBus bookEventBus) {

        fragmentTextbookBinding.toolbar.toolbarIvTitle.setText(Constant.bookDataDTO.getBook());
        presenter.getJpLesson(Constant.bookDataDTO.getSource() + "");
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        initOperation();
        presenter.getJpLesson(Constant.bookDataDTO.getSource() + "");

    }


    private void initOperation() {

        fragmentTextbookBinding.toolbar.toolbarIvBack.setVisibility(View.GONE);
        fragmentTextbookBinding.toolbar.toolbarIvTitle.setText(Constant.bookDataDTO.getBook());
        fragmentTextbookBinding.toolbar.toolbarIvRight.setVisibility(View.VISIBLE);
        fragmentTextbookBinding.toolbar.toolbarIvRight.setImageResource(R.mipmap.ic_book);
        fragmentTextbookBinding.toolbar.toolbarIvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), BookListActivity.class));
            }
        });
        //列表
        if (lineItemDecoration != null && fragmentTextbookBinding.textbookRvList.getItemDecorationCount() == 0) {

            fragmentTextbookBinding.textbookRvList.addItemDecoration(lineItemDecoration);
        }
        fragmentTextbookBinding
                .textbookRvList
                .setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        if (textbookAdapter != null && fragmentTextbookBinding.textbookRvList.getAdapter() == null) {

            fragmentTextbookBinding.textbookRvList.setAdapter(textbookAdapter);
        }

        //播放器
        fragmentTextbookBinding.textbookLlSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (musicController != null) {

                    TextbookDetailsActivity.startActivity(getActivity(), musicController.getDataPosition(), textbookAdapter.getData());
                }
            }
        });
        fragmentTextbookBinding.textbookIvSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (musicController == null) {

                    return;
                }
                if (musicController.isPlaying()) {

                    musicController.pause();
                    fragmentTextbookBinding.textbookIvSound.setImageResource(R.mipmap.icon_home_play);
                } else {

                    musicController.start();
                    fragmentTextbookBinding.textbookIvSound.setImageResource(R.mipmap.icon_home_pause);
                }
            }
        });

        //播放模式
        fragmentTextbookBinding.textbookIvPlaytype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (musicController == null) {

                    return;
                }

                if (musicController.getMode() == 1) {

                    musicController.setPlayMode(2);
                    fragmentTextbookBinding.textbookIvPlaytype.setImageResource(R.mipmap.icon_home_list);
                    Toast.makeText(MyApplication.getContext(), "列表播放", Toast.LENGTH_SHORT).show();
                } else if (musicController.getMode() == 2) {

                    musicController.setPlayMode(3);
                    fragmentTextbookBinding.textbookIvPlaytype.setImageResource(R.mipmap.icon_home_single);
                    Toast.makeText(MyApplication.getContext(), "单曲播放", Toast.LENGTH_SHORT).show();
                } else {

                    musicController.setPlayMode(1);
                    fragmentTextbookBinding.textbookIvPlaytype.setImageResource(R.mipmap.icon_home_random);
                    Toast.makeText(MyApplication.getContext(), "随机播放", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //闯关
        fragmentTextbookBinding.textbookLlBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getActivity() != null) {

                    MainActivity activity = (MainActivity) getActivity();
                    activity.jumpPosition(1);
                }
            }
        });
        //视频
        fragmentTextbookBinding.textbookLlVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EventBus.getDefault().post(new MediaPauseEventbus());
//                HeadlineInfoHelper.init(MyApplication.getContext());
                startActivity(new Intent(requireActivity(), HeadlineVideoActivity.class));
            }
        });
        //配音
        fragmentTextbookBinding.textbookLlDubbing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getActivity() != null) {

                    MainActivity activity = (MainActivity) getActivity();
                    activity.jumpPosition(2);
                }
            }
        });
        //微课
        fragmentTextbookBinding.textbookLlImooc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getActivity() != null) {

                    MainActivity activity = (MainActivity) getActivity();
                    activity.jumpPosition(3);
                }
            }
        });
    }

    @Override
    protected View initLayout(LayoutInflater inflater, ViewGroup container) {
        fragmentTextbookBinding = FragmentTextbookBinding.inflate(inflater, container, false);
        return fragmentTextbookBinding.getRoot();
    }

    @Override
    protected TextbookContract.TextbookPresenter initPresenter() {
        return new TextbookPresenter();
    }

    @Override
    public void showLoading(String msg) {

        fragmentTextbookBinding.textbookPb.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {

        fragmentTextbookBinding.textbookPb.setVisibility(View.GONE);
    }

    @Override
    public void toast(String msg) {

        Toast.makeText(MyApplication.getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getJpLessonComplete(JpLessonBean jpLessonBean) {

        hideLoading();

        //获取收藏的数据到本地
//        if (Constant.userinfo != null) {
//
//            List<JpLesson> jpLessonList = LitePal.where("collect = 1").find(JpLesson.class);
//            if (jpLessonList.size() == 0) {
//
//                String signStr = MD5Util.MD5("iyuba" + Constant.userinfo.getUid() + Constant.TOPIC + Constant.APPID + TimeUtils.getDays());
//                presenter.getCollect(Constant.userinfo.getUid(), signStr, "all", Constant.APPID, 0);
//            }
//        }

        if (textbookAdapter.getEmptyView() == null) {

            textbookAdapter.setEmptyView(emptyView);
        }

        if (jpLessonBean == null) {

            empty_tv_content.setText("请求超时，点击重试");
        } else {

            if (jpLessonBean.getData() == null || jpLessonBean.getData().size() == 0) {

                empty_tv_content.setText("没有数据，点击重试");
            } else {

                List<JpLesson> jpLessonList = jpLessonBean.getData();
                //本地存储数据
                for (int i = 0, size = jpLessonList.size(); i < size; i++) {

                    JpLesson l = jpLessonList.get(i);
                    List<JpLesson> lessons = LitePal
                            .where("lessonid = ? and source = ?", Integer.parseInt(l.getLessonID()) + "", l.getSource())
                            .find(JpLesson.class);
                    if (lessons.size() == 0) {//没有数据

                        l.setToDefault("collect");
                        l.setLessonID(Integer.parseInt(l.getLessonID()) + "");
                        l.save();

                    } else {

                        JpLesson sp = lessons.get(0);
                        sp.setLesson(l.getLesson());
                        sp.setLessonCH(l.getLessonCH());
                        sp.setSource(l.getSource());
                        sp.setSentenceNum(l.getSentenceNum());
                        sp.setWordNum(l.getWordNum());
                        sp.setVersion(l.getVersion());
                        l.setTestNumber(sp.getTestNumber());
                        sp.updateAll("lessonid = ? and source = ?", Integer.parseInt(l.getLessonID()) + "", l.getSource());
                    }
                }

                textbookAdapter.setNewData(jpLessonList);
            }
        }

        //广告
        if (Constant.userinfo == null || !Constant.userinfo.isVip()) {

            String uid = "0";
            if (Constant.userinfo != null) {

                uid = Constant.userinfo.getUid() + "";
            }

            presenter.getAdEntryAll(Constant.APPID + "", 2, uid);
        }
    }

    /**
     * 获取收藏的文章准备
     * 2022 10 19  此获取收藏不能使用
     *
     * @param collectBean
     */
    @Override
    public void getCollectComplete(CollectBean collectBean) {

        List<CollectBean.DataDTO> dtoList = collectBean.getData();

        //获取收藏的数据
        Map<String, CollectBean.DataDTO> dtoMap = new HashMap<>();
        for (int i = 0; i < dtoList.size(); i++) {

            CollectBean.DataDTO dataDTO = dtoList.get(i);
            if (dataDTO.getTopic().equals(Constant.TOPIC)) {//是不是此app 的

                if (!dtoMap.containsKey(dataDTO.getVoaid())) {//没有则存储

                    dtoMap.put(dataDTO.getVoaid(), dataDTO);
                }
            }
        }

        Iterator<String> iterator = dtoMap.keySet().iterator();

        while (iterator.hasNext()) {

            CollectBean.DataDTO dataDTO = dtoMap.get(iterator.next());
            //todo  source
            // TODO: 2022/10/19  
            //是否有此数据，没有则添加，有则更新
            List<JpLesson> jpLessons = LitePal.where("lessonID = ?", dataDTO.getVoaid()).find(JpLesson.class);
            if (jpLessons.size() == 0) {

                JpLesson jpLesson = new JpLesson();
                jpLesson.setLessonID(dataDTO.getVoaid());
                jpLesson.setCollect(1);
                jpLesson.save();
            } else {

                JpLesson lesson = jpLessons.get(0);
                lesson.setCollect(1);
                lesson.updateAll("lessonID = ?", dataDTO.getVoaid());
            }
        }

    }

    @Override
    public void getAdEntryAllComplete(AdEntryBean adEntryBean) {

        if (adEntryBean.getResult().equals("-1")) {

//            initYouDao();
        } else {

            setAdAdapter(adEntryBean.getData());
        }
    }


    private void setAdAdapter(AdEntryBean.DataDTO dataBean) {

        Activity activity = requireActivity();
        if (activity == null) {

            return;
        }

        OkHttpClient mClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();

        EnumSet<RequestParameters.NativeAdAsset> desiredAssets = EnumSet.of(
                RequestParameters.NativeAdAsset.TITLE,
                RequestParameters.NativeAdAsset.TEXT,
                RequestParameters.NativeAdAsset.ICON_IMAGE,
                RequestParameters.NativeAdAsset.MAIN_IMAGE,
                RequestParameters.NativeAdAsset.CALL_TO_ACTION_TEXT);
        RequestParameters requestParameters = new RequestParameters.RequestParametersBuilder()
                .location(null)
                .keywords(null)
                .desiredAssets(desiredAssets)
                .build();
        YDNative ydNative = new YDNative(activity, "edbd2c39ce470cd72472c402cccfb586", requestParameters);

        IyuNative iyuNative = new IyuNative(activity, Constant.ADAPPID + "", mClient);

//        YDSDKTemplateNative csjTemplateNative = new YDSDKTemplateNative(activity, BuildConfig.TEMPLATE_SCREEN_AD_KEY_CSJ);
//        YDSDKTemplateNative ylhTemplateNative = new YDSDKTemplateNative(activity, BuildConfig.TEMPLATE_SCREEN_AD_KEY_YLH);
//        YDSDKTemplateNative ksTemplateNative = new YDSDKTemplateNative(requireActivity(), BuildConfig.TEMPLATE_SCREEN_AD_KEY_KS);
//        YDSDKTemplateNative bdTemplateNative = new YDSDKTemplateNative(activity, BuildConfig.TEMPLATE_SCREEN_AD_KEY_BD);

        //添加key
        HashMap<Integer, YDSDKTemplateNative> ydsdkMap = new HashMap<>();
//        ydsdkMap.put(StreamType.GDT, ylhTemplateNative);
//        ydsdkMap.put(StreamType.KS, ksTemplateNative);
//        ydsdkMap.put(StreamType.BAIDU, bdTemplateNative);
//        ydsdkMap.put(StreamType.TT, csjTemplateNative);


        MixNative mixNative = new MixNative(ydNative, iyuNative, ydsdkMap);
        PositionLoadWay loadWay = new PositionLoadWay();
        loadWay.setStreamSource(new int[]{
                Integer.parseInt(dataBean.getFirstLevel()),
                Integer.parseInt(dataBean.getSecondLevel()),
                Integer.parseInt(dataBean.getThirdLevel())});
        mixNative.setLoadWay(loadWay);
//        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        //设置穿山甲广告不显示的问题
//        mixNative.setWidthHeight(displayMetrics.widthPixels, DensityUtil.dp2px(MyApplication.getInstance(), 130));


        int startPosition = 3;
        int positionInterval = 5;
        NativeAdPositioning.ClientPositioning positioning = new NativeAdPositioning.ClientPositioning();
        positioning.addFixedPosition(startPosition);
        positioning.enableRepeatingPositions(positionInterval);
        NativeRecyclerAdapter mAdAdapter = new NativeRecyclerAdapter(activity, textbookAdapter, positioning);
        mAdAdapter.setNativeEventListener(new NativeEventListener() {
            @Override
            public void onNativeImpression(View view, NativeResponse nativeResponse) {

            }

            @Override
            public void onNativeClick(View view, NativeResponse nativeResponse) {

            }
        });
        mAdAdapter.setAdSource(mixNative);

        mixNative.setYDSDKTemplateNativeClosedListener(new MixNative.YDSDKTemplateNativeClosedListener() {
            @Override
            public void onClosed(View view) {
                View itemView = (View) ((View) view.getParent()).getParent();
                RecyclerView.ViewHolder viewHolder = fragmentTextbookBinding.textbookRvList.getChildViewHolder(itemView);
                int position = viewHolder.getBindingAdapterPosition();
                mAdAdapter.removeAdsWithAdjustedPosition(position);
            }
        });

        MixViewBinder mixViewBinder = new MixViewBinder.Builder(R.layout.item_ad_mix)
                .templateContainerId(R.id.mix_fl_ad)
                .nativeContainerId(R.id.headline_ll_item)
                .nativeImageId(R.id.native_main_image)
                .nativeTitleId(R.id.native_title)
                .build();
        MixAdRenderer mixAdRenderer = new MixAdRenderer(mixViewBinder);
        mAdAdapter.registerAdRenderer(mixAdRenderer);
        fragmentTextbookBinding.textbookRvList.setAdapter(mAdAdapter);
        mAdAdapter.loadAds();
    }


    /**
     * 停止media
     */
    public void pauseMedia() {

        if (musicController != null) {

            if (musicController.isPlaying()) {

                musicController.pause();
                requireActivity().sendBroadcast(new Intent(MediaService.FLAG_MEDIA_BROADCAST));
            }
        }
    }


    /**
     * 用来接收通知栏状态的改变
     */
    class MusicBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (action.equals(MediaService.FLAG_MEDIA_BROADCAST)) {//通知点击事件间接相关，用来更新播放按钮

                if (musicController != null) {

                    if (musicController.isPlaying()) {

                        fragmentTextbookBinding.textbookIvSound.setImageResource(R.mipmap.icon_home_pause);
                    } else {

                        fragmentTextbookBinding.textbookIvSound.setImageResource(R.mipmap.icon_home_play);
                    }
                }
            } else if (action.equals(MediaService.FLAG_MEDIA_PLAY)) {//绑定播放器获取musicController

                if (musicController == null) {
                    bindServer();
                }
            } else if (action.equals(MediaService.FLAG_SWITCH_DATA)) {//切换数据会触发

                if (musicController == null) {

                    return;
                }

                JpLesson jpLesson = musicController.getCurData();
                fragmentTextbookBinding.textbookTvTitle.setText(jpLesson.getLesson());
                if (musicController.isPlaying()) {//判断是否正在播放来设置按钮的图标

                    fragmentTextbookBinding.textbookIvSound.setImageResource(R.mipmap.icon_home_pause);
                } else {

                    fragmentTextbookBinding.textbookIvSound.setImageResource(R.mipmap.icon_home_play);
                }
            }
        }
    }
}