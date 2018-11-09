package com.gyf.immersionbar.fragment.dialog;

import android.view.Gravity;

import com.gyf.barlibrary.ImmersionBar;
import com.gyf.immersionbar.R;
import com.gyf.immersionbar.activity.DialogActivity;

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
        ImmersionBar.with(this).navigationBarColor(R.color.cool_green_normal).init();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((DialogActivity) getActivity()).getActivityImmersionBar().keyboardEnable(true).init();
    }
}
