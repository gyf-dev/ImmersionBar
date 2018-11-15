package com.gyf.barlibrary;

import android.content.res.Configuration;
import android.support.v4.app.Fragment;

/**
 * 为了方便在Fragment使用沉浸式请继承ImmersionFragment，
 * 请在immersionBarEnabled方法中实现你的沉浸式代码，
 * ImmersionProxy已经做了ImmersionBar.with(mFragment).destroy()了，所以不需要在你的代码中做这个处理了
 * 如果不能继承，请拷贝代码到你的项目中
 *
 * @author geyifeng
 * @date 2017/5/12
 */
public abstract class ImmersionFragment extends Fragment implements ImmersionOwner {

    /**
     * ImmersionBar代理类
     */
    private ImmersionProxy immersionProxy = new ImmersionProxy(this);

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        immersionProxy.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onResume() {
        super.onResume();
        immersionProxy.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        immersionProxy.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        immersionProxy.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        immersionProxy.onConfigurationChanged(newConfig);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        immersionProxy.onHiddenChanged(hidden);
    }

    /**
     * 用户可见时候调用
     * On visible.
     */
    @Override
    public void onVisible() {
    }

    /**
     * 用户不可见时候调用
     * On invisible.
     */
    @Override
    public void onInvisible() {
    }

    @Override
    public boolean immersionBarEnabled() {
        return true;
    }
}
