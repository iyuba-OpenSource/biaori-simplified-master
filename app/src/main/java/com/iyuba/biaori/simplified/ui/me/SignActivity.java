package com.iyuba.biaori.simplified.ui.me;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.iyuba.biaori.simplified.Constant;
import com.iyuba.biaori.simplified.MyApplication;
import com.iyuba.biaori.simplified.R;
import com.iyuba.biaori.simplified.model.bean.me.MyTimeBean;
import com.iyuba.biaori.simplified.model.bean.textbook.UpdateScoreBean;
import com.iyuba.biaori.simplified.presenter.me.SignPresenter;
import com.iyuba.biaori.simplified.util.DateUtil;
import com.iyuba.biaori.simplified.util.NumberUtil;
import com.iyuba.biaori.simplified.util.QRCodeEncoder;
import com.iyuba.biaori.simplified.util.popup.LoadingPopup;
import com.iyuba.biaori.simplified.view.me.SignContract;
import com.iyuba.module.toolbox.DensityUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * 我的-打卡签到页面
 */
public class SignActivity extends AppCompatActivity implements SignContract.SignView {

    private ImageView sign_iv_bg;
    private ImageView qrImage;
    private TextView sign_tv_days, sign_tv_word, sign_tv_over;
    private TextView tv_sign;
    private ImageView userIcon;
    private TextView tvShareMsg;
    private TextView mSignHistory;
    /**
     * 要求能打卡的时间
     */
    private final int signStudyTime = 3 * 60;

    String shareTxt;
    LinearLayout ll;
    private TextView tvUserName;
    private TextView tvAppName;

    private SignPresenter signPresenter;
    private MyTimeBean myTimeBean;
    private LoadingPopup loadingPopup;


    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();

        signPresenter = new SignPresenter();
        signPresenter.attchView(this);

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_sign);

        showLoading(null);

        initView();
        initData();
        initBackGround();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (signPresenter != null) {

            signPresenter.detachView();
        }
    }


    private void initData() {

        //请求数据
        signPresenter.getMyTime(Constant.userinfo.getUid() + "", DateUtil.getDays(), 1);
    }


    @Override
    public void showLoading(String msg) {

        if (loadingPopup == null) {

            loadingPopup = new LoadingPopup(SignActivity.this);
        }
        loadingPopup.setContent("正在加载数据....");
        loadingPopup.showPopupWindow();
    }

    @Override
    public void hideLoading() {

        if (loadingPopup != null) {

            loadingPopup.dismiss();
        }
    }

    public void toast(String msg) {
        Toast.makeText(MyApplication.getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void initView() {

        sign_iv_bg = findViewById(R.id.sign_iv_bg);
        sign_tv_days = findViewById(R.id.sign_tv_days);
        sign_tv_word = findViewById(R.id.sign_tv_word);
        sign_tv_over = findViewById(R.id.sign_tv_over);

        tv_sign = findViewById(R.id.tv_sign);
        ll = findViewById(R.id.ll);
        qrImage = findViewById(R.id.tv_qrcode);
        userIcon = findViewById(R.id.iv_userimg);
        tvUserName = findViewById(R.id.tv_username);
        tvAppName = findViewById(R.id.tv_appname);
        tvShareMsg = findViewById(R.id.tv_sharemsg);
        mSignHistory = findViewById(R.id.tv_sign_history);


        findViewById(R.id.tv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCloseAlert();
            }
        });

        mSignHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//打卡记录
                startActivity(new Intent(SignActivity.this, CalendarActivity.class));
            }
        });

        //打卡
        tv_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (myTimeBean == null) {

                    return;
                }
                int time = Integer.parseInt(myTimeBean.getTotalTime());

                if (time < signStudyTime) {

                    toast(String.format(Locale.CHINA, "打卡失败，当前已学习%d秒，\n满%d分钟可打卡", time, 3));
                } else {

                    mSignHistory.setVisibility(View.GONE);
                    qrImage.setVisibility(View.VISIBLE);
                    tv_sign.setVisibility(View.GONE);
                    tvShareMsg.setVisibility(View.VISIBLE);
                    findViewById(R.id.tv_close).setVisibility(View.INVISIBLE);
                    tvShareMsg.setText("长按图片识别二维码");
                    tvShareMsg.setGravity(Gravity.CENTER_HORIZONTAL);
                    findViewById(R.id.tv_desc).setVisibility(View.VISIBLE);
                    tvShareMsg.setBackground(getDrawable(R.drawable.sign_bg_yellow));
                    writeBitmapToFile();
                    findViewById(R.id.tv_desc).setVisibility(View.INVISIBLE);
                    showShareOnMoment(MyApplication.getContext(), Constant.userinfo.getUid() + "",
                            Constant.APPID + "");
                }
            }
        });

    }

    private void showCloseAlert() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(SignActivity.this);
        dialog.setTitle("温馨提示");
        dialog.setMessage("点击下面的打卡按钮,成功分享至微信朋友圈,可以领取红包哦!您确定要退出吗?");
        dialog.setPositiveButton("留下打卡", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setNegativeButton("去意已决", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void initBackGround() {
        //头像
        String head = Constant.userinfo.getImgSrc();

        if (head.startsWith("http://")) {

            Glide.with(this).load(head).into(userIcon);
        } else {

            Glide.with(this).load(Constant.URL_STATIC1 + "/uc_server/" + head).into(userIcon);
        }
        //用户名
        tvUserName.setText(Constant.userinfo.getUsername());

        tvAppName.setText(getString(R.string.app_name) + "\n练习听力的好帮手!");

    }

    /**
     * 截图
     */
    public void writeBitmapToFile() {

        View view = getWindow().getDecorView();
        ((TextView) findViewById(R.id.tv_desc)).setText("刚刚在 「" + getString(R.string.app_name) + "」上完成了打卡");
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        if (bitmap == null) {
            return;
        }
        bitmap.setHasAlpha(false);
        bitmap.prepareToDraw();
        File newpngfile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "aaa.png");

        if (newpngfile.exists()) {
            newpngfile.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(newpngfile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * @author
     * @time
     * @describe 启动获取积分(红包的接口)
     */

    private void startInterfaceADDScore() {

        Date currentTime = new Date();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentTime);
        final String time = Base64.encodeToString(dateString.getBytes(), Base64.DEFAULT);

        signPresenter.updateScore(time, Constant.userinfo.getUid() + "", Constant.APPID + "", "0", 81, 1);
    }


    public void showShareOnMoment(Context context, final String userID, final String AppId) {

        share(context,
                getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/aaa.png",
                null,
                null,
                "学日语的朋友们快扫个码！自从用上了爱语吧的" +
                        getString(R.string.app_name) +
                        "app，我的日语听说水平明显提高！我们在这可以零起点学英语，练日语听力和口语、人工智能学日语！高效学日语！墙裂推荐~"
                , "每日学习打卡~",
                userID,
                AppId);
    }

    public void share(Context context, String imagePath, String imageUrl, String url,
                      String text, String comment, String userID, String AppId) {
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("Label", text);
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
        Toast.makeText(MyApplication.getContext(), "推荐语录已经复制到粘贴板", Toast.LENGTH_SHORT).show();

        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setShareType(Platform.SHARE_IMAGE);
        sp.setTitle(text);
        sp.setText(text);
        sp.setImageUrl(imageUrl);
        sp.setImagePath(imagePath);
        sp.setUrl(url);
        Platform wechatMoments = ShareSDK.getPlatform(WechatMoments.NAME);
        wechatMoments.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                startInterfaceADDScore();
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Log.e("--分享失败=== onError", throwable.toString());
            }

            @Override
            public void onCancel(Platform platform, int i) {
                Log.e("okCallbackonError", "onCancel");
                Log.e("--分享取消=== onCancel", "....");
            }
        });
        wechatMoments.share(sp);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        showCloseAlert();
    }

    @Override
    public void getMyTime(MyTimeBean myTimeBean) {

        this.myTimeBean = myTimeBean;

        /**
         * 计算显示哪张背景图片
         */
        int index = Integer.parseInt(myTimeBean.getTotalDays()) % 10;
        String sql = "http://staticvip.iyuba.cn/images/mobile/jp/";
        index++;

        sql = sql + index + ".jpg";
        Glide.with(SignActivity.this).load(sql).into(sign_iv_bg);


        int time = Integer.parseInt(myTimeBean.getTotalTime());
        sign_tv_days.setText("学习天数:\n" + myTimeBean.getTotalDays() + "天");
        sign_tv_word.setText("总单词:\n" + myTimeBean.getTotalWord());
        int rankRate = 100 - Integer.parseInt(myTimeBean.getRanking()) * 100 / Integer.parseInt(myTimeBean.getTotalUser());
        sign_tv_over.setText("超越了：\n" + rankRate + "%同学");
        shareTxt = myTimeBean.getSentence() + "我在" + getString(R.string.app_name) + "坚持学习了" + myTimeBean.getTotalDays() + "天"
                + "单词如下";
        //打卡
        String qrIconUrl = Constant.URL_APP + "/share.jsp?uid=" + Constant.userinfo.getUid()
                + "&appId=" + Constant.APPID + "&shareId=" + myTimeBean.getShareId();
        Bitmap qr_bitmap = QRCodeEncoder
                .syncEncodeQRCode(qrIconUrl, DensityUtil.dp2px(MyApplication.getContext(), 65), Color.BLACK, Color.WHITE, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        qrImage.setImageBitmap(qr_bitmap);
    }

    @Override
    public void updateScoreComplete(UpdateScoreBean updateScoreBean) {


        String money = updateScoreBean.getMoney();//本次打卡获得的钱数 ，单位 分
        String addCredit = updateScoreBean.getAddcredit();//本次打卡获得的积分
        String days = updateScoreBean.getDays();//连续打卡天数
        String totalCredit = updateScoreBean.getTotalcredit();//总积分
        //打卡成功,您已连续打卡xx天,获得xx元红包,关注[爱语课吧]微信公众号即可提现!
        float moneyThisTime = Float.parseFloat(money);
        if (moneyThisTime > 0) {
            float moneyTotal = Float.parseFloat(totalCredit);
            Toast.makeText(MyApplication.getContext(), "打卡成功," + "您已连续打卡" + days + "天,  获得" + NumberUtil.getFormatDouble(moneyThisTime * 0.01) + "元,总计: " + NumberUtil.getFormatDouble(moneyTotal * 0.01) + "元," + "满10元可在\"爱语吧\"公众号提现", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(MyApplication.getContext(), "打卡成功，连续打卡" + days + "天,获得" + addCredit + "积分，总积分: " + totalCredit, Toast.LENGTH_LONG).show();
            finish();
        }
    }

}