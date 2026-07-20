package com.gyf.immersionbar;

import androidx.annotation.NonNull;

/**
 * 状态栏信息
 *
 * @author geyifeng
 * @date 2026 /7/19
 */
public class StatusBar {

    /**
     * 状态栏当前是否可见
     */
    private final boolean visible;
    /**
     * 状态栏高度
     */
    private final int height;
    /**
     * 是否为首次回调（首次快照派发时为true）
     */
    private final boolean firstCallback;

    StatusBar(boolean visible, int height, boolean firstCallback) {
        this.visible = visible;
        this.height = height;
        this.firstCallback = firstCallback;
    }

    /**
     * 状态栏当前是否可见
     *
     * @return true表示可见
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * 状态栏高度，单位为px
     *
     * @return the status bar height
     */
    public int getHeight() {
        return height;
    }

    /**
     * 是否为首次回调（首次快照派发）
     *
     * @return true表示这是监听器收到的首次回调
     */
    public boolean isFirstCallback() {
        return firstCallback;
    }

    @NonNull
    @Override
    public String toString() {
        return "StatusBar{" +
                "visible=" + visible +
                ", height=" + height +
                ", firstCallback=" + firstCallback +
                '}';
    }
}
