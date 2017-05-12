package com.gyf.immersionbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.gyf.barlibrary.BarHide;
import com.gyf.barlibrary.BarParams;
import com.gyf.barlibrary.ImmersionBar;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private Button btn_skip, btn_skip2, btn_skip3, btn_swipe_back, btn_fits, btn_left, btn_status_hide, btn_navigation_hide, btn_bar_hide,
            btn_bar_show, btn_full, btn_bar_font_dark, btn_bar_font_light;
    private DrawerLayout drawer;
    private LinearLayout news;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("沉浸式");
        toolbar.setTitleTextColor(Color.WHITE);
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
//                .fullScreen(true)      //有导航栏的情况下，activity全屏显示，也就是activity最下面被导航栏覆盖，不写默认非全屏
//                .hideBar(BarHide.FLAG_HIDE_BAR)  //隐藏状态栏或导航栏或两者，不写默认不隐藏
//                .setViewSupportTransformColor(toolbar) //设置支持view变色，支持一个view，不指定颜色，默认和状态栏同色，还有两个重载方法
//                .addViewSupportTransformColor(toolbar)  //设置支持view变色，可以添加多个view，不指定颜色，默认和状态栏同色，还有两个重载方法
//                .fitsSystemWindows(false)    //解决状态栏和布局重叠问题，默认为false，当为true时一定要指定statusBarColor()，不然状态栏为透明色
//                .statusBarColorTransform(R.color.orange)  //状态栏变色后的颜色
//                .navigationBarColorTransform(R.color.orange) //导航栏变色后的颜色
//                .barColorTransform(R.color.orange)  //状态栏和导航栏变色后的颜色
//                .removeSupportView()  //移除通过setViewSupportTransformColor()方法指定的view
//                .removeSupportView(toolbar)  //移除指定view支持
//                .removeSupportAllView() //移除全部view支持
//                .init();  //必须调用方可沉浸式
        btn_skip = (Button) findViewById(R.id.btn_skip);
        btn_skip2 = (Button) findViewById(R.id.btn_skip2);
        btn_skip3 = (Button) findViewById(R.id.btn_skip3);
        btn_swipe_back = (Button) findViewById(R.id.btn_swipe_back);
        btn_fits = (Button) findViewById(R.id.btn_fits);
        btn_left = (Button) findViewById(R.id.btn_left);
        btn_status_hide = (Button) findViewById(R.id.btn_status_hide);
        btn_navigation_hide = (Button) findViewById(R.id.btn_navigation_hide);
        btn_bar_hide = (Button) findViewById(R.id.btn_bar_hide);
        btn_bar_show = (Button) findViewById(R.id.btn_bar_show);
        btn_full = (Button) findViewById(R.id.btn_full);
        btn_bar_font_dark = (Button) findViewById(R.id.btn_bar_font_dark);
        btn_bar_font_light = (Button) findViewById(R.id.btn_bar_font_light);
        drawer = (DrawerLayout) findViewById(R.id.drawer);
        news = (LinearLayout) findViewById(R.id.news);

        btn_skip.setOnClickListener(this);
        btn_skip2.setOnClickListener(this);
        btn_skip3.setOnClickListener(this);
        btn_swipe_back.setOnClickListener(this);
        btn_fits.setOnClickListener(this);
        btn_left.setOnClickListener(this);
        btn_status_hide.setOnClickListener(this);
        btn_navigation_hide.setOnClickListener(this);
        btn_bar_hide.setOnClickListener(this);
        btn_bar_show.setOnClickListener(this);
        btn_full.setOnClickListener(this);
        btn_bar_font_dark.setOnClickListener(this);
        btn_bar_font_light.setOnClickListener(this);
        news.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_skip:
                Intent intent = new Intent(MainActivity.this, TestActivity.class);
                MainActivity.this.startActivity(intent);
                break;
            case R.id.btn_skip2:
                Intent intent2 = new Intent(MainActivity.this, Test2Activity.class);
                MainActivity.this.startActivity(intent2);
                break;
            case R.id.btn_skip3:
                Intent intent3 = new Intent(MainActivity.this, Test3Activity.class);
                MainActivity.this.startActivity(intent3);
                break;
            case R.id.btn_swipe_back:
                Intent intent4 = new Intent(MainActivity.this, Test4Activity.class);
                MainActivity.this.startActivity(intent4);
                break;
            case R.id.btn_fits:
                Intent intent5 = new Intent(MainActivity.this, Test5Activity.class);
                MainActivity.this.startActivity(intent5);
                break;
            case R.id.btn_left:
                drawer.openDrawer(Gravity.START);
                break;
            case R.id.btn_status_hide:
                ImmersionBar.with(this).hideBar(BarHide.FLAG_HIDE_STATUS_BAR).init();
                break;
            case R.id.btn_navigation_hide:
                ImmersionBar.with(this).hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR).init();
                break;
            case R.id.btn_bar_hide:
                ImmersionBar.with(this).hideBar(BarHide.FLAG_HIDE_BAR).init();
                break;
            case R.id.btn_full:
                BarParams barParams = ImmersionBar.with(this).getBarParams();
                if (barParams.fullScreen)
                    ImmersionBar.with(this).fullScreen(false).init();
                else
                    ImmersionBar.with(this).fullScreen(true).init();
                break;
            case R.id.btn_bar_show:
                ImmersionBar.with(this).hideBar(BarHide.FLAG_SHOW_BAR).init();
                break;
            case R.id.btn_bar_font_dark:
                ImmersionBar.with(this).statusBarDarkFont(true).init();
                break;
            case R.id.btn_bar_font_light:
                ImmersionBar.with(this).statusBarDarkFont(false).init();
                break;
            case R.id.news:
                Intent intent0 = new Intent(MainActivity.this, Test2Activity.class);
                MainActivity.this.startActivity(intent0);

                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        drawer.closeDrawer(Gravity.START);
    }
}
