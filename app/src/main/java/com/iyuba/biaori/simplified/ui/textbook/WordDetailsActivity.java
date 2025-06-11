package com.iyuba.biaori.simplified.ui.textbook;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.iyuba.biaori.simplified.Constant;
import com.iyuba.biaori.simplified.MyApplication;
import com.iyuba.biaori.simplified.R;
import com.iyuba.biaori.simplified.databinding.ActivityWordDetailsBinding;
import com.iyuba.biaori.simplified.db.JpWord;
import com.iyuba.biaori.simplified.entity.MediaPauseEventbus;
import com.iyuba.biaori.simplified.model.bean.textbook.EvaluteRecordBean;
import com.iyuba.biaori.simplified.model.bean.textbook.JpWordSentenceBean;
import com.iyuba.biaori.simplified.presenter.textbook.WordDetailsPresenter;
import com.iyuba.biaori.simplified.ui.BaseActivity;
import com.iyuba.biaori.simplified.ui.login.LoginActivity;
import com.iyuba.biaori.simplified.ui.login.WxLoginActivity;
import com.iyuba.biaori.simplified.util.popup.PermissionPopup;
import com.iyuba.biaori.simplified.view.textbook.WordDetailsContract;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * 单词详情
 */
public class WordDetailsActivity extends BaseActivity<WordDetailsContract.WordDetailsView, WordDetailsContract.WordDetailsPresenter>
        implements WordDetailsContract.WordDetailsView {

    private String TAG = "WordDetailsActivity";

    private ActivityWordDetailsBinding activityWordDetailsBinding;

    /**
     * 从上一个页面接收的单词列表
     */
    private List<JpWord> dataDTOList;
    //bundle 单词在列表中的位置
    private int position = 0;

    private MediaPlayer player;

    /**
     * 是否显示句子
     */
    private boolean isShowSentence = false;

    /**
     * 听原音的动画
     */
    private AnimationDrawable animation_original;

    /**
     * 上面喇叭的动画
     */
    private AnimationDrawable animation_top_original;

    /**
     * 听跟读的动画
     */
    private AnimationDrawable animation_follow;

    /**
     * 播放的是哪个音频，动画就作用在哪个控件上
     * 1：上面的喇叭
     * 2：听原音
     * 3: 听跟读
     * 0：自动播放单词音频
     */
    private int flag_play = 0;

    /**
     * 动画set1
     */
    private AnimationSet set1;

    private File saveFile;

    private RxPermissions rxPermissions;

    /**
     * 存储权限是否被授权
     */
    private SharedPreferences sp;

    private MediaRecorder mediaRecorder;
    /**
     * 是否正在录制声音
     */
    private boolean isRecord = false;

    /**
     * 是否自动播放语音
     */
    private boolean isAutoPlay = false;

    /**
     * 获取音频焦点
     */
    private AudioManager audioManager;

    /**
     * android8.0以上的音频焦点
     */
    private AudioFocusRequest audioFocusRequest;

    private PermissionPopup permissionPopup;


    AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {

            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_GAIN:
                    //当其他应用申请焦点之后又释放焦点会触发此回调
                    //可重新播放音乐
                    Log.d(TAG, "AUDIOFOCUS_GAIN");
//                        start();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS:
                    //长时间丢失焦点,当其他应用申请的焦点为 AUDIOFOCUS_GAIN 时，
                    //会触发此回调事件，例如播放 QQ 音乐，网易云音乐等
                    //通常需要暂停音乐播放，若没有暂停播放就会出现和其他音乐同时输出声音
                    Log.d(TAG, "AUDIOFOCUS_LOSS");
//                        stop();
                    //释放焦点，该方法可根据需要来决定是否调用
                    //若焦点释放掉之后，将不会再自动获得
//                        mAudioManager.abandonAudioFocus(mAudioFocusChange);
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    //短暂性丢失焦点，当其他应用申请 AUDIOFOCUS_GAIN_TRANSIENT 或AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE 时，
                    //会触发此回调事件，例如播放短视频，拨打电话等。
                    //通常需要暂停音乐播放
                    if (player != null) {

                        player.pause();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        getBundle();
        initMediaPlayer();
        initOperation();
        showWord();
        initAnim1();
    }

    /**
     * 初始化动画
     */
    private void initAnim1() {

        set1 = new AnimationSet(true);
        //缩放动画，以中心从原始放大到1.3倍
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 1.3f, 1.0f, 1.3f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        //渐变动画
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.5f);
        scaleAnimation.setDuration(800);
        scaleAnimation.setRepeatCount(Animation.INFINITE);
        alphaAnimation.setRepeatCount(Animation.INFINITE);
        set1.setDuration(800);
        set1.addAnimation(scaleAnimation);
        set1.addAnimation(alphaAnimation);
    }

    /**
     * 初始化音频播放器
     */
    private void initMediaPlayer() {

        player = new MediaPlayer();
        player.setLooping(false);
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

                stopAnimation();
                player.start();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                    audioFocusRequest
                            = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)
                            .setAudioAttributes(new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA).build())
                            .setOnAudioFocusChangeListener(onAudioFocusChangeListener)
                            .build();
                    audioManager.requestAudioFocus(audioFocusRequest);
                } else {
                    audioManager.requestAudioFocus(onAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                }


                if (flag_play == 1) {

                    animation_top_original = (AnimationDrawable) activityWordDetailsBinding.wdIvAudio.getDrawable();
                    animation_top_original.start();
                } else if (flag_play == 2) {

                    animation_original = (AnimationDrawable) activityWordDetailsBinding.wdIvListen.getDrawable();
                    animation_original.start();
                } else if (flag_play == 3) {

                    animation_follow = (AnimationDrawable) activityWordDetailsBinding.wdIvFollow.getDrawable();
                    animation_follow.start();
                }
            }
        });
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                    audioManager.requestAudioFocus(audioFocusRequest);
                } else {

                    audioManager.abandonAudioFocus(onAudioFocusChangeListener);
                }

                stopAnimation();
            }
        });
    }

    /**
     * 停止动画
     */
    private void stopAnimation() {

        if (animation_original != null) {
            animation_original.selectDrawable(0);
            animation_original.stop();
        }
        if (animation_top_original != null) {
            animation_top_original.selectDrawable(0);
            animation_top_original.stop();
        }
        if (animation_follow != null) {

            animation_follow.selectDrawable(0);
            animation_follow.stop();
        }
    }

    /**
     * 播放音频文件
     *
     * @param urlStr 地址
     */
    private void play(String urlStr) {

        EventBus.getDefault().post(new MediaPauseEventbus());
        try {
            player.reset();
            player.setDataSource(urlStr);
            player.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化点击事件
     */
    private void initOperation() {


        sp = getSharedPreferences(Constant.SP_PERMISSION, MODE_PRIVATE);
        mediaRecorder = new MediaRecorder();

        activityWordDetailsBinding.toolbar.toolbarSwitchRight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    isAutoPlay = true;
                } else {

                    isAutoPlay = false;
                }
            }
        });

        activityWordDetailsBinding.toolbar.toolbarIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        activityWordDetailsBinding.toolbar.toolbarIvTitle.setText("单词详情");
        //上面的喇叭
        activityWordDetailsBinding.wdIvAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dataDTOList.size() == 0) {

                    return;
                }
                flag_play = 1;
                JpWord dataDTO = dataDTOList.get(position);

                String urlStr;
                if (dataDTO.getSound().startsWith("http")) {

                    urlStr = dataDTO.getSound();
                } else {

                    urlStr = Constant.URL_STATIC2 + "/Japan/" + dataDTO.getSource() + "/word/" + dataDTO.getSound().split("_")[0]
                            + "/"
                            + dataDTO.getSound() + ".mp3";
                }

                play(urlStr);
            }
        });
        //切换单词和句子
        activityWordDetailsBinding.wdIvSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dataDTOList.size() == 0) {

                    return;
                }
                JpWord dataDTO = dataDTOList.get(position);
                if (isShowSentence) {//

                    activityWordDetailsBinding.wdTvWord.setText(dataDTO.getWord());
                    activityWordDetailsBinding.wdTvWordchBottom.setText(dataDTO.getWordCh());
                    isShowSentence = false;
                } else {

                    activityWordDetailsBinding.wdTvWord.setText(dataDTO.getSentence());
                    activityWordDetailsBinding.wdTvWordchBottom.setText(dataDTO.getSentenceCh());
                    isShowSentence = true;
                }
            }
        });
        //上一个
        activityWordDetailsBinding.wdButPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dataDTOList.size() == 0) {

                    return;
                }
                position--;
                if (position < 0) {

                    position = 0;
                    return;
                }
                flag_play = 0;
                showWord();
            }
        });
        //下一个
        activityWordDetailsBinding.wdButNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dataDTOList.size() == 0) {

                    return;
                }
                position++;
                if (position >= dataDTOList.size()) {
                    position = dataDTOList.size() - 1;
                    return;
                }
                flag_play = 0;//自动播放时不触发动画
                showWord();
            }
        });
        //听原音
        activityWordDetailsBinding.wdLlListen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dataDTOList.size() == 0) {

                    return;
                }

                flag_play = 2;
                JpWord dataDTO = dataDTOList.get(position);
                if (isShowSentence) {//显示句子

                    String uriStr;
                    if (dataDTO.getSound().startsWith("http")) {//一种是单词列表的数据 另一种是收藏单词的数据

                        uriStr = dataDTO.getSentenceSound();
                    } else {

                        uriStr = Constant.URL_STATIC2 + "/Japan/" + dataDTO.getSource() + "/wordExamples/"
                                + dataDTO.getSound().split("_")[0]
                                + "/"
                                + dataDTO.getSound()
                                + ".mp3";
                    }
                    play(uriStr);

                } else {

                    String urlStr;
                    if (dataDTO.getSound().startsWith("http")) {

                        urlStr = dataDTO.getSound();
                    } else {

                        urlStr = Constant.URL_STATIC2 + "/Japan/" + dataDTO.getSource() + "/word/" + dataDTO.getSound().split("_")[0]
                                + "/"
                                + dataDTO.getSound() + ".mp3";
                    }
                    play(urlStr);
                }
            }
        });
        //点击评测
        activityWordDetailsBinding.wdLlTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requestRecord();
            }
        });
        //听跟读
        activityWordDetailsBinding.wdLlFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JpWord dataDTO = dataDTOList.get(position);
                if (dataDTO.getUrl() == null) {

                    return;
                }

                flag_play = 3;
                String uriStr = Constant.URL_IUSERSPEECH + "/voa/" + dataDTO.getUrl();
                play(uriStr);
            }
        });
        //收藏
        activityWordDetailsBinding.wdIbCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                collect();
            }
        });
    }

    /**
     * 收藏功能
     */
    private void collect() {

        if (Constant.userinfo == null) {

            startActivity(new Intent(WordDetailsActivity.this, WxLoginActivity.class));
            toast("请登录");
            return;
        }
        JpWord word = dataDTOList.get(position);
        if (word.getCollect() == 1) {

            presenter.updateWordCollect(Constant.userinfo.getUid() + "", word.getId() + "",
                    "del", Constant.APPID + "");
        } else {

            presenter.updateWordCollect(Constant.userinfo.getUid() + "", word.getId() + "",
                    "add", Constant.APPID + "");
        }
    }


    private void getBundle() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            dataDTOList = (List<JpWord>) bundle.getSerializable("WORD_LIST");
            position = bundle.getInt("POSITION", 0);
        }
    }

    /**
     * @param activity
     * @param dataDTOList 单词列表
     */
    public static void startActivity(Activity activity, List<JpWord> dataDTOList, int position) {

        Intent intent = new Intent(activity, WordDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("WORD_LIST", (Serializable) dataDTOList);
        bundle.putInt("POSITION", position);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }


    @Override
    public View initLayout() {
        activityWordDetailsBinding = ActivityWordDetailsBinding.inflate(getLayoutInflater());
        return activityWordDetailsBinding.getRoot();
    }

    @Override
    public WordDetailsContract.WordDetailsPresenter initPresenter() {
        return new WordDetailsPresenter();
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

    /**
     * 请求权限
     */
    @SuppressLint("CheckResult")
    private void requestRecord() {

        if (rxPermissions == null) {
            rxPermissions = new RxPermissions(WordDetailsActivity.this);
        }
        if (rxPermissions.isGranted(Manifest.permission.RECORD_AUDIO)) {//授权了

            recordAudio();
        } else {
            int state = sp.getInt(Constant.SP_KEY_PERMISSION_RECORD, 0);
            if (state == 0) {

                initPermissionPopup("评测将要请求录音权限！");

            } else {

                toast("你禁止了录音权限，请在权限管理中打开");
            }
        }
    }


    /**
     * 录制音频
     */
    private void recordAudio() {

        EventBus.getDefault().post(new MediaPauseEventbus());

        if (isRecord) {

            isRecord = false;
            mediaRecorder.stop();
            activityWordDetailsBinding.wdIvTestAnim.clearAnimation();

            JpWord dataDTO = dataDTOList.get(position);
            presenter.jtest(Constant.userinfo == null ? 0 : Constant.userinfo.getUid(), Integer.parseInt(dataDTO.getIdindex()),
                    isShowSentence ? 1 : 0, Integer.parseInt(dataDTO.getSourceid()), getType(Integer.parseInt(dataDTO.getLevel())),
                    saveFile.getAbsolutePath(), isShowSentence ? dataDTO.getSentence() : dataDTO.getWord());
        } else {

            isRecord = true;
            activityWordDetailsBinding.wdIvTestAnim.startAnimation(set1);
            //获取音乐目录
            saveFile = new File(getExternalFilesDir(Environment.DIRECTORY_MUSIC), System.currentTimeMillis() + ".mp3");
            if (!saveFile.getParentFile().exists()) {
                saveFile.getParentFile().mkdirs();
            }
            mediaRecorder.reset();
            mediaRecorder.setOutputFile(saveFile.getAbsolutePath());
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            try {
                mediaRecorder.prepare();
                mediaRecorder.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 从收藏进入此页面不成立
     *
     * @return
     */
    private String getType(int level) {

        String newsId;
        if (isShowSentence) {
            //短句
            newsId = Constant.bookDataDTO.getName() + "ShortSentence";
        } else {

            if (level < 6) {

                newsId = "n" + Constant.bookDataDTO.getLevel();
            } else {

                newsId = Constant.bookDataDTO.getName() + "word";
            }

        }
        return newsId;
    }

    /**
     * 显示单词
     */
    public void showWord() {

        if (dataDTOList.size() == 0) {

            return;
        }
        JpWord dataDTO = dataDTOList.get(position);

        //获取最新数据，收藏标志会变化
        List<JpWord> jpWordList = LitePal.where("wordid = ?", dataDTO.getId() + "").find(JpWord.class);
        if (jpWordList.size() != 0) {

            jpWordList.get(0).setUrl(dataDTO.getUrl());
            jpWordList.get(0).setTotal_score(dataDTO.getTotal_score());

            dataDTO = jpWordList.get(0);
            dataDTOList.set(position, jpWordList.get(0));
        }

        //top上班部分  日语单词
        activityWordDetailsBinding.wdTvWordTop.setText(dataDTO.getWord());
        //pron
        if (dataDTO.getPron() == null) {

            activityWordDetailsBinding.wdTvPron.setText("");
        } else {
            activityWordDetailsBinding.wdTvPron.setText("[" + dataDTO.getPron() + "]");
        }
        //中文解释
        if (dataDTO.getSpeech() == null) {//词性是否为空

            activityWordDetailsBinding.wdTvWordch.setText(dataDTO.getWordCh());
        } else {

            activityWordDetailsBinding.wdTvWordch.setText("【" + dataDTO.getSpeech() + "】" + " " + dataDTO.getWordCh());
        }
        //是否收藏
        if (dataDTO.getCollect() == 1) {

            activityWordDetailsBinding.wdIbCollect.setImageResource(R.mipmap.ic_collected);
        } else {

            activityWordDetailsBinding.wdIbCollect.setImageResource(R.mipmap.ic_uncollect);
        }

        //下半部分  日语单词或者句子
        if (isShowSentence) {//显示句子

            activityWordDetailsBinding.wdTvWord.setText(dataDTO.getSentence());
            //中文句子或者中文单词
            activityWordDetailsBinding.wdTvWordchBottom.setText(dataDTO.getSentenceCh());
        } else {//显示单词
            activityWordDetailsBinding.wdTvWord.setText(dataDTO.getWord());
            //中文句子或者中文单词
            activityWordDetailsBinding.wdTvWordchBottom.setText(dataDTO.getWordCh());
        }
        //单词下标
        activityWordDetailsBinding.wdTvIndex.setText((position + 1) + "/" + dataDTOList.size());
        //是否自动播放
        if (isAutoPlay) {

            String urlStr;
            if (dataDTO.getSound().startsWith("http")) {

                urlStr = dataDTO.getSound();
            } else {

                urlStr = Constant.URL_STATIC2 + "/Japan/" + dataDTO.getSource() + "/word/" + dataDTO.getSound().split("_")[0]
                        + "/"
                        + dataDTO.getSound() + ".mp3";
            }
            play(urlStr);
        }
        //评测前
        if (dataDTO.getUrl() == null) {

            activityWordDetailsBinding.wdLlFollow.setVisibility(View.INVISIBLE);
            activityWordDetailsBinding.wdIvScore.setVisibility(View.INVISIBLE);
            activityWordDetailsBinding.wdLlScore.setVisibility(View.INVISIBLE);
            activityWordDetailsBinding.wdTvLowscore.setVisibility(View.INVISIBLE);

        } else {//评测后

            activityWordDetailsBinding.wdLlFollow.setVisibility(View.VISIBLE);

            double scoreD = Double.parseDouble(dataDTO.getTotal_score()) * 20.0;
            if (scoreD < 60) {//低于60分

                activityWordDetailsBinding.wdIvScore.setVisibility(View.VISIBLE);
                activityWordDetailsBinding.wdTvLowscore.setVisibility(View.VISIBLE);
                activityWordDetailsBinding.wdLlScore.setVisibility(View.INVISIBLE);
            } else {

                activityWordDetailsBinding.wdIvScore.setVisibility(View.INVISIBLE);
                activityWordDetailsBinding.wdTvLowscore.setVisibility(View.INVISIBLE);
                activityWordDetailsBinding.wdLlScore.setVisibility(View.VISIBLE);
                activityWordDetailsBinding.wdTvScore.setText(scoreD + "");
            }
        }
    }

    @Override
    public void jtestComplete(EvaluteRecordBean evaluteRecordBean) {

        //记录评测后获得的音频链接及得分
        JpWord dataDTO = dataDTOList.get(position);
        dataDTO.setUrl(evaluteRecordBean.getData().getUrl());
        dataDTO.setTotal_score(evaluteRecordBean.getData().getTotalScore());
        showWord();
    }

    @Override
    public void updateCollectComplete(String wordid, String type) {


        JpWord collectWord = null;
        for (int i = 0; i < dataDTOList.size(); i++) {

            JpWord word = dataDTOList.get(i);
            if ((word.getId() + "").equals(wordid)) {

                collectWord = word;
                break;
            }
        }
        if (collectWord != null) {

            if (type.equals("add")) {

                collectWord.setCollect(1);
            } else {

                collectWord.setToDefault("collect");
                collectWord.setCollect(0);
            }

            List<JpWord> jpWordList = LitePal.where("wordid = ?", collectWord.getId() + "").find(JpWord.class);
            if (jpWordList.size() == 0) {//数据库没有此数据

                collectWord.save();
            } else {//有此数据更新数据库

                collectWord.updateAll("wordid = ?", collectWord.getId() + "");
            }
        }
        //更新界面
        JpWord curWord = dataDTOList.get(position);
        if (curWord == collectWord) {

            if (type.equals("add")) {

                activityWordDetailsBinding.wdIbCollect.setImageResource(R.mipmap.ic_collected);
            } else {

                activityWordDetailsBinding.wdIbCollect.setImageResource(R.mipmap.ic_uncollect);
            }
        }
    }

    /**
     * 权限提醒
     *
     * @param content
     */
    private void initPermissionPopup(String content) {

        if (permissionPopup == null) {

            permissionPopup = new PermissionPopup(WordDetailsActivity.this);
            permissionPopup.setCallback(new PermissionPopup.Callback() {
                @SuppressLint("CheckResult")
                @Override
                public void clickOk() {

                    if (rxPermissions == null) {

                        rxPermissions = new RxPermissions(WordDetailsActivity.this);
                    }
                    rxPermissions.request(Manifest.permission.RECORD_AUDIO)
                            .subscribe(new Consumer<Boolean>() {
                                @Override
                                public void accept(Boolean aBoolean) throws Exception {

                                    if (aBoolean) {

                                        recordAudio();
                                    } else {

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
}