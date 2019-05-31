package com.gyf.immersionbar;

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
}
