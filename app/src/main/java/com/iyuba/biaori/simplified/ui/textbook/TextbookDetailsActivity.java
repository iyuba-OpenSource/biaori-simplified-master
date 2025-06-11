package com.iyuba.biaori.simplified.ui.textbook;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.iyuba.biaori.simplified.Constant;
import com.iyuba.biaori.simplified.MyApplication;
import com.iyuba.biaori.simplified.R;
import com.iyuba.biaori.simplified.adapter.MyFragmentAdapter2;
import com.iyuba.biaori.simplified.databinding.ActivityTextbookDetailsBinding;
import com.iyuba.biaori.simplified.db.Book;
import com.iyuba.biaori.simplified.db.JpLesson;
import com.iyuba.biaori.simplified.db.Sentence;
import com.iyuba.biaori.simplified.model.bean.textbook.JpSentenceBean;
import com.iyuba.biaori.simplified.model.bean.textbook.PdfFileBean;
import com.iyuba.biaori.simplified.model.bean.textbook.UpdateScoreBean;
import com.iyuba.biaori.simplified.presenter.textbook.TextbookDetailsPresenter;
import com.iyuba.biaori.simplified.ui.BaseActivity;
import com.iyuba.biaori.simplified.ui.fragment.textbook.EvaluatingFragment;
import com.iyuba.biaori.simplified.ui.fragment.textbook.LessonWordFragment;
import com.iyuba.biaori.simplified.ui.fragment.textbook.OriginalFragment;
import com.iyuba.biaori.simplified.ui.fragment.textbook.ParaFragment;
import com.iyuba.biaori.simplified.ui.fragment.textbook.RankingFragment;
import com.iyuba.biaori.simplified.ui.login.LoginActivity;
import com.iyuba.biaori.simplified.ui.login.WxLoginActivity;
import com.iyuba.biaori.simplified.ui.vip.VipActivity;
import com.iyuba.biaori.simplified.util.popup.MorePopup;
import com.iyuba.biaori.simplified.view.textbook.TextbookDetailsContract;
import com.mob.MobSDK;

import org.litepal.LitePal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
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
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 教材评测主页面  课文详情主界面
 */
public class TextbookDetailsActivity extends BaseActivity<TextbookDetailsContract.TextbookDetailsView,
        TextbookDetailsContract.TextbookDetailsPresenter>
        implements TextbookDetailsContract.TextbookDetailsView {

    private ActivityTextbookDetailsBinding activityTextbookDetailsBinding;
    private final String[] title = {"原文", "阅读", "单词", "评测", "排行"};
    private List<Fragment> fragmentList;

    private MyFragmentAdapter2 myFragmentAdapter2;

    /**
     * 播放的位置在音频中
     */
    private int position;

    private List<JpLesson> itemList;

    private MorePopup morePopup;

    private MorePopup pdfPopup;

    private JpLesson jpLesson;

    private SharedPreferences downloadSP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        getBundle();


        downloadSP = getSharedPreferences(Constant.SP_DOWNLOAD, MODE_PRIVATE);

        getBundleForLesson();
        activityTextbookDetailsBinding.toolbar.toolbarIvTitle.setText(jpLesson.getLesson());
        activityTextbookDetailsBinding.toolbar.toolbarIvRight.setVisibility(View.VISIBLE);
        activityTextbookDetailsBinding.toolbar.toolbarIvRight.setImageResource(R.mipmap.icon_more);
        activityTextbookDetailsBinding.toolbar.toolbarIvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                initMorePopup();
            }
        });
        activityTextbookDetailsBinding.toolbar.toolbarIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        activityTextbookDetailsBinding.tdTl.setupWithViewPager(activityTextbookDetailsBinding.tdVp);


        List<Book> bookList = LitePal.where("source = ?", jpLesson.getSource()).find(Book.class);
        String bookName = "jp3";
        if (bookList.size() != 0) {

            bookName = bookList.get(0).getName();
        }
        DecimalFormat decimalFormat = new DecimalFormat("#000");
        String lessonid = decimalFormat.format(Integer.parseInt(jpLesson.getLessonID()));
        List<Sentence> sentenceList = LitePal.where("source = ? and sourceid = ? and startTime IS NOT NULL", bookName, lessonid).find(Sentence.class);
        if (sentenceList.size() > 0) {

            initTab();
            initFragment();
        } else {

            presenter.getJpSentence(bookName, jpLesson.getLessonID());
        }
    }


    /**
     * 下载、分享pdf的弹窗
     */
    @Override
    public void showPdfDialog(PdfFileBean pdfFileBean) {


        if (!Constant.userinfo.isVip()) {//不是会员则下载次数+1

            //记录下载次数
            int count = downloadSP.getInt(Constant.SP_DOWNLOAD_COUNT, 0);
            count++;
            downloadSP.edit().putInt(Constant.SP_DOWNLOAD_COUNT, count).apply();
        }

        if (pdfPopup != null) {

            pdfPopup.dismiss();
        }

        androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(TextbookDetailsActivity.this)
                .setMessage("pdf文件链接地址已经生成！")
                .setPositiveButton("发送", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        sendPdfToQQOrWX(pdfFileBean);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("打开", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        getpdf(pdfFileBean);
                        dialog.dismiss();
                    }
                }).create();
        alertDialog.show();

    }

    @Override
    public void getJpSentenceComplete(JpSentenceBean jpSentenceBean) {

        if (jpSentenceBean == null) {

            return;
        }

        //存储句子数据
        List<Sentence> sentenceList = jpSentenceBean.getData();
        for (int i = 0; i < sentenceList.size(); i++) {

            Sentence sentence = sentenceList.get(i);
            List<Sentence> sentences = LitePal
                    .where("idindex = ? and sourceid = ? and source = ?", sentence.getIdindex(), sentence.getSourceid(), sentence.getSource())
                    .find(Sentence.class);
            if (sentences.size() > 0) {

                Sentence st = sentences.get(0);
                sentence.setScore(st.getScore());
                sentence.setRecordSoundUrl(st.getRecordSoundUrl());
                sentence.updateAll("idindex = ? and sourceid = ? and source = ?", sentence.getIdindex(), sentence.getSourceid(), sentence.getSource());
            } else {

                sentence.save();
            }
        }
        //显示页面
        initTab();
        initFragment();
    }

    /**
     * 更多
     */
    private void initMorePopup() {

        if (morePopup == null) {

            List<String> stringList = new ArrayList<>();
            stringList.add("pdf导出");
            stringList.add("课文分享");
            stringList.add("下载音频");
            morePopup = new MorePopup(this);
            morePopup.setAlignBackground(true);
            morePopup.initOperation(stringList);
            morePopup.setCallback(new MorePopup.Callback() {
                @Override
                public void getString(String s) {

                    if (s.equals("pdf导出")) {

                        if (Constant.userinfo == null) {

                            startActivity(new Intent(TextbookDetailsActivity.this, WxLoginActivity.class));
                            toast("请登录");
                            return;
                        }

                        if (Constant.userinfo.isVip()) {
                            initPDFPopup();
                        } else {
                            showCreditDialog();
//                            showBuyVipDialog();
                        }
                    } else if (s.equals("课文分享")) {

                        String logoPath = createLogoFile();
                        String url = Constant.URL_LESSON_SHARE + "source=" + jpLesson.getSource()
                                + "&sourceid=" + jpLesson.getLessonID();
                        showShare(jpLesson.getLesson(), jpLesson.getLessonCH(), logoPath, url, null);
                    } else if (s.equals("下载音频")) {

                        downloadMp3();
                    }
                    morePopup.dismiss();
                }
            });
        }
        morePopup.showPopupWindow(activityTextbookDetailsBinding.toolbar.toolbarIvRight);
    }


    /**
     * 下载mp3文件
     */
    private void downloadMp3() {

        String mp3Path = getExternalFilesDir(Environment.DIRECTORY_MUSIC).toString() +
                jpLesson.getSound().replace("http://static2.iyuba.cn", "");

        File file = new File(mp3Path);
        if (file.exists()) {

            toast("音频文件已下载 " + mp3Path);
            return;
        }

        if (!file.getParentFile().exists()) {

            file.getParentFile().mkdirs();
        }

        toast("音频文件正在下载 ");
        new Thread(new Runnable() {
            @Override
            public void run() {

                RequestBody requestBody = new FormBody.Builder()
                        .build();
                Request request = new Request.Builder()
                        .url(jpLesson.getSound())
                        .post(requestBody)
                        .build();
                OkHttpClient client = new OkHttpClient();
                try {
                    Response response = client.newCall(request).execute();
                    InputStream inputStream = response.body().byteStream();
                    long totalSize = response.body().contentLength();

                    FileOutputStream fileOutputStream = new FileOutputStream(mp3Path);
                    byte[] b = new byte[2048];

                    int count = 0;
                    int p = 0;
                    int lodp = 0;
                    int l = inputStream.read(b);
                    while (l != -1) {

                        fileOutputStream.write(b, 0, l);
                        l = inputStream.read(b);
                        count += l;
                        p = (int) (count * 100.0 / totalSize);
                        if (lodp != p && p % 10 == 0) {

                            toast("正在下载音频文件：" + p + "%");
                            lodp = p;
                        }
                        Log.d("download", count * 100.0 / totalSize + "");
                    }
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    inputStream.close();


                    List<JpLesson> jpLessonList = LitePal
                            .where("source = ? and lessonid =?", jpLesson.getSource(), Integer.parseInt(jpLesson.getLessonID()) + "")
                            .find(JpLesson.class);
                    if (jpLessonList.size() > 0) {

                        JpLesson jpLesson = jpLessonList.get(0);
                        jpLesson.setDownloadFlag(1);
                        jpLesson.updateAll("source = ? and lessonid = ?", jpLesson.getSource(), Integer.parseInt(jpLesson.getLessonID()) + "");
                    }

                    toast("下载音频完成");


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    /**
     * 创建logo文件
     */
    private String createLogoFile() {

        File logoFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "logo.png");
        if (!logoFile.exists()) {
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(logoFile);
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return logoFile.getAbsolutePath();
    }


    /**
     * pdf发送到QQ或微信
     */
//    private void sendQQOrWX() {
//
//        if (Constant.userinfo == null) {
//
//            startActivity(new Intent(TextbookDetailsActivity.this,WxLoginActivity.class));
//            toast("请登录");
//            return;
//        }
//        if (!Constant.userinfo.isVip()) {
//
//            toast("您还不是会员");
//            VipActivity.startActivity(this, 0);
//            return;
//        }
//        initPDFPopup(1);
//
//    }

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

        oks.setImagePath(imagePath);

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

                SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
                String flag = null;
                try {
                    flag = Base64.encodeToString(
                            URLEncoder.encode(df.format(new Date(System.currentTimeMillis())), "UTF-8").getBytes(), Base64.DEFAULT);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                int srid = 0;
                if (platform.getName() == QQ.NAME || platform.getName() == Wechat.NAME) {

                    srid = 49;
                } else {
                    srid = 19;
                }
                presenter.updateScore(flag, Constant.userinfo.getUid() + "", Constant.APPID + "",
                        "100" + Constant.bookDataDTO.getSource() + jpLesson.getLessonID(), srid, 1);
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {

            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        });
    }

    /**
     * 非vip 用户提示 需要扣除积分
     */
    private void showCreditDialog() {

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("温馨提示");
        builder.setMessage("非VIP用户每次下载扣除20积分，前5次免费");
        builder.setPositiveButton("下载", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                int count = downloadSP.getInt(Constant.SP_DOWNLOAD_COUNT, 0);//免费下载次数是否小于5次
                if (count < 5) {

                    initPDFPopup();
                } else {

                    SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
                    String flag = null;
                    try {
                        flag = Base64.encodeToString(
                                URLEncoder.encode(df.format(new Date(System.currentTimeMillis())), "UTF-8").getBytes(), Base64.DEFAULT);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    presenter.updateScore(flag, Constant.userinfo.getUid() + "", Constant.APPID + "",
                            "100" + Constant.bookDataDTO.getSource() + jpLesson.getLessonID(), 40, 1);
                    dialog.dismiss();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        builder.show();
    }

    /**
     * pdf导出的弹窗
     */
    private void initPDFPopup() {

        if (pdfPopup == null) {

            List<String> stringList = new ArrayList<>();
            stringList.add("导出日文");
            stringList.add("导出中日双语");
            pdfPopup = new MorePopup(this);
            pdfPopup.initOperation(stringList);
            pdfPopup.setBackgroundColor(Color.parseColor("#99888888"));
            pdfPopup.setCallback(new MorePopup.Callback() {
                @Override
                public void getString(String s) {

                    if (s.equals("导出日文")) {

                        presenter.getJapanesePdfFile(Constant.bookDataDTO.getSource() + "", jpLesson.getLessonID(), pdfPopup.getFlag());
                    } else if (s.equals("导出中日双语")) {

                        presenter.getJapanesePdfFileWithCn(Constant.bookDataDTO.getSource() + "", jpLesson.getLessonID(), pdfPopup.getFlag());
                    }
                }
            });
        }
        pdfPopup.setPopupGravity(Gravity.CENTER);
        pdfPopup.showPopupWindow();
    }

    /**
     * 显示成功获取pdf发alert
     *
     * @param
     */
//    private void showPdfComplete(String s) {
//
//        AlertDialog dialog = new AlertDialog.Builder(this)
//                .setTitle("PDF链接生成成功！")
//                .setCancelable(true)
//                .create();
//        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "下载", (v, u) -> {
//
//            dialog.dismiss();
//            Uri uri = Uri.parse(s);
//            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//            startActivity(intent);
//        });
//        dialog.show();
//    }
    private void getBundleForLesson() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            position = bundle.getInt("POSITION", 0);
            itemList = (List<JpLesson>) bundle.getSerializable("DATALIST");
            jpLesson = itemList.get(position);
        }
    }


    public static void startActivity(Activity activity, int position, List<JpLesson> jpLessonList) {

        Intent intent = new Intent(activity, TextbookDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("POSITION", position);
        bundle.putSerializable("DATALIST", (Serializable) jpLessonList);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }


    private void initTab() {

        for (int i = 0; i < title.length; i++) {

            TabLayout.Tab tab = activityTextbookDetailsBinding.tdTl.newTab();
            tab.setText(title[i]);
            activityTextbookDetailsBinding.tdTl.addTab(tab);
        }
    }

    private void initFragment() {

        fragmentList = new ArrayList<>();
        fragmentList.add(OriginalFragment.newInstance(jpLesson.getLessonID(), position, itemList, Integer.parseInt(jpLesson.getSource())));
        fragmentList.add(ParaFragment.newInstance(jpLesson.getLessonID(), position, itemList, Integer.parseInt(jpLesson.getSource())));
        fragmentList.add(LessonWordFragment.newInstance(Integer.parseInt(jpLesson.getLessonID())));
        fragmentList.add(EvaluatingFragment.newInstance(Integer.parseInt(jpLesson.getLessonID()), Integer.parseInt(jpLesson.getSource())));
        fragmentList.add(RankingFragment.newInstance(Integer.parseInt(jpLesson.getSource()), Integer.parseInt(jpLesson.getLessonID())));

        myFragmentAdapter2 = new MyFragmentAdapter2(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, fragmentList, title);
        activityTextbookDetailsBinding.tdVp.setAdapter(myFragmentAdapter2);
    }

    @Override
    public View initLayout() {
        activityTextbookDetailsBinding = ActivityTextbookDetailsBinding.inflate(getLayoutInflater());
        return activityTextbookDetailsBinding.getRoot();
    }

    @Override
    public TextbookDetailsContract.TextbookDetailsPresenter initPresenter() {
        return new TextbookDetailsPresenter();
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
    public void getpdf(PdfFileBean pdfFileBean) {

        pdfPopup.dismiss();

        Uri uri = Uri.parse(Constant.URL_AI + "/management" + pdfFileBean.getPath());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @Override
    public void updateScore(UpdateScoreBean updateScoreBean, int srid) {

        if (srid == 40) {

            initPDFPopup();
        } else {

            if (!updateScoreBean.getAddcredit().equals("0")) {

                toast("分享成功，增加" + updateScoreBean.getAddcredit() + "积分");
            }
        }
    }

    @Override
    public void sendPdfToQQOrWX(PdfFileBean pdfFileBean) {

        pdfPopup.dismiss();
        String logoPath = createLogoFile();
        showShare(jpLesson.getLesson() + ".pdf", jpLesson.getLessonCH(), logoPath, Constant.URL_AI + "/management" + pdfFileBean.getPath(), null);
    }


    /**
     * 设置新的数据并显示
     *
     * @param jpLesson
     */
    public void setNewData(JpLesson jpLesson, int position) {

        this.jpLesson = jpLesson;
        this.position = position;
        //title
        activityTextbookDetailsBinding.toolbar.toolbarIvTitle.setText(jpLesson.getLesson());
        //原文
        OriginalFragment oriFragment = null;
        for (int i = 0; i < fragmentList.size(); i++) {

            Fragment fragment = fragmentList.get(i);
            if (fragment instanceof OriginalFragment) {

                oriFragment = (OriginalFragment) fragment;
                break;
            }
        }
        if (oriFragment != null) {
            Bundle bundle = oriFragment.getArguments();
            if (bundle != null) {

                bundle.putString("LESSON_ID", jpLesson.getLessonID());
                bundle.putInt("POSITION", position);
            }
            oriFragment.switchData();
        }
        //单词页面
        LessonWordFragment lessonWordFragment = null;
        for (int i = 0; i < fragmentList.size(); i++) {

            Fragment fragment = fragmentList.get(i);
            if (fragment instanceof LessonWordFragment) {

                lessonWordFragment = (LessonWordFragment) fragment;
                break;
            }
        }
        if (lessonWordFragment != null) {

            Bundle bundle = lessonWordFragment.getArguments();
            if (bundle != null) {

                bundle.putInt("SOURCEID", Integer.parseInt(jpLesson.getLessonID()));
            }

            lessonWordFragment.switchData();
        }

    }
}