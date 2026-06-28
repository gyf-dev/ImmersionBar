package com.gyf.immersionbar;

import androidx.annotation.NonNull;

/**
 * Bar相关信息
 *
 * @author geyifeng
 * @date 2019-05-10 18:43
 */
public class BarProperties {

    /**
     * 是否是竖屏
     */
    private boolean portrait;
    /**
     * 是否是左横屏
     */
    private boolean landscapeLeft;
    /**
     * 是否是右横屏
     */
    private boolean landscapeRight;
    /**
     * 是否是刘海屏
     */
    private boolean notchScreen;
    /**
     * 是否有导航栏
     */
    private boolean hasNavigationBar;
    /**
     * 导航栏是否在底部（false表示在侧边，如横屏三键导航在右侧）
     */
    private boolean navigationAtBottom;
    /**
     * 导航栏类型（经典三键/手势/三段式手势/两键/未知）
     */
    private NavigationBarType navigationBarType = NavigationBarType.UNKNOWN;
    /**
     * 是否为手势导航
     */
    private boolean gestureNavigation;
    /**
     * 状态栏当前是否可见（运行时显隐会实时刷新并通过OnBarListener回调）
     */
    private boolean statusBarVisible = true;
    /**
     * 导航栏当前是否可见（运行时显隐会实时刷新并通过OnBarListener回调）
     */
    private boolean navigationBarVisible = true;
    /**
     * 状态栏高度，刘海屏横竖屏有可能状态栏高度不一样
     */
    private int statusBarHeight;
    /**
     * 导航栏高度
     */
    private int navigationBarHeight;
    /**
     * 导航栏宽度
     */
    private int navigationBarWidth;
    /**
     * 刘海屏高度
     */
    private int notchHeight;
    /**
     * ActionBar高度
     */
    private int actionBarHeight;

    BarProperties() {
    }

    /**
     * 拷贝构造，用于留存上一次派发的快照以做去重比较。
     *
     * @param other 源对象
     */
    BarProperties(BarProperties other) {
        this.portrait = other.portrait;
        this.landscapeLeft = other.landscapeLeft;
        this.landscapeRight = other.landscapeRight;
        this.notchScreen = other.notchScreen;
        this.hasNavigationBar = other.hasNavigationBar;
        this.navigationAtBottom = other.navigationAtBottom;
        this.navigationBarType = other.navigationBarType;
        this.gestureNavigation = other.gestureNavigation;
        this.statusBarVisible = other.statusBarVisible;
        this.navigationBarVisible = other.navigationBarVisible;
        this.statusBarHeight = other.statusBarHeight;
        this.navigationBarHeight = other.navigationBarHeight;
        this.navigationBarWidth = other.navigationBarWidth;
        this.notchHeight = other.notchHeight;
        this.actionBarHeight = other.actionBarHeight;
    }

    public boolean isPortrait() {
        return portrait;
    }

    void setPortrait(boolean portrait) {
        this.portrait = portrait;
    }

    public boolean isLandscapeLeft() {
        return landscapeLeft;
    }

    void setLandscapeLeft(boolean landscapeLeft) {
        this.landscapeLeft = landscapeLeft;
    }

    public boolean isLandscapeRight() {
        return landscapeRight;
    }

    void setLandscapeRight(boolean landscapeRight) {
        this.landscapeRight = landscapeRight;
    }

    public boolean isNotchScreen() {
        return notchScreen;
    }

    void setNotchScreen(boolean notchScreen) {
        this.notchScreen = notchScreen;
    }

    public boolean hasNavigationBar() {
        return hasNavigationBar;
    }

    void setNavigationBar(boolean hasNavigationBar) {
        this.hasNavigationBar = hasNavigationBar;
    }

    public boolean isNavigationAtBottom() {
        return navigationAtBottom;
    }

    void setNavigationAtBottom(boolean navigationAtBottom) {
        this.navigationAtBottom = navigationAtBottom;
    }

    public NavigationBarType getNavigationBarType() {
        return navigationBarType;
    }

    void setNavigationBarType(NavigationBarType navigationBarType) {
        this.navigationBarType = navigationBarType;
    }

    public boolean isGestureNavigation() {
        return gestureNavigation;
    }

    void setGestureNavigation(boolean gestureNavigation) {
        this.gestureNavigation = gestureNavigation;
    }

    public boolean isStatusBarVisible() {
        return statusBarVisible;
    }

    void setStatusBarVisible(boolean statusBarVisible) {
        this.statusBarVisible = statusBarVisible;
    }

    public boolean isNavigationBarVisible() {
        return navigationBarVisible;
    }

    void setNavigationBarVisible(boolean navigationBarVisible) {
        this.navigationBarVisible = navigationBarVisible;
    }

    public int getStatusBarHeight() {
        return statusBarHeight;
    }

    void setStatusBarHeight(int statusBarHeight) {
        this.statusBarHeight = statusBarHeight;
    }

    public int getNavigationBarHeight() {
        return navigationBarHeight;
    }

    void setNavigationBarHeight(int navigationBarHeight) {
        this.navigationBarHeight = navigationBarHeight;
    }

    public int getNavigationBarWidth() {
        return navigationBarWidth;
    }

    void setNavigationBarWidth(int navigationBarWidth) {
        this.navigationBarWidth = navigationBarWidth;
    }

    public int getNotchHeight() {
        return notchHeight;
    }

    void setNotchHeight(int notchHeight) {
        this.notchHeight = notchHeight;
    }

    public int getActionBarHeight() {
        return actionBarHeight;
    }

    void setActionBarHeight(int actionBarHeight) {
        this.actionBarHeight = actionBarHeight;
    }

    @NonNull
    @Override
    public String toString() {
        return "BarProperties{" +
                "portrait=" + portrait +
                ", landscapeLeft=" + landscapeLeft +
                ", landscapeRight=" + landscapeRight +
                ", notchScreen=" + notchScreen +
                ", hasNavigationBar=" + hasNavigationBar +
                ", navigationAtBottom=" + navigationAtBottom +
                ", navigationBarType=" + navigationBarType +
                ", gestureNavigation=" + gestureNavigation +
                ", statusBarVisible=" + statusBarVisible +
                ", navigationBarVisible=" + navigationBarVisible +
                ", statusBarHeight=" + statusBarHeight +
                ", navigationBarHeight=" + navigationBarHeight +
                ", navigationBarWidth=" + navigationBarWidth +
                ", notchHeight=" + notchHeight +
                ", actionBarHeight=" + actionBarHeight +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BarProperties that = (BarProperties) o;
        return portrait == that.portrait
                && landscapeLeft == that.landscapeLeft
                && landscapeRight == that.landscapeRight
                && notchScreen == that.notchScreen
                && hasNavigationBar == that.hasNavigationBar
                && navigationAtBottom == that.navigationAtBottom
                && gestureNavigation == that.gestureNavigation
                && statusBarVisible == that.statusBarVisible
                && navigationBarVisible == that.navigationBarVisible
                && statusBarHeight == that.statusBarHeight
                && navigationBarHeight == that.navigationBarHeight
                && navigationBarWidth == that.navigationBarWidth
                && notchHeight == that.notchHeight
                && actionBarHeight == that.actionBarHeight
                && navigationBarType == that.navigationBarType;
    }

    @Override
    public int hashCode() {
        int result = (portrait ? 1 : 0);
        result = 31 * result + (landscapeLeft ? 1 : 0);
        result = 31 * result + (landscapeRight ? 1 : 0);
        result = 31 * result + (notchScreen ? 1 : 0);
        result = 31 * result + (hasNavigationBar ? 1 : 0);
        result = 31 * result + (navigationAtBottom ? 1 : 0);
        result = 31 * result + (gestureNavigation ? 1 : 0);
        result = 31 * result + (statusBarVisible ? 1 : 0);
        result = 31 * result + (navigationBarVisible ? 1 : 0);
        result = 31 * result + (navigationBarType != null ? navigationBarType.hashCode() : 0);
        result = 31 * result + statusBarHeight;
        result = 31 * result + navigationBarHeight;
        result = 31 * result + navigationBarWidth;
        result = 31 * result + notchHeight;
        result = 31 * result + actionBarHeight;
        return result;
    }
}
