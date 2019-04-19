package com.gyf.immersionbar.simple.utils;

import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Window;

import com.gyf.immersionbar.simple.R;

import java.util.ArrayList;
import java.util.Random;

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


    public static String getPic() {
        Random random = new Random();
        return "http://106.14.135.179/ImmersionBar/" + random.nextInt(40) + ".jpg";
    }

    public static ArrayList<String> getPics() {
        ArrayList<String> pics = new ArrayList<>();
        Random random = new Random();

        do {
            String s = "http://106.14.135.179/ImmersionBar/" + random.nextInt(40) + ".jpg";
            if (!pics.contains(s)) {
                pics.add(s);
            }
        } while (pics.size() < 4);
        return pics;
    }

    public static String getPhonePic() {
        Random random = new Random();
        return "http://106.14.135.179/ImmersionBar/phone/" + random.nextInt(40) + ".jpeg";
    }

    public static int getFlower() {
        Random random = new Random();
        int i = random.nextInt(99);
        if (i < 33) {
            return R.mipmap.icon_flower_1;
        } else if (i < 66) {
            return R.mipmap.icon_flower_2;
        } else {
            return R.mipmap.icon_flower_3;
        }
    }

}
