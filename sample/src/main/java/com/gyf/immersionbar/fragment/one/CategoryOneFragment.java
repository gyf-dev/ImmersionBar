package com.gyf.immersionbar.fragment.one;

import com.gyf.barlibrary.ImmersionBar;
import com.gyf.immersionbar.R;
import com.gyf.immersionbar.fragment.BaseFragment;

/**
 * @author geyifeng
 * @date 2017/5/12
 */
public class CategoryOneFragment extends BaseFragment {

    @Override
    protected int setLayoutId() {
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
