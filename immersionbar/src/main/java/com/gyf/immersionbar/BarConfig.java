package com.gyf.immersionbar;

import static com.gyf.immersionbar.Constants.IMMERSION_NAVIGATION_BAR_HEIGHT;
import static com.gyf.immersionbar.Constants.IMMERSION_NAVIGATION_BAR_HEIGHT_LANDSCAPE;
import static com.gyf.immersionbar.Constants.IMMERSION_NAVIGATION_BAR_WIDTH;
import static com.gyf.immersionbar.Constants.IMMERSION_STATUS_BAR_HEIGHT;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
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

/**
 * The type Bar config.
 *
 * @author geyifeng
 * @date 2017 /5/11
 */
class BarConfig {

    private final int mStatusBarHeight;
    private final int mActionBarHeight;
    private final boolean mHasNavigationBar;
    private final int mNavigationBarHeight;
    private final int mNavigationBarWidth;
    private final boolean mInPortrait;
    private final float mSmallestWidthDp;
    private final boolean mNavigationAtBottom;

    /**
     * Instantiates a new Bar config.
     *
     * @param activity the activity
     */
    BarConfig(Activity activity) {
        this(activity.getWindow());
    }

    /**
     * Instantiates a new Bar config.
     * 以Window为核心，同时支持Activity和Dialog。
     *
     * @param window the window
     */
    BarConfig(Window window) {
        Context context = window.getContext();
        Resources res = context.getResources();
        mInPortrait = (res.getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);
        mSmallestWidthDp = getSmallestWidthDp(window);
        mStatusBarHeight = getInternalDimensionSize(context, IMMERSION_STATUS_BAR_HEIGHT);
        mActionBarHeight = getActionBarHeight(window);
        mNavigationBarHeight = getNavigationBarHeight(window);
        mNavigationBarWidth = getNavigationBarWidth(window);
        mHasNavigationBar = (mNavigationBarHeight > 0);
        mNavigationAtBottom = computeNavigationAtBottom(window);
    }

    /**
     * 计算导航栏是否在底部。
     * android 15以上edge-to-edge下，手势导航条横竖屏都在底部，老的“手机横屏导航栏在侧边”启发式失效，
     * 改用WindowInsets的实际方向判断；insets不可用或低版本时回退到原启发式。
     *
     * @param window window
     * @return true表示导航栏在底部，false表示在侧边
     */
    private boolean computeNavigationAtBottom(Window window) {
        if (Build.VERSION.SDK_INT >= Version.VANILLA_ICE_CREAM) {
            Insets navInsets = getNavigationBarInsets(window);
            if (navInsets != null && (navInsets.bottom > 0 || navInsets.left > 0 || navInsets.right > 0)) {
                //insets可用：底部有inset即在底部，否则（仅左右有inset）在侧边
                return navInsets.bottom > 0;
            }
            //insets尚不可用（视图未attach）时回退到原启发式
        }
        //老逻辑：平板（smallestWidthDp>=600）或竖屏时导航栏在底部
        return (mSmallestWidthDp >= 600 || mInPortrait);
    }

    @TargetApi(14)
    private int getActionBarHeight(Window window) {
        int result = 0;
        if (Build.VERSION.SDK_INT >= Version.ICE_CREAM_SANDWICH) {
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
        }
        return result;
    }

    @TargetApi(14)
    private int getNavigationBarHeight(Window window) {
        int result = 0;
        if (Build.VERSION.SDK_INT >= Version.ICE_CREAM_SANDWICH) {
            if (hasNavBar(window)) {
                if (Build.VERSION.SDK_INT >= Version.VANILLA_ICE_CREAM) {
                    //android 15以上优先用WindowInsets的实际高度（手势导航/三键导航高度更准）
                    Insets navInsets = getNavigationBarInsets(window);
                    if (navInsets != null) {
                        //insets可用时完全信任它：导航栏在侧边时bottom为0，不能回退到资源高度
                        return navInsets.bottom;
                    }
                }
                return getNavigationBarHeightInternal(window.getContext());
            }
        }
        return result;
    }

    @TargetApi(14)
    private int getNavigationBarWidth(Window window) {
        int result = 0;
        if (Build.VERSION.SDK_INT >= Version.ICE_CREAM_SANDWICH) {
            if (hasNavBar(window)) {
                if (Build.VERSION.SDK_INT >= Version.VANILLA_ICE_CREAM) {
                    //android 15以上优先用WindowInsets的实际宽度（横屏导航栏在侧边）
                    Insets navInsets = getNavigationBarInsets(window);
                    if (navInsets != null) {
                        //insets可用时完全信任它：导航栏在底部时左右为0，不能回退到资源宽度
                        return Math.max(navInsets.left, navInsets.right);
                    }
                }
                return getInternalDimensionSize(window.getContext(), IMMERSION_NAVIGATION_BAR_WIDTH);
            }
        }
        return result;
    }

    @TargetApi(14)
    private boolean hasNavBar(Window window) {
        if (Build.VERSION.SDK_INT >= Version.JELLY_BEAN_MR1) {
            GestureUtils.GestureBean gestureBean = GestureUtils.getGestureBean(window.getContext());
            if (!gestureBean.checkNavigation && gestureBean.isGesture) {
                return false;
            }
        }
        if (Build.VERSION.SDK_INT >= Version.VANILLA_ICE_CREAM) {
            //android 15以上edge-to-edge下，App窗口铺满全屏，realHeight-displayHeight恒为0，
            //DisplayMetrics差值判断失效，改用WindowInsets判断是否存在导航栏
            Insets navInsets = getNavigationBarInsets(window);
            if (navInsets != null) {
                return navInsets.bottom > 0 || navInsets.left > 0 || navInsets.right > 0;
            }
            //insets尚不可用（视图未attach）时回退到原逻辑
        }
        //其他手机根据屏幕真实高度与显示高度是否相同来判断
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

    /**
     * 获取系统导航栏的WindowInsets（API 35+，edge-to-edge下唯一可靠的导航栏尺寸来源）。
     * 优先使用WindowMetrics（不依赖视图是否attach），其次回退到decorView的rootWindowInsets。
     * 接收Window而非Activity，以同时支持Activity和Dialog。
     *
     * @param window window
     * @return 导航栏insets，不可用时返回null
     */
    @TargetApi(Version.VANILLA_ICE_CREAM)
    private static Insets getNavigationBarInsets(Window window) {
        //WindowMetrics直接查询WindowManager，BarConfig构造时视图未attach也能拿到值
        WindowManager windowManager = window.getWindowManager();
        if (windowManager != null) {
            WindowInsets insets = windowManager.getCurrentWindowMetrics().getWindowInsets();
            if (insets != null) {
                return insets.getInsets(WindowInsets.Type.navigationBars());
            }
        }
        //回退：视图已attach时decorView也能拿到
        View decorView = window.getDecorView();
        WindowInsets insets = decorView.getRootWindowInsets();
        if (insets != null) {
            return insets.getInsets(WindowInsets.Type.navigationBars());
        }
        return null;
    }

    static int getInternalDimensionSize(Context context, String key) {
        int result = 0;
        try {
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
     * @param context
     * @return
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