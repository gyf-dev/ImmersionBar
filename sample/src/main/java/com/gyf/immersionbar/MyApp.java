package com.gyf.immersionbar;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 *
 * @author geyifeng
 * @date 2017/8/4
 */
public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }
}
