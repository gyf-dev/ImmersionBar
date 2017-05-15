package com.gyf.immersionbar.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gyf.barlibrary.BarParams;
import com.gyf.barlibrary.ImmersionBar;
import com.gyf.immersionbar.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by geyifeng on 2017/5/8.
 */

public class Over4Activity extends BaseActivity {

    @BindView(R.id.text)
    TextView textView;
    @BindView(R.id.status_bar_view)
    View view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_over4);
        ButterKnife.bind(this);
        ImmersionBar.with(this)
                .statusBarView(view)
                .navigationBarColor(R.color.colorPrimary)
                .init();
        textView.setText("和方法一类似，都是在标题栏的上方增加View标签，但是高度指定为0dp，" +
                "也不需要在dimens.xml文件里做android版本的判断了，全部交给ImmersionBar来完成吧，" +
                "只需要使用ImmersionBar的statusBarView方法，然后在方法里指定view就可以啦，" +
                "详情参看此页面的实现");
    }
}
