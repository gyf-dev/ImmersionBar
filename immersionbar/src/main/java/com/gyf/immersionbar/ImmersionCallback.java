package com.gyf.immersionbar;

/**
 * The interface Immersion callback.
 * 内部回调，EMUI3.x导航栏监听专用；外部请使用{@link OnBarPropertiesChangedListener}。
 *
 * @author geyifeng
 * @date 2019 /4/11 5:04 PM
 */
@SuppressWarnings("deprecation")
interface ImmersionCallback extends OnNavigationBarListener, Runnable {
}
