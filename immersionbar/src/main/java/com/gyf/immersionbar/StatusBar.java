package com.gyf.immersionbar;

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

    StatusBar(boolean visible, int height) {
        this.visible = visible;
        this.height = height;
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
}
