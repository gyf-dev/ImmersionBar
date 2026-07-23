package com.gyf.immersionbar;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.Configuration;
import androidx.fragment.app.Fragment;

/**
 * @author geyifeng
 * @date 2019/4/11 6:43 PM
 */
@SuppressWarnings("all")
public final class SupportRequestBarManagerFragment extends Fragment {

    private ImmersionDelegate mDelegate;

    public ImmersionBar get(Object o) {
        if (mDelegate == null) {
            mDelegate = new ImmersionDelegate(o);
            if (isResumed()) {
                mDelegate.onResume();
            }
        }
        return mDelegate.get();
    }

    public ImmersionBar get(Activity activity, Dialog dialog) {
        if (mDelegate == null) {
            mDelegate = new ImmersionDelegate(activity, dialog);
            if (isResumed()) {
                mDelegate.onResume();
            }
        }
        return mDelegate.get();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mDelegate != null) {
            mDelegate.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mDelegate != null) {
            mDelegate.onPause();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mDelegate != null) {
            mDelegate.onDestroy();
            mDelegate = null;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mDelegate != null) {
            mDelegate.onConfigurationChanged(newConfig);
        }
    }
}
