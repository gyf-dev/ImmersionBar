package com.gyf.immersionbar.fragment.dialog;

import android.support.v7.widget.Toolbar;

import com.gyf.barlibrary.ImmersionBar;
import com.gyf.immersionbar.R;
import com.gyf.immersionbar.activity.DialogActivity;

import butterknife.BindView;

/**
 * 全屏DialogFragment
 *
 * @author geyifeng
 * @date 2017/7/28
 */
public class FullDialogFragment extends BaseDialogFragment {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    public void onStart() {
        super.onStart();
        mWindow.setWindowAnimations(R.style.RightDialog);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dialog;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        ImmersionBar.with(this)
                .titleBar(toolbar)
                .statusBarDarkFont(true)
                .navigationBarColor(R.color.btn3)
                .keyboardEnable(true)
                .init();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((DialogActivity) getActivity()).getActivityImmersionBar().keyboardEnable(true).init();
    }
}
