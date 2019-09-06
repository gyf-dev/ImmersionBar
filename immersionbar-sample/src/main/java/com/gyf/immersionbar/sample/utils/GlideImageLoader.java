package com.gyf.immersionbar.sample.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gyf.immersionbar.sample.R;
import com.youth.banner.loader.ImageLoader;

/**
 * @author geyifeng
 * @date 2017/6/4
 */
public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        Glide.with(context)
                .load(path)
                .apply(new RequestOptions().placeholder(R.mipmap.test))
                .into(imageView);
    }

}
