package com.gyf.immersionbar.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gyf.barlibrary.ImmersionBar;
import com.gyf.immersionbar.R;

/**
 * 全屏DialogFragment
 * Created by geyifeng on 2017/7/28.
 */

public class FullDialogFragment extends DialogFragment {

    private ImmersionBar mImmersionBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏
        setStyle(DialogFragment.STYLE_NORMAL, R.style.MyDialog);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setWindowAnimations(R.style.RightDialog);
        mImmersionBar = ImmersionBar.with(getActivity(), dialog, "FullDialogFragment");
        mImmersionBar.statusBarDarkFont(true).navigationBarColor(R.color.colorPrimary).keyboardEnable(true).init();
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mImmersionBar.titleBar(R.id.toolbar, view).init();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mImmersionBar.destroy();
    }
}
