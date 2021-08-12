package com.gyf.immersionbar;

import android.app.Application;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;

import java.util.ArrayList;

import static com.gyf.immersionbar.Constants.IMMERSION_NAVIGATION_BAR_MODE_EMUI;

/**
 * 华为Emui3状态栏监听器
 *
 * @author geyifeng
 * @date 2019/4/10 6:02 PM
 */
final class EMUI3NavigationBarObserver extends ContentObserver {

    private ArrayList<ImmersionCallback> mCallbacks;
    private Application mApplication;
    private Boolean mIsRegister = false;

    static EMUI3NavigationBarObserver getInstance() {
        return NavigationBarObserverInstance.INSTANCE;
    }

    private EMUI3NavigationBarObserver() {
        super(new Handler(Looper.getMainLooper()));
    }

    void register(Application application) {
        this.mApplication = application;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && mApplication != null
                && mApplication.getContentResolver() != null && !mIsRegister) {
            Uri uri = Settings.System.getUriFor(IMMERSION_NAVIGATION_BAR_MODE_EMUI);
            if (uri != null) {
                mApplication.getContentResolver().registerContentObserver(uri, true, this);
                mIsRegister = true;
            }
        }
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && mApplication != null && mApplication.getContentResolver() != null
                && mCallbacks != null && !mCallbacks.isEmpty()) {
            int type = Settings.System.getInt(mApplication.getContentResolver(), IMMERSION_NAVIGATION_BAR_MODE_EMUI, 0);
            NavigationBarType navigationBarType = NavigationBarType.CLASSIC;
            if (type == 1) {
                navigationBarType = NavigationBarType.GESTURES;
            }
            for (ImmersionCallback callback : mCallbacks) {
                callback.onNavigationBarChange(type == 0, navigationBarType);
            }
        }
    }

    void addOnNavigationBarListener(ImmersionCallback callback) {
        if (callback == null) {
            return;
        }
        if (mCallbacks == null) {
            mCallbacks = new ArrayList<>();
        }
        if (!mCallbacks.contains(callback)) {
            mCallbacks.add(callback);
        }
    }

    void removeOnNavigationBarListener(ImmersionCallback callback) {
        if (callback == null || mCallbacks == null) {
            return;
        }
        mCallbacks.remove(callback);
    }

    private static class NavigationBarObserverInstance {
        private static final EMUI3NavigationBarObserver INSTANCE = new EMUI3NavigationBarObserver();
    }

}
