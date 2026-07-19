package com.gyf.immersionbar;

import androidx.annotation.NonNull;

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
     * @param statusBar the status bar
     */
    void onStatusBarChanged(@NonNull StatusBar statusBar);
}
