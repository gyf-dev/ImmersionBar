package com.gyf.immersionbar.sample.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Window;

import com.gyf.immersionbar.sample.R;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author geyifeng
 * @date 2019/4/14 4:59 PM
 */
public class Utils {

    public static Integer[] getWidthAndHeight(Window window) {
        if (window == null) {
            return null;
        }
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
        return getPics(4);
    }

    public static ArrayList<String> getPics(int num) {
        ArrayList<String> pics = new ArrayList<>();
        Random random = new Random();

        do {
            String s = "http://106.14.135.179/ImmersionBar/" + random.nextInt(40) + ".jpg";
            if (!pics.contains(s)) {
                pics.add(s);
            }
        } while (pics.size() < num);
        return pics;
    }

    public static String getFullPic() {
        Random random = new Random();
        return "http://106.14.135.179/ImmersionBar/phone/" + random.nextInt(40) + ".jpeg";
    }

    public static int getFlowerIcon() {
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

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager manager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = manager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

}
