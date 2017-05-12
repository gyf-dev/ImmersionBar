package com.gyf.immersionbar;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;

/**
 * Created by geyifeng on 2017/5/8.
 */

public class Test3Activity extends BaseActivity {

    private TextView textView;
    private Toolbar mToolbar;
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this)
                .statusBarColor(R.color.colorPrimary)
                .navigationBarColor(R.color.btn8)
                .fullScreen(true)
                .init();
        setContentView(R.layout.activity_test3);
        linearLayout = (LinearLayout) findViewById(R.id.line1);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setTitle("通过透明度设置状态栏");
        textView = (TextView) findViewById(R.id.text_view);
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        SeekBar seekBar = (SeekBar) findViewById(R.id.seek_bar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float alpha = (float) progress / 100;
                textView.setText("透明度:" + alpha + "f");
                linearLayout.setBackgroundColor(ColorUtils.blendARGB(
                        ContextCompat.getColor(Test3Activity.this,R.color.darker_gray),
                        ContextCompat.getColor(Test3Activity.this,R.color.btn5),alpha));
                ImmersionBar.with(Test3Activity.this)
                        .barAlpha(alpha)
                        .statusBarColorTransform(R.color.btn14)
                        .navigationBarColorTransform(R.color.btn3)
                        .setViewSupportTransformColor(mToolbar)
                        .addViewSupportTransformColor(btn1, R.color.btn1, R.color.btn4)
                        .addViewSupportTransformColor(btn2, R.color.btn3, R.color.btn12)
                        .addViewSupportTransformColor(btn3, R.color.btn5, R.color.btn10)
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
