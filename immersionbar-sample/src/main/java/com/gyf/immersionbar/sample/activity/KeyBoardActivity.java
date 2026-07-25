package com.gyf.immersionbar.sample.activity;

import android.content.Context;
import android.os.Build;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.core.content.ContextCompat;

import com.gyf.immersionbar.BarProperties;
import com.gyf.immersionbar.ImmersionBar;
import com.gyf.immersionbar.sample.R;
import com.gyf.immersionbar.sample.databinding.ActivityKeyBoardBinding;

import java.util.Locale;

/**
 * 软键盘与底部输入框适配验证页。
 *
 * @author geyifeng
 * @date 2017/5/8
 */
public class KeyBoardActivity extends BaseActivity {

    private ActivityKeyBoardBinding binding;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_key_board;
    }

    @Override
    protected void initImmersionBar() {
        ImmersionBar.with(this)
                .navigationBarColor(R.color.colorPrimary)
                .titleBar(binding.toolbar)
                .keyboardEnable(true)
                .setOnKeyboardListener(this::renderKeyboardState)
                .addOnBarPropertiesChangedListener(this::renderEnvironment)
                .init();
    }

    @Override
    protected void initView() {
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        renderKeyboardState(false, 0);
        binding.tvEnvironment.setText(buildEnvironmentText(null));
    }

    @Override
    protected void setListener() {
        binding.toolbar.setNavigationOnClickListener(v -> finish());
        binding.switchKeyboardEnable.setOnCheckedChangeListener((buttonView, isChecked) -> {
            ImmersionBar.with(this).keyboardEnable(isChecked).init();
            binding.tvKeyboardMode.setText(isChecked
                    ? "keyboardEnable(true)：库接管键盘适配"
                    : "keyboardEnable(false)：用于对比系统默认行为");
            if (!isChecked) {
                hideKeyboard();
            }
        });
        binding.btnShowKeyboard.setOnClickListener(v -> showKeyboard(binding.editBottom));
        binding.btnHideKeyboard.setOnClickListener(v -> hideKeyboard());
    }

    @Override
    protected void initViewBinding() {
        binding = ActivityKeyBoardBinding.bind(getContentView());
    }

    private void renderKeyboardState(boolean isPopup, int keyboardHeight) {
        float density = getResources().getDisplayMetrics().density;
        String state = isPopup ? "已弹出" : "已关闭";
        binding.tvKeyboardState.setText(String.format(Locale.getDefault(),
                "键盘状态：%s   高度：%d px / %.1f dp",
                state, keyboardHeight, keyboardHeight / density));
        binding.tvKeyboardState.setTextColor(ContextCompat.getColor(this,
                isPopup ? R.color.green : R.color.black));
    }

    private void renderEnvironment(BarProperties properties) {
        binding.tvEnvironment.setText(buildEnvironmentText(properties));
    }

    private String buildEnvironmentText(BarProperties properties) {
        String navigationMode = "等待系统栏首次回调";
        int navigationBarHeight = 0;
        if (properties != null) {
            navigationMode = properties.isGestureNavigation()
                    ? "手势导航 / " + properties.getNavigationBarType()
                    : "经典导航键 / " + properties.getNavigationBarType();
            navigationBarHeight = properties.getNavigationBarHeight();
        }
        String implementation = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
                ? "WindowInsets.Type.ime()"
                : "getWindowVisibleDisplayFrame()";
        return String.format(Locale.getDefault(),
                "Android %s (API %d)  targetSdk %d\n导航模式：%s\n导航栏高度：%d px\n键盘分支：%s",
                Build.VERSION.RELEASE, Build.VERSION.SDK_INT,
                getApplicationInfo().targetSdkVersion, navigationMode,
                navigationBarHeight, implementation);
    }

    private void showKeyboard(EditText editText) {
        editText.requestFocus();
        editText.postDelayed(() -> {
            InputMethodManager manager =
                    (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (manager != null) {
                manager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 100);
    }

    private void hideKeyboard() {
        InputMethodManager manager =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager != null) {
            manager.hideSoftInputFromWindow(binding.getRoot().getWindowToken(), 0);
        }
        binding.line.requestFocus();
    }
}
