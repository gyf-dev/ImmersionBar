package com.gyf.immersionbar.sample.activity;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gyf.immersionbar.ImmersionBar;
import com.gyf.immersionbar.sample.AppManager;
import com.gyf.immersionbar.sample.R;
import com.gyf.immersionbar.sample.fragment.dialog.BottomDialogFragment;
import com.gyf.immersionbar.sample.fragment.dialog.FullDialogFragment;
import com.gyf.immersionbar.sample.fragment.dialog.LeftDialogFragment;
import com.gyf.immersionbar.sample.fragment.dialog.RightDialogFragment;
import com.gyf.immersionbar.sample.fragment.dialog.TopDialogFragment;
import com.gyf.immersionbar.sample.utils.Utils;

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

    private Window mDialogWindow;

    private int mId = 0;

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
            FullDialogFragment fullDialogFragment = new FullDialogFragment();
            fullDialogFragment.show(getSupportFragmentManager(), FullDialogFragment.class.getSimpleName());
        });
        btnTopFragment.setOnClickListener(v -> {
            TopDialogFragment fullDialogFragment = new TopDialogFragment();
            fullDialogFragment.show(getSupportFragmentManager(), TopDialogFragment.class.getSimpleName());
        });
        btnBottomFragment.setOnClickListener(v -> {
            BottomDialogFragment fullDialogFragment = new BottomDialogFragment();
            fullDialogFragment.show(getSupportFragmentManager(), BottomDialogFragment.class.getSimpleName());
        });
        btnLeftFragment.setOnClickListener(v -> {
            LeftDialogFragment fullDialogFragment = new LeftDialogFragment();
            fullDialogFragment.show(getSupportFragmentManager(), LeftDialogFragment.class.getSimpleName());
        });
        btnRightFragment.setOnClickListener(v -> {
            RightDialogFragment fullDialogFragment = new RightDialogFragment();
            fullDialogFragment.show(getSupportFragmentManager(), RightDialogFragment.class.getSimpleName());
        });
    }

    @OnClick({R.id.btn_full, R.id.btn_top, R.id.btn_bottom, R.id.btn_left, R.id.btn_right})
    public void onClick(View view) {
        //弹出Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialog);
        mAlertDialog = builder.create();
        mAlertDialog.setOnDismissListener(this);
        mAlertDialog.show();
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog, null);
        Toolbar toolbar = dialogView.findViewById(R.id.toolbar);
        ImageView iv = dialogView.findViewById(R.id.mIv);
        Glide.with(this).asBitmap().load(Utils.getPic())
                .apply(new RequestOptions().placeholder(R.mipmap.test))
                .into(iv);
        mAlertDialog.setContentView(dialogView);
        mDialogWindow = mAlertDialog.getWindow();
        if (mDialogWindow != null) {
            //解决无法弹出输入法的问题
            mDialogWindow.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                    WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        }
        //计算屏幕宽高
        Integer[] widthAndHeight = Utils.getWidthAndHeight(getWindow());
        if (mDialogWindow != null) {
            mId = view.getId();
            switch (view.getId()) {
                case R.id.btn_full:
                    mDialogWindow.setGravity(Gravity.TOP);
                    mDialogWindow.setWindowAnimations(R.style.RightAnimation);
                    mDialogWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    ImmersionBar.with(this, mAlertDialog)
                            .titleBar(toolbar)
                            .navigationBarColor(R.color.btn3)
                            .statusBarDarkFont(true)
                            .keyboardEnable(true)
                            .init();
                    break;
                case R.id.btn_top:
                    mDialogWindow.setGravity(Gravity.TOP);
                    mDialogWindow.setWindowAnimations(R.style.TopAnimation);
                    mDialogWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, widthAndHeight[1] / 2);
                    ImmersionBar.with(this, mAlertDialog)
                            .titleBar(toolbar)
                            .navigationBarWithKitkatEnable(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                            .navigationBarColor(R.color.btn4)
                            .init();
                    break;
                case R.id.btn_bottom:
                    mDialogWindow.setGravity(Gravity.BOTTOM);
                    mDialogWindow.setWindowAnimations(R.style.BottomAnimation);
                    mDialogWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, widthAndHeight[1] / 2);
                    ImmersionBar.with(this, mAlertDialog)
                            .navigationBarColor(R.color.cool_green_normal)
                            .init();
                    break;
                case R.id.btn_left:
                    mDialogWindow.setGravity(Gravity.TOP | Gravity.START);
                    mDialogWindow.setWindowAnimations(R.style.LeftAnimation);
                    mDialogWindow.setLayout(widthAndHeight[0] / 2, ViewGroup.LayoutParams.MATCH_PARENT);
                    ImmersionBar.with(this, mAlertDialog)
                            .titleBar(toolbar)
                            .navigationBarWithKitkatEnable(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                            .navigationBarColor(R.color.btn11)
                            .keyboardEnable(true)
                            .init();
                    break;
                case R.id.btn_right:
                    mDialogWindow.setGravity(Gravity.TOP | Gravity.END);
                    mDialogWindow.setWindowAnimations(R.style.RightAnimation);
                    mDialogWindow.setLayout(widthAndHeight[0] / 2, ViewGroup.LayoutParams.MATCH_PARENT);
                    ImmersionBar.with(this, mAlertDialog)
                            .titleBar(toolbar)
                            .navigationBarColor(R.color.btn8)
                            .keyboardEnable(true)
                            .init();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        AppManager.getInstance().hideSoftKeyBoard(this);
        ImmersionBar.destroy(this, mAlertDialog);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Integer[] widthAndHeight = Utils.getWidthAndHeight(getWindow());
        if (mAlertDialog != null) {
            switch (mId) {
                case R.id.btn_top:
                    mDialogWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, widthAndHeight[1] / 2);
                    ImmersionBar.with(this, mAlertDialog)
                            .navigationBarWithKitkatEnable(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
                            .init();
                    break;
                case R.id.btn_bottom:
                    mDialogWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, widthAndHeight[1] / 2);
                    break;
                case R.id.btn_left:
                    mDialogWindow.setLayout(widthAndHeight[0] / 2, ViewGroup.LayoutParams.MATCH_PARENT);
                    ImmersionBar.with(this, mAlertDialog)
                            .navigationBarWithKitkatEnable(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
                            .init();
                    break;
                case R.id.btn_right:
                    mDialogWindow.setLayout(widthAndHeight[0] / 2, ViewGroup.LayoutParams.MATCH_PARENT);
                    break;
                default:
                    break;
            }
        }
    }
}
