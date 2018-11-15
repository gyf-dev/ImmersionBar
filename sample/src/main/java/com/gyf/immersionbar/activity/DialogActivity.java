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
 * @author geyifeng
 * @date 2017/7/31
 */
public class DialogActivity extends BaseActivity implements DialogInterface.OnDismissListener {

    @BindView(R.id.btn_full_fragment)
    Button btnFullFragment;
    @BindView(R.id.btn_top_fragment)
    Button btnTopFragment;
    @BindView(R.id.btn_bottom_fragment)
    Button btnBottomFragment;
    @BindView(R.id.btn_left_fragment)
    Button btnLeftFragment;
    @BindView(R.id.btn_right_fragment)
    Button btnRightFragment;

    private AlertDialog mAlertDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_dialog;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        ImmersionBar.with(this).titleBar(R.id.toolbar).keyboardEnable(true).init();
    }

    @Override
    protected void setListener() {
        btnFullFragment.setOnClickListener(v -> {
            //如果弹出的dialog里有输入框并且activity里设置了keyboardEnable为true的话，
            //为了防止dialog里的输入框弹起的时候，activity的输入框也跟着弹起，
            //当弹出Dialog的时候，要把activity的keyboardEnable方法设置为false，
            //当dialog关闭时，要把keyboardEnable设置为打开之前的状态
            ImmersionBar.with(this).keyboardEnable(false).init();
            FullDialogFragment fullDialogFragment = new FullDialogFragment();
            fullDialogFragment.show(getSupportFragmentManager(), FullDialogFragment.class.getSimpleName());
        });
        btnTopFragment.setOnClickListener(v -> {
            ImmersionBar.with(this).keyboardEnable(false).init();
            TopDialogFragment fullDialogFragment = new TopDialogFragment();
            fullDialogFragment.show(getSupportFragmentManager(), TopDialogFragment.class.getSimpleName());
        });
        btnBottomFragment.setOnClickListener(v -> {
            ImmersionBar.with(this).keyboardEnable(false).init();
            BottomDialogFragment fullDialogFragment = new BottomDialogFragment();
            fullDialogFragment.show(getSupportFragmentManager(), BottomDialogFragment.class.getSimpleName());
        });
        btnLeftFragment.setOnClickListener(v -> {
            ImmersionBar.with(this).keyboardEnable(false).init();
            LeftDialogFragment fullDialogFragment = new LeftDialogFragment();
            fullDialogFragment.show(getSupportFragmentManager(), LeftDialogFragment.class.getSimpleName());
        });
        btnRightFragment.setOnClickListener(v -> {
            ImmersionBar.with(this).keyboardEnable(false).init();
            RightDialogFragment fullDialogFragment = new RightDialogFragment();
            fullDialogFragment.show(getSupportFragmentManager(), RightDialogFragment.class.getSimpleName());
        });
    }

    @OnClick({R.id.btn_full, R.id.btn_top, R.id.btn_bottom, R.id.btn_left, R.id.btn_right})
    public void onClick(View view) {
        //取消activity的软键盘监听
        ImmersionBar.with(this).keyboardEnable(false).init();
        //弹出Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialog);
        mAlertDialog = builder.create();
        mAlertDialog.setOnDismissListener(this);
        mAlertDialog.show();
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog, null);
        Toolbar toolbar = dialogView.findViewById(R.id.toolbar);
        mAlertDialog.setContentView(dialogView);
        Window window = mAlertDialog.getWindow();
        if (window != null) {
            //解决无法弹出输入法的问题
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                    WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        }
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
                if (window != null) {
                    window.setGravity(Gravity.TOP);
                    window.setWindowAnimations(R.style.RightDialog);
                    window.setLayout(width, height);
                    ImmersionBar.with(this, mAlertDialog)
                            .titleBar(toolbar)
                            .navigationBarColor(R.color.btn3)
                            .keyboardEnable(true)
                            .init();
                }
                break;
            case R.id.btn_top:
                if (window != null) {
                    window.setGravity(Gravity.TOP);
                    window.setWindowAnimations(R.style.TopDialog);
                    window.setLayout(width, height / 2);
                    ImmersionBar.with(this, mAlertDialog)
                            .titleBar(toolbar)
                            .navigationBarWithKitkatEnable(false)
                            .init();
                }
                break;
            case R.id.btn_bottom:
                if (window != null) {
                    window.setGravity(Gravity.BOTTOM);
                    window.setWindowAnimations(R.style.BottomDialog);
                    window.setLayout(width, height / 2);
                    ImmersionBar.with(this, mAlertDialog)
                            .navigationBarColor(R.color.cool_green_normal)
                            .init();
                }
                break;
            case R.id.btn_left:
                if (window != null) {
                    window.setGravity(Gravity.TOP | Gravity.START);
                    window.setWindowAnimations(R.style.LeftDialog);
                    window.setLayout(width * 2 / 3, height);
                    ImmersionBar.with(this, mAlertDialog)
                            .titleBar(toolbar)
                            .navigationBarColor(R.color.btn11)
                            .keyboardEnable(true).init();
                }
                break;
            case R.id.btn_right:
                if (window != null) {
                    window.setGravity(Gravity.TOP | Gravity.END);
                    window.setWindowAnimations(R.style.RightDialog);
                    window.setLayout(width * 2 / 3, height);
                    ImmersionBar.with(this, mAlertDialog)
                            .titleBar(toolbar)
                            .navigationBarColor(R.color.btn6)
                            .keyboardEnable(true)
                            .init();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        ImmersionBar.with(this).keyboardEnable(true).init();
        if (mAlertDialog != null) {
            ImmersionBar.with(this, mAlertDialog).destroy();
        }
    }

    public ImmersionBar getActivityImmersionBar() {
        return ImmersionBar.with(this);
    }
}
