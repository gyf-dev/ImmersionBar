package com.gyf.immersionbar;

/**
 * The interface On status bar listener.
 *
 * @author geyifeng
 * @date 2026 /7/19 6:12 PM
 */
public interface OnStatusBarChangedListener {
    /**
     * On status bar changed.
     *
     * @param isVisible       是否显示
     * @param statusBarHeight 状态栏高度
     */
    void onStatusBarChanged(boolean isVisible, int statusBarHeight);
}
