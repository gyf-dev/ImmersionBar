package com.gyf.immersionbar.fragment.five;

import android.os.Bundle;

import com.gyf.immersionbar.R;

/**
 * Created by geyifeng on 2017/7/20.
 */

public class ServiceFiveFragment extends BaseFiveFragment {

    public static ServiceFiveFragment newInstance() {
        Bundle args = new Bundle();
        ServiceFiveFragment fragment = new ServiceFiveFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_two_service;
    }
}
