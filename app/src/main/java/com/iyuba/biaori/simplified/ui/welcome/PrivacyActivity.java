package com.iyuba.biaori.simplified.ui.welcome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.iyuba.biaori.simplified.databinding.ActivityMyWebBinding;
import com.iyuba.biaori.simplified.databinding.ActivityPrivacyBinding;

/**
 * 主要是显示隐私协议的
 */
public class PrivacyActivity extends AppCompatActivity {

    private ActivityPrivacyBinding activityPrivacyBinding;

    private String url;

    private String toolbarName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityPrivacyBinding = ActivityPrivacyBinding.inflate(getLayoutInflater());
        setContentView(activityPrivacyBinding.getRoot());

        getBundle();

        activityPrivacyBinding.toolbar.toolbarIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        activityPrivacyBinding.toolbar.toolbarIvTitle.setText(toolbarName);
        activityPrivacyBinding.webWv.loadUrl(url);
    }


    private void getBundle() {

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {

            url = bundle.getString("URL");
            toolbarName = bundle.getString("TOOLBAR_NAME");
        }
    }

    public static void startActivity(Activity activity, String url, String toolbarName) {

        Intent intent = new Intent(activity, PrivacyActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("URL", url);
        bundle.putString("TOOLBAR_NAME", toolbarName);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }
}