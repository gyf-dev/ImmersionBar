package com.gyf.immersionbar;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 状态栏信息
 *
 * @author geyifeng
 * @date 2026 /7/19
 */
public class StatusBar {

    public static final int CHANGE_NONE = 0;
    public static final int CHANGE_VISIBILITY = 1;
    public static final int CHANGE_HEIGHT = 1 << 1;
    public static final int CHANGE_HEIGHT_IGNORING_VISIBILITY = 1 << 2;
    public static final int CHANGE_ALL = CHANGE_VISIBILITY | CHANGE_HEIGHT | CHANGE_HEIGHT_IGNORING_VISIBILITY;

    @IntDef(flag = true, value = {
            CHANGE_NONE,
            CHANGE_VISIBILITY,
            CHANGE_HEIGHT,
            CHANGE_HEIGHT_IGNORING_VISIBILITY,
            CHANGE_ALL
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface Change {
    }

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
    /**
     * 相对上一次回调发生变化的属性位
     */
    @Change
    private final int changedFlags;

    StatusBar(boolean visible, int height, int heightIgnoringVisibility, boolean firstCallback,
            @Change int changedFlags) {
        this.visible = visible;
        this.height = height;
        this.heightIgnoringVisibility = heightIgnoringVisibility;
        this.firstCallback = firstCallback;
        this.changedFlags = changedFlags;
    }

    static StatusBar from(BarProperties barProperties, boolean firstCallback, @Change int changedFlags) {
        return new StatusBar(barProperties.isStatusBarVisible(), barProperties.getStatusBarHeight(),
                barProperties.getStatusBarHeightIgnoringVisibility(), firstCallback, changedFlags);
    }

    @Change
    static int calculateChanges(@BarProperties.Change int barPropertiesChanges) {
        int changes = CHANGE_NONE;
        if ((barPropertiesChanges & BarProperties.CHANGE_STATUS_BAR_VISIBILITY) != 0) {
            changes |= CHANGE_VISIBILITY;
        }
        if ((barPropertiesChanges & BarProperties.CHANGE_STATUS_BAR_HEIGHT) != 0) {
            changes |= CHANGE_HEIGHT;
        }
        if ((barPropertiesChanges & BarProperties.CHANGE_STATUS_BAR_HEIGHT_IGNORING_VISIBILITY) != 0) {
            changes |= CHANGE_HEIGHT_IGNORING_VISIBILITY;
        }
        return changes;
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
        return "StatusBar{" +
                "visible=" + visible +
                ", height=" + height +
                ", heightIgnoringVisibility=" + heightIgnoringVisibility +
                ", firstCallback=" + firstCallback +
                ", changedFlags=" + changedFlags +
                '}';
    }
}
