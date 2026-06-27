package com.gyf.immersionbar.sample.fragment.three;

import android.view.View;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;

import com.gyf.immersionbar.sample.R;
import com.gyf.immersionbar.sample.databinding.FragmentThreeCategoryBinding;
import com.gyf.immersionbar.sample.adapter.NewsAdapter;
import com.gyf.immersionbar.sample.fragment.BaseFragment;


/**
 * @author geyifeng
 * @date 2017/5/12
 */
public class CategoryThreeFragment extends BaseFragment {

    private FragmentThreeCategoryBinding binding;
    private String[] mTabName = {"关注", "头条", "视频", "娱乐", "体育", "新时代", "要闻", "知否", "段子", "本地", "公开课", "财经", "科技", "汽车"};

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_three_category;
    }

    @Override
    protected void initView() {
        super.initView();
        for (String s : mTabName) {
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText(s));
        }
        binding.viewPager.setAdapter(new NewsAdapter(getChildFragmentManager(), mTabName));
        binding.tabLayout.setupWithViewPager(binding.viewPager);
    }
    @Override
    protected void initViewBinding(View view) {
        binding = FragmentThreeCategoryBinding.bind(view);
    }

}
