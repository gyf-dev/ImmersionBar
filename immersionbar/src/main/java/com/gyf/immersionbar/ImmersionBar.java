package com.gyf.immersionbar;

import static com.gyf.immersionbar.Constants.FLAG_FITS_DEFAULT;
import static com.gyf.immersionbar.Constants.FLAG_FITS_STATUS;
import static com.gyf.immersionbar.Constants.FLAG_FITS_SYSTEM_WINDOWS;
import static com.gyf.immersionbar.Constants.FLAG_FITS_TITLE;
import static com.gyf.immersionbar.Constants.FLAG_FITS_TITLE_MARGIN_TOP;
import static com.gyf.immersionbar.Constants.IMMERSION_BOUNDARY_COLOR;
import static com.gyf.immersionbar.Constants.IMMERSION_NAVIGATION_BAR_DARK_MIUI;
import static com.gyf.immersionbar.Constants.IMMERSION_NAVIGATION_BAR_VIEW_ID;
import static com.gyf.immersionbar.Constants.IMMERSION_STATUS_BAR_DARK_MIUI;
import static com.gyf.immersionbar.Constants.IMMERSION_STATUS_BAR_VIEW_ID;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.FloatRange;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * android 4.4以上沉浸式以及bar的管理
 *
 * @author gyf
 * @date 2017 /05/09
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
public final class ImmersionBar implements ImmersionCallback {

    private final Activity mActivity;
    private Fragment mSupportFragment;
    private android.app.Fragment mFragment;
    private Dialog mDialog;
    private Window mWindow;
    private ViewGroup mDecorView;
    private ViewGroup mContentView;
    private ImmersionBar mParentBar;

    /**
     * 是否是在Fragment里使用
     */
    private boolean mIsFragment = false;
    /**
     * 是否是DialogFragment
     */
    private boolean mIsDialogFragment = false;
    /**
     * 是否是在Dialog里使用
     */
    private boolean mIsDialog = false;
    /**
     * 用户配置的bar参数
     */
    private BarParams mBarParams;
    /**
     * 系统bar相关信息
     */
    private BarConfig mBarConfig;
    /**
     * 导航栏的高度，适配Emui3系统有用
     */
    private int mNavigationBarHeight = 0;
    /**
     * 导航栏的宽度，适配Emui3系统有用
     */
    private int mNavigationBarWidth = 0;
    /**
     * ActionBar的高度
     */
    private int mActionBarHeight = 0;
    /**
     * 软键盘监听相关
     */
    private FitsKeyboard mFitsKeyboard = null;
    /**
     * 用户使用tag增加的bar参数的集合
     */
    private final Map<String, BarParams> mTagMap = new HashMap<>();
    /**
     * 当前顶部布局和状态栏重叠是以哪种方式适配的
     */
    private int mFitsStatusBarType = FLAG_FITS_DEFAULT;
    /**
     * 是否已经调用过init()方法了
     */
    private boolean mInitialized = false;
    /**
     * ActionBar是否是在LOLLIPOP下设备使用
     */
    private boolean mIsActionBarBelowLOLLIPOP = false;

    private boolean mKeyboardTempEnable = false;

    private int mPaddingLeft = 0, mPaddingTop = 0, mPaddingRight = 0, mPaddingBottom = 0;

    /**
     * 在Activity使用
     * With immersion bar.
     *
     * @param activity the activity
     * @return the immersion bar
     */
    public static ImmersionBar with(@NonNull Activity activity) {
        return getRetriever().get(activity, false);
    }

    /**
     * 在Activity使用
     *
     * @param activity the activity
     * @param isOnly   the is only fragment实例对象是否唯一，默认是false，不唯一，isOnly影响tag以何种形式生成
     * @return the immersion bar
     */
    public static ImmersionBar with(@NonNull Activity activity, boolean isOnly) {
        return getRetriever().get(activity, isOnly);
    }

    /**
     * 在Fragment使用
     * With immersion bar.
     *
     * @param fragment the fragment
     * @return the immersion bar
     */
    public static ImmersionBar with(@NonNull Fragment fragment) {
        return getRetriever().get(fragment, false);
    }

    /**
     * 在Fragment使用
     * With immersion bar.
     *
     * @param fragment the fragment
     * @param isOnly   the is only fragment实例对象是否唯一，默认是false，不唯一，isOnly影响tag以何种形式生成
     * @return the immersion bar
     */
    public static ImmersionBar with(@NonNull Fragment fragment, boolean isOnly) {
        return getRetriever().get(fragment, isOnly);
    }

    /**
     * 在Fragment使用
     * With immersion bar.
     *
     * @param fragment the fragment
     * @return the immersion bar
     */
    public static ImmersionBar with(@NonNull android.app.Fragment fragment) {
        return getRetriever().get(fragment, false);
    }

    /**
     * 在Fragment使用
     * With immersion bar.
     *
     * @param fragment the fragment
     * @param isOnly   the is only fragment实例对象是否唯一，默认是false，不唯一，isOnly影响tag以何种形式生成
     * @return the immersion bar
     */
    public static ImmersionBar with(@NonNull android.app.Fragment fragment, boolean isOnly) {
        return getRetriever().get(fragment, isOnly);
    }

    /**
     * 在DialogFragment使用
     * With immersion bar.
     *
     * @param dialogFragment the dialog fragment
     * @return the immersion bar
     */
    public static ImmersionBar with(@NonNull DialogFragment dialogFragment) {
        return getRetriever().get(dialogFragment, false);
    }

    /**
     * 在DialogFragment使用
     *
     * @param dialogFragment the dialog fragment
     * @param isOnly         the is only fragment实例对象是否唯一，默认是false，不唯一，isOnly影响tag以何种形式生成
     * @return the immersion bar
     */
    public static ImmersionBar with(@NonNull DialogFragment dialogFragment, boolean isOnly) {
        return getRetriever().get(dialogFragment, isOnly);
    }

    /**
     * 在DialogFragment使用
     * With immersion bar.
     *
     * @param dialogFragment the dialog fragment
     * @return the immersion bar
     */
    public static ImmersionBar with(@NonNull android.app.DialogFragment dialogFragment) {
        return getRetriever().get(dialogFragment, false);
    }

    /**
     * 在DialogFragment使用
     *
     * @param dialogFragment the dialog fragment
     * @param isOnly         the is only fragment实例对象是否唯一，默认是false，不唯一，isOnly影响tag以何种形式生成
     * @return the immersion bar
     */
    public static ImmersionBar with(@NonNull android.app.DialogFragment dialogFragment, boolean isOnly) {
        return getRetriever().get(dialogFragment, isOnly);
    }

    /**
     * 在dialog里使用
     * With immersion bar.
     *
     * @param activity the activity
     * @param dialog   the dialog
     * @return the immersion bar
     */
    public static ImmersionBar with(@NonNull Activity activity, @NonNull Dialog dialog) {
        return getRetriever().get(activity, dialog, false);
    }

    /**
     * 在dialog里使用
     *
     * @param activity the activity
     * @param dialog   the dialog
     * @param isOnly   the is only fragment实例对象是否唯一，默认是false，不唯一，isOnly影响tag以何种形式生成
     * @return the immersion bar
     */
    public static ImmersionBar with(@NonNull Activity activity, @NonNull Dialog dialog, boolean isOnly) {
        return getRetriever().get(activity, dialog, isOnly);
    }

    /**
     * 销毁Fragment
     *
     * @param fragment the Fragment
     */
    public static void destroy(@NonNull Fragment fragment) {
        getRetriever().destroy(fragment, false);
    }

    /**
     * 销毁Fragment
     *
     * @param fragment the Fragment
     * @param isOnly   the is only fragment实例对象是否唯一，默认是false，不唯一，isOnly影响tag以何种形式生成
     */
    public static void destroy(@NonNull Fragment fragment, boolean isOnly) {
        getRetriever().destroy(fragment, isOnly);
    }

    /**
     * 销毁Fragment
     *
     * @param fragment the android.app.Fragment
     */
    public static void destroy(@NonNull android.app.Fragment fragment) {
        getRetriever().destroy(fragment, false);
    }


    /**
     * 销毁Fragment
     *
     * @param fragment the android.app.Fragment
     * @param isOnly   the is only fragment实例对象是否唯一，默认是false，不唯一，isOnly影响tag以何种形式生成
     */
    public static void destroy(@NonNull android.app.Fragment fragment, boolean isOnly) {
        getRetriever().destroy(fragment, isOnly);
    }

    /**
     * 在Dialog里销毁，不包括DialogFragment
     *
     * @param activity the activity
     * @param dialog   the dialog
     */
    public static void destroy(@NonNull Activity activity, @NonNull Dialog dialog) {
        getRetriever().destroy(activity, dialog, false);
    }

    /**
     * 在Dialog里销毁，不包括DialogFragment
     *
     * @param activity the activity
     * @param dialog   the dialog
     * @param isOnly   the is only fragment实例对象是否唯一，默认是false，不唯一，isOnly影响tag以何种形式生成
     */
    public static void destroy(@NonNull Activity activity, @NonNull Dialog dialog, boolean isOnly) {
        getRetriever().destroy(activity, dialog, isOnly);
    }

    /**
     * 通过上面配置后初始化后方可成功调用
     */
    public void init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && mBarParams.barEnable) {
            //更新Bar的参数
            updateBarParams();
            //设置沉浸式
            setBar();
            //修正界面显示
            fitsWindows();
            //适配软键盘与底部输入框冲突问题
            fitsKeyboard();
            //变色view
            transformView();
            mInitialized = true;
        }
    }

    /**
     * 内部方法无需调用
     */
    void onDestroy() {
        //取消监听
        cancelListener();
        if (mIsDialog && mParentBar != null) {
            mParentBar.mBarParams.keyboardEnable = mParentBar.mKeyboardTempEnable;
            if (mParentBar.mBarParams.barHide != BarHide.FLAG_SHOW_BAR) {
                mParentBar.setBar();
            }
        }
        mInitialized = false;
    }

    void onResume() {
        updateBarConfig();
        if (!mIsFragment && mInitialized && mBarParams != null) {
            if (OSUtils.isEMUI3_x() && mBarParams.navigationBarWithEMUI3Enable) {
                init();
            } else {
                if (mBarParams.barHide != BarHide.FLAG_SHOW_BAR) {
                    setBar();
                }
            }
        }
    }

    void onConfigurationChanged(Configuration newConfig) {
        updateBarConfig();
        if (OSUtils.isEMUI3_x() || Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            if (mInitialized && !mIsFragment && mBarParams.navigationBarWithKitkatEnable) {
                init();
            } else {
                fitsWindows();
            }
        } else {
            fitsWindows();
        }
    }

    /**
     * 更新Bar的参数
     * Update bar params.
     */
    private void updateBarParams() {
        adjustDarkModeParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //获得Bar相关信息
            if (!mInitialized || mIsFragment) {
                updateBarConfig();
            }
            if (mParentBar != null) {
                //如果在Fragment中使用，让Activity同步Fragment的BarParams参数
                if (mIsFragment) {
                    mParentBar.mBarParams = mBarParams;
                }
                //如果dialog里设置了keyboardEnable为true，则Activity中所设置的keyboardEnable为false
                if (mIsDialog) {
                    if (mParentBar.mKeyboardTempEnable) {
                        mParentBar.mBarParams.keyboardEnable = false;
                    }
                }
            }
        }
    }

    /**
     * 初始化状态栏和导航栏
     */
    void setBar() {
        //防止系统栏隐藏时内容区域大小发生变化
        int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !OSUtils.isEMUI3_x()) {
            //适配刘海屏
            fitsNotchScreen();
            //初始化5.0以上，包含5.0
            uiFlags = initBarAboveLOLLIPOP(uiFlags);
            //android 6.0以上设置状态栏字体为暗色
            uiFlags = setStatusBarDarkFont(uiFlags);
            //android 8.0以上设置导航栏图标为暗色
            uiFlags = setNavigationIconDark(uiFlags);
            //适配android 11以上
            setBarDarkFontAboveR();
        } else {
            //初始化5.0以下，4.4以上沉浸式
            initBarBelowLOLLIPOP();
        }
        //隐藏状态栏或者导航栏
        uiFlags = hideBarBelowR(uiFlags);
        //应用flag
        mDecorView.setSystemUiVisibility(uiFlags);
        //适配小米和魅族状态栏黑白
        setSpecialBarDarkMode();
        //适配android 11以上
        hideBarAboveR();
        //导航栏显示隐藏监听，目前只支持带有导航栏的华为和小米手机
        if (mBarParams.onNavigationBarListener != null) {
            NavigationBarObserver.getInstance().register(mActivity.getApplication());
        }
    }

    private void setBarDarkFontAboveR() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            setStatusBarDarkFontAboveR();
            setNavigationIconDarkAboveR();
        }
    }

    private void setSpecialBarDarkMode() {
        if (OSUtils.isMIUI6Later()) {
            //修改miui状态栏字体颜色
            SpecialBarFontUtils.setMIUIBarDark(mWindow, IMMERSION_STATUS_BAR_DARK_MIUI, mBarParams.statusBarDarkFont);
            //修改miui导航栏图标为黑色
            if (mBarParams.navigationBarEnable) {
                SpecialBarFontUtils.setMIUIBarDark(mWindow, IMMERSION_NAVIGATION_BAR_DARK_MIUI, mBarParams.navigationBarDarkIcon);
            }
        }
        // 修改Flyme OS状态栏字体颜色
        if (OSUtils.isFlymeOS4Later()) {
            if (mBarParams.flymeOSStatusBarFontColor != 0) {
                SpecialBarFontUtils.setStatusBarDarkIcon(mActivity, mBarParams.flymeOSStatusBarFontColor);
            } else {
                SpecialBarFontUtils.setStatusBarDarkIcon(mActivity, mBarParams.statusBarDarkFont);
            }
        }
    }

    /**
     * 适配刘海屏
     * Fits notch screen.
     */
    private void fitsNotchScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && !mInitialized) {
            try {
                WindowManager.LayoutParams lp = mWindow.getAttributes();
                lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
                mWindow.setAttributes(lp);
            } catch (Exception e) {

            }
        }
    }

    /**
     * 初始化android 5.0以上状态栏和导航栏
     *
     * @param uiFlags the ui flags
     * @return the int
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private int initBarAboveLOLLIPOP(int uiFlags) {
        //获得默认导航栏颜色
        if (!mInitialized) {
            mBarParams.defaultNavigationBarColor = mWindow.getNavigationBarColor();
        }
        //Activity全屏显示，但状态栏不会被隐藏覆盖，状态栏依然可见，Activity顶端布局部分会被状态栏遮住。
        uiFlags |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        if (mBarParams.fullScreen && mBarParams.navigationBarEnable) {
            //Activity全屏显示，但导航栏不会被隐藏覆盖，导航栏依然可见，Activity底部布局部分会被导航栏遮住。
            uiFlags |= View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
        }
        mWindow.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //判断是否存在导航栏
        if (mBarConfig.hasNavigationBar()) {
            mWindow.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        //需要设置这个才能设置状态栏和导航栏颜色
        mWindow.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        if (mBarParams.statusBarColorEnabled) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                mWindow.setStatusBarContrastEnforced(false);
            }
            mWindow.setStatusBarColor(ColorUtils.blendARGB(mBarParams.statusBarColor,
                    mBarParams.statusBarColorTransform, mBarParams.statusBarAlpha));
        } else {
            mWindow.setStatusBarColor(ColorUtils.blendARGB(mBarParams.statusBarColor,
                    Color.TRANSPARENT, mBarParams.statusBarAlpha));
        }
        //设置导航栏颜色
        if (mBarParams.navigationBarEnable) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                mWindow.setNavigationBarContrastEnforced(false);
            }
            mWindow.setNavigationBarColor(ColorUtils.blendARGB(mBarParams.navigationBarColor,
                    mBarParams.navigationBarColorTransform, mBarParams.navigationBarAlpha));
        } else {
            mWindow.setNavigationBarColor(mBarParams.defaultNavigationBarColor);
        }
        return uiFlags;
    }

    /**
     * 初始化android 4.4和emui3.1状态栏和导航栏
     */
    private void initBarBelowLOLLIPOP() {
        //透明状态栏
        mWindow.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //创建一个假的状态栏
        setupStatusBarView();
        //判断是否存在导航栏，是否禁止设置导航栏
        if (mBarConfig.hasNavigationBar() || OSUtils.isEMUI3_x()) {
            if (mBarParams.navigationBarEnable && mBarParams.navigationBarWithKitkatEnable) {
                //透明导航栏，设置这个，如果有导航栏，底部布局会被导航栏遮住
                mWindow.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            } else {
                mWindow.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
            if (mNavigationBarHeight == 0) {
                mNavigationBarHeight = mBarConfig.getNavigationBarHeight();
            }
            if (mNavigationBarWidth == 0) {
                mNavigationBarWidth = mBarConfig.getNavigationBarWidth();
            }
            //创建一个假的导航栏
            setupNavBarView();
        }
    }

    /**
     * 设置一个可以自定义颜色的状态栏
     */
    private void setupStatusBarView() {
        View statusBarView = mDecorView.findViewById(IMMERSION_STATUS_BAR_VIEW_ID);
        if (statusBarView == null) {
            statusBarView = new View(mActivity);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                    mBarConfig.getStatusBarHeight());
            params.gravity = Gravity.TOP;
            statusBarView.setLayoutParams(params);
            statusBarView.setVisibility(View.VISIBLE);
            statusBarView.setId(IMMERSION_STATUS_BAR_VIEW_ID);
            mDecorView.addView(statusBarView);
        }
        if (mBarParams.statusBarColorEnabled) {
            statusBarView.setBackgroundColor(ColorUtils.blendARGB(mBarParams.statusBarColor,
                    mBarParams.statusBarColorTransform, mBarParams.statusBarAlpha));
        } else {
            statusBarView.setBackgroundColor(ColorUtils.blendARGB(mBarParams.statusBarColor,
                    Color.TRANSPARENT, mBarParams.statusBarAlpha));
        }
    }

    /**
     * 设置一个可以自定义颜色的导航栏
     */
    private void setupNavBarView() {
        View navigationBarView = mDecorView.findViewById(IMMERSION_NAVIGATION_BAR_VIEW_ID);
        if (navigationBarView == null) {
            navigationBarView = new View(mActivity);
            navigationBarView.setId(IMMERSION_NAVIGATION_BAR_VIEW_ID);
            mDecorView.addView(navigationBarView);
        }

        FrameLayout.LayoutParams params;
        if (mBarConfig.isNavigationAtBottom()) {
            params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, mBarConfig.getNavigationBarHeight());
            params.gravity = Gravity.BOTTOM;
        } else {
            params = new FrameLayout.LayoutParams(mBarConfig.getNavigationBarWidth(), FrameLayout.LayoutParams.MATCH_PARENT);
            params.gravity = Gravity.END;
        }
        navigationBarView.setLayoutParams(params);
        navigationBarView.setBackgroundColor(ColorUtils.blendARGB(mBarParams.navigationBarColor,
                mBarParams.navigationBarColorTransform, mBarParams.navigationBarAlpha));

        if (mBarParams.navigationBarEnable && mBarParams.navigationBarWithKitkatEnable && !mBarParams.hideNavigationBar) {
            navigationBarView.setVisibility(View.VISIBLE);
        } else {
            navigationBarView.setVisibility(View.GONE);
        }
    }

    /**
     * 调整深色亮色模式参数
     */
    private void adjustDarkModeParams() {
        int statusBarColor = ColorUtils.blendARGB(mBarParams.statusBarColor,
                mBarParams.statusBarColorTransform, mBarParams.statusBarAlpha);
        if (mBarParams.autoStatusBarDarkModeEnable && statusBarColor != Color.TRANSPARENT) {
            boolean statusBarDarkFont = statusBarColor > IMMERSION_BOUNDARY_COLOR;
            statusBarDarkFont(statusBarDarkFont, mBarParams.autoStatusBarDarkModeAlpha);
        }
        int navigationBarColor = ColorUtils.blendARGB(mBarParams.navigationBarColor,
                mBarParams.navigationBarColorTransform, mBarParams.navigationBarAlpha);
        if (mBarParams.autoNavigationBarDarkModeEnable && navigationBarColor != Color.TRANSPARENT) {
            boolean navigationBarDarkIcon = navigationBarColor > IMMERSION_BOUNDARY_COLOR;
            navigationBarDarkIcon(navigationBarDarkIcon, mBarParams.autoNavigationBarDarkModeAlpha);
        }
    }

    /**
     * Hide bar.
     * 隐藏或显示状态栏和导航栏。
     *
     * @param uiFlags the ui flags
     * @return the int
     */
    private int hideBarBelowR(int uiFlags) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return uiFlags;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            switch (mBarParams.barHide) {
                case FLAG_HIDE_BAR:
                    uiFlags |= View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.INVISIBLE;
                    break;
                case FLAG_HIDE_STATUS_BAR:
                    uiFlags |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.INVISIBLE;
                    break;
                case FLAG_HIDE_NAVIGATION_BAR:
                    uiFlags |= View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
                    break;
                case FLAG_SHOW_BAR:
                    uiFlags |= View.SYSTEM_UI_FLAG_VISIBLE;
                    break;
                default:
                    break;
            }
        }
        return uiFlags | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
    }

    /**
     * 修正界面显示
     */
    private void fitsWindows() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !OSUtils.isEMUI3_x()) {
                //android 5.0以上解决状态栏和布局重叠问题
                fitsWindowsAboveLOLLIPOP();
            } else {
                //android 5.0以下解决状态栏和布局重叠问题
                fitsWindowsBelowLOLLIPOP();
            }
            //适配状态栏与布局重叠问题
            fitsLayoutOverlap();
        }
    }

    /**
     * android 5.0以下解决状态栏和布局重叠问题
     */
    private void fitsWindowsBelowLOLLIPOP() {
        if (mBarParams.isSupportActionBar) {
            mIsActionBarBelowLOLLIPOP = true;
            mContentView.post(this);
        } else {
            mIsActionBarBelowLOLLIPOP = false;
            postFitsWindowsBelowLOLLIPOP();
        }
    }

    @Override
    public void run() {
        postFitsWindowsBelowLOLLIPOP();
    }

    private void postFitsWindowsBelowLOLLIPOP() {
        //解决android4.4有导航栏的情况下，activity底部被导航栏遮挡的问题和android 5.0以下解决状态栏和布局重叠问题
        fitsWindowsKITKAT();
        //解决华为emui3.1或者3.0导航栏手动隐藏的问题
        if (!mIsFragment && OSUtils.isEMUI3_x()) {
            fitsWindowsEMUI();
        }
    }

    /**
     * android 5.0以上解决状态栏和布局重叠问题
     * Fits windows above lollipop.
     */
    private void fitsWindowsAboveLOLLIPOP() {
        if (checkFitsSystemWindows(mDecorView.findViewById(android.R.id.content))) {
            setPadding(0, 0, 0, 0);
            return;
        }
        int top = 0;
        if (mBarParams.fits && mFitsStatusBarType == FLAG_FITS_SYSTEM_WINDOWS) {
            top = mBarConfig.getStatusBarHeight();
        }
        if (mBarParams.isSupportActionBar) {
            top = mBarConfig.getStatusBarHeight() + mActionBarHeight;
        }
        setPadding(0, top, 0, 0);
    }

    /**
     * 解决android4.4有导航栏的情况下，activity底部被导航栏遮挡的问题和android 5.0以下解决状态栏和布局重叠问题
     * Fits windows below lollipop.
     */
    private void fitsWindowsKITKAT() {
        if (checkFitsSystemWindows(mDecorView.findViewById(android.R.id.content))) {
            setPadding(0, 0, 0, 0);
            return;
        }
        int top = 0, right = 0, bottom = 0;
        if (mBarParams.fits && mFitsStatusBarType == FLAG_FITS_SYSTEM_WINDOWS) {
            top = mBarConfig.getStatusBarHeight();
        }
        if (mBarParams.isSupportActionBar) {
            top = mBarConfig.getStatusBarHeight() + mActionBarHeight;
        }
        if (mBarConfig.hasNavigationBar() && mBarParams.navigationBarEnable && mBarParams.navigationBarWithKitkatEnable) {
            if (!mBarParams.fullScreen) {
                if (mBarConfig.isNavigationAtBottom()) {
                    bottom = mBarConfig.getNavigationBarHeight();
                } else {
                    right = mBarConfig.getNavigationBarWidth();
                }
            }
            if (mBarParams.hideNavigationBar) {
                if (mBarConfig.isNavigationAtBottom()) {
                    bottom = 0;
                } else {
                    right = 0;
                }
            } else {
                if (!mBarConfig.isNavigationAtBottom()) {
                    right = mBarConfig.getNavigationBarWidth();
                }
            }
        }
        setPadding(0, top, right, bottom);
    }

    /**
     * 注册emui3.x导航栏监听函数
     * Register emui 3 x.
     */
    private void fitsWindowsEMUI() {
        View navigationBarView = mDecorView.findViewById(IMMERSION_NAVIGATION_BAR_VIEW_ID);
        if (mBarParams.navigationBarEnable && mBarParams.navigationBarWithKitkatEnable) {
            if (navigationBarView != null) {
                EMUI3NavigationBarObserver.getInstance().addOnNavigationBarListener(this);
                EMUI3NavigationBarObserver.getInstance().register(mActivity.getApplication());
            }
        } else {
            EMUI3NavigationBarObserver.getInstance().removeOnNavigationBarListener(this);
            navigationBarView.setVisibility(View.GONE);
        }
    }

    /**
     * 更新BarConfig
     */
    private void updateBarConfig() {
        mBarConfig = new BarConfig(mActivity);
        if (!mInitialized || mIsActionBarBelowLOLLIPOP) {
            mActionBarHeight = mBarConfig.getActionBarHeight();
        }
    }

    @Override
    public void onNavigationBarChange(boolean show, NavigationBarType type) {
        View navigationBarView = mDecorView.findViewById(IMMERSION_NAVIGATION_BAR_VIEW_ID);
        if (navigationBarView != null) {
            mBarConfig = new BarConfig(mActivity);
            int bottom = mContentView.getPaddingBottom(), right = mContentView.getPaddingRight();
            if (!show) {
                //导航键隐藏了
                navigationBarView.setVisibility(View.GONE);
                bottom = 0;
                right = 0;
            } else {
                //导航键显示了
                navigationBarView.setVisibility(View.VISIBLE);
                if (checkFitsSystemWindows(mDecorView.findViewById(android.R.id.content))) {
                    bottom = 0;
                    right = 0;
                } else {
                    if (mNavigationBarHeight == 0) {
                        mNavigationBarHeight = mBarConfig.getNavigationBarHeight();
                    }
                    if (mNavigationBarWidth == 0) {
                        mNavigationBarWidth = mBarConfig.getNavigationBarWidth();
                    }
                    if (!mBarParams.hideNavigationBar) {
                        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) navigationBarView.getLayoutParams();
                        if (mBarConfig.isNavigationAtBottom()) {
                            params.gravity = Gravity.BOTTOM;
                            params.height = mNavigationBarHeight;
                            bottom = !mBarParams.fullScreen ? mNavigationBarHeight : 0;
                            right = 0;
                        } else {
                            params.gravity = Gravity.END;
                            params.width = mNavigationBarWidth;
                            bottom = 0;
                            right = !mBarParams.fullScreen ? mNavigationBarWidth : 0;
                        }
                        navigationBarView.setLayoutParams(params);
                    }
                }
            }
            setPadding(0, mContentView.getPaddingTop(), right, bottom);
        }
    }

    /**
     * Sets status bar dark font.
     * 设置状态栏字体颜色，android6.0以上
     */
    private int setStatusBarDarkFont(int uiFlags) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mBarParams.statusBarDarkFont) {
                return uiFlags | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            } else {
                return uiFlags;
            }
        } else {
            return uiFlags;
        }
    }

    /**
     * 设置导航栏图标亮色与暗色
     * Sets dark navigation icon.
     */
    private int setNavigationIconDark(int uiFlags) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (mBarParams.navigationBarDarkIcon) {
                return uiFlags | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
            } else {
                return uiFlags;
            }
        } else {
            return uiFlags;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void setStatusBarDarkFontAboveR() {
        WindowInsetsController windowInsetsController = mContentView.getWindowInsetsController();
        if (mBarParams.statusBarDarkFont) {
            if (mWindow != null) {
                unsetSystemUiFlag(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
            windowInsetsController.setSystemBarsAppearance(
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS);
        } else {
            windowInsetsController.setSystemBarsAppearance(
                    0,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void setNavigationIconDarkAboveR() {
        WindowInsetsController controller = mContentView.getWindowInsetsController();
        if (mBarParams.navigationBarDarkIcon) {
            controller.setSystemBarsAppearance(
                    WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS,
                    WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS);
        } else {
            controller.setSystemBarsAppearance(
                    0,
                    WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS);
        }
    }

    private void hideBarAboveR() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsController controller = mContentView.getWindowInsetsController();
            if (controller != null) {
                switch (mBarParams.barHide) {
                    case FLAG_HIDE_BAR:
                        controller.hide(WindowInsets.Type.statusBars());
                        controller.hide(WindowInsets.Type.navigationBars());
                        break;
                    case FLAG_HIDE_STATUS_BAR:
                        controller.hide(WindowInsets.Type.statusBars());
                        break;
                    case FLAG_HIDE_NAVIGATION_BAR:
                        controller.hide(WindowInsets.Type.navigationBars());
                        break;
                    case FLAG_SHOW_BAR:
                        controller.show(WindowInsets.Type.statusBars());
                        controller.show(WindowInsets.Type.navigationBars());
                        break;
                    default:
                        break;
                }
                controller.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
            }
        }
    }

    protected void unsetSystemUiFlag(int systemUiFlag) {
        View decorView = mWindow.getDecorView();
        decorView.setSystemUiVisibility(
                decorView.getSystemUiVisibility()
                        & ~systemUiFlag);
    }

    /**
     * 适配状态栏与布局重叠问题
     * Fits layout overlap.
     */
    private void fitsLayoutOverlap() {
        int fixHeight = 0;
        if (mBarParams.fitsLayoutOverlapEnable) {
            fixHeight = mBarConfig.getStatusBarHeight();
        }
        switch (mFitsStatusBarType) {
            case FLAG_FITS_TITLE:
                //通过设置paddingTop重新绘制标题栏高度
                setTitleBar(mActivity, fixHeight, mBarParams.titleBarView);
                break;
            case FLAG_FITS_TITLE_MARGIN_TOP:
                //通过设置marginTop重新绘制标题栏高度
                setTitleBarMarginTop(mActivity, fixHeight, mBarParams.titleBarView);
                break;
            case FLAG_FITS_STATUS:
                //通过状态栏高度动态设置状态栏布局
                setStatusBarView(mActivity, fixHeight, mBarParams.statusBarView);
                break;
            default:
                break;
        }
    }

    /**
     * 变色view
     * Transform view.
     */
    private void transformView() {
        if (mBarParams.viewMap.size() != 0) {
            Set<Map.Entry<View, Map<Integer, Integer>>> entrySet = mBarParams.viewMap.entrySet();
            for (Map.Entry<View, Map<Integer, Integer>> entry : entrySet) {
                View view = entry.getKey();
                Map<Integer, Integer> map = entry.getValue();
                Integer colorBefore = mBarParams.statusBarColor;
                Integer colorAfter = mBarParams.statusBarColorTransform;
                for (Map.Entry<Integer, Integer> integerEntry : map.entrySet()) {
                    colorBefore = integerEntry.getKey();
                    colorAfter = integerEntry.getValue();
                }
                if (view != null) {
                    if (Math.abs(mBarParams.viewAlpha - 0.0f) == 0) {
                        view.setBackgroundColor(ColorUtils.blendARGB(colorBefore, colorAfter, mBarParams.statusBarAlpha));
                    } else {
                        view.setBackgroundColor(ColorUtils.blendARGB(colorBefore, colorAfter, mBarParams.viewAlpha));
                    }
                }
            }
        }
    }

    /**
     * 取消注册emui3.x导航栏监听函数和软键盘监听
     * Cancel listener.
     */
    private void cancelListener() {
        if (mActivity != null) {
            if (mFitsKeyboard != null) {
                mFitsKeyboard.cancel();
                mFitsKeyboard = null;
            }
            EMUI3NavigationBarObserver.getInstance().removeOnNavigationBarListener(this);
            NavigationBarObserver.getInstance().removeOnNavigationBarListener(mBarParams.onNavigationBarListener);
        }
    }

    /**
     * 解决底部输入框与软键盘问题
     * Keyboard enable.
     */
    private void fitsKeyboard() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (!mIsFragment) {
                if (mBarParams.keyboardEnable) {
                    if (mFitsKeyboard == null) {
                        mFitsKeyboard = new FitsKeyboard(this);
                    }
                    mFitsKeyboard.enable(mBarParams.keyboardMode);
                } else {
                    if (mFitsKeyboard != null) {
                        mFitsKeyboard.disable();
                    }
                }
            } else {
                if (mParentBar != null) {
                    if (mParentBar.mBarParams.keyboardEnable) {
                        if (mParentBar.mFitsKeyboard == null) {
                            mParentBar.mFitsKeyboard = new FitsKeyboard(mParentBar);
                        }
                        mParentBar.mFitsKeyboard.enable(mParentBar.mBarParams.keyboardMode);
                    } else {
                        if (mParentBar.mFitsKeyboard != null) {
                            mParentBar.mFitsKeyboard.disable();
                        }
                    }
                }
            }
        }
    }

    void fitsParentBarKeyboard() {
        if (mParentBar != null && mParentBar.mFitsKeyboard != null) {
            mParentBar.mFitsKeyboard.disable();
            mParentBar.mFitsKeyboard.resetKeyboardHeight();
        }
    }

    /**
     * Gets bar params.
     *
     * @return the bar params
     */
    public BarParams getBarParams() {
        return mBarParams;
    }

    private void setPadding(int left, int top, int right, int bottom) {
        if (mContentView != null) {
            mContentView.setPadding(left, top, right, bottom);
        }
        mPaddingLeft = left;
        mPaddingTop = top;
        mPaddingRight = right;
        mPaddingBottom = bottom;
    }

    /**
     * Gets padding left.
     *
     * @return the padding left
     */
    int getPaddingLeft() {
        return mPaddingLeft;
    }

    /**
     * Gets padding top.
     *
     * @return the padding top
     */
    int getPaddingTop() {
        return mPaddingTop;
    }

    /**
     * Gets padding right.
     *
     * @return the padding right
     */
    int getPaddingRight() {
        return mPaddingRight;
    }

    /**
     * Gets padding bottom.
     *
     * @return the padding bottom
     */
    int getPaddingBottom() {
        return mPaddingBottom;
    }

    Activity getActivity() {
        return mActivity;
    }

    Window getWindow() {
        return mWindow;
    }

    Fragment getSupportFragment() {
        return mSupportFragment;
    }

    android.app.Fragment getFragment() {
        return mFragment;
    }

    /**
     * 是否是在Fragment的使用的
     *
     * @return the boolean
     */
    boolean isFragment() {
        return mIsFragment;
    }

    boolean isDialogFragment() {
        return mIsDialogFragment;
    }

    /**
     * 是否已经调用过init()方法了
     */
    boolean initialized() {
        return mInitialized;
    }

    BarConfig getBarConfig() {
        if (mBarConfig == null) {
            mBarConfig = new BarConfig(mActivity);
        }
        return mBarConfig;
    }


    int getActionBarHeight() {
        return mActionBarHeight;
    }

    /**
     * 判断手机支不支持状态栏字体变色
     * Is support status bar dark font boolean.
     *
     * @return the boolean
     */
    public static boolean isSupportStatusBarDarkFont() {
        return OSUtils.isMIUI6Later() || OSUtils.isFlymeOS4Later()
                || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
    }

    /**
     * 判断导航栏图标是否支持变色
     * Is support navigation icon dark boolean.
     *
     * @return the boolean
     */
    public static boolean isSupportNavigationIconDark() {
        return OSUtils.isMIUI6Later() || Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }

    /**
     * 为标题栏paddingTop和高度增加fixHeight的高度
     * Sets title bar.
     *
     * @param activity  the activity
     * @param fixHeight the fix height
     * @param view      the view
     */
    public static void setTitleBar(final Activity activity, int fixHeight, View... view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (activity == null) {
                return;
            }
            if (fixHeight < 0) {
                fixHeight = 0;
            }
            for (final View v : view) {
                if (v == null) {
                    continue;
                }
                final int statusBarHeight = fixHeight;
                Integer fitsHeight = (Integer) v.getTag(R.id.immersion_fits_layout_overlap);
                if (fitsHeight == null) {
                    fitsHeight = 0;
                }
                if (fitsHeight != statusBarHeight) {
                    v.setTag(R.id.immersion_fits_layout_overlap, statusBarHeight);
                    ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
                    if (layoutParams == null) {
                        layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    }
                    if (layoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT ||
                            layoutParams.height == ViewGroup.LayoutParams.MATCH_PARENT) {
                        final ViewGroup.LayoutParams finalLayoutParams = layoutParams;
                        final Integer finalFitsHeight = fitsHeight;
                        v.post(new Runnable() {
                            @Override
                            public void run() {
                                finalLayoutParams.height = v.getHeight() + statusBarHeight - finalFitsHeight;
                                v.setPadding(v.getPaddingLeft(),
                                        v.getPaddingTop() + statusBarHeight - finalFitsHeight,
                                        v.getPaddingRight(),
                                        v.getPaddingBottom());
                                v.setLayoutParams(finalLayoutParams);
                            }
                        });
                    } else {
                        layoutParams.height += statusBarHeight - fitsHeight;
                        v.setPadding(v.getPaddingLeft(), v.getPaddingTop() + statusBarHeight - fitsHeight,
                                v.getPaddingRight(), v.getPaddingBottom());
                        v.setLayoutParams(layoutParams);
                    }
                }
            }
        }
    }

    /**
     * 为标题栏paddingTop和高度增加状态栏的高度
     * Sets title bar.
     *
     * @param activity the activity
     * @param view     the view
     */
    public static void setTitleBar(final Activity activity, View... view) {
        setTitleBar(activity, getStatusBarHeight(activity), view);
    }

    public static void setTitleBar(Fragment fragment, int fixHeight, View... view) {
        if (fragment == null) {
            return;
        }
        setTitleBar(fragment.getActivity(), fixHeight, view);
    }

    public static void setTitleBar(Fragment fragment, View... view) {
        if (fragment == null) {
            return;
        }
        setTitleBar(fragment.getActivity(), view);
    }

    public static void setTitleBar(android.app.Fragment fragment, int fixHeight, View... view) {
        if (fragment == null) {
            return;
        }
        setTitleBar(fragment.getActivity(), fixHeight, view);
    }

    public static void setTitleBar(android.app.Fragment fragment, View... view) {
        if (fragment == null) {
            return;
        }
        setTitleBar(fragment.getActivity(), view);
    }

    /**
     * 为标题栏marginTop增加fixHeight的高度
     * Sets title bar margin top.
     *
     * @param activity  the activity
     * @param fixHeight the fix height
     * @param view      the view
     */
    public static void setTitleBarMarginTop(Activity activity, int fixHeight, View... view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (activity == null) {
                return;
            }
            if (fixHeight < 0) {
                fixHeight = 0;
            }
            for (View v : view) {
                if (v == null) {
                    continue;
                }
                Integer fitsHeight = (Integer) v.getTag(R.id.immersion_fits_layout_overlap);
                if (fitsHeight == null) {
                    fitsHeight = 0;
                }
                if (fitsHeight != fixHeight) {
                    v.setTag(R.id.immersion_fits_layout_overlap, fixHeight);
                    ViewGroup.LayoutParams lp = v.getLayoutParams();
                    if (lp == null) {
                        lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    }
                    ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) lp;
                    layoutParams.setMargins(layoutParams.leftMargin,
                            layoutParams.topMargin + fixHeight - fitsHeight,
                            layoutParams.rightMargin,
                            layoutParams.bottomMargin);
                    v.setLayoutParams(layoutParams);
                }
            }
        }
    }

    /**
     * 为标题栏marginTop增加状态栏的高度
     * Sets title bar margin top.
     *
     * @param activity the activity
     * @param view     the view
     */
    public static void setTitleBarMarginTop(Activity activity, View... view) {
        setTitleBarMarginTop(activity, getStatusBarHeight(activity), view);
    }

    public static void setTitleBarMarginTop(Fragment fragment, int fixHeight, View... view) {
        if (fragment == null) {
            return;
        }
        setTitleBarMarginTop(fragment.getActivity(), fixHeight, view);
    }

    public static void setTitleBarMarginTop(Fragment fragment, View... view) {
        if (fragment == null) {
            return;
        }
        setTitleBarMarginTop(fragment.getActivity(), view);
    }

    public static void setTitleBarMarginTop(android.app.Fragment fragment, int fixHeight, View...
            view) {
        if (fragment == null) {
            return;
        }
        setTitleBarMarginTop(fragment.getActivity(), fixHeight, view);
    }

    public static void setTitleBarMarginTop(android.app.Fragment fragment, View... view) {
        if (fragment == null) {
            return;
        }
        setTitleBarMarginTop(fragment.getActivity(), view);
    }

    /**
     * 单独在标题栏的位置增加view，高度为fixHeight的高度
     * Sets status bar view.
     *
     * @param activity  the activity
     * @param fixHeight the fix height
     * @param view      the view
     */
    public static void setStatusBarView(Activity activity, int fixHeight, View... view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (activity == null) {
                return;
            }
            if (fixHeight < 0) {
                fixHeight = 0;
            }
            for (View v : view) {
                if (v == null) {
                    continue;
                }
                Integer fitsHeight = (Integer) v.getTag(R.id.immersion_fits_layout_overlap);
                if (fitsHeight == null) {
                    fitsHeight = 0;
                }
                if (fitsHeight != fixHeight) {
                    v.setTag(R.id.immersion_fits_layout_overlap, fixHeight);
                    ViewGroup.LayoutParams lp = v.getLayoutParams();
                    if (lp == null) {
                        lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
                    }
                    lp.height = fixHeight;
                    v.setLayoutParams(lp);
                }
            }
        }
    }

    /**
     * 单独在标题栏的位置增加view，高度为状态栏的高度
     * Sets status bar view.
     *
     * @param activity the activity
     * @param view     the view
     */
    public static void setStatusBarView(Activity activity, View... view) {
        setStatusBarView(activity, getStatusBarHeight(activity), view);
    }

    public static void setStatusBarView(Fragment fragment, int fixHeight, View... view) {
        if (fragment == null) {
            return;
        }
        setStatusBarView(fragment.getActivity(), fixHeight, view);
    }

    public static void setStatusBarView(Fragment fragment, View... view) {
        if (fragment == null) {
            return;
        }
        setStatusBarView(fragment.getActivity(), view);
    }

    public static void setStatusBarView(android.app.Fragment fragment, int fixHeight, View...
            view) {
        if (fragment == null) {
            return;
        }
        setStatusBarView(fragment.getActivity(), fixHeight, view);
    }

    public static void setStatusBarView(android.app.Fragment fragment, View... view) {
        if (fragment == null) {
            return;
        }
        setStatusBarView(fragment.getActivity(), view);
    }

    /**
     * 调用系统view的setFitsSystemWindows方法
     * Sets fits system windows.
     *
     * @param activity        the activity
     * @param applySystemFits the apply system fits
     */
    public static void setFitsSystemWindows(Activity activity, boolean applySystemFits) {
        if (activity == null) {
            return;
        }
        setFitsSystemWindows(((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0), applySystemFits);
    }

    public static void setFitsSystemWindows(Activity activity) {
        setFitsSystemWindows(activity, true);
    }

    public static void setFitsSystemWindows(Fragment fragment, boolean applySystemFits) {
        if (fragment == null) {
            return;
        }
        setFitsSystemWindows(fragment.getActivity(), applySystemFits);
    }

    public static void setFitsSystemWindows(Fragment fragment) {
        if (fragment == null) {
            return;
        }
        setFitsSystemWindows(fragment.getActivity());
    }

    public static void setFitsSystemWindows(android.app.Fragment fragment, boolean applySystemFits) {
        if (fragment == null) {
            return;
        }
        setFitsSystemWindows(fragment.getActivity(), applySystemFits);
    }

    public static void setFitsSystemWindows(android.app.Fragment fragment) {
        if (fragment == null) {
            return;
        }
        setFitsSystemWindows(fragment.getActivity());
    }

    private static void setFitsSystemWindows(View view, boolean applySystemFits) {
        if (view == null) {
            return;
        }
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            if (viewGroup instanceof DrawerLayout) {
                setFitsSystemWindows(viewGroup.getChildAt(0), applySystemFits);
            } else {
                viewGroup.setFitsSystemWindows(applySystemFits);
                viewGroup.setClipToPadding(true);
            }
        } else {
            view.setFitsSystemWindows(applySystemFits);
        }
    }

    /**
     * 检查布局根节点是否使用了android:fitsSystemWindows="true"属性
     * Check fits system windows boolean.
     *
     * @param view the view
     * @return the boolean
     */
    public static boolean checkFitsSystemWindows(View view) {
        if (view == null) {
            return false;
        }
        if (view.getFitsSystemWindows()) {
            return true;
        }
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0, count = viewGroup.getChildCount(); i < count; i++) {
                View childView = viewGroup.getChildAt(i);
                if (childView instanceof DrawerLayout) {
                    if (checkFitsSystemWindows(childView)) {
                        return true;
                    }
                }
                if (childView.getFitsSystemWindows()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Has navigtion bar boolean.
     * 判断是否存在导航栏
     *
     * @param activity the activity
     * @return the boolean
     */
    @TargetApi(14)
    public static boolean hasNavigationBar(@NonNull Activity activity) {
        BarConfig config = new BarConfig(activity);
        return config.hasNavigationBar();
    }

    @TargetApi(14)
    public static boolean hasNavigationBar(@NonNull Fragment fragment) {
        if (fragment.getActivity() == null) {
            return false;
        }
        return hasNavigationBar(fragment.getActivity());
    }

    @TargetApi(14)
    public static boolean hasNavigationBar(@NonNull android.app.Fragment fragment) {
        if (fragment.getActivity() == null) {
            return false;
        }
        return hasNavigationBar(fragment.getActivity());
    }

    @TargetApi(14)
    public static boolean hasNavigationBar(@NonNull Context context) {
        return getNavigationBarHeight(context) > 0;
    }

    /**
     * Gets navigation bar height.
     * 获得导航栏的高度
     *
     * @param activity the activity
     * @return the navigation bar height
     */
    @TargetApi(14)
    public static int getNavigationBarHeight(@NonNull Activity activity) {
        BarConfig config = new BarConfig(activity);
        return config.getNavigationBarHeight();
    }

    @TargetApi(14)
    public static int getNavigationBarHeight(@NonNull Fragment fragment) {
        if (fragment.getActivity() == null) {
            return 0;
        }
        return getNavigationBarHeight(fragment.getActivity());
    }

    @TargetApi(14)
    public static int getNavigationBarHeight(@NonNull android.app.Fragment fragment) {
        if (fragment.getActivity() == null) {
            return 0;
        }
        return getNavigationBarHeight(fragment.getActivity());
    }

    @TargetApi(14)
    public static int getNavigationBarHeight(@NonNull Context context) {
        GestureUtils.GestureBean bean = GestureUtils.getGestureBean(context);
        if (bean.isGesture && !bean.checkNavigation) {
            return 0;
        } else {
            return BarConfig.getNavigationBarHeightInternal(context);
        }
    }

    /**
     * Gets navigation bar width.
     * 获得导航栏的宽度
     *
     * @param activity the activity
     * @return the navigation bar width
     */
    @TargetApi(14)
    public static int getNavigationBarWidth(@NonNull Activity activity) {
        BarConfig config = new BarConfig(activity);
        return config.getNavigationBarWidth();
    }

    @TargetApi(14)
    public static int getNavigationBarWidth(@NonNull Fragment fragment) {
        if (fragment.getActivity() == null) {
            return 0;
        }
        return getNavigationBarWidth(fragment.getActivity());
    }

    @TargetApi(14)
    public static int getNavigationBarWidth(@NonNull android.app.Fragment fragment) {
        if (fragment.getActivity() == null) {
            return 0;
        }
        return getNavigationBarWidth(fragment.getActivity());
    }

    @TargetApi(14)
    public static int getNavigationBarWidth(@NonNull Context context) {
        GestureUtils.GestureBean bean = GestureUtils.getGestureBean(context);
        if (bean.isGesture && !bean.checkNavigation) {
            return 0;
        } else {
            return BarConfig.getNavigationBarWidthInternal(context);
        }
    }

    /**
     * Is navigation at bottom boolean.
     * 判断导航栏是否在底部
     *
     * @param activity the activity
     * @return the boolean
     */
    @TargetApi(14)
    public static boolean isNavigationAtBottom(@NonNull Activity activity) {
        BarConfig config = new BarConfig(activity);
        return config.isNavigationAtBottom();
    }

    @TargetApi(14)
    public static boolean isNavigationAtBottom(@NonNull Fragment fragment) {
        if (fragment.getActivity() == null) {
            return false;
        }
        return isNavigationAtBottom(fragment.getActivity());
    }

    @TargetApi(14)
    public static boolean isNavigationAtBottom(@NonNull android.app.Fragment fragment) {
        if (fragment.getActivity() == null) {
            return false;
        }
        return isNavigationAtBottom(fragment.getActivity());
    }

    /**
     * Gets status bar height.
     * 或得状态栏的高度
     *
     * @param activity the activity
     * @return the status bar height
     */
    @TargetApi(14)
    public static int getStatusBarHeight(@NonNull Activity activity) {
        BarConfig config = new BarConfig(activity);
        return config.getStatusBarHeight();
    }

    @TargetApi(14)
    public static int getStatusBarHeight(@NonNull Fragment fragment) {
        if (fragment.getActivity() == null) {
            return 0;
        }
        return getStatusBarHeight(fragment.getActivity());
    }

    @TargetApi(14)
    public static int getStatusBarHeight(@NonNull android.app.Fragment fragment) {
        if (fragment.getActivity() == null) {
            return 0;
        }
        return getStatusBarHeight(fragment.getActivity());
    }

    @TargetApi(14)
    public static int getStatusBarHeight(@NonNull Context context) {
        return BarConfig.getInternalDimensionSize(context, Constants.IMMERSION_STATUS_BAR_HEIGHT);
    }

    /**
     * Gets action bar height.
     * 或得ActionBar得高度
     *
     * @param activity the activity
     * @return the action bar height
     */
    @TargetApi(14)
    public static int getActionBarHeight(@NonNull Activity activity) {
        BarConfig config = new BarConfig(activity);
        return config.getActionBarHeight();
    }

    @TargetApi(14)
    public static int getActionBarHeight(@NonNull Fragment fragment) {
        if (fragment.getActivity() == null) {
            return 0;
        }
        return getActionBarHeight(fragment.getActivity());
    }

    @TargetApi(14)
    public static int getActionBarHeight(@NonNull android.app.Fragment fragment) {
        if (fragment.getActivity() == null) {
            return 0;
        }
        return getActionBarHeight(fragment.getActivity());
    }

    /**
     * 是否是刘海屏
     * Has notch screen boolean.
     * e.g:getWindow().getDecorView().post(() -> ImmersionBar.hasNotchScreen(this));
     *
     * @param activity the activity
     * @return the boolean
     */
    public static boolean hasNotchScreen(@NonNull Activity activity) {
        return NotchUtils.hasNotchScreen(activity);
    }

    public static boolean hasNotchScreen(@NonNull Fragment fragment) {
        if (fragment.getActivity() == null) {
            return false;
        }
        return hasNotchScreen(fragment.getActivity());
    }

    public static boolean hasNotchScreen(@NonNull android.app.Fragment fragment) {
        if (fragment.getActivity() == null) {
            return false;
        }
        return hasNotchScreen(fragment.getActivity());
    }

    /**
     * 是否是刘海屏
     * Has notch screen boolean.
     *
     * @param view the view
     * @return the boolean
     */
    public static boolean hasNotchScreen(@NonNull View view) {
        return NotchUtils.hasNotchScreen(view);
    }

    /**
     * 刘海屏高度
     * Notch height int.
     * e.g: getWindow().getDecorView().post(() -> ImmersionBar.getNotchHeight(this));
     *
     * @param activity the activity
     * @return the int
     */
    public static int getNotchHeight(@NonNull Activity activity) {
        return NotchUtils.getNotchHeight(activity);
    }

    public static int getNotchHeight(@NonNull Fragment fragment) {
        if (fragment.getActivity() == null) {
            return 0;
        }
        return getNotchHeight(fragment.getActivity());
    }

    public static int getNotchHeight(@NonNull android.app.Fragment fragment) {
        if (fragment.getActivity() == null) {
            return 0;
        }
        return getNotchHeight(fragment.getActivity());
    }

    public static void getNotchHeight(@NonNull Activity activity, NotchCallback callback) {
        NotchUtils.getNotchHeight(activity, callback);
    }

    public static void getNotchHeight(@NonNull Fragment fragment, NotchCallback callback) {
        if (fragment.getActivity() == null) {
            return;
        }
        getNotchHeight(fragment.getActivity(), callback);
    }

    public static void getNotchHeight(@NonNull android.app.Fragment fragment, NotchCallback callback) {
        if (fragment.getActivity() == null) {
            return;
        }
        getNotchHeight(fragment.getActivity(), callback);
    }

    /**
     * 隐藏状态栏
     * Hide status bar.
     *
     * @param window the window
     */
    public static void hideStatusBar(@NonNull Window window) {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 显示状态栏
     * Show status bar.
     *
     * @param window the window
     */
    public static void showStatusBar(@NonNull Window window) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 是否是手势
     *
     * @param context Context
     * @return the boolean
     */
    public static boolean isGesture(Context context) {
        return GestureUtils.getGestureBean(context).isGesture;
    }

    /**
     * 是否是手势
     *
     * @param fragment Fragment
     * @return the boolean
     */
    public static boolean isGesture(Fragment fragment) {
        Context context = fragment.getContext();
        if (context == null) return false;
        return isGesture(context);
    }

    /**
     * 是否是手势
     *
     * @param fragment android.app.Fragment
     * @return the boolean
     */
    public static boolean isGesture(android.app.Fragment fragment) {
        Context context = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            context = fragment.getContext();
        }
        if (context == null) return false;
        return isGesture(context);
    }

    /**
     * 在Activity里初始化
     * Instantiates a new Immersion bar.
     *
     * @param activity the activity
     */
    ImmersionBar(Activity activity) {
        mActivity = activity;
        initCommonParameter(mActivity.getWindow());
    }

    /**
     * 在Fragment里初始化
     * Instantiates a new Immersion bar.
     *
     * @param fragment the fragment
     */
    ImmersionBar(Fragment fragment) {
        mIsFragment = true;
        mActivity = fragment.getActivity();
        mSupportFragment = fragment;
        checkInitWithActivity();
        initCommonParameter(mActivity.getWindow());
    }

    /**
     * 在Fragment里初始化
     * Instantiates a new Immersion bar.
     *
     * @param fragment the fragment
     */
    ImmersionBar(android.app.Fragment fragment) {
        mIsFragment = true;
        mActivity = fragment.getActivity();
        mFragment = fragment;
        checkInitWithActivity();
        initCommonParameter(mActivity.getWindow());
    }

    /**
     * 在dialogFragment里使用
     * Instantiates a new Immersion bar.
     *
     * @param dialogFragment the dialog fragment
     */
    ImmersionBar(DialogFragment dialogFragment) {
        mIsDialog = true;
        mIsDialogFragment = true;
        mActivity = dialogFragment.getActivity();
        mSupportFragment = dialogFragment;
        mDialog = dialogFragment.getDialog();
        checkInitWithActivity();
        initCommonParameter(mDialog.getWindow());
    }

    /**
     * 在dialogFragment里使用
     * Instantiates a new Immersion bar.
     *
     * @param dialogFragment the dialog fragment
     */
    ImmersionBar(android.app.DialogFragment dialogFragment) {
        mIsDialog = true;
        mIsDialogFragment = true;
        mActivity = dialogFragment.getActivity();
        mFragment = dialogFragment;
        mDialog = dialogFragment.getDialog();
        checkInitWithActivity();
        initCommonParameter(mDialog.getWindow());
    }

    /**
     * 在Dialog里初始化
     * Instantiates a new Immersion bar.
     *
     * @param activity the activity
     * @param dialog   the dialog
     */
    ImmersionBar(Activity activity, Dialog dialog) {
        mIsDialog = true;
        mActivity = activity;
        mDialog = dialog;
        checkInitWithActivity();
        initCommonParameter(mDialog.getWindow());
    }

    /**
     * 检查是否已经在Activity中初始化了，未初始化则自动初始化
     */
    private void checkInitWithActivity() {
        if (mParentBar == null) {
            mParentBar = with(mActivity);
        }
        if (mParentBar != null && !mParentBar.mInitialized) {
            mParentBar.init();
        }
    }

    /**
     * 初始化共同参数
     *
     * @param window window
     */
    private void initCommonParameter(Window window) {
        mWindow = window;
        mBarParams = new BarParams();
        mDecorView = (ViewGroup) mWindow.getDecorView();
        mContentView = mDecorView.findViewById(android.R.id.content);
    }

    /**
     * 透明状态栏，默认透明
     *
     * @return the immersion bar
     */
    public ImmersionBar transparentStatusBar() {
        mBarParams.statusBarColor = Color.TRANSPARENT;
        return this;
    }

    /**
     * 透明导航栏，默认黑色
     *
     * @return the immersion bar
     */
    public ImmersionBar transparentNavigationBar() {
        mBarParams.navigationBarColor = Color.TRANSPARENT;
        mBarParams.fullScreen = true;
        return this;
    }

    /**
     * 透明状态栏和导航栏
     *
     * @return the immersion bar
     */
    public ImmersionBar transparentBar() {
        mBarParams.statusBarColor = Color.TRANSPARENT;
        mBarParams.navigationBarColor = Color.TRANSPARENT;
        mBarParams.fullScreen = true;
        return this;
    }

    /**
     * 状态栏颜色
     *
     * @param statusBarColor 状态栏颜色，资源文件（R.color.xxx）
     * @return the immersion bar
     */
    public ImmersionBar statusBarColor(@ColorRes int statusBarColor) {
        return this.statusBarColorInt(ContextCompat.getColor(mActivity, statusBarColor));
    }

    /**
     * 状态栏颜色
     *
     * @param statusBarColor 状态栏颜色，资源文件（R.color.xxx）
     * @param alpha          the alpha  透明度
     * @return the immersion bar
     */
    public ImmersionBar statusBarColor(@ColorRes int statusBarColor,
                                       @FloatRange(from = 0f, to = 1f) float alpha) {
        return this.statusBarColorInt(ContextCompat.getColor(mActivity, statusBarColor), alpha);
    }

    /**
     * 状态栏颜色
     *
     * @param statusBarColor          状态栏颜色，资源文件（R.color.xxx）
     * @param statusBarColorTransform the status bar color transform 状态栏变换后的颜色
     * @param alpha                   the alpha  透明度
     * @return the immersion bar
     */
    public ImmersionBar statusBarColor(@ColorRes int statusBarColor,
                                       @ColorRes int statusBarColorTransform,
                                       @FloatRange(from = 0f, to = 1f) float alpha) {
        return this.statusBarColorInt(ContextCompat.getColor(mActivity, statusBarColor),
                ContextCompat.getColor(mActivity, statusBarColorTransform),
                alpha);
    }

    /**
     * 状态栏颜色
     * Status bar color int immersion bar.
     *
     * @param statusBarColor the status bar color
     * @return the immersion bar
     */
    public ImmersionBar statusBarColor(String statusBarColor) {
        return this.statusBarColorInt(Color.parseColor(statusBarColor));
    }

    /**
     * 状态栏颜色
     *
     * @param statusBarColor 状态栏颜色
     * @param alpha          the alpha  透明度
     * @return the immersion bar
     */
    public ImmersionBar statusBarColor(String statusBarColor,
                                       @FloatRange(from = 0f, to = 1f) float alpha) {
        return this.statusBarColorInt(Color.parseColor(statusBarColor), alpha);
    }

    /**
     * 状态栏颜色
     *
     * @param statusBarColor          状态栏颜色
     * @param statusBarColorTransform the status bar color transform 状态栏变换后的颜色
     * @param alpha                   the alpha  透明度
     * @return the immersion bar
     */
    public ImmersionBar statusBarColor(String statusBarColor,
                                       String statusBarColorTransform,
                                       @FloatRange(from = 0f, to = 1f) float alpha) {
        return this.statusBarColorInt(Color.parseColor(statusBarColor),
                Color.parseColor(statusBarColorTransform),
                alpha);
    }

    /**
     * 状态栏颜色
     *
     * @param statusBarColor 状态栏颜色，资源文件（R.color.xxx）
     * @return the immersion bar
     */
    public ImmersionBar statusBarColorInt(@ColorInt int statusBarColor) {
        mBarParams.statusBarColor = statusBarColor;
        return this;
    }

    /**
     * 状态栏颜色
     *
     * @param statusBarColor 状态栏颜色，资源文件（R.color.xxx）
     * @param alpha          the alpha  透明度
     * @return the immersion bar
     */
    public ImmersionBar statusBarColorInt(@ColorInt int statusBarColor,
                                          @FloatRange(from = 0f, to = 1f) float alpha) {
        mBarParams.statusBarColor = statusBarColor;
        mBarParams.statusBarAlpha = alpha;
        return this;
    }

    /**
     * 状态栏颜色
     *
     * @param statusBarColor          状态栏颜色，资源文件（R.color.xxx）
     * @param statusBarColorTransform the status bar color transform 状态栏变换后的颜色
     * @param alpha                   the alpha  透明度
     * @return the immersion bar
     */
    public ImmersionBar statusBarColorInt(@ColorInt int statusBarColor,
                                          @ColorInt int statusBarColorTransform,
                                          @FloatRange(from = 0f, to = 1f) float alpha) {
        mBarParams.statusBarColor = statusBarColor;
        mBarParams.statusBarColorTransform = statusBarColorTransform;
        mBarParams.statusBarAlpha = alpha;
        return this;
    }

    /**
     * 导航栏颜色
     *
     * @param navigationBarColor the navigation bar color 导航栏颜色
     * @return the immersion bar
     */
    public ImmersionBar navigationBarColor(@ColorRes int navigationBarColor) {
        return this.navigationBarColorInt(ContextCompat.getColor(mActivity, navigationBarColor));
    }

    /**
     * 导航栏颜色
     *
     * @param navigationBarColor the navigation bar color 导航栏颜色
     * @param navigationAlpha    the navigation alpha 透明度
     * @return the immersion bar
     */
    public ImmersionBar navigationBarColor(@ColorRes int navigationBarColor,
                                           @FloatRange(from = 0f, to = 1f) float navigationAlpha) {
        return this.navigationBarColorInt(ContextCompat.getColor(mActivity, navigationBarColor), navigationAlpha);
    }

    /**
     * 导航栏颜色
     *
     * @param navigationBarColor          the navigation bar color 导航栏颜色
     * @param navigationBarColorTransform the navigation bar color transform  导航栏变色后的颜色
     * @param navigationAlpha             the navigation alpha  透明度
     * @return the immersion bar
     */
    public ImmersionBar navigationBarColor(@ColorRes int navigationBarColor,
                                           @ColorRes int navigationBarColorTransform,
                                           @FloatRange(from = 0f, to = 1f) float navigationAlpha) {
        return this.navigationBarColorInt(ContextCompat.getColor(mActivity, navigationBarColor),
                ContextCompat.getColor(mActivity, navigationBarColorTransform), navigationAlpha);
    }

    /**
     * 导航栏颜色
     *
     * @param navigationBarColor the navigation bar color 导航栏颜色
     * @return the immersion bar
     */
    public ImmersionBar navigationBarColor(String navigationBarColor) {
        return this.navigationBarColorInt(Color.parseColor(navigationBarColor));
    }

    /**
     * 导航栏颜色
     *
     * @param navigationBarColor the navigation bar color 导航栏颜色
     * @param navigationAlpha    the navigation alpha 透明度
     * @return the immersion bar
     */
    public ImmersionBar navigationBarColor(String navigationBarColor,
                                           @FloatRange(from = 0f, to = 1f) float navigationAlpha) {
        return this.navigationBarColorInt(Color.parseColor(navigationBarColor), navigationAlpha);
    }

    /**
     * 导航栏颜色
     *
     * @param navigationBarColor          the navigation bar color 导航栏颜色
     * @param navigationBarColorTransform the navigation bar color transform  导航栏变色后的颜色
     * @param navigationAlpha             the navigation alpha  透明度
     * @return the immersion bar
     */
    public ImmersionBar navigationBarColor(String navigationBarColor,
                                           String navigationBarColorTransform,
                                           @FloatRange(from = 0f, to = 1f) float navigationAlpha) {
        return this.navigationBarColorInt(Color.parseColor(navigationBarColor),
                Color.parseColor(navigationBarColorTransform), navigationAlpha);
    }

    /**
     * 导航栏颜色
     *
     * @param navigationBarColor the navigation bar color 导航栏颜色
     * @return the immersion bar
     */
    public ImmersionBar navigationBarColorInt(@ColorInt int navigationBarColor) {
        mBarParams.navigationBarColor = navigationBarColor;
        return this;
    }

    /**
     * 导航栏颜色
     *
     * @param navigationBarColor the navigation bar color 导航栏颜色
     * @param navigationAlpha    the navigation alpha 透明度
     * @return the immersion bar
     */
    public ImmersionBar navigationBarColorInt(@ColorInt int navigationBarColor,
                                              @FloatRange(from = 0f, to = 1f) float navigationAlpha) {
        mBarParams.navigationBarColor = navigationBarColor;
        mBarParams.navigationBarAlpha = navigationAlpha;
        return this;
    }

    /**
     * 导航栏颜色
     *
     * @param navigationBarColor          the navigation bar color 导航栏颜色
     * @param navigationBarColorTransform the navigation bar color transform  导航栏变色后的颜色
     * @param navigationAlpha             the navigation alpha  透明度
     * @return the immersion bar
     */
    public ImmersionBar navigationBarColorInt(@ColorInt int navigationBarColor,
                                              @ColorInt int navigationBarColorTransform,
                                              @FloatRange(from = 0f, to = 1f) float navigationAlpha) {
        mBarParams.navigationBarColor = navigationBarColor;
        mBarParams.navigationBarColorTransform = navigationBarColorTransform;
        mBarParams.navigationBarAlpha = navigationAlpha;
        return this;
    }

    /**
     * 状态栏和导航栏颜色
     *
     * @param barColor the bar color
     * @return the immersion bar
     */
    public ImmersionBar barColor(@ColorRes int barColor) {
        return this.barColorInt(ContextCompat.getColor(mActivity, barColor));
    }

    /**
     * 状态栏和导航栏颜色
     *
     * @param barColor the bar color
     * @param barAlpha the bar alpha
     * @return the immersion bar
     */
    public ImmersionBar barColor(@ColorRes int barColor, @FloatRange(from = 0f, to = 1f) float barAlpha) {
        return this.barColorInt(ContextCompat.getColor(mActivity, barColor), barColor);
    }

    /**
     * 状态栏和导航栏颜色
     *
     * @param barColor          the bar color
     * @param barColorTransform the bar color transform
     * @param barAlpha          the bar alpha
     * @return the immersion bar
     */
    public ImmersionBar barColor(@ColorRes int barColor,
                                 @ColorRes int barColorTransform,
                                 @FloatRange(from = 0f, to = 1f) float barAlpha) {
        return this.barColorInt(ContextCompat.getColor(mActivity, barColor),
                ContextCompat.getColor(mActivity, barColorTransform), barAlpha);
    }

    /**
     * 状态栏和导航栏颜色
     *
     * @param barColor the bar color
     * @return the immersion bar
     */
    public ImmersionBar barColor(String barColor) {
        return this.barColorInt(Color.parseColor(barColor));
    }

    /**
     * 状态栏和导航栏颜色
     *
     * @param barColor the bar color
     * @param barAlpha the bar alpha
     * @return the immersion bar
     */
    public ImmersionBar barColor(String barColor, @FloatRange(from = 0f, to = 1f) float barAlpha) {
        return this.barColorInt(Color.parseColor(barColor), barAlpha);
    }

    /**
     * 状态栏和导航栏颜色
     *
     * @param barColor          the bar color
     * @param barColorTransform the bar color transform
     * @param barAlpha          the bar alpha
     * @return the immersion bar
     */
    public ImmersionBar barColor(String barColor,
                                 String barColorTransform,
                                 @FloatRange(from = 0f, to = 1f) float barAlpha) {
        return this.barColorInt(Color.parseColor(barColor), Color.parseColor(barColorTransform), barAlpha);
    }

    /**
     * 状态栏和导航栏颜色
     *
     * @param barColor the bar color
     * @return the immersion bar
     */
    public ImmersionBar barColorInt(@ColorInt int barColor) {
        mBarParams.statusBarColor = barColor;
        mBarParams.navigationBarColor = barColor;
        return this;
    }

    /**
     * 状态栏和导航栏颜色
     *
     * @param barColor the bar color
     * @param barAlpha the bar alpha
     * @return the immersion bar
     */
    public ImmersionBar barColorInt(@ColorInt int barColor, @FloatRange(from = 0f, to = 1f) float barAlpha) {
        mBarParams.statusBarColor = barColor;
        mBarParams.navigationBarColor = barColor;
        mBarParams.statusBarAlpha = barAlpha;
        mBarParams.navigationBarAlpha = barAlpha;
        return this;
    }

    /**
     * 状态栏和导航栏颜色
     *
     * @param barColor          the bar color
     * @param barColorTransform the bar color transform
     * @param barAlpha          the bar alpha
     * @return the immersion bar
     */
    public ImmersionBar barColorInt(@ColorInt int barColor,
                                    @ColorInt int barColorTransform,
                                    @FloatRange(from = 0f, to = 1f) float barAlpha) {
        mBarParams.statusBarColor = barColor;
        mBarParams.navigationBarColor = barColor;

        mBarParams.statusBarColorTransform = barColorTransform;
        mBarParams.navigationBarColorTransform = barColorTransform;

        mBarParams.statusBarAlpha = barAlpha;
        mBarParams.navigationBarAlpha = barAlpha;
        return this;
    }


    /**
     * 状态栏根据透明度最后变换成的颜色
     *
     * @param statusBarColorTransform the status bar color transform
     * @return the immersion bar
     */
    public ImmersionBar statusBarColorTransform(@ColorRes int statusBarColorTransform) {
        return this.statusBarColorTransformInt(ContextCompat.getColor(mActivity, statusBarColorTransform));
    }

    /**
     * 状态栏根据透明度最后变换成的颜色
     *
     * @param statusBarColorTransform the status bar color transform
     * @return the immersion bar
     */
    public ImmersionBar statusBarColorTransform(String statusBarColorTransform) {
        return this.statusBarColorTransformInt(Color.parseColor(statusBarColorTransform));
    }

    /**
     * 状态栏根据透明度最后变换成的颜色
     *
     * @param statusBarColorTransform the status bar color transform
     * @return the immersion bar
     */
    public ImmersionBar statusBarColorTransformInt(@ColorInt int statusBarColorTransform) {
        mBarParams.statusBarColorTransform = statusBarColorTransform;
        return this;
    }

    /**
     * 导航栏根据透明度最后变换成的颜色
     *
     * @param navigationBarColorTransform the m navigation bar color transform
     * @return the immersion bar
     */
    public ImmersionBar navigationBarColorTransform(@ColorRes int navigationBarColorTransform) {
        return this.navigationBarColorTransformInt(ContextCompat.getColor(mActivity, navigationBarColorTransform));
    }

    /**
     * 导航栏根据透明度最后变换成的颜色
     *
     * @param navigationBarColorTransform the m navigation bar color transform
     * @return the immersion bar
     */
    public ImmersionBar navigationBarColorTransform(String navigationBarColorTransform) {
        return this.navigationBarColorTransformInt(Color.parseColor(navigationBarColorTransform));
    }

    /**
     * 导航栏根据透明度最后变换成的颜色
     *
     * @param navigationBarColorTransform the m navigation bar color transform
     * @return the immersion bar
     */
    public ImmersionBar navigationBarColorTransformInt(@ColorInt int navigationBarColorTransform) {
        mBarParams.navigationBarColorTransform = navigationBarColorTransform;
        return this;
    }

    /**
     * 状态栏和导航栏根据透明度最后变换成的颜色
     *
     * @param barColorTransform the bar color transform
     * @return the immersion bar
     */
    public ImmersionBar barColorTransform(@ColorRes int barColorTransform) {
        return this.barColorTransformInt(ContextCompat.getColor(mActivity, barColorTransform));
    }

    /**
     * 状态栏和导航栏根据透明度最后变换成的颜色
     *
     * @param barColorTransform the bar color transform
     * @return the immersion bar
     */
    public ImmersionBar barColorTransform(String barColorTransform) {
        return this.barColorTransformInt(Color.parseColor(barColorTransform));
    }

    /**
     * 状态栏和导航栏根据透明度最后变换成的颜色
     *
     * @param barColorTransform the bar color transform
     * @return the immersion bar
     */
    public ImmersionBar barColorTransformInt(@ColorInt int barColorTransform) {
        mBarParams.statusBarColorTransform = barColorTransform;
        mBarParams.navigationBarColorTransform = barColorTransform;
        return this;
    }

    /**
     * Add 颜色变换支持View
     *
     * @param view the view
     * @return the immersion bar
     */
    public ImmersionBar addViewSupportTransformColor(View view) {
        return this.addViewSupportTransformColorInt(view, mBarParams.statusBarColorTransform);
    }

    /**
     * Add 颜色变换支持View
     *
     * @param view                    the view
     * @param viewColorAfterTransform the view color after transform
     * @return the immersion bar
     */
    public ImmersionBar addViewSupportTransformColor(View view, @ColorRes int viewColorAfterTransform) {
        return this.addViewSupportTransformColorInt(view, ContextCompat.getColor(mActivity, viewColorAfterTransform));
    }

    /**
     * Add 颜色变换支持View
     *
     * @param view                     the view
     * @param viewColorBeforeTransform the view color before transform
     * @param viewColorAfterTransform  the view color after transform
     * @return the immersion bar
     */
    public ImmersionBar addViewSupportTransformColor(View view, @ColorRes int viewColorBeforeTransform,
                                                     @ColorRes int viewColorAfterTransform) {
        return this.addViewSupportTransformColorInt(view,
                ContextCompat.getColor(mActivity, viewColorBeforeTransform),
                ContextCompat.getColor(mActivity, viewColorAfterTransform));
    }

    /**
     * Add 颜色变换支持View
     *
     * @param view                    the view
     * @param viewColorAfterTransform the view color after transform
     * @return the immersion bar
     */
    public ImmersionBar addViewSupportTransformColor(View view, String viewColorAfterTransform) {
        return this.addViewSupportTransformColorInt(view, Color.parseColor(viewColorAfterTransform));
    }

    /**
     * Add 颜色变换支持View
     *
     * @param view                     the view
     * @param viewColorBeforeTransform the view color before transform
     * @param viewColorAfterTransform  the view color after transform
     * @return the immersion bar
     */
    public ImmersionBar addViewSupportTransformColor(View view, String viewColorBeforeTransform,
                                                     String viewColorAfterTransform) {
        return this.addViewSupportTransformColorInt(view,
                Color.parseColor(viewColorBeforeTransform),
                Color.parseColor(viewColorAfterTransform));
    }

    /**
     * Add 颜色变换支持View
     *
     * @param view                    the view
     * @param viewColorAfterTransform the view color after transform
     * @return the immersion bar
     */
    public ImmersionBar addViewSupportTransformColorInt(View view, @ColorInt int viewColorAfterTransform) {
        if (view == null) {
            throw new IllegalArgumentException("View参数不能为空");
        }
        Map<Integer, Integer> map = new HashMap<>();
        map.put(mBarParams.statusBarColor, viewColorAfterTransform);
        mBarParams.viewMap.put(view, map);
        return this;
    }

    /**
     * Add 颜色变换支持View
     *
     * @param view                     the view
     * @param viewColorBeforeTransform the view color before transform
     * @param viewColorAfterTransform  the view color after transform
     * @return the immersion bar
     */
    public ImmersionBar addViewSupportTransformColorInt(View view, @ColorInt int viewColorBeforeTransform,
                                                        @ColorInt int viewColorAfterTransform) {
        if (view == null) {
            throw new IllegalArgumentException("View参数不能为空");
        }
        Map<Integer, Integer> map = new HashMap<>();
        map.put(viewColorBeforeTransform, viewColorAfterTransform);
        mBarParams.viewMap.put(view, map);
        return this;
    }

    /**
     * view透明度
     * View alpha immersion bar.
     *
     * @param viewAlpha the view alpha
     * @return the immersion bar
     */
    public ImmersionBar viewAlpha(@FloatRange(from = 0f, to = 1f) float viewAlpha) {
        mBarParams.viewAlpha = viewAlpha;
        return this;
    }

    /**
     * Remove support view immersion bar.
     *
     * @param view the view
     * @return the immersion bar
     */
    public ImmersionBar removeSupportView(View view) {
        if (view == null) {
            throw new IllegalArgumentException("View参数不能为空");
        }
        Map<Integer, Integer> map = mBarParams.viewMap.get(view);
        if (map != null && map.size() != 0) {
            mBarParams.viewMap.remove(view);
        }
        return this;
    }

    /**
     * Remove support all view immersion bar.
     *
     * @return the immersion bar
     */
    public ImmersionBar removeSupportAllView() {
        if (mBarParams.viewMap.size() != 0) {
            mBarParams.viewMap.clear();
        }
        return this;
    }

    /**
     * 有导航栏的情况下，Activity是否全屏显示
     *
     * @param isFullScreen the is full screen
     * @return the immersion bar
     */
    public ImmersionBar fullScreen(boolean isFullScreen) {
        mBarParams.fullScreen = isFullScreen;
        return this;
    }

    /**
     * 状态栏透明度
     *
     * @param statusAlpha the status alpha
     * @return the immersion bar
     */
    public ImmersionBar statusBarAlpha(@FloatRange(from = 0f, to = 1f) float statusAlpha) {
        mBarParams.statusBarAlpha = statusAlpha;
        mBarParams.statusBarTempAlpha = statusAlpha;
        return this;
    }

    /**
     * 导航栏透明度
     *
     * @param navigationAlpha the navigation alpha
     * @return the immersion bar
     */
    public ImmersionBar navigationBarAlpha(@FloatRange(from = 0f, to = 1f) float navigationAlpha) {
        mBarParams.navigationBarAlpha = navigationAlpha;
        mBarParams.navigationBarTempAlpha = navigationAlpha;
        return this;
    }

    /**
     * 状态栏和导航栏透明度
     *
     * @param barAlpha the bar alpha
     * @return the immersion bar
     */
    public ImmersionBar barAlpha(@FloatRange(from = 0f, to = 1f) float barAlpha) {
        mBarParams.statusBarAlpha = barAlpha;
        mBarParams.statusBarTempAlpha = barAlpha;
        mBarParams.navigationBarAlpha = barAlpha;
        mBarParams.navigationBarTempAlpha = barAlpha;
        return this;
    }

    /**
     * 是否启用 自动根据StatusBar和NavigationBar颜色调整深色模式与亮色模式
     *
     * @param isEnable true启用 默认false
     * @return the immersion bar
     */
    public ImmersionBar autoDarkModeEnable(boolean isEnable) {
        return this.autoDarkModeEnable(isEnable, 0.2f);
    }

    /**
     * 是否启用自动根据StatusBar和NavigationBar颜色调整深色模式与亮色模式
     * Auto dark mode enable immersion bar.
     *
     * @param isEnable          the is enable
     * @param autoDarkModeAlpha the auto dark mode alpha
     * @return the immersion bar
     */
    public ImmersionBar autoDarkModeEnable(boolean isEnable, @FloatRange(from = 0f, to = 1f) float autoDarkModeAlpha) {
        mBarParams.autoStatusBarDarkModeEnable = isEnable;
        mBarParams.autoStatusBarDarkModeAlpha = autoDarkModeAlpha;
        mBarParams.autoNavigationBarDarkModeEnable = isEnable;
        mBarParams.autoNavigationBarDarkModeAlpha = autoDarkModeAlpha;
        return this;
    }

    /**
     * 是否启用自动根据StatusBar颜色调整深色模式与亮色模式
     * Auto status bar dark mode enable immersion bar.
     *
     * @param isEnable the is enable
     * @return the immersion bar
     */
    public ImmersionBar autoStatusBarDarkModeEnable(boolean isEnable) {
        return this.autoStatusBarDarkModeEnable(isEnable, 0.2f);
    }

    /**
     * 是否启用自动根据StatusBar颜色调整深色模式与亮色模式
     * Auto status bar dark mode enable immersion bar.
     *
     * @param isEnable          the is enable
     * @param autoDarkModeAlpha the auto dark mode alpha
     * @return the immersion bar
     */
    public ImmersionBar autoStatusBarDarkModeEnable(boolean isEnable, @FloatRange(from = 0f, to = 1f) float autoDarkModeAlpha) {
        mBarParams.autoStatusBarDarkModeEnable = isEnable;
        mBarParams.autoStatusBarDarkModeAlpha = autoDarkModeAlpha;
        return this;
    }

    /**
     * 是否启用自动根据StatusBar颜色调整深色模式与亮色模式
     * Auto navigation bar dark mode enable immersion bar.
     *
     * @param isEnable the is enable
     * @return the immersion bar
     */
    public ImmersionBar autoNavigationBarDarkModeEnable(boolean isEnable) {
        return this.autoNavigationBarDarkModeEnable(isEnable, 0.2f);
    }

    /**
     * 是否启用自动根据NavigationBar颜色调整深色模式与亮色模式
     * Auto navigation bar dark mode enable immersion bar.
     *
     * @param isEnable          the is enable
     * @param autoDarkModeAlpha the auto dark mode alpha
     * @return the immersion bar
     */
    public ImmersionBar autoNavigationBarDarkModeEnable(boolean isEnable, @FloatRange(from = 0f, to = 1f) float autoDarkModeAlpha) {
        mBarParams.autoNavigationBarDarkModeEnable = isEnable;
        mBarParams.autoNavigationBarDarkModeAlpha = autoDarkModeAlpha;
        return this;
    }

    /**
     * 状态栏字体深色或亮色
     *
     * @param isDarkFont true 深色
     * @return the immersion bar
     */
    public ImmersionBar statusBarDarkFont(boolean isDarkFont) {
        return statusBarDarkFont(isDarkFont, 0.2f);
    }

    /**
     * 状态栏字体深色或亮色，判断设备支不支持状态栏变色来设置状态栏透明度
     * Status bar dark font immersion bar.
     *
     * @param isDarkFont  the is dark font
     * @param statusAlpha the status alpha 如果不支持状态栏字体变色可以使用statusAlpha来指定状态栏透明度，比如白色状态栏的时候可以用到
     * @return the immersion bar
     */
    public ImmersionBar statusBarDarkFont(boolean isDarkFont, @FloatRange(from = 0f, to = 1f) float statusAlpha) {
        mBarParams.statusBarDarkFont = isDarkFont;
        if (isDarkFont && !isSupportStatusBarDarkFont()) {
            mBarParams.statusBarAlpha = statusAlpha;
        } else {
            mBarParams.flymeOSStatusBarFontColor = mBarParams.flymeOSStatusBarFontTempColor;
            mBarParams.statusBarAlpha = mBarParams.statusBarTempAlpha;
        }
        return this;
    }

    /**
     * 导航栏图标深色或亮色，只支持android o以上版本
     * Navigation bar dark icon immersion bar.
     *
     * @param isDarkIcon the is dark icon
     * @return the immersion bar
     */
    public ImmersionBar navigationBarDarkIcon(boolean isDarkIcon) {
        return navigationBarDarkIcon(isDarkIcon, 0.2f);
    }

    /**
     * 导航栏图标深色或亮色，只支持android o以上版本，判断设备支不支持导航栏图标变色来设置导航栏透明度
     * Navigation bar dark icon immersion bar.
     *
     * @param isDarkIcon      the is dark icon
     * @param navigationAlpha the navigation alpha 如果不支持导航栏图标变色可以使用navigationAlpha来指定导航栏透明度，比如白色导航栏的时候可以用到
     * @return the immersion bar
     */
    public ImmersionBar navigationBarDarkIcon(boolean isDarkIcon, @FloatRange(from = 0f, to = 1f) float navigationAlpha) {
        mBarParams.navigationBarDarkIcon = isDarkIcon;
        if (isDarkIcon && !isSupportNavigationIconDark()) {
            mBarParams.navigationBarAlpha = navigationAlpha;
        } else {
            mBarParams.navigationBarAlpha = mBarParams.navigationBarTempAlpha;
        }
        return this;
    }

    /**
     * 修改 Flyme OS系统手机状态栏字体颜色，优先级高于statusBarDarkFont(boolean isDarkFont)方法
     * Flyme os status bar font color immersion bar.
     *
     * @param flymeOSStatusBarFontColor the flyme os status bar font color
     * @return the immersion bar
     */
    public ImmersionBar flymeOSStatusBarFontColor(@ColorRes int flymeOSStatusBarFontColor) {
        mBarParams.flymeOSStatusBarFontColor = ContextCompat.getColor(mActivity, flymeOSStatusBarFontColor);
        mBarParams.flymeOSStatusBarFontTempColor = mBarParams.flymeOSStatusBarFontColor;
        return this;
    }

    /**
     * 修改 Flyme OS系统手机状态栏字体颜色，优先级高于statusBarDarkFont(boolean isDarkFont)方法
     * Flyme os status bar font color immersion bar.
     *
     * @param flymeOSStatusBarFontColor the flyme os status bar font color
     * @return the immersion bar
     */
    public ImmersionBar flymeOSStatusBarFontColor(String flymeOSStatusBarFontColor) {
        mBarParams.flymeOSStatusBarFontColor = Color.parseColor(flymeOSStatusBarFontColor);
        mBarParams.flymeOSStatusBarFontTempColor = mBarParams.flymeOSStatusBarFontColor;
        return this;
    }

    /**
     * 修改 Flyme OS系统手机状态栏字体颜色，优先级高于statusBarDarkFont(boolean isDarkFont)方法
     * Flyme os status bar font color immersion bar.
     *
     * @param flymeOSStatusBarFontColor the flyme os status bar font color
     * @return the immersion bar
     */
    public ImmersionBar flymeOSStatusBarFontColorInt(@ColorInt int flymeOSStatusBarFontColor) {
        mBarParams.flymeOSStatusBarFontColor = flymeOSStatusBarFontColor;
        mBarParams.flymeOSStatusBarFontTempColor = mBarParams.flymeOSStatusBarFontColor;
        return this;
    }

    /**
     * 隐藏导航栏或状态栏
     *
     * @param barHide the bar hide
     * @return the immersion bar
     */
    public ImmersionBar hideBar(BarHide barHide) {
        mBarParams.barHide = barHide;
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT || OSUtils.isEMUI3_x()) {
            mBarParams.hideNavigationBar = (mBarParams.barHide == BarHide.FLAG_HIDE_NAVIGATION_BAR) ||
                    (mBarParams.barHide == BarHide.FLAG_HIDE_BAR);
        }
        return this;
    }


    /**
     * 解决布局与状态栏重叠问题，该方法将调用系统view的setFitsSystemWindows方法，一旦window已经focus在设置为false将不会生效，
     * 默认不会使用该方法，如果是渐变色状态栏和顶部图片请不要调用此方法或者设置为false
     * Apply system fits immersion bar.
     *
     * @param applySystemFits the apply system fits
     * @return the immersion bar
     */
    public ImmersionBar applySystemFits(boolean applySystemFits) {
        mBarParams.fitsLayoutOverlapEnable = !applySystemFits;
        setFitsSystemWindows(mActivity, applySystemFits);
        return this;
    }

    /**
     * 解决布局与状态栏重叠问题
     *
     * @param fits the fits
     * @return the immersion bar
     */
    public ImmersionBar fitsSystemWindows(boolean fits) {
        mBarParams.fits = fits;
        if (mBarParams.fits) {
            if (mFitsStatusBarType == FLAG_FITS_DEFAULT) {
                mFitsStatusBarType = FLAG_FITS_SYSTEM_WINDOWS;
            }
        } else {
            mFitsStatusBarType = FLAG_FITS_DEFAULT;
        }
        return this;
    }

    /**
     * 解决布局与状态栏重叠问题，支持侧滑返回
     * Fits system windows immersion bar.
     *
     * @param fits         the fits
     * @param contentColor the content color 整体界面背景色
     * @return the immersion bar
     */
    public ImmersionBar fitsSystemWindows(boolean fits, @ColorRes int contentColor) {
        return fitsSystemWindowsInt(fits, ContextCompat.getColor(mActivity, contentColor));
    }

    /**
     * 解决布局与状态栏重叠问题，支持侧滑返回
     * Fits system windows immersion bar.
     *
     * @param fits                  the fits
     * @param contentColor          the content color 整体界面背景色
     * @param contentColorTransform the content color transform  整体界面变换后的背景色
     * @param contentAlpha          the content alpha 整体界面透明度
     * @return the immersion bar
     */
    public ImmersionBar fitsSystemWindows(boolean fits, @ColorRes int contentColor
            , @ColorRes int contentColorTransform, @FloatRange(from = 0f, to = 1f) float contentAlpha) {
        return fitsSystemWindowsInt(fits, ContextCompat.getColor(mActivity, contentColor),
                ContextCompat.getColor(mActivity, contentColorTransform), contentAlpha);
    }

    /**
     * 解决布局与状态栏重叠问题，支持侧滑返回
     * Fits system windows int immersion bar.
     *
     * @param fits         the fits
     * @param contentColor the content color 整体界面背景色
     * @return the immersion bar
     */
    public ImmersionBar fitsSystemWindowsInt(boolean fits, @ColorInt int contentColor) {
        return fitsSystemWindowsInt(fits, contentColor, Color.BLACK, 0);
    }

    /**
     * 解决布局与状态栏重叠问题，支持侧滑返回
     * Fits system windows int immersion bar.
     *
     * @param fits                  the fits
     * @param contentColor          the content color 整体界面背景色
     * @param contentColorTransform the content color transform 整体界面变换后的背景色
     * @param contentAlpha          the content alpha 整体界面透明度
     * @return the immersion bar
     */
    public ImmersionBar fitsSystemWindowsInt(boolean fits, @ColorInt int contentColor
            , @ColorInt int contentColorTransform, @FloatRange(from = 0f, to = 1f) float contentAlpha) {
        mBarParams.fits = fits;
        mBarParams.contentColor = contentColor;
        mBarParams.contentColorTransform = contentColorTransform;
        mBarParams.contentAlpha = contentAlpha;
        if (mBarParams.fits) {
            if (mFitsStatusBarType == FLAG_FITS_DEFAULT) {
                mFitsStatusBarType = FLAG_FITS_SYSTEM_WINDOWS;
            }
        } else {
            mFitsStatusBarType = FLAG_FITS_DEFAULT;
        }
        mContentView.setBackgroundColor(ColorUtils.blendARGB(mBarParams.contentColor,
                mBarParams.contentColorTransform, mBarParams.contentAlpha));
        return this;
    }

    /**
     * 是否可以修复状态栏与布局重叠，默认为true，只适合ImmersionBar#statusBarView，ImmersionBar#titleBar，ImmersionBar#titleBarMarginTop
     * Fits layout overlap enable immersion bar.
     *
     * @param fitsLayoutOverlapEnable the fits layout overlap enable
     * @return the immersion bar
     */
    public ImmersionBar fitsLayoutOverlapEnable(boolean fitsLayoutOverlapEnable) {
        mBarParams.fitsLayoutOverlapEnable = fitsLayoutOverlapEnable;
        return this;
    }

    /**
     * 通过状态栏高度动态设置状态栏布局
     *
     * @param view the view
     * @return the immersion bar
     */
    public ImmersionBar statusBarView(View view) {
        if (view == null) {
            return this;
        }
        mBarParams.statusBarView = view;
        if (mFitsStatusBarType == FLAG_FITS_DEFAULT) {
            mFitsStatusBarType = FLAG_FITS_STATUS;
        }
        return this;
    }

    /**
     * 通过状态栏高度动态设置状态栏布局,只能在Activity中使用
     *
     * @param viewId the view id
     * @return the immersion bar
     */
    public ImmersionBar statusBarView(@IdRes int viewId) {
        return statusBarView(mActivity.findViewById(viewId));
    }

    /**
     * 通过状态栏高度动态设置状态栏布局
     * Status bar view immersion bar.
     *
     * @param viewId   the view id
     * @param rootView the root view
     * @return the immersion bar
     */
    public ImmersionBar statusBarView(@IdRes int viewId, View rootView) {
        return statusBarView(rootView.findViewById(viewId));
    }

    /**
     * 解决状态栏与布局顶部重叠又多了种方法
     * Title bar immersion bar.
     *
     * @param view the view
     * @return the immersion bar
     */
    public ImmersionBar titleBar(View view) {
        if (view == null) {
            return this;
        }
        return titleBar(view, true);
    }

    /**
     * 解决状态栏与布局顶部重叠又多了种方法
     * Title bar immersion bar.
     *
     * @param view                          the view
     * @param statusBarColorTransformEnable the status bar flag 默认为true false表示状态栏不支持变色，true表示状态栏支持变色
     * @return the immersion bar
     */
    public ImmersionBar titleBar(View view, boolean statusBarColorTransformEnable) {
        if (view == null) {
            return this;
        }
        if (mFitsStatusBarType == FLAG_FITS_DEFAULT) {
            mFitsStatusBarType = FLAG_FITS_TITLE;
        }
        mBarParams.titleBarView = view;
        mBarParams.statusBarColorEnabled = statusBarColorTransformEnable;
        return this;
    }

    /**
     * 解决状态栏与布局顶部重叠又多了种方法，只支持Activity
     * Title bar immersion bar.
     *
     * @param viewId the view id
     * @return the immersion bar
     */
    public ImmersionBar titleBar(@IdRes int viewId) {
        return titleBar(viewId, true);
    }

    /**
     * Title bar immersion bar.
     *
     * @param viewId                        the view id
     * @param statusBarColorTransformEnable the status bar flag
     * @return the immersion bar
     */
    public ImmersionBar titleBar(@IdRes int viewId, boolean statusBarColorTransformEnable) {
        if (mSupportFragment != null && mSupportFragment.getView() != null) {
            return titleBar(mSupportFragment.getView().findViewById(viewId), statusBarColorTransformEnable);
        } else if (mFragment != null && mFragment.getView() != null) {
            return titleBar(mFragment.getView().findViewById(viewId), statusBarColorTransformEnable);
        } else {
            return titleBar(mActivity.findViewById(viewId), statusBarColorTransformEnable);
        }
    }

    /**
     * Title bar immersion bar.
     *
     * @param viewId   the view id
     * @param rootView the root view
     * @return the immersion bar
     */
    public ImmersionBar titleBar(@IdRes int viewId, View rootView) {
        return titleBar(rootView.findViewById(viewId), true);
    }

    /**
     * 解决状态栏与布局顶部重叠又多了种方法，支持任何view
     * Title bar immersion bar.
     *
     * @param viewId                        the view id
     * @param rootView                      the root view
     * @param statusBarColorTransformEnable the status bar flag 默认为true false表示状态栏不支持变色，true表示状态栏支持变色
     * @return the immersion bar
     */
    public ImmersionBar titleBar(@IdRes int viewId, View rootView, boolean statusBarColorTransformEnable) {
        return titleBar(rootView.findViewById(viewId), statusBarColorTransformEnable);
    }

    /**
     * 绘制标题栏距离顶部的高度为状态栏的高度
     * Title bar margin top immersion bar.
     *
     * @param viewId the view id   标题栏资源id
     * @return the immersion bar
     */
    public ImmersionBar titleBarMarginTop(@IdRes int viewId) {
        if (mSupportFragment != null && mSupportFragment.getView() != null) {
            return titleBarMarginTop(mSupportFragment.getView().findViewById(viewId));
        } else if (mFragment != null && mFragment.getView() != null) {
            return titleBarMarginTop(mFragment.getView().findViewById(viewId));
        } else {
            return titleBarMarginTop(mActivity.findViewById(viewId));
        }
    }

    /**
     * 绘制标题栏距离顶部的高度为状态栏的高度
     * Title bar margin top immersion bar.
     *
     * @param viewId   the view id  标题栏资源id
     * @param rootView the root view  布局view
     * @return the immersion bar
     */
    public ImmersionBar titleBarMarginTop(@IdRes int viewId, View rootView) {
        return titleBarMarginTop(rootView.findViewById(viewId));
    }

    /**
     * 绘制标题栏距离顶部的高度为状态栏的高度
     * Title bar margin top immersion bar.
     *
     * @param view the view  要改变的标题栏view
     * @return the immersion bar
     */
    public ImmersionBar titleBarMarginTop(View view) {
        if (view == null) {
            return this;
        }
        if (mFitsStatusBarType == FLAG_FITS_DEFAULT) {
            mFitsStatusBarType = FLAG_FITS_TITLE_MARGIN_TOP;
        }
        mBarParams.titleBarView = view;
        return this;
    }

    /**
     * 支持有actionBar的界面,调用该方法，布局讲从actionBar下面开始绘制
     * Support action bar immersion bar.
     *
     * @param isSupportActionBar the is support action bar
     * @return the immersion bar
     */
    public ImmersionBar supportActionBar(boolean isSupportActionBar) {
        mBarParams.isSupportActionBar = isSupportActionBar;
        return this;
    }

    /**
     * Status bar color transform enable immersion bar.
     *
     * @param statusBarColorTransformEnable the status bar flag
     * @return the immersion bar
     */
    public ImmersionBar statusBarColorTransformEnable(boolean statusBarColorTransformEnable) {
        mBarParams.statusBarColorEnabled = statusBarColorTransformEnable;
        return this;
    }

    /**
     * 一键重置所有参数
     * Reset immersion bar.
     *
     * @return the immersion bar
     */
    public ImmersionBar reset() {
        mBarParams = new BarParams();
        mFitsStatusBarType = FLAG_FITS_DEFAULT;
        return this;
    }

    /**
     * 给某个页面设置tag来标识这页bar的属性.
     * Add tag bar tag.
     *
     * @param tag the tag
     * @return the bar tag
     */
    public ImmersionBar addTag(String tag) {
        if (isEmpty(tag)) {
            throw new IllegalArgumentException("tag不能为空");
        }
        BarParams barParams = mBarParams.clone();
        mTagMap.put(tag, barParams);
        return this;
    }

    /**
     * 根据tag恢复到某次调用时的参数
     * Recover immersion bar.
     *
     * @param tag the tag
     * @return the immersion bar
     */
    public ImmersionBar getTag(String tag) {
        if (isEmpty(tag)) {
            throw new IllegalArgumentException("tag不能为空");
        }
        BarParams barParams = mTagMap.get(tag);
        if (barParams != null) {
            mBarParams = barParams.clone();
        }
        return this;
    }

    /**
     * 解决软键盘与底部输入框冲突问题 ，默认是false
     * Keyboard enable immersion bar.
     *
     * @param enable the enable
     * @return the immersion bar
     */
    public ImmersionBar keyboardEnable(boolean enable) {
        return keyboardEnable(enable, mBarParams.keyboardMode);
    }

    /**
     * 解决软键盘与底部输入框冲突问题 ，默认是false
     *
     * @param enable       the enable
     * @param keyboardMode the keyboard mode
     * @return the immersion bar
     */
    public ImmersionBar keyboardEnable(boolean enable, int keyboardMode) {
        mBarParams.keyboardEnable = enable;
        mBarParams.keyboardMode = keyboardMode;
        mKeyboardTempEnable = enable;
        return this;
    }

    /**
     * 修改键盘模式
     * Keyboard mode immersion bar.
     *
     * @param keyboardMode the keyboard mode
     * @return the immersion bar
     */
    public ImmersionBar keyboardMode(int keyboardMode) {
        mBarParams.keyboardMode = keyboardMode;
        return this;
    }

    /**
     * 软键盘弹出关闭的回调监听
     * Sets on keyboard listener.
     *
     * @param onKeyboardListener the on keyboard listener
     * @return the on keyboard listener
     */
    public ImmersionBar setOnKeyboardListener(@Nullable OnKeyboardListener onKeyboardListener) {
        if (mBarParams.onKeyboardListener == null) {
            mBarParams.onKeyboardListener = onKeyboardListener;
        }
        return this;
    }

    /**
     * 导航栏显示隐藏监听器
     * Sets on navigation bar listener.
     *
     * @param onNavigationBarListener the on navigation bar listener
     * @return the on navigation bar listener
     */
    public ImmersionBar setOnNavigationBarListener(OnNavigationBarListener onNavigationBarListener) {
        if (onNavigationBarListener != null) {
            if (mBarParams.onNavigationBarListener == null) {
                mBarParams.onNavigationBarListener = onNavigationBarListener;
                NavigationBarObserver.getInstance().addOnNavigationBarListener(mBarParams.onNavigationBarListener);
            }
        } else {
            if (mBarParams.onNavigationBarListener != null) {
                NavigationBarObserver.getInstance().removeOnNavigationBarListener(mBarParams.onNavigationBarListener);
                mBarParams.onNavigationBarListener = null;
            }
        }
        return this;
    }


    /**
     * Bar监听，第一次调用和横竖屏切换都会触发此方法，比如可以解决横竖屏切换，横屏情况下，刘海屏遮挡布局的问题
     * Sets on bar listener.
     *
     * @param onBarListener the on bar listener
     * @return the on bar listener
     */
    public ImmersionBar setOnBarListener(OnBarListener onBarListener) {
        if (onBarListener != null) {
            if (mBarParams.onBarListener == null) {
                mBarParams.onBarListener = onBarListener;
            }
        } else {
            if (mBarParams.onBarListener != null) {
                mBarParams.onBarListener = null;
            }
        }
        return this;
    }

    /**
     * 是否可以修改导航栏颜色，默认为true
     * 优先级 navigationBarEnable  > navigationBarWithKitkatEnable > navigationBarWithEMUI3Enable
     * Navigation bar enable immersion bar.
     *
     * @param navigationBarEnable the enable
     * @return the immersion bar
     */
    public ImmersionBar navigationBarEnable(boolean navigationBarEnable) {
        mBarParams.navigationBarEnable = navigationBarEnable;
        return this;
    }

    /**
     * 是否可以修改4.4设备导航栏颜色，默认为true
     * 优先级 navigationBarEnable  > navigationBarWithKitkatEnable > navigationBarWithEMUI3Enable
     *
     * @param navigationBarWithKitkatEnable the navigation bar with kitkat enable
     * @return the immersion bar
     */
    public ImmersionBar navigationBarWithKitkatEnable(boolean navigationBarWithKitkatEnable) {
        mBarParams.navigationBarWithKitkatEnable = navigationBarWithKitkatEnable;
        return this;
    }

    /**
     * 是否能修改华为emui3.1导航栏颜色，默认为true，
     * 优先级 navigationBarEnable  > navigationBarWithKitkatEnable > navigationBarWithEMUI3Enable
     * Navigation bar with emui 3 enable immersion bar.
     *
     * @param navigationBarWithEMUI3Enable the navigation bar with emui 3 1 enable
     * @return the immersion bar
     */
    public ImmersionBar navigationBarWithEMUI3Enable(boolean navigationBarWithEMUI3Enable) {
        //是否可以修改emui3系列手机导航栏
        if (OSUtils.isEMUI3_x()) {
            mBarParams.navigationBarWithEMUI3Enable = navigationBarWithEMUI3Enable;
            mBarParams.navigationBarWithKitkatEnable = navigationBarWithEMUI3Enable;
        }
        return this;
    }

    /**
     * 是否可以使用沉浸式，如果已经是true了，在改为false，之前沉浸式效果不会消失，之后设置的沉浸式效果也不会生效
     * Bar enable immersion bar.
     *
     * @param barEnable the bar enable
     * @return the immersion bar
     */
    public ImmersionBar barEnable(boolean barEnable) {
        mBarParams.barEnable = barEnable;
        return this;
    }

    private static RequestManagerRetriever getRetriever() {
        return RequestManagerRetriever.getInstance();
    }

    private static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }
}
