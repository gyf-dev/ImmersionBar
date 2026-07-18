package com.gyf.immersionbar;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.Configuration;
import android.os.Build;
import android.view.View;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

/**
 * @author geyifeng
 * @date 2019/4/12 4:01 PM
 */
class ImmersionDelegate implements Runnable {

    private ImmersionBar mImmersionBar;
    private BarProperties mLastBarProperties;

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
        if (mImmersionBar != null) {
            mImmersionBar.setImmersionDelegate(this);
        }
    }

    ImmersionDelegate(Activity activity, Dialog dialog) {
        if (mImmersionBar == null) {
            mImmersionBar = new ImmersionBar(activity, dialog);
        }
        //回传delegate引用，使运行时系统栏显隐变化能反向触发BarProperties快照刷新
        mImmersionBar.setImmersionDelegate(this);
    }

    public ImmersionBar get() {
        return mImmersionBar;
    }

    void onActivityCreated(Configuration configuration) {
        refreshBarProperties();
    }

    void onResume() {
        if (mImmersionBar != null) {
            mImmersionBar.onResume();
        }
    }

    void onDestroy() {
        mLastBarProperties = null;
        if (mImmersionBar != null) {
            mImmersionBar.onDestroy();
            mImmersionBar = null;
        }
    }

    void onConfigurationChanged(Configuration newConfig) {
        if (mImmersionBar != null) {
            mImmersionBar.onConfigurationChanged(newConfig);
            refreshBarProperties();
        }
    }

    /**
     * 运行时系统栏显隐/导航模式变化时，投递{@link #run()}重建BarProperties快照并按去重规则回调监听器。
     *
     * <p>凡是会改变insets的运行时事件（hideBar/showBar、手势临时显隐、三键⇄手势切换）
     * 都能通过{@link #run()}重新读取完整快照，让BarProperties保持实时。
     *
     * <p>首次快照尚未建立（{@code mBarProperties == null}）时直接跳过，交由
     * {@link #onActivityCreated}统一构建。
     */
    void refreshBarProperties() {
        ImmersionBar immersionBar = mImmersionBar;
        if (immersionBar == null || !immersionBar.initialized() || Build.VERSION.SDK_INT < Version.KITKAT) {
            return;
        }
        View decorView = immersionBar.getWindow().getDecorView();
        decorView.post(this);
    }


    @SuppressWarnings("deprecation")
    @Override
    public void run() {
        ImmersionBar immersionBar = mImmersionBar;
        if (immersionBar == null || !immersionBar.initialized() || Build.VERSION.SDK_INT < Version.KITKAT) {
            return;
        }
        OnBarListener onBarListener = immersionBar.getBarParams().onBarListener;
        if (onBarListener == null && !immersionBar.hasOnBarPropertiesChangedListeners()) {
            return;
        }
        BarProperties barProperties = BarPropertiesUtils.getBarProperties(immersionBar.getWindow());
        if (mLastBarProperties == null || !mLastBarProperties.equals(barProperties)) {
            mLastBarProperties = new BarProperties(barProperties);
            if (onBarListener != null) {
                onBarListener.onBarChange(barProperties);
            }
            immersionBar.dispatchOnBarPropertiesChanged(barProperties);
        }
    }
}
