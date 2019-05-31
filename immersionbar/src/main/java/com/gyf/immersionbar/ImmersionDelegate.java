package com.gyf.immersionbar;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.Configuration;
import android.os.Build;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.Surface;

/**
 * @author geyifeng
 * @date 2019/4/12 4:01 PM
 */
class ImmersionDelegate implements Runnable {

    private ImmersionBar mImmersionBar;
    private int mStatusBarHeight = 0;
    private BarProperties mBarProperties;
    private OnBarListener mOnBarListener;
    private int mNotchHeight;

    ImmersionDelegate(Object o) {
        if (o instanceof Activity) {
            if (mImmersionBar == null) {
                mImmersionBar = new ImmersionBar((Activity) o);
                mStatusBarHeight = ImmersionBar.getStatusBarHeight((Activity) o);
            }
        } else if (o instanceof Fragment) {
            if (mImmersionBar == null) {
                if (o instanceof DialogFragment) {
                    mImmersionBar = new ImmersionBar((DialogFragment) o);
                } else {
                    mImmersionBar = new ImmersionBar((Fragment) o);
                }
                mStatusBarHeight = ImmersionBar.getStatusBarHeight((Fragment) o);
            }
        } else if (o instanceof android.app.Fragment) {
            if (mImmersionBar == null) {
                if (o instanceof android.app.DialogFragment) {
                    mImmersionBar = new ImmersionBar((android.app.DialogFragment) o);
                } else {
                    mImmersionBar = new ImmersionBar((android.app.Fragment) o);
                }
                mStatusBarHeight = ImmersionBar.getStatusBarHeight((android.app.Fragment) o);
            }
        }
    }

    ImmersionDelegate(Activity activity, Dialog dialog) {
        if (mImmersionBar == null) {
            mImmersionBar = new ImmersionBar(activity, dialog);
            mStatusBarHeight = ImmersionBar.getStatusBarHeight(activity);
        }
    }

    public ImmersionBar get() {
        return mImmersionBar;
    }

    void onActivityCreated(Configuration configuration) {
        barChanged(configuration);
    }

    void onResume() {
        if (mImmersionBar != null && !mImmersionBar.isFragment() && mImmersionBar.initialized()) {
            if (OSUtils.isEMUI3_x() && mImmersionBar.getBarParams().navigationBarWithEMUI3Enable) {
                reinitialize();
            } else {
                if (mImmersionBar.getBarParams().barHide != BarHide.FLAG_SHOW_BAR) {
                    mImmersionBar.setBar();
                }
            }
        }
    }

    void onDestroy() {
        mBarProperties = null;
        if (mImmersionBar != null) {
            mImmersionBar.destroy();
            mImmersionBar = null;
        }
    }

    void onConfigurationChanged(Configuration newConfig) {
        if (mImmersionBar != null) {
            if (OSUtils.isEMUI3_x() || Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
                if (mImmersionBar.initialized() && !mImmersionBar.isFragment() && mImmersionBar.getBarParams().navigationBarWithKitkatEnable) {
                    reinitialize();
                } else {
                    fitsWindows();
                }
            } else {
                fitsWindows();
            }
            barChanged(newConfig);
        }
    }

    /**
     * 重新初始化，适配一些特殊机型
     */
    private void reinitialize() {
        if (mImmersionBar != null) {
            mImmersionBar.init();
        }
    }

    /**
     * 状态栏高度改变的时候重新适配布局重叠问题
     */
    private void fitsWindows() {
        int statusBarHeight = ImmersionBar.getStatusBarHeight(mImmersionBar.getActivity());
        if (mStatusBarHeight != statusBarHeight) {
            mImmersionBar.fitsWindows();
            mStatusBarHeight = statusBarHeight;
        }
    }


    /**
     * 横竖屏切换监听
     * Orientation change.
     *
     * @param configuration the configuration
     */
    private void barChanged(Configuration configuration) {
        if (mImmersionBar != null && mImmersionBar.initialized() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mOnBarListener = mImmersionBar.getBarParams().onBarListener;
            if (mOnBarListener != null) {
                final Activity activity = mImmersionBar.getActivity();
                if (mBarProperties == null) {
                    mBarProperties = new BarProperties();
                }
                mBarProperties.setPortrait(configuration.orientation == Configuration.ORIENTATION_PORTRAIT);
                int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
                if (rotation == Surface.ROTATION_90) {
                    mBarProperties.setLandscapeLeft(true);
                    mBarProperties.setLandscapeRight(false);
                } else if (rotation == Surface.ROTATION_270) {
                    mBarProperties.setLandscapeLeft(false);
                    mBarProperties.setLandscapeRight(true);
                } else {
                    mBarProperties.setLandscapeLeft(false);
                    mBarProperties.setLandscapeRight(false);
                }
                activity.getWindow().getDecorView().post(this);
            }
        }
    }

    @Override
    public void run() {
        if (mImmersionBar != null && mImmersionBar.getActivity() != null) {
            Activity activity = mImmersionBar.getActivity();
            BarConfig barConfig = new BarConfig(activity);
            mBarProperties.setStatusBarHeight(barConfig.getStatusBarHeight());
            mBarProperties.setNavigationBar(barConfig.hasNavigationBar());
            mBarProperties.setNavigationBarHeight(barConfig.getNavigationBarHeight());
            mBarProperties.setNavigationBarWidth(barConfig.getNavigationBarWidth());
            mBarProperties.setActionBarHeight(barConfig.getActionBarHeight());
            boolean notchScreen = NotchUtils.hasNotchScreen(activity);
            mBarProperties.setNotchScreen(notchScreen);
            if (notchScreen && mNotchHeight == 0) {
                mNotchHeight = NotchUtils.getNotchHeight(activity);
                mBarProperties.setNotchHeight(mNotchHeight);
            }
            mOnBarListener.onBarChange(mBarProperties);
        }
    }
}
