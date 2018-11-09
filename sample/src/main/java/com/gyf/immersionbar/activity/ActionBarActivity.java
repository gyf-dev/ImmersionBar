package com.gyf.immersionbar.activity;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.support.v7.app.ActionBar;
import android.widget.Button;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.gyf.immersionbar.R;

import butterknife.BindView;

/**
 * @author geyifeng
 * @date 2017/6/2
 */

public class ActionBarActivity extends BaseActivity {

    @BindView(R.id.btn)
    Button btn;
    @BindView(R.id.text)
    TextView text;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_action_bar;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        ImmersionBar.with(this).keyboardEnable(true).init();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("结合actionBar使用");
        }
        text.setText("上面图片被actionBar遮挡住了,我想使布局从actionBar下面开始绘制，怎么办？");
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void setListener() {
        btn.setOnClickListener(v -> {
            ImmersionBar.with(this)
                    .supportActionBar(true)
                    .statusBarColor(R.color.colorPrimary)
                    .init();
            text.setText("哈哈哈！解决啦！就问你惊不惊喜，意不意外，刺不刺激！！！" +
                    "重点是这个方法supportActionBar(true)，实现原理，当为true时，布局距离顶部的" +
                    "padding值为状态栏的高度+ActionBar的高度");
            btn.setText("解决啦");
        });
    }
}
