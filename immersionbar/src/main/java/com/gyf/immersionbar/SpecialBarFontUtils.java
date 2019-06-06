package com.gyf.immersionbar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Flyme OS 修改状态栏字体颜色工具类
 *
 * @author gyf
 * @date 2017 /05/30
 */
class SpecialBarFontUtils {
    private static Method mSetStatusBarColorIcon;
    private static Method mSetStatusBarDarkIcon;
    private static Field mStatusBarColorFiled;
    private static int SYSTEM_UI_FLAG_LIGHT_STATUS_BAR = 0;

    static {
        try {
            mSetStatusBarColorIcon = Activity.class.getMethod("setStatusBarDarkIcon", int.class);
        } catch (NoSuchMethodException ignored) {

        }
        try {
            mSetStatusBarDarkIcon = Activity.class.getMethod("setStatusBarDarkIcon", boolean.class);
        } catch (NoSuchMethodException ignored) {

        }
        try {
            mStatusBarColorFiled = WindowManager.LayoutParams.class.getField("statusBarColor");
        } catch (NoSuchFieldException ignored) {

        }
        try {
            Field field = View.class.getField("SYSTEM_UI_FLAG_LIGHT_STATUS_BAR");
            SYSTEM_UI_FLAG_LIGHT_STATUS_BAR = field.getInt(null);
        } catch (NoSuchFieldException ignored) {

        } catch (IllegalAccessException ignored) {

        }
    }

    /**
     * 判断颜色是否偏黑色
     *
     * @param color 颜色
     * @param level 级别
     * @return boolean boolean
     */
    static boolean isBlackColor(int color, int level) {
        int grey = toGrey(color);
        return grey < level;
    }

    /**
     * 颜色转换成灰度值
     *
     * @param rgb 颜色
     * @return the int
     * @return　灰度值
     */
    static int toGrey(int rgb) {
        int blue = rgb & 0x000000FF;
        int green = (rgb & 0x0000FF00) >> 8;
        int red = (rgb & 0x00FF0000) >> 16;
        return (red * 38 + green * 75 + blue * 15) >> 7;
    }

    /**
     * 设置状态栏字体图标颜色
     *
     * @param activity 当前activity
     * @param color    颜色
     */
    public static void setStatusBarDarkIcon(Activity activity, int color) {
        if (mSetStatusBarColorIcon != null) {
            try {
                mSetStatusBarColorIcon.invoke(activity, color);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } else {
            boolean whiteColor = isBlackColor(color, 50);
            if (mStatusBarColorFiled != null) {
                setStatusBarDarkIcon(activity, whiteColor, whiteColor);
                setStatusBarDarkIcon(activity.getWindow(), color);
            } else {
                setStatusBarDarkIcon(activity, whiteColor);
            }
        }
    }

    /**
     * 设置状态栏字体图标颜色(只限全屏非activity情况)
     *
     * @param window 当前窗口
     * @param color  颜色
     */
    public static void setStatusBarDarkIcon(Window window, int color) {
        try {
            setStatusBarColor(window, color);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                setStatusBarDarkIcon(window.getDecorView(), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置状态栏字体图标颜色
     *
     * @param activity 当前activity
     * @param dark     是否深色 true为深色 false 为白色
     */
    public static void setStatusBarDarkIcon(Activity activity, boolean dark) {
        setStatusBarDarkIcon(activity, dark, true);
    }

    private static boolean changeMeizuFlag(WindowManager.LayoutParams winParams, String flagName, boolean on) {
        try {
            Field f = winParams.getClass().getDeclaredField(flagName);
            f.setAccessible(true);
            int bits = f.getInt(winParams);
            Field f2 = winParams.getClass().getDeclaredField("meizuFlags");
            f2.setAccessible(true);
            int meizuFlags = f2.getInt(winParams);
            int oldFlags = meizuFlags;
            if (on) {
                meizuFlags |= bits;
            } else {
                meizuFlags &= ~bits;
            }
            if (oldFlags != meizuFlags) {
                f2.setInt(winParams, meizuFlags);
                return true;
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 设置状态栏颜色
     *
     * @param view
     * @param dark
     */
    private static void setStatusBarDarkIcon(View view, boolean dark) {
        int oldVis = view.getSystemUiVisibility();
        int newVis = oldVis;
        if (dark) {
            newVis |= SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        } else {
            newVis &= ~SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        }
        if (newVis != oldVis) {
            view.setSystemUiVisibility(newVis);
        }
    }

    /**
     * 设置状态栏颜色
     *
     * @param window
     * @param color
     */
    private static void setStatusBarColor(Window window, int color) {
        WindowManager.LayoutParams winParams = window.getAttributes();
        if (mStatusBarColorFiled != null) {
            try {
                int oldColor = mStatusBarColorFiled.getInt(winParams);
                if (oldColor != color) {
                    mStatusBarColorFiled.set(winParams, color);
                    window.setAttributes(winParams);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置状态栏字体图标颜色(只限全屏非activity情况)
     *
     * @param window 当前窗口
     * @param dark   是否深色 true为深色 false 为白色
     */
    public static void setStatusBarDarkIcon(Window window, boolean dark) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            changeMeizuFlag(window.getAttributes(), "MEIZU_FLAG_DARK_STATUS_BAR_ICON", dark);
        } else {
            View decorView = window.getDecorView();
            setStatusBarDarkIcon(decorView, dark);
            setStatusBarColor(window, 0);
        }
    }

    private static void setStatusBarDarkIcon(Activity activity, boolean dark, boolean flag) {
        if (mSetStatusBarDarkIcon != null) {
            try {
                mSetStatusBarDarkIcon.invoke(activity, dark);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } else {
            if (flag) {
                setStatusBarDarkIcon(activity.getWindow(), dark);
            }
        }
    }

    @SuppressLint("PrivateApi")
    static void setMIUIBarDark(Window window, String key, boolean dark) {
        if (window != null) {
            Class<? extends Window> clazz = window.getClass();
            try {
                int darkModeFlag;
                Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField(key);
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    //状态栏透明且黑色字体
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);
                } else {
                    //清除黑色字体
                    extraFlagField.invoke(window, 0, darkModeFlag);
                }
            } catch (Exception ignored) {

            }
        }
    }
}
