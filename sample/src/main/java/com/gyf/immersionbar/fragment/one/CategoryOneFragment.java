package com.gyf.immersionbar.fragment.one;

import com.gyf.barlibrary.ImmersionBar;
import com.gyf.immersionbar.R;

/**
 * @author geyifeng
 * @date 2017/5/12
 */
public class CategoryOneFragment extends BaseLazyFragment {

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_one_category;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f)
                .navigationBarColor(R.color.btn3)
                .init();
    }
}
