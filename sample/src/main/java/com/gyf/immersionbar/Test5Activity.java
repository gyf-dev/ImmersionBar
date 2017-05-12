package com.gyf.immersionbar;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gyf.barlibrary.BarParams;
import com.gyf.barlibrary.ImmersionBar;

/**
 * Created by geyifeng on 2017/5/8.
 */

public class Test5Activity extends BaseActivity {

    private Toolbar mToolbar;
    private Button btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this)
                .statusBarColor(R.color.colorPrimary)
                .fitsSystemWindows(true)
                .fullScreen(true)
                .init();
        setContentView(R.layout.activity_test5);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setTitle("状态栏配合fitsSystemWindows使用");
        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BarParams barParams = ImmersionBar.with(Test5Activity.this).getBarParams();
                if(barParams.fits){
                    ImmersionBar.with(Test5Activity.this)
                            .transparentStatusBar()
                            .fitsSystemWindows(false)
                            .init();
                    btn.setText("fitsSystemWindows演示:false");
                }else {
                    ImmersionBar.with(Test5Activity.this)
                            .statusBarColor(R.color.colorPrimary)
                            .fitsSystemWindows(true)
                            .fullScreen(true)
                            .init();
                    btn.setText("fitsSystemWindows演示:true");
                }
            }
        });
    }
}
