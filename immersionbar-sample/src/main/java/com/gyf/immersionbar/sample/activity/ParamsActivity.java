package com.gyf.immersionbar.sample.activity;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

import com.gyf.immersionbar.ImmersionBar;
import com.gyf.immersionbar.sample.R;
import com.gyf.immersionbar.sample.databinding.ActivityParamsBinding;


/**
 * @author geyifeng
 */
public class ParamsActivity extends BaseActivity {

    private ActivityParamsBinding binding;
    private boolean mIsHideStatusBar = false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_params;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        ImmersionBar.with(this).titleBar(binding.toolbar)
                .setOnStatusBarListener(show -> Toast.makeText(ParamsActivity.this, "状态栏" + (show ? "显示了" : "隐藏了"), Toast.LENGTH_SHORT).show())
                .setOnNavigationBarListener((show, type) -> {
                    initView();
                    Toast.makeText(this, "导航栏" + (show ? "显示了" : "隐藏了"), Toast.LENGTH_SHORT).show();
                })
                .navigationBarColor(R.color.btn13).init();
    }

    @Override
    protected void initData() {
        super.initData();
        binding.toolbar.setTitle(getIntent().getCharSequenceExtra("title"));
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        super.initView();
        binding.tvStatus.setText(getText(getTitle(binding.tvStatus) + ImmersionBar.getStatusBarHeight(this)));
        binding.tvHasNav.setText(getText(getTitle(binding.tvHasNav) + ImmersionBar.hasNavigationBar(this)));
        binding.tvNav.setText(getText(getTitle(binding.tvNav) + ImmersionBar.getNavigationBarHeight(this)));
        binding.tvNavWidth.setText(getText(getTitle(binding.tvNavWidth) + ImmersionBar.getNavigationBarWidth(this)));
        binding.tvAction.setText(getText(getTitle(binding.tvAction) + ImmersionBar.getActionBarHeight(this)));
        binding.tvHasNotch.post(() -> binding.tvHasNotch.setText(getText(getTitle(binding.tvHasNotch) + ImmersionBar.hasNotchScreen(this))));
        binding.tvNotchHeight.post(() -> binding.tvNotchHeight.setText(getText(getTitle(binding.tvNotchHeight) + ImmersionBar.getNotchHeight(this))));
        binding.tvFits.setText(getText(getTitle(binding.tvFits) + ImmersionBar.checkFitsSystemWindows(findViewById(android.R.id.content))));
        binding.tvStatusDark.setText(getText(getTitle(binding.tvStatusDark) + ImmersionBar.isSupportStatusBarDarkFont()));
        binding.tvNavigationDark.setText(getText(getTitle(binding.tvNavigationDark) + ImmersionBar.isSupportNavigationIconDark()));
        binding.tvGesture.setText(getText(getTitle(binding.tvGesture) + ImmersionBar.isGesture(this)));
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void setListener() {
        super.setListener();
        binding.btnStatus.setOnClickListener(v -> {
            if (!mIsHideStatusBar) {
                ImmersionBar.hideStatusBar(getWindow());
                mIsHideStatusBar = true;
            } else {
                ImmersionBar.showStatusBar(getWindow());
                mIsHideStatusBar = false;
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(binding.tvInsets, (view, windowInsetsCompat) -> {
            binding.tvInsets.setText(getText(getTitle(binding.tvInsets) + windowInsetsCompat.getSystemWindowInsetTop()));
            return windowInsetsCompat.consumeSystemWindowInsets();
        });
    }

    private SpannableString getText(String text) {
        String[] split = text.split(" {3}");
        SpannableString spannableString = new SpannableString(text);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(ContextCompat.getColor(this, R.color.btn3));
        spannableString.setSpan(colorSpan, text.length() - split[1].length(), text.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    private String getTitle(TextView textView) {
        String[] split = textView.getText().toString().split(" {3}");
        return split[0] + "   ";
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        initView();
    }

    @Override
    protected void initViewBinding() {
        binding = ActivityParamsBinding.bind(getContentView());
    }

}
