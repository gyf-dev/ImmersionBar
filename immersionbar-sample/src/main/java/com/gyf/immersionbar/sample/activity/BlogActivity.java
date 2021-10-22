package com.gyf.immersionbar.sample.activity;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gyf.immersionbar.ImmersionBar;
import com.gyf.immersionbar.sample.R;

import butterknife.BindView;

import static android.view.KeyEvent.KEYCODE_BACK;

/**
 * @author geyifeng
 * @date 2017/8/3
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
    protected int getLayoutId() {
        return R.layout.activity_git_hub;
    }

    @Override
    protected void initView() {
        if ("github".equals(blog)) {
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
        if ("github".equals(blog)) {
            ImmersionBar.with(this).fitsSystemWindows(true).statusBarColor(R.color.github_color).init();
        } else {
            ImmersionBar.with(this).fitsSystemWindows(true).statusBarColorInt(Color.WHITE).statusBarDarkFont(true, 0.2f).init();
        }
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
                ViewParent parent = mWebView.getParent();
                if (parent != null) {
                    ((ViewGroup) parent).removeView(mWebView);
                }
                mWebView.stopLoading();
                // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
                mWebView.getSettings().setJavaScriptEnabled(false);
                mWebView.clearHistory();
                mWebView.clearView();
                mWebView.removeAllViews();
                mWebView.destroy();
                mWebView = null;
            }
        } catch (Exception ignored) {
        }
    }
}
