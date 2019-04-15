package com.gyf.immersionbar.simple.utils;

import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Window;

/**
 * @author geyifeng
 * @date 2019/4/14 4:59 PM
 */
public class Utils {
    public static Integer[] getWidthAndHeight(Window window) {
        Integer[] integer = new Integer[2];
        DisplayMetrics dm = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.getWindowManager().getDefaultDisplay().getRealMetrics(dm);
        } else {
            window.getWindowManager().getDefaultDisplay().getMetrics(dm);
        }
        integer[0] = dm.widthPixels;
        integer[1] = dm.heightPixels;
        return integer;
    }
}
