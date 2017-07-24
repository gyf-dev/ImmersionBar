package com.gyf.immersionbar.activity;

import android.widget.TextView;

import com.gyf.immersionbar.R;

import butterknife.BindView;

/**
 * Created by geyifeng on 2017/5/8.
 */

public class Over2Activity extends BaseActivity {

    @BindView(R.id.text)
    TextView textView;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_over2;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.barColor(R.color.colorPrimary)
                .init();
    }

    @Override
    protected void initView() {
        super.initView();
        textView.setText("使用系统的fitsSystemWindows属性,在布局的根节点，" +
                "指定fitsSystemWindows为true，然后在代码中使用ImmersionBar指定状态栏的颜色，详情参看此页面的实现");
    }
}
