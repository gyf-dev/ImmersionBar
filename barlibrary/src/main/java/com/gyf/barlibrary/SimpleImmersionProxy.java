package com.gyf.barlibrary;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Fragment快速实现沉浸式的代理类
 *
 * @author geyifeng
 * @date 2018/11/15 12:53 PM
 */
public class SimpleImmersionProxy {
    /**
     * 要操作的Fragment对象
     */
    private Fragment mFragment;
    /**
     * 沉浸式实现接口
     */
    private SimpleImmersionOwner mSimpleImmersionOwner;
    /**
     * Fragment的view是否已经初始化完成
     */
    private boolean mIsActivityCreated;

    public SimpleImmersionProxy(Fragment fragment) {
        this.mFragment = fragment;
        if (fragment instanceof SimpleImmersionOwner) {
            this.mSimpleImmersionOwner = (SimpleImmersionOwner) fragment;
        } else {
            throw new IllegalArgumentException("Fragment请实现SimpleImmersionOwner接口");
        }
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        setImmersionBar();
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        mIsActivityCreated = true;
        setImmersionBar();
    }

    public void onDestroy() {
        if (mFragment != null && mFragment.getActivity() != null && mSimpleImmersionOwner.immersionBarEnabled()) {
            ImmersionBar.with(mFragment).destroy();
        }
        mFragment = null;
        mSimpleImmersionOwner = null;
    }

    public void onConfigurationChanged(Configuration newConfig) {
        setImmersionBar();
    }

    public void onHiddenChanged(boolean hidden) {
        if (mFragment != null) {
            mFragment.setUserVisibleHint(!hidden);
        }
    }

    /**
     * 是否已经对用户可见
     * Is user visible hint boolean.
     *
     * @return the boolean
     */
    public boolean isUserVisibleHint() {
        if (mFragment != null) {
            return mFragment.getUserVisibleHint();
        } else {
            return false;
        }
    }

    private void setImmersionBar() {
        if (mFragment != null && mIsActivityCreated && mFragment.getUserVisibleHint()
                && mSimpleImmersionOwner.immersionBarEnabled()) {
            mSimpleImmersionOwner.initImmersionBar();
        }
    }
}
