package com.gyf.immersionbar;

/**
 * @author: ifan
 * @date: 8/12/21
 */
public enum NavigationBarType {
    /**
     * 经典导航键
     */
    CLASSIC(0),
    /**
     * 手势导航
     */
    GESTURES(1),
    /**
     * 手势导航，三段式，小按钮
     */
    GESTURES_THREE_STAGE(2),
    /**
     * 两个按钮
     */
    DOUBLE(3),
    /**
     * 未知
     */
    UNKNOWN(-1);

    private final int type;

    NavigationBarType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
