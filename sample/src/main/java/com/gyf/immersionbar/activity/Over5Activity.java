package com.gyf.immersionbar.activity;

import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.gyf.immersionbar.R;

import butterknife.BindView;

/**
 * Created by geyifeng on 2017/5/8.
 */

public class Over5Activity extends BaseActivity {

    @BindView(R.id.text)
    TextView textView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_over5;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(toolbar)
                .navigationBarColor(R.color.colorPrimary)
                .keyboardEnable(true)
                .init();
    }

    @Override
    protected void initView() {
        textView.setText("不需要在xml文件增加view给状态栏预留空间，重点是这个方法titleBar(toolbar)，实现原理：" +
                "根据状态栏高度动态设置标题栏（demo是ToolBar，也可以是其他的）高度，" +
                "设置标题栏距离顶部padding值的为状态栏的高度，记住xml标题栏的高度不能指定为wrap_content，" +
                "不然绘制的高度只有状态栏的高度");
    }
}
