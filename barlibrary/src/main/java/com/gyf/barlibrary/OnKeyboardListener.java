package com.gyf.barlibrary;

/**
 * 软键盘监听
 *
 * @author geyifeng
 * @date 2017/8/28
 */
public interface OnKeyboardListener {
    /**
     * On keyboard change.
     *
     * @param isPopup        the is popup  是否弹出
     * @param keyboardHeight the keyboard height  软键盘高度
     */
    void onKeyboardChange(boolean isPopup, int keyboardHeight);
}
