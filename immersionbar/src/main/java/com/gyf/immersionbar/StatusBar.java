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
     * 状态栏当前高度，隐藏时为0
     */
    private final int height;
    /**
     * 忽略可见性的状态栏高度（隐藏时也返回其实际尺寸）
     */
    private final int heightIgnoringVisibility;
    /**
     * 是否为首次回调（首次快照派发时为true）
     */
    private final boolean firstCallback;

    StatusBar(boolean visible, int height, int heightIgnoringVisibility, boolean firstCallback) {
        this.visible = visible;
        this.height = height;
        this.heightIgnoringVisibility = heightIgnoringVisibility;
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
     * 状态栏当前高度，隐藏时为0，单位为px
     *
     * @return the current status bar height
     */
    public int getHeight() {
        return height;
    }

    /**
     * 状态栏高度（忽略可见性，隐藏时也返回其实际尺寸），单位为px
     *
     * @return the status bar height ignoring visibility
     */
    public int getHeightIgnoringVisibility() {
        return heightIgnoringVisibility;
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
                ", heightIgnoringVisibility=" + heightIgnoringVisibility +
                ", firstCallback=" + firstCallback +
                '}';
    }
}
