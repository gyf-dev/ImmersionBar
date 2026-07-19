package com.gyf.immersionbar;

import androidx.annotation.NonNull;

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
     * 忽略可见性的导航栏高度（隐藏时也返回其实际尺寸）
     */
    private final int heightIgnoringVisibility;
    /**
     * 导航栏宽度（导航栏在侧边时有效）
     */
    private final int width;
    /**
     * 导航栏类型（经典三键/手势/三段式手势/两键/未知）
     */
    private final NavigationBarType type;

    NavigationBar(boolean visible, int height, int heightIgnoringVisibility, int width, NavigationBarType type) {
        this.visible = visible;
        this.height = height;
        this.heightIgnoringVisibility = heightIgnoringVisibility;
        this.width = width;
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
     * 导航栏高度（忽略可见性，隐藏时也返回其实际尺寸），单位为px
     *
     * @return the navigation bar height ignoring visibility
     */
    public int getHeightIgnoringVisibility() {
        return heightIgnoringVisibility;
    }

    /**
     * 导航栏宽度（导航栏在侧边时有效），单位为px
     *
     * @return the navigation bar width
     */
    public int getWidth() {
        return width;
    }

    /**
     * 导航栏类型
     *
     * @return the NavigationBarType
     */
    @NonNull
    public NavigationBarType getType() {
        return type;
    }

    @NonNull
    @Override
    public String toString() {
        return "NavigationBar{" +
                "visible=" + visible +
                ", height=" + height +
                ", heightIgnoringVisibility=" + heightIgnoringVisibility +
                ", width=" + width +
                ", type=" + type +
                '}';
    }
}
