package com.iyuba.biaori.simplified.ui.dubbing;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.coremedia.iso.boxes.Container;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.analytics.AnalyticsListener;
import com.googlecode.mp4parser.FileDataSourceImpl;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AACTrackImpl;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;
import com.googlecode.mp4parser.authoring.tracks.DTSTrackImpl;
import com.googlecode.mp4parser.authoring.tracks.MP3TrackImpl;
import com.iyuba.biaori.simplified.Constant;
import com.iyuba.biaori.simplified.MyApplication;
import com.iyuba.biaori.simplified.R;
import com.iyuba.biaori.simplified.adapter.dubbing.DubbingAdapter;
import com.iyuba.biaori.simplified.databinding.ActivityDubbingBinding;
import com.iyuba.biaori.simplified.entity.MediaPauseEventbus;
import com.iyuba.biaori.simplified.model.bean.dubbing.DubbingTextBean;
import com.iyuba.biaori.simplified.model.bean.textbook.EvaluteRecordBean;
import com.iyuba.biaori.simplified.presenter.dubbing.DubbingPresenter;
import com.iyuba.biaori.simplified.ui.BaseActivity;
import com.iyuba.biaori.simplified.ui.textbook.WordDetailsActivity;
import com.iyuba.biaori.simplified.util.LineItemDecoration;
import com.iyuba.biaori.simplified.util.popup.DownloadPopup;
import com.iyuba.biaori.simplified.util.popup.PermissionPopup;
import com.iyuba.biaori.simplified.view.dubbing.DubbingContract;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 配音界面
 */
public class DubbingActivity extends BaseActivity<DubbingContract.DubbingView, DubbingContract.DubbingPresenter>
        implements DubbingContract.DubbingView {

    private ActivityDubbingBinding activityDubbingBinding;

    private DubbingAdapter dubbingAdapter;

    private LineItemDecoration lineItemDecoration;

    private ExoPlayer player;

    private String id;
    private String category;
    private String bg_sound;
    private String pic;
    private String title;

    private static String ID = "ID";
    private static String CATEGORY = "CATEGORY";
    //背景音频
    private static String BG_SOUND = "BG_SOUND";
    private static String PIC = "PIC";
    private static String TITLE = "TITLE";


    private MediaRecorder mediaRecorder;

    private Handler handler;

    private HandlerThread handlerThread;

    private MediaPlayer userMediaPlayer;

    private MediaPlayer bgMediaPlayer;
    /**
     * 检测播放，用来暂停
     */
    private Handler playHandler;

    private DownloadPopup downloadPopup;

    //视频
    private String url = null;

    /**
     * 权限请求
     */
    private RxPermissions rxPermissions;

    private PermissionPopup permissionPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getBundle();
        initOperation();

        testPlay();
        testRecord();

        if (Constant.userinfo == null) {

            url = "http://static0.iyuba.cn" + "/video/voa/" + category + "/" + id + ".mp4";
        } else {

            if (Constant.userinfo.isVip()) {

                url = "http://staticvip.iyuba.cn" + "/video/voa/" + category + "/" + id + ".mp4";
            } else {

                url = "http://static0.iyuba.cn" + "/video/voa/" + category + "/" + id + ".mp4";
            }

        }


        new Thread(new Runnable() {
            @Override
            public void run() {

                downloadVideo(url, id);
            }
        }).start();


        MediaItem mediaItem = MediaItem.fromUri(url);

        player = new ExoPlayer.Builder(this).build();
        activityDubbingBinding.dubbingSpv.setPlayer(player);
        player.setMediaItem(mediaItem);
        player.prepare();

        player.addAnalyticsListener(new AnalyticsListener() {
            @Override
            public void onIsPlayingChanged(EventTime eventTime, boolean isPlaying) {
//                AnalyticsListener.super.onIsPlayingChanged(eventTime, isPlaying);

                if (!isPlaying) {

                    player.setVolume(0.8f);
                    if (dubbingAdapter.getPostion() == -1) {

                        return;
                    }
                    DubbingTextBean.VoatextDTO voatextDTO = dubbingAdapter.getItem(dubbingAdapter.getPostion());
                    player.seekTo((long) (voatextDTO.getTiming() * 1000));
                } else {

                    EventBus.getDefault().post(new MediaPauseEventbus());
                    //启动检测
                    playHandler.sendEmptyMessage(1);
                }
            }
        });


        presenter.textExamApi("json", id);
    }


    private void initDownloadPopup(String msg) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (downloadPopup == null) {

                    downloadPopup = new DownloadPopup(DubbingActivity.this);
                }
                downloadPopup.setContent(msg);
                downloadPopup.showPopupWindow();
            }
        });

    }

    private void hideDownloadPopup() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (downloadPopup != null) {

                    downloadPopup.dismiss();
                }
            }
        });

    }

    /**
     * 检测播放的进度
     */
    private void testPlay() {

        playHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {

                if (dubbingAdapter.getPostion() == -1) {

                    return false;
                }
                long endTime = 0;
                int next = dubbingAdapter.getPostion() + 1;//计算一句的开始时间
                if (next < dubbingAdapter.getItemCount()) {

                    DubbingTextBean.VoatextDTO voatextDTO = dubbingAdapter.getItem(next);
                    endTime = (long) (voatextDTO.getTiming() * 1000);
                } else {//最后一条数据处理

                    DubbingTextBean.VoatextDTO voatextDTO = dubbingAdapter.getItem(dubbingAdapter.getPostion());
                    endTime = (long) (voatextDTO.getEndTiming() * 1000) + 1000;
                }

                if (player.isPlaying()) {

                    if (player.getCurrentPosition() >= endTime) {

                        stopPlay();
                        dubbingAdapter.getItem(dubbingAdapter.getPostion()).setPlaying(false);
                        //更新播放按钮
                        dubbingAdapter.notifyItemChanged(dubbingAdapter.getPostion());
                    } else {

                        playHandler.sendEmptyMessageDelayed(1, 100);
                    }
                }
                return false;
            }
        });
    }


    /**
     * 检测录音相关
     */
    private void testRecord() {

        handlerThread = new HandlerThread("检测录音时长");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {

                DubbingTextBean.VoatextDTO voatextDTO = dubbingAdapter.getItem(dubbingAdapter.getPostion());
                if (voatextDTO.isRecord()) {

                    long recordTime = System.currentTimeMillis() - voatextDTO.getStartRecordTime();
                    voatextDTO.setRecordPosition(recordTime);
                    double maxDuration = 0;
                    int next = dubbingAdapter.getPostion() + 1;
                    if (next < dubbingAdapter.getItemCount()) {//下一个位置是否有数据

                        maxDuration = dubbingAdapter.getItem(next).getTiming() - voatextDTO.getTiming();
                    } else {//没有下一条数据，则直接计算本条数据的时间
                        maxDuration = voatextDTO.getEndTiming() - voatextDTO.getTiming() + 1;
                    }

                    if (recordTime < maxDuration * 1000) {//录音的时间小于最大录音时间

                        handler.sendEmptyMessageDelayed(1, 100);
                    } else {

                        //停止录音
                        voatextDTO.setRecord(false);
                        voatextDTO.setRecordPosition((long) (maxDuration * 1000));
                        stopRecord(voatextDTO);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            dubbingAdapter.notifyItemChanged(dubbingAdapter.getPostion());
                        }
                    });
                }
                return false;
            }
        });
    }

    /**
     * 播放自己录制音频
     */
    private void playSound() {

        if (userMediaPlayer == null) {

            userMediaPlayer = new MediaPlayer();
            userMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {

                    playBgSound();
                }
            });
        }
        userMediaPlayer.reset();

        int p = dubbingAdapter.getPostion();
        if (p == -1) {

            return;
        }
        DubbingTextBean.VoatextDTO voatextDTO = dubbingAdapter.getItem(p);

        try {
            userMediaPlayer.setDataSource(Constant.URL_IUSERSPEECH + "/voa/" + voatextDTO.getUrl());
            userMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 播放背景音乐
     */
    private void playBgSound() {

        if (bgMediaPlayer == null) {

            bgMediaPlayer = new MediaPlayer();

        }
        bgMediaPlayer.reset();
        bgMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

                int p = dubbingAdapter.getPostion();

                if (p == -1) {

                    return;
                }

                DubbingTextBean.VoatextDTO voatextDTO = dubbingAdapter.getItem(p);

                int curPosition = (int) (voatextDTO.getTiming() * 1000);

                userMediaPlayer.start();
                //背景音乐
                bgMediaPlayer.seekTo(curPosition);
                bgMediaPlayer.start();

                player.setVolume(0);//静音，凸显背景音乐和自己录制的音频
                player.seekTo(curPosition);
                player.play();
                //更新播放按钮
                dubbingAdapter.notifyItemChanged(dubbingAdapter.getPostion());
                //触发player的播放和暂停回调
            }
        });
        try {
            //非新闻类的
            bgMediaPlayer.setVolume(0.02f, 0.02f);
            if (!bg_sound.startsWith("http")) {//处理新闻的

                bgMediaPlayer.setVolume(1.0f, 1.0f);
                bg_sound = "http://staticvip.iyuba.cn/sounds/voa" + bg_sound;
            }
            bgMediaPlayer.setDataSource(bg_sound);
            bgMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initOperation() {

        dubbingAdapter = new DubbingAdapter(R.layout.item_dubbing, new ArrayList<>());
        activityDubbingBinding.dubbingRv.setLayoutManager(new LinearLayoutManager(this));
        activityDubbingBinding.dubbingRv.setAdapter(dubbingAdapter);
        activityDubbingBinding.dubbingRv.setItemAnimator(null);

        dubbingAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                DubbingTextBean.VoatextDTO voatextDTO = dubbingAdapter.getItem(position);

            }
        });
        dubbingAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                DubbingTextBean.VoatextDTO voatextDTO = dubbingAdapter.getItem(position);
                //是否有冲突，播放和录音有冲突
                if (dubbingAdapter.getPostion() == -1 || dubbingAdapter.getPostion() == position) {

                    if (dubbingAdapter.getPostion() == -1) {//位置在默认位置才需要进行处理

                        dubbingAdapter.setPostion(position);
                    }
                    playAndRecord(view, voatextDTO, position);
                } else {//选择操作的这项，与之前不一致，可能会有冲突，需处理

                    DubbingTextBean.VoatextDTO voatext = dubbingAdapter.getItem(dubbingAdapter.getPostion());
                    if (voatext.isPlaying() || voatext.isRecord()) {

                        if (voatext.isPlaying()) {
                            //停止播放
                            stopPlay();
                            voatext.setPlaying(false);
                            dubbingAdapter.notifyItemChanged(dubbingAdapter.getPostion());
                        }
                        if (voatext.isRecord()) {//如果正在录音则关闭录音

                            mediaRecorder.stop();
                            voatext.setRecordPosition(0);
                            voatext.setRecord(false);
                        }
                        dubbingAdapter.setPostion(position);
                        playAndRecord(view, voatextDTO, position);
                    } else {//操作的这一项没有录音也没有播放
                        dubbingAdapter.setPostion(position);
                        playAndRecord(view, voatextDTO, position);
                    }
                }
            }
        });

        lineItemDecoration = new LineItemDecoration(this, LinearLayoutManager.VERTICAL);
        lineItemDecoration.setDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.space_10dp, null));
        activityDubbingBinding.dubbingRv.addItemDecoration(lineItemDecoration);

        activityDubbingBinding.dubbingIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        //预览
        activityDubbingBinding.dubbingTvPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dubbingAdapter.getItemCount() != 0) {


                    boolean hasRecord = false;
                    List<DubbingTextBean.VoatextDTO> voatextDTOList = dubbingAdapter.getData();
                    for (int i = 0; i < voatextDTOList.size(); i++) {

                        DubbingTextBean.VoatextDTO voatextDTO = voatextDTOList.get(i);
                        if (voatextDTO.getUrl() != null) {

                            hasRecord = true;
                            break;
                        }
                    }

                    if (hasRecord) {

                        DubbingPreviewActivity.startActivity(DubbingActivity.this, dubbingAdapter.getData(),
                                id, category, bg_sound, pic, title);
                    } else {

                        toast("请配音");
                    }
                }
            }
        });
    }

    /**
     * 停止播放录音、视频、背景音乐
     */
    private void stopPlay() {

        if (userMediaPlayer != null && userMediaPlayer.isPlaying()) {
            userMediaPlayer.pause();
        }
        if (bgMediaPlayer.isPlaying()) {
            bgMediaPlayer.pause();
        }
        if (player.isPlaying()) {
            player.pause();
        }
    }

    /**
     * 录音和播放视频
     *
     * @param view
     * @param voatextDTO
     * @param position
     */
    private void playAndRecord(View view, DubbingTextBean.VoatextDTO voatextDTO, int position) {

        if (view.getId() == R.id.dubbing_iv_record) {//录音

            if (voatextDTO.isPlaying()) {

                voatextDTO.setPlaying(false);//录音和播放互斥
                //更新播放按钮
                dubbingAdapter.notifyItemChanged(dubbingAdapter.getPostion());
                stopPlay();
            }
            //检测是否授权录音权限
            requestRecord(voatextDTO, position);
        } else {//播放自己的录音
            //录音和播放互斥
            if (voatextDTO.isRecord() && mediaRecorder != null) {//中断录音
                mediaRecorder.reset();
                voatextDTO.setRecord(false);
                voatextDTO.setRecordPosition(0);
                //更新录音进度
                dubbingAdapter.notifyItemChanged(dubbingAdapter.getPostion());
            }
            if (voatextDTO.isPlaying()) {

                voatextDTO.setPlaying(false);
                stopPlay();
                //更新播放按钮
                dubbingAdapter.notifyItemChanged(dubbingAdapter.getPostion());
            } else {

                voatextDTO.setPlaying(true);
                //更新播放按钮
                dubbingAdapter.notifyItemChanged(dubbingAdapter.getPostion());
                playSound();
            }
        }
    }

    /**
     * 开始录音和停止录音
     *
     * @param voatextDTO
     * @param position
     */
    private void startStopRecord(DubbingTextBean.VoatextDTO voatextDTO, int position) {

        if (voatextDTO.isRecord()) {

            toast("结束录音");
            voatextDTO.setRecord(false);
            stopRecord(voatextDTO);
        } else {

            toast("开始录音");
            voatextDTO.setRecord(true);
            initRecorder(voatextDTO, position);
        }
    }

    /**
     * 权限提醒
     *
     * @param content
     */
    private void initPermissionPopup(String content, DubbingTextBean.VoatextDTO voatextDTO, int position) {

        if (permissionPopup == null) {

            permissionPopup = new PermissionPopup(DubbingActivity.this);
            permissionPopup.setCallback(new PermissionPopup.Callback() {
                @SuppressLint("CheckResult")
                @Override
                public void clickOk() {

                    if (rxPermissions == null) {

                        rxPermissions = new RxPermissions(DubbingActivity.this);
                    }
                    rxPermissions.request(Manifest.permission.RECORD_AUDIO)
                            .subscribe(new Consumer<Boolean>() {
                                @Override
                                public void accept(Boolean aBoolean) throws Exception {

                                    if (aBoolean) {

                                        startStopRecord(voatextDTO, position);
                                    } else {
                                        SharedPreferences sp = getSharedPreferences(Constant.SP_PERMISSION, MODE_PRIVATE);
                                        sp.edit().putInt(Constant.SP_KEY_PERMISSION_RECORD, 1).apply();
                                        toast("你禁止了录音权限");
                                    }
                                }
                            });
                    permissionPopup.dismiss();
                }
            });
        }
        permissionPopup.setContent(content);
        permissionPopup.showPopupWindow();
    }


    /**
     * 请求录音权限
     */
    @SuppressLint("CheckResult")
    private void requestRecord(DubbingTextBean.VoatextDTO voatextDTO, int position) {

        if (rxPermissions == null) {

            rxPermissions = new RxPermissions(DubbingActivity.this);
        }
        if (rxPermissions.isGranted(Manifest.permission.RECORD_AUDIO)) {

            startStopRecord(voatextDTO, position);
        } else {

            SharedPreferences sp = getSharedPreferences(Constant.SP_PERMISSION, MODE_PRIVATE);
            int flag = sp.getInt(Constant.SP_KEY_PERMISSION_RECORD, 0);
            if (flag == 0) {

                initPermissionPopup("配音将要申请录音权限！", voatextDTO, position);
            } else {

                toast("你禁止了录音权限，请在权限管理中打开");
            }
        }
    }

    /**
     * 启动录音
     *
     * @param voatextDTO
     */
    private void initRecorder(DubbingTextBean.VoatextDTO voatextDTO, int position) {

        if (mediaRecorder == null) {
            //初始化
            mediaRecorder = new MediaRecorder();
        }
        if (!getExternalFilesDir(Environment.DIRECTORY_MUSIC).exists()) {
            getExternalFilesDir(Environment.DIRECTORY_MUSIC).mkdirs();
        }

        mediaRecorder.reset();
        mediaRecorder.setOutputFile(getExternalFilesDir(Environment.DIRECTORY_MUSIC).getAbsolutePath() + "/" + (id + voatextDTO.getParaId()) + ".aac");
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        //计算最大的录音时间
        int newPos = position + 1;
        double maxDuration = 0;
        if (newPos < dubbingAdapter.getItemCount()) {

            maxDuration = dubbingAdapter.getItem(newPos).getTiming() - voatextDTO.getTiming();
        } else {//最后一条加1秒

            maxDuration = voatextDTO.getEndTiming() - voatextDTO.getTiming() + 1.0;
        }
        mediaRecorder.setMaxDuration((int) (maxDuration * 1000));
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            //记录开始录音时间
            voatextDTO.setStartRecordTime(System.currentTimeMillis());
            //启动检测
            handler.sendEmptyMessage(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 停止录音
     */
    private void stopRecord(DubbingTextBean.VoatextDTO voatextDTO) {

        if (mediaRecorder == null) {

            return;
        }
        mediaRecorder.stop();
        voatextDTO.setEndRecordTime(System.currentTimeMillis());

        int userId = Constant.userinfo == null ? 0 : Constant.userinfo.getUid();
        String filePath = getExternalFilesDir(Environment.DIRECTORY_MUSIC).getAbsolutePath() + "/" + id + voatextDTO.getParaId() + ".aac";
        presenter.jtest(userId, Integer.parseInt(voatextDTO.getIdIndex()),
                Integer.parseInt(voatextDTO.getParaId()), Integer.parseInt(id), "biaori", filePath
                , voatextDTO.getSentence()
        );
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
     * @param id       新闻id
     */
    public static void startActivity(Activity activity, String id, String category, String bg_sound, String pic, String title) {

        Intent intent = new Intent(activity, DubbingActivity.class);
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
    public View initLayout() {
        activityDubbingBinding = ActivityDubbingBinding.inflate(getLayoutInflater());
        return activityDubbingBinding.getRoot();
    }

    @Override
    public DubbingContract.DubbingPresenter initPresenter() {
        return new DubbingPresenter();
    }

    @Override
    public void showLoading(String msg) {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void toast(String msg) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Toast.makeText(MyApplication.getContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void textExamApiComplete(DubbingTextBean dubbingTextBean) {


        dubbingAdapter.setNewData(dubbingTextBean.getVoatext());
    }

    @Override
    public void jtestComplete(EvaluteRecordBean evaluteRecordBean, int paraId) {

        List<DubbingTextBean.VoatextDTO> voatextDTOList = dubbingAdapter.getData();
        int index = 0;
        for (int i = 0; i < voatextDTOList.size(); i++) {//找到评测的这句

            DubbingTextBean.VoatextDTO voatextDTO = voatextDTOList.get(i);
            if (voatextDTO.getParaId().equals(paraId + "")) {

                index = i;
                break;
            }
        }
        String url = evaluteRecordBean.getData().getUrl();
        String socre = evaluteRecordBean.getData().getTotalScore();
        voatextDTOList.get(index).setUrl(url);
        voatextDTOList.get(index).setScore(socre);
        dubbingAdapter.notifyItemChanged(index);
    }


    @Override
    protected void onStop() {
        super.onStop();

        if (player != null) {

            player.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (player != null) {

            player.release();
        }
        if (userMediaPlayer != null) {

            userMediaPlayer.release();
        }
        if (bgMediaPlayer != null) {

            bgMediaPlayer.release();
        }
    }


    /**
     * 下载视频
     *
     * @param url
     * @param id
     */
    private void downloadVideo(String url, String id) {

        String filePath = getExternalFilesDir(Environment.DIRECTORY_MOVIES).getAbsolutePath() + "/" + id + ".mp4";
        if (new File(filePath).exists()) {

            return;
        }

        initDownloadPopup("正在下载视频");
        OkHttpClient client = new OkHttpClient.Builder()
//                .addInterceptor(new RetryIntercepter(3))//重试3次
//                .addInterceptor(new GzipRequestInterceptor())//gzip压缩
                .connectTimeout(3 * 60 * 1000, TimeUnit.MILLISECONDS) //连接超时
                .readTimeout(3 * 60 * 1000, TimeUnit.MILLISECONDS) //读取超时
                .writeTimeout(60 * 1000, TimeUnit.MILLISECONDS) //写超时
                .callTimeout(3 * 60 * 1000, TimeUnit.MILLISECONDS)
                // okhttp默认使用的RealConnectionPool初始化线程数==2147483647，在服务端会导致大量线程TIMED_WAITING
                //ThreadPoolExecutor(0, 2147483647, 60L, TimeUnit.SECONDS, new SynchronousQueue(), Util.threadFactory("OkHttp ConnectionPool", true));
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {

                InputStream inputStream = response.body().byteStream();
                long contentLength = response.body().contentLength();
                FileOutputStream fileOutputStream = new FileOutputStream(getExternalFilesDir(Environment.DIRECTORY_MOVIES).getAbsolutePath() + "/" + id + ".mp4");

                byte[] b = new byte[1024];
                int curDataSize = 0;
                int len = inputStream.read(b);
                curDataSize = len;

                try {

                    while (len != -1) {

                        fileOutputStream.write(b, 0, len);
                        len = inputStream.read(b);
                        curDataSize = +len + curDataSize;
                        initDownloadPopup("正在下载视频" + (100 * curDataSize / contentLength) + "%");
                    }
                } catch (Exception e) {

                    hideDownloadPopup();
                    toast("下载异常");
                }
                hideDownloadPopup();
                fileOutputStream.flush();
                fileOutputStream.close();
                inputStream.close();
            } else {

                Log.d("download", response.message());
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}