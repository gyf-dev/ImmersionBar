package com.gyf.immersionbar.simple;

import android.app.Application;

import com.tencent.bugly.crashreport.CrashReport;

/**
 * @author geyifeng
 * @date 2017/8/4
 */
public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CrashReport.initCrashReport(getApplicationContext(), "31a5f1f394", false);
    }
}
