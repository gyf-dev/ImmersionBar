package com.gyf.immersionbar.simple.fragment;

/**
 * 不使用沉浸式的Fragment基类
 *
 * @author geyifeng
 * @date 2017/4/7
 */
public abstract class BaseFragment extends BaseImmersionFragment {
    @Override
    public boolean immersionBarEnabled() {
        return false;
    }
}
