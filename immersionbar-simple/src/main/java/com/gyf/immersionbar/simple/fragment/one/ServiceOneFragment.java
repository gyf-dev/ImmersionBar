package com.gyf.immersionbar.simple.fragment.one;

import com.gyf.immersionbar.ImmersionBar;
import com.gyf.immersionbar.simple.R;
import com.gyf.immersionbar.simple.fragment.BaseImmersionFragment;

/**
 * @author geyifeng
 * @date 2017/5/12
 */
public class ServiceOneFragment extends BaseImmersionFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_one_service;
    }

    @Override
    public void initImmersionBar() {
        super.initImmersionBar();
        ImmersionBar.with(this)
                .navigationBarColor(R.color.btn13)
                .keyboardEnable(false)
                .init();
    }
}
