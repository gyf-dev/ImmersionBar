package com.gyf.immersionbar.sample.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import com.google.android.material.animation.ArgbEvaluatorCompat;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;
import com.gyf.immersionbar.sample.R;
import com.gyf.immersionbar.sample.databinding.ActivityAutoStatusFontBinding;


/**
 * 自动调整状态栏字体
 *
 * @author github.com/dengyuhan
 * @date 2018/12/16 03:56
 */
public class AutoDarkModeActivity extends BaseActivity implements SeekBar.OnSeekBarChangeListener {

    private ActivityAutoStatusFontBinding binding;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_auto_status_font;
    }

    @Override
    protected void initImmersionBar() {
        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .statusBarColorInt(Color.BLACK)
                .navigationBarColorInt(Color.BLACK)
                .autoDarkModeEnable(true)
                .init();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void setListener() {
        binding.seekBar.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        float fraction = (float) progress / 100;
        Integer barColor = ArgbEvaluatorCompat.getInstance().evaluate(fraction, Color.BLACK, Color.WHITE);
        Integer textColor = ArgbEvaluatorCompat.getInstance().evaluate(fraction, Color.WHITE, Color.BLACK);

        binding.toolbar.setBackgroundColor(barColor);
        binding.tvTitle.setTextColor(textColor);

        ImmersionBar.with(this)
                .statusBarColorInt(barColor)
                .navigationBarColorInt(barColor)
                .init();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
    @Override
    protected void initViewBinding() {
        binding = ActivityAutoStatusFontBinding.bind(getContentView());
    }

}
