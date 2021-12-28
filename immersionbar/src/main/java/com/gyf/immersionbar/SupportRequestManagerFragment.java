package com.gyf.immersionbar;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * @author geyifeng
 * @date 2019/4/11 6:43 PM
 */
public final class SupportRequestManagerFragment extends Fragment {

    private ImmersionDelegate mDelegate;

    public ImmersionBar get(Object o) {
        if (mDelegate == null) {
            mDelegate = new ImmersionDelegate(o);
        }
        return mDelegate.get();
    }

    public ImmersionBar get(Activity activity, Dialog dialog) {
        if (mDelegate == null) {
            mDelegate = new ImmersionDelegate(activity, dialog);
        }
        return mDelegate.get();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mDelegate != null) {
            mDelegate.onActivityCreated(getResources().getConfiguration());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mDelegate != null) {
            mDelegate.onResume();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
