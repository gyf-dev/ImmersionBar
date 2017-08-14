package com.gyf.immersionbar.activity;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.apkfuns.logutils.LogUtils;
import com.gyf.barlibrary.ImmersionBar;
import com.gyf.immersionbar.R;
import com.gyf.immersionbar.fragment.FullDialogFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by geyifeng on 2017/7/31.
 */

public class DialogActivity extends BaseActivity implements DialogInterface.OnDismissListener {

    @BindView(R.id.btn_full)
    Button btn_full;
    private ImmersionBar mImmersionBarDialog = null;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_dialog;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(R.id.toolbar).keyboardEnable(true).init();
    }

    @Override
    protected void setListener() {
        btn_full.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullDialogFragment fullDialogFragment = new FullDialogFragment();
                fullDialogFragment.show(getSupportFragmentManager(), "FullDialogFragment");
            }
        });
    }

    @OnClick({R.id.btn_full_two, R.id.btn_top, R.id.btn_bottom, R.id.btn_left, R.id.btn_right})
    public void onClick(View view) {
        mImmersionBar.keyboardEnable(false).init();  //取消activity的软键盘监听
        //弹出Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialog);
        AlertDialog dialog = builder.create();
        dialog.setOnDismissListener(this);
        dialog.show();
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog, null);
        Toolbar toolbar = (Toolbar) dialogView.findViewById(R.id.toolbar);
        dialog.setContentView(dialogView);
        Window window = dialog.getWindow();
        //解决无法弹出输入法的问题
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        //计算屏幕宽高
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        switch (view.getId()) {
            case R.id.btn_full_two:
                window.setGravity(Gravity.TOP);
                window.setWindowAnimations(R.style.RightDialog);
                window.setLayout(width, height);
                mImmersionBarDialog = ImmersionBar.with(this, dialog, "Full");
                mImmersionBarDialog.titleBar(toolbar)
                        .navigationBarWithKitkatEnable(false)
                        .keyboardEnable(true)
                        .init();
                break;
            case R.id.btn_top:
                window.setGravity(Gravity.TOP);
                window.setWindowAnimations(R.style.TopDialog);
                window.setLayout(width, height / 2);
                mImmersionBarDialog = ImmersionBar.with(this, dialog, "Top");
                mImmersionBarDialog.titleBar(toolbar)
                        .navigationBarWithKitkatEnable(false)
                        .init();
                break;
            case R.id.btn_bottom:
                window.setGravity(Gravity.BOTTOM);
                window.setWindowAnimations(R.style.BottomDialog);
                window.setLayout(width, height / 2);
                mImmersionBarDialog = ImmersionBar.with(this, dialog, "Bottom");
                mImmersionBarDialog.navigationBarColor(R.color.cool_green_normal).init();
                break;
            case R.id.btn_left:
                window.setGravity(Gravity.TOP | Gravity.START);
                window.setWindowAnimations(R.style.LeftDialog);
                window.setLayout(width * 2 / 3, height);
                mImmersionBarDialog = ImmersionBar.with(this, dialog, "Left");
                mImmersionBarDialog.titleBar(toolbar)
                        .navigationBarWithKitkatEnable(false)
                        .keyboardEnable(true)
                        .init();
                break;
            case R.id.btn_right:
                window.setGravity(Gravity.TOP | Gravity.END);
                window.setWindowAnimations(R.style.RightDialog);
                window.setLayout(width * 2 / 3, height);
                mImmersionBarDialog = ImmersionBar.with(this, dialog, "Right");
                mImmersionBarDialog.titleBar(toolbar)
                        .navigationBarWithKitkatEnable(false)
                        .keyboardEnable(true)
                        .init();
                break;
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        mImmersionBar.keyboardEnable(true).init();
        if (mImmersionBarDialog != null)
            mImmersionBarDialog.destroy();
    }
}
