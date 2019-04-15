package com.gyf.immersionbar.simple.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.apkfuns.logutils.LogUtils;

/**
 * @author geyifeng
 * @date 2019/4/13 11:32 PM
 */
public class TestFragment extends Fragment {
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LogUtils.e("onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.e("onCreate");
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtils.e("onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.e("onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtils.e("onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtils.e("onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.e("onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtils.e("onDetach");
    }
}
