package com.gyf.immersionbar.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.gyf.immersionbar.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by geyifeng on 2017/5/8.
 */

public class Over2Activity extends BaseActivity {

    @BindView(R.id.text)
    TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_over2);
        ButterKnife.bind(this);
        ImmersionBar.with(this)
                .barColor(R.color.colorPrimary)
                .init();
        textView.setText("使用系统的fitsSystemWindows属性,在布局的根节点，" +
                "指定fitsSystemWindows为true，然后在代码中使用ImmersionBar指定状态栏的颜色，详情参看此页面的实现");
    }
}
