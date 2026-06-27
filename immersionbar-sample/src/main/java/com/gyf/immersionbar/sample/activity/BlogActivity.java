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
import com.gyf.immersionbar.sample.databinding.ActivityGitHubBinding;


import static android.view.KeyEvent.KEYCODE_BACK;

/**
 * @author geyifeng
 * @date 2017/8/3
 */

public class BlogActivity extends BaseActivity {

    private ActivityGitHubBinding binding;
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
            binding.webGit.loadUrl("https://github.com/gyf-dev/ImmersionBar");
        } else {
            binding.webGit.loadUrl("https://www.jianshu.com/p/2a884e211a62");
        }
        binding.webGit.setWebViewClient(new WebViewClient() {
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
        if ((keyCode == KEYCODE_BACK) && binding.webGit.canGoBack()) {
            binding.webGit.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (binding.webGit != null) {
                ViewParent parent = binding.webGit.getParent();
                if (parent != null) {
                    ((ViewGroup) parent).removeView(binding.webGit);
                }
                binding.webGit.stopLoading();
                // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
                binding.webGit.getSettings().setJavaScriptEnabled(false);
                binding.webGit.clearHistory();
                binding.webGit.clearView();
                binding.webGit.removeAllViews();
                binding.webGit.destroy();
            }
        } catch (Exception ignored) {
        }
    }
    @Override
    protected void initViewBinding() {
        binding = ActivityGitHubBinding.bind(getContentView());
    }

}
