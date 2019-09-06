package com.gyf.immersionbar.sample.fragment.five;

import android.os.Bundle;

import com.gyf.immersionbar.ImmersionBar;
import com.gyf.immersionbar.sample.R;

/**
 * @author geyifeng
 * @date 2017/7/20
 */
public class ServiceFiveFragment extends BaseFiveFragment {

    public static ServiceFiveFragment newInstance() {
        Bundle args = new Bundle();
        ServiceFiveFragment fragment = new ServiceFiveFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_two_service;
    }

    @Override
    public void initImmersionBar() {
        super.initImmersionBar();
        ImmersionBar.with(this)
                .navigationBarColor(R.color.btn2)
                .keyboardEnable(false)
                .statusBarDarkFont(true, 0.2f)
                .navigationBarDarkIcon(true)
                .init();
    }
}
