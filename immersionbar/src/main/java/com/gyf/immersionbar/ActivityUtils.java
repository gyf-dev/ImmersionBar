package com.gyf.immersionbar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Activity工具类
 *
 * @author gyf
 */
class ActivityUtils {

    private ActivityUtils() {
    }

    /**
     * 获取Activity。
     *
     * @param dialog the dialog
     * @return the activity
     */
    @Nullable
    public static Activity getActivity(@NonNull Dialog dialog) {
        return getActivity(dialog.getContext());
    }

    /**
     * 获取Activity。
     *
     * @param window the window
     * @return the activity
     */
    @Nullable
    public static Activity getActivity(@NonNull Window window) {
        return getActivity(window.getContext());
    }

    /**
     * 获取Activity。
     *
     * @param context the context
     * @return the activity
     */
    @Nullable
    public static Activity getActivity(@NonNull Context context) {
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }
}
