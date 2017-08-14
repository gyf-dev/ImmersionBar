package com.gyf.immersionbar.fragment.two;

import android.support.v7.widget.Toolbar;

import com.gyf.immersionbar.R;

import butterknife.BindView;

/**
 * Created by geyifeng on 2017/7/20.
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
        mImmersionBar
                .titleBar(toolbar)
                .init();
        //或者
        //ImmersionBar.setTitleBar(getActivity(),toolbar);
    }
}
