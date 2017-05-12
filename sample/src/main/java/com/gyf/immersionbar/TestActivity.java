package com.gyf.immersionbar;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.gyf.barlibrary.ImmersionBar;

/**
 * Created by gyf on 2016/10/24.
 */
public class TestActivity extends BaseActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private Toolbar toolbar;
    private View topView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this).transparentStatusBar().navigationBarColor(R.color.colorPrimary).fullScreen(true).init();
        setContentView(R.layout.activity_test);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("图片状态栏");
        toolbar.setTitleTextColor(Color.WHITE);
        topView = findViewById(R.id.top_view);
        Button btn_status_color = (Button) findViewById(R.id.btn_status_color);
        Button btn_navigation_color = (Button) findViewById(R.id.btn_navigation_color);
        Button btn_color = (Button) findViewById(R.id.btn_color);
        SeekBar seekBar = (SeekBar) findViewById(R.id.seek_bar);
        btn_status_color.setOnClickListener(this);
        btn_navigation_color.setOnClickListener(this);
        btn_color.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_status_color:
                ImmersionBar.with(this).statusBarColor(R.color.colorAccent).removeSupportView().init();
                break;
            case R.id.btn_navigation_color:
                ImmersionBar.with(this).navigationBarColor(R.color.colorAccent).init();
                break;
            case R.id.btn_color:
                ImmersionBar.with(this)
                        .transparentStatusBar()
                        .navigationBarColor(R.color.colorPrimary)
                        .init();
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        float alpha = (float) progress / 100;
        ImmersionBar.with(this)
                .barColorTransform(R.color.orange)
                .navigationBarColorTransform(R.color.tans)
                .addViewSupportTransformColor(toolbar)
                .barAlpha(alpha)
                .init();
//        topView.setBackgroundColor(ColorUtils.blendARGB(ContextCompat.getColor(this, R.color.tans), ContextCompat.getColor(this, R.color.colorPrimary), alpha));
//        toolbar.setBackgroundColor(ColorUtils.blendARGB(ContextCompat.getColor(this, R.color.tans), ContextCompat.getColor(this, R.color.colorPrimary), alpha));

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
