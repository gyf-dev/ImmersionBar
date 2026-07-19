package com.gyf.immersionbar;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.Configuration;
import android.os.Build;
import android.view.View;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

/**
 * @author geyifeng
 * @date 2019/4/12 4:01 PM
 */
@SuppressWarnings("deprecation")
class ImmersionDelegate {

    private ImmersionBar mImmersionBar;
    private BarProperties mLastBarProperties;

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
        refreshBarProperties();
    }

    void onResume() {
        if (mImmersionBar != null) {
            mImmersionBar.onResume();
        }
    }

    void onDestroy() {
        mLastBarProperties = null;
        if (mImmersionBar != null) {
            mImmersionBar.onDestroy();
            mImmersionBar = null;
        }
    }

    void onConfigurationChanged(Configuration newConfig) {
        if (mImmersionBar != null) {
            mImmersionBar.onConfigurationChanged(newConfig);
            refreshBarProperties();
        }
    }

    /**
     * 运行时系统栏显隐/导航模式变化时。
     *
     * <p>凡是会改变insets的运行时事件（hideBar/showBar、手势临时显隐、三键⇄手势切换）
     *
     * <p>首次快照尚未建立（{@code mBarProperties == null}）时直接跳过，交由
     * {@link #onActivityCreated}统一构建。
     */
    void refreshBarProperties() {
        ImmersionBar immersionBar = mImmersionBar;
        if (immersionBar == null || !immersionBar.initialized() || Build.VERSION.SDK_INT < Version.KITKAT) {
            return;
        }
        View decorView = immersionBar.getWindow().getDecorView();
        decorView.post(this::dispatchBarProperties);
    }


    void dispatchBarProperties() {
        ImmersionBar immersionBar = mImmersionBar;
        if (immersionBar == null || !immersionBar.initialized() || Build.VERSION.SDK_INT < Version.KITKAT) {
            return;
        }
        BarProperties lastBarProperties = mLastBarProperties;
        BarProperties barProperties = BarPropertiesUtils.getBarProperties(immersionBar.getWindow());
        if (isBarPropertiesChanged(lastBarProperties, barProperties)) {
            mLastBarProperties = new BarProperties(barProperties);
            immersionBar.dispatchOnBarPropertiesChanged(barProperties);
        }
        if (shouldDispatchStatusBarChanged(lastBarProperties, barProperties)) {
            immersionBar.dispatchOnStatusBarChanged(new StatusBar(barProperties.isStatusBarVisible(),
                    barProperties.getStatusBarHeight()));
        }
        if (shouldDispatchNavigationBarChanged(lastBarProperties, barProperties)) {
            immersionBar.dispatchOnNavigationBarChanged(new NavigationBar(barProperties.isNavigationBarVisible(),
                    barProperties.getNavigationBarHeight(), barProperties.getNavigationBarType()));
        }
    }

    /**
     * BarProperties快照是否相对上次分发发生变化：首次快照（无上次快照）视为变化。
     */
    private boolean isBarPropertiesChanged(BarProperties lastBarProperties, BarProperties barProperties) {
        return lastBarProperties == null || !lastBarProperties.equals(barProperties);
    }

    /**
     * 是否需要分发状态栏变化（OnStatusBarChangedListener集合及废弃的OnStatusBarListener）：
     * 首次快照（无上次快照）或状态栏可见性、高度变化时为true。
     */
    private boolean shouldDispatchStatusBarChanged(BarProperties lastBarProperties, BarProperties barProperties) {
        return lastBarProperties == null
                || lastBarProperties.isStatusBarVisible() != barProperties.isStatusBarVisible()
                || lastBarProperties.getStatusBarHeight() != barProperties.getStatusBarHeight();
    }

    /**
     * 是否需要分发导航栏变化（OnNavigationBarChangedListener集合及废弃的OnNavigationBarListener）：
     * 首次快照（无上次快照）或导航栏可见性、高度、导航类型变化时为true。
     */
    private boolean shouldDispatchNavigationBarChanged(BarProperties lastBarProperties, BarProperties barProperties) {
        return lastBarProperties == null
                || lastBarProperties.isNavigationBarVisible() != barProperties.isNavigationBarVisible()
                || lastBarProperties.getNavigationBarHeight() != barProperties.getNavigationBarHeight()
                || lastBarProperties.getNavigationBarType() != barProperties.getNavigationBarType();
    }
}
