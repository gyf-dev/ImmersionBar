package com.gyf.barlibrary;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

/**
 * 解决
 * Created by geyifeng on 2017/5/17.
 */

public class KeyboardPatch {
    private Activity mActivity;
    private View mDecorView;
    private View mContentView;


    private KeyboardPatch() {
    }

    /**
     * 构造函数
     *
     * @param activity    需要解决bug的activity
     * @param contentView 界面容器，如果使用ImmersionBar这个库，对于android 5.0来说，在activity中一般是R.id.content
     *                    ，也可能是Fragment的容器，根据个人需要传递，为了方便指定布局的根节点就行
     */
    private KeyboardPatch(Activity activity, View contentView) {
        this.mActivity = activity;
        this.mDecorView = activity.getWindow().getDecorView();
        this.mContentView = contentView;
    }

    public static KeyboardPatch patch(Activity activity, View contentView) {
        return new KeyboardPatch(activity, contentView);
    }

    /**
     * 监听layout变化
     */
    public void enable() {
        mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mDecorView.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
        }
    }

    /**
     * 取消监听
     */
    public void disable() {
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

            mDecorView.getWindowVisibleDisplayFrame(r);
            int height = mDecorView.getContext().getResources().getDisplayMetrics().heightPixels;
            int diff = height - r.bottom;

            if (diff != 0) {
                if (mContentView.getPaddingBottom() != diff) {
                    mContentView.setPadding(0, 0, 0, diff);
                }
            } else {
                if (mContentView.getPaddingBottom() != 0) {
                    mContentView.setPadding(0, 0, 0, 0);
                }
            }
        }
    };
}
