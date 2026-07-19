package com.gyf.immersionbar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.view.Surface;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/**
 * BarProperties工具类
 *
 * @author gyf
 */
class BarPropertiesUtils {

    private BarPropertiesUtils() {
    }

    /**
     * 获取当前BarProperties快照。
     *
     * @param activity the activity
     * @return BarProperties快照
     */
    @NonNull
    public static BarProperties getBarProperties(@NonNull Activity activity) {
        return getBarProperties(WindowUtils.getWindow(activity));
    }

    /**
     * 获取当前BarProperties快照。
     *
     * @param fragment the fragment
     * @return BarProperties快照
     */
    @NonNull
    public static BarProperties getBarProperties(@NonNull Fragment fragment) {
        Window window = WindowUtils.getWindow(fragment);
        if (window == null) {
            return new BarProperties();
        }
        return getBarProperties(window);
    }

    /**
     * 获取当前BarProperties快照。
     *
     * @param fragment the fragment
     * @return BarProperties快照
     */
    @NonNull
    public static BarProperties getBarProperties(@NonNull android.app.Fragment fragment) {
        Window window = WindowUtils.getWindow(fragment);
        if (window == null) {
            return new BarProperties();
        }
        return getBarProperties(window);
    }

    /**
     * 获取当前BarProperties快照。
     *
     * @param dialog the dialog
     * @return BarProperties快照
     */
    @NonNull
    public static BarProperties getBarProperties(@NonNull Dialog dialog) {
        Window window = WindowUtils.getWindow(dialog);
        if (window == null) {
            return new BarProperties();
        }
        return getBarProperties(window);
    }

    /**
     * 获取当前BarProperties快照。
     *
     * @param view the view
     * @return BarProperties快照
     */
    @NonNull
    public static BarProperties getBarProperties(@NonNull View view) {
        Window window = WindowUtils.getWindow(view);
        if (window == null) {
            return new BarProperties();
        }
        return getBarProperties(window);
    }

    /**
     * 获取当前BarProperties快照。
     *
     * @param context the context
     * @return BarProperties快照
     */
    @NonNull
    public static BarProperties getBarProperties(@NonNull Context context) {
        Window window = WindowUtils.getWindow(context);
        if (window == null) {
            return new BarProperties();
        }
        return getBarProperties(window);
    }

    /**
     * 获取当前BarProperties快照。
     *
     * @param window the window
     * @return BarProperties快照
     */
    @NonNull
    public static BarProperties getBarProperties(@NonNull Window window) {
        BarProperties barProperties = new BarProperties();
        Context context = window.getContext();
        Configuration configuration = context.getResources().getConfiguration();
        barProperties.setPortrait(configuration.orientation == Configuration.ORIENTATION_PORTRAIT);
        int rotation = window.getWindowManager().getDefaultDisplay().getRotation();
        if (rotation == Surface.ROTATION_90) {
            barProperties.setLandscapeLeft(true);
            barProperties.setLandscapeRight(false);
        } else if (rotation == Surface.ROTATION_270) {
            barProperties.setLandscapeLeft(false);
            barProperties.setLandscapeRight(true);
        } else {
            barProperties.setLandscapeLeft(false);
            barProperties.setLandscapeRight(false);
        }
        BarConfig barConfig = new BarConfig(window);
        barProperties.setStatusBarHeight(barConfig.getStatusBarHeight());
        barProperties.setStatusBarVisible(barConfig.isStatusBarVisible());
        barProperties.setNavigationBar(barConfig.hasNavigationBar());
        barProperties.setNavigationAtBottom(barConfig.isNavigationAtBottom());
        GestureUtils.GestureBean gestureBean = GestureUtils.getGestureBean(context);
        barProperties.setNavigationBarType(gestureBean.type);
        barProperties.setGestureNavigation(gestureBean.isGesture);
        barProperties.setNavigationBarHeight(barConfig.getNavigationBarHeight());
        barProperties.setNavigationBarHeightIgnoringVisibility(barConfig.getNavigationBarHeightIgnoringVisibility());
        barProperties.setNavigationBarVisible(barConfig.isNavigationBarVisible());
        barProperties.setNavigationBarWidth(barConfig.getNavigationBarWidth());
        barProperties.setActionBarHeight(barConfig.getActionBarHeight());
        boolean notchScreen = NotchUtils.hasNotchScreen(window);
        barProperties.setNotchScreen(notchScreen);
        if (notchScreen) {
            barProperties.setNotchHeight(NotchUtils.getNotchHeight(window));
        }
        return barProperties;
    }
}
