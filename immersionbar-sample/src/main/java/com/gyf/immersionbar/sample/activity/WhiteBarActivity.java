package com.gyf.immersionbar.sample.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;
import com.gyf.immersionbar.sample.R;

import butterknife.BindView;

/**
 * @author geyifeng
 * @date 2017/6/2
 */
public class WhiteBarActivity extends BaseActivity {

    @BindView(R.id.btn)
    Button btn;
    @BindView(R.id.text)
    TextView text;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_white_status_bar;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        ImmersionBar.with(this).titleBar(R.id.toolbar).navigationBarColorInt(Color.WHITE).init();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void setListener() {
        btn.setOnClickListener(v -> {
            ImmersionBar.with(this)
                    .statusBarDarkFont(true)
                    .navigationBarDarkIcon(true)
                    .init();
            text.setText("A：对于状态栏重点在于statusBarDarkFont(true,0.2f)这个方法，" +
                    "原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，" +
                    "如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度；" +
                    "对于导航栏重点在于navigationBarDarkIcon(true,0.2f)这个方法，" +
                    "原理：如果当前设备支持导航栏图标变色，会设置导航栏图标为黑色，" +
                    "如果当前设备不支持导航栏图标变色，会使当前导航栏加上透明度，否则不执行透明度；");
            btn.setVisibility(View.GONE);
        });
    }
}
