package com.gyf.immersionbar.fragment.one;

import com.gyf.barlibrary.ImmersionBar;
import com.gyf.immersionbar.R;

/**
 * @author geyifeng
 * @date 2017/5/12
 */
public class ServiceOneFragment extends BaseLazyFragment {

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_one_service;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        ImmersionBar.with(this)
                .navigationBarColor(R.color.btn13)
                .init();
    }
}
