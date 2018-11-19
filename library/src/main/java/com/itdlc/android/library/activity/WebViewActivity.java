package com.itdlc.android.library.activity;

import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.itdlc.android.library.Const;
import com.itdlc.android.library.R;
import com.itdlc.android.library.base.BaseActivity;

import org.simple.eventbus.EventBus;

/**
 * Created by Administrator on 2018/2/2.
 * 服务协议
 */

public class WebViewActivity extends BaseActivity {
    private WebView serviceWebview;
    private TextView tvBottom;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_webview;
    }

    @Override
    protected void initialView() {
        super.initialView();

        serviceWebview = findViewById(R.id.web_view);

        setTitle(getIntent().getStringExtra("title"));

        String strBottom = getIntent().getStringExtra("bottom");
        if (!TextUtils.isEmpty(strBottom)) {
            tvBottom = findViewById(R.id.tv_bottom);
            tvBottom.setText(strBottom);
            tvBottom.setVisibility(View.VISIBLE);
            tvBottom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventBus.getDefault().post(0, Const.Event.WEB_VIEW_BOTTOM_CLICK);
                    finish();
                }
            });
        }

        WebSettings settings = serviceWebview.getSettings();
        settings.setLoadsImagesAutomatically(true);
        settings.setJavaScriptEnabled(true);
        if (getIntent().getBooleanExtra("zoom", false)) {
            settings.setSupportZoom(true);
            settings.setBuiltInZoomControls(true);
            settings.setDisplayZoomControls(false);
          View  mStatusBarHolder = findViewById(R.id.status_bar_placeholder);
            if (mStatusBarHolder != null) {
                mStatusBarHolder.setVisibility(View.GONE);
            }
        }
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);

        serviceWebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        serviceWebview.loadUrl(getIntent().getStringExtra("url"));
    }
}
