package com.gyf.immersionbar.sample.activity;

import android.annotation.SuppressLint;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;
import com.gyf.immersionbar.sample.R;

import butterknife.BindView;

/**
 * @author geyifeng
 * @date 2017/5/8
 */

public class Over2Activity extends BaseActivity {

    @BindView(R.id.text)
    TextView textView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_over2;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        ImmersionBar.with(this).barColor(R.color.colorPrimary).init();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        super.initView();
        textView.setText("使用系统的fitsSystemWindows属性,在布局的根节点，" +
                "指定fitsSystemWindows为true，然后在代码中使用ImmersionBar指定状态栏的颜色，详情参看此页面的实现");
    }
}
