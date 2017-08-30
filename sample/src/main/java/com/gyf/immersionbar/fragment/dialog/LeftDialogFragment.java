package com.gyf.immersionbar.fragment.dialog;

import android.support.v7.widget.Toolbar;
import android.view.Gravity;

import com.gyf.immersionbar.R;
import com.gyf.immersionbar.activity.DialogActivity;

import butterknife.BindView;

/**
 * 左边DialogFragment
 * Created by geyifeng on 2017/7/28.
 */

public class LeftDialogFragment extends BaseDialogFragment {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    public void onStart() {
        super.onStart();
        mWindow.setGravity(Gravity.TOP | Gravity.START);
        mWindow.setWindowAnimations(R.style.LeftDialog);
        mWindow.setLayout(mWidth * 2 / 3, mHeight);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dialog;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(toolbar)
                .navigationBarColor(R.color.btn11)
                .keyboardEnable(true)
                .init();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        ((DialogActivity) getActivity()).getActivityImmersionBar().keyboardEnable(true).init();
    }
}
