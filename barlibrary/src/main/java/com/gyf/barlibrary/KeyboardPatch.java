package com.gyf.barlibrary;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

/**
 * 解决底部输入框和软键盘的问题
 * Created by geyifeng on 2017/5/17.
 */
public class KeyboardPatch {

    private Activity mActivity;
    private Window mWindow;
    private View mDecorView;
    private View mContentView;
    private View mChildView;

    private BarParams mBarParams;

    private int paddingLeft;
    private int paddingTop;
    private int paddingRight;
    private int paddingBottom;

    private int keyboardHeightPrevious;
    private int statusBarHeight;
    private int actionBarHeight;
    private int navigationBarHeight;
    private boolean navigationAtBottom;

    private KeyboardPatch(Activity activity) {
        this(activity, ((FrameLayout) activity.getWindow().getDecorView().findViewById(android.R.id.content)).getChildAt(0));
    }

    private KeyboardPatch(Activity activity, View contentView) {
        this(activity, null, "", contentView);
    }

    private KeyboardPatch(Activity activity, Dialog dialog, String tag) {
        this(activity, dialog, tag, dialog.getWindow().findViewById(android.R.id.content));
    }

    private KeyboardPatch(Activity activity, Dialog dialog, String tag, View contentView) {
        this.mActivity = activity;
        this.mWindow = dialog != null ? dialog.getWindow() : activity.getWindow();
        this.mDecorView = mWindow.getDecorView();
        this.mContentView = contentView != null ? contentView
                : mWindow.getDecorView().findViewById(android.R.id.content);
        this.mBarParams = dialog != null ? ImmersionBar.with(activity, dialog, tag).getBarParams()
                : ImmersionBar.with(activity).getBarParams();
        if (mBarParams == null)
            throw new IllegalArgumentException("先使用ImmersionBar初始化");
    }

    private KeyboardPatch(Activity activity, Window window) {
        this.mActivity = activity;
        this.mWindow = window;
        this.mDecorView = mWindow.getDecorView();
        FrameLayout frameLayout = (FrameLayout) mDecorView.findViewById(android.R.id.content);
        this.mChildView = frameLayout.getChildAt(0);
        this.mContentView = mChildView != null ? mChildView : frameLayout;

        this.paddingLeft = mContentView.getPaddingLeft();
        this.paddingTop = mContentView.getPaddingTop();
        this.paddingRight = mContentView.getPaddingRight();
        this.paddingBottom = mContentView.getPaddingBottom();

        BarConfig barConfig = new BarConfig(mActivity);
        this.statusBarHeight = barConfig.getStatusBarHeight();
        this.navigationBarHeight = barConfig.getNavigationBarHeight();
        this.actionBarHeight = barConfig.getActionBarHeight();
        navigationAtBottom = barConfig.isNavigationAtBottom();

    }

    public static KeyboardPatch patch(Activity activity) {
        return new KeyboardPatch(activity);
    }

    public static KeyboardPatch patch(Activity activity, View contentView) {
        return new KeyboardPatch(activity, contentView);
    }

    public static KeyboardPatch patch(Activity activity, Dialog dialog, String tag) {
        return new KeyboardPatch(activity, dialog, tag);
    }

    public static KeyboardPatch patch(Activity activity, Dialog dialog, String tag, View contentView) {
        return new KeyboardPatch(activity, dialog, tag, contentView);
    }

    protected static KeyboardPatch patch(Activity activity, Window window) {
        return new KeyboardPatch(activity, window);
    }

    protected void setBarParams(BarParams barParams) {
        this.mBarParams = barParams;
    }

    /**
     * 监听layout变化
     */
    public void enable() {
        enable(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    public void enable(int mode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mWindow.setSoftInputMode(mode);
            mDecorView.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);//当在一个视图树中全局布局发生改变或者视图树中的某个视图的可视状态发生改变时，所要调用的回调函数的接口类
        }
    }

    /**
     * 取消监听
     */
    public void disable() {
        disable(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    public void disable(int mode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mWindow.setSoftInputMode(mode);
            mDecorView.getViewTreeObserver().removeOnGlobalLayoutListener(onGlobalLayoutListener);
        }
    }

    private ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            //如果布局根节点使用了android:fitsSystemWindows="true"属性或者导航栏不在底部，无需处理
            if (!navigationAtBottom)
                return;
            Rect r = new Rect();
            mDecorView.getWindowVisibleDisplayFrame(r); //获取当前窗口可视区域大小
            int diff;
            int keyboardHeight;
            boolean isPopup = false;
            if (mBarParams.systemWindows) {
                keyboardHeight = mContentView.getHeight() - r.bottom - navigationBarHeight;
                if (mBarParams.onKeyboardListener != null) {
                    if (keyboardHeight > navigationBarHeight)
                        isPopup = true;
                    mBarParams.onKeyboardListener.onKeyboardChange(isPopup, keyboardHeight);
                }
                return;
            }
            if (mChildView != null) {
                if (mBarParams.isSupportActionBar)
                    diff = mContentView.getHeight() + statusBarHeight + actionBarHeight - r.bottom;
                else if (mBarParams.fits)
                    diff = mContentView.getHeight() + statusBarHeight - r.bottom;
                else
                    diff = mContentView.getHeight() - r.bottom;
                if (mBarParams.fullScreen)
                    keyboardHeight = diff - navigationBarHeight;
                else
                    keyboardHeight = diff;
                if (mBarParams.fullScreen && diff == navigationBarHeight) {
                    diff -= navigationBarHeight;
                }
                if (keyboardHeight != keyboardHeightPrevious) {
                    mContentView.setPadding(paddingLeft, paddingTop, paddingRight, diff + paddingBottom);
                    keyboardHeightPrevious = keyboardHeight;
                    if (mBarParams.onKeyboardListener != null) {
                        if (keyboardHeight > navigationBarHeight)
                            isPopup = true;
                        mBarParams.onKeyboardListener.onKeyboardChange(isPopup, keyboardHeight);
                    }
                }
            } else {
                diff = mContentView.getHeight() - r.bottom;

                if (mBarParams.navigationBarEnable && mBarParams.navigationBarWithKitkatEnable) {
                    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT || OSUtils.isEMUI3_1()) {
                        keyboardHeight = diff - navigationBarHeight;
                    } else {
                        if (!mBarParams.fullScreen)
                            keyboardHeight = diff;
                        else
                            keyboardHeight = diff - navigationBarHeight;
                    }
                    if (mBarParams.fullScreen && diff == navigationBarHeight)
                        diff -= navigationBarHeight;
                } else
                    keyboardHeight = diff;
                if (keyboardHeight != keyboardHeightPrevious) {
                    if (mBarParams.isSupportActionBar) {
                        mContentView.setPadding(0, statusBarHeight + actionBarHeight, 0, diff);
                    } else if (mBarParams.fits) {
                        mContentView.setPadding(0, statusBarHeight, 0, diff);
                    } else
                        mContentView.setPadding(0, 0, 0, diff);
                    keyboardHeightPrevious = keyboardHeight;
                    if (mBarParams.onKeyboardListener != null) {
                        if (keyboardHeight > navigationBarHeight)
                            isPopup = true;
                        mBarParams.onKeyboardListener.onKeyboardChange(isPopup, keyboardHeight);
                    }
                }
            }
        }
    };

}
