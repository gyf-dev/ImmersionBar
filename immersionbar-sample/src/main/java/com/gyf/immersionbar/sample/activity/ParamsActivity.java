package com.gyf.immersionbar.sample.activity;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

import com.gyf.immersionbar.BarProperties;
import com.gyf.immersionbar.ImmersionBar;
import com.gyf.immersionbar.sample.R;
import com.gyf.immersionbar.sample.databinding.ActivityParamsBinding;


/**
 * @author geyifeng
 */
public class ParamsActivity extends BaseActivity {

    private ActivityParamsBinding binding;
    private boolean mIsHideStatusBar = false;
    /**
     * Bar最新快照，由OnBarPropertiesChangedListener回调下发
     */
    private BarProperties mBarProperties;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_params;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        ImmersionBar.with(this).titleBar(binding.toolbar)
                .addOnBarPropertiesChangedListener(barProperties -> {
                    Log.d(mTag, "onBarPropertiesChanged: " + barProperties);
                    mBarProperties = barProperties;
                    initView();
                })
                .addOnStatusBarChangedListener(statusBar -> {
                    Log.d(mTag, "onStatusBarChanged: " + statusBar);
                    if (!statusBar.isFirstCallback()) {
                        Toast.makeText(this, "状态栏" + (statusBar.isVisible() ? "显示了" : "隐藏了"), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnNavigationBarChangedListener(navigationBar -> {
                    Log.d(mTag, "onNavigationBarChanged: " + navigationBar);
                    if (!navigationBar.isFirstCallback()) {
                        Toast.makeText(this, "导航栏" + (navigationBar.isVisible() ? "显示了" : "隐藏了"), Toast.LENGTH_SHORT).show();
                    }
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
        // 等待OnBarPropertiesChangedListener回调下发Bar快照后再刷新UI
        if (mBarProperties == null) {
            return;
        }
        BarProperties p = mBarProperties;
        binding.tvPortrait.setText(getText(getTitle(binding.tvPortrait) + p.isPortrait()));
        binding.tvLandscapeLeft.setText(getText(getTitle(binding.tvLandscapeLeft) + p.isLandscapeLeft()));
        binding.tvLandscapeRight.setText(getText(getTitle(binding.tvLandscapeRight) + p.isLandscapeRight()));
        binding.tvStatus.setText(getText(getTitle(binding.tvStatus) + p.getStatusBarHeight()));
        binding.tvStatusIgnoringVisibility.setText(getText(getTitle(binding.tvStatusIgnoringVisibility)
                + p.getStatusBarHeightIgnoringVisibility()));
        binding.tvStatusVisible.setText(getText(getTitle(binding.tvStatusVisible) + p.isStatusBarVisible()));
        binding.tvHasNav.setText(getText(getTitle(binding.tvHasNav) + p.hasNavigationBar()));
        binding.tvNavVisible.setText(getText(getTitle(binding.tvNavVisible) + p.isNavigationBarVisible()));
        binding.tvNavAtBottom.setText(getText(getTitle(binding.tvNavAtBottom) + p.isNavigationAtBottom()));
        binding.tvNavType.setText(getText(getTitle(binding.tvNavType) + p.getNavigationBarType()));
        binding.tvNav.setText(getText(getTitle(binding.tvNav) + p.getNavigationBarHeight()));
        binding.tvNavIgnoringVisibility.setText(getText(getTitle(binding.tvNavIgnoringVisibility)
                + p.getNavigationBarHeightIgnoringVisibility()));
        binding.tvNavWidth.setText(getText(getTitle(binding.tvNavWidth) + p.getNavigationBarWidth()));
        binding.tvGesture.setText(getText(getTitle(binding.tvGesture) + p.isGestureNavigation()));
        binding.tvHasNotch.setText(getText(getTitle(binding.tvHasNotch) + p.isNotchScreen()));
        binding.tvNotchHeight.setText(getText(getTitle(binding.tvNotchHeight) + p.getNotchHeight()));
        binding.tvAction.setText(getText(getTitle(binding.tvAction) + p.getActionBarHeight()));
        // 以下信息不在BarProperties快照中，仍使用静态方法获取
        binding.tvFits.setText(getText(getTitle(binding.tvFits) + ImmersionBar.checkFitsSystemWindows(findViewById(android.R.id.content))));
        binding.tvStatusDark.setText(getText(getTitle(binding.tvStatusDark) + ImmersionBar.isSupportStatusBarDarkFont()));
        binding.tvNavigationDark.setText(getText(getTitle(binding.tvNavigationDark) + ImmersionBar.isSupportNavigationIconDark()));
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
