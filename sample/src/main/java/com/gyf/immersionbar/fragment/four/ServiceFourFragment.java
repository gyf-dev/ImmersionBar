package com.gyf.immersionbar.fragment.four;

import com.gyf.barlibrary.ImmersionBar;
import com.gyf.immersionbar.R;

/**
 * @author geyifeng
 * @date 2017/7/20
 */
public class ServiceFourFragment extends BaseFourFragment {
    @Override
    protected int setLayoutId() {
        return R.layout.fragment_two_service;
    }

    @Override
    protected void initView() {
        super.initView();
        ImmersionBar.setTitleBar(mActivity, getView().findViewById(R.id.toolbar));
    }
}
