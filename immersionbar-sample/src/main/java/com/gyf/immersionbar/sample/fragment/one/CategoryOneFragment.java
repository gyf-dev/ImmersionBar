package com.gyf.immersionbar.sample.fragment.one;

import com.gyf.immersionbar.ImmersionBar;
import com.gyf.immersionbar.sample.R;
import com.gyf.immersionbar.sample.fragment.BaseImmersionFragment;

/**
 * @author geyifeng
 * @date 2017/5/12
 */
public class CategoryOneFragment extends BaseImmersionFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_one_category;
    }

    @Override
    public void initImmersionBar() {
        super.initImmersionBar();
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f)
                .navigationBarColor(R.color.btn3)
                .init();
    }
}
