package com.gyf.immersionbar;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;

import androidx.annotation.RequiresApi;

/**
 * 系统栏（状态栏/导航栏）运行时显隐监听器，per-window。
 *
 * <p>运行时显隐（如{@link ImmersionBar#hideBar}、系统手势临时显隐）不流经ContentProvider，
 * {@link NavigationBarObserver}那套{@link android.database.ContentObserver}监听不到，只能靠运行时信号检测：
 * <ul>
 *   <li>API 30+（R）：{@link View#setOnApplyWindowInsetsListener} + {@link WindowInsets#isVisible(int)}</li>
 *   <li>API 16~29：{@link View#setOnSystemUiVisibilityChangeListener} + SYSTEM_UI_FLAG_* 位</li>
 *   <li>API &lt;16：无可用API，no-op</li>
 * </ul>
 *
 * <p><b>为何用隐形锚点view而非DecorView：</b>insets/systemUi监听在每个view上是单槽位，后设覆盖前设。
 * 若直接挂DecorView，会与开发者自己挂在DecorView上的监听互相覆盖。改为自建一个0×0的锚点子view、把监听挂它身上，
 * 利用系统栏事件从DecorView向下分发到子view的机制拿到信号，从而与开发者的DecorView监听互不干扰。
 * 锚点插到index 0，保证在可能消费insets的内容兄弟view之前先收到insets。
 *
 * <p>本类只负责“何时变化”，把真实可见性回调给{@link ImmersionBar#onBarVisibilityChange(boolean, boolean)}处理。
 *
 * @author geyifeng
 */
final class BarVisibilityObserver {

    private final ImmersionBar mImmersionBar;
    private final ViewGroup mDecorView;

    /**
     * 挂载监听的隐形锚点view（0×0），addView到DecorView，避免与开发者的DecorView监听抢槽位。
     */
    private View mAnchorView;

    /**
     * 上次回调出去的可见性，用于去重；null表示尚未播种。
     */
    private Boolean mLastStatusVisible = null;
    private Boolean mLastNavigationVisible = null;

    BarVisibilityObserver(ImmersionBar immersionBar) {
        mImmersionBar = immersionBar;
        Window window = immersionBar.getWindow();
        //DecorView恒为FrameLayout子类，可安全转为ViewGroup以addView锚点
        mDecorView = (ViewGroup) window.getDecorView();
    }

    /**
     * 创建锚点view并按版本注册运行时显隐监听，最后用当前真实状态播种一次。
     * 重复调用安全（锚点只创建一次，监听覆盖同一槽位）。
     */
    @SuppressLint("InflateParams")
    void enable() {
        if (Build.VERSION.SDK_INT < Version.JELLY_BEAN) {
            //API <16 无可用运行时检测API，no-op（仅保留URI模式切换监听）
            return;
        }
        if (mAnchorView == null) {
            mAnchorView = new View(mDecorView.getContext());
            mDecorView.addView(mAnchorView, new ViewGroup.LayoutParams(0, 0));
        }
        if (Build.VERSION.SDK_INT >= Version.R) {
            enableInsetsListener();
        } else {
            enableSystemUiListener();
        }
        seed();
    }

    /**
     * 移除锚点view、注销监听并复位去重状态。
     */
    void disable() {
        if (mAnchorView != null) {
            //与enable对称：按版本注销对应监听后再移除锚点view
            if (Build.VERSION.SDK_INT >= Version.R) {
                mAnchorView.setOnApplyWindowInsetsListener(null);
            } else if (Build.VERSION.SDK_INT >= Version.JELLY_BEAN) {
                mAnchorView.setOnSystemUiVisibilityChangeListener(null);
            }
            mDecorView.removeView(mAnchorView);
            mAnchorView = null;
        }
        mLastStatusVisible = null;
        mLastNavigationVisible = null;
    }

    @RequiresApi(api = Version.R)
    private void enableInsetsListener() {
        mAnchorView.setOnApplyWindowInsetsListener((v, insets) -> {
            dispatch(insets.isVisible(WindowInsets.Type.statusBars()),
                    insets.isVisible(WindowInsets.Type.navigationBars()));
            //锚点不消费insets：返回默认派发结果，让兄弟内容view照常收到
            return v.onApplyWindowInsets(insets);
        });
    }

    private void enableSystemUiListener() {
        mAnchorView.setOnSystemUiVisibilityChangeListener(visibility -> {
            //SystemUiVisibility是窗口级flag，这里取DecorView的全量flag判定（回调参数仅含部分变化位）
            dispatchFromSystemUiFlags(mDecorView.getSystemUiVisibility());
        });
    }

    /**
     * 用当前真实状态播种一次。
     * systemUi监听只在变化时回调，不播种则“enable后首次隐藏”会因无历史值只播种不回调；故主动读当前态。
     */
    private void seed() {
        if (Build.VERSION.SDK_INT >= Version.R) {
            WindowInsets insets = mDecorView.getRootWindowInsets();
            if (insets != null) {
                dispatch(insets.isVisible(WindowInsets.Type.statusBars()),
                        insets.isVisible(WindowInsets.Type.navigationBars()));
            }
        } else {
            dispatchFromSystemUiFlags(mDecorView.getSystemUiVisibility());
        }
    }

    private void dispatchFromSystemUiFlags(int visibility) {
        boolean statusVisible = (visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0;
        boolean navigationVisible = (visibility & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0;
        dispatch(statusVisible, navigationVisible);
    }

    /**
     * 去重后回调ImmersionBar：仅当status/nav可见性相对上次发生变化（或首次）时才派发。
     */
    private void dispatch(boolean statusVisible, boolean navigationVisible) {
        if (mLastStatusVisible != null && mLastNavigationVisible != null
                && mLastStatusVisible == statusVisible && mLastNavigationVisible == navigationVisible) {
            return;
        }
        mLastStatusVisible = statusVisible;
        mLastNavigationVisible = navigationVisible;
        mImmersionBar.onBarVisibilityChange(statusVisible, navigationVisible);
    }
}
