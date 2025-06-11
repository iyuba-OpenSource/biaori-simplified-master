package com.iyuba.biaori.simplified.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.iyuba.biaori.simplified.R;

import java.text.DecimalFormat;

/**
 * 用dialog activity实现弹窗效果,用于奖励的弹窗
 */
public class AlertActivity extends AppCompatActivity {

    private int reward;

    public static void startActivity(Activity activity, int reward) {

        Intent intent = new Intent(activity, AlertActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("REWARD", reward);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    private void getBundle() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            reward = bundle.getInt("REWARD");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        getBundle();

        double rewardDouble = reward / 100.0f;
        DecimalFormat decimalFormat = new DecimalFormat("#.##");


        new AlertDialog.Builder(this)
                .setTitle("恭喜您！")
                .setMessage("本次学习获得" + decimalFormat.format(rewardDouble) + "元红包奖励,已自动存入您的钱包账户。\n红包可在【爱语吧】微信公众号提现，继续学习领取更多红包奖励吧！")
                .setPositiveButton("好的", (dialog, which) -> {

                    dialog.dismiss();
                    finish();
                })
                .show();

    }
}