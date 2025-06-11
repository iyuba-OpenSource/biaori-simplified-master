package com.iyuba.biaori.simplified.ui.dubbing;

import androidx.annotation.NonNull;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.analytics.AnalyticsListener;
import com.iyuba.biaori.simplified.Constant;
import com.iyuba.biaori.simplified.MyApplication;
import com.iyuba.biaori.simplified.databinding.ActivityDubbingPreviewBinding;
import com.iyuba.biaori.simplified.entity.DubbingPublish;
import com.iyuba.biaori.simplified.entity.MediaPauseEventbus;
import com.iyuba.biaori.simplified.model.bean.dubbing.DubbingPublishBean;
import com.iyuba.biaori.simplified.model.bean.dubbing.DubbingTextBean;
import com.iyuba.biaori.simplified.model.bean.textbook.UpdateScoreBean;
import com.iyuba.biaori.simplified.presenter.dubbing.DubbingPreviewPresenter;
import com.iyuba.biaori.simplified.ui.BaseActivity;
import com.iyuba.biaori.simplified.ui.login.LoginActivity;
import com.iyuba.biaori.simplified.ui.login.WxLoginActivity;
import com.iyuba.biaori.simplified.view.dubbing.DubbingPreviewContract;
import com.mob.MobSDK;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * 配音预览页面
 */
public class DubbingPreviewActivity extends BaseActivity<DubbingPreviewContract.DubbingPreviewView, DubbingPreviewContract.DubbingPreviewPresenter>
        implements DubbingPreviewContract.DubbingPreviewView {

    private ActivityDubbingPreviewBinding activityDubbingPreviewBinding;

    private List<DubbingTextBean.VoatextDTO> voatextDTOList;

    private String id;

    private String category;

    private MediaPlayer bgMediaPlayer;

    private MediaPlayer sentenceMediaPlayer;

    /**
     * 视频地址
     */
    private String url;

    /**
     * 视频播放器
     */
    private ExoPlayer player;

    /**
     * 背景音乐地址
     */
    private String bg_sound;


    /**
     * 图片地址
     */
    private String pic;

    /**
     * 新闻或视频的标题
     */
    private String title;

    private Handler handler;

    /**
     * @param activity
     * @param voatextDTOList 数据
     * @param id             id
     */
    public static void startActivity(Activity activity, List<DubbingTextBean.VoatextDTO> voatextDTOList,
                                     String id, String category, String bg_sound, String pic, String title) {

        Intent intent = new Intent(activity, DubbingPreviewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("DATA", (Serializable) voatextDTOList);
        bundle.putString("ID", id);
        bundle.putString("CATEGORY", category);
        bundle.putString("BG_SOUND", bg_sound);
        bundle.putString("PIC", pic);
        bundle.putString("TITLE", title);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    private void getBundle() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            voatextDTOList = (List<DubbingTextBean.VoatextDTO>) bundle.getSerializable("DATA");
            id = bundle.getString("ID");
            category = bundle.getString("CATEGORY");
            bg_sound = bundle.getString("BG_SOUND");
            pic = bundle.getString("PIC");
            title = bundle.getString("TITLE");
        }
    }

    /**
     * 背景音乐播放器
     */
    private void initBgMediaPlayer(String url) {

        if (bgMediaPlayer == null) {

            bgMediaPlayer = new MediaPlayer();
            bgMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {

                    EventBus.getDefault().post(new MediaPauseEventbus());
                    bgMediaPlayer.start();
                }
            });
        }
        try {
            bgMediaPlayer.setVolume(0.05f, 0.05f);
            bgMediaPlayer.setDataSource(url);
            bgMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 播放录制的音频
     *
     * @param url
     */
    private void initSentenceMediaPlayer(String url) {

        if (sentenceMediaPlayer == null) {

            sentenceMediaPlayer = new MediaPlayer();
            sentenceMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {

                    sentenceMediaPlayer.start();
                }
            });
        }
        try {
            sentenceMediaPlayer.reset();
            sentenceMediaPlayer.setDataSource(url);
            sentenceMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getBundle();
        testPlay();
        initOperation();

        url = getExternalFilesDir(Environment.DIRECTORY_MOVIES) + "/" + id + ".mp4";
        if (new File(url).exists()) {

            MediaItem mediaItem = MediaItem.fromUri(url);
            player = new ExoPlayer.Builder(this).build();
            activityDubbingPreviewBinding.dpSpv.setPlayer(player);
            player.setMediaItem(mediaItem);
            player.prepare();
            player.setVolume(0);

            player.addAnalyticsListener(new AnalyticsListener() {
                @Override
                public void onIsPlayingChanged(EventTime eventTime, boolean isPlaying) {

                    if (isPlaying) {

                        initBgMediaPlayer(bg_sound);
                        handler.sendEmptyMessage(1);
                    } else {

                        if (bgMediaPlayer != null) {

                            bgMediaPlayer.pause();
                        }
                        if (sentenceMediaPlayer != null) {

                            sentenceMediaPlayer.pause();
                        }
                    }
                }
            });
        }
    }

    private void initOperation() {

        //准确度
        double totalScore = 0;
        //评测的个数
        int accurateCount = 0;
        for (int i = 0; i < voatextDTOList.size(); i++) {

            DubbingTextBean.VoatextDTO voatextDTO = voatextDTOList.get(i);
            if (!voatextDTO.getScore().equals("")) {

                totalScore = totalScore + Double.parseDouble(voatextDTO.getScore());
                accurateCount++;
            }
        }

        int aPrcent = 0;
        if (accurateCount != 0) {

            aPrcent = (int) (totalScore * 20 / accurateCount);
        }

        activityDubbingPreviewBinding.dpPbAccurate.setMax(100);
        activityDubbingPreviewBinding.dpPbAccurate.setProgress(aPrcent);
        activityDubbingPreviewBinding.dpTvAccurate.setText(aPrcent + "%");
        //完成度
        int completePrcent = 100 * accurateCount / voatextDTOList.size();
        activityDubbingPreviewBinding.dpPbIntegrity.setMax(100);
        activityDubbingPreviewBinding.dpPbIntegrity.setProgress(completePrcent);
        activityDubbingPreviewBinding.dpTvIntegrity.setText(completePrcent + "%");

        activityDubbingPreviewBinding.dpTvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        activityDubbingPreviewBinding.dpIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        activityDubbingPreviewBinding.dpTvPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (Constant.userinfo != null) {

                    DubbingPublish dubbingPublish = new DubbingPublish();
                    dubbingPublish.setAppName("biaori");
                    dubbingPublish.setFlag(1);
                    dubbingPublish.setFormat("json");
                    dubbingPublish.setIdIndex(0);
                    dubbingPublish.setParaId(0);
                    dubbingPublish.setPlatform("android");

                    int recordCount = 0;
                    int totalScore = 0;
                    for (int i = 0; i < voatextDTOList.size(); i++) {

                        DubbingTextBean.VoatextDTO voatextDTO = voatextDTOList.get(i);
                        if (voatextDTO.getUrl() != null) {

                            totalScore = (int) (totalScore + Double.parseDouble(voatextDTO.getScore()));
                            recordCount++;
                        }
                    }
                    dubbingPublish.setScore(20 * totalScore / recordCount);
                    dubbingPublish.setShuoshuotype(3);
                    dubbingPublish.setSound(bg_sound);
                    dubbingPublish.setTopic("biaori");
                    dubbingPublish.setUsername(Constant.userinfo.getUsername());
                    dubbingPublish.setVoaid(Integer.parseInt(id));
                    List<DubbingPublish.WavList> wavLists = new ArrayList<>();
                    for (int i = 0; i < voatextDTOList.size(); i++) {

                        DubbingTextBean.VoatextDTO voatextDTO = voatextDTOList.get(i);
                        if (voatextDTO.getUrl() != null) {

                            DubbingPublish.WavList wavList = new DubbingPublish.WavList();
                            wavList.setBeginTime(voatextDTO.getTiming() + "");
                            int next = i + 1;
                            String endTime;
                            if (next < voatextDTOList.size()) {

                                endTime = voatextDTOList.get(next).getTiming() + "";
                            } else {
                                endTime = voatextDTO.getEndTiming() + "";
                            }
                            wavList.setEndTime(endTime);

                            double d = Double.parseDouble(endTime) - voatextDTO.getTiming();
                            wavList.setDuration(d + "");
                            wavList.setIndex(Integer.parseInt(voatextDTO.getParaId()));
                            wavList.setURL(voatextDTO.getUrl());
                            wavLists.add(wavList);
                        }
                    }
                    dubbingPublish.setWavList(wavLists);
                    presenter.publishDubbing(60002, 3, Constant.userinfo.getUid() + "", dubbingPublish);
                } else {

                    startActivity(new Intent(DubbingPreviewActivity.this, WxLoginActivity.class));
                }
            }
        });
    }

    /**
     * 检测播放时间，来播放录制的音频文件
     */
    private void testPlay() {

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {

                if (player.isPlaying() && voatextDTOList != null) {

                    long curPosition = player.getCurrentPosition();
                    for (int i = 0; i < voatextDTOList.size(); i++) {

                        DubbingTextBean.VoatextDTO voatextDTO = voatextDTOList.get(i);
                        if (curPosition > (voatextDTO.getTiming() * 1000) && curPosition < (voatextDTO.getEndTiming() * 1000)
                                && voatextDTO.getUrl() != null) {

                            if (sentenceMediaPlayer != null) {

                                if (!sentenceMediaPlayer.isPlaying()) {

                                    initSentenceMediaPlayer(getExternalFilesDir(Environment.DIRECTORY_MUSIC).getAbsolutePath() + "/" + id + voatextDTO.getParaId() + ".aac");
                                }
                            } else {
                                //播放录制音频
                                initSentenceMediaPlayer(getExternalFilesDir(Environment.DIRECTORY_MUSIC).getAbsolutePath() + "/" + id + voatextDTO.getParaId() + ".aac");
                            }
                            break;
                        }
                    }
                }
                handler.sendEmptyMessageDelayed(1, 50);
                return false;
            }
        });
    }

    @Override
    public View initLayout() {
        activityDubbingPreviewBinding = ActivityDubbingPreviewBinding.inflate(getLayoutInflater());
        return activityDubbingPreviewBinding.getRoot();
    }

    @Override
    public DubbingPreviewContract.DubbingPreviewPresenter initPresenter() {
        return new DubbingPreviewPresenter();
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
    public void publishDubbingCompelte(DubbingPublishBean dubbingPublishBean) {

        String urlTitle = "http://voa.iyuba.cn/voa/talkShowShare.jsp?shuoshuoId=" + dubbingPublishBean.getShuoShuoId() + "&apptype=biaori";
        share("配音员：" + Constant.userinfo.getUsername() + title, urlTitle, "爱语吧 " + title, pic, urlTitle);
    }

    @Override
    public void updateScore(UpdateScoreBean updateScoreBean) {

        if (updateScoreBean.getAddcredit().equals("0")) {

            toast("分享成功");
        } else {
            toast("分享成功，获取" + updateScoreBean.getAddcredit() + "积分");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (sentenceMediaPlayer != null) {

            sentenceMediaPlayer.release();
            sentenceMediaPlayer = null;
        }
        if (player != null) {

            player.release();
            player = null;
        }
        if (bgMediaPlayer != null) {

            bgMediaPlayer.release();
            bgMediaPlayer = null;
        }

    }

    /**
     * 分享
     */
    private void share(String title, String titleUrl, String text, String imgUrl, String wxUrl) {

        OnekeyShare oks = new OnekeyShare();
        //指定分享的平台，如果为空，还是会调用九宫格的平台列表界面
//        if (platform != null) {
//            oks.setPlatform(platform);
//        }
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(title);
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl(titleUrl);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(text);
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl(imgUrl);
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(wxUrl);
        //分享回调
        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                // 分享成功回调

                if (Constant.userinfo == null) {

                    return;
                }

                int srid = 0;
                if (platform.getName() == QQ.NAME || platform.getName() == Wechat.NAME) {

                    srid = 49;
                } else {
                    srid = 19;
                }

                SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
                String flag = null;
                try {
                    flag = Base64.encodeToString(
                            URLEncoder.encode(df.format(new Date(System.currentTimeMillis())), "UTF-8").getBytes(), Base64.DEFAULT);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                presenter.updateScore(flag, Constant.userinfo.getUid() + "", Constant.APPID + "",
                        id, srid, 1);
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                // 分享失败回调   platform:平台对象，i:表示当前的动作(9表示分享)，throwable:异常信息

            }

            @Override
            public void onCancel(Platform platform, int i) {
                // 分享取消回调
            }
        });
        // 启动分享
        oks.show(MobSDK.getContext());
    }
}