package com.gyf.immersionbar.sample.activity;

import android.annotation.SuppressLint;
import androidx.appcompat.widget.Toolbar;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;
import com.gyf.immersionbar.sample.R;
import com.gyf.immersionbar.sample.databinding.ActivityOver5Binding;


/**
 * @author geyifeng
 * @date 2017/5/8
 */
public class Over5Activity extends BaseActivity {

    private ActivityOver5Binding binding;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_over5;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        ImmersionBar.with(this).titleBar(binding.toolbar)
                .navigationBarColor(R.color.colorPrimary)
                .keyboardEnable(true)
                .init();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        binding.text.setText("不需要在xml文件增加view给状态栏预留空间，重点是这个方法titleBar(binding.toolbar)，实现原理：" +
                "根据状态栏高度动态设置标题栏（demo是ToolBar，也可以是其他的）高度，" +
                "设置标题栏距离顶部padding值的为状态栏的高度，记住xml标题栏的高度不能指定为wrap_content，" +
                "不然绘制的高度只有状态栏的高度");
    }
    @Override
    protected void initViewBinding() {
        binding = ActivityOver5Binding.bind(getContentView());
    }

}
