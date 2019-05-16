package com.gyf.immersionbar.simple;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.gyf.immersionbar.simple.service.NetworkService;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * @author geyifeng
 * @date 2017/8/4
 */
public class MyApp extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        // bugly
        CrashReport.initCrashReport(getApplicationContext(), "31a5f1f394", false);
        // 网络监听服务
        NetworkService.enqueueWork(this);
    }

    public static Context getContext() {
        return mContext;
    }
}
