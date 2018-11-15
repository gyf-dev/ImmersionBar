package com.gyf.barlibrary;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.DisplayCutout;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 刘海屏判断
 * The type Notch utils.
 *
 * @author geyifeng
 * @date 2018 /11/14 12:09 AM
 */
public class NotchUtils {

    /**
     * 系统属性
     * The constant SYSTEM_PROPERTIES.
     */
    private static final String SYSTEM_PROPERTIES = "android.os.SystemProperties";
    /**
     * 小米刘海
     * The constant NOTCH_XIAO_MI.
     */
    private static final String NOTCH_XIAO_MI = "ro.miui.notch";
    /**
     * 华为刘海
     * The constant NOTCH_HUA_WEI.
     */
    private static final String NOTCH_HUA_WEI = "com.huawei.android.util.HwNotchSizeUtil";
    /**
     * VIVO刘海
     * The constant NOTCH_VIVO.
     */
    private static final String NOTCH_VIVO = "android.util.FtFeature";
    /**
     * OPPO刘海
     * The constant NOTCH_OPPO.
     */
    private static final String NOTCH_OPPO = "com.oppo.feature.screen.heteromorphism";


    /**
     * 判断是否是刘海屏
     * Has notch screen boolean.
     *
     * @param activity the activity
     * @return the boolean
     */
    public static boolean hasNotchScreen(Activity activity) {
        return activity != null && (hasNotchAtXiaoMi(activity) ||
                hasNotchAtHuaWei(activity) ||
                hasNotchAtOPPO(activity) ||
                hasNotchAtVIVO(activity) ||
                hasNotchAtAndroidP(activity));
    }

    /**
     * 判断是否是刘海屏
     * Has notch screen boolean.
     *
     * @param view the view
     * @return the boolean
     */
    public static boolean hasNotchScreen(View view) {
        return view != null && (hasNotchAtXiaoMi(view.getContext()) ||
                hasNotchAtHuaWei(view.getContext()) ||
                hasNotchAtOPPO(view.getContext()) ||
                hasNotchAtVIVO(view.getContext()) ||
                hasNotchAtAndroidP(view));
    }

    /**
     * Has notch at android p boolean.
     *
     * @param view the view
     * @return the boolean
     */
    private static boolean hasNotchAtAndroidP(View view) {
        return getDisplayCutout(view) != null;
    }

    /**
     * Android P 刘海屏判断
     * Has notch at android p boolean.
     *
     * @param activity the activity
     * @return the boolean
     */
    private static boolean hasNotchAtAndroidP(Activity activity) {
        return getDisplayCutout(activity) != null;
    }


    /**
     * 获得DisplayCutout
     * Gets display cutout.
     *
     * @param activity the activity
     * @return the display cutout
     */
    private static DisplayCutout getDisplayCutout(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if (activity != null) {
                Window window = activity.getWindow();
                if (window != null) {
                    WindowInsets windowInsets = window.getDecorView().getRootWindowInsets();
                    if (windowInsets != null) {
                        return windowInsets.getDisplayCutout();
                    }
                }
            }
        }
        return null;
    }

    private static DisplayCutout getDisplayCutout(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if (view != null) {
                WindowInsets windowInsets = view.getRootWindowInsets();
                if (windowInsets != null) {
                    return windowInsets.getDisplayCutout();
                }
            }
        }
        return null;
    }

    /**
     * 小米刘海屏判断.
     * Has notch at xiao mi int.
     *
     * @param context the context
     * @return the int
     */
    private static boolean hasNotchAtXiaoMi(Context context) {
        int result = 0;
        if ("Xiaomi".equals(Build.MANUFACTURER)) {
            try {
                ClassLoader classLoader = context.getClassLoader();
                @SuppressLint("PrivateApi")
                Class<?> aClass = classLoader.loadClass(SYSTEM_PROPERTIES);
                Method method = aClass.getMethod("getInt", String.class, int.class);
                result = (Integer) method.invoke(aClass, NOTCH_XIAO_MI, 0);

            } catch (NoSuchMethodException ignored) {
            } catch (IllegalAccessException ignored) {
            } catch (InvocationTargetException ignored) {
            } catch (ClassNotFoundException ignored) {
            }
        }
        return result == 1;
    }

    /**
     * 华为刘海屏判断
     * Has notch at hua wei boolean.
     *
     * @param context the context
     * @return the boolean
     */
    private static boolean hasNotchAtHuaWei(Context context) {
        boolean result = false;
        try {
            ClassLoader classLoader = context.getClassLoader();
            Class<?> aClass = classLoader.loadClass(NOTCH_HUA_WEI);
            Method get = aClass.getMethod("hasNotchInScreen");
            result = (boolean) get.invoke(aClass);
        } catch (ClassNotFoundException ignored) {
        } catch (NoSuchMethodException ignored) {
        } catch (Exception ignored) {
        }
        return result;
    }

    /**
     * VIVO刘海屏判断
     * Has notch at vivo boolean.
     *
     * @param context the context
     * @return the boolean
     */
    private static boolean hasNotchAtVIVO(Context context) {
        boolean result = false;
        try {
            ClassLoader classLoader = context.getClassLoader();
            @SuppressLint("PrivateApi")
            Class<?> aClass = classLoader.loadClass(NOTCH_VIVO);
            Method method = aClass.getMethod("isFeatureSupport", int.class);
            result = (boolean) method.invoke(aClass, 0x00000020);
        } catch (ClassNotFoundException ignored) {
        } catch (NoSuchMethodException ignored) {
        } catch (Exception ignored) {
        }
        return result;
    }

    /**
     * OPPO刘海屏判断
     * Has notch at oppo boolean.
     *
     * @param context the context
     * @return the boolean
     */
    private static boolean hasNotchAtOPPO(Context context) {
        return context.getPackageManager().hasSystemFeature(NOTCH_OPPO);
    }
}
