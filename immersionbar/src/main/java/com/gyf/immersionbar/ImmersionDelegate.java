package com.gyf.immersionbar;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.Configuration;
import android.os.Build;
import android.view.Surface;
import android.view.View;
import android.view.WindowInsets;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

/**
 * @author geyifeng
 * @date 2019/4/12 4:01 PM
 */
class ImmersionDelegate implements Runnable {

    private ImmersionBar mImmersionBar;
    private BarProperties mBarProperties;
    private BarProperties mLastBarProperties;
    private OnBarListener mOnBarListener;
    private int mNotchHeight;

    ImmersionDelegate(Object o) {
        if (o instanceof Activity) {
            if (mImmersionBar == null) {
                mImmersionBar = new ImmersionBar((Activity) o);
            }
        } else if (o instanceof Fragment) {
            if (mImmersionBar == null) {
                if (o instanceof DialogFragment) {
                    mImmersionBar = new ImmersionBar((DialogFragment) o);
                } else {
                    mImmersionBar = new ImmersionBar((Fragment) o);
                }
            }
        } else if (o instanceof android.app.Fragment) {
            if (mImmersionBar == null) {
                if (o instanceof android.app.DialogFragment) {
                    mImmersionBar = new ImmersionBar((android.app.DialogFragment) o);
                } else {
                    mImmersionBar = new ImmersionBar((android.app.Fragment) o);
                }
            }
        }
        //回传delegate引用，使运行时系统栏显隐变化能反向触发BarProperties快照刷新
        if (mImmersionBar != null) {
            mImmersionBar.setImmersionDelegate(this);
        }
    }

    ImmersionDelegate(Activity activity, Dialog dialog) {
        if (mImmersionBar == null) {
            mImmersionBar = new ImmersionBar(activity, dialog);
        }
        //回传delegate引用，使运行时系统栏显隐变化能反向触发BarProperties快照刷新
        mImmersionBar.setImmersionDelegate(this);
    }

    public ImmersionBar get() {
        return mImmersionBar;
    }

    void onActivityCreated(Configuration configuration) {
        barChanged(configuration);
    }

    void onResume() {
        if (mImmersionBar != null) {
            mImmersionBar.onResume();
        }
    }

    void onDestroy() {
        mBarProperties = null;
        mLastBarProperties = null;
        mOnBarListener = null;
        if (mImmersionBar != null) {
            mImmersionBar.onDestroy();
            mImmersionBar = null;
        }
    }

    void onConfigurationChanged(Configuration newConfig) {
        if (mImmersionBar != null) {
            mImmersionBar.onConfigurationChanged(newConfig);
            barChanged(newConfig);
        }
    }

    /**
     * 运行时系统栏显隐/导航模式变化时，重建BarProperties快照并按去重规则回调OnBarListener。
     *
     * <p>与{@link #onConfigurationChanged}不同，这并非配置变化，方向字段沿用上一次config快照即可；
     * 其余字段（可见性、导航类型、是否手势、各项高度、刘海等）由{@link #run()}统一重新读取，
     * 因此凡是会改变insets的运行时事件（hideBar/showBar、手势临时显隐、三键⇄手势切换）都能让快照保持实时。
     *
     * <p>首次config快照尚未建立（{@code mBarProperties == null}）时直接跳过，交由
     * {@link #onActivityCreated}统一构建，避免方向字段缺失。
     */
    void refreshBarProperties() {
        if (mImmersionBar == null || !mImmersionBar.initialized() || Build.VERSION.SDK_INT < Version.KITKAT) {
            return;
        }
        mOnBarListener = mImmersionBar.getBarParams().onBarListener;
        if (mOnBarListener == null) {
            return;
        }
        final Activity activity = mImmersionBar.getActivity();
        if (activity == null || mBarProperties == null) {
            return;
        }
        activity.getWindow().getDecorView().post(this);
    }

    /**
     * 横竖屏切换监听
     * Orientation change.
     *
     * @param configuration the configuration
     */
    private void barChanged(Configuration configuration) {
        if (mImmersionBar != null && mImmersionBar.initialized() && Build.VERSION.SDK_INT >= Version.KITKAT) {
            mOnBarListener = mImmersionBar.getBarParams().onBarListener;
            if (mOnBarListener != null) {
                final Activity activity = mImmersionBar.getActivity();
                if (activity == null) {
                    return;
                }
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
            mBarProperties.setStatusBarVisible(barConfig.isStatusBarVisible());
            mBarProperties.setNavigationBar(barConfig.hasNavigationBar());
            mBarProperties.setNavigationAtBottom(barConfig.isNavigationAtBottom());
            //导航栏类型与是否手势导航（一次查询同时填两个字段）
            GestureUtils.GestureBean gestureBean = GestureUtils.getGestureBean(activity);
            mBarProperties.setNavigationBarType(gestureBean.type);
            mBarProperties.setGestureNavigation(gestureBean.isGesture);
            mBarProperties.setNavigationBarHeight(barConfig.getNavigationBarHeight());
            mBarProperties.setNavigationBarVisible(barConfig.isNavigationBarVisible());
            mBarProperties.setNavigationBarWidth(barConfig.getNavigationBarWidth());
            mBarProperties.setActionBarHeight(barConfig.getActionBarHeight());
            boolean notchScreen = NotchUtils.hasNotchScreen(activity);
            mBarProperties.setNotchScreen(notchScreen);
            if (notchScreen && mNotchHeight == 0) {
                mNotchHeight = NotchUtils.getNotchHeight(activity);
                mBarProperties.setNotchHeight(mNotchHeight);
            }
            //仅当内容相对上次派发发生变化（或首次）时才回调，避免配置变化但Bar信息未变时的无谓回调
            if (mLastBarProperties == null || !mLastBarProperties.equals(mBarProperties)) {
                mLastBarProperties = new BarProperties(mBarProperties);
                mOnBarListener.onBarChange(mBarProperties);
            }
        }
    }
}
