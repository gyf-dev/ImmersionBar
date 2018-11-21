package com.gyf.immersionbar.fragment.two;

import com.gyf.barlibrary.ImmersionBar;
import com.gyf.immersionbar.R;
import com.gyf.immersionbar.fragment.BaseFragment;

/**
 * @author geyifeng
 * @date 2017/7/20
 */

public class HomeTwoFragment extends BaseFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_two_home;
    }

    @Override
    public void initImmersionBar() {
        super.initImmersionBar();
        ImmersionBar.with(this)
                .navigationBarColor(R.color.colorPrimary)
                .keyboardEnable(false)
                .init();
    }
}
