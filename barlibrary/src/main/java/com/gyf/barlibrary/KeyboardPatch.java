package com.gyf.barlibrary;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

/**
 * 解决底部输入框和软键盘的问题
 * Created by geyifeng on 2017/5/17.
 */
public class KeyboardPatch {
    private Activity mActivity;
    private View mDecorView;
    private View mContentView;
    private boolean flag = false;
    private BarParams mBarParams;

    private KeyboardPatch(Activity activity) {
        this(activity, activity.findViewById(android.R.id.content));
    }

    private KeyboardPatch(Activity activity, View contentView) {
        this.mActivity = activity;
        this.mDecorView = activity.getWindow().getDecorView();
        this.mContentView = contentView;
        mBarParams = ImmersionBar.with(mActivity).getBarParams();
        if (contentView.equals(activity.findViewById(android.R.id.content))) {
            this.flag = false;
        } else {
            this.flag = true;
        }
    }

    /**
     * Patch keyboard patch.
     *
     * @param activity the activity
     * @return the keyboard patch
     */
    public static KeyboardPatch patch(Activity activity) {
        return new KeyboardPatch(activity);
    }

    /**
     * Patch keyboard patch.
     *
     * @param activity    the activity
     * @param contentView 界面容器，指定布局的根节点
     * @return the keyboard patch
     */
    public static KeyboardPatch patch(Activity activity, View contentView) {
        return new KeyboardPatch(activity, contentView);
    }

    /**
     * 监听layout变化
     */
    public void enable() {
        enable(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    public void enable(int mode) {
        mActivity.getWindow().setSoftInputMode(mode);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mDecorView.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);//当在一个视图树中全局布局发生改变或者视图树中的某个视图的可视状态发生改变时，所要调用的回调函数的接口类
        }
    }

    /**
     * 取消监听
     */
    public void disable() {
        disable(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    public void disable(int mode) {
        mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
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
            if (diff > 0) {
                if (mContentView.getPaddingBottom() != diff) {
                    if (flag || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !OSUtils.isEMUI3_1())
                            || !mBarParams.navigationBarEnable) {
                        if (mBarParams.fits)
                            mContentView.setPadding(0, ImmersionBar.getStatusBarHeight(mActivity), 0, diff);
                        else
                            mContentView.setPadding(0, 0, 0, diff);
                    } else {
                        if (mBarParams.fits)
                            mContentView.setPadding(0, ImmersionBar.getStatusBarHeight(mActivity),
                                    0, diff + ImmersionBar.getNavigationBarHeight(mActivity));
                        else
                            mContentView.setPadding(0, 0, 0, diff + ImmersionBar.getNavigationBarHeight(mActivity));
                    }
                }
            } else {
                if (mContentView.getPaddingBottom() != 0) {
                    if (flag || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !OSUtils.isEMUI3_1())
                            || !mBarParams.navigationBarEnable) {
                        if (mBarParams.fits)
                            mContentView.setPadding(0, ImmersionBar.getStatusBarHeight(mActivity), 0, 0);
                        else
                            mContentView.setPadding(0, 0, 0, 0);
                    } else {
                        if (mBarParams.fits)
                            mContentView.setPadding(0, ImmersionBar.getStatusBarHeight(mActivity),
                                    0, ImmersionBar.getNavigationBarHeight(mActivity));
                        else
                            mContentView.setPadding(0, 0, 0, ImmersionBar.getNavigationBarHeight(mActivity));
                    }
                }
            }
        }
    };
}
