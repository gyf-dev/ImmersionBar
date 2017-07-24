package com.gyf.immersionbar.activity;

import com.gyf.immersionbar.R;

/**
 * Created by geyifeng on 2017/6/6.
 */

public class ShapeActivity extends BaseActivity {

    @Override
    protected int setLayoutId() {
        return R.layout.activity_shape;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(R.id.toolbar)
                .navigationBarColor(R.color.shape1)
                .init();
    }
}
