package com.gyf.immersionbar.fragment;

import android.support.v7.widget.Toolbar;

import com.gyf.barlibrary.ImmersionBar;
import com.gyf.immersionbar.R;

import butterknife.BindView;

/**
 * Created by geyifeng on 2017/5/12.
 */

public class TwoFragment extends BaseFragment {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_two;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void immersionInit() {
        ImmersionBar.with(this)
                .titleBar(toolbar)
                .statusBarDarkFont(true, 0.2f)
                .navigationBarColor(R.color.btn3)
                .init();
    }
}
