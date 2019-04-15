package com.gyf.immersionbar.simple.fragment.four;

import com.gyf.immersionbar.ImmersionBar;
import com.gyf.immersionbar.simple.R;

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
