package com.gyf.immersionbar;

/**
 * The interface On status bar listener.
 * 状态栏显示隐藏监听。
 *
 * @author geyifeng
 * @deprecated 使用{@link OnStatusBarChangedListener}代替。
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
