package com.gyf.immersionbar.sample.fragment.four;

import android.widget.ImageView;

import android.view.View;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gyf.immersionbar.sample.R;
import com.gyf.immersionbar.sample.databinding.FragmentTwoHomeBinding;
import com.gyf.immersionbar.sample.fragment.BaseFragment;
import com.gyf.immersionbar.sample.utils.Utils;


/**
 * @author geyifeng
 * @date 2017/7/20
 */
public class HomeFourFragment extends BaseFragment {

    private FragmentTwoHomeBinding binding;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_two_home;
    }

    @Override
    protected void initView() {
        Glide.with(this).asBitmap().load(Utils.getPic())
                .apply(new RequestOptions().placeholder(R.mipmap.test))
                .into(binding.iv);
    }
    @Override
    protected void initViewBinding(View view) {
        binding = FragmentTwoHomeBinding.bind(view);
    }

}
