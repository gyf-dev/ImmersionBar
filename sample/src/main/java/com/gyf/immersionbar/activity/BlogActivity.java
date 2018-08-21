package com.gyf.immersionbar.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gyf.immersionbar.R;

import butterknife.BindView;

import static android.view.KeyEvent.KEYCODE_BACK;

/**
 * Created by geyifeng on 2017/8/3.
 */

public class BlogActivity extends BaseActivity {

    @BindView(R.id.web_git)
    WebView mWebView;
    private String blog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        blog = getIntent().getBundleExtra("bundle").getString("blog");
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_git_hub;
    }

    @Override
    protected void initView() {
        if (blog.equals("github")) {
            mWebView.loadUrl("https://github.com/gyf-dev/ImmersionBar");
        } else {
            mWebView.loadUrl("https://www.jianshu.com/p/2a884e211a62");
        }
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.toString());
                return true;
            }
        });
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        if (blog.equals("github")) {
            mImmersionBar.fitsSystemWindows(true).statusBarColor(R.color.github_color).init();
        } else
            mImmersionBar.fitsSystemWindows(true).statusBarColorInt(Color.WHITE).statusBarDarkFont(true, 0.2f).init();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (mWebView != null) {
                mWebView.removeAllViews();
                mWebView.destroy();
                mWebView = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
