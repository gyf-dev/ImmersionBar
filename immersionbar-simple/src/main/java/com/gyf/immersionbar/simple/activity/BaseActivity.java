package com.gyf.immersionbar.simple.activity;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.gyf.immersionbar.ImmersionBar;
import com.gyf.immersionbar.simple.R;

import butterknife.ButterKnife;

/**
 * Activity基类
 *
 * @author geyifeng
 * @date 2017/5/9
 */
public abstract class BaseActivity extends AppCompatActivity {

    private InputMethodManager mInputMethodManager;

    protected Activity mActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        setContentView(getLayoutId());
        //绑定控件
        ButterKnife.bind(this);
        //初始化沉浸式
        if (isImmersionBarEnabled()) {
            initImmersionBar();
        }
        //初始化数据
        initData();
        //view与数据绑定
        initView();
        //设置监听
        setListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mInputMethodManager = null;
    }

    /**
     * 子类设置布局Id
     *
     * @return the layout id
     */
    protected abstract int getLayoutId();

    protected void initImmersionBar() {
        //在BaseActivity里初始化
        ImmersionBar.with(this).navigationBarColor(R.color.colorPrimary).init();
    }

    protected void initData() {
    }

    protected void initView() {
    }

    protected void setListener() {
    }

    /**
     * 是否可以使用沉浸式
     * Is immersion bar enabled boolean.
     *
     * @return the boolean
     */
    protected boolean isImmersionBarEnabled() {
        return true;
    }

    @Override
    public void finish() {
        super.finish();
        hideSoftKeyBoard();
    }

    public void hideSoftKeyBoard() {
        View localView = getCurrentFocus();
        if (this.mInputMethodManager == null) {
            this.mInputMethodManager = ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE));
        }
        if ((localView != null) && (this.mInputMethodManager != null)) {
            this.mInputMethodManager.hideSoftInputFromWindow(localView.getWindowToken(), 2);
        }
    }
}
