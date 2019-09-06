package com.gyf.immersionbar.sample.fragment.one;

import com.gyf.immersionbar.ImmersionBar;
import com.gyf.immersionbar.sample.R;
import com.gyf.immersionbar.sample.fragment.BaseImmersionFragment;

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
