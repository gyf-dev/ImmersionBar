package com.gyf.immersionbar.sample.activity;

import android.annotation.SuppressLint;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.ActionBar;

import com.gyf.immersionbar.ImmersionBar;
import com.gyf.immersionbar.sample.R;
import com.gyf.immersionbar.sample.databinding.ActivityWeb2Binding;

/**
 * Issue #584真实复现页：在WebView中打开移动版百度并点击网页搜索框唤起键盘。
 */
public class Web2Activity extends BaseActivity {

    private static final String BAIDU_URL = "https://m.baidu.com/";

    private ActivityWeb2Binding binding;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_web2;
    }

    @Override
    protected void initImmersionBar() {
        ImmersionBar.with(this)
                .statusBarColor(R.color.issue_584_purple)
                .navigationBarColor(R.color.issue_584_purple)
                .titleBar(binding.toolbar)
                .keyboardEnable(true)
                .init();
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void initView() {
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        WebSettings settings = binding.web.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        binding.web.setWebViewClient(new WebViewClient());
        binding.web.loadUrl(BAIDU_URL);
    }

    @Override
    protected void setListener() {
        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && binding.web.canGoBack()) {
            binding.web.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        destroyWebView(binding.web);
        super.onDestroy();
    }

    @Override
    protected void initViewBinding() {
        binding = ActivityWeb2Binding.bind(getContentView());
    }

    private void destroyWebView(WebView webView) {
        try {
            ViewParent parent = webView.getParent();
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(webView);
            }
            webView.stopLoading();
            webView.getSettings().setJavaScriptEnabled(false);
            webView.clearHistory();
            webView.removeAllViews();
            webView.destroy();
        } catch (Exception ignored) {
        }
    }
}
