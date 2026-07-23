package com.gyf.immersionbar;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 导航栏信息
 *
 * @author geyifeng
 * @date 2026 /7/19
 */
public class NavigationBar {

    public static final int CHANGE_NONE = 0;
    public static final int CHANGE_VISIBILITY = 1;
    public static final int CHANGE_HEIGHT = 1 << 1;
    public static final int CHANGE_HEIGHT_IGNORING_VISIBILITY = 1 << 2;
    public static final int CHANGE_WIDTH = 1 << 3;
    public static final int CHANGE_TYPE = 1 << 4;
    public static final int CHANGE_ALL = CHANGE_VISIBILITY | CHANGE_HEIGHT
            | CHANGE_HEIGHT_IGNORING_VISIBILITY | CHANGE_WIDTH | CHANGE_TYPE;

    @IntDef(flag = true, value = {
            CHANGE_NONE,
            CHANGE_VISIBILITY,
            CHANGE_HEIGHT,
            CHANGE_HEIGHT_IGNORING_VISIBILITY,
            CHANGE_WIDTH,
            CHANGE_TYPE,
            CHANGE_ALL
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface Change {
    }

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
    /**
     * 是否为首次回调（首次快照派发时为true）
     */
    private final boolean firstCallback;
    /**
     * 相对上一次回调发生变化的属性位
     */
    @Change
    private final int changedFlags;

    NavigationBar(boolean visible, int height, int heightIgnoringVisibility, int width, NavigationBarType type,
            boolean firstCallback, @Change int changedFlags) {
        this.visible = visible;
        this.height = height;
        this.heightIgnoringVisibility = heightIgnoringVisibility;
        this.width = width;
        this.type = type;
        this.firstCallback = firstCallback;
        this.changedFlags = changedFlags;
    }

    static NavigationBar from(BarProperties barProperties, boolean firstCallback, @Change int changedFlags) {
        return new NavigationBar(barProperties.isNavigationBarVisible(), barProperties.getNavigationBarHeight(),
                barProperties.getNavigationBarHeightIgnoringVisibility(), barProperties.getNavigationBarWidth(),
                barProperties.getNavigationBarType(), firstCallback, changedFlags);
    }

    @Change
    static int calculateChanges(@BarProperties.Change int barPropertiesChanges) {
        int changes = CHANGE_NONE;
        if ((barPropertiesChanges & BarProperties.CHANGE_NAVIGATION_BAR_VISIBILITY) != 0) {
            changes |= CHANGE_VISIBILITY;
        }
        if ((barPropertiesChanges & BarProperties.CHANGE_NAVIGATION_BAR_HEIGHT) != 0) {
            changes |= CHANGE_HEIGHT;
        }
        if ((barPropertiesChanges & BarProperties.CHANGE_NAVIGATION_BAR_HEIGHT_IGNORING_VISIBILITY) != 0) {
            changes |= CHANGE_HEIGHT_IGNORING_VISIBILITY;
        }
        if ((barPropertiesChanges & BarProperties.CHANGE_NAVIGATION_BAR_WIDTH) != 0) {
            changes |= CHANGE_WIDTH;
        }
        if ((barPropertiesChanges & BarProperties.CHANGE_NAVIGATION_BAR_TYPE) != 0) {
            changes |= CHANGE_TYPE;
        }
        return changes;
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

    /**
     * 是否为首次回调（首次快照派发）
     *
     * @return true表示这是监听器收到的首次回调
     */
    public boolean isFirstCallback() {
        return firstCallback;
    }

    /**
     * 判断指定属性是否相对上一次回调发生变化。传入多个常量按位或时，任一属性变化即返回true。
     * 首次回调没有历史快照，所有属性均视为变化。
     *
     * @param change 一个或多个CHANGE_*常量
     * @return true表示指定属性中至少有一个发生变化
     */
    public boolean hasChanged(@Change int change) {
        return (changedFlags & change) != 0;
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
                ", firstCallback=" + firstCallback +
                ", changedFlags=" + changedFlags +
                '}';
    }
}
