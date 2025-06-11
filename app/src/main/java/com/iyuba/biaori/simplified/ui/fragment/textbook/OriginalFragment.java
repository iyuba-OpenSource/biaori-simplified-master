package com.iyuba.biaori.simplified.ui.fragment.textbook;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.iyuba.biaori.simplified.Constant;
import com.iyuba.biaori.simplified.MyApplication;
import com.iyuba.biaori.simplified.R;
import com.iyuba.biaori.simplified.WelcomeActivity;
import com.iyuba.biaori.simplified.adapter.textbook.OriginalAdapter;
import com.iyuba.biaori.simplified.databinding.FragmentOriginalBinding;
import com.iyuba.biaori.simplified.db.Book;
import com.iyuba.biaori.simplified.db.JpLesson;
import com.iyuba.biaori.simplified.db.Sentence;
import com.iyuba.biaori.simplified.model.bean.AdEntryBean;
import com.iyuba.biaori.simplified.model.bean.textbook.JpSentenceBean;
import com.iyuba.biaori.simplified.model.bean.textbook.UCollectBean;
import com.iyuba.biaori.simplified.model.bean.textbook.UpdateCollectBean;
import com.iyuba.biaori.simplified.presenter.textbook.OriginalPresenter;
import com.iyuba.biaori.simplified.service.MediaService;
import com.iyuba.biaori.simplified.ui.BaseFragment;
import com.iyuba.biaori.simplified.ui.login.LoginActivity;
import com.iyuba.biaori.simplified.ui.login.WxLoginActivity;
import com.iyuba.biaori.simplified.ui.textbook.TextbookDetailsActivity;
import com.iyuba.biaori.simplified.util.SourceUtil;
import com.iyuba.biaori.simplified.util.popup.SpeedPopup;
import com.iyuba.biaori.simplified.view.textbook.OriginalContract;
import com.iyuba.imooclib.ui.web.Web;
import com.iyuba.module.toolbox.DensityUtil;
import com.yd.saas.base.interfaces.AdViewBannerListener;
import com.yd.saas.config.exception.YdError;
import com.yd.saas.ydsdk.YdBanner;

import org.litepal.LitePal;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;


/**
 * 原文fragment
 */
public class OriginalFragment extends BaseFragment<OriginalContract.OriginalView, OriginalContract.OriginalPresenter>
        implements OriginalContract.OriginalView, AdViewBannerListener {

    private String lessonID;
    //播放位置
    private int position;
    //课文列表
    private List<JpLesson> itemList;


    /**
     * 课本source
     */
    private int source;

    private FragmentOriginalBinding fragmentOriginalBinding;

    private OriginalAdapter originalAdapter;

    private View emptyView;

    private TextView empty_tv_content;

    /**
     * 设置播放时间、总时间等
     */
    private HandlerThread handlerThread;
    private Handler handler;

    private DecimalFormat decimalFormat;

    private SpeedPopup speedPopup;

    /**
     * 后台播放
     * MediaService的binder
     */
    private MediaService.MusicController musicController;

    /**
     * 通知点击的广播接收器
     */
    private MusicBroadcastReceiver musicBroadcastReceiver;

    private Activity activity;

    private AdEntryBean.DataDTO dataDTO;

    public OriginalFragment() {


    }


    public static OriginalFragment newInstance(String lessonID, int position, List<JpLesson> jpLessonList, int source) {

        OriginalFragment fragment = new OriginalFragment();
        Bundle bundle = new Bundle();
        bundle.putString("LESSON_ID", lessonID);
        bundle.putInt("POSITION", position);
        bundle.putSerializable("DATALIST", (Serializable) jpLessonList);
        bundle.putInt("SOURCE", source);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        //获取一个手动销毁的activity
        activity = getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {

            lessonID = bundle.getString("LESSON_ID", null);
            position = bundle.getInt("POSITION", 0);
            itemList = (List<JpLesson>) bundle.getSerializable("DATALIST");
            source = bundle.getInt("SOURCE");
        }

        decimalFormat = new DecimalFormat("#00");

        startHandler();
        //注册通知按钮点击事件的监听器

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MediaService.FLAG_SWITCH_DATA);
        intentFilter.addAction(MediaService.FLAG_MEDIA_BROADCAST);
        musicBroadcastReceiver = new MusicBroadcastReceiver();
        activity.registerReceiver(musicBroadcastReceiver, intentFilter);
    }

    /**
     * 开启服务
     */
    private void initMediaServer(String sSource) {

        Intent intent = new Intent(getContext(), MediaService.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("DATALIST", (Serializable) itemList);
        bundle.putString("SENTENCE_SOURCE", sSource);
        intent.putExtras(bundle);
        activity.startService(intent);
        activity.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            musicController = ((MediaService.MusicController) service);

            //是否正在播放，正在播放则判断是否是同一个数据，如果是同一个数据则不做操作，如果不是同一个数据则直接播新的数据；没有正在播放则直接播放新的数据
            if (musicController.isPlaying()) {

                JpLesson newJpLesson = itemList.get(position);
                JpLesson jpLesson = musicController.getCurData();
                if (jpLesson == null || !newJpLesson.getLessonID().equals(jpLesson.getLessonID())) {

                    musicController.setDataPosition(position);
                    musicController.play();
                }
                fragmentOriginalBinding.originalIvPlay.setImageResource(R.mipmap.ic_pause);
            } else {
                musicController.setDataPosition(position);
                musicController.play();
                fragmentOriginalBinding.originalIvPlay.setImageResource(R.mipmap.ic_pause);
            }
            //模式
            int mode = musicController.getMode();
            if (mode == MediaService.MODE_RANDOM) {

                fragmentOriginalBinding.originalIvMode.setImageResource(R.mipmap.icon_home_random_g);
            } else if (mode == MediaService.MODE_LIST) {

                fragmentOriginalBinding.originalIvMode.setImageResource(R.mipmap.icon_home_list_g);
            } else {

                fragmentOriginalBinding.originalIvMode.setImageResource(R.mipmap.icon_home_single_g);
            }

            //更新ui播放的状态变化
            handler.sendEmptyMessage(1);
            //启动新闻首页的播放器
            MyApplication.getContext().sendBroadcast(new Intent(MyApplication.getContext().getPackageName() + ".player"));
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

            if (musicController != null) {
                musicController = null;
            }
        }
    };

    /**
     * 切换数据
     */
    public void switchData() {

        Bundle bundle = getArguments();
        if (bundle != null) {

            lessonID = bundle.getString("LESSON_ID", null);
            position = bundle.getInt("POSITION", 0);
        }
        //请求数据
        presenter.getJpSentence(Constant.bookDataDTO.getName(), lessonID);
    }


    /**
     * 更新ui
     */
    private void startHandler() {

        handlerThread = new HandlerThread("检测播放与recler的位置");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {

                if (musicController != null && musicController.isPlaying()) {

                    long cp = musicController.getPosition();
                    long dur = musicController.getMusicDuration();

                    if (cp > dur) {//如果CurrentPosition超出Duration，则CurrentPosition==Duration
                        cp = dur;
                    }

                    setViewText(getTimeStr(cp), getTimeStr(dur), cp, dur);
                }
                handler.sendEmptyMessageDelayed(1, 50);
                return false;
            }
        });
    }

    /**
     * 设置seekbar、播放时间、总时间、adapter,滚动到指定位置
     *
     * @param cp
     * @param dur
     * @param cpInt
     * @param durInt
     */
    private synchronized void setViewText(String cp, String dur, long cpInt, long durInt) {

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {

                fragmentOriginalBinding.originalTvPlayTime.setText(cp);
                fragmentOriginalBinding.originalTvDuration.setText(dur);

                fragmentOriginalBinding.originalSb.setProgress((int) cpInt);
                fragmentOriginalBinding.originalSb.setMax((int) durInt);

                int s = (int) (cpInt / 1000);

                for (int i = 0, size = originalAdapter.getData().size(); i < size; i++) {

                    Sentence dataDTO = originalAdapter.getItem(i);
                    if (s < Double.parseDouble(dataDTO.getEndTime()) && s > Double.parseDouble(dataDTO.getStartTime())) {

                        if (originalAdapter.getPosition() != i) {

                            originalAdapter.setPosition(i);
                            moveToPosition(fragmentOriginalBinding.originalRvSentences, i);
                            originalAdapter.notifyDataSetChanged();
                            break;
                        }
                    }
                }


            }
        });
    }

    /**
     * 同步播放
     *
     * @param rv
     * @param position
     */
    private void moveToPosition(RecyclerView rv, int position) {

        int firstItem = rv.getChildLayoutPosition(rv.getChildAt(0));
        int lastItem = rv.getChildLayoutPosition(rv.getChildAt(rv.getChildCount() - 1));
        if (position < firstItem || position > lastItem) {
            rv.smoothScrollToPosition(position);
        } else {
            int movePosition = position - firstItem;
            int top = rv.getChildAt(movePosition).getTop();
            rv.smoothScrollBy(0, top);
        }
    }

    /**
     * 获取播放时间
     *
     * @param mill
     * @return
     */
    private String getTimeStr(long mill) {

        long s = mill / 1000;

        long ts = s % 60;
        long tm = s / 60;


        String cpStr = decimalFormat.format(ts);
        String durStr = decimalFormat.format(tm);

        return durStr + ":" + cpStr;
    }


    private void initSpeedPopup() {

        if (speedPopup == null) {

            speedPopup = new SpeedPopup(getContext());
            speedPopup.setCallback(new SpeedPopup.Callback() {
                @Override
                public void getChoose(float speedFloat) {

                    musicController.setPlaySpeed(speedFloat);
                    fragmentOriginalBinding.originalTvSpeed.setText(speedFloat + "x");
                    speedPopup.dismiss();
                }
            });
        }
        speedPopup.setChoosed(fragmentOriginalBinding.originalTvSpeed.getText().toString());
        speedPopup.showPopupWindow();
    }

    @Override
    public void onResume() {
        super.onResume();

        checkCollect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (handlerThread != null) {

            handlerThread.quit();
        }
        if (serviceConnection != null && musicController != null) {

            activity.unbindService(serviceConnection);
        }
        if (musicBroadcastReceiver != null) {

            activity.unregisterReceiver(musicBroadcastReceiver);
        }
        activity = null;
    }

    @Override
    protected View initLayout(LayoutInflater inflater, ViewGroup container) {

        fragmentOriginalBinding = FragmentOriginalBinding.inflate(inflater, container, false);
        return fragmentOriginalBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (musicController != null) {

            if (musicController.isPlaying()) {

                fragmentOriginalBinding.originalIvPlay.setImageResource(R.mipmap.ic_pause);

            } else {

                fragmentOriginalBinding.originalIvPlay.setImageResource(R.mipmap.ic_play);
            }
        }

        initOperation();

        //获取课本的name
        List<Book> bookList = LitePal.where("source = ?", source + "").find(Book.class);
        String bookName = "jp3";
        if (bookList.size() != 0) {

            bookName = bookList.get(0).getName();
        }

        DecimalFormat decimalFormat = new DecimalFormat("#000");
        String lessonidStr = decimalFormat.format(Integer.parseInt(lessonID));
        List<Sentence> sentenceList = LitePal.where("source = ? and sourceid = ? and starttime  is not null", bookName, lessonidStr).find(Sentence.class);


        originalAdapter.setNewData(sentenceList);
        if (sentenceList.size() != 0) {

            if (musicController == null) {//是否已绑定

                initMediaServer(sentenceList.get(0).getSource());
            } else {

                musicController.setSource(sentenceList.get(0).getSource());
            }
        }


        if (Constant.userinfo == null || !Constant.userinfo.isVip()) {

            String uid = "0";
            if (Constant.userinfo != null) {

                uid = Constant.userinfo.getUid() + "";
            }
            presenter.getAdEntryAll(Constant.APPID + "", 4, uid);
        }


//        checkCollect();
        //请求数据
//        presenter.getJpSentence(Constant.bookDataDTO.getName(), lessonID);
    }

    /**
     * 检测收藏
     */
    private void checkCollect() {

        if (Constant.userinfo != null) {

            //检测是否收藏此课文，Integer.parseInt(lessonID)处理id为001这种形式
            List<JpLesson> jpLessons = LitePal
                    .where("lessonid = ? and source = ?", Integer.parseInt(lessonID) + "", itemList.get(position).getSource())
                    .find(JpLesson.class);
            if (jpLessons.size() != 0) {

                JpLesson jpLesson = jpLessons.get(0);
                if (jpLesson.getCollect() == 1) {

                    fragmentOriginalBinding.originalCbCollect.setChecked(true);
                } else {

                    fragmentOriginalBinding.originalCbCollect.setChecked(false);
                }
            }
        }
    }


    private void initOperation() {

        originalAdapter = new OriginalAdapter(R.layout.item_original, new ArrayList<>());
        fragmentOriginalBinding.originalRvSentences.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        fragmentOriginalBinding.originalRvSentences.setAdapter(originalAdapter);

        //没有数据或者报错显示的控件
        emptyView = LayoutInflater.from(getContext()).inflate(R.layout.empty_view, null);
        empty_tv_content = emptyView.findViewById(R.id.empty_tv_content);
        empty_tv_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                presenter.getJpSentence(Constant.bookDataDTO.getName(), lessonID);
            }
        });

        //seekbar
        fragmentOriginalBinding.originalSb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                if (musicController != null) {

                    musicController.seek(seekBar.getProgress());
                }
            }
        });
        //速度
        fragmentOriginalBinding.originalFlSpeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Constant.userinfo == null) {

                    startActivity(new Intent(getActivity(), WxLoginActivity.class));
                } else {

                    if (Constant.userinfo.isVip()) {

                        initSpeedPopup();
                    } else {

                        toast("调速功能需要购买会员");
                    }
                }
            }
        });
        //播放或者暂停
        fragmentOriginalBinding.originalIvPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (musicController == null) {

                    return;
                }

                if (musicController.isPlaying()) {

//                        handlerThread.quit();
                    musicController.pause();
                    fragmentOriginalBinding.originalIvPlay.setImageResource(R.mipmap.ic_play);
                } else {

//                        startHandler();
//                        handler.sendEmptyMessage(1);
                    musicController.start();
                    fragmentOriginalBinding.originalIvPlay.setImageResource(R.mipmap.ic_pause);
                }
            }
        });
        //收藏
        fragmentOriginalBinding.originalFlCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Constant.userinfo == null) {

                    startActivity(new Intent(getActivity(), WxLoginActivity.class));
                } else {

                    List<JpLesson> jpLessons = LitePal
                            .where("lessonid = ? and source = ?", Integer.parseInt(lessonID) + "", itemList.get(position).getSource())
                            .find(JpLesson.class);
                    String typeStr = "upt";
                    if (jpLessons.size() == 0) {//没有数据直接存

                        typeStr = "upt";
                    } else {//有数据

                        JpLesson jpLesson = jpLessons.get(0);
                        if (jpLesson.getCollect() == 1) {

                            typeStr = "del";
                        } else {

                            typeStr = "upt";
                        }
                    }

                    String name = SourceUtil.getNameFromSource(Integer.parseInt(itemList.get(position).getSource()));
                    presenter.updateCollect_wx(Constant.userinfo.getUid() + "", lessonID, 0, "Iyuba",
                            0, typeStr, name);
                }
            }
        });
        //播放模式
        fragmentOriginalBinding.originalIvMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (musicController == null) {

                    return;
                }
                int mode = musicController.getMode();
                if (mode == 1) {

                    musicController.setPlayMode(MediaService.MODE_LIST);
                    fragmentOriginalBinding.originalIvMode.setImageResource(R.mipmap.icon_home_list_g);
                    toast("列表循环");
                } else if (mode == 2) {

                    musicController.setPlayMode(MediaService.MODE_SINGLE);
                    fragmentOriginalBinding.originalIvMode.setImageResource(R.mipmap.icon_home_single_g);
                    toast("单曲循环");
                } else {

                    musicController.setPlayMode(MediaService.MODE_RANDOM);
                    fragmentOriginalBinding.originalIvMode.setImageResource(R.mipmap.icon_home_random_g);
                    toast("随机播放");
                }
            }
        });
        //自家广告关闭
        fragmentOriginalBinding.originalIvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragmentOriginalBinding.originalFlAd.setVisibility(View.GONE);
            }
        });
        //广告
        fragmentOriginalBinding.originalIvAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dataDTO != null) {

                    startActivity(Web.buildIntent(requireActivity(), dataDTO.getStartuppicUrl(), "推广"));
                }
            }
        });
    }

    @Override
    protected OriginalContract.OriginalPresenter initPresenter() {
        return new OriginalPresenter();
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
    public void getJpSentenceComplete(JpSentenceBean jpSentenceBean) {


        if (originalAdapter.getEmptyView() == null) {

            originalAdapter.setEmptyView(emptyView);
        }

        if (jpSentenceBean == null) {

            empty_tv_content.setText("连接超时点击重试");
        } else {


            if (jpSentenceBean.getSize() == 0) {

                empty_tv_content.setText("没有更多数据，点击重试");
            } else {

                originalAdapter.setNewData(jpSentenceBean.getData());
                if (jpSentenceBean.getData().size() != 0) {

                    if (musicController == null) {//是否已绑定

                        initMediaServer(jpSentenceBean.getData().get(0).getSource());
                    } else {

                        musicController.setSource(jpSentenceBean.getData().get(0).getSource());
                    }
                }
            }
        }
    }


    @Override
    public void updateCollectComplete(UCollectBean uCollectBean, String type) {


        if (type.equals("upt")) {

            fragmentOriginalBinding.originalCbCollect.setChecked(true);
            //是否有此数据，没有则添加，有则更新
            List<JpLesson> jpLessons = LitePal
                    .where("lessonid = ? and source = ?", Integer.parseInt(lessonID) + "", itemList.get(position).getSource())
                    .find(JpLesson.class);
            if (jpLessons.size() == 0) {

                JpLesson jpLesson = new JpLesson();
                jpLesson.setLessonID(Integer.parseInt(lessonID) + "");
                jpLesson.setCollect(1);
                jpLesson.save();
            } else {

                JpLesson lesson = jpLessons.get(0);
                lesson.setCollect(1);
                lesson.updateAll("lessonid = ? and source = ?", Integer.parseInt(lessonID) + "", itemList.get(position).getSource());
            }

        } else {//del

            fragmentOriginalBinding.originalCbCollect.setChecked(false);
            //是否有此数据，没有则添加，有则更新
            List<JpLesson> jpLessons = LitePal
                    .where("lessonid = ? and source = ?", Integer.parseInt(lessonID) + "", itemList.get(position).getSource())
                    .find(JpLesson.class);
            if (jpLessons.size() == 0) {

                JpLesson jpLesson = new JpLesson();
                jpLesson.setLessonID(Integer.parseInt(lessonID) + "");
                jpLesson.setCollect(0);
                jpLesson.save();
            } else {

                //updateall不管用
                JpLesson lesson = jpLessons.get(0);
                //不能从其他值set成0,只能使用setToDefault
                lesson.setToDefault("collect");
                lesson.updateAll("lessonid = ? and source = ?", Integer.parseInt(lessonID) + "", itemList.get(position).getSource());
            }

        }
    }

    @Override
    public void getAdEntryAllComplete(AdEntryBean adEntryBean) {

        dataDTO = adEntryBean.getData();
        String type = dataDTO.getType();

        if (type.equals(Constant.AD_ADS1) || type.equals(Constant.AD_ADS2) || type.equals(Constant.AD_ADS3)
                || type.equals(Constant.AD_ADS4) || type.equals(Constant.AD_ADS5)) {

            DisplayMetrics displayMetrics = requireContext().getResources().getDisplayMetrics();
            int width = displayMetrics.widthPixels;
            int height = DensityUtil.dp2px(requireContext(), 65);

            YdBanner mBanner = new YdBanner.Builder(requireContext())
                    .setKey("0126")
                    .setWidth(width)
                    .setHeight(height)
                    .setMaxTimeoutSeconds(5)
                    .setBannerListener(this)
                    .build();

            mBanner.requestBanner();
        } else if (type.equals("web")) {

            Glide.with(requireActivity()).load(dataDTO.getStartuppicUrl()).into(fragmentOriginalBinding.originalIvAd);
        }
    }

    @Override
    public void onReceived(View view) {

        fragmentOriginalBinding.originalFlAd.removeAllViews();
        fragmentOriginalBinding.originalFlAd.addView(view);
        fragmentOriginalBinding.originalFlAd.setVisibility(View.VISIBLE);
        Timber.d("onReceived");
    }

    @Override
    public void onAdExposure() {

    }

    @Override
    public void onAdClick(String s) {

    }

    @Override
    public void onClosed() {

        fragmentOriginalBinding.originalFlAd.setVisibility(View.GONE);
        Timber.d("onClosed");
    }

    @Override
    public void onAdFailed(YdError ydError) {

        Timber.d("onAdFailed:" + ydError.getMsg());
    }

    /**
     * 用来接收通知栏状态的改变及播放数据的改变
     */
    class MusicBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (action.equals(MediaService.FLAG_MEDIA_BROADCAST)) {//通知点击事件间接相关，用来更新播放按钮

                if (musicController != null) {

                    if (musicController.isPlaying()) {

                        fragmentOriginalBinding.originalIvPlay.setImageResource(R.mipmap.ic_pause);
                    } else {

                        fragmentOriginalBinding.originalIvPlay.setImageResource(R.mipmap.ic_play);
                    }
                }
            } else if (action.equals(MediaService.FLAG_SWITCH_DATA)) {//切换数据

                TextbookDetailsActivity textbookDetailsActivity = (TextbookDetailsActivity) getActivity();
                if (textbookDetailsActivity != null && musicController != null) {
                    textbookDetailsActivity.setNewData(musicController.getCurData(), position);
                }
            }
        }
    }
}