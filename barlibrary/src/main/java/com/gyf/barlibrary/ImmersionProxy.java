package com.gyf.barlibrary;

import android.content.res.Configuration;
import android.support.v4.app.Fragment;

/**
 * ImmersionBar代理类
 *
 * @author geyifeng
 * @date 2018/11/15 12:53 PM
 */
public class ImmersionProxy {

    /**
     * 是否第一次显示
     */
    private boolean mIsFirstShow;
    /**
     * 是否显示
     */
    private boolean mVisible;

    private ImmersionOwner mImmersionOwner;
    private Fragment mFragment;

    public ImmersionProxy(Fragment fragment) {
        this.mFragment = fragment;
        if (fragment instanceof ImmersionOwner) {
            this.mImmersionOwner = (ImmersionOwner) fragment;
        } else {
            throw new IllegalArgumentException("Fragment请实现ImmersionOwner接口");
        }
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (mIsFirstShow) {
            if (mFragment.getUserVisibleHint()) {
                mVisible = true;
                mImmersionOwner.onVisible();
                if (mImmersionOwner.immersionBarEnabled()) {
                    mImmersionOwner.initImmersionBar();
                }
            } else {
                mVisible = false;
                mImmersionOwner.onInvisible();
            }
        }
    }

    public void onResume() {
        if (mFragment.getUserVisibleHint()) {
            mVisible = true;
            mImmersionOwner.onVisible();
            if (mImmersionOwner.immersionBarEnabled() && !mIsFirstShow) {
                mImmersionOwner.initImmersionBar();
            }
        }
        mIsFirstShow = true;
    }

    public void onPause() {
        mImmersionOwner.onInvisible();
    }

    public void onDestroy() {
        if (mImmersionOwner.immersionBarEnabled() && mFragment != null && mFragment.getActivity() != null) {
            ImmersionBar.with(mFragment).destroy();
        }
        mFragment = null;
        mImmersionOwner = null;
    }

    public void onConfigurationChanged(Configuration newConfig) {
        if (mVisible) {
            mImmersionOwner.onVisible();
            if (mImmersionOwner.immersionBarEnabled()) {
                mImmersionOwner.initImmersionBar();
            }
        }
    }

    public void onHiddenChanged(boolean hidden) {
        mFragment.setUserVisibleHint(!hidden);
    }
}
