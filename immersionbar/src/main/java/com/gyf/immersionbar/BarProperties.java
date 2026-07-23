package com.gyf.immersionbar;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Bar相关信息
 *
 * @author geyifeng
 * @date 2019-05-10 18:43
 */
public class BarProperties {

    public static final int CHANGE_NONE = 0;
    public static final int CHANGE_PORTRAIT = 1;
    public static final int CHANGE_LANDSCAPE_LEFT = 1 << 1;
    public static final int CHANGE_LANDSCAPE_RIGHT = 1 << 2;
    public static final int CHANGE_NOTCH_SCREEN = 1 << 3;
    public static final int CHANGE_HAS_NAVIGATION_BAR = 1 << 4;
    public static final int CHANGE_NAVIGATION_AT_BOTTOM = 1 << 5;
    public static final int CHANGE_NAVIGATION_BAR_TYPE = 1 << 6;
    public static final int CHANGE_GESTURE_NAVIGATION = 1 << 7;
    public static final int CHANGE_STATUS_BAR_VISIBILITY = 1 << 8;
    public static final int CHANGE_NAVIGATION_BAR_VISIBILITY = 1 << 9;
    public static final int CHANGE_STATUS_BAR_HEIGHT = 1 << 10;
    public static final int CHANGE_STATUS_BAR_HEIGHT_IGNORING_VISIBILITY = 1 << 11;
    public static final int CHANGE_NAVIGATION_BAR_HEIGHT = 1 << 12;
    public static final int CHANGE_NAVIGATION_BAR_HEIGHT_IGNORING_VISIBILITY = 1 << 13;
    public static final int CHANGE_NAVIGATION_BAR_WIDTH = 1 << 14;
    public static final int CHANGE_NOTCH_HEIGHT = 1 << 15;
    public static final int CHANGE_ACTION_BAR_HEIGHT = 1 << 16;
    public static final int CHANGE_ALL = (1 << 17) - 1;

    @IntDef(flag = true, value = {
            CHANGE_NONE,
            CHANGE_PORTRAIT,
            CHANGE_LANDSCAPE_LEFT,
            CHANGE_LANDSCAPE_RIGHT,
            CHANGE_NOTCH_SCREEN,
            CHANGE_HAS_NAVIGATION_BAR,
            CHANGE_NAVIGATION_AT_BOTTOM,
            CHANGE_NAVIGATION_BAR_TYPE,
            CHANGE_GESTURE_NAVIGATION,
            CHANGE_STATUS_BAR_VISIBILITY,
            CHANGE_NAVIGATION_BAR_VISIBILITY,
            CHANGE_STATUS_BAR_HEIGHT,
            CHANGE_STATUS_BAR_HEIGHT_IGNORING_VISIBILITY,
            CHANGE_NAVIGATION_BAR_HEIGHT,
            CHANGE_NAVIGATION_BAR_HEIGHT_IGNORING_VISIBILITY,
            CHANGE_NAVIGATION_BAR_WIDTH,
            CHANGE_NOTCH_HEIGHT,
            CHANGE_ACTION_BAR_HEIGHT,
            CHANGE_ALL
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface Change {
    }

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
     * 状态栏当前是否可见（运行时显隐会实时刷新并通过OnBarPropertiesChangedListener回调）
     */
    private boolean statusBarVisible = true;
    /**
     * 导航栏当前是否可见（运行时显隐会实时刷新并通过OnBarPropertiesChangedListener回调）
     */
    private boolean navigationBarVisible = true;
    /**
     * 状态栏当前高度，隐藏时为0；刘海屏横竖屏有可能状态栏高度不一样
     */
    private int statusBarHeight;
    /**
     * 忽略可见性的状态栏高度（隐藏时也返回其实际尺寸）
     */
    private int statusBarHeightIgnoringVisibility;
    /**
     * 导航栏高度
     */
    private int navigationBarHeight;
    /**
     * 忽略可见性的导航栏高度（隐藏时也返回其实际尺寸）
     */
    private int navigationBarHeightIgnoringVisibility;
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
    /**
     * 是否为首次回调（首次快照派发时为true）。属于派发元数据而非窗口状态：
     * 不参与equals/hashCode去重比较，也不随拷贝构造复制
     */
    private boolean firstCallback;
    /**
     * 相对上一次已分发快照发生变化的属性位。属于派发元数据，不参与equals/hashCode或快照复制。
     */
    @Change
    private int changedFlags = CHANGE_NONE;

    BarProperties() {
    }

    /**
     * 拷贝构造，用于留存上一次派发的快照以做去重比较。
     * 仅复制窗口状态字段；firstCallback是每次派发的元数据，不复制。
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
        this.statusBarHeightIgnoringVisibility = other.statusBarHeightIgnoringVisibility;
        this.navigationBarHeight = other.navigationBarHeight;
        this.navigationBarHeightIgnoringVisibility = other.navigationBarHeightIgnoringVisibility;
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

    /**
     * 状态栏当前高度，隐藏时为0，单位为px
     *
     * @return the current status bar height
     */
    public int getStatusBarHeight() {
        return statusBarHeight;
    }

    void setStatusBarHeight(int statusBarHeight) {
        this.statusBarHeight = statusBarHeight;
    }

    /**
     * 状态栏高度（忽略可见性，隐藏时也返回其实际尺寸），单位为px
     *
     * @return the status bar height ignoring visibility
     */
    public int getStatusBarHeightIgnoringVisibility() {
        return statusBarHeightIgnoringVisibility;
    }

    void setStatusBarHeightIgnoringVisibility(int statusBarHeightIgnoringVisibility) {
        this.statusBarHeightIgnoringVisibility = statusBarHeightIgnoringVisibility;
    }

    public int getNavigationBarHeight() {
        return navigationBarHeight;
    }

    void setNavigationBarHeight(int navigationBarHeight) {
        this.navigationBarHeight = navigationBarHeight;
    }

    public int getNavigationBarHeightIgnoringVisibility() {
        return navigationBarHeightIgnoringVisibility;
    }

    void setNavigationBarHeightIgnoringVisibility(int navigationBarHeightIgnoringVisibility) {
        this.navigationBarHeightIgnoringVisibility = navigationBarHeightIgnoringVisibility;
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

    /**
     * 是否为首次回调（首次快照派发）
     *
     * @return true表示这是监听器收到的首次回调
     */
    public boolean isFirstCallback() {
        return firstCallback;
    }

    void setFirstCallback(boolean firstCallback) {
        this.firstCallback = firstCallback;
    }

    /**
     * 判断指定属性是否相对上一次已分发快照发生变化。传入多个常量按位或时，任一属性变化即返回true。
     * 首次回调没有历史快照，所有属性均视为变化。
     *
     * @param change 一个或多个CHANGE_*常量
     * @return true表示指定属性中至少有一个发生变化
     */
    public boolean hasChanged(@Change int change) {
        return (changedFlags & change) != 0;
    }

    void setChangedFlags(@Change int changedFlags) {
        this.changedFlags = changedFlags;
    }

    /**
     * 计算当前快照相对上一次已分发快照发生变化的属性位。
     *
     * @param last 上一次已分发快照，null表示首次快照
     * @return 一个或多个CHANGE_*常量
     */
    @Change
    int calculateChanges(BarProperties last) {
        if (last == null) {
            return CHANGE_ALL;
        }
        int changes = CHANGE_NONE;
        if (last.portrait != portrait) {
            changes |= CHANGE_PORTRAIT;
        }
        if (last.landscapeLeft != landscapeLeft) {
            changes |= CHANGE_LANDSCAPE_LEFT;
        }
        if (last.landscapeRight != landscapeRight) {
            changes |= CHANGE_LANDSCAPE_RIGHT;
        }
        if (last.notchScreen != notchScreen) {
            changes |= CHANGE_NOTCH_SCREEN;
        }
        if (last.hasNavigationBar != hasNavigationBar) {
            changes |= CHANGE_HAS_NAVIGATION_BAR;
        }
        if (last.navigationAtBottom != navigationAtBottom) {
            changes |= CHANGE_NAVIGATION_AT_BOTTOM;
        }
        if (last.navigationBarType != navigationBarType) {
            changes |= CHANGE_NAVIGATION_BAR_TYPE;
        }
        if (last.gestureNavigation != gestureNavigation) {
            changes |= CHANGE_GESTURE_NAVIGATION;
        }
        if (last.statusBarVisible != statusBarVisible) {
            changes |= CHANGE_STATUS_BAR_VISIBILITY;
        }
        if (last.navigationBarVisible != navigationBarVisible) {
            changes |= CHANGE_NAVIGATION_BAR_VISIBILITY;
        }
        if (last.statusBarHeight != statusBarHeight) {
            changes |= CHANGE_STATUS_BAR_HEIGHT;
        }
        if (last.statusBarHeightIgnoringVisibility != statusBarHeightIgnoringVisibility) {
            changes |= CHANGE_STATUS_BAR_HEIGHT_IGNORING_VISIBILITY;
        }
        if (last.navigationBarHeight != navigationBarHeight) {
            changes |= CHANGE_NAVIGATION_BAR_HEIGHT;
        }
        if (last.navigationBarHeightIgnoringVisibility != navigationBarHeightIgnoringVisibility) {
            changes |= CHANGE_NAVIGATION_BAR_HEIGHT_IGNORING_VISIBILITY;
        }
        if (last.navigationBarWidth != navigationBarWidth) {
            changes |= CHANGE_NAVIGATION_BAR_WIDTH;
        }
        if (last.notchHeight != notchHeight) {
            changes |= CHANGE_NOTCH_HEIGHT;
        }
        if (last.actionBarHeight != actionBarHeight) {
            changes |= CHANGE_ACTION_BAR_HEIGHT;
        }
        return changes;
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
                ", statusBarHeightIgnoringVisibility=" + statusBarHeightIgnoringVisibility +
                ", navigationBarHeight=" + navigationBarHeight +
                ", navigationBarHeightIgnoringVisibility=" + navigationBarHeightIgnoringVisibility +
                ", navigationBarWidth=" + navigationBarWidth +
                ", notchHeight=" + notchHeight +
                ", actionBarHeight=" + actionBarHeight +
                ", firstCallback=" + firstCallback +
                ", changedFlags=" + changedFlags +
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
                && statusBarHeightIgnoringVisibility == that.statusBarHeightIgnoringVisibility
                && navigationBarHeight == that.navigationBarHeight
                && navigationBarHeightIgnoringVisibility == that.navigationBarHeightIgnoringVisibility
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
        result = 31 * result + statusBarHeightIgnoringVisibility;
        result = 31 * result + navigationBarHeight;
        result = 31 * result + navigationBarHeightIgnoringVisibility;
        result = 31 * result + navigationBarWidth;
        result = 31 * result + notchHeight;
        result = 31 * result + actionBarHeight;
        return result;
    }
}
