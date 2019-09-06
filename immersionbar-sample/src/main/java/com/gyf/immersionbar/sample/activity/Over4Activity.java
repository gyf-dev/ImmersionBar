package com.gyf.immersionbar.sample.activity;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;
import com.gyf.immersionbar.sample.R;

import butterknife.BindView;

/**
 * @author geyifeng
 * @date 2017/5/8
 */
public class Over4Activity extends BaseActivity {

    @BindView(R.id.text)
    TextView textView;
    @BindView(R.id.status_bar_view)
    View view;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_over4;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        ImmersionBar.with(this).statusBarView(view)
                .navigationBarColor(R.color.colorPrimary)
                .keyboardEnable(true)
                .init();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        textView.setText("和方法一类似，都是在标题栏的上方增加View标签，但是高度指定为0dp，" +
                "也不需要在dimens.xml文件里做android版本的判断了，全部交给ImmersionBar来完成吧，" +
                "只需要使用ImmersionBar的statusBarView方法，然后在方法里指定view就可以啦，" +
                "详情参看此页面的实现");
    }
}
