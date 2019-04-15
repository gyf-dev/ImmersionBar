package com.gyf.immersionbar;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.Configuration;
import android.os.Build;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

/**
 * @author geyifeng
 * @date 2019/4/12 4:01 PM
 */
class ImmersionDelegate {

    private ImmersionBar mImmersionBar;

    ImmersionDelegate(Object o) {
        if (o instanceof Activity) {
            if (mImmersionBar == null) {
                mImmersionBar = new ImmersionBar((Activity) o);
            }
        } else if (o instanceof Fragment) {
            if (mImmersionBar == null) {
                if (o instanceof DialogFragment) {
                    mImmersionBar = new ImmersionBar((DialogFragment) o);
                } else {
                    mImmersionBar = new ImmersionBar((Fragment) o);
                }
            }
        } else if (o instanceof android.app.Fragment) {
            if (mImmersionBar == null) {
                if (o instanceof android.app.DialogFragment) {
                    mImmersionBar = new ImmersionBar((android.app.DialogFragment) o);
                } else {
                    mImmersionBar = new ImmersionBar((android.app.Fragment) o);
                }
            }
        }
    }

    ImmersionDelegate(Activity activity, Dialog dialog) {
        if (mImmersionBar == null) {
            mImmersionBar = new ImmersionBar(activity, dialog);
        }
    }

    public ImmersionBar get() {
        return mImmersionBar;
    }

    void onResume() {
        if (mImmersionBar != null && OSUtils.isEMUI3_x()) {
            if (mImmersionBar.initialized() && !mImmersionBar.isFragment() && mImmersionBar.getBarParams().navigationBarWithEMUI3Enable) {
                mImmersionBar.init();
            }
        }
    }

    void onDestroy() {
        if (mImmersionBar != null) {
            mImmersionBar.destroy();
            mImmersionBar = null;
        }
    }

    void onConfigurationChanged(Configuration newConfig) {
        if (mImmersionBar != null) {
            if (OSUtils.isEMUI3_x() || Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
                if (mImmersionBar.initialized() && !mImmersionBar.isFragment() && mImmersionBar.getBarParams().navigationBarWithKitkatEnable) {
                    mImmersionBar.init();
                }
            }
        }
    }
}
