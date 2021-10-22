package com.gyf.immersionbar.sample.fragment.five;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gyf.immersionbar.ImmersionBar;
import com.gyf.immersionbar.sample.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * 基于Fragmentation框架实现沉浸式，如果要在Fragment实现沉浸式，请在onSupportVisible实现沉浸式，
 * 至于解决布局重叠问题，建议使用readme里第四种或者第五种，参考onViewCreated方法
 *
 * @author geyifeng
 * @date 2017/8/12
 */
public abstract class BaseFiveFragment extends SupportFragment {
    protected Activity mActivity;
    protected View mRootView;
    private Unbinder unbinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(getLayoutId(), container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        View titleBar = view.findViewById(setTitleBar());
        ImmersionBar.setTitleBar(mActivity, titleBar);
        View statusBarView = view.findViewById(setStatusBarView());
        ImmersionBar.setStatusBarView(mActivity, statusBarView);
        initData();
        initView();
        setListener();
    }

    protected int setTitleBar() {
        return R.id.toolbar;
    }

    protected int setStatusBarView() {
        return 0;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        //请在onSupportVisible实现沉浸式
        if (isImmersionBarEnabled()) {
            initImmersionBar();
        }
    }

    public void initImmersionBar() {
        ImmersionBar.with(this).keyboardEnable(true).init();
    }

    private boolean isImmersionBarEnabled() {
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }


    /**
     * Gets layout id.
     *
     * @return the layout id
     */
    protected abstract int getLayoutId();

    /**
     * 初始化数据
     */
    protected void initData() {

    }

    /**
     * view与数据绑定
     */
    protected void initView() {

    }

    /**
     * 设置监听
     */
    protected void setListener() {

    }
}
