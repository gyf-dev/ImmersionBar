package com.gyf.immersionbar;

import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowInsets;
import android.widget.FrameLayout;

import androidx.annotation.RequiresApi;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

/**
 * 适配软键盘弹出问题
 *
 * @author geyifeng
 * @date 2018/11/9 10:24 PM
 */
class FitsKeyboard implements ViewTreeObserver.OnGlobalLayoutListener {

    private ImmersionBar mImmersionBar;
    private Window mWindow;
    private View mDecorView;
    private FrameLayout mContentRoot;
    private View mContentView;
    private View mChildView;
    private int mPaddingLeft = 0, mPaddingTop = 0, mPaddingRight = 0, mPaddingBottom = 0;
    private int mTempKeyboardHeight;
    private boolean mIsAddListener;

    FitsKeyboard(ImmersionBar immersionBar) {
        mImmersionBar = immersionBar;
        mWindow = immersionBar.getWindow();
        mDecorView = mWindow.getDecorView();
        mContentRoot = mDecorView.findViewById(android.R.id.content);
        if (immersionBar.isDialogFragment()) {
            Fragment supportFragment = immersionBar.getSupportFragment();
            if (supportFragment != null) {
                mChildView = supportFragment.getView();
            } else {
                android.app.Fragment fragment = immersionBar.getFragment();
                if (fragment != null) {
                    mChildView = fragment.getView();
                }
            }
        } else {
            mChildView = mContentRoot.getChildAt(0);
            if (mChildView instanceof DrawerLayout) {
                mChildView = ((DrawerLayout) mChildView).getChildAt(0);
            }
        }
        if (mChildView != null) {
            mPaddingLeft = mChildView.getPaddingLeft();
            mPaddingTop = mChildView.getPaddingTop();
            mPaddingRight = mChildView.getPaddingRight();
            mPaddingBottom = mChildView.getPaddingBottom();
        }
        mContentView = mChildView != null ? mChildView : mContentRoot;
    }

    void enable(int mode) {
        if (Build.VERSION.SDK_INT >= Version.KITKAT) {
            mWindow.setSoftInputMode(mode);
            if (!mIsAddListener) {
                mDecorView.getViewTreeObserver().addOnGlobalLayoutListener(this);
                mIsAddListener = true;
            }
        }
    }

    void disable() {
        if (Build.VERSION.SDK_INT >= Version.KITKAT && mIsAddListener) {
            setContentPadding(getDefaultPaddingBottom());
        }
    }

    void cancel() {
        if (Build.VERSION.SDK_INT >= Version.KITKAT && mIsAddListener) {
            mDecorView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            mIsAddListener = false;
        }
    }

    void resetKeyboardHeight() {
        mTempKeyboardHeight = 0;
    }

    @Override
    public void onGlobalLayout() {
        if (!isKeyboardEnabled()) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Version.R && handleKeyboardAboveR()) {
            return;
        }
        handleKeyboardBelowR();
    }

    private boolean isKeyboardEnabled() {
        return mImmersionBar != null
                && mImmersionBar.getBarParams() != null
                && mImmersionBar.getBarParams().keyboardEnable;
    }

    @RequiresApi(api = Version.R)
    private boolean handleKeyboardAboveR() {
        WindowInsets windowInsets = mDecorView.getRootWindowInsets();
        if (windowInsets == null) {
            return false;
        }
        int imeInsetBottom = windowInsets.getInsets(WindowInsets.Type.ime()).bottom;
        boolean imeVisible = windowInsets.isVisible(WindowInsets.Type.ime()) && imeInsetBottom > 0;
        int keyboardHeight = imeVisible ? getKeyboardOverlap(imeInsetBottom) : 0;
        boolean isPopup = keyboardHeight > 0;
        if (shouldApplyKeyboardPadding()) {
            setContentPadding(getDefaultPaddingBottom() + keyboardHeight);
        }
        if (keyboardHeight == mTempKeyboardHeight) {
            return true;
        }
        mTempKeyboardHeight = keyboardHeight;
        dispatchKeyboardChange(isPopup, keyboardHeight);
        return true;
    }

    private int getKeyboardOverlap(int imeInsetBottom) {
        int[] decorLocation = new int[2];
        int[] contentLocation = new int[2];
        mDecorView.getLocationInWindow(decorLocation);
        mContentView.getLocationInWindow(contentLocation);
        int windowBottom = decorLocation[1] + mDecorView.getHeight();
        int contentBottom = contentLocation[1] + mContentView.getHeight();
        int consumedBottomPadding = mChildView == null ? mImmersionBar.getPaddingBottom() : 0;
        return calculateKeyboardOverlap(windowBottom, contentBottom, consumedBottomPadding, imeInsetBottom);
    }

    static int calculateKeyboardOverlap(int windowBottom, int contentBottom,
                                        int consumedBottomPadding, int imeInsetBottom) {
        int imeTop = windowBottom - imeInsetBottom;
        return Math.max(0, contentBottom - consumedBottomPadding - imeTop);
    }

    private void handleKeyboardBelowR() {
        BarConfig barConfig = mImmersionBar.getBarConfig();
        int navigationBarHeight = barConfig.isNavigationAtBottom()
                ? barConfig.getNavigationBarHeight() : barConfig.getNavigationBarWidth();
        Rect rect = new Rect();
        //获取当前窗口可视区域大小
        mDecorView.getWindowVisibleDisplayFrame(rect);
        int keyboardHeight = mContentView.getHeight() - rect.bottom;
        if (keyboardHeight == mTempKeyboardHeight) {
            return;
        }
        mTempKeyboardHeight = keyboardHeight;
        boolean isPopup = false;
        if (shouldApplyKeyboardPadding()) {
            int bottom = 0;
            if (mChildView != null) {
                if (mImmersionBar.getBarParams().isSupportActionBar) {
                    keyboardHeight += mImmersionBar.getActionBarHeight() + barConfig.getStatusBarHeight();
                }
                if (mImmersionBar.getBarParams().fits) {
                    keyboardHeight += barConfig.getStatusBarHeight();
                }
                if (keyboardHeight > navigationBarHeight) {
                    bottom = keyboardHeight + mPaddingBottom;
                    isPopup = true;
                }
            } else {
                bottom = mImmersionBar.getPaddingBottom();
                keyboardHeight -= navigationBarHeight;
                if (keyboardHeight > navigationBarHeight) {
                    bottom = keyboardHeight + navigationBarHeight;
                    isPopup = true;
                }
            }
            setContentPadding(bottom);
        } else {
            keyboardHeight -= navigationBarHeight;
            if (keyboardHeight > navigationBarHeight) {
                isPopup = true;
            }
        }
        dispatchKeyboardChange(isPopup, Math.max(0, keyboardHeight));
    }

    private boolean shouldApplyKeyboardPadding() {
        return !ImmersionBar.checkFitsSystemWindows(mContentRoot);
    }

    private int getDefaultPaddingBottom() {
        return mChildView != null ? mPaddingBottom : mImmersionBar.getPaddingBottom();
    }

    private void setContentPadding(int bottom) {
        int left = mChildView != null ? mPaddingLeft : mImmersionBar.getPaddingLeft();
        int top = mChildView != null ? mPaddingTop : mImmersionBar.getPaddingTop();
        int right = mChildView != null ? mPaddingRight : mImmersionBar.getPaddingRight();
        if (mContentView.getPaddingLeft() != left
                || mContentView.getPaddingTop() != top
                || mContentView.getPaddingRight() != right
                || mContentView.getPaddingBottom() != bottom) {
            mContentView.setPadding(left, top, right, bottom);
        }
    }

    private void dispatchKeyboardChange(boolean isPopup, int keyboardHeight) {
        mImmersionBar.dispatchOnKeyboardChanged(isPopup, keyboardHeight);
        if (!isPopup && mImmersionBar.getBarParams().barHide != BarHide.FLAG_SHOW_BAR) {
            mImmersionBar.setBar();
        }
        if (!isPopup) {
            mImmersionBar.fitsParentBarKeyboard();
        }
    }
}
