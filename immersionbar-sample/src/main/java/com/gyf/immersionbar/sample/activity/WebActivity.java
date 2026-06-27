package com.gyf.immersionbar.sample.activity;

import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.gyf.immersionbar.ImmersionBar;
import com.gyf.immersionbar.sample.R;
import com.gyf.immersionbar.sample.databinding.ActivityWebBinding;


/**
 * @author geyifeng
 * @date 2017/6/19
 */
public class WebActivity extends BaseActivity {

    private ActivityWebBinding binding;
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
        binding.web.loadUrl("file:///android_asset/input_webview.html");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (binding.web != null) {
                ViewParent parent = binding.web.getParent();
                if (parent != null) {
                    ((ViewGroup) parent).removeView(binding.web);
                }
                binding.web.stopLoading();
                // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
                binding.web.getSettings().setJavaScriptEnabled(false);
                binding.web.clearHistory();
                binding.web.clearView();
                binding.web.removeAllViews();
                binding.web.destroy();
            }
        } catch (Exception ignored) {
        }
    }
    @Override
    protected void initViewBinding() {
        binding = ActivityWebBinding.bind(getContentView());
    }

}
