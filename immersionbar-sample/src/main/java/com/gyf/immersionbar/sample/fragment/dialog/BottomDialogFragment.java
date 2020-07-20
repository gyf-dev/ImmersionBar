package com.gyf.immersionbar.sample.fragment.dialog;

import android.content.res.Configuration;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;

import com.gyf.immersionbar.ImmersionBar;
import com.gyf.immersionbar.sample.R;

/**
 * 底部DialogFragment
 *
 * @author geyifeng
 * @date 2017/7/28
 */
public class BottomDialogFragment extends BaseDialogFragment {
    @Override
    public void onStart() {
        super.onStart();
        mWindow.setGravity(Gravity.BOTTOM);
        mWindow.setWindowAnimations(R.style.BottomAnimation);
        mWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, mWidthAndHeight[1] / 2);
        Log.e(TAG, mWidthAndHeight[0] + ":" + mWidthAndHeight[1]);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dialog;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        ImmersionBar.with(this)
                .transparentStatusBar()
                .transparentNavigationBar()
                .navigationBarDarkIcon(true)
                .navigationBarColor(R.color.cool_green_normal)
                .init();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, mWidthAndHeight[1] / 2);
    }
}
