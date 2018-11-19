package com.itdlc.android.library.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.itdlc.android.library.R;
import com.itdlc.android.library.base.BaseActivity;
import com.itdlc.android.library.utils.ImageUtils;

public class WebViewContentActivity extends BaseActivity {
    private WebView mWebView;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_webview;
    }

    @Override
    protected void initialView() {
        super.initialView();
        mWebView = findViewById(R.id.web_view);
        setTitle(getIntent().getStringExtra("title"));
        initWebView();
    }

    public static void start(Context context, String title, String content) {
        Intent intent = new Intent(context, WebViewContentActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("content", content);
        context.startActivity(intent);
    }

    @SuppressLint("JavascriptInterface")
    private void initWebView() {

        WebSettings webSettings = mWebView.getSettings();
        // 设置与Js交互的权限
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);//关键点 `wode

        // 先载入JS代码
        // 格式规定为:file:///android_asset/文件名.html
        mWebView.loadUrl("file:///android_asset/wqContent/content.html");

        mWebView.addJavascriptInterface(this, "Android");
        mWebView.requestFocus();
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
    }

    @JavascriptInterface
    public void setValue(String productID) {
        Log.e(TAG, "callOnJs2: " + productID);
    }

    @JavascriptInterface
    public void showImgDetail(String imgSrc, String curSrc) {
        Log.e(TAG, "showImgDetail: " + imgSrc + " : " + curSrc);
    }

    @JavascriptInterface
    public String getValue() {
        return ImageUtils.transformAllUrl(getIntent().getStringExtra("content"));
    }
}
