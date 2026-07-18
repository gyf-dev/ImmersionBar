package com.gyf.immersionbar;

/**
 * The interface On status bar listener.
 * 状态栏显示隐藏监听。
 *
 * @author geyifeng
 * @deprecated 统一使用{@link OnBarPropertiesChangedListener}，通过{@link BarProperties#isStatusBarVisible()}
 * 获取状态栏显隐。
 */
@Deprecated
public interface OnStatusBarListener {
    /**
     * On status bar change.
     *
     * @param show 状态栏是否显示
     */
    void onStatusBarChange(boolean show);
}
