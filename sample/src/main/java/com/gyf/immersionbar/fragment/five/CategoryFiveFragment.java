package com.gyf.immersionbar.fragment.five;

import android.os.Bundle;

import com.gyf.immersionbar.R;

/**
 * Created by geyifeng on 2017/7/20.
 */

public class CategoryFiveFragment extends BaseFiveFragment {

    public static CategoryFiveFragment newInstance() {
        Bundle args = new Bundle();
        CategoryFiveFragment fragment = new CategoryFiveFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_two_category;
    }
}
