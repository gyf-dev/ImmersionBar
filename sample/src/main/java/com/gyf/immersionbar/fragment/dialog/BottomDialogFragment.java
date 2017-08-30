package com.gyf.immersionbar.fragment.dialog;

import android.view.Gravity;

import com.gyf.immersionbar.R;
import com.gyf.immersionbar.activity.DialogActivity;

/**
 * 底部DialogFragment
 * Created by geyifeng on 2017/7/28.
 */

public class BottomDialogFragment extends BaseDialogFragment {

    @Override
    public void onStart() {
        super.onStart();
        mWindow.setGravity(Gravity.BOTTOM);
        mWindow.setWindowAnimations(R.style.BottomDialog);
        mWindow.setLayout(mWidth, mHeight / 2);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dialog;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.navigationBarColor(R.color.cool_green_normal).init();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((DialogActivity) getActivity()).getActivityImmersionBar().keyboardEnable(true).init();
    }
}
