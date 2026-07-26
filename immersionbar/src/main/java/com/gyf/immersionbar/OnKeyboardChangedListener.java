package com.gyf.immersionbar;

import androidx.annotation.NonNull;

/**
 * 软键盘变化监听器。
 *
 * @author geyifeng
 * @date 2026/7/26
 */
public interface OnKeyboardChangedListener {
    /**
     * 软键盘状态发生变化。
     *
     * @param keyboard the keyboard
     */
    void onKeyboardChanged(@NonNull Keyboard keyboard);
}
