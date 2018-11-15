package com.gyf.immersionbar.fragment.two;

import com.gyf.barlibrary.ImmersionBar;
import com.gyf.immersionbar.R;
import com.gyf.immersionbar.fragment.BaseFragment;

/**
 * @author geyifeng
 * @date 2017/7/20
 */
public class ServiceTwoFragment extends BaseFragment {
    @Override
    protected int setLayoutId() {
        return R.layout.fragment_two_service;
    }

    @Override
    public void initImmersionBar() {
        super.initImmersionBar();
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f)
                .navigationBarColor(R.color.btn2)
                .keyboardEnable(false)
                .init();
    }
}
