package com.iyuba.biaori.simplified.ui.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.iyuba.biaori.simplified.Constant;
import com.iyuba.biaori.simplified.MainActivity;
import com.iyuba.biaori.simplified.MyApplication;
import com.iyuba.biaori.simplified.R;
import com.iyuba.biaori.simplified.databinding.ActivityRegisterBinding;
import com.iyuba.biaori.simplified.databinding.FragmentMeBinding;
import com.iyuba.biaori.simplified.model.bean.login.UserBean;
import com.iyuba.biaori.simplified.model.bean.login.VCodeBean;
import com.iyuba.biaori.simplified.presenter.login.RegisterPresenter;
import com.iyuba.biaori.simplified.ui.BaseActivity;
import com.iyuba.biaori.simplified.ui.welcome.MyWebActivity;
import com.iyuba.biaori.simplified.util.LoginUtil;
import com.iyuba.biaori.simplified.util.MD5Util;
import com.iyuba.biaori.simplified.view.login.RegisterContract;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * 注册页面
 */
public class RegisterActivity extends BaseActivity<RegisterContract.RegisterView, RegisterContract.RegisterPresenter>
        implements RegisterContract.RegisterView {

    private ActivityRegisterBinding activityRegisterBinding;

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sp = getSharedPreferences(Constant.SP_USER, MODE_PRIVATE);
        initOperation();
        SMSSDK.registerEventHandler(eh);
    }


    /**
     * mob的短信验证
     */
    EventHandler eh = new EventHandler() {
        @Override
        public void afterEvent(int event, int result, Object data) {
            // TODO 此处为子线程！不可直接处理UI线程！处理后续操作需传到主线程中操作！
            if (result == SMSSDK.RESULT_COMPLETE) {
                //成功回调
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//成功
                    //提交短信、语音验证码成功

                    String phone = activityRegisterBinding.loginEtPhone.getText().toString();
                    String defaultName = "iyuba" + getRandom() + phone.substring(7);
                    String defaultPwd = phone.substring(5);

                    String sign = MD5Util.MD5("11002" + defaultName + MD5Util.MD5(defaultPwd) + "iyubaV2");
                    presenter.register(11002, phone, defaultName, MD5Util.MD5(defaultPwd), "android", Constant.APPID, "biaori", "json", sign);

                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    //获取短信验证码成功
                    toast("获取短信验证码成功");

                    getVCodeComplete(true);
                } else if (event == SMSSDK.EVENT_GET_VOICE_VERIFICATION_CODE) {
                    //获取语音验证码成功
                } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                    //返回支持发送验证码的国家列表
                } else if (event == SMSSDK.EVENT_GET_VERIFY_TOKEN_CODE) {
                    //本机验证获取token成功
//                    TokenVerifyResult tokenVerifyResult = (TokenVerifyResult) data;
                    //SMSSDK.login(phoneNum,tokenVerifyResult);
                } else if (event == SMSSDK.EVENT_VERIFY_LOGIN) {
                    //本机验证登陆成功
                }
            } else if (result == SMSSDK.RESULT_ERROR) {
                //失败回调
//                getVCodeComplete(false);
                toast("获取验证码失败或验证码失效");
            } else {
                //其他失败回调
                ((Throwable) data).printStackTrace();
            }
        }
    };

    private void initOperation() {

        activityRegisterBinding.toolbar.toolbarIvTitle.setText("注册");
        activityRegisterBinding.toolbar.toolbarIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        activityRegisterBinding.loginButGetVcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phone = activityRegisterBinding.loginEtPhone.getText().toString();
                if (phone.trim().length() != 11) {

                    toast("请输入正确的手机号");
                    return;
                }
                SMSSDK.getVerificationCode("611045", "86", phone);
            }
        });

        activityRegisterBinding.loginButRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phone = activityRegisterBinding.loginEtPhone.getText().toString();
                String vcode = activityRegisterBinding.registerEtVcode.getText().toString();
                if (phone.trim().length() != 11) {

                    toast("请输入正确的手机号");
                    return;
                }
                if (vcode.length() != 4) {

                    toast("验证码错误");
                    return;
                }
                if (!activityRegisterBinding.registerCb.isChecked()) {

                    toast("请勾选协议");
                    return;
                }

                //验证码验证
                SMSSDK.submitVerificationCode("86", phone, vcode);

            }
        });

        String priStr = "我已阅读并同意使用条款和隐私政策";
        SpannableString spannableString = new SpannableString(priStr);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {

                MyWebActivity.startActivity(RegisterActivity.this, Constant.URL_PROTOCOLUSE, "用户协议");
            }
        }, priStr.indexOf("使用条款"), priStr.indexOf("使用条款") + "使用条款".length(), SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {

                MyWebActivity.startActivity(RegisterActivity.this, Constant.URL_PROTOCOLPRI, "隐私政策");
            }
        }, priStr.indexOf("隐私政策"), priStr.indexOf("隐私政策") + "隐私政策".length(), SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);

        //协议
        activityRegisterBinding.registerTvPri.setText(spannableString);
        activityRegisterBinding.registerTvPri.setMovementMethod(LinkMovementMethod.getInstance());
        activityRegisterBinding.registerTvPri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (activityRegisterBinding.registerCb.isChecked()) {

                    activityRegisterBinding.registerCb.setChecked(false);
                } else {

                    activityRegisterBinding.registerCb.setChecked(true);
                }
            }
        });
        activityRegisterBinding.registerLlPri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (activityRegisterBinding.registerCb.isChecked()) {

                    activityRegisterBinding.registerCb.setChecked(false);
                } else {

                    activityRegisterBinding.registerCb.setChecked(true);
                }
            }
        });

    }


    // 返回随机四位数，用于拼接用户初始昵称
    private int getRandom() {
        double v = Math.random() * 9000 + 1000;
        return (int) v;
    }

    @Override
    public View initLayout() {
        activityRegisterBinding = ActivityRegisterBinding.inflate(getLayoutInflater());
        return activityRegisterBinding.getRoot();
    }

    @Override
    public RegisterContract.RegisterPresenter initPresenter() {
        return new RegisterPresenter();
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
    public void getVCodeComplete(boolean b) {


        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (b) {

                    activityRegisterBinding.loginButGetVcode.setTextColor(Color.parseColor("#00a490"));
                    activityRegisterBinding.loginButGetVcode.setBackgroundResource(R.drawable.shape_btn_grey_bg);
                    activityRegisterBinding.loginButGetVcode.setEnabled(false);
                    countDownTimer.start();
                } else {

                    activityRegisterBinding.loginButGetVcode.setTextColor(Color.WHITE);
                    activityRegisterBinding.loginButGetVcode.setBackgroundResource(R.drawable.shape_btn_bg);
                    activityRegisterBinding.loginButGetVcode.setEnabled(true);
                    activityRegisterBinding.loginButGetVcode.setText("获取验证码");
                }
            }
        });

    }

    @Override
    public void registerComplete(UserBean.UserinfoDTO userBean) {

        LoginUtil.login(userBean, sp);
        startActivity(new Intent(RegisterActivity.this, MainActivity.class));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eh);
        if (countDownTimer != null) {

            countDownTimer.cancel();
        }
    }

    CountDownTimer countDownTimer = new CountDownTimer(60000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {

            int s = (int) (millisUntilFinished / 1000);
            activityRegisterBinding.loginButGetVcode.setText(s + "s");
        }

        @Override
        public void onFinish() {

            activityRegisterBinding.loginButGetVcode.setTextColor(Color.WHITE);
            activityRegisterBinding.loginButGetVcode.setBackgroundResource(R.drawable.shape_btn_bg);
            activityRegisterBinding.loginButGetVcode.setEnabled(true);
            activityRegisterBinding.loginButGetVcode.setText("获取验证码");
        }
    };
}