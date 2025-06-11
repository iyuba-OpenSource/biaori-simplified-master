package com.iyuba.biaori.simplified.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.iyuba.biaori.simplified.Constant;
import com.iyuba.biaori.simplified.MyApplication;
import com.iyuba.biaori.simplified.R;
import com.iyuba.biaori.simplified.db.Book;
import com.iyuba.biaori.simplified.db.JpLesson;
import com.iyuba.biaori.simplified.db.Sentence;
import com.iyuba.biaori.simplified.presenter.textbook.MediaPresenter;
import com.iyuba.biaori.simplified.util.DateUtil;
import com.iyuba.biaori.simplified.view.textbook.MediaContract;
import com.iyuba.module.toolbox.MD5;

import org.litepal.LitePal;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

/**
 * 新闻音频播放器
 */
public class MediaService extends Service implements MediaContract.MediaView {

    private static final String TAG = "MediaService";
    private MediaPlayer mPlayer;

    private List<JpLesson> itemList;

    private int position;


    /**
     * 课本的source，如：jp3
     */
    private String sSource;

    /**
     * 随机播放
     */
    public final static int MODE_RANDOM = 1;

    /**
     * 列表播放
     */
    public final static int MODE_LIST = 2;

    /**
     * 单曲循环
     */
    public final static int MODE_SINGLE = 3;

    /**
     * 默认播放模式为3
     */
    private int mode = 3;
    /**
     * 随机播放
     */
    private Random random;

    /**
     * 格式化id
     */
    private DecimalFormat decimalFormat;


    /**
     * 通知栏上的点击事件
     */
    public static String FLAG_MUSIC_PLAY = MyApplication.getContext().getPackageName() + ".play";


    /**
     * 启动教材首页的绑定service
     */
    public static String FLAG_MEDIA_PLAY = MyApplication.getContext().getPackageName() + ".player";

    /**
     * 在broadcast中更改完播放转状态触发，更新教材首页播放器、详情页播放器的状态及更新
     */
    public static String FLAG_MEDIA_BROADCAST = MyApplication.getContext().getPackageName() + ".broadcast.update_play_state";

    /**
     * 随机播放、顺数播放引起的   详情页切换数据   教材首页播放器更新title及播放状态
     */
    public static String FLAG_SWITCH_DATA = MyApplication.getContext().getPackageName() + ".broadcast.switch_data";

    private RemoteViews remoteViews;


    private Notification notification;

    private NotificationManagerCompat nManagerCompat;

    private AudioManager audioManager;

    /**
     * android 8.0的音频焦点
     */
    private AudioFocusRequest audioFocusRequest;

    /**
     * presenter
     */
    private MediaPresenter mediaPresenter;


    /**
     * 上传听力进度的
     * 开始时间
     */
    private String beginTime;

    /**
     * 格式化id，如1->001
     *
     * @param id
     * @return
     */
    private String formatId(String id) {

        return decimalFormat.format(Integer.parseInt(id));
    }


    /* 绑定服务的实现流程：
     * 1.服务 onCreate， onBind， onDestroy 方法
     * 2.onBind 方法需要返回一个 IBinder 对象
     * 3.如果 Activity 绑定，Activity 就可以取到 IBinder 对象，可以直接调用对象的方法
     */
    // 相同应用内部不同组件绑定，可以使用内部类以及Binder对象来返回。
    public class MusicController extends Binder {


        /**
         * 传递source值，用来拼接文章音频的链接
         *
         * @param sSource
         */
        public void setSource(String sSource) {

            MediaService.this.sSource = sSource;
        }

        /**
         * 课文
         *
         * @return
         */
        public JpLesson getCurData() {

            if (itemList != null && position < itemList.size()) {

                return itemList.get(position);
            } else {
                return null;
            }
        }

        public int getDataPosition() {

            return position;
        }

        public void setDataPosition(int position) {

            MediaService.this.position = position;
        }

        public void play() {

//            String soundUrl = Constant.URL_STATIC2 + "/Japan/" + sSource + "/lesson/" + formatId(itemList.get(position).getLessonID()) + ".mp3";
            JpLesson jpLesson = itemList.get(position);

            String path = getExternalFilesDir(Environment.DIRECTORY_MUSIC)
                    .getAbsolutePath()
                    + jpLesson.getSound().replace("http://static2.iyuba.cn", "");
            String soundUrl = null;
            if (new File(path).exists()) {

                soundUrl = path;
            } else {

                soundUrl = itemList.get(position).getSound();
            }
            try {
                mPlayer.reset();
                mPlayer.setDataSource(soundUrl);
                mPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (remoteViews != null) {

                remoteViews.setImageViewResource(R.id.music_iv_bg, R.mipmap.ic_launcher);
                remoteViews.setTextViewText(R.id.music_tv_title, itemList.get(position).getLesson());
                remoteViews.setImageViewResource(R.id.music_iv_but, R.mipmap.icon_home_pause);
                startForeground(1, notification);
            }
        }

        public boolean isPlaying() {

            return mPlayer.isPlaying();
        }

        public void pause() {

            mPlayer.pause();  //暂停音乐
            sendPlayCurrent(0);
            //通知
            if (remoteViews != null) {

                remoteViews.setImageViewResource(R.id.music_iv_but, R.mipmap.icon_home_play);
                stopForeground(false);
                nManagerCompat.notify(1, notification);
            }
//            if (callback != null) {
//
//                callback.stateChange();
//            }
        }

        public long getMusicDuration() {
            return mPlayer.getDuration();  //获取文件的总长度
        }

        public long getPosition() {
            return mPlayer.getCurrentPosition();  //获取当前播放进度
        }

        public void seek(int position) {
            mPlayer.seekTo(position);  //重新设定播放进度
        }

        public void start() {
            mPlayer.start();
            beginTime = DateUtil.getCurTime();
            if (remoteViews != null) {

                remoteViews.setImageViewResource(R.id.music_iv_but, R.mipmap.icon_home_pause);
                nManagerCompat.notify(1, notification);
            }
        }

        public boolean setPlaySpeed(float speed) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PlaybackParams params = mPlayer.getPlaybackParams();
                params.setSpeed(speed);
                mPlayer.setPlaybackParams(params);
                return true;
            }
            return false;
        }


        /**
         * 设置播放模式
         *
         * @param mode
         */
        public void setPlayMode(int mode) {

            MediaService.this.mode = mode;
        }

        /**
         * 获取模式
         *
         * @return
         */
        public int getMode() {

            return mode;
        }
    }

    /**
     * 当绑定服务的时候，自动回调这个方法
     * 返回的对象可以直接操作Service内部的内容
     *
     * @param intent
     * @return
     */
    @Override
    public IBinder onBind(Intent intent) {

        return new MusicController();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

//        position = intent.getIntExtra("POSITION", 0);
        if (intent != null) {

            Bundle bundle = intent.getExtras();
            if (bundle != null) {

                itemList = (List<JpLesson>) bundle.getSerializable("DATALIST");
                sSource = bundle.getString("SENTENCE_SOURCE");
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mode = MODE_SINGLE;
        random = new Random();
        decimalFormat = new DecimalFormat("#000");

        mediaPresenter = new MediaPresenter();
        mediaPresenter.attchView(this);
        initMediaPlayer();
        initNotification();
        initAudioManager();
        //广播接收器
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MediaService.FLAG_MUSIC_PLAY);
        registerReceiver(new MusicBroadcast(), intentFilter);
    }


    /**
     * 初始化播放器
     */
    private void initMediaPlayer() {

        mPlayer = new MediaPlayer();
        mPlayer.setLooping(false);
        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

                mPlayer.start();
                beginTime = DateUtil.getCurTime();
            }
        });
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

                sendPlayCurrent(1);
                if (mode == MODE_LIST) {//列表播放

                    mPlayer.reset();
                    position++;
                    if (position >= itemList.size()) {//防止越界

                        position = 0;
                    }
                    //本地地址
                    String path = getExternalFilesDir(Environment.DIRECTORY_MUSIC)
                            .getAbsolutePath()
                            + itemList.get(position).getSound().replace("http://static2.iyuba.cn", "");
                    String soundUrl = null;
                    if (new File(path).exists()) {

                        soundUrl = path;
                    } else {

                        soundUrl = itemList.get(position).getSound();
                    }
                    try {
                        mPlayer.setDataSource(soundUrl);
                        mPlayer.prepareAsync();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (mode == MODE_SINGLE) {//单曲循环

                    mPlayer.reset();
                    //本地地址
                    String path = getExternalFilesDir(Environment.DIRECTORY_MUSIC)
                            .getAbsolutePath()
                            + itemList.get(position).getSound().replace("http://static2.iyuba.cn", "");
                    String soundUrl = null;
                    if (new File(path).exists()) {

                        soundUrl = path;
                    } else {

                        soundUrl = itemList.get(position).getSound();
                    }
                    try {
                        mPlayer.setDataSource(soundUrl);
                        mPlayer.prepareAsync();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {//MODE_RANDOM

                    int rInt = random.nextInt(itemList.size());
                    position = rInt;
                    //本地地址
                    String path = getExternalFilesDir(Environment.DIRECTORY_MUSIC)
                            .getAbsolutePath()
                            + itemList.get(position).getSound().replace("http://static2.iyuba.cn", "");
                    String soundUrl = null;
                    if (new File(path).exists()) {

                        soundUrl = path;
                    } else {

                        soundUrl = itemList.get(position).getSound();
                    }
                    try {
                        mPlayer.reset();
                        mPlayer.setDataSource(soundUrl);
                        mPlayer.prepareAsync();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                //通知
                if (remoteViews != null) {

                    JpLesson jpLesson = itemList.get(position);
                    remoteViews.setImageViewResource(R.id.music_iv_bg, R.mipmap.ic_launcher);
                    remoteViews.setTextViewText(R.id.music_tv_title, jpLesson.getLesson());
                    startForeground(1, notification);
                }

                //切换音频数据源
                sendBroadcast(new Intent(FLAG_SWITCH_DATA));
//                if (callback != null) {//切换新闻就调用
//
//                    callback.switchData(itemList.get(position), position);
//                }
            }
        });
        mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return false;
            }
        });
    }


    /**
     * 初始化通知
     */
    private void initNotification() {


        Intent intent = new Intent(MediaService.FLAG_MUSIC_PLAY);

        PendingIntent pendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {

            pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        } else {

            pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        remoteViews = new RemoteViews(this.getPackageName(), R.layout.notification_music);
        remoteViews.setOnClickPendingIntent(R.id.music_iv_but, pendingIntent);

        nManagerCompat = NotificationManagerCompat.from(this);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {//android8.0及以上的通道

            NotificationChannel notificationChannel = new NotificationChannel("MediaServiceChannel", "MediaServiceChannel", NotificationManager.IMPORTANCE_DEFAULT);
            nManagerCompat.createNotificationChannel(notificationChannel);
        }
        notification = new NotificationCompat.Builder(getApplicationContext(), "MediaServiceChannel")
                .setCustomContentView(remoteViews)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setDefaults(NotificationCompat.DEFAULT_LIGHTS)
                .build();

        startForeground(1, notification);
    }

    /**
     * 初始化音频焦点，用来处理串音的问题
     */
    private void initAudioManager() {

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int flag;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//8.0以上处理

            audioFocusRequest
                    = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                    .setAudioAttributes(new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA).build())
                    .setOnAudioFocusChangeListener(onAudioFocusChangeListener)
                    .build();
            flag = audioManager.requestAudioFocus(audioFocusRequest);
        } else {

            flag = audioManager.requestAudioFocus(onAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        }
        if (flag == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {

            Log.d(TAG, "音频焦点获取成功");
        } else {

            Log.d(TAG, "音频焦点获取失败");
        }
    }

    AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {

            Log.d(TAG, focusChange + "");
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_GAIN:
                    //当其他应用申请焦点之后又释放焦点会触发此回调
                    //可重新播放音乐
                    Log.d(TAG, "AUDIOFOCUS_GAIN");

                    /*if (mPlayer != null) {

                        mPlayer.start();
                        remoteViews.setImageViewResource(R.id.music_iv_but, R.mipmap.icon_home_pause);
                        startForeground(1, notification);
                    }*/
                    break;
                case AudioManager.AUDIOFOCUS_LOSS:
                    //长时间丢失焦点,当其他应用申请的焦点为 AUDIOFOCUS_GAIN 时，
                    //会触发此回调事件，例如播放 QQ 音乐，网易云音乐等
                    //通常需要暂停音乐播放，若没有暂停播放就会出现和其他音乐同时输出声音
                    Log.d(TAG, "AUDIOFOCUS_LOSS");
                    if (mPlayer != null && mPlayer.isPlaying()) {

                        mPlayer.pause();
                        sendPlayCurrent(0);
                        if (remoteViews != null) {

                            stopForeground(false);
                            remoteViews.setImageViewResource(R.id.music_iv_but, R.mipmap.icon_home_play);
                            nManagerCompat.notify(1, notification);
                        }
                    }
                    //释放焦点，该方法可根据需要来决定是否调用
                    //若焦点释放掉之后，将不会再自动获得
//                        mAudioManager.abandonAudioFocus(mAudioFocusChange);
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    //短暂性丢失焦点，当其他应用申请 AUDIOFOCUS_GAIN_TRANSIENT 或AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE 时，
                    //会触发此回调事件，例如播放短视频，拨打电话等。
                    //通常需要暂停音乐播放
                    if (mPlayer != null) {

                        mPlayer.pause();
                        sendPlayCurrent(0);
                        if (remoteViews != null) {

                            stopForeground(false);
                            remoteViews.setImageViewResource(R.id.music_iv_but, R.mipmap.icon_home_play);
                            nManagerCompat.notify(1, notification);
                        }
                    }
                    Log.d(TAG, "AUDIOFOCUS_LOSS_TRANSIENT");
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    //短暂性丢失焦点并作降音处理
                    Log.d(TAG, "AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK");
                    break;
            }
        }
    };


    /**
     * 将播放进度发送到服务器
     * endflag 0暂停  1 播放完成
     */
    private void sendPlayCurrent(int endflag) {

        String uid = "0";
        if (Constant.userinfo != null) {

            uid = Constant.userinfo.getUid() + "";
        }
        if (!uid.equals("0")) {

            int currentPosition;
            if (endflag == 0) {//是否播放完成

                currentPosition = mPlayer.getCurrentPosition();
            } else {

                currentPosition = mPlayer.getDuration();
            }
            JpLesson jpLesson = itemList.get(position);
            int id = Integer.parseInt(jpLesson.getLessonID());
            String sign = MD5.getMD5ofStr(uid + beginTime + DateUtil.getCurTime());


            //获取book的name
            String bookname = "jp3";
            List<Book> books = LitePal.where("source = ?", jpLesson.getSource()).find(Book.class);
            if (books.size() > 0) {

                bookname = books.get(0).getName();
            }

            //计算当前播放到第几句
            decimalFormat.applyPattern("#000");
            String sourceid = decimalFormat.format(Integer.parseInt(jpLesson.getSource()));
            int index = 0;
            List<Sentence> sentenceList = LitePal.where("sourceid = ? and source = ? and starttime is not null", sourceid, bookname).find(Sentence.class);
            for (int i = 0; i < sentenceList.size(); i++) {

                Sentence sentence = sentenceList.get(i);
                int sTime = (int) (Double.parseDouble(sentence.getStartTime()) * 1000);
                int eTime = (int) (Double.parseDouble(sentence.getEndTime()) * 1000);
                if (currentPosition >= sTime && currentPosition <= eTime) {

                    index = i;
                    break;
                }
            }
            String endTime = DateUtil.getCurTime();

            List<Book> bookList = LitePal.where("source = ?", jpLesson.getSource()).find(Book.class);
            Book book = null;
            if (bookList.size() > 0) {
                book = bookList.get(0);
            }
            if (book != null) {

                mediaPresenter.updateStudyRecordNew("json", uid, beginTime, endTime, book.getName(),
                        "1", 0 + "", "android", Constant.APPID + "", "",
                        id + "", sign, endflag, index, book);
            }
        }
    }


    /**
     * 任意一次unbindService()方法，都会触发这个方法
     * 用于释放一些绑定时使用的资源
     *
     * @param intent
     * @return
     */
    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {

        /**
         * 释放音频焦点
         */
        if (audioManager != null) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                audioManager.abandonAudioFocusRequest(audioFocusRequest);
            } else {

                audioManager.abandonAudioFocus(onAudioFocusChangeListener);
            }
        }

        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.stop();
            mPlayer.release();
        }
        mPlayer = null;
        super.onDestroy();

        if (mediaPresenter != null) {

            mediaPresenter.detachView();
        }
    }

    /**
     * 用来接收通知的点击事件
     */
    class MusicBroadcast extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (action.equals(MediaService.FLAG_MUSIC_PLAY)) {

                if (mPlayer == null) {

                    return;
                }
                if (mPlayer.isPlaying()) {

                    mPlayer.pause();
                    sendPlayCurrent(0);
                    if (remoteViews != null) {

                        remoteViews.setImageViewResource(R.id.music_iv_but, R.mipmap.icon_home_play);
                        stopForeground(false);//停止前台通知，适用普通的通知便于划走
                        nManagerCompat.notify(1, notification);
                    }
                } else {

                    mPlayer.start();
                    beginTime = DateUtil.getCurTime();
                    if (remoteViews != null) {

                        remoteViews.setImageViewResource(R.id.music_iv_but, R.mipmap.icon_home_pause);
                        startForeground(1, notification);
                    }
                }

                sendBroadcast(new Intent(FLAG_MEDIA_BROADCAST));
            }
        }
    }
}