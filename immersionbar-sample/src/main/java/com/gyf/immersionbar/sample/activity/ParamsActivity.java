package com.gyf.immersionbar.sample.activity;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

import com.gyf.immersionbar.ImmersionBar;
import com.gyf.immersionbar.sample.R;

import butterknife.BindView;

/**
 * @author geyifeng
 */
public class ParamsActivity extends BaseActivity {

    @BindView(R.id.mToolbar)
    Toolbar mToolbar;
    @BindView(R.id.mTvStatus)
    TextView mTvStatus;
    @BindView(R.id.mTvHasNav)
    TextView mTvHasNav;
    @BindView(R.id.mTvNav)
    TextView mTvNav;
    @BindView(R.id.mTvNavWidth)
    TextView mTvNavWidth;
    @BindView(R.id.mTvAction)
    TextView mTvAction;
    @BindView(R.id.mTvHasNotch)
    TextView mTvHasNotch;
    @BindView(R.id.mTvInsets)
    TextView mTvInsets;
    @BindView(R.id.mTvNotchHeight)
    TextView mTvNotchHeight;
    @BindView(R.id.mTvFits)
    TextView mTvFits;
    @BindView(R.id.mTvStatusDark)
    TextView mTvStatusDark;
    @BindView(R.id.mTvNavigationDark)
    TextView mTvNavigationDark;
    @BindView(R.id.mBtnStatus)
    Button mBtnStatus;
    @BindView(R.id.mTvGesture)
    TextView mTvGesture;

    private boolean mIsHideStatusBar = false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_params;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        ImmersionBar.with(this).titleBar(mToolbar)
                .setOnNavigationBarListener((show, type) -> {
                    initView();
                    Toast.makeText(this, "导航栏" + (show ? "显示了" : "隐藏了"), Toast.LENGTH_SHORT).show();
                })
                .navigationBarColor(R.color.btn13).init();
    }

    @Override
    protected void initData() {
        super.initData();
        mToolbar.setTitle(getIntent().getCharSequenceExtra("title"));
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        super.initView();
        mTvStatus.setText(getText(getTitle(mTvStatus) + ImmersionBar.getStatusBarHeight(this)));
        mTvHasNav.setText(getText(getTitle(mTvHasNav) + ImmersionBar.hasNavigationBar(this)));
        mTvNav.setText(getText(getTitle(mTvNav) + ImmersionBar.getNavigationBarHeight(this)));
        mTvNavWidth.setText(getText(getTitle(mTvNavWidth) + ImmersionBar.getNavigationBarWidth(this)));
        mTvAction.setText(getText(getTitle(mTvAction) + ImmersionBar.getActionBarHeight(this)));
        mTvHasNotch.post(() -> mTvHasNotch.setText(getText(getTitle(mTvHasNotch) + ImmersionBar.hasNotchScreen(this))));
        mTvNotchHeight.post(() -> mTvNotchHeight.setText(getText(getTitle(mTvNotchHeight) + ImmersionBar.getNotchHeight(this))));
        mTvFits.setText(getText(getTitle(mTvFits) + ImmersionBar.checkFitsSystemWindows(findViewById(android.R.id.content))));
        mTvStatusDark.setText(getText(getTitle(mTvStatusDark) + ImmersionBar.isSupportStatusBarDarkFont()));
        mTvNavigationDark.setText(getText(getTitle(mTvNavigationDark) + ImmersionBar.isSupportNavigationIconDark()));
        mTvGesture.setText(getText(getTitle(mTvGesture) + ImmersionBar.isGesture(this)));
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void setListener() {
        super.setListener();
        mBtnStatus.setOnClickListener(v -> {
            if (!mIsHideStatusBar) {
                ImmersionBar.hideStatusBar(getWindow());
                mIsHideStatusBar = true;
            } else {
                ImmersionBar.showStatusBar(getWindow());
                mIsHideStatusBar = false;
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(mTvInsets, (view, windowInsetsCompat) -> {
            mTvInsets.setText(getText(getTitle(mTvInsets) + windowInsetsCompat.getSystemWindowInsetTop()));
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
}
