package com.gyf.immersionbar.sample.fragment.two;

import com.gyf.immersionbar.ImmersionBar;
import com.gyf.immersionbar.sample.R;
import com.gyf.immersionbar.sample.fragment.BaseImmersionFragment;

/**
 * @author geyifeng
 * @date 2017/7/20
 */

public class CategoryTwoFragment extends BaseImmersionFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_two_category;
    }

    @Override
    public void initImmersionBar() {
        super.initImmersionBar();
        ImmersionBar.with(this)
                .navigationBarColor(R.color.btn1)
                .init();
    }
}
