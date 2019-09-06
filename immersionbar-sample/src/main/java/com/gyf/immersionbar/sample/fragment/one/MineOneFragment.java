package com.gyf.immersionbar.sample.fragment.one;

import com.gyf.immersionbar.ImmersionBar;
import com.gyf.immersionbar.sample.R;
import com.gyf.immersionbar.sample.fragment.BaseImmersionFragment;

/**
 * @author geyifeng
 * @date 2017/5/12
 */
public class MineOneFragment extends BaseImmersionFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_one_mine;
    }

    @Override
    public void initImmersionBar() {
        super.initImmersionBar();
        ImmersionBar.with(this)
                .statusBarDarkFont(true)
                .navigationBarColor(R.color.btn1)
                .navigationBarDarkIcon(true)
                .init();
    }
}
