package com.gyf.immersionbar.sample.fragment.dialog;

import androidx.appcompat.widget.Toolbar;

import com.gyf.immersionbar.ImmersionBar;
import com.gyf.immersionbar.sample.R;

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
        mWindow.setWindowAnimations(R.style.RightAnimation);
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
}
