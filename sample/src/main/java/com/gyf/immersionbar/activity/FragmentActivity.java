package com.gyf.immersionbar.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.gyf.barlibrary.ImmersionBar;
import com.gyf.immersionbar.R;
import com.gyf.immersionbar.fragment.FourFragment;
import com.gyf.immersionbar.fragment.OneFragment;
import com.gyf.immersionbar.fragment.ThreeFragment;
import com.gyf.immersionbar.fragment.TwoFragment;
import com.gyf.immersionbar.view.CustomViewPager;
import com.lzy.widget.AlphaIndicator;

import java.util.ArrayList;

/**
 * Created by geyifeng on 2017/5/8.
 */

public class FragmentActivity extends BaseActivity {


    private ArrayList<Fragment> mFragments;
    private OneFragment oneFragment;
    private TwoFragment twoFragment;
    private ThreeFragment threeFragment;
    private FourFragment fourFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        initData();
        CustomViewPager viewPager = (CustomViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        AlphaIndicator alphaIndicator = (AlphaIndicator) findViewById(R.id.alphaIndicator);
        alphaIndicator.setViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                switch (position) {
//                    case 0:
//                        ImmersionBar.with(oneFragment)
//                                .navigationBarColor(R.color.btn4)
//                                .init();
//                        break;
//                    case 1:
//                        ImmersionBar.with(twoFragment)
//                                .statusBarDarkFont(true)
//                                .navigationBarColor(R.color.btn3)
//                                .init();
//                        break;
//                    case 2:
//                        ImmersionBar.with(threeFragment)
//                                .navigationBarColor(R.color.btn13)
//                                .init();
//                        break;
//                    case 3:
//                        ImmersionBar.with(fourFragment)
//                                .statusBarDarkFont(true)
//                                .navigationBarColor(R.color.btn1)
//                                .init();
//                        break;
//                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setOffscreenPageLimit(3);
        viewPager.setScroll(false);
    }

    private void initData() {
        mFragments = new ArrayList<>();
        oneFragment = new OneFragment();
        twoFragment = new TwoFragment();
        threeFragment = new ThreeFragment();
        fourFragment = new FourFragment();
        mFragments.add(oneFragment);
        mFragments.add(twoFragment);
        mFragments.add(threeFragment);
        mFragments.add(fourFragment);
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
