package com.gyf.immersionbar.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import com.gyf.immersionbar.R;
import com.gyf.immersionbar.fragment.one.CategoryOneFragment;
import com.gyf.immersionbar.fragment.one.MineOneFragment;
import com.gyf.immersionbar.fragment.one.HomeOneFragment;
import com.gyf.immersionbar.fragment.one.ServiceOneFragment;
import com.gyf.immersionbar.view.CustomViewPager;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by geyifeng on 2017/5/8.
 */

public class FragmentOneActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    @BindView(R.id.viewPager)
    CustomViewPager viewPager;
    @BindView(R.id.ll_home)
    LinearLayout ll_home;
    @BindView(R.id.ll_category)
    LinearLayout ll_category;
    @BindView(R.id.ll_service)
    LinearLayout ll_service;
    @BindView(R.id.ll_mine)
    LinearLayout ll_mine;
    private ArrayList<Fragment> mFragments;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_fragment_one;
    }

    @Override
    protected void initData() {
        mFragments = new ArrayList<>();
        HomeOneFragment homeOneFragment = new HomeOneFragment();
        CategoryOneFragment categoryOneFragment = new CategoryOneFragment();
        ServiceOneFragment serviceOneFragment = new ServiceOneFragment();
        MineOneFragment mineOneFragment = new MineOneFragment();
        mFragments.add(homeOneFragment);
        mFragments.add(categoryOneFragment);
        mFragments.add(serviceOneFragment);
        mFragments.add(mineOneFragment);
    }

    @Override
    protected void initView() {
        viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(3);
        ll_home.setSelected(true);
    }

    @Override
    protected void setListener() {
        ll_home.setOnClickListener(this);
        ll_category.setOnClickListener(this);
        ll_service.setOnClickListener(this);
        ll_mine.setOnClickListener(this);
        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_home:
                viewPager.setCurrentItem(0);
                tabSelected(ll_home);
                break;
            case R.id.ll_category:
                viewPager.setCurrentItem(1);
                tabSelected(ll_category);
                break;
            case R.id.ll_service:
                viewPager.setCurrentItem(2);
                tabSelected(ll_service);
                break;
            case R.id.ll_mine:
                viewPager.setCurrentItem(3);
                tabSelected(ll_mine);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                tabSelected(ll_home);
                break;
            case 1:
                tabSelected(ll_category);
                break;
            case 2:
                tabSelected(ll_service);
                break;
            case 3:
                tabSelected(ll_mine);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void tabSelected(LinearLayout linearLayout) {
        ll_home.setSelected(false);
        ll_category.setSelected(false);
        ll_service.setSelected(false);
        ll_mine.setSelected(false);
        linearLayout.setSelected(true);
    }

    private class MyAdapter extends FragmentPagerAdapter {
        MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }
}
