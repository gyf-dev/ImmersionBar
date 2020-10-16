package com.gyf.immersionbar;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;

import static com.gyf.immersionbar.Constants.IMMERSION_EMUI_NAVIGATION_BAR_HIDE_SHOW;
import static com.gyf.immersionbar.Constants.IMMERSION_MIUI_NAVIGATION_BAR_HIDE_SHOW;
import static com.gyf.immersionbar.Constants.IMMERSION_NAVIGATION_BAR_HEIGHT;
import static com.gyf.immersionbar.Constants.IMMERSION_NAVIGATION_BAR_HEIGHT_LANDSCAPE;
import static com.gyf.immersionbar.Constants.IMMERSION_NAVIGATION_BAR_WIDTH;
import static com.gyf.immersionbar.Constants.IMMERSION_STATUS_BAR_HEIGHT;

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

    /**
     * Instantiates a new Bar config.
     *
     * @param activity
     *         the activity
     */
    BarConfig(Activity activity) {
        Resources res = activity.getResources();
        mInPortrait = (res.getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);
        mSmallestWidthDp = getSmallestWidthDp(activity);
        mStatusBarHeight = getInternalDimensionSize(activity, IMMERSION_STATUS_BAR_HEIGHT);
        mActionBarHeight = getActionBarHeight(activity);
        mNavigationBarHeight = getNavigationBarHeight(activity);
        mNavigationBarWidth = getNavigationBarWidth(activity);
        mHasNavigationBar = (mNavigationBarHeight > 0);
    }

    @TargetApi(14)
    private int getActionBarHeight(Activity activity) {
        int result = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            View actionBar = activity.getWindow().findViewById(R.id.action_bar_container);
            if (actionBar != null) {
                result = actionBar.getMeasuredHeight();
            }
            if (result == 0) {
                TypedValue tv = new TypedValue();
                activity.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true);
                result = TypedValue.complexToDimensionPixelSize(tv.data, activity.getResources().getDisplayMetrics());
            }
        }
        return result;
    }

    @TargetApi(14)
    private int getNavigationBarHeight(Context context) {
        int result = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            if (hasNavBar((Activity) context)) {
                String key;
                if (mInPortrait) {
                    key = IMMERSION_NAVIGATION_BAR_HEIGHT;
                } else {
                    key = IMMERSION_NAVIGATION_BAR_HEIGHT_LANDSCAPE;
                }
                return getInternalDimensionSize(context, key);
            }
        }
        return result;
    }

    @TargetApi(14)
    private int getNavigationBarWidth(Context context) {
        int result = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            if (hasNavBar((Activity) context)) {
                return getInternalDimensionSize(context, IMMERSION_NAVIGATION_BAR_WIDTH);
            }
        }
        return result;
    }

    /**
     * 修改：判断是否有导航栏等方式
     *
     * @param activity
     *
     * @return
     *
     * @author Michael Lee 2020-10-16
     */
    @TargetApi(14)
    private boolean hasNavBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            //判断小米手机是否开启了全面屏，开启了，直接返回false
            if (Settings.Global.getInt(activity.getContentResolver(), IMMERSION_MIUI_NAVIGATION_BAR_HIDE_SHOW, 0) != 0) {
                return false;
            }
            //判断华为手机是否隐藏了导航栏，隐藏了，直接返回false
            if (OSUtils.isEMUI()) {
                if (OSUtils.isEMUI3_x() || Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    if (Settings.System.getInt(activity.getContentResolver(), IMMERSION_EMUI_NAVIGATION_BAR_HIDE_SHOW, 0) != 0) {
                        return false;
                    }
                } else {
                    if (Settings.Global.getInt(activity.getContentResolver(), IMMERSION_EMUI_NAVIGATION_BAR_HIDE_SHOW, 0) != 0) {
                        return false;
                    }
                }
            }
        }

        // 判断是否拥有物理按键
        boolean hasMenuKey = ViewConfiguration.get(activity)
                .hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap
                .deviceHasKey(KeyEvent.KEYCODE_BACK);

        if (!hasMenuKey && !hasBackKey) {
            return true;
        }

        ///判断获取navigation_bar_height是否为0
        Resources resources = activity.getResources();
        int resourceId = resources.getIdentifier(
                activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT
                        ? IMMERSION_NAVIGATION_BAR_HEIGHT : IMMERSION_NAVIGATION_BAR_HEIGHT_LANDSCAPE
                , "dimen", "android");

        return resourceId > 0 && resources.getDimensionPixelSize(resourceId) == 0;
    }

    /**
     * 修改：获取导航栏真实高度
     *
     * @param context
     * @param key
     *
     * @return 分四种情况：
     * 1、固定展示导航栏：返回导航栏高度
     * 2、可隐藏可展示的导航栏（手势展示），返回导航栏高度
     * 3、隐藏的导航栏（全面屏手势）或无导航栏，返回0
     * 4、一个小横条的导航栏（Google官方全面屏样式），返回小横条高度
     *
     * @author Michael Lee 2020-10-16
     */
    private int getInternalDimensionSize(Context context, String key) {
        int result = 0;
        try {
            int resourceId = Resources.getSystem().getIdentifier(key, "dimen", "android");
            if (resourceId > 0) {
                int sizeOne = context.getResources().getDimensionPixelSize(resourceId);
                //从系统的配置来获取总是会返回导航栏高度，所以去掉
                //下面代码是适配屏幕缩放，从app这里获取出来的是app内缩放的尺寸
                float densityOne = context.getResources().getDisplayMetrics().density;
                float densityTwo = Resources.getSystem().getDisplayMetrics().density;
                float f = sizeOne * densityTwo / densityOne;
                return (int) ((f >= 0) ? (f + 0.5f) : (f - 0.5f));
            }
        } catch (Resources.NotFoundException ignored) {
            return 0;
        }
        return result;
    }

    @SuppressLint("NewApi")
    private float getSmallestWidthDp(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        } else {
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        }
        float widthDp = metrics.widthPixels / metrics.density;
        float heightDp = metrics.heightPixels / metrics.density;
        return Math.min(widthDp, heightDp);
    }

    /**
     * Should a navigation bar appear at the bottom of the screen in the current device
     * configuration? A navigation bar may appear on the right side of the screen in certain
     * configurations.
     *
     * @return True if navigation should appear at the bottom of the screen, False otherwise.
     */
    boolean isNavigationAtBottom() {
        return (mSmallestWidthDp >= 600 || mInPortrait);
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
     * @return The height of the navigation bar (in pixels). If the device does not have soft
     * navigation keys, this will always return 0.
     */
    int getNavigationBarHeight() {
        return mNavigationBarHeight;
    }

    /**
     * Get the width of the system navigation bar when it is placed vertically on the screen.
     *
     * @return The width of the navigation bar (in pixels). If the device does not have soft
     * navigation keys, this will always return 0.
     */
    int getNavigationBarWidth() {
        return mNavigationBarWidth;
    }
}