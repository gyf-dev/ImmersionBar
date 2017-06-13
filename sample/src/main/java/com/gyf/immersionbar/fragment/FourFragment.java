package com.gyf.immersionbar.fragment;

import android.support.v7.widget.Toolbar;

import com.gyf.barlibrary.ImmersionBar;
import com.gyf.immersionbar.R;

import butterknife.BindView;

/**
 * Created by geyifeng on 2017/5/12.
 */

public class FourFragment extends BaseFragment {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_four;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void immersionInit() {
        ImmersionBar.with(this)
                .titleBar(toolbar)
                .statusBarDarkFont(true)
                .navigationBarColor(R.color.btn1)
                .init();
    }
}
