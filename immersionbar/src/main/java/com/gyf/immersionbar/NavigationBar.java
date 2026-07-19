package com.gyf.immersionbar;

/**
 * 导航栏信息
 *
 * @author geyifeng
 * @date 2026 /7/19
 */
public class NavigationBar {

    /**
     * 导航栏当前是否可见
     */
    private final boolean visible;
    /**
     * 导航栏高度
     */
    private final int height;
    /**
     * 导航栏类型（经典三键/手势/三段式手势/两键/未知）
     */
    private final NavigationBarType type;

    NavigationBar(boolean visible, int height, NavigationBarType type) {
        this.visible = visible;
        this.height = height;
        this.type = type;
    }

    /**
     * 导航栏当前是否可见
     *
     * @return true表示可见
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * 导航栏高度，单位为px
     *
     * @return the navigation bar height
     */
    public int getHeight() {
        return height;
    }

    /**
     * 导航栏类型
     *
     * @return the NavigationBarType
     */
    public NavigationBarType getType() {
        return type;
    }
}
