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
    private boolean mFlag = false;
    private BarParams mBarParams;

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
        this.mDecorView = activity.getWindow().getDecorView();
        this.mContentView = contentView != null ? contentView
                : mWindow.getDecorView().findViewById(android.R.id.content);
        this.mBarParams = dialog != null ? ImmersionBar.with(activity, dialog, tag).getBarParams()
                : ImmersionBar.with(activity).getBarParams();
        if (mBarParams == null)
            throw new IllegalArgumentException("先使用ImmersionBar初始化");
        if (!mContentView.equals(mDecorView.findViewById(android.R.id.content)))
            this.mFlag = true;
    }

    private KeyboardPatch(Activity activity, Window window, BarParams barParams) {
        this.mActivity = activity;
        this.mWindow = window;
        this.mDecorView = activity.getWindow().getDecorView();
        this.mBarParams = barParams;
        FrameLayout frameLayout = (FrameLayout) mWindow.getDecorView().findViewById(android.R.id.content);
        if (frameLayout.getChildAt(0) != null && !mBarParams.systemWindows) {
            this.mFlag = true;
        }
        this.mContentView = frameLayout.getChildAt(0) != null ? frameLayout.getChildAt(0) : frameLayout;
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

    protected static KeyboardPatch patch(Activity activity, Window window, BarParams barParams) {
        return new KeyboardPatch(activity, window, barParams);
    }

    /**
     * 监听layout变化
     */
    public void enable() {
        enable(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    public void enable(int mode) {
        mWindow.setSoftInputMode(mode);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
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
        mWindow.setSoftInputMode(mode);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mDecorView.getViewTreeObserver().removeOnGlobalLayoutListener(onGlobalLayoutListener);
        }
    }

    private ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            Rect r = new Rect();
            mDecorView.getWindowVisibleDisplayFrame(r); //获取当前窗口可视区域大小的
            int height = mDecorView.getContext().getResources().getDisplayMetrics().heightPixels; //获取屏幕密度，不包含导航栏
            int diff = height - r.bottom;
            if (diff >= 0) {
                if (mFlag || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !OSUtils.isEMUI3_1())
                        || !mBarParams.navigationBarEnable || !mBarParams.navigationBarWithKitkatEnable) {
                    mContentView.setPadding(0, mContentView.getPaddingTop(), 0, diff);
                } else {
                    mContentView.setPadding(0, mContentView.getPaddingTop(),
                            0, diff + ImmersionBar.getNavigationBarHeight(mActivity));
                }
            }
        }
    };
}
