package com.gyf.immersionbar;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.Configuration;
import android.os.Build;
import android.view.View;

import androidx.core.view.ViewCompat;
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
    /**
     * 宿主页面是否处于前台。不可见期间不分发回调，也不更新最后一次已分发快照。
     */
    private boolean mResumed;
    /**
     * Android 15+恢复页面时，WindowInsetsController.hide()的结果可能晚于onResume生效。
     * 在配置的隐藏状态真正应用前不读取并分发过渡快照。
     */
    private boolean mAwaitingResumeBarState;
    private final Runnable mDispatchBarPropertiesRunnable = this::dispatchBarProperties;
    private final Runnable mDispatchBarPropertiesAfterFrameRunnable = () -> {
        ImmersionBar immersionBar = mImmersionBar;
        if (!canDispatchBarProperties(immersionBar)) {
            return;
        }
        View decorView = immersionBar.getWindow().getDecorView();
        decorView.removeCallbacks(mDispatchBarPropertiesRunnable);
        ViewCompat.postOnAnimation(decorView, mDispatchBarPropertiesRunnable);
    };

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

    void onResume() {
        mResumed = true;
        if (mImmersionBar != null) {
            mAwaitingResumeBarState = shouldAwaitResumeBarState(mImmersionBar);
            mImmersionBar.onResume();
            //不可见期间可能发生系统栏变化；恢复后与最后一次已分发快照做差量比较。
            refreshBarProperties();
        }
    }

    void onPause() {
        mResumed = false;
        mAwaitingResumeBarState = false;
        cancelPendingBarPropertiesRefresh();
    }

    void onDestroy() {
        mResumed = false;
        mAwaitingResumeBarState = false;
        cancelPendingBarPropertiesRefresh();
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
     * <p>仅在宿主页面处于前台且完成初始化后排队刷新；不可见期间不分发回调、也不推进快照，
     * 页面恢复时由{@link #onResume()}重新读取当前属性并做差量分发。
     */
    void refreshBarProperties() {
        ImmersionBar immersionBar = mImmersionBar;
        if (!canDispatchBarProperties(immersionBar)) {
            return;
        }
        View decorView = immersionBar.getWindow().getDecorView();
        decorView.removeCallbacks(mDispatchBarPropertiesRunnable);
        decorView.removeCallbacks(mDispatchBarPropertiesAfterFrameRunnable);
        if (mAwaitingResumeBarState) {
            //至少跨过一次完整绘制，再读取Android 15+异步应用后的WindowInsets。
            ViewCompat.postOnAnimation(decorView, mDispatchBarPropertiesAfterFrameRunnable);
        } else {
            //同一消息循环内的多个变化信号合并为一次快照读取。
            decorView.post(mDispatchBarPropertiesRunnable);
        }
    }

    private boolean canDispatchBarProperties(ImmersionBar immersionBar) {
        return mResumed && immersionBar != null && immersionBar.initialized()
                && Build.VERSION.SDK_INT >= Version.KITKAT;
    }

    private void cancelPendingBarPropertiesRefresh() {
        ImmersionBar immersionBar = mImmersionBar;
        if (immersionBar == null) {
            return;
        }
        View decorView = immersionBar.getWindow().getDecorView();
        decorView.removeCallbacks(mDispatchBarPropertiesRunnable);
        decorView.removeCallbacks(mDispatchBarPropertiesAfterFrameRunnable);
    }

    private boolean shouldAwaitResumeBarState(ImmersionBar immersionBar) {
        if (Build.VERSION.SDK_INT < Version.VANILLA_ICE_CREAM) {
            return false;
        }
        BarParams barParams = immersionBar.getBarParams();
        return barParams != null && barParams.barHide != BarHide.FLAG_SHOW_BAR;
    }

    private boolean isResumeBarStateApplied(ImmersionBar immersionBar, BarProperties barProperties) {
        BarParams barParams = immersionBar.getBarParams();
        if (barParams == null) {
            return true;
        }
        switch (barParams.barHide) {
            case FLAG_HIDE_STATUS_BAR:
                return !barProperties.isStatusBarVisible();
            case FLAG_HIDE_NAVIGATION_BAR:
                return !barProperties.hasNavigationBar() || !barProperties.isNavigationBarVisible();
            case FLAG_HIDE_BAR:
                return !barProperties.isStatusBarVisible() && !barProperties.isNavigationBarVisible();
            case FLAG_SHOW_BAR:
            default:
                return true;
        }
    }

    void dispatchBarProperties() {
        ImmersionBar immersionBar = mImmersionBar;
        //post出去后宿主可能已经onPause，执行时必须再次校验，避免旧页面收到回调并推进快照。
        if (!canDispatchBarProperties(immersionBar)) {
            return;
        }
        BarProperties lastBarProperties = mLastBarProperties;
        BarProperties barProperties = BarPropertiesUtils.getBarProperties(immersionBar.getWindow());
        if (mAwaitingResumeBarState) {
            if (!isResumeBarStateApplied(immersionBar, barProperties)) {
                //仍是Android 15恢复过程中的旧Insets，等待BarVisibilityObserver下一个真实Insets信号。
                return;
            }
            mAwaitingResumeBarState = false;
        }

        //首次快照（无上次快照）时下面三个分发会同时触发，统一打上首次回调标记。
        boolean firstCallback = lastBarProperties == null;
        int barPropertiesChanges = barProperties.calculateChanges(lastBarProperties);
        int statusBarChanges = StatusBar.calculateChanges(barPropertiesChanges);
        int navigationBarChanges = NavigationBar.calculateChanges(barPropertiesChanges);
        barProperties.setFirstCallback(firstCallback);
        barProperties.setChangedFlags(barPropertiesChanges);

        if (barPropertiesChanges != BarProperties.CHANGE_NONE) {
            mLastBarProperties = new BarProperties(barProperties);
            immersionBar.dispatchOnBarPropertiesChanged(barProperties);
        }
        if (statusBarChanges != StatusBar.CHANGE_NONE) {
            immersionBar.dispatchOnStatusBarChanged(StatusBar.from(barProperties, firstCallback, statusBarChanges));
        }
        if (navigationBarChanges != NavigationBar.CHANGE_NONE) {
            immersionBar.dispatchOnNavigationBarChanged(
                    NavigationBar.from(barProperties, firstCallback, navigationBarChanges));
        }
    }


}
