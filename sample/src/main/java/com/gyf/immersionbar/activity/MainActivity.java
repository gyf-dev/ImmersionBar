package com.gyf.immersionbar.activity;

import android.content.Intent;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;
import com.gyf.barlibrary.BarHide;
import com.gyf.barlibrary.BarParams;
import com.gyf.barlibrary.FlymeOSStatusBarFontUtils;
import com.gyf.barlibrary.ImmersionBar;
import com.gyf.barlibrary.OSUtils;
import com.gyf.immersionbar.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.drawer)
    DrawerLayout drawer;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ImmersionBar.with(this).titleBar(toolbar).init();
//        ImmersionBar.with(this)
//                .transparentStatusBar()  //透明状态栏，不写默认透明色
//                .transparentNavigationBar()  //透明导航栏，不写默认黑色(设置此方法，fullScreen()方法自动为true)
//                .transparentBar()             //透明状态栏和导航栏，不写默认状态栏为透明色，导航栏为黑色（设置此方法，fullScreen()方法自动为true）
//                .statusBarColor(R.color.colorPrimary)     //状态栏颜色，不写默认透明色
//                .navigationBarColor(R.color.colorPrimary) //导航栏颜色，不写默认黑色
//                .barColor(R.color.colorPrimary)  //同时自定义状态栏和导航栏颜色，不写默认状态栏为透明色，导航栏为黑色
//                .statusBarAlpha(0.3f)  //状态栏透明度，不写默认0.0f
//                .navigationBarAlpha(0.4f)  //导航栏透明度，不写默认0.0F
//                .barAlpha(0.3f)  //状态栏和导航栏透明度，不写默认0.0f
//                .statusBarDarkFont(true)   //状态栏字体是深色，不写默认为亮色
//                .flymeOSStatusBarFontColor(R.color.btn3)  //修改flyme OS状态栏字体颜色
//                .fullScreen(true)      //有导航栏的情况下，activity全屏显示，也就是activity最下面被导航栏覆盖，不写默认非全屏
//                .hideBar(BarHide.FLAG_HIDE_BAR)  //隐藏状态栏或导航栏或两者，不写默认不隐藏
//                .addViewSupportTransformColor(toolbar)  //设置支持view变色，可以添加多个view，不指定颜色，默认和状态栏同色，还有两个重载方法
//                .titleBar(view)    //解决状态栏和布局重叠问题，任选其一
//                .statusBarView(view)  //解决状态栏和布局重叠问题，任选其一
//                .fitsSystemWindows(false)    //解决状态栏和布局重叠问题，任选其一，默认为false，当为true时一定要指定statusBarColor()，不然状态栏为透明色
//                .supportActionBar(true) //支持ActionBar使用
//                .statusBarColorTransform(R.color.orange)  //状态栏变色后的颜色
//                .navigationBarColorTransform(R.color.orange) //导航栏变色后的颜色
//                .barColorTransform(R.color.orange)  //状态栏和导航栏变色后的颜色
//                .removeSupportView(toolbar)  //移除指定view支持
//                .removeSupportAllView() //移除全部view支持
//                .addTag("tag")  //给以上设置的参数打标记
//                .getTag("tag")  //根据tag获得沉浸式参数
//                .reset()  //重置所以沉浸式参数
//                .init();  //必须调用方可沉浸式
    }


    @OnClick({R.id.btn_pic_color, R.id.btn_pic, R.id.btn_color, R.id.btn_shape, R.id.btn_swipe_back, R.id.btn_fragment,
            R.id.btn_drawer, R.id.btn_coordinator, R.id.btn_action_bar, R.id.btn_flyme, R.id.btn_over,
            R.id.btn_key_board, R.id.btn_white_status_bar, R.id.btn_status_hide, R.id.btn_navigation_hide, R.id.btn_bar_hide,
            R.id.btn_bar_show, R.id.btn_full, R.id.btn_bar_font_dark, R.id.btn_bar_font_light})
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
            case R.id.btn_drawer:
                drawer.openDrawer(Gravity.START);
                break;
            case R.id.btn_flyme:
                startActivity(new Intent(this, FlymeActivity.class));
                break;
            case R.id.btn_coordinator:
                startActivity(new Intent(this, CoordinatorActivity.class));
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
                ImmersionBar.with(this).hideBar(BarHide.FLAG_HIDE_STATUS_BAR).init();
                break;
            case R.id.btn_navigation_hide:
                if (ImmersionBar.hasNavigationBar(this))
                    ImmersionBar.with(this).hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR).init();
                else
                    Toast.makeText(this, "当前设备没有导航栏或者低于4.4系统", Toast.LENGTH_SHORT).show();
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
                    if (barParams.fullScreen)
                        ImmersionBar.with(this).fullScreen(false).navigationBarColor(R.color.black).init();
                    else
                        ImmersionBar.with(this).fullScreen(true).transparentNavigationBar().init();
                } else
                    Toast.makeText(this, "当前设备没有导航栏或者低于4.4系统", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_bar_font_dark:
                if (ImmersionBar.isSupportStatusBarDarkFont())
                    ImmersionBar.with(this).statusBarDarkFont(true).init();
                else
                    Toast.makeText(this, "当前设备不支持状态栏字体变色", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_bar_font_light:
                ImmersionBar.with(this).statusBarDarkFont(false).init();
                break;
        }

    }
    private ContentObserver mNavigationStatusObserver = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            int navigationBarIsMin = Settings.System.getInt(getContentResolver(),
                    "navigationbar_is_min", 0);
            if (navigationBarIsMin == 1) {
                //导航键隐藏了
                ImmersionBar.with(MainActivity.this)
                        .hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR)
                        .init();
            } else {
                ImmersionBar.with(MainActivity.this)
                        .hideBar(BarHide.FLAG_SHOW_BAR)
                        .init();
                //导航键显示了
            }
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        drawer.closeDrawer(Gravity.START);
    }
}
