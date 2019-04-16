package com.gyf.immersionbar.simple.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.BarParams;
import com.gyf.immersionbar.ImmersionBar;
import com.gyf.immersionbar.simple.BuildConfig;
import com.gyf.immersionbar.simple.R;
import com.gyf.immersionbar.simple.utils.Utils;

import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.blurry.Blurry;

/**
 * @author geyifeng
 */
public class MainActivity extends BaseActivity implements DrawerLayout.DrawerListener {

    @BindView(R.id.drawer)
    DrawerLayout drawer;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_bg)
    ImageView ivBg;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.btn_params)
    Button btnParams;
    @BindView(R.id.btn_params_kotlin)
    Button btnParamsKotlin;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }


    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        ImmersionBar.with(this).titleBar(R.id.toolbar).init();
    }

    @Override
    protected void setListener() {
        super.setListener();
        drawer.addDrawerListener(this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        setSidePic();
        tvVersion.setText("当前版本：" + BuildConfig.VERSION_NAME);
    }

    @OnClick({R.id.btn_pic_color, R.id.btn_pic, R.id.btn_color, R.id.btn_params, R.id.btn_params_kotlin, R.id.btn_shape, R.id.btn_swipe_back, R.id.btn_fragment,
            R.id.btn_dialog, R.id.btn_drawer, R.id.btn_tab, R.id.btn_tab_two, R.id.btn_coordinator, R.id.btn_web, R.id.btn_action_bar, R.id.btn_flyme, R.id.btn_over,
            R.id.btn_key_board, R.id.btn_all_edit, R.id.btn_login, R.id.btn_white_status_bar, R.id.btn_auto_status_font, R.id.btn_status_hide, R.id.btn_navigation_hide,
            R.id.btn_bar_hide, R.id.btn_bar_show, R.id.btn_full, R.id.btn_bar_font_dark, R.id.btn_bar_font_light, R.id.ll_github, R.id.ll_jianshu})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.btn_params:
                intent = new Intent(this, ParamsActivity.class);
                intent.putExtra("title", btnParams.getText());
                break;
            case R.id.btn_params_kotlin:
                intent = new Intent(this, KotlinActivity.class);
                intent.putExtra("title", btnParamsKotlin.getText());
                break;
            case R.id.btn_pic_color:
                intent = new Intent(this, PicAndColorActivity.class);
                break;
            case R.id.btn_pic:
                intent = new Intent(this, PicActivity.class);
                break;
            case R.id.btn_color:
                intent = new Intent(this, ColorActivity.class);
                break;
            case R.id.btn_shape:
                intent = new Intent(this, ShapeActivity.class);
                break;
            case R.id.btn_swipe_back:
                intent = new Intent(this, BackActivity.class);
                break;
            case R.id.btn_fragment:
                intent = new Intent(this, FragmentActivity.class);
                break;
            case R.id.btn_dialog:
                intent = new Intent(this, DialogActivity.class);
                break;
            case R.id.btn_drawer:
                drawer.openDrawer(Gravity.START);
                break;
            case R.id.btn_flyme:
                intent = new Intent(this, FlymeActivity.class);
                break;
            case R.id.btn_coordinator:
                intent = new Intent(this, CoordinatorActivity.class);
                break;
            case R.id.btn_tab:
                intent = new Intent(this, TabLayoutActivity.class);
                break;
            case R.id.btn_tab_two:
                intent = new Intent(this, TabLayout2Activity.class);
                break;
            case R.id.btn_web:
                intent = new Intent(this, WebActivity.class);
                break;
            case R.id.btn_action_bar:
                intent = new Intent(this, ActionBarActivity.class);
                break;
            case R.id.btn_over:
                intent = new Intent(this, OverActivity.class);
                break;
            case R.id.btn_key_board:
                intent = new Intent(this, KeyBoardActivity.class);
                break;
            case R.id.btn_all_edit:
                intent = new Intent(this, AllEditActivity.class);
                break;
            case R.id.btn_login:
                intent = new Intent(this, LoginActivity.class);
                break;
            case R.id.btn_white_status_bar:
                intent = new Intent(this, WhiteBarActivity.class);
                break;
            case R.id.btn_auto_status_font:
                intent = new Intent(this, AutoDarkModeActivity.class);
                break;
            case R.id.btn_status_hide:
                ImmersionBar.with(this).hideBar(BarHide.FLAG_HIDE_STATUS_BAR).init();
                break;
            case R.id.btn_navigation_hide:
                if (ImmersionBar.hasNavigationBar(this)) {
                    ImmersionBar.with(this).hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR).init();
                } else {
                    Toast.makeText(this, "当前设备没有导航栏或者低于4.4系统", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_bar_hide:
                ImmersionBar.with(this).hideBar(BarHide.FLAG_HIDE_BAR).init();
                break;
            case R.id.btn_bar_show:
                ImmersionBar.with(this).hideBar(BarHide.FLAG_SHOW_BAR).init();
                break;
            case R.id.btn_full:
                if (ImmersionBar.hasNavigationBar(this)) {
                    BarParams barParams = ImmersionBar.with(this).getBarParams();
                    if (barParams.fullScreen) {
                        ImmersionBar.with(this).fullScreen(false).navigationBarColor(R.color.colorPrimary).init();
                    } else {
                        ImmersionBar.with(this).fullScreen(true).transparentNavigationBar().init();
                    }
                } else {
                    Toast.makeText(this, "当前设备没有导航栏或者低于4.4系统", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_bar_font_dark:
                if (ImmersionBar.isSupportStatusBarDarkFont()) {
                    ImmersionBar.with(this).statusBarDarkFont(true).init();
                } else {
                    Toast.makeText(this, "当前设备不支持状态栏字体变色", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_bar_font_light:
                ImmersionBar.with(this).statusBarDarkFont(false).init();
                break;
            case R.id.ll_github:
                intent = new Intent(this, BlogActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("blog", "github");
                intent.putExtra("bundle", bundle);
                break;
            case R.id.ll_jianshu:
                intent = new Intent(this, BlogActivity.class);
                Bundle bundle2 = new Bundle();
                bundle2.putString("blog", "jianshu");
                intent.putExtra("bundle", bundle2);
                break;
            default:
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }

    private void setSidePic() {
        Glide.with(this).asBitmap().load(Utils.getPic())
                .apply(new RequestOptions().placeholder(R.mipmap.test))
                .into(new BitmapImageViewTarget(ivBg) {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        super.onResourceReady(resource, transition);
                        Blurry.with(MainActivity.this).from(resource).into(ivBg);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        drawer.removeDrawerListener(this);
    }

    @Override
    public void onDrawerSlide(@NonNull View view, float v) {

    }

    @Override
    public void onDrawerOpened(@NonNull View view) {

    }

    @Override
    public void onDrawerClosed(@NonNull View view) {
        setSidePic();
    }

    @Override
    public void onDrawerStateChanged(int i) {

    }
}
