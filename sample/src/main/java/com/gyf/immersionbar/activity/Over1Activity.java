package com.gyf.immersionbar.activity;

import android.annotation.SuppressLint;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.gyf.immersionbar.R;

import butterknife.BindView;

/**
 * @author geyifeng
 * @date 2017/5/8
 */
public class Over1Activity extends BaseActivity {

    @BindView(R.id.text)
    TextView textView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_over1;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        ImmersionBar.with(this).navigationBarColor(R.color.colorPrimary).keyboardEnable(true).init();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        textView.setText("在标题栏的上方增加View标签，高度根据android版本来判断，" +
                "在values-v19/dimens.xml文件里指定高度为25dp（20~25dp最佳，根据需求定），" +
                "在values/dimens.xml文件里，指定高度为0dp，详情参看此页面的实现");
    }
}