package com.gyf.immersionbar;

/**
 * @author geyifeng
 * @date 2019/4/12 6:39 PM
 */
class Constants {
    /**
     * android 4.4或者emui3状态栏ID识位
     */
    static final int IMMERSION_STATUS_BAR_VIEW_ID = R.id.immersion_status_bar_view;
    /**
     * android 4.4或者emui3导航栏ID识位
     */
    static final int IMMERSION_NAVIGATION_BAR_VIEW_ID = R.id.immersion_navigation_bar_view;
    /**
     * 状态栏高度标识位
     */
    static final String IMMERSION_STATUS_BAR_HEIGHT = "status_bar_height";
    /**
     * 导航栏竖屏高度标识位
     */
    static final String IMMERSION_NAVIGATION_BAR_HEIGHT = "navigation_bar_height";
    /**
     * 导航栏横屏高度标识位
     */
    static final String IMMERSION_NAVIGATION_BAR_HEIGHT_LANDSCAPE = "navigation_bar_height_landscape";
    /**
     * 导航栏宽度标识位位
     */
    static final String IMMERSION_NAVIGATION_BAR_WIDTH = "navigation_bar_width";
    /**
     * 小米导航栏显示隐藏标识位 0-三按钮导航，1-手势导航
     */
    static final String IMMERSION_NAVIGATION_BAR_MODE_MIUI = "force_fsg_nav_bar";
    /**
     * 小米导航栏手势导航情况下，是否隐藏手势提示线,0：显示 1：隐藏
     */
    static final String IMMERSION_NAVIGATION_BAR_MODE_MIUI_HIDE = "hide_gesture_line";
    /**
     * 华为导航栏显示隐藏标识位 0-三按钮导航，1-手势导航
     */
    static final String IMMERSION_NAVIGATION_BAR_MODE_EMUI = "navigationbar_is_min";
    /**
     * VIVO导航栏显示隐藏标识位 0-三按钮导航，1-经典三段式，2-全屏手势
     */
    static final String IMMERSION_NAVIGATION_BAR_MODE_VIVO = "navigation_gesture_on";
    /**
     * OPPO导航栏显示隐藏标识位 0-三按钮导航，1-手势导航，2-上划手势，3-侧滑手势
     */
    static final String IMMERSION_NAVIGATION_BAR_MODE_OPPO = "hide_navigationbar_enable";
    /**
     * SAMSUNG导航栏显示隐藏标识位 0-三按钮导航 1-手势导航
     */
    static final String IMMERSION_NAVIGATION_BAR_MODE_SAMSUNG = "navigation_bar_gesture_while_hidden";
    /**
     * 三星导航栏手势导航情况下,手势类型 0：三段式线条 1：单线条
     */
    static final String IMMERSION_NAVIGATION_BAR_MODE_SAMSUNG_GESTURE_TYPE = "navigation_bar_gesture_detail_type";
    /**
     * 三星导航栏手势导航情况下，是否隐藏手势提示线,0：隐藏 1：显示
     */
    static final String IMMERSION_NAVIGATION_BAR_MODE_SAMSUNG_GESTURE = "navigation_bar_gesture_hint";
    /**
     * SAMSUNG导航栏显示隐藏标识位 0-三按钮导航，1-手势导航
     */
    static final String IMMERSION_NAVIGATION_BAR_MODE_SAMSUNG_OLD = "navigationbar_hide_bar_enabled";
    /**
     * 默认手势导航 0-三按钮导航，1-双按钮导航，2-手势导航
     */
    static final String IMMERSION_NAVIGATION_BAR_MODE_DEFAULT = "navigation_mode";
    /**
     * MIUI状态栏字体黑色与白色标识位
     */
    static final String IMMERSION_STATUS_BAR_DARK_MIUI = "EXTRA_FLAG_STATUS_BAR_DARK_MODE";
    /**
     * MIUI导航栏图标黑色与白色标识位
     */
    static final String IMMERSION_NAVIGATION_BAR_DARK_MIUI = "EXTRA_FLAG_NAVIGATION_BAR_DARK_MODE";

    /**
     * 自动改变字体颜色的临界值标识位
     */
    static final int IMMERSION_BOUNDARY_COLOR = 0xFFBABABA;

    /**
     * 修复状态栏与布局重叠标识位，默认不修复
     */
    static final int FLAG_FITS_DEFAULT = 0X00;
    /**
     * 修复状态栏与布局重叠标识位，使用titleBar方法修复
     */
    static final int FLAG_FITS_TITLE = 0X01;
    /**
     * 修复状态栏与布局重叠标识位，使用titleBarMarginTop方法修复
     */
    static final int FLAG_FITS_TITLE_MARGIN_TOP = 0X02;
    /**
     * 修复状态栏与布局重叠标识位，使用StatusBarView方法修复
     */
    static final int FLAG_FITS_STATUS = 0X03;
    /**
     * 修复状态栏与布局重叠标识位，使用fitsSystemWindows方法修复
     */
    static final int FLAG_FITS_SYSTEM_WINDOWS = 0X04;
}
