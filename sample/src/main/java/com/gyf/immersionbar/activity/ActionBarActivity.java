package com.gyf.immersionbar.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.gyf.immersionbar.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by geyifeng on 2017/6/2.
 */

public class ActionBarActivity extends BaseActivity {

    @BindView(R.id.btn)
    Button btn;
    @BindView(R.id.text)
    TextView text;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_bar);
        ButterKnife.bind(this);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("结合actionBar使用");
        }
        text.setText("上面图片被actionBar遮挡住了,我想使布局从actionBar下面开始绘制，怎么办？");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImmersionBar.with(ActionBarActivity.this)
                        .statusBarColor(R.color.colorPrimary)
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
