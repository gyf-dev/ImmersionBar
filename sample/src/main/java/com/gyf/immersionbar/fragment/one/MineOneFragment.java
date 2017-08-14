package com.gyf.immersionbar.fragment.one;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.gyf.barlibrary.ImmersionBar;
import com.gyf.immersionbar.R;

import butterknife.BindView;

/**
 * Created by geyifeng on 2017/5/12.
 */

public class MineOneFragment extends BaseLazyFragment {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImmersionBar.setTitleBar(getActivity(), toolbar);
    }

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
