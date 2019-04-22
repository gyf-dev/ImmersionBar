package com.gyf.immersionbar.simple.utils;

import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gyf.immersionbar.simple.R;

/**
 * @author geyifeng
 * @date 2019-04-20 02:00
 */
public class GlideUtils {
    public static void load(Object object, ImageView imageView) {
        load(object, imageView, R.mipmap.test);
    }

    public static void load(Object object, ImageView imageView, @DrawableRes int placeholder) {
        Glide.with(imageView).load(object).apply(new RequestOptions().placeholder(placeholder)).into(imageView);
    }
}
