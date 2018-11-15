package com.gyf.immersionbar.fragment.two;

import com.gyf.barlibrary.ImmersionBar;
import com.gyf.immersionbar.R;
import com.gyf.immersionbar.fragment.BaseFragment;

/**
 * @author geyifeng
 * @date 2017/7/20
 */

public class CategoryTwoFragment extends BaseFragment {

    @Override
    protected int setLayoutId() {
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
