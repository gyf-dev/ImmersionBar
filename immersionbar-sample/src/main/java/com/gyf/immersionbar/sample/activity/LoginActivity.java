package com.gyf.immersionbar.sample.activity;

import com.gyf.immersionbar.ImmersionBar;
import com.gyf.immersionbar.sample.R;

/**
 * @author geyifeng
 */
public class LoginActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        ImmersionBar.with(this).titleBar(R.id.toolbar).keyboardEnable(true).init();
    }

}
