package com.gyf.immersionbar.sample.utils;

import android.content.Context;

/**
 * @author geyifeng
 * @date 2019/4/19 3:56 PM
 */
public class DensityUtil {
    public static int dip2px(Context c, float dpValue) {
        final float scale = c.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
