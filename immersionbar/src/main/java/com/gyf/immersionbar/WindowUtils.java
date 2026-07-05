package com.gyf.immersionbar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Window工具类
 *
 * @author gyf
 */
class WindowUtils {

    private WindowUtils() {
    }

    /**
     * 获取Window。
     *
     * @param activity the activity
     * @return the window
     */
    @NonNull
    public static Window getWindow(@NonNull Activity activity) {
        return activity.getWindow();
    }

    /**
     * 获取Window。
     *
     * @param dialog the dialog
     * @return the window
     */
    @Nullable
    public static Window getWindow(@NonNull Dialog dialog) {
        Window window = dialog.getWindow();
        if (window != null) {
            return window;
        }
        Activity activity = ActivityUtils.getActivity(dialog);
        if (activity == null) {
            return null;
        }
        return getWindow(activity);
    }

    /**
     * 获取Window。
     *
     * @param fragment the fragment
     * @return the window
     */
    @Nullable
    public static Window getWindow(@NonNull Fragment fragment) {
        Activity activity = fragment.getActivity();
        if (activity == null) {
            return null;
        }
        return getWindow(activity);
    }

    /**
     * 获取Window。
     *
     * @param fragment the fragment
     * @return the window
     */
    @Nullable
    public static Window getWindow(@NonNull android.app.Fragment fragment) {
        Activity activity = fragment.getActivity();
        if (activity == null) {
            return null;
        }
        return getWindow(activity);
    }

    /**
     * 获取Window。
     *
     * @param view the view
     * @return the window
     */
    @Nullable
    public static Window getWindow(@NonNull View view) {
        return getWindow(view.getContext());
    }

    /**
     * 获取Window。
     *
     * @param context the context
     * @return the window
     */
    @Nullable
    public static Window getWindow(@NonNull Context context) {
        Activity activity = ActivityUtils.getActivity(context);
        if (activity == null) {
            return null;
        }
        return getWindow(activity);
    }
}
