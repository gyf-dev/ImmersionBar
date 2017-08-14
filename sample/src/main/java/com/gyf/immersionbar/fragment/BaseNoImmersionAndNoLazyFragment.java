package com.gyf.immersionbar.fragment;

import com.gyf.immersionbar.fragment.two.BaseTwoFragment;

/**
 * Created by geyifeng on 2017/7/22.
 */

public abstract class BaseNoImmersionAndNoLazyFragment extends BaseTwoFragment {

    @Override
    protected boolean isImmersionBarEnabled() {
        return false;
    }
}
