package com.gyf.immersionbar;

/**
 * The interface On navigation bar listener.
 *
 * @author geyifeng
 * @date 2019 /4/10 6:12 PM
 * @deprecated 统一使用{@link OnBarPropertiesChangedListener}，通过{@link BarProperties#isNavigationBarVisible()}
 * 和{@link BarProperties#getNavigationBarType()}获取导航栏显隐与导航类型。
 */
@Deprecated
public interface OnNavigationBarListener {
    /**
     * On navigation bar change.
     *
     * @param type the NavigationBarType
     */
    void onNavigationBarChange(boolean show, NavigationBarType type);
}
