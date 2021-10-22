package com.gyf.immersionbar.sample.activity;

import android.annotation.SuppressLint;
import androidx.appcompat.widget.Toolbar;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;
import com.gyf.immersionbar.sample.R;

import butterknife.BindView;

/**
 * @author geyifeng
 * @date 2017/5/8
 */
public class Over6Activity extends BaseActivity {

    @BindView(R.id.text)
    TextView textView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_over6;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        ImmersionBar.with(this).navigationBarColor(R.color.colorPrimary)
                .titleBarMarginTop(R.id.toolbar)
                .statusBarColor(R.color.colorPrimary)
                .keyboardEnable(true)
                .init();

        //或者使用静态方法设置
        //ImmersionBar.setTitleBarMarginTop(this,toolbar);
    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        textView.setText("不需要在xml文件增加view给状态栏预留空间，重点是这个方法titleBarMarginTop(toolbar)，实现原理：" +
                "根据状态栏高度动态设置标题栏（demo是ToolBar，也可以是其他的）高度，" +
                "设置标题栏距离顶部MarginTop值的为状态栏的高度，然后在指定状态栏的颜色即可！");
    }
}
