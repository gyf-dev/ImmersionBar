package com.gyf.immersionbar.fragment.five;

import android.os.Bundle;

import com.gyf.immersionbar.R;

/**
 * Created by geyifeng on 2017/7/20.
 */

public class MineFiveFragment extends BaseFiveFragment {

    public static MineFiveFragment newInstance() {
        Bundle args = new Bundle();
        MineFiveFragment fragment = new MineFiveFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_two_mine;
    }

}
