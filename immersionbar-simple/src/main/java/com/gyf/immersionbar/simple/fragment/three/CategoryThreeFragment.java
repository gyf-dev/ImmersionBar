package com.gyf.immersionbar.simple.fragment.three;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.gyf.immersionbar.simple.R;
import com.gyf.immersionbar.simple.adapter.NewsAdapter;
import com.gyf.immersionbar.simple.fragment.BaseFragment;

import butterknife.BindView;

/**
 * @author geyifeng
 * @date 2017/5/12
 */
public class CategoryThreeFragment extends BaseFragment {

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private String[] mTabName = {"关注", "头条", "视频", "娱乐", "体育", "新时代", "要闻", "知否", "段子", "本地", "公开课", "财经", "科技", "汽车"};

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_three_category;
    }

    @Override
    protected void initView() {
        super.initView();
        for (String s : mTabName) {
            tabLayout.addTab(tabLayout.newTab().setText(s));
        }
        viewPager.setAdapter(new NewsAdapter(getChildFragmentManager(), mTabName));
        tabLayout.setupWithViewPager(viewPager);
    }
}
