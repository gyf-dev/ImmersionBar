package com.gyf.immersionbar;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;

/**
 * Created by geyifeng on 2017/5/8.
 */

public class Test4Activity extends BaseActivity {

    private TextView textView;
    private Toolbar mToolbar;
    private Button btn1;
    private Button btn2;
    private Button btn3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this)
                .navigationBarColor(R.color.colorPrimary)
                .fullScreen(true)
                .init();
        setContentView(R.layout.activity_test4);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setTitle("配合侧滑返回一起使用");
    }

    @Override
    public boolean isSupportSwipeBack() {
        return true;
    }
}
