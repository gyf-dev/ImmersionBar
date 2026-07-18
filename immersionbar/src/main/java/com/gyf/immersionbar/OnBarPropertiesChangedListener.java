package com.gyf.immersionbar;

import androidx.annotation.NonNull;

/**
 * Listener for changes to status and navigation bar properties.
 *
 * @author geyifeng
 * @date 2019 -05-10 18:22
 */
public interface OnBarPropertiesChangedListener {

    /**
     * On bar info change.
     *
     * @param barProperties the bar info
     */
    void onBarPropertiesChanged(@NonNull BarProperties barProperties);
}
