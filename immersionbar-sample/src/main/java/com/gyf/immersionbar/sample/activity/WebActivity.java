package com.gyf.immersionbar.sample.activity;

import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.gyf.immersionbar.ImmersionBar;
import com.gyf.immersionbar.sample.R;

import butterknife.BindView;

/**
 * @author geyifeng
 * @date 2017/6/19
 */
public class WebActivity extends BaseActivity {
    @BindView(R.id.web)
    WebView mWebView;
    @BindView(R.id.line)
    LinearLayout layout;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_web;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        ImmersionBar.with(this).titleBar(R.id.toolbar)
                //解决软键盘与底部输入框冲突问题
                .keyboardEnable(true)
                .init();
    }

    @Override
    protected void initView() {
        mWebView.loadUrl("file:///android_asset/input_webview.html");
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
