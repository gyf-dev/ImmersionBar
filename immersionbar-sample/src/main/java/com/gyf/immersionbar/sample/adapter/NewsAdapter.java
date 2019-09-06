package com.gyf.immersionbar.sample.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.gyf.immersionbar.sample.fragment.three.NewsFragment;

/**
 * @author geyifeng
 * @date 2019-05-05 17:37
 */
public class NewsAdapter extends FragmentPagerAdapter {

    private String[] mTitle;

    public NewsAdapter(FragmentManager fm, String[] title) {
        super(fm);
        mTitle = title;
    }

    @Override
    public Fragment getItem(int i) {
        return NewsFragment.newInstance(mTitle[i]);
    }

    @Override
    public int getCount() {
        return mTitle.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitle[position];
    }
}