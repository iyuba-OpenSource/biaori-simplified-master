package com.iyuba.biaori.simplified.ui.fragment.textbook;

import static android.content.Context.MODE_PRIVATE;
import static android.os.Build.VERSION_CODES.BASE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.iyuba.biaori.simplified.Constant;
import com.iyuba.biaori.simplified.MyApplication;
import com.iyuba.biaori.simplified.R;
import com.iyuba.biaori.simplified.adapter.textbook.EvaluatingAdapter;
import com.iyuba.biaori.simplified.databinding.FragmentEvaluatingBinding;
import com.iyuba.biaori.simplified.db.Book;
import com.iyuba.biaori.simplified.db.JpLesson;
import com.iyuba.biaori.simplified.db.Sentence;
import com.iyuba.biaori.simplified.entity.MediaPauseEventbus;
import com.iyuba.biaori.simplified.model.bean.textbook.EvaluteRecordBean;
import com.iyuba.biaori.simplified.model.bean.textbook.MergeBean;
import com.iyuba.biaori.simplified.model.bean.textbook.PublishEvalBean;
import com.iyuba.biaori.simplified.presenter.textbook.EvaluatingPresenter;
import com.iyuba.biaori.simplified.ui.BaseFragment;
import com.iyuba.biaori.simplified.ui.login.LoginActivity;
import com.iyuba.biaori.simplified.ui.login.WxLoginActivity;
import com.iyuba.biaori.simplified.ui.textbook.WordDetailsActivity;
import com.iyuba.biaori.simplified.ui.vip.VipActivity;
import com.iyuba.biaori.simplified.util.LineItemDecoration;
import com.iyuba.biaori.simplified.util.popup.PermissionPopup;
import com.iyuba.biaori.simplified.view.textbook.EvaluatingContract;
import com.mob.MobSDK;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 评测页面
 */
public class EvaluatingFragment extends BaseFragment<EvaluatingContract.EvaluatingView, EvaluatingContract.EvaluatingPresenter>
        implements EvaluatingContract.EvaluatingView {


    /**
     * 课文id
     */
    private int lessonid;
    /**
     * 课本source
     */
    private int source;


    private FragmentEvaluatingBinding binding;

    private EvaluatingAdapter evaluatingAdapter;

    private MediaPlayer mediaPlayer;

    private MediaRecorder mediaRecorder;

    /**
     * 权限请求
     */
    private RxPermissions rxPermissions;

    private SharedPreferences pSP;

    private String bookName = "jp3";

    private MergeBean mergeBean;

    private DecimalFormat decimalFormat;

    /**
     * 记录评测次数
     */
    private SharedPreferences evalSP;

    /**
     * 存储评测次数的文件
     */
    private String SP_NAME = "EVAL";

    /**
     * 权限弹窗
     */
    private PermissionPopup permissionPopup;

    public EvaluatingFragment() {
    }

    public static EvaluatingFragment newInstance(int lessonid, int source) {
        EvaluatingFragment fragment = new EvaluatingFragment();
        Bundle args = new Bundle();
        args.putInt("LESSON_ID", lessonid);
        args.putInt("SOURCE", source);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            lessonid = getArguments().getInt("LESSON_ID");
            source = getArguments().getInt("SOURCE");
        }

        evalSP = MyApplication.getContext().getSharedPreferences(SP_NAME, MODE_PRIVATE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();


        if (mediaPlayer != null) {

            mediaPlayer.release();
        }

        if (mediaRecorder != null) {

            mediaRecorder.release();
        }

        if (recordHandler != null) {
            recordHandler.removeMessages(1);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initOperation();

        if (decimalFormat == null) {

            decimalFormat = new DecimalFormat("#00");
        }

        pSP = view.getContext().getSharedPreferences(Constant.SP_PERMISSIONS, MODE_PRIVATE);

        List<Book> bookList = LitePal.where("source = ?", source + "").find(Book.class);

        if (bookList.size() != 0) {

            bookName = bookList.get(0).getName();
        }

        //格式化id
        DecimalFormat decimalFormat = new DecimalFormat("#000");
        String lessonIDStr = decimalFormat.format(lessonid);

        LineItemDecoration lineItemDecoration = new LineItemDecoration(requireContext(), LinearLayoutManager.VERTICAL);
        lineItemDecoration.setDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.space_10dp, requireActivity().getTheme()));
        if (binding.evaluatingTvList.getItemDecorationCount() == 0) {

            binding.evaluatingTvList.addItemDecoration(lineItemDecoration);
        }


        List<Sentence> sentenceList = LitePal.where("source = ? and sourceid = ? and starttime is not  null", bookName, lessonIDStr).find(Sentence.class);
        binding.evaluatingTvList.setLayoutManager(new LinearLayoutManager(view.getContext()));
        evaluatingAdapter = new EvaluatingAdapter(R.layout.item_evaluating, sentenceList);
        binding.evaluatingTvList.setHasFixedSize(true);
        binding.evaluatingTvList.setItemAnimator(null);
        binding.evaluatingTvList.setAdapter(evaluatingAdapter);
        evaluatingAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                int id = view.getId();
                dealClick(id, position);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mediaRecorder != null) {

            mediaRecorder.reset();
        }
        if (evaluatingAdapter != null) {

            evaluatingAdapter.setPlay(false);
            evaluatingAdapter.setRecord(false);
            evaluatingAdapter.notifyItemChanged(evaluatingAdapter.getPosition());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void initOperation() {

        //合成
        binding.evaluatingTvMerge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mergeStr = binding.evaluatingTvMerge.getText().toString();
                if (mergeStr.equals("合成")) {

                    StringBuilder stringBuilder = new StringBuilder();
                    int recordCount = 0;
                    List<Sentence> sentenceList = evaluatingAdapter.getData();
                    for (int i = 0; i < sentenceList.size(); i++) {

                        Sentence sentence = sentenceList.get(i);
                        if (sentence.getRecordSoundUrl() != null) {

                            recordCount++;
                            stringBuilder.append(sentence.getRecordSoundUrl() + ",");
                        }
                    }
                    if (recordCount < 2) {

                        toast("至少两句才能合成");
                        return;
                    }

                    stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());


                    presenter.merge(stringBuilder.toString(), bookName);
                } else if (mergeStr.equals("听")) {

                    EventBus.getDefault().post(new MediaPauseEventbus());
                    evaluatingAdapter.setFlag(3);
                    initMediaPlayer(mergeBean, null, -1);
                } else if (mergeStr.equals("停")) {//播放过程中点击

                    if (mediaPlayer != null) {

                        mediaPlayer.pause();
                    }
                    binding.evaluatingTvMerge.setText("听");
                }
            }
        });
        //发布合成音频
        binding.evaluatingTvPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mergeBean == null) {

                    toast("请先合成音频");
                    return;
                }
                if (Constant.userinfo == null) {

                    toast("请登录");
                    startActivity(new Intent(requireActivity(), WxLoginActivity.class));
                    return;
                }

                List<Book> bookList = LitePal.where("source = ?", source + "").find(Book.class);
                String bookName = "jp3";
                if (bookList.size() != 0) {

                    bookName = bookList.get(0).getName();
                }

                String uid = "0";
                String name = "";
                if (Constant.userinfo != null) {

                    uid = Constant.userinfo.getUid() + "";
                    name = Constant.userinfo.getUsername();
                }

                int totalScore = 0;
                int count = 0;
                List<Sentence> sentenceList = evaluatingAdapter.getData();
                for (int i = 0; i < sentenceList.size(); i++) {

                    Sentence sentence = sentenceList.get(i);
                    if (sentence.getScore() != null) {

                        totalScore = (int) (totalScore + Double.parseDouble(sentence.getScore()) * 20);
                        count++;
                    }
                }
                presenter.publishEval(bookName, mergeBean.getUrl(), "json", 0, 0, "android",
                        60002, totalScore / count, 4, uid, name, lessonid + "");
            }
        });
    }

    @Override
    protected View initLayout(LayoutInflater inflater, ViewGroup container) {

        binding = FragmentEvaluatingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    protected EvaluatingContract.EvaluatingPresenter initPresenter() {
        return new EvaluatingPresenter();
    }


    /**
     * 处理点击事件
     *
     * @param id
     * @param position
     */
    private void dealClick(int id, int position) {

        if (id == R.id.evaluating_iv_play) {//播放音频

            //停止后台播放，如果后台播放正在播放的话
            EventBus.getDefault().post(new MediaPauseEventbus());
            evaluatingAdapter.setFlag(1);
            playOriginal(position);
        } else if (id == R.id.evaluating_rpb_record) {

            EventBus.getDefault().post(new MediaPauseEventbus());
            requestRecord(position);
        } else if (id == R.id.evaluating_iv_play_oneself) {

            EventBus.getDefault().post(new MediaPauseEventbus());
            evaluatingAdapter.setFlag(2);
            playOriginal(position);
        } else if (id == R.id.evaluating_iv_submit) {

            publish(position);
        } else if (id == R.id.evaluating_iv_share) {

            share(position);
        }
    }

    /**
     * 分享
     */
    private void share(int position) {

        Sentence dataDTO = evaluatingAdapter.getItem(position);

        String username = "";
        if (Constant.userinfo != null) {

            username = Constant.userinfo.getUsername();
        }
        String title = username + "在日语听力语音评测中获得了" + (int) (Double.parseDouble(dataDTO.getScore()) * 20) + "分";
        String imagepath = Constant.URL_APP + "/ios/images/bignews/" + "biaori.png";
        String url = Constant.URL_VOA + "/voa/play.jsp?id=" + dataDTO.getShuoshuoId() + "&appid=" + Constant.APPID + "&apptype=biaori";

        showShare(title, "", imagepath, url, imagepath);
    }


    /**
     * 分享
     *
     * @param title
     * @param text
     * @param imagePath
     * @param url
     * @param imageUrl
     */
    private void showShare(String title, String text, String imagePath, String url, String imageUrl) {

        OnekeyShare oks = new OnekeyShare();
//        oks.addHiddenPlatform(SinaWeibo.NAME);

//        oks.setImagePath(imagePath);

        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(title);
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl(url);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(text);
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl(imageUrl);
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(url);
        oks.show(MobSDK.getContext());
        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {

            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        });
    }

    private void publish(int position) {

        if (Constant.userinfo == null) {

            startActivity(new Intent(getActivity(), WxLoginActivity.class));
            return;
        }

        List<Book> bookList = LitePal.where("source = ?", source + "").find(Book.class);
        String bookName = "jp3";
        if (bookList.size() != 0) {

            bookName = bookList.get(0).getName();
        }

        String uid = "0";
        String name = "";
        if (Constant.userinfo != null) {

            uid = Constant.userinfo.getUid() + "";
            name = Constant.userinfo.getUsername();
        }
        Sentence dataDTO = evaluatingAdapter.getItem(position);
        presenter.publishEval(bookName, dataDTO.getRecordSoundUrl(), "json", Integer.parseInt(dataDTO.getIdindex()), 1, "android",
                60002, (int) (Double.parseDouble(dataDTO.getScore()) * 20), 2, uid, name, dataDTO.getSourceid());
    }


    /**
     * 播放原音频
     */
    private void playOriginal(int position) {

        if (!evaluatingAdapter.isPlay() && !evaluatingAdapter.isRecord()) {//没有录音则可执行

            Sentence dataDTO = evaluatingAdapter.getItem(position);
            initMediaPlayer(null, dataDTO, position);
        } else if (evaluatingAdapter.isPlay()) {

            if (position == evaluatingAdapter.getPosition()) {//操作的数据正在播放

                evaluatingAdapter.setPlay(false);
                mediaPlayer.stop();
                evaluatingAdapter.notifyItemChanged(position);
            } else {//操作的数据没有播放，其他数据在播放
                Sentence dataDTO = evaluatingAdapter.getItem(position);
                int oPosition = evaluatingAdapter.getPosition();
                evaluatingAdapter.setPlay(false);
                mediaPlayer.stop();
                evaluatingAdapter.notifyItemChanged(oPosition);
                initMediaPlayer(null, dataDTO, position);
            }
        } else if (evaluatingAdapter.isRecord()) {

            evaluatingAdapter.setRecord(false);
            mediaRecorder.stop();
            evaluatingAdapter.notifyItemChanged(evaluatingAdapter.getPosition());
            Sentence dataDTO = evaluatingAdapter.getItem(position);
            initMediaPlayer(null, dataDTO, position);
        }
    }


    /**
     * 播放句子
     */
    private void initMediaPlayer(MergeBean mergeBean, Sentence titleDataDTO, int p) {

        if (mediaPlayer == null) {

            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {

                    mediaPlayer.start();
                    if (evaluatingAdapter.getFlag() != 3) {
                        evaluatingAdapter.notifyItemChanged(evaluatingAdapter.getPosition());
                    } else {//合成音频

                        binding.evaluatingTvMerge.setText("停");
                        binding.evaluatingSbProgress.setMax(mp.getDuration());
                        binding.evaluatingSbProgress.setProgress(0);
                        handler.sendEmptyMessage(1);
                    }
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {

                    if (evaluatingAdapter.getFlag() == 3) {


                        binding.evaluatingTvMerge.setText("听");
                        binding.evaluatingSbProgress.setProgress(0);
                        binding.evaluatingTvCtime.setText("00:00");
                    } else {

                        //完成则更改播放状态
                        Sentence dataDTO = evaluatingAdapter.getItem(evaluatingAdapter.getPosition());
                        if (dataDTO != null) {

                            evaluatingAdapter.setPlay(false);
                            evaluatingAdapter.notifyItemChanged(evaluatingAdapter.getPosition());
                        }
                    }
                }
            });
        }

        int flag = evaluatingAdapter.getFlag();
        if (flag == 3) {

        } else {//item的原音和录音触发

            //记录播放的数据的位置
            evaluatingAdapter.setPosition(p);//记录操作单词的数据位置
            evaluatingAdapter.setPlay(true);
        }


        mediaPlayer.reset();
        try {
            //http://static2.iyuba.cn/Japan/jp3/talk/001/001_01.mp3

            String url = null;
            if (flag == 3) {//合成音频

                url = Constant.URL_IUSERSPEECH + "/voa/" + mergeBean.getUrl();
            } else {

                if (evaluatingAdapter.getFlag() == 1) {

                    url = Constant.URL_STATIC2 + "/Japan/" + titleDataDTO.getSource() + "/talk/" + titleDataDTO.getSourceid() + "/" + titleDataDTO.getSound() + ".mp3";
                } else {//播放录音

                    url = Constant.URL_IUSERSPEECH + "/voa/" + titleDataDTO.getRecordSoundUrl();
                }
            }
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 录音
     */
    private void initRecord(Sentence titleDataDTO, int p) {

        if (mediaRecorder == null) {

            mediaRecorder = new MediaRecorder();
        }
        mediaRecorder.reset();

        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setAudioChannels(1);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setAudioSamplingRate(44100);
        mediaRecorder.setOutputFile(MyApplication.getContext().getExternalFilesDir(Environment.DIRECTORY_MUSIC).getAbsolutePath() + "/"
                + titleDataDTO.getSound() + ".mp3");
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (evaluatingAdapter != null) {

            evaluatingAdapter.setRecord(true);
            evaluatingAdapter.setPosition(p);
            evaluatingAdapter.notifyItemChanged(p);
        }
        toast("开始录音，再次点击录音结束");
        recordHandler.sendEmptyMessage(1);
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
     * 请求录音权限
     *
     * @param position
     */
    @SuppressLint("CheckResult")
    private void requestRecord(int position) {


        if (rxPermissions == null) {

            rxPermissions = new RxPermissions(requireActivity());
        }

        if (rxPermissions.isGranted(Manifest.permission.RECORD_AUDIO)) {

            record(position);
        } else {

            int record = pSP.getInt(Constant.SP_KEY_RECORD, 0);
            if (record == 0) {

                initPermissionPopup("评测需要请求录音权限！", position);
            } else {

                toast("请在应用权限管理中打开录音权限！");
            }
        }
    }


    /**
     * 权限提醒
     *
     * @param content
     */
    private void initPermissionPopup(String content, int position) {

        if (permissionPopup == null) {

            permissionPopup = new PermissionPopup(requireActivity());
            permissionPopup.setCallback(new PermissionPopup.Callback() {
                @SuppressLint("CheckResult")
                @Override
                public void clickOk() {

                    if (rxPermissions == null) {

                        rxPermissions = new RxPermissions(requireActivity());
                    }
                    rxPermissions
                            .request(Manifest.permission.RECORD_AUDIO)
                            .subscribe(new Consumer<Boolean>() {
                                @Override
                                public void accept(Boolean aBoolean) {

                                    if (aBoolean) {

                                        record(position);
                                    } else {

                                        pSP.edit().putInt(Constant.SP_KEY_RECORD, 1).apply();
                                        toast("您拒绝了录音权限，请在权限管理中打开权限！");
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
     * 登录提醒
     */
    private void loginAlert() {

        AlertDialog alertDialog = new AlertDialog
                .Builder(requireActivity())
                .setTitle("提示")
                .setMessage("您还没有登录，是否要登录")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                })
                .setPositiveButton("去登录", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        startActivity(new Intent(requireActivity(), WxLoginActivity.class));
                    }
                })
                .show();
    }

    private void vipAlert() {


        AlertDialog alertDialog = new AlertDialog
                .Builder(requireActivity())
                .setTitle("提示")
                .setMessage("非vip用户每课只能评测三句，vip用户免费评测，是否要购买vip？")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                })
                .setPositiveButton("去购买", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        VipActivity.startActivity(requireActivity(), 0);
                    }
                })
                .show();
    }


    /**
     * 录音评测
     *
     * @param i
     */
    private void record(int i) {


        //检测评测次数
        if (Constant.userinfo == null) {

            int count = evalSP.getInt("eavl" + source + lessonid, 0);
            if (count >= 3) {

                loginAlert();
                return;
            }
        } else {

            if (!Constant.userinfo.isVip()) {

                int count = evalSP.getInt("eavl" + source + lessonid, 0);
                if (count >= 3) {

                    vipAlert();
                    return;
                }
            }
        }


        if (!evaluatingAdapter.isPlay() && !evaluatingAdapter.isRecord()) {//没有录音则可执行

            Sentence dataDTO = evaluatingAdapter.getItem(i);
            initRecord(dataDTO, i);
        } else if (evaluatingAdapter.isRecord()) {

            if (i == evaluatingAdapter.getPosition()) {//操作的数据正在播放

                evaluatingAdapter.setRecord(false);
                evaluatingAdapter.setPlay(false);
                mediaRecorder.stop();
                Sentence dataDTO = evaluatingAdapter.getItem(i);
                test(dataDTO);
            } else {//操作的数据没有播放，其他数据在播放

                Sentence dataDTO = evaluatingAdapter.getItem(i);
                int oPosition = evaluatingAdapter.getPosition();
                evaluatingAdapter.setRecord(false);
                evaluatingAdapter.setPlay(false);
                mediaRecorder.stop();
                evaluatingAdapter.notifyItemChanged(oPosition);
                initRecord(dataDTO, i);
            }
        } else if (evaluatingAdapter.isPlay()) {//在音频播放的情况下，录音

            evaluatingAdapter.setPlay(false);
            mediaPlayer.stop();
            evaluatingAdapter.notifyItemChanged(evaluatingAdapter.getPosition());
            Sentence dataDTO = evaluatingAdapter.getItem(i);
            initRecord(dataDTO, i);
        }
    }


    /**
     * 请求评测接口
     *
     * @param dataDTO
     */
    private void test(Sentence dataDTO) {

        String uid = "0";
        if (Constant.userinfo != null) {

            uid = Constant.userinfo.getUid() + "";
        }

        MediaType type = MediaType.parse("application/octet-stream");
        File file = new File(MyApplication.getContext().getExternalFilesDir(Environment.DIRECTORY_MUSIC).getAbsolutePath() + "/" + dataDTO.getSound() + ".mp3");
        RequestBody fileBody = RequestBody.create(type, file);

      /*  RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("type", dataDTO.getSource())
                .addFormDataPart("userId", uid)
                .addFormDataPart("newsId", dataDTO.getSourceid() + "")
                .addFormDataPart("paraId", dataDTO.getSourceid())
                .addFormDataPart("IdIndex", dataDTO.getIdindex())
                .addFormDataPart("sentence", dataDTO.getSentence())
                .addFormDataPart("file", file.getName(), fileBody)
//                .addFormDataPart("wordId", "0")
//                .addFormDataPart("flg", "0")
//                .addFormDataPart("appId", Constant.APPID + "")
                .build();*/


        Map<String, RequestBody> params = new HashMap<>();
        params.put("userId", toRequestBody(uid));
        params.put("IdIndex", toRequestBody(dataDTO.getIdindex()));
        params.put("paraId", toRequestBody(dataDTO.getSourceid()));
        params.put("newsId", toRequestBody(dataDTO.getSourceid()));
        params.put("type", toRequestBody(dataDTO.getSource()));
        params.put("sentence", toRequestBody(dataDTO.getSentence()));
        params.put("file\";filename=\"" + file.getName(), fileBody);
        presenter.jtest(params, dataDTO.getIdindex());
    }

    Handler recordHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {

            if (evaluatingAdapter.isRecord() && mediaRecorder != null) {

                double ratio = (double) mediaRecorder.getMaxAmplitude() / BASE;
                double db = 0;// 分贝
                if (ratio > 1) {

                    db = 20 * Math.log10(ratio);
                    evaluatingAdapter.setDb(db);
                    evaluatingAdapter.notifyItemChanged(evaluatingAdapter.getPosition());
                }
                recordHandler.sendEmptyMessageDelayed(1, 200);
            }

            return false;
        }
    });


    private RequestBody toRequestBody(String param) {
        return RequestBody.create(MediaType.parse("text/plain"), param);
    }

    @Override
    public void jtestComplete(EvaluteRecordBean evaluteRecordBean, String idIndex) {

        List<Sentence> sentenceList = evaluatingAdapter.getData();
        for (int i = 0; i < sentenceList.size(); i++) {

            Sentence sentence = sentenceList.get(i);
            if (sentence.getIdindex().equals(idIndex)) {

                sentence.setRecordSoundUrl(evaluteRecordBean.getData().getUrl());
                sentence.setScore(evaluteRecordBean.getData().getTotalScore());

                DecimalFormat decimalFormat = new DecimalFormat("#000");
                sentence.updateAll("sourceid = ? and idindex = ? and source = ?", decimalFormat.format(lessonid), idIndex, sentence.getSource());
                evaluatingAdapter.notifyItemChanged(i);
                break;
            }
        }
        //记录评测的次数
        int count = evalSP.getInt("eavl" + source + lessonid, 0);
        evalSP.edit().putInt("eavl" + source + lessonid, ++count).apply();
    }

    @Override
    public void getPublishResult(PublishEvalBean publishEvalBean, String idindex, int shuoshuotype) {

        if (shuoshuotype == 2) {//单句

            List<Sentence> sentenceList = evaluatingAdapter.getData();
            for (int i = 0; i < sentenceList.size(); i++) {

                Sentence sentence = sentenceList.get(i);
                if (Integer.parseInt(sentence.getIdindex()) == Integer.parseInt(idindex)) {//存储ShuoshuoId

                    sentence.setShuoshuoId(publishEvalBean.getShuoshuoId() + "");
                    DecimalFormat decimalFormat = new DecimalFormat("#000");
                    sentence.updateAll("sourceid = ? and idindex = ?", decimalFormat.format(lessonid), idindex);
                    break;
                }
            }
        } else {//发布合成音频后，调用分享

            String username;
            if (Constant.userinfo == null) {

                username = Constant.userinfo.getUsername();
            } else {

                username = "";
            }

            String title = username + "在「" + getString(R.string.app_name) + "」中进行口语评测";
            String text = "";
            List<JpLesson> jpLessonList = LitePal.where("source = ? and lessonid =?", source + "", lessonid + "").find(JpLesson.class);
            if (jpLessonList.size() > 0) {

                text = jpLessonList.get(0).getLesson();
            }
            String imagepath = Constant.URL_APP + "/ios/images/bignews/" + "biaori.png";
            if (mergeBean != null) {

                String url = Constant.URL_VOA + "/voa/play.jsp?id=" + publishEvalBean.getShuoshuoId() + "&addr=" + mergeBean.getUrl() + "&apptype=biaori";
                showShare(title, text, imagepath, url, imagepath);
            }
        }
    }


    @Override
    public void getMerge(MergeBean mergeBean) {

        this.mergeBean = mergeBean;

        binding.evaluatingTvMerge.setText("听");
    }

    @Override
    public void showReward(String rewardStr) {

        if (!rewardStr.equals("0")) {

            int reward = Integer.parseInt(rewardStr);
            double rewardDouble = reward / 100.0f;
            DecimalFormat decimalFormat = new DecimalFormat("#.##");


            new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                    .setTitle("恭喜您！")
                    .setMessage("本次学习获得" + decimalFormat.format(rewardDouble) + "元红包奖励,已自动存入您的钱包账户。\n红包可在【爱语吧】微信公众号提现，继续学习领取更多红包奖励吧！")
                    .setPositiveButton("好的", (dialog, which) -> {

                        dialog.dismiss();
                    })
                    .show();
        }
    }


    /**
     * 合成音频进度条
     */
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {

            if (mediaPlayer.isPlaying()) {

                int cp = mediaPlayer.getCurrentPosition();

                String curStr = getTimeStr(cp);
                String durStr = getTimeStr(mediaPlayer.getDuration());

                binding.evaluatingTvCtime.setText(curStr);
                binding.evaluatingTvTtime.setText(durStr);
                binding.evaluatingSbProgress.setProgress(cp);
                handler.sendEmptyMessageDelayed(1, 200);
            }
            return false;
        }
    });


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
}