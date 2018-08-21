package com.gyf.immersionbar.activity;

import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.gyf.barlibrary.ImmersionBar;
import com.gyf.immersionbar.R;
import com.gyf.immersionbar.fragment.dialog.BottomDialogFragment;
import com.gyf.immersionbar.fragment.dialog.FullDialogFragment;
import com.gyf.immersionbar.fragment.dialog.LeftDialogFragment;
import com.gyf.immersionbar.fragment.dialog.RightDialogFragment;
import com.gyf.immersionbar.fragment.dialog.TopDialogFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by geyifeng on 2017/7/31.
 */

public class DialogActivity extends BaseActivity implements DialogInterface.OnDismissListener {

    @BindView(R.id.btn_full_fragment)
    Button btn_full_fragment;
    @BindView(R.id.btn_top_fragment)
    Button btn_top_fragment;
    @BindView(R.id.btn_bottom_fragment)
    Button btn_bottom_fragment;
    @BindView(R.id.btn_left_fragment)
    Button btn_left_fragment;
    @BindView(R.id.btn_right_fragment)
    Button btn_right_fragment;
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
        btn_full_fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果弹出的dialog里有输入框并且activity里设置了keyboardEnable为true的话，
                //当弹出Dialog的时候，要把activity的keyboardEnable方法设置为false，
                //当dialog关闭时，要把keyboardEnable设置为打开之前的状态
                mImmersionBar.keyboardEnable(false).init();
                FullDialogFragment fullDialogFragment = new FullDialogFragment();
                fullDialogFragment.show(getSupportFragmentManager(), "FullDialogFragment");
            }
        });
        btn_top_fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImmersionBar.keyboardEnable(false).init();
                TopDialogFragment fullDialogFragment = new TopDialogFragment();
                fullDialogFragment.show(getSupportFragmentManager(), "TopDialogFragment");
            }
        });
        btn_bottom_fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImmersionBar.keyboardEnable(false).init();
                BottomDialogFragment fullDialogFragment = new BottomDialogFragment();
                fullDialogFragment.show(getSupportFragmentManager(), "BottomDialogFragment");
            }
        });
        btn_left_fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImmersionBar.keyboardEnable(false).init();
                LeftDialogFragment fullDialogFragment = new LeftDialogFragment();
                fullDialogFragment.show(getSupportFragmentManager(), "LeftDialogFragment");
            }
        });
        btn_right_fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImmersionBar.keyboardEnable(false).init();
                RightDialogFragment fullDialogFragment = new RightDialogFragment();
                fullDialogFragment.show(getSupportFragmentManager(), "RightDialogFragment");
            }
        });
    }

    @OnClick({R.id.btn_full, R.id.btn_top, R.id.btn_bottom, R.id.btn_left, R.id.btn_right})
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
        //window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        //计算屏幕宽高
        int width;
        int height;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getRealMetrics(dm);
            width = dm.widthPixels;
            height = dm.heightPixels;
        } else {
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            width = metrics.widthPixels;
            height = metrics.heightPixels;
        }

        switch (view.getId()) {
            case R.id.btn_full:
                window.setGravity(Gravity.TOP);
                window.setWindowAnimations(R.style.RightDialog);
                window.setLayout(width, height);
                mImmersionBarDialog = ImmersionBar.with(this, dialog);
                mImmersionBarDialog.titleBar(toolbar)
                        .navigationBarColor(R.color.btn3)
                        .keyboardEnable(true)
                        .init();
                break;
            case R.id.btn_top:
                window.setGravity(Gravity.TOP);
                window.setWindowAnimations(R.style.TopDialog);
                window.setLayout(width, height / 2);
                mImmersionBarDialog = ImmersionBar.with(this, dialog);
                mImmersionBarDialog.titleBar(toolbar).navigationBarWithKitkatEnable(false).init();
                break;
            case R.id.btn_bottom:
                window.setGravity(Gravity.BOTTOM);
                window.setWindowAnimations(R.style.BottomDialog);
                window.setLayout(width, height / 2);
                mImmersionBarDialog = ImmersionBar.with(this, dialog);
                mImmersionBarDialog.navigationBarColor(R.color.cool_green_normal).init();
                break;
            case R.id.btn_left:
                window.setGravity(Gravity.TOP | Gravity.START);
                window.setWindowAnimations(R.style.LeftDialog);
                window.setLayout(width * 2 / 3, height);
                mImmersionBarDialog = ImmersionBar.with(this, dialog);
                mImmersionBarDialog.titleBar(toolbar)
                        .navigationBarColor(R.color.btn11)
                        .keyboardEnable(true).init();
                break;
            case R.id.btn_right:
                window.setGravity(Gravity.TOP | Gravity.END);
                window.setWindowAnimations(R.style.RightDialog);
                window.setLayout(width * 2 / 3, height);
                mImmersionBarDialog = ImmersionBar.with(this, dialog);
                mImmersionBarDialog.titleBar(toolbar)
                        .navigationBarColor(R.color.btn6)
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

    public ImmersionBar getActivityImmersionBar() {
        return mImmersionBar;
    }
}
