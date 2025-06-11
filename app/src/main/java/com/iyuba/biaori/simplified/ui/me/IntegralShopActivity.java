package com.iyuba.biaori.simplified.ui.me;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.iyuba.biaori.simplified.databinding.ActivityIntegralShopBinding;


/**
 * 积分商城
 */
public class IntegralShopActivity extends AppCompatActivity {

    private ActivityIntegralShopBinding activityIntegralShopBinding;

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityIntegralShopBinding = ActivityIntegralShopBinding.inflate(getLayoutInflater());
        setContentView(activityIntegralShopBinding.getRoot());

        getBundle();
        initOperation();
    }


    private void getBundle() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            url = bundle.getString("URL");
        }
    }

    /**
     * @param activity
     * @param url      链接地址
     */
    public static void startActivity(Activity activity, String url) {

        Intent intent = new Intent(activity, IntegralShopActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("URL", url);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    private void initOperation() {

        activityIntegralShopBinding.isIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        WebSettings webSettings = activityIntegralShopBinding.isWv.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);


        activityIntegralShopBinding.isWv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        activityIntegralShopBinding.isWv.setWebChromeClient(new WebChromeClient() {

            public void onProgressChanged(WebView view, int progress) {
                IntegralShopActivity.this.setProgress(progress * 100);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
//                if (TextUtils.isEmpty(titleStr)) {
//                    textView.setText(title);
//                }
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {


                return false;
            }

        });

        activityIntegralShopBinding.isWv.setDownloadListener(new DownloadListener() {

            @Override
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        activityIntegralShopBinding.isWv.loadUrl(url);
    }


    @Override
    public void onBackPressed() {
        if (activityIntegralShopBinding.isWv.canGoBack()) {
            activityIntegralShopBinding.isWv.goBack(); // goBack()表示返回webView的上一页面
        } else if (!activityIntegralShopBinding.isWv.canGoBack()) {

            super.onBackPressed();
        }
    }
}