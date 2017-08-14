package com.gyf.immersionbar.fragment;

import com.gyf.immersionbar.fragment.one.BaseLazyFragment;

/**
 * Created by geyifeng on 2017/7/22.
 */

public abstract class BaseNoImmersionFragment extends BaseLazyFragment {

    @Override
    protected boolean isImmersionBarEnabled() {
        return false;
    }
}