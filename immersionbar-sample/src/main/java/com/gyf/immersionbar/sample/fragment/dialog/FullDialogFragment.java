package com.gyf.immersionbar.sample.fragment.dialog;

import android.view.View;
import androidx.appcompat.widget.Toolbar;

import com.gyf.immersionbar.ImmersionBar;
import com.gyf.immersionbar.sample.R;
import com.gyf.immersionbar.sample.databinding.DialogBinding;


/**
 * 全屏DialogFragment
 *
 * @author geyifeng
 * @date 2017/7/28
 */
public class FullDialogFragment extends BaseDialogFragment {

    private DialogBinding binding;
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
                .titleBar(binding.toolbar)
                .statusBarDarkFont(true)
                .navigationBarColor(R.color.btn3)
                .keyboardEnable(true)
                .init();
    }
    @Override
    protected void initViewBinding(View view) {
        binding = DialogBinding.bind(view);
    }

}
