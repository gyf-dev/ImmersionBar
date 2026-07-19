package com.gyf.immersionbar;

/**
 * The interface On navigation bar listener.
 *
 * @author geyifeng
 * @date 2026 /7/19 6:12 PM
 */
public interface OnNavigationBarChangedListener {
    /**
     * On navigation bar changed.
     *
     * @param isVisible           是否显示
     * @param navigationBarHeight 导航栏高度
     * @param type                导航栏类型
     */
    void onNavigationBarChanged(boolean isVisible, int navigationBarHeight, NavigationBarType type);
}
