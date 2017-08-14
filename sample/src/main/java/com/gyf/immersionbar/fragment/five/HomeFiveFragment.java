package com.gyf.immersionbar.fragment.five;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.gyf.immersionbar.R;

import butterknife.BindView;

/**
 * Created by geyifeng on 2017/7/20.
 */

public class HomeFiveFragment extends BaseFiveFragment {
    @BindView(R.id.text)
    TextView text;

    public static HomeFiveFragment newInstance() {
        Bundle args = new Bundle();
        HomeFiveFragment fragment = new HomeFiveFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_two_home;
    }

    @Override
    protected void initView() {
        super.initView();
        text.setText("使用Fragmentation框架要实现沉浸式，建议在加载Fragment的Activity中进行沉浸式初始化；解决布局重叠问题" +
                "，建议使用readme里第四种或者第五种方法解决，参考BaseFiveFragment的onViewCreated方法；" +
                "如果使用fitsSystemWindows(true)方法解决布局重叠问题，由于是采用单Activity多Fragment实现交互，Fragment之间切换就没那么丝滑了，" +
                "如果要在Fragment单独使用沉浸式，请在onSupportVisible方法中实现。参考BaseFiveFragment的onSupportVisible方法");
    }
}
