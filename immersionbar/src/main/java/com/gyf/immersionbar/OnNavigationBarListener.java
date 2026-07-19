package com.gyf.immersionbar;

/**
 * The interface On navigation bar listener.
 *
 * @author geyifeng
 * @date 2019 /4/10 6:12 PM
 */
public interface OnNavigationBarListener {
    /**
     * On navigation bar change.
     *
     * @param type the NavigationBarType
     */
    void onNavigationBarChange(boolean show, NavigationBarType type);
}
