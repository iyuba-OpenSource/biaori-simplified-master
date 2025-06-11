package com.iyuba.biaori.simplified;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iyuba.biaori.simplified.databinding.ActivityWelcomeBinding;
import com.iyuba.biaori.simplified.db.Book;
import com.iyuba.biaori.simplified.model.bean.AdEntryBean;
import com.iyuba.biaori.simplified.presenter.WelcomePresenter;
import com.iyuba.biaori.simplified.ui.BaseActivity;
import com.iyuba.biaori.simplified.ui.welcome.MyWebActivity;
import com.iyuba.biaori.simplified.ui.welcome.PrivacyActivity;
import com.iyuba.biaori.simplified.util.BookUtil;
import com.iyuba.biaori.simplified.util.popup.PrivacyPopup;
import com.iyuba.biaori.simplified.view.WelcomeContract;
import com.iyuba.imooclib.ui.web.Web;
import com.yd.saas.base.interfaces.AdViewSpreadListener;
import com.yd.saas.config.exception.YdError;
import com.yd.saas.ydsdk.YdSpread;
import com.youdao.sdk.common.YoudaoSDK;
import com.youdao.sdk.nativeads.NativeErrorCode;
import com.youdao.sdk.nativeads.NativeResponse;
import com.youdao.sdk.nativeads.RequestParameters;
import com.youdao.sdk.nativeads.YouDaoNative;

import org.litepal.LitePal;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Timer;

import timber.log.Timber;

/**
 * 首页页面
 */
public class WelcomeActivity extends BaseActivity<WelcomeContract.WelcomeView, WelcomeContract.WelcomePresenter>
        implements WelcomeContract.WelcomeView, YouDaoNative.YouDaoNativeNetworkListener, AdViewSpreadListener {

    private ActivityWelcomeBinding binding;

    private PrivacyPopup privacyPopup;

    private SharedPreferences sp;

    /**
     * 广告
     */

    private AdEntryBean.DataDTO dataDTO;

    /**
     * 倒计时
     */
    private long time5s = 0;

    private boolean isAdCLick = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dealWindow();
        initOperation();

        //初始化课本数据
        BookUtil bookUtil = new BookUtil();
        bookUtil.initBook();


        sp = getSharedPreferences(Constant.SP_PRIVACY, MODE_PRIVATE);
        int pState = sp.getInt(Constant.SP_KEY_PRIVACY_STATE, 0);
        if (pState == 0) {

            initPrivacyPopup();
        } else {

            binding.welcomeTvJump.setVisibility(View.VISIBLE);
            String uid = Constant.userinfo == null ? "0" : Constant.userinfo.getUid() + "";
            presenter.getAdEntryAll(Constant.APPID + "", 1, uid);
            countDownTimer.start();
        }
    }

    private void initOperation() {

        binding.welcomeIvAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dataDTO == null) {

                    startActivity(Web.buildIntent(WelcomeActivity.this, "http://app.iyuba.cn", "推广"));
                } else {

                    if (dataDTO.getType().equals("youdao")) {


                    } else {
                        //开始
                        String startuppicUrl = dataDTO.getStartuppicUrl();
                        startActivity(Web.buildIntent(WelcomeActivity.this, startuppicUrl, "推广"));
                    }
                }
            }
        });
        //跳转
        binding.welcomeTvJump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (countDownTimer != null) {

                    countDownTimer.cancel();
                }
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isAdCLick) {//点击了就直接跳转mainactivity

            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    public View initLayout() {

        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public WelcomeContract.WelcomePresenter initPresenter() {
        return new WelcomePresenter();
    }

    /**
     * 处理状态栏和虚拟返回键
     */
    private void dealWindow() {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {

            WindowInsetsController windowInsetsController = getWindow().getInsetsController();
            windowInsetsController.hide(WindowInsets.Type.systemBars());
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {

            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_FULLSCREEN);
        } else {

            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }


    private void initPrivacyPopup() {

        if (privacyPopup == null) {

            privacyPopup = new PrivacyPopup(this);
            privacyPopup.setCallback(new PrivacyPopup.Callback() {
                @Override
                public void yes() {

                    LocalBroadcastManager.getInstance(WelcomeActivity.this).sendBroadcastSync(new Intent(Constant.ACTION_INIT));
                    SharedPreferences sp = getSharedPreferences(Constant.SP_PRIVACY, MODE_PRIVATE);
                    sp.edit().putInt(Constant.SP_KEY_PRIVACY_STATE, 1).apply();
                    privacyPopup.dismiss();

                    startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                    finish();
                }

                @Override
                public void no() {

                    privacyPopup.dismiss();
                    finish();
                }

                @Override
                public void user() {

                    PrivacyActivity.startActivity(WelcomeActivity.this, Constant.URL_PROTOCOLUSE, "用户协议");
                }

                @Override
                public void privacy() {

                    PrivacyActivity.startActivity(WelcomeActivity.this, Constant.URL_PROTOCOLPRI, "隐私政策");
                }
            });
        }
        privacyPopup.showPopupWindow();
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
    public void getAdEntryAllComplete(AdEntryBean adEntryBean) {

       /* AdEntryBean.DataDTO dataDTO = adEntryBean.getData();
        this.adEntryBean = adEntryBean;

        if (dataDTO.getType().equals("youdao")) {

            YouDaoNative youdaoNative = new YouDaoNative(this, "a710131df1638d888ff85698f0203b46", this);
            RequestParameters requestParameters = RequestParameters
                    .builder()
                    .keywords("日语")
                    .keywords("日本语")
                    .keywords("日语等级考试")
                    .keywords("日语单词")
                    .keywords("日语听力")
                    .build();
            youdaoNative.makeRequest(requestParameters);
        } else {//自己家的广告

            Glide.with(this).load(Constant.URL_STATIC3 + "/dev/" + dataDTO.getStartuppic()).into(binding.welcomeIvAd);
        }*/


        dataDTO = adEntryBean.getData();
        String type = dataDTO.getType();
        if (type.equals("web")) {

            Glide.with(WelcomeActivity.this).load("http://static3.iyuba.cn/dev" + dataDTO.getStartuppic()).into(binding.welcomeIvAd);
        } else if (type.equals(Constant.AD_ADS1) || type.equals(Constant.AD_ADS2) || type.equals(Constant.AD_ADS3)
                || type.equals(Constant.AD_ADS4) || type.equals(Constant.AD_ADS5)) {

            YdSpread mSplashAd = new YdSpread.Builder(WelcomeActivity.this)
                    .setKey("0125")
                    .setContainer(binding.welcomeFlAd)
                    .setSpreadListener(this)
                    .setCountdownSeconds(4)
                    .setSkipViewVisibility(true)
                    .build();
            mSplashAd.requestSpread();
        } else {

            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    public void onNativeLoad(NativeResponse nativeResponse) {

        nativeResponse.handleClick(binding.welcomeIvAd);

        Glide.with(binding.welcomeIvAd.getContext()).load(nativeResponse.getMainImageUrl()).into(binding.welcomeIvAd);
        nativeResponse.recordImpression(binding.welcomeIvAd);
    }

    @Override
    public void onNativeFail(NativeErrorCode nativeErrorCode) {

    }

    @Override
    protected void onStop() {
        super.onStop();

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (countDownTimer != null) {

            countDownTimer.cancel();
        }
    }

    /**
     * 计时器
     */
    CountDownTimer countDownTimer = new CountDownTimer(5000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {

            binding.welcomeTvJump.setText("跳过(" + (millisUntilFinished / 1000) + ")");
            time5s = millisUntilFinished;
        }

        @Override
        public void onFinish() {

            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            finish();
        }
    };

    @Override
    public void onAdDisplay() {

        Timber.d("onAdDisplay");
    }

    @Override
    public void onAdClose() {

        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void onAdClick(String s) {

        isAdCLick = true;
        Timber.d("onAdClick");
    }

    @Override
    public void onAdFailed(YdError ydError) {

        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
        finish();
        Timber.d("onAdFailed:" + ydError.getMsg());
    }
}