package com.gyf.immersionbar.activity;

import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.gyf.immersionbar.R;

import butterknife.BindView;

/**
 * Created by geyifeng on 2017/6/2.
 */

public class ActionBarActivity extends BaseActivity {

    @BindView(R.id.btn)
    Button btn;
    @BindView(R.id.text)
    TextView text;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_action_bar;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.keyboardEnable(true).init();
    }

    @Override
    protected void initView() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("结合actionBar使用");
        }
        text.setText("上面图片被actionBar遮挡住了,我想使布局从actionBar下面开始绘制，怎么办？");
    }

    @Override
    protected void setListener() {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImmersionBar.statusBarColor(R.color.colorPrimary)
                        .supportActionBar(true)
                        .init();
                text.setText("哈哈哈！解决啦！就问你惊不惊喜，意不意外，刺不刺激！！！" +
                        "重点是这个方法supportActionBar(true)，实现原理，当为true时，布局距离顶部的" +
                        "padding值为状态栏的高度+ActionBar的高度");
                btn.setText("解决啦");
            }
        });
    }
}
