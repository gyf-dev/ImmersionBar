package com.gyf.immersionbar;

import android.app.Application;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackManager;

/**
 * Created by geyifeng on 2017/5/11.
 */

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        BGASwipeBackManager.getInstance().init(this);
    }
}
