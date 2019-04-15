package com.gyf.immersionbar;

/**
 * @author geyifeng
 * @date 2019/4/12 6:39 PM
 */
class Constants {
    /**
     * android 4.4或者emui3状态栏ID识位
     */
    static final int IMMERSION_ID_STATUS_BAR_VIEW = R.id.immersion_status_bar_view;
    /**
     * android 4.4或者emui3导航栏ID识位
     */
    static final int IMMERSION_ID_NAVIGATION_BAR_VIEW = R.id.immersion_navigation_bar_view;
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
     * MIUI导航栏显示隐藏标识位
     */
    static final String IMMERSION_MIUI_NAVIGATION_BAR_HIDE_SHOW = "force_fsg_nav_bar";
    /**
     * EMUI导航栏显示隐藏标识位
     */
    static final String IMMERSION_EMUI_NAVIGATION_BAR_HIDE_SHOW = "navigationbar_is_min";
    /**
     * MIUI状态栏字体黑色与白色标识位
     */
    static final String IMMERSION_MIUI_STATUS_BAR_DARK = "EXTRA_FLAG_STATUS_BAR_DARK_MODE";
    /**
     * MIUI导航栏图标黑色与白色标识位
     */
    static final String IMMERSION_MIUI_NAVIGATION_BAR_DARK = "EXTRA_FLAG_NAVIGATION_BAR_DARK_MODE";

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
