package com.gyf.immersionbar.fragment.four;

import android.support.v7.widget.Toolbar;

import com.gyf.barlibrary.ImmersionBar;
import com.gyf.immersionbar.R;

import butterknife.BindView;

/**
 * @author geyifeng
 * @date 2017/7/20
 */
public class HomeFourFragment extends BaseFourFragment {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_two_home;
    }

    @Override
    protected void initView() {
        ImmersionBar.setTitleBar(mActivity, toolbar);
    }
}
