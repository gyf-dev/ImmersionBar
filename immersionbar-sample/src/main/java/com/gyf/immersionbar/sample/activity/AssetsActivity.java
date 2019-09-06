package com.gyf.immersionbar.sample.activity;

import com.gyf.immersionbar.ImmersionBar;
import com.gyf.immersionbar.sample.R;

/**
 * @author geyifeng
 * @date 2019/3/25 2:47 PM
 */
public class AssetsActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_assets;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        ImmersionBar.with(this).fitsSystemWindows(true).statusBarColor("#F56A3D").keyboardEnable(true).init();
    }
}
