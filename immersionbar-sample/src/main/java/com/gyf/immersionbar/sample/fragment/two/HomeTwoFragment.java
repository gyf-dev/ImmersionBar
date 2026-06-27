package com.gyf.immersionbar.sample.fragment.two;

import android.widget.ImageView;

import android.view.View;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gyf.immersionbar.ImmersionBar;
import com.gyf.immersionbar.sample.R;
import com.gyf.immersionbar.sample.databinding.FragmentTwoHomeBinding;
import com.gyf.immersionbar.sample.fragment.BaseImmersionFragment;
import com.gyf.immersionbar.sample.utils.Utils;


/**
 * @author geyifeng
 * @date 2017/7/20
 */

public class HomeTwoFragment extends BaseImmersionFragment {

    private FragmentTwoHomeBinding binding;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_two_home;
    }

    @Override
    public void initImmersionBar() {
        super.initImmersionBar();
        ImmersionBar.with(this)
                .navigationBarColor(R.color.colorPrimary)
                .keyboardEnable(false)
                .init();
    }

    @Override
    protected void initView() {
        super.initView();
        Glide.with(this).asBitmap().load(Utils.getPic())
                .apply(new RequestOptions().placeholder(R.mipmap.test))
                .into(binding.iv);
    }
    @Override
    protected void initViewBinding(View view) {
        binding = FragmentTwoHomeBinding.bind(view);
    }

}
