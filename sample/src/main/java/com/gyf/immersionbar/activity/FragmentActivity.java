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
import com.lzy.widget.AlphaIndicator;

import java.util.ArrayList;

/**
 * Created by geyifeng on 2017/5/8.
 */

public class FragmentActivity extends BaseActivity {


    private ArrayList<Fragment> mFragments;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        ImmersionBar.with(this).statusBarDarkFont(false)
                .navigationBarColor(R.color.btn4)
                .init();
        initData();
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        AlphaIndicator alphaIndicator = (AlphaIndicator) findViewById(R.id.alphaIndicator);
        alphaIndicator.setViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                ImmersionBar immersionBar = ImmersionBar.with(FragmentActivity.this);
//                switch (position) {
//                    case 0:
//                        immersionBar.statusBarDarkFont(false)
//                                .navigationBarColor(R.color.btn4)
//                                .init();
//                        break;
//                    case 1:
//                        immersionBar.statusBarDarkFont(true)
//                                .navigationBarColor(R.color.btn3)
//                                .init();
//                        break;
//                    case 2:
//                        immersionBar.statusBarDarkFont(false)
//                                .navigationBarColor(R.color.btn13)
//                                .init();
//                        break;
//                    case 3:
//                        immersionBar.statusBarDarkFont(true)
//                                .navigationBarColor(R.color.btn1)
//                                .init();
//                        break;
//                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initData() {
        mFragments = new ArrayList<>();
        OneFragment oneFragment = new OneFragment();
        TwoFragment twoFragment = new TwoFragment();
        ThreeFragment threeFragment = new ThreeFragment();
        FourFragment fourFragment = new FourFragment();
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
