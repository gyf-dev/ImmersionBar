package com.gyf.immersionbar.fragment.two;

import android.support.v7.widget.Toolbar;

import com.gyf.barlibrary.ImmersionBar;
import com.gyf.immersionbar.R;

import butterknife.BindView;

/**
 *
 * @author geyifeng
 * @date 2017/7/20
 */

public class MineTwoFragment extends BaseTwoFragment {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_two_mine;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        ImmersionBar.with(this)
                .titleBar(toolbar)
                .navigationBarColor(R.color.btn7)
                .init();
    }
}
