package com.gyf.immersionbar.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.gyf.immersionbar.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by geyifeng on 2017/5/8.
 */

public class ColorActivity extends BaseActivity {

    @BindView(R.id.text_view)
    TextView textView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.btn1)
    Button btn1;
    @BindView(R.id.btn2)
    Button btn2;
    @BindView(R.id.btn3)
    Button btn3;
    @BindView(R.id.line1)
    LinearLayout linearLayout;
    @BindView(R.id.seek_bar)
    SeekBar seekBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color);
        ButterKnife.bind(this);
        ImmersionBar.with(this)
                .statusBarView(R.id.top_view)
                .statusBarColor(R.color.colorPrimary)
                .navigationBarColor(R.color.btn8)
                .init();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float alpha = (float) progress / 100;
                textView.setText("透明度:" + alpha + "f");
                ImmersionBar.with(ColorActivity.this)
                        .barAlpha(alpha)
                        .statusBarColorTransform(R.color.btn14)
                        .navigationBarColorTransform(R.color.btn3)
                        .addViewSupportTransformColor(mToolbar)
                        .addViewSupportTransformColor(btn1, R.color.btn1, R.color.btn4)
                        .addViewSupportTransformColor(btn2, R.color.btn3, R.color.btn12)
                        .addViewSupportTransformColor(btn3, R.color.btn5, R.color.btn10)
                        .addViewSupportTransformColor(linearLayout, R.color.darker_gray, R.color.btn5)
                        .init();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
