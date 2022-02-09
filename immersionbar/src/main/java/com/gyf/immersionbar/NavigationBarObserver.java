package com.gyf.immersionbar;

import static com.gyf.immersionbar.Constants.IMMERSION_NAVIGATION_BAR_MODE_DEFAULT;
import static com.gyf.immersionbar.Constants.IMMERSION_NAVIGATION_BAR_MODE_EMUI;
import static com.gyf.immersionbar.Constants.IMMERSION_NAVIGATION_BAR_MODE_MIUI;
import static com.gyf.immersionbar.Constants.IMMERSION_NAVIGATION_BAR_MODE_MIUI_HIDE;
import static com.gyf.immersionbar.Constants.IMMERSION_NAVIGATION_BAR_MODE_OPPO;
import static com.gyf.immersionbar.Constants.IMMERSION_NAVIGATION_BAR_MODE_SAMSUNG;
import static com.gyf.immersionbar.Constants.IMMERSION_NAVIGATION_BAR_MODE_SAMSUNG_GESTURE;
import static com.gyf.immersionbar.Constants.IMMERSION_NAVIGATION_BAR_MODE_SAMSUNG_GESTURE_TYPE;
import static com.gyf.immersionbar.Constants.IMMERSION_NAVIGATION_BAR_MODE_SAMSUNG_OLD;
import static com.gyf.immersionbar.Constants.IMMERSION_NAVIGATION_BAR_MODE_VIVO;

import android.app.Application;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;

import java.util.ArrayList;

/**
 * 导航栏显示隐藏处理，目前支持华为、小米、VOVO、Android 10带有导航栏的手机
 *
 * @author geyifeng
 * @date 2019/4/10 6:02 PM
 */
final class NavigationBarObserver extends ContentObserver {

    private ArrayList<OnNavigationBarListener> mListeners;
    private Application mApplication;
    private boolean mIsRegister = false;

    static NavigationBarObserver getInstance() {
        return NavigationBarObserverInstance.INSTANCE;
    }

    private NavigationBarObserver() {
        super(new Handler(Looper.getMainLooper()));
    }

    void register(Application application) {
        this.mApplication = application;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && mApplication != null
                && mApplication.getContentResolver() != null && !mIsRegister) {
            Uri uri;
            Uri uri1 = null;
            Uri uri2 = null;
            if (OSUtils.isHuaWei() || OSUtils.isEMUI()) {
                if (OSUtils.isEMUI3_x() || Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    uri = Settings.System.getUriFor(IMMERSION_NAVIGATION_BAR_MODE_EMUI);
                } else {
                    uri = Settings.Global.getUriFor(IMMERSION_NAVIGATION_BAR_MODE_EMUI);
                }
            } else if (OSUtils.isXiaoMi() || OSUtils.isMIUI()) {
                uri = Settings.Global.getUriFor(IMMERSION_NAVIGATION_BAR_MODE_MIUI);
                uri1 = Settings.Global.getUriFor(IMMERSION_NAVIGATION_BAR_MODE_MIUI_HIDE);
            } else if (OSUtils.isVivo() || OSUtils.isFuntouchOrOriginOs()) {
                uri = Settings.Secure.getUriFor(IMMERSION_NAVIGATION_BAR_MODE_VIVO);
            } else if (OSUtils.isOppo() || OSUtils.isColorOs()) {
                uri = Settings.Secure.getUriFor(IMMERSION_NAVIGATION_BAR_MODE_OPPO);
            } else if (OSUtils.isSamsung()) {
                int i = Settings.Global.getInt(mApplication.getContentResolver(), IMMERSION_NAVIGATION_BAR_MODE_SAMSUNG_OLD, -1);
                if (i == -1) {
                    uri = Settings.Global.getUriFor(IMMERSION_NAVIGATION_BAR_MODE_SAMSUNG);
                    uri1 = Settings.Global.getUriFor(IMMERSION_NAVIGATION_BAR_MODE_SAMSUNG_GESTURE_TYPE);
                    uri2 = Settings.Global.getUriFor(IMMERSION_NAVIGATION_BAR_MODE_SAMSUNG_GESTURE);
                } else {
                    uri = Settings.Global.getUriFor(IMMERSION_NAVIGATION_BAR_MODE_SAMSUNG_OLD);
                }
            } else {
                uri = Settings.Secure.getUriFor(IMMERSION_NAVIGATION_BAR_MODE_DEFAULT);
            }
            if (uri != null) {
                mApplication.getContentResolver().registerContentObserver(uri, true, this);
                mIsRegister = true;
            }
            if (uri1 != null) {
                mApplication.getContentResolver().registerContentObserver(uri1, true, this);
            }
            if (uri2 != null) {
                mApplication.getContentResolver().registerContentObserver(uri2, true, this);
            }
        }
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        if (mListeners != null && !mListeners.isEmpty()) {
            GestureUtils.GestureBean bean = GestureUtils.getGestureBean(mApplication);
            boolean show;
            if (bean.isGesture) {
                if (bean.checkNavigation) {
                    int navigationBarHeight = BarConfig.getNavigationBarHeightInternal(mApplication);
                    show = navigationBarHeight > 0;
                } else {
                    show = false;
                }
            } else {
                show = true;
            }
            for (OnNavigationBarListener onNavigationBarListener : mListeners) {
                onNavigationBarListener.onNavigationBarChange(show, bean.type);
            }
        }
    }

    void addOnNavigationBarListener(OnNavigationBarListener listener) {
        if (listener == null) {
            return;
        }
        if (mListeners == null) {
            mListeners = new ArrayList<>();
        }
        if (!mListeners.contains(listener)) {
            mListeners.add(listener);
        }
    }

    void removeOnNavigationBarListener(OnNavigationBarListener listener) {
        if (listener == null || mListeners == null) {
            return;
        }
        mListeners.remove(listener);
    }

    private static class NavigationBarObserverInstance {
        private static final NavigationBarObserver INSTANCE = new NavigationBarObserver();
    }

}
