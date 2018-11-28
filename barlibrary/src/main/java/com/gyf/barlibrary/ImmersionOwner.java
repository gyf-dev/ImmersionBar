package com.gyf.barlibrary;

/**
 * ImmersionBar接口
 * The interface Immersion owner.
 *
 * @author geyifeng
 * @date 2018 /11/15 12:52 PM
 */
public interface ImmersionOwner {

    /**
     * 懒加载，在view初始化完成之前执行
     * On lazy before view.
     */
    void onLazyBeforeView();

    /**
     * 懒加载，在view初始化完成之后执行
     * On lazy after view.
     */
    void onLazyAfterView();

    /**
     * 用户可见时候调用
     * On visible.
     */
    void onVisible();

    /**
     * 用户不可见时候调用
     * On invisible.
     */
    void onInvisible();

    /**
     * 初始化沉浸式代码
     * Init immersion bar.
     */
    void initImmersionBar();

    /**
     * 是否可以实现沉浸式，当为true的时候才可以执行initImmersionBar方法
     * Immersion bar enabled boolean.
     *
     * @return the boolean
     */
    boolean immersionBarEnabled();
}
