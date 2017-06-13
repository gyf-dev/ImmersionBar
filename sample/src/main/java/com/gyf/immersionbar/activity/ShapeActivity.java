package com.gyf.immersionbar.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.gyf.barlibrary.ImmersionBar;
import com.gyf.immersionbar.R;

/**
 * Created by geyifeng on 2017/6/6.
 */

public class ShapeActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shape);
        ImmersionBar.with(this)
                .titleBar(R.id.toolbar)
                .navigationBarColor(R.color.shape1)
                .init();
    }
}
