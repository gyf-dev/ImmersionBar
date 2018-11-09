package com.gyf.immersionbar.fragment.one;

import com.gyf.barlibrary.ImmersionBar;
import com.gyf.immersionbar.R;

/**
 * @author geyifeng
 * @date 2017/5/12
 */
public class MineOneFragment extends BaseLazyFragment {

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_one_mine;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        ImmersionBar.with(this)
                .statusBarDarkFont(true)
                .navigationBarColor(R.color.btn1)
                .init();
    }
}
