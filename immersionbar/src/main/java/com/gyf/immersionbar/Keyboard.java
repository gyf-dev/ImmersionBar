package com.gyf.immersionbar;

import androidx.annotation.NonNull;

/**
 * 软键盘信息。
 *
 * @author geyifeng
 * @date 2026/7/26
 */
public class Keyboard {
    /**
     * 软键盘当前是否弹出。
     */
    private final boolean popup;
    /**
     * 软键盘当前高度。
     */
    private final int height;

    Keyboard(boolean popup, int height) {
        this.popup = popup;
        this.height = height;
    }

    /**
     * 软键盘当前是否弹出。
     *
     * @return true表示软键盘已弹出
     */
    public boolean isPopup() {
        return popup;
    }

    /**
     * 软键盘当前高度，关闭时为0，单位为px。
     *
     * @return the current keyboard height
     */
    public int getHeight() {
        return height;
    }

    @NonNull
    @Override
    public String toString() {
        return "Keyboard{" +
                "popup=" + popup +
                ", height=" + height +
                '}';
    }
}
