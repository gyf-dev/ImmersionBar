package com.gyf.barlibrary;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.database.ContentObserver;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * android 4.4以上沉浸式以及bar的管理
 *
 * @author gyf
 * @date 2017 /05/09
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
public class ImmersionBar {

    private static final int IMMERSION_STATUS_BAR_VIEW = R.id.immersion_status_bar_view;
    private static final int IMMERSION_NAVIGATION_BAR_VIEW = R.id.immersion_navigation_bar_view;
    private static final String NAVIGATIONBAR_IS_MIN = "navigationbar_is_min";
    private static final String MIUI_STATUS_BAR_DARK = "EXTRA_FLAG_STATUS_BAR_DARK_MODE";
    private static final String MIUI_NAVIGATION_BAR_DARK = "EXTRA_FLAG_NAVIGATION_BAR_DARK_MODE";

    private static final int FLAG_FITS_DEFAULT = 0X00;
    private static final int FLAG_FITS_TITLE = 0X01;
    private static final int FLAG_FITS_TITLE_MARGIN_TOP = 0X02;
    private static final int FLAG_FITS_STATUS = 0X03;
    private static final int FLAG_FITS_SYSTEM_WINDOWS = 0X04;

    /**
     * 维护ImmersionBar的集合
     */
    private static Map<String, ImmersionBar> mImmersionBarMap = new HashMap<>();

    private Activity mActivity;
    private Fragment mFragment;
    private Dialog mDialog;
    private Window mWindow;
    private ViewGroup mDecorView;
    private ViewGroup mContentView;

    /**
     * 用户配置的bar参数
     */
    private BarParams mBarParams;
    /**
     * 系统bar相关信息
     */
    private BarConfig mBarConfig;
    /**
     * 沉浸式名字
     */
    private String mImmersionBarName;
    /**
     * 导航栏的高度，适配Emui3系统有用
     */
    private int mNavigationBarHeight = 0;
    /**
     * 导航栏的宽度，适配Emui3系统有用
     */
    private int mNavigationBarWidth = 0;
    /**
     * 是否是在Activity使用的沉浸式
     */
    private boolean mIsFragment = false;
    /**
     * Emui系统导航栏监听器
     */
    private ContentObserver mNavigationObserver = null;
    private FitsKeyboard mFitsKeyboard = null;
    /**
     * 用户使用tag增加的bar参数的集合
     */
    private Map<String, BarParams> mTagMap = new HashMap<>();
    /**
     * 是否适配过布局与导航栏重叠了
     */
    private boolean mIsFitsLayoutOverlap = false;
    /**
     * 当前是以哪种方式适配的
     */
    private int mFitsStatusBarType = FLAG_FITS_DEFAULT;
    /**
     * 是否已经获取到当前导航栏颜色了
     */
    private boolean mHasNavigationBarColor = false;

    /**
     * 是否已经适配刘海屏标识
     * The M is fits notch.
     */
    private boolean mIsFitsNotch = false;

    private int mPaddingLeft = 0, mPaddingTop = 0, mPaddingRight = 0, mPaddingBottom = 0;

    /**
     * 在Activit里初始化
     * Instantiates a new Immersion bar.
     *
     * @param activity the activity
     */
    private ImmersionBar(Activity activity) {

        mActivity = activity;
        mWindow = mActivity.getWindow();

        mImmersionBarName = mActivity.toString();

        mBarParams = new BarParams();

        mDecorView = (ViewGroup) mWindow.getDecorView();
        mContentView = mDecorView.findViewById(android.R.id.content);
    }

    /**
     * 在Fragment里初始化
     * Instantiates a new Immersion bar.
     *
     * @param fragment the fragment
     */
    private ImmersionBar(Fragment fragment) {
        this(fragment.getActivity(), fragment);
    }

    private ImmersionBar(Activity activity, Fragment fragment) {
        mActivity = activity;
        mFragment = fragment;
        if (mActivity == null) {
            throw new IllegalArgumentException("Activity不能为空!!!");
        }
        if (mImmersionBarMap.get(mActivity.toString()) == null) {
            throw new IllegalArgumentException("必须先在宿主Activity初始化");
        }

        mIsFragment = true;
        mWindow = mActivity.getWindow();

        mImmersionBarName = activity.toString() + fragment.toString();

        mBarParams = new BarParams();

        mDecorView = (ViewGroup) mWindow.getDecorView();
        mContentView = mDecorView.findViewById(android.R.id.content);
    }


    /**
     * 在dialogFragment里使用
     * Instantiates a new Immersion bar.
     *
     * @param dialogFragment the dialog fragment
     */
    private ImmersionBar(DialogFragment dialogFragment) {
        this(dialogFragment, dialogFragment.getDialog());
    }

    private ImmersionBar(DialogFragment dialogFragment, Dialog dialog) {
        mActivity = dialogFragment.getActivity();
        mFragment = dialogFragment;
        mDialog = dialog;
        if (mActivity == null) {
            throw new IllegalArgumentException("Activity不能为空!!!");
        }
        if (mDialog == null) {
            throw new IllegalArgumentException("DialogFragment中的dialog不能为空");
        }
        if (mImmersionBarMap.get(mActivity.toString()) == null) {
            throw new IllegalArgumentException("必须先在宿主Activity初始化");
        }

        mWindow = mDialog.getWindow();

        mImmersionBarName = mActivity.toString() + dialogFragment.toString();

        mBarParams = new BarParams();

        mDecorView = (ViewGroup) mWindow.getDecorView();
        mContentView = mDecorView.findViewById(android.R.id.content);
    }

    /**
     * 在Dialog里初始化
     * Instantiates a new Immersion bar.
     *
     * @param activity the activity
     * @param dialog   the dialog
     */
    private ImmersionBar(Activity activity, Dialog dialog) {
        this(activity, dialog, "");
    }

    /**
     * 在Dialog里初始化
     * Instantiates a new Immersion bar.
     *
     * @param activity  the activity
     * @param dialog    the dialog
     * @param dialogTag the dialog tag  dialog标识，不能为空
     */
    private ImmersionBar(Activity activity, Dialog dialog, String dialogTag) {
        mActivity = activity;
        mDialog = dialog;
        if (mActivity == null) {
            throw new IllegalArgumentException("Activity不能为空!!!");
        }
        if (mDialog == null) {
            throw new IllegalArgumentException("dialog不能为空");
        }
        if (mImmersionBarMap.get(mActivity.toString()) == null) {
            throw new IllegalArgumentException("必须先在宿主Activity初始化");
        }

        mWindow = mDialog.getWindow();
        mImmersionBarName = activity.toString() + dialog.toString() + dialogTag;

        mBarParams = new BarParams();

        mDecorView = (ViewGroup) mWindow.getDecorView();
        mContentView = mDecorView.findViewById(android.R.id.content);
    }

    /**
     * 初始化Activity
     * With immersion bar.
     *
     * @param activity the activity
     * @return the immersion bar
     */
    public static ImmersionBar with(@NonNull Activity activity) {
        ImmersionBar immersionBar = mImmersionBarMap.get(activity.toString());
        if (immersionBar == null) {
            immersionBar = new ImmersionBar(activity);
            mImmersionBarMap.put(activity.toString(), immersionBar);
        }
        return immersionBar;
    }

    /**
     * 调用该方法必须保证加载Fragment的Activity先初始化
     * With immersion bar.
     *
     * @param fragment the fragment
     * @return the immersion bar
     */
    public static ImmersionBar with(@NonNull Fragment fragment) {
        if (fragment.getActivity() == null) {
            throw new IllegalArgumentException("Activity不能为空!!!");
        }
        ImmersionBar immersionBar = mImmersionBarMap.get(fragment.getActivity().toString() + fragment.toString());
        if (immersionBar == null) {
            immersionBar = new ImmersionBar(fragment);
            mImmersionBarMap.put(fragment.getActivity().toString() + fragment.toString(), immersionBar);
        }
        return immersionBar;
    }

    /**
     * With immersion bar.
     *
     * @param activity the activity
     * @param fragment the fragment
     * @return the immersion bar
     * @deprecated 请使用ImmersionBar with(@NonNull Fragment fragment)
     */
    public static ImmersionBar with(@NonNull Activity activity, @NonNull Fragment fragment) {
        ImmersionBar immersionBar = mImmersionBarMap.get(activity.toString() + fragment.toString());
        if (immersionBar == null) {
            immersionBar = new ImmersionBar(activity, fragment);
            mImmersionBarMap.put(activity.toString() + fragment.toString(), immersionBar);
        }
        return immersionBar;
    }


    /**
     * 在DialogFragment使用
     * With immersion bar.
     *
     * @param dialogFragment the dialog fragment
     * @return the immersion bar
     */
    public static ImmersionBar with(@NonNull DialogFragment dialogFragment) {
        if (dialogFragment.getActivity() == null) {
            throw new IllegalArgumentException("Activity不能为空!!!");
        }
        ImmersionBar immersionBar = mImmersionBarMap.get(dialogFragment.getActivity().toString() + dialogFragment.toString());
        if (immersionBar == null) {
            immersionBar = new ImmersionBar(dialogFragment);
            mImmersionBarMap.put(dialogFragment.getActivity().toString() + dialogFragment.toString(), immersionBar);
        }
        return immersionBar;
    }

    /**
     * 在DialogFragment使用，已过时
     *
     * @param dialogFragment the dialog fragment
     * @param dialog         the dialog
     * @return the immersion bar
     * @deprecated 请使用ImmersionBar with(@NonNull DialogFragment dialogFragment)
     */
    @Deprecated
    public static ImmersionBar with(@NonNull DialogFragment dialogFragment, @NonNull Dialog dialog) {
        if (dialogFragment.getActivity() == null) {
            throw new IllegalArgumentException("Activity不能为空!!!");
        }
        ImmersionBar immersionBar = mImmersionBarMap.get(dialogFragment.getActivity().toString() + dialogFragment.toString());
        if (immersionBar == null) {
            immersionBar = new ImmersionBar(dialogFragment, dialog);
            mImmersionBarMap.put(dialogFragment.getActivity().toString() + dialogFragment.toString(), immersionBar);
        }
        return immersionBar;
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
        ImmersionBar immersionBar = mImmersionBarMap.get(activity.toString() + dialog.toString());
        if (immersionBar == null) {
            immersionBar = new ImmersionBar(activity, dialog);
            mImmersionBarMap.put(activity.toString() + dialog.toString(), immersionBar);
        }
        return immersionBar;
    }

    /**
     * 在dialog里使用，已过时
     * With immersion bar.
     *
     * @param activity  the activity
     * @param dialog    the dialog
     * @param dialogTag the dialog tag
     * @return the immersion bar
     * @deprecated 请使用ImmersionBar with(@NonNull Activity activity, @NonNull Dialog dialog)
     */
    @Deprecated
    public static ImmersionBar with(@NonNull Activity activity, @NonNull Dialog dialog, @NonNull String dialogTag) {
        ImmersionBar immersionBar = mImmersionBarMap.get(activity.toString() + dialog.toString() + dialogTag);
        if (immersionBar == null) {
            immersionBar = new ImmersionBar(activity, dialog, dialogTag);
            mImmersionBarMap.put(activity.toString() + dialog.toString() + dialogTag, immersionBar);
        }
        return immersionBar;
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
        if (map.size() != 0) {
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
        mBarParams.navigationBarAlpha = barAlpha;
        return this;
    }


    /**
     * 是否启用 自动根据StatusBar和NavigationBar颜色调整深色模式与亮色模式
     *
     * @param isEnable true启用 默认false
     * @return the immersion bar
     */
    public ImmersionBar autoDarkModeEnable(boolean isEnable) {
        mBarParams.autoDarkModeEnable = isEnable;
        return this;
    }

    /**
     * 状态栏字体深色或亮色
     *
     * @param isDarkFont true 深色
     * @return the immersion bar
     */
    public ImmersionBar statusBarDarkFont(boolean isDarkFont) {
        return statusBarDarkFont(isDarkFont, 0f);
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
        if (!isDarkFont) {
            mBarParams.flymeOSStatusBarFontColor = 0;
        }
        if (isSupportStatusBarDarkFont()) {
            mBarParams.statusBarAlpha = 0;
        } else {
            mBarParams.statusBarAlpha = statusAlpha;
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
        return navigationBarDarkIcon(isDarkIcon, 0f);
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
        if (isSupportNavigationIconDark()) {
            mBarParams.navigationBarAlpha = 0;
        } else {
            mBarParams.navigationBarAlpha = navigationAlpha;
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
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT || OSUtils.isEMUI3_1()) {
            if ((mBarParams.barHide == BarHide.FLAG_HIDE_NAVIGATION_BAR) ||
                    (mBarParams.barHide == BarHide.FLAG_HIDE_BAR)) {
                mBarParams.hideNavigationBar = true;
            } else {
                mBarParams.hideNavigationBar = false;
            }
        }
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
        if (mFragment != null && mFragment.getView() != null) {
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
        if (mFragment != null && mFragment.getView() != null) {
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
    public ImmersionBar setOnKeyboardListener(OnKeyboardListener onKeyboardListener) {
        if (mBarParams.onKeyboardListener == null) {
            mBarParams.onKeyboardListener = onKeyboardListener;
        }
        return this;
    }

    /**
     * 是否可以修改导航栏颜色，默认为true
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
     *
     * @param navigationBarWithKitkatEnable the navigation bar with kitkat enable
     * @return the immersion bar
     */
    public ImmersionBar navigationBarWithKitkatEnable(boolean navigationBarWithKitkatEnable) {
        mBarParams.navigationBarWithKitkatEnable = navigationBarWithKitkatEnable;
        return this;
    }

    /**
     * 是否能修改华为emui3.1导航栏颜色，默认为true
     * Navigation bar with emui 3 enable immersion bar.
     *
     * @param navigationBarWithEMUI3Enable the navigation bar with emui 3 1 enable
     * @return the immersion bar
     */
    public ImmersionBar navigationBarWithEMUI3Enable(boolean navigationBarWithEMUI3Enable) {
        mBarParams.navigationBarWithEMUI3Enable = navigationBarWithEMUI3Enable;
        return this;
    }

    /**
     * 当xml里使用android:fitsSystemWindows="true"属性时，
     * 解决4.4和emui3.1手机底部有时会出现多余空白的问题 ，已过时，代码中没用的此处
     * Fix margin atbottom immersion bar.
     *
     * @param fixMarginAtBottom the fix margin atbottom
     * @return the immersion bar
     * @deprecated
     */
    @Deprecated
    public ImmersionBar fixMarginAtBottom(boolean fixMarginAtBottom) {
        mBarParams.fixMarginAtBottom = fixMarginAtBottom;
        return this;
    }

    /**
     * 通过上面配置后初始化后方可成功调用
     */
    public void init() {
        //更新Bar的参数
        updateBarParams();
        //设置沉浸式
        setBar();
        //适配状态栏与布局重叠问题
        fitsLayoutOverlap();
        //适配软键盘与底部输入框冲突问题
        fitsKeyboard();
        //变色view
        transformView();
    }

    /**
     * 当Activity/Fragment/Dialog关闭的时候调用
     */
    public void destroy() {
        //取消监听
        cancelListener();
        //删除当前界面对应的ImmersionBar对象
        Iterator<Map.Entry<String, ImmersionBar>> iterator = mImmersionBarMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, ImmersionBar> entry = iterator.next();
            if (entry.getKey().contains(mImmersionBarName) || (entry.getKey().equals(mImmersionBarName))) {
                iterator.remove();
            }
        }
    }

    /**
     * 更新Bar的参数
     * Update bar params.
     */
    private void updateBarParams() {
        adjustDarkModeParams();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //是否可以修改emui3系列手机导航栏
            if (OSUtils.isEMUI3_x() && mBarParams.navigationBarWithKitkatEnable) {
                mBarParams.navigationBarWithKitkatEnable = mBarParams.navigationBarWithEMUI3Enable;
            }
            //获得Bar相关信息
            mBarConfig = new BarConfig(mActivity);
            //如果在Fragment中使用，让Activity同步Fragment的BarParams参数
            if (mIsFragment) {
                ImmersionBar immersionBar = mImmersionBarMap.get(mActivity.toString());
                if (immersionBar != null) {
                    immersionBar.mBarParams = mBarParams;
                }
            }
        }
    }

    /**
     * 初始化状态栏和导航栏
     */
    private void setBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //防止系统栏隐藏时内容区域大小发生变化
            int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !OSUtils.isEMUI3_1()) {
                //适配刘海屏
                fitsNotchScreen();
                //初始化5.0以上，包含5.0
                uiFlags = initBarAboveLOLLIPOP(uiFlags);
                //android 6.0以上设置状态栏字体为暗色
                uiFlags = setStatusBarDarkFont(uiFlags);
                //android 8.0以上设置导航栏图标为暗色
                uiFlags = setNavigationIconDark(uiFlags);
            } else {
                //初始化5.0以下，4.4以上沉浸式
                initBarBelowLOLLIPOP();
            }
            //隐藏状态栏或者导航栏
            uiFlags = hideBar(uiFlags);
            //修正界面显示
            fitsWindows();
            mDecorView.setSystemUiVisibility(uiFlags);
        }
        if (OSUtils.isMIUI6Later()) {
            //修改miui状态栏字体颜色
            setMIUIBarDark(mWindow, MIUI_STATUS_BAR_DARK, mBarParams.statusBarDarkFont);
            //修改miui导航栏图标为黑色
            if (mBarParams.navigationBarEnable) {
                setMIUIBarDark(mWindow, MIUI_NAVIGATION_BAR_DARK, mBarParams.navigationBarDarkIcon);
            }
        }
        // 修改Flyme OS状态栏字体颜色
        if (OSUtils.isFlymeOS4Later()) {
            if (mBarParams.flymeOSStatusBarFontColor != 0) {
                FlymeOSStatusBarFontUtils.setStatusBarDarkIcon(mActivity, mBarParams.flymeOSStatusBarFontColor);
            } else {
                FlymeOSStatusBarFontUtils.setStatusBarDarkIcon(mActivity, mBarParams.statusBarDarkFont);
            }
        }
    }

    /**
     * 适配刘海屏
     * Fits notch screen.
     */
    private void fitsNotchScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && !mIsFitsNotch) {
            WindowManager.LayoutParams lp = mWindow.getAttributes();
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            mWindow.setAttributes(lp);
            mIsFitsNotch = true;
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
        if (!mHasNavigationBarColor) {
            mBarParams.defaultNavigationBarColor = mWindow.getNavigationBarColor();
            mHasNavigationBarColor = true;
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
            mWindow.setStatusBarColor(ColorUtils.blendARGB(mBarParams.statusBarColor,
                    mBarParams.statusBarColorTransform, mBarParams.statusBarAlpha));
        } else {
            mWindow.setStatusBarColor(ColorUtils.blendARGB(mBarParams.statusBarColor,
                    Color.TRANSPARENT, mBarParams.statusBarAlpha));
        }
        //设置导航栏颜色
        if (mBarParams.navigationBarEnable) {
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
        if (mBarConfig.hasNavigationBar() || OSUtils.isEMUI3_1() || OSUtils.isEMUI3_0()) {
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
        View statusBarView = mDecorView.findViewById(IMMERSION_STATUS_BAR_VIEW);
        if (statusBarView == null) {
            statusBarView = new View(mActivity);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                    mBarConfig.getStatusBarHeight());
            params.gravity = Gravity.TOP;
            statusBarView.setLayoutParams(params);
            statusBarView.setVisibility(View.VISIBLE);
            statusBarView.setId(IMMERSION_STATUS_BAR_VIEW);
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
        View navigationBarView = mDecorView.findViewById(IMMERSION_NAVIGATION_BAR_VIEW);
        if (navigationBarView == null) {
            navigationBarView = new View(mActivity);
            navigationBarView.setId(IMMERSION_NAVIGATION_BAR_VIEW);
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
        if (mBarParams.autoDarkModeEnable) {
            int boundaryColor = 0xFFBABABA;
            statusBarDarkFont(mBarParams.statusBarColor != Color.TRANSPARENT && mBarParams.statusBarColor > boundaryColor);
            navigationBarDarkIcon(mBarParams.navigationBarColor != Color.TRANSPARENT && mBarParams.navigationBarColor > boundaryColor);
        }
    }

    /**
     * Hide bar.
     * 隐藏或显示状态栏和导航栏。
     *
     * @param uiFlags the ui flags
     * @return the int
     */
    private int hideBar(int uiFlags) {
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !OSUtils.isEMUI3_1()) {
            //android 5.0以上解决状态栏和布局重叠问题
            fitsWindowsAboveLOLLIPOP();
        } else {
            //解决android4.4有导航栏的情况下，activity底部被导航栏遮挡的问题和android 5.0以下解决状态栏和布局重叠问题
            fitsWindowsBelowLOLLIPOP();
            //解决华为emui3.1或者3.0导航栏手动隐藏的问题
            if (!mIsFragment && OSUtils.isEMUI3_x()) {
                fitsWindowsEMUI();
            }
        }
    }

    /**
     * android 5.0以上解决状态栏和布局重叠问题
     * Fits windows above lollipop.
     */
    private void fitsWindowsAboveLOLLIPOP() {
        if (checkFitsSystemWindows(mDecorView.findViewById(android.R.id.content))) {
            if (mBarParams.isSupportActionBar) {
                setPadding(0, mBarConfig.getActionBarHeight(), 0, 0);
            }
            return;
        }
        int top = 0;
        if (mBarParams.fits && mFitsStatusBarType == FLAG_FITS_SYSTEM_WINDOWS) {
            top = mBarConfig.getStatusBarHeight();
        }
        if (mBarParams.isSupportActionBar) {
            top = mBarConfig.getStatusBarHeight() + mBarConfig.getActionBarHeight();
        }
        setPadding(0, top, 0, 0);
    }

    /**
     * 解决android4.4有导航栏的情况下，activity底部被导航栏遮挡的问题和android 5.0以下解决状态栏和布局重叠问题
     * Fits windows below lollipop.
     */
    private void fitsWindowsBelowLOLLIPOP() {
        if (checkFitsSystemWindows(mDecorView.findViewById(android.R.id.content))) {
            if (mBarParams.isSupportActionBar) {
                setPadding(0, mBarConfig.getActionBarHeight(), 0, 0);
            }
            return;
        }
        int top = 0, right = 0, bottom = 0;
        if (mBarParams.fits && mFitsStatusBarType == FLAG_FITS_SYSTEM_WINDOWS) {
            top = mBarConfig.getStatusBarHeight();
        }
        if (mBarParams.isSupportActionBar) {
            top = mBarConfig.getStatusBarHeight() + mBarConfig.getActionBarHeight();
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
        final View navigationBarView = mDecorView.findViewById(IMMERSION_NAVIGATION_BAR_VIEW);
        if (mBarParams.navigationBarEnable && mBarParams.navigationBarWithKitkatEnable) {
            if (navigationBarView != null && mNavigationObserver == null) {
                mNavigationObserver = new ContentObserver(new Handler()) {
                    @Override
                    public void onChange(boolean selfChange) {
                        mBarConfig = new BarConfig(mActivity);
                        int bottom = mContentView.getPaddingBottom(), right = mContentView.getPaddingRight();
                        if (mActivity != null && mActivity.getContentResolver() != null) {
                            int navigationBarIsMin = Settings.System.getInt(mActivity.getContentResolver(),
                                    NAVIGATIONBAR_IS_MIN, 0);
                            if (navigationBarIsMin == 1) {
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
                        }
                        setPadding(0, mContentView.getPaddingTop(), right, bottom);

                    }
                };
                if (mActivity != null && mActivity.getContentResolver() != null && mNavigationObserver != null) {
                    mActivity.getContentResolver().registerContentObserver(Settings.System.getUriFor
                            (NAVIGATIONBAR_IS_MIN), true, mNavigationObserver);
                }
            }
        } else {
            navigationBarView.setVisibility(View.GONE);
        }
    }

    /**
     * Sets status bar dark font.
     * 设置状态栏字体颜色，android6.0以上
     */
    private int setStatusBarDarkFont(int uiFlags) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && mBarParams.statusBarDarkFont) {
            return uiFlags | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        } else {
            return uiFlags;
        }
    }

    /**
     * 设置导航栏图标亮色与暗色
     * Sets dark navigation icon.
     */
    private int setNavigationIconDark(int uiFlags) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && mBarParams.navigationBarDarkIcon) {
            return uiFlags | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
        } else {
            return uiFlags;
        }
    }

    @SuppressLint("PrivateApi")
    private void setMIUIBarDark(Window window, String key, boolean dark) {
        if (window != null) {
            Class<? extends Window> clazz = window.getClass();
            try {
                int darkModeFlag;
                Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField(key);
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    //状态栏透明且黑色字体
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);
                } else {
                    //清除黑色字体
                    extraFlagField.invoke(window, 0, darkModeFlag);
                }
            } catch (Exception ignored) {

            }
        }
    }

    /**
     * 适配状态栏与布局重叠问题
     * Fits layout overlap.
     */
    private void fitsLayoutOverlap() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && !mIsFitsLayoutOverlap) {
            switch (mFitsStatusBarType) {
                case FLAG_FITS_TITLE:
                    //通过设置paddingTop重新绘制标题栏高度
                    setTitleBar(mActivity, mBarParams.titleBarView);
                    mIsFitsLayoutOverlap = true;
                    break;
                case FLAG_FITS_TITLE_MARGIN_TOP:
                    //通过设置marginTop重新绘制标题栏高度
                    setTitleBarMarginTop(mActivity, mBarParams.titleBarView);
                    mIsFitsLayoutOverlap = true;
                    break;
                case FLAG_FITS_STATUS:
                    //通过状态栏高度动态设置状态栏布局
                    setStatusBarView(mActivity, mBarParams.statusBarView);
                    mIsFitsLayoutOverlap = true;
                    break;
                default:
                    break;
            }
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
            if (mNavigationObserver != null) {
                mActivity.getContentResolver().unregisterContentObserver(mNavigationObserver);
                mNavigationObserver = null;
            }
            if (mFitsKeyboard != null) {
                mFitsKeyboard.cancel();
                mFitsKeyboard = null;
            }
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
                        mFitsKeyboard = new FitsKeyboard(this, mActivity, mWindow);
                    }
                    mFitsKeyboard.enable(mBarParams.keyboardMode);
                } else {
                    if (mFitsKeyboard != null) {
                        mFitsKeyboard.disable();
                    }
                }
            } else {
                ImmersionBar immersionBar = mImmersionBarMap.get(mActivity.toString());
                if (immersionBar != null) {
                    if (immersionBar.mBarParams.keyboardEnable) {
                        if (immersionBar.mFitsKeyboard == null) {
                            immersionBar.mFitsKeyboard = new FitsKeyboard(immersionBar, immersionBar.mActivity, immersionBar.mWindow);
                        }
                        immersionBar.mFitsKeyboard.enable(immersionBar.mBarParams.keyboardMode);
                    } else {
                        if (immersionBar.mFitsKeyboard != null) {
                            immersionBar.mFitsKeyboard.disable();
                        }
                    }
                }
            }
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

    int getPaddingLeft() {
        return mPaddingLeft;
    }

    int getPaddingTop() {
        return mPaddingTop;
    }

    int getPaddingRight() {
        return mPaddingRight;
    }

    int getPaddingBottom() {
        return mPaddingBottom;
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
     * 单独设置标题栏的高度
     * Sets title bar.
     *
     * @param activity the activity
     * @param view     the view
     */
    public static void setTitleBar(final Activity activity, final View view) {
        if (activity == null) {
            return;
        }
        if (view == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            final ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            if (layoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT ||
                    layoutParams.height == ViewGroup.LayoutParams.MATCH_PARENT) {
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        layoutParams.height = view.getHeight() + getStatusBarHeight(activity);
                        view.setPadding(view.getPaddingLeft(),
                                view.getPaddingTop() + getStatusBarHeight(activity),
                                view.getPaddingRight(),
                                view.getPaddingBottom());
                    }
                });
            } else {
                layoutParams.height += getStatusBarHeight(activity);
                view.setPadding(view.getPaddingLeft(), view.getPaddingTop() + getStatusBarHeight(activity),
                        view.getPaddingRight(), view.getPaddingBottom());
            }
        }
    }

    /**
     * 设置标题栏MarginTop值为导航栏的高度
     * Sets title bar margin top.
     *
     * @param activity the activity
     * @param view     the view
     */
    public static void setTitleBarMarginTop(Activity activity, View view) {
        if (activity == null) {
            return;
        }
        if (view == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            layoutParams.setMargins(layoutParams.leftMargin,
                    layoutParams.topMargin + getStatusBarHeight(activity),
                    layoutParams.rightMargin,
                    layoutParams.bottomMargin);
        }
    }

    /**
     * 单独在标题栏的位置增加view，高度为状态栏的高度
     * Sets status bar view.
     *
     * @param activity the activity
     * @param view     the view
     */
    public static void setStatusBarView(Activity activity, View view) {
        if (activity == null) {
            return;
        }
        if (view == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ViewGroup.LayoutParams params = view.getLayoutParams();
            params.height = getStatusBarHeight(activity);
            view.setLayoutParams(params);
        }
    }

    /**
     * 解决顶部与布局重叠问题
     * Sets fits system windows.
     *
     * @param activity the activity
     */
    public static void setFitsSystemWindows(Activity activity) {
        if (activity == null) {
            return;
        }
        ViewGroup parent = activity.findViewById(android.R.id.content);
        for (int i = 0, count = parent.getChildCount(); i < count; i++) {
            View childView = parent.getChildAt(i);
            if (childView instanceof ViewGroup) {
                if (childView instanceof DrawerLayout) {
                    continue;
                }
                childView.setFitsSystemWindows(true);
                ((ViewGroup) childView).setClipToPadding(true);
            }
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

    /**
     * 是否是刘海屏(在Android P上并不一定准确)
     * Has notch screen boolean.
     *
     * @param activity the activity
     * @return the boolean
     */
    public static boolean hasNotchScreen(@NonNull Activity activity) {
        BarConfig config = new BarConfig(activity);
        return config.hasNotchScreen();
    }

    /**
     * 是否是刘海屏(在Android P上并不一定准确)
     * Has notch screen boolean.
     *
     * @param view the view
     * @return the boolean
     */
    public static boolean hasNotchScreen(@NonNull View view) {
        return NotchUtils.hasNotchScreen(view);
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

    private static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }
}
