package com.gyf.immersionbar.fragment;

/**
 * Fragment的基类,禁止懒加载功能
 * Created by geyifeng on 2017/4/7.
 */
public abstract class BaseNoLazyFragment extends BaseLazyFragment {

    @Override
    protected boolean isLazyLoad() {
        return false;
    }
}
