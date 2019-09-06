package com.gyf.immersionbar.sample.fragment.five;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gyf.immersionbar.ImmersionBar;
import com.gyf.immersionbar.sample.R;
import com.gyf.immersionbar.sample.utils.Utils;

import butterknife.BindView;

/**
 * @author geyifeng
 * @date 2017/7/20
 */
public class HomeFiveFragment extends BaseFiveFragment {

    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.mIv)
    ImageView mIv;

    public static HomeFiveFragment newInstance() {
        Bundle args = new Bundle();
        HomeFiveFragment fragment = new HomeFiveFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_two_home;
    }

    @Override
    public void initImmersionBar() {
        super.initImmersionBar();
        ImmersionBar.with(this).navigationBarColor(R.color.colorPrimary).init();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        super.initView();
        text.setText("使用Fragmentation框架要实现沉浸式，建议在加载Fragment的Activity中进行沉浸式初始化；解决布局重叠问题" +
                "，建议使用readme里第四种或者第五种方法解决，参考BaseFiveFragment的onViewCreated方法；" +
                "如果使用fitsSystemWindows(true)方法解决布局重叠问题，由于是采用单Activity多Fragment实现交互，Fragment之间切换就没那么丝滑了，" +
                "如果要在Fragment单独使用沉浸式，请在onSupportVisible方法中实现。参考BaseFiveFragment的onSupportVisible方法");
        Glide.with(this).asBitmap().load(Utils.getPic())
                .apply(new RequestOptions().placeholder(R.mipmap.test))
                .into(mIv);
    }
}
