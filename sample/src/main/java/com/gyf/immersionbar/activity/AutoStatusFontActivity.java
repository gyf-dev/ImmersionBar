package com.gyf.immersionbar.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.design.animation.ArgbEvaluatorCompat;
import android.view.View;
import android.widget.SeekBar;

import com.gyf.barlibrary.ImmersionBar;
import com.gyf.immersionbar.R;

import butterknife.BindView;

/**
 * 自动调整状态栏字体
 *
 * @author github.com/dengyuhan
 * @date 2018/12/16 03:56
 */
public class AutoStatusFontActivity extends BaseActivity implements SeekBar.OnSeekBarChangeListener {

    @BindView(R.id.seek_bar)
    SeekBar seek_bar;

    @BindView(R.id.toolbar)
    View toolbar;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_auto_status_font;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        ImmersionBar.with(this)
                .enableAutoDarkFont(true)
                .titleBar(toolbar)
                .statusBarColorInt(Color.BLACK)
                .addViewSupportTransformColor(toolbar).init();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void setListener() {
        seek_bar.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        float fraction = (float) progress / 100;
        final Integer statusBarColor = ArgbEvaluatorCompat.getInstance().evaluate(fraction, Color.BLACK, Color.WHITE);

        ImmersionBar.with(this)
                .enableAutoDarkFont(true)
                .titleBar(toolbar)
                .statusBarColorInt(statusBarColor)
                .addViewSupportTransformColor(toolbar).init();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
