package com.gyf.immersionbar;

import androidx.annotation.NonNull;

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
     * @param navigationBar the navigation bar
     */
    void onNavigationBarChanged(@NonNull NavigationBar navigationBar);
}
