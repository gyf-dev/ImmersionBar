package com.gyf.immersionbar;

import static com.gyf.immersionbar.Constants.IMMERSION_NAVIGATION_BAR_HEIGHT;
import static com.gyf.immersionbar.Constants.IMMERSION_NAVIGATION_BAR_HEIGHT_LANDSCAPE;
import static com.gyf.immersionbar.Constants.IMMERSION_NAVIGATION_BAR_WIDTH;
import static com.gyf.immersionbar.Constants.IMMERSION_STATUS_BAR_HEIGHT;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Insets;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

/**
 * The type Bar config.
 *
 * @author geyifeng
 * @date 2017 /5/11
 */
@SuppressWarnings("deprecation")
class BarConfig {

    private final int mStatusBarHeight;
    private final boolean mStatusBarVisible;
    private final int mActionBarHeight;
    private final boolean mHasNavigationBar;
    private final int mNavigationBarHeight;
    private final int mNavigationBarHeightIgnoringVisibility;
    private final boolean mNavigationBarVisible;
    private final int mNavigationBarWidth;
    private final boolean mInPortrait;
    private final float mSmallestWidthDp;
    private final boolean mNavigationAtBottom;

    /**
     * Instantiates a new Bar config.
     *
     * @param activity the activity
     */
    BarConfig(@NonNull Activity activity) {
        this(activity.getWindow());
    }

    /**
     * Instantiates a new Bar config.
     *
     * @param fragment the fragment
     */
    BarConfig(@NonNull Fragment fragment) {
        this(requireWindow(WindowUtils.getWindow(fragment)));
    }

    /**
     * Instantiates a new Bar config.
     *
     * @param fragment the fragment
     */
    BarConfig(@NonNull android.app.Fragment fragment) {
        this(requireWindow(WindowUtils.getWindow(fragment)));
    }

    /**
     * Instantiates a new Bar config.
     *
     * @param dialog the dialog
     */
    BarConfig(@NonNull Dialog dialog) {
        this(requireWindow(WindowUtils.getWindow(dialog)));
    }

    /**
     * Instantiates a new Bar config.
     * 以Window为核心，同时支持Activity、Fragment和Dialog。
     *
     * @param window the window
     */
    BarConfig(@NonNull Window window) {
        Context context = window.getContext();
        Resources res = context.getResources();
        mInPortrait = (res.getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);
        mSmallestWidthDp = getSmallestWidthDp(window);
        mStatusBarHeight = getStatusBarHeight(window);
        mStatusBarVisible = isStatusBarVisible(window);
        mActionBarHeight = getActionBarHeight(window);
        mNavigationBarHeight = getNavigationBarHeight(window);
        mNavigationBarHeightIgnoringVisibility = getNavigationBarHeightIgnoringVisibility(window);
        mNavigationBarVisible = isNavigationBarVisible(window);
        mNavigationBarWidth = getNavigationBarWidth(window);
        mHasNavigationBar = (mNavigationBarHeight > 0);
        mNavigationAtBottom = computeNavigationAtBottom(window);
    }

    @NonNull
    private static Window requireWindow(Window window) {
        if (window == null) {
            throw new IllegalArgumentException("Owner must be attached to a Window.");
        }
        return window;
    }

    /**
     * 计算导航栏是否在底部。
     * android 11(API 30)起WindowInsets可用，优先用其实际方向判断（底部有inset即在底部，否则在侧边），
     * 比老的“手机横屏导航栏在侧边”启发式更准；insets不可用或低版本时回退到原启发式。
     *
     * @param window window
     * @return true表示导航栏在底部，false表示在侧边
     */
    private boolean computeNavigationAtBottom(Window window) {
        if (Build.VERSION.SDK_INT >= Version.R) {
            Insets navInsets = getNavigationBarInsets(window);
            if (navInsets != null && (navInsets.bottom > 0 || navInsets.left > 0 || navInsets.right > 0)) {
                return navInsets.bottom > 0;
            }
        }
        return (mSmallestWidthDp >= 600 || mInPortrait);
    }

    @SuppressLint("ObsoleteSdkInt")
    @androidx.annotation.RequiresApi(Version.ICE_CREAM_SANDWICH)
    private int getStatusBarHeight(Window window) {
        if (Build.VERSION.SDK_INT >= Version.R) {
            Insets statusInsets = getStatusBarInsetsIgnoringVisibility(window);
            if (statusInsets != null) {
                return statusInsets.top;
            }
        }
        return getInternalDimensionSize(window.getContext(), IMMERSION_STATUS_BAR_HEIGHT);
    }

    @SuppressLint("ObsoleteSdkInt")
    @androidx.annotation.RequiresApi(Version.ICE_CREAM_SANDWICH)
    private boolean isStatusBarVisible(Window window) {
        if (Build.VERSION.SDK_INT >= Version.R) {
            WindowInsets insets = window.getDecorView().getRootWindowInsets();
            if (insets != null) {
                return insets.isVisible(WindowInsets.Type.statusBars());
            }
        } else if (Build.VERSION.SDK_INT >= Version.JELLY_BEAN) {
            View decorView = window.getDecorView();
            int visibility = decorView.getSystemUiVisibility();
            return (visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0;
        }
        return true;
    }

    @SuppressLint("ObsoleteSdkInt")
    @androidx.annotation.RequiresApi(Version.ICE_CREAM_SANDWICH)
    private boolean isNavigationBarVisible(Window window) {
        if (Build.VERSION.SDK_INT >= Version.R) {
            WindowInsets insets = window.getDecorView().getRootWindowInsets();
            if (insets != null) {
                return insets.isVisible(WindowInsets.Type.navigationBars());
            }
        } else if (Build.VERSION.SDK_INT >= Version.JELLY_BEAN) {
            return hasNavBar(window);
        }
        return true;
    }

    private int getActionBarHeight(Window window) {
        int result = 0;
        View actionBar = window.findViewById(R.id.action_bar_container);
        if (actionBar != null) {
            result = actionBar.getMeasuredHeight();
        }
        if (result == 0) {
            Context context = window.getContext();
            TypedValue tv = new TypedValue();
            context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true);
            result = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
        }
        return result;
    }

    @SuppressLint("ObsoleteSdkInt")
    @androidx.annotation.RequiresApi(Version.ICE_CREAM_SANDWICH)
    private int getNavigationBarHeight(Window window) {
        int result = 0;
        if (hasNavBar(window)) {
            if (Build.VERSION.SDK_INT >= Version.R) {
                Insets navInsets = getNavigationBarInsets(window);
                if (navInsets != null) {
                    return navInsets.bottom;
                }
            }
            return getNavigationBarHeightInternal(window.getContext());
        }
        return result;
    }

    /**
     * 获取导航栏高度，忽略运行时可见性（隐藏时也返回其实际尺寸）。
     * 手势导航（且厂商不可检测导航栏存在性）与hasNavBar一致视为无导航栏返回0；
     * API 30+用getInsetsIgnoringVisibility，低版本用与可见性无关的静态资源高度。
     */
    @SuppressLint("ObsoleteSdkInt")
    @androidx.annotation.RequiresApi(Version.ICE_CREAM_SANDWICH)
    private int getNavigationBarHeightIgnoringVisibility(Window window) {
        if (Build.VERSION.SDK_INT >= Version.JELLY_BEAN_MR1) {
            GestureUtils.GestureBean gestureBean = GestureUtils.getGestureBean(window.getContext());
            if (!gestureBean.checkNavigation && gestureBean.isGesture) {
                return 0;
            }
        }
        if (Build.VERSION.SDK_INT >= Version.R) {
            Insets navInsets = getNavigationBarInsetsIgnoringVisibility(window);
            if (navInsets != null) {
                return navInsets.bottom;
            }
        }
        return getNavigationBarHeightInternal(window.getContext());
    }

    @SuppressLint("ObsoleteSdkInt")
    @androidx.annotation.RequiresApi(Version.ICE_CREAM_SANDWICH)
    private int getNavigationBarWidth(Window window) {
        int result = 0;
        if (hasNavBar(window)) {
            if (Build.VERSION.SDK_INT >= Version.R) {
                Insets navInsets = getNavigationBarInsets(window);
                if (navInsets != null) {
                    int sideWidth = Math.max(navInsets.left, navInsets.right);
                    if (sideWidth > 0) {
                        return sideWidth;
                    }
                }
            }
            return getInternalDimensionSize(window.getContext(), IMMERSION_NAVIGATION_BAR_WIDTH);
        }
        return result;
    }

    @SuppressLint("ObsoleteSdkInt")
    @androidx.annotation.RequiresApi(Version.ICE_CREAM_SANDWICH)
    private boolean hasNavBar(Window window) {
        if (Build.VERSION.SDK_INT >= Version.JELLY_BEAN_MR1) {
            GestureUtils.GestureBean gestureBean = GestureUtils.getGestureBean(window.getContext());
            if (!gestureBean.checkNavigation && gestureBean.isGesture) {
                return false;
            }
        }
        if (isNavigationBarHidden(window)) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= Version.R) {
            Insets navInsets = getNavigationBarInsets(window);
            if (navInsets != null) {
                return navInsets.bottom > 0 || navInsets.left > 0 || navInsets.right > 0;
            }
        }
        WindowManager windowManager = window.getWindowManager();
        Display d = windowManager.getDefaultDisplay();

        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Version.JELLY_BEAN_MR1) {
            d.getRealMetrics(realDisplayMetrics);
        }

        int realHeight = realDisplayMetrics.heightPixels;
        int realWidth = realDisplayMetrics.widthPixels;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        d.getMetrics(displayMetrics);

        int displayHeight = displayMetrics.heightPixels;
        int displayWidth = displayMetrics.widthPixels;
        return (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0;
    }

    private boolean isNavigationBarHidden(Window window) {
        if (Build.VERSION.SDK_INT >= Version.JELLY_BEAN && Build.VERSION.SDK_INT < Version.R) {
            if (OSUtils.isEMUI3_x() && EMUI3NavigationBarObserver.getInstance().isNavigationBarHidden(window.getContext())) {
                return true;
            }
            View decorView = window.getDecorView();
            int visibility = decorView.getSystemUiVisibility();
            return (visibility & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) != 0;
        }
        return false;
    }

    /**
     * 获取系统导航栏的WindowInsets（API 30+，挖孔屏/edge-to-edge下比DisplayMetrics差值更可靠的导航栏尺寸来源）。
     * 优先使用WindowMetrics（不依赖视图是否attach），其次回退到decorView的rootWindowInsets。
     * 接收Window而非Activity，以同时支持Activity和Dialog。
     *
     * @param window window
     * @return 导航栏insets，不可用时返回null
     */
    @RequiresApi(Version.R)
    private static Insets getNavigationBarInsets(Window window) {
        View decorView = window.getDecorView();
        WindowInsets insets = decorView.getRootWindowInsets();
        if (insets != null) {
            return insets.getInsets(WindowInsets.Type.navigationBars());
        }
        WindowManager windowManager = window.getWindowManager();
        if (windowManager != null) {
            insets = windowManager.getCurrentWindowMetrics().getWindowInsets();
            return insets.getInsets(WindowInsets.Type.navigationBars());
        }
        return null;
    }

    /**
     * 获取系统导航栏的WindowInsets（忽略可见性，隐藏时也返回其实际尺寸）。获取途径同{@link #getNavigationBarInsets}。
     *
     * @param window window
     * @return 导航栏insets，不可用时返回null
     */
    @RequiresApi(Version.R)
    private static Insets getNavigationBarInsetsIgnoringVisibility(Window window) {
        View decorView = window.getDecorView();
        WindowInsets insets = decorView.getRootWindowInsets();
        if (insets != null) {
            return insets.getInsetsIgnoringVisibility(WindowInsets.Type.navigationBars());
        }
        WindowManager windowManager = window.getWindowManager();
        if (windowManager != null) {
            insets = windowManager.getCurrentWindowMetrics().getWindowInsets();
            return insets.getInsetsIgnoringVisibility(WindowInsets.Type.navigationBars());
        }
        return null;
    }

    /**
     * 获取系统状态栏的WindowInsets（API 30+，挖孔屏等机型下比资源status_bar_height更可靠的状态栏尺寸来源）。
     * 使用getInsetsIgnoringVisibility以获取状态栏的实际物理尺寸，无论其是否被隐藏。
     *
     * @param window window
     * @return 状态栏insets，不可用时返回null
     */
    @RequiresApi(Version.R)
    private static Insets getStatusBarInsetsIgnoringVisibility(Window window) {
        View decorView = window.getDecorView();
        WindowInsets insets = decorView.getRootWindowInsets();
        if (insets != null) {
            return insets.getInsetsIgnoringVisibility(WindowInsets.Type.statusBars());
        }
        WindowManager windowManager = window.getWindowManager();
        if (windowManager != null) {
            insets = windowManager.getCurrentWindowMetrics().getWindowInsets();
            return insets.getInsetsIgnoringVisibility(WindowInsets.Type.statusBars());
        }
        return null;
    }

    static int getInternalDimensionSize(Context context, String key) {
        int result = 0;
        try {
            @SuppressLint("DiscouragedApi")
            int resourceId = Resources.getSystem().getIdentifier(key, "dimen", "android");
            if (resourceId > 0) {
                int sizeOne = context.getResources().getDimensionPixelSize(resourceId);
                int sizeTwo = Resources.getSystem().getDimensionPixelSize(resourceId);

                if (sizeTwo >= sizeOne && !(Build.VERSION.SDK_INT >= Version.Q &&
                        !key.equals(IMMERSION_STATUS_BAR_HEIGHT))) {
                    return sizeTwo;
                } else {
                    float densityOne = context.getResources().getDisplayMetrics().density;
                    float densityTwo = Resources.getSystem().getDisplayMetrics().density;
                    float f = sizeOne * densityTwo / densityOne;
                    return (int) ((f >= 0) ? (f + 0.5f) : (f - 0.5f));
                }
            }
        } catch (Resources.NotFoundException ignored) {
            return 0;
        }
        return result;
    }

    @SuppressLint("NewApi")
    private float getSmallestWidthDp(Window window) {
        DisplayMetrics metrics = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Version.JELLY_BEAN) {
            window.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        } else {
            window.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        }
        float widthDp = metrics.widthPixels / metrics.density;
        float heightDp = metrics.heightPixels / metrics.density;
        return Math.min(widthDp, heightDp);
    }

    /**
     * Should a navigation bar appear at the bottom of the screen in the current
     * device configuration? A navigation bar may appear on the right side of
     * the screen in certain configurations.
     *
     * @return True if navigation should appear at the bottom of the screen, False otherwise.
     */
    boolean isNavigationAtBottom() {
        return mNavigationAtBottom;
    }

    /**
     * Get the height of the system status bar.
     *
     * @return The height of the status bar (in pixels).
     */
    int getStatusBarHeight() {
        return mStatusBarHeight;
    }

    /**
     * Check if the system status bar is visible.
     *
     * @return True if the status bar is visible, False otherwise.
     */
    boolean isStatusBarVisible() {
        return mStatusBarVisible;
    }

    /**
     * Get the height of the action bar.
     *
     * @return The height of the action bar (in pixels).
     */
    int getActionBarHeight() {
        return mActionBarHeight;
    }

    /**
     * Does this device have a system navigation bar?
     *
     * @return True if this device uses soft key navigation, False otherwise.
     */
    boolean hasNavigationBar() {
        return mHasNavigationBar;
    }

    /**
     * Get the height of the system navigation bar.
     *
     * @return The height of the navigation bar (in pixels). If the device does not have soft navigation keys, this will always return 0.
     */
    int getNavigationBarHeight() {
        return mNavigationBarHeight;
    }

    /**
     * 获取导航栏高度，忽略运行时可见性（隐藏时也返回其实际尺寸）。
     *
     * @return The height of the navigation bar (in pixels), regardless of its current visibility.
     */
    int getNavigationBarHeightIgnoringVisibility() {
        return mNavigationBarHeightIgnoringVisibility;
    }

    /**
     * Check if the system navigation bar is visible.
     *
     * @return True if the navigation bar is visible, False otherwise.
     */
    boolean isNavigationBarVisible() {
        return mNavigationBarVisible;
    }

    /**
     * Get the width of the system navigation bar when it is placed vertically on the screen.
     *
     * @return The width of the navigation bar (in pixels). If the device does not have soft navigation keys, this will always return 0.
     */
    int getNavigationBarWidth() {
        return mNavigationBarWidth;
    }

    /**
     * 获得导航栏高度
     *
     */
    static int getNavigationBarHeightInternal(@NonNull Context context) {
        String key;
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            key = IMMERSION_NAVIGATION_BAR_HEIGHT;
        } else {
            key = IMMERSION_NAVIGATION_BAR_HEIGHT_LANDSCAPE;
        }
        return getInternalDimensionSize(context, key);
    }

    static int getNavigationBarWidthInternal(@NonNull Context context) {
        return getInternalDimensionSize(context, IMMERSION_NAVIGATION_BAR_WIDTH);
    }
}