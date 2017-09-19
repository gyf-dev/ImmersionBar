package com.gyf.immersionbar.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gyf.barlibrary.BarHide;
import com.gyf.barlibrary.BarParams;
import com.gyf.barlibrary.ImmersionBar;
import com.gyf.immersionbar.R;

import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.blurry.Blurry;

public class MainActivity extends BaseActivity {

    @BindView(R.id.drawer)
    DrawerLayout drawer;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_bg)
    ImageView iv_bg;
    @BindView(R.id.tv_version)
    TextView tv_version;

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(R.id.toolbar).init();
    }

    @Override
    protected void initView() {
        Blurry.with(this).from(BitmapFactory.decodeResource(getResources(), R.mipmap.test)).into(iv_bg);
        tv_version.setText("当前版本：" + getVersionName(this));
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_main;
    }

    @OnClick({R.id.btn_pic_color, R.id.btn_pic, R.id.btn_color, R.id.btn_shape, R.id.btn_swipe_back, R.id.btn_fragment,
            R.id.btn_dialog, R.id.btn_drawer, R.id.btn_tab, R.id.btn_coordinator, R.id.btn_web, R.id.btn_action_bar, R.id.btn_flyme, R.id.btn_over,
            R.id.btn_key_board, R.id.btn_white_status_bar, R.id.btn_status_hide, R.id.btn_navigation_hide, R.id.btn_bar_hide,
            R.id.btn_bar_show, R.id.btn_full, R.id.btn_bar_font_dark, R.id.btn_bar_font_light, R.id.ll_github, R.id.ll_jianshu})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_pic_color:
                startActivity(new Intent(this, PicAndColorActivity.class));
                break;
            case R.id.btn_pic:
                startActivity(new Intent(this, PicActivity.class));
                break;
            case R.id.btn_color:
                startActivity(new Intent(this, ColorActivity.class));
                break;
            case R.id.btn_shape:
                startActivity(new Intent(this, ShapeActivity.class));
                break;
            case R.id.btn_swipe_back:
                startActivity(new Intent(this, BackActivity.class));
                break;
            case R.id.btn_fragment:
                startActivity(new Intent(this, FragmentActivity.class));
                break;
            case R.id.btn_dialog:
                startActivity(new Intent(this, DialogActivity.class));
                break;
            case R.id.btn_drawer:
                drawer.openDrawer(Gravity.START);
                break;
            case R.id.btn_flyme:
                startActivity(new Intent(this, FlymeActivity.class));
                break;
            case R.id.btn_coordinator:
                startActivity(new Intent(this, CoordinatorActivity.class));
                break;
            case R.id.btn_tab:
                startActivity(new Intent(this, TabLayoutActivity.class));
                break;
            case R.id.btn_web:
                startActivity(new Intent(this, WebActivity.class));
                break;
            case R.id.btn_action_bar:
                startActivity(new Intent(this, ActionBarActivity.class));
                break;
            case R.id.btn_over:
                startActivity(new Intent(this, OverActivity.class));
                break;
            case R.id.btn_key_board:
                startActivity(new Intent(this, KeyBoardActivity.class));
                break;
            case R.id.btn_white_status_bar:
                startActivity(new Intent(this, WhiteStatusBarActivity.class));
                break;
            case R.id.btn_status_hide:
                mImmersionBar.hideBar(BarHide.FLAG_HIDE_STATUS_BAR).init();
                break;
            case R.id.btn_navigation_hide:
                if (ImmersionBar.hasNavigationBar(this))
                    ImmersionBar.with(this).hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR).init();
                else
                    Toast.makeText(this, "当前设备没有导航栏或者低于4.4系统", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_bar_hide:
                mImmersionBar.hideBar(BarHide.FLAG_HIDE_BAR).init();
                break;
            case R.id.btn_bar_show:
                mImmersionBar.hideBar(BarHide.FLAG_SHOW_BAR).init();
                break;
            case R.id.btn_full:
                if (ImmersionBar.hasNavigationBar(this)) {
                    BarParams barParams = mImmersionBar.getBarParams();
                    if (barParams.fullScreen)
                        mImmersionBar.fullScreen(false).navigationBarColor(R.color.black).init();
                    else
                        mImmersionBar.fullScreen(true).transparentNavigationBar().init();
                } else
                    Toast.makeText(this, "当前设备没有导航栏或者低于4.4系统", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_bar_font_dark:
                if (ImmersionBar.isSupportStatusBarDarkFont())
                    mImmersionBar.statusBarDarkFont(true).init();
                else
                    Toast.makeText(this, "当前设备不支持状态栏字体变色", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_bar_font_light:
                mImmersionBar.statusBarDarkFont(false).init();
                break;
            case R.id.ll_github:
                Intent intent = new Intent(this, BlogActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("blog", "github");
                intent.putExtra("bundle", bundle);
                startActivity(intent);
                break;
            case R.id.ll_jianshu:
                Intent intent2 = new Intent(this, BlogActivity.class);
                Bundle bundle2 = new Bundle();
                bundle2.putString("blog", "jianshu");
                intent2.putExtra("bundle", bundle2);
                startActivity(intent2);
                break;
        }

    }

    public String getVersionName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo;
        String versionName = "";
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }
}
