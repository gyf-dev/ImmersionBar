package com.gyf.immersionbar;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;

import static com.gyf.immersionbar.Constants.IMMERSION_NAVIGATION_BAR_MODE_DEFAULT;
import static com.gyf.immersionbar.Constants.IMMERSION_NAVIGATION_BAR_MODE_EMUI;
import static com.gyf.immersionbar.Constants.IMMERSION_NAVIGATION_BAR_MODE_MIUI;
import static com.gyf.immersionbar.Constants.IMMERSION_NAVIGATION_BAR_MODE_OPPO;
import static com.gyf.immersionbar.Constants.IMMERSION_NAVIGATION_BAR_MODE_SAMSUNG;
import static com.gyf.immersionbar.Constants.IMMERSION_NAVIGATION_BAR_MODE_VIVO;

/**
 * 手势utils
 *
 * @author: ifan
 * @date: 8/13/21
 */
class GestureUtils {

    /**
     * 获取全面屏相关信息
     *
     * @param context Context
     * @return FullScreenBean
     */
    public static GestureBean getGestureBean(Context context) {
        GestureBean gestureBean = new GestureBean();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && context != null && context.getContentResolver() != null) {
            ContentResolver contentResolver = context.getContentResolver();
            NavigationBarType navigationBarType = NavigationBarType.UNKNOWN;
            int type;
            boolean isGesture = false;
            if (OSUtils.isHuaWei() || OSUtils.isEMUI()) {
                if (OSUtils.isEMUI3_x() || Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    type = Settings.System.getInt(contentResolver, IMMERSION_NAVIGATION_BAR_MODE_EMUI, 0);
                } else {
                    type = Settings.Global.getInt(contentResolver, IMMERSION_NAVIGATION_BAR_MODE_EMUI, 0);
                }
                if (type == 0) {
                    navigationBarType = NavigationBarType.CLASSIC;
                    isGesture = false;
                } else if (type == 1) {
                    navigationBarType = NavigationBarType.GESTURES;
                    isGesture = true;
                }
            } else if (OSUtils.isXiaoMi() || OSUtils.isMIUI()) {
                type = Settings.Global.getInt(contentResolver, IMMERSION_NAVIGATION_BAR_MODE_MIUI, 0);
                if (type == 0) {
                    navigationBarType = NavigationBarType.CLASSIC;
                    isGesture = false;
                } else if (type == 1) {
                    navigationBarType = NavigationBarType.GESTURES;
                    isGesture = true;
                }
            } else if (OSUtils.isVivo() || OSUtils.isFuntouchOrOriginOs()) {
                type = Settings.Secure.getInt(contentResolver, IMMERSION_NAVIGATION_BAR_MODE_VIVO, 0);
                if (type == 0) {
                    navigationBarType = NavigationBarType.CLASSIC;
                    isGesture = false;
                } else if (type == 1) {
                    navigationBarType = NavigationBarType.SMALL;
                    isGesture = true;
                } else if (type == 2) {
                    navigationBarType = NavigationBarType.GESTURES;
                    isGesture = true;
                }
            } else if (OSUtils.isOppo() || OSUtils.isColorOs()) {
                type = Settings.Secure.getInt(contentResolver, IMMERSION_NAVIGATION_BAR_MODE_OPPO, 0);
                if (type == 0) {
                    navigationBarType = NavigationBarType.CLASSIC;
                    isGesture = false;
                } else if (type == 1 || type == 2 || type == 3) {
                    navigationBarType = NavigationBarType.GESTURES;
                    isGesture = true;
                }
            } else if (OSUtils.isSamsung()) {
                type = Settings.Global.getInt(contentResolver, IMMERSION_NAVIGATION_BAR_MODE_SAMSUNG, 0);
                if (type == 0) {
                    navigationBarType = NavigationBarType.CLASSIC;
                    isGesture = false;
                } else if (type == 1) {
                    navigationBarType = NavigationBarType.GESTURES;
                    isGesture = true;
                }
            } else {
                type = Settings.Secure.getInt(contentResolver, IMMERSION_NAVIGATION_BAR_MODE_DEFAULT, 0);
                if (type == 0) {
                    navigationBarType = NavigationBarType.CLASSIC;
                    isGesture = false;
                } else if (type == 1) {
                    navigationBarType = NavigationBarType.DOUBLE;
                    isGesture = false;
                } else if (type == 2) {
                    navigationBarType = NavigationBarType.GESTURES;
                    isGesture = true;
                }
            }
            gestureBean.isGesture = isGesture;
            gestureBean.type = navigationBarType;
        }
        return gestureBean;
    }

    static class GestureBean {
        /**
         * 是否有手势操作
         */
        public boolean isGesture = false;
        /**
         * 手势类型
         */
        public NavigationBarType type;
    }
}
