package com.gyf.barlibrary;

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
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * android 4.4以上沉浸式以及bar的管理
 * Created by gyf on 2017/05/09.
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
public class ImmersionBar {

    private static Map<String, BarParams> mMap = new HashMap<>();
    private static Map<String, BarParams> mTagMap = new HashMap<>();
    private static Map<String, ArrayList<String>> mTagKeyMap = new HashMap<>();

    private Activity mActivity;
    private Window mWindow;
    private ViewGroup mDecorView;
    private ViewGroup mContentView;
    private Dialog mDialog;

    private BarParams mBarParams;
    private BarConfig mConfig;

    private String mActivityName;
    private String mFragmentName;
    private String mImmersionBarName;

    private static final String NAVIGATIONBAR_IS_MIN = "navigationbar_is_min";

    /**
     * 在Activit里初始化
     * Instantiates a new Immersion bar.
     *
     * @param activity the activity
     */
    private ImmersionBar(Activity activity) {
        WeakReference<Activity> activityWeakReference = new WeakReference<>(activity);
        mActivity = activityWeakReference.get();
        mWindow = mActivity.getWindow();
        mActivityName = activity.getClass().getName();
        mImmersionBarName = mActivityName;
        initParams();
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
        if (activity == null) {
            throw new IllegalArgumentException("Activity不能为空!!!");
        }
        WeakReference<Activity> activityWeakReference = new WeakReference<>(activity);
        WeakReference<Fragment> fragmentWeakReference = new WeakReference<>(fragment);
        mActivity = activityWeakReference.get();
        mWindow = mActivity.getWindow();
        mActivityName = mActivity.getClass().getName();
        mFragmentName = mActivityName + "_AND_" + fragmentWeakReference.get().getClass().getName();
        mImmersionBarName = mFragmentName;
        initParams();
    }

    private ImmersionBar(DialogFragment dialogFragment, Dialog dialog) {
        WeakReference<DialogFragment> dialogFragmentWeakReference = new WeakReference<>(dialogFragment);
        WeakReference<Dialog> dialogWeakReference = new WeakReference<>(dialog);
        mActivity = dialogFragmentWeakReference.get().getActivity();
        mDialog = dialogWeakReference.get();
        mWindow = mDialog.getWindow();
        mActivityName = mActivity.getClass().getName();
        mImmersionBarName = mActivityName + "_AND_" + dialogFragmentWeakReference.get().getClass().getName();
        initParams();
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
        WeakReference<Activity> activityWeakReference = new WeakReference<>(activity);
        WeakReference<Dialog> dialogWeakReference = new WeakReference<>(dialog);
        mActivity = activityWeakReference.get();
        mDialog = dialogWeakReference.get();
        mWindow = mDialog.getWindow();
        mActivityName = mActivity.getClass().getName();
        mImmersionBarName = mActivityName + "_AND_" + dialogTag;
        initParams();
    }

    /**
     * 初始化沉浸式默认参数
     * Init params.
     */
    private void initParams() {
        mDecorView = (ViewGroup) mWindow.getDecorView();
        mContentView = (ViewGroup) mDecorView.findViewById(android.R.id.content);
        mConfig = new BarConfig(mActivity);
        if (mMap.get(mImmersionBarName) == null) {
            mBarParams = new BarParams();
            if (!isEmpty(mFragmentName)) { //保证一个activity页面有同一个状态栏view和导航栏view
                if (mMap.get(mActivityName) == null)
                    throw new IllegalArgumentException("在Fragment里使用时，请先在加载Fragment的Activity里初始化！！！");
                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT
                        || OSUtils.isEMUI3_1()) {
                    mBarParams.statusBarView = mMap.get(mActivityName).statusBarView;
                    mBarParams.navigationBarView = mMap.get(mActivityName).navigationBarView;
                }
                mBarParams.keyboardPatch = mMap.get(mActivityName).keyboardPatch;
            }
            mMap.put(mImmersionBarName, mBarParams);
        } else {
            mBarParams = mMap.get(mImmersionBarName);
        }
    }

    /**
     * 初始化Activity
     * With immersion bar.
     *
     * @param activity the activity
     * @return the immersion bar
     */
    public static ImmersionBar with(@NonNull Activity activity) {
        if (activity == null)
            throw new IllegalArgumentException("Activity不能为null");
        return new ImmersionBar(activity);
    }

    /**
     * 调用该方法必须保证加载Fragment的Activity先初始化,已过时，使用with(Activity activity, Fragment fragment)方法
     * With immersion bar.
     *
     * @param fragment the fragment
     * @return the immersion bar
     */
    public static ImmersionBar with(@NonNull Fragment fragment) {
        if (fragment == null)
            throw new IllegalArgumentException("Fragment不能为null");
        return new ImmersionBar(fragment);
    }

    public static ImmersionBar with(@NonNull Activity activity, @NonNull Fragment fragment) {
        if (activity == null)
            throw new IllegalArgumentException("Activity不能为null");
        if (fragment == null)
            throw new IllegalArgumentException("Fragment不能为null");
        return new ImmersionBar(activity, fragment);
    }

    public static ImmersionBar with(@NonNull DialogFragment dialogFragment, @NonNull Dialog dialog) {
        if (dialogFragment == null)
            throw new IllegalArgumentException("DialogFragment不能为null");
        if (dialog == null)
            throw new IllegalArgumentException("Dialog不能为null");
        return new ImmersionBar(dialogFragment, dialog);
    }

    /**
     * 在dialog里使用
     * With immersion bar.
     *
     * @param activity  the activity
     * @param dialog    the dialog
     * @param dialogTag the dialog tag
     * @return the immersion bar
     */
    public static ImmersionBar with(@NonNull Activity activity, @NonNull Dialog dialog, @NonNull String dialogTag) {
        if (activity == null)
            throw new IllegalArgumentException("Activity不能为null");
        if (dialog == null)
            throw new IllegalArgumentException("Dialog不能为null");
        if (isEmpty(dialogTag))
            throw new IllegalArgumentException("tag不能为null或空");
        return new ImmersionBar(activity, dialog, dialogTag);
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
        mBarParams.navigationBarColorTemp = mBarParams.navigationBarColor;
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
        mBarParams.navigationBarColorTemp = mBarParams.navigationBarColor;
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
        mBarParams.navigationBarColorTemp = mBarParams.navigationBarColor;
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
        mBarParams.navigationBarColorTemp = mBarParams.navigationBarColor;
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
        mBarParams.navigationBarColorTemp = mBarParams.navigationBarColor;
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
        mBarParams.navigationBarColorTemp = mBarParams.navigationBarColor;
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
        mBarParams.navigationBarColorTemp = mBarParams.navigationBarColor;
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
        mBarParams.navigationBarColorTemp = mBarParams.navigationBarColor;

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
        mBarParams.darkFont = isDarkFont;
        if (!isDarkFont)
            mBarParams.flymeOSStatusBarFontColor = 0;
        if (isSupportStatusBarDarkFont()) {
            mBarParams.statusBarAlpha = 0;
        } else {
            mBarParams.statusBarAlpha = statusAlpha;
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
                mBarParams.navigationBarColor = Color.TRANSPARENT;
                mBarParams.fullScreenTemp = true;
            } else {
                mBarParams.navigationBarColor = mBarParams.navigationBarColorTemp;
                mBarParams.fullScreenTemp = false;
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
        return this;
    }

    /**
     * 解决布局与状态栏重叠问题，支持侧滑返回
     * Fits system windows immersion bar.
     *
     * @param fits                      the fits
     * @param statusBarColorContentView the status bar color content view  状态栏颜色
     * @return the immersion bar
     */
    public ImmersionBar fitsSystemWindows(boolean fits, @ColorRes int statusBarColorContentView) {
        return fitsSystemWindows(fits, statusBarColorContentView,
                android.R.color.black, 0);
    }

    /**
     * 解决布局与状态栏重叠问题，支持侧滑返回
     * Fits system windows immersion bar.
     *
     * @param fits                               the fits
     * @param statusBarColorContentView          the status bar color content view 状态栏颜色
     * @param statusBarColorContentViewTransform the status bar color content view transform  状态栏变色后的颜色
     * @param statusBarContentViewAlpha          the status bar content view alpha  透明度
     * @return the immersion bar
     */
    public ImmersionBar fitsSystemWindows(boolean fits, @ColorRes int statusBarColorContentView
            , @ColorRes int statusBarColorContentViewTransform, @FloatRange(from = 0f, to = 1f) float statusBarContentViewAlpha) {
        mBarParams.fits = fits;
        mBarParams.statusBarColorContentView = ContextCompat.getColor(mActivity, statusBarColorContentView);
        mBarParams.statusBarColorContentViewTransform = ContextCompat.getColor(mActivity, statusBarColorContentViewTransform);
        mBarParams.statusBarContentViewAlpha = statusBarContentViewAlpha;
        mBarParams.statusBarColorContentView = ContextCompat.getColor(mActivity, statusBarColorContentView);
        mContentView.setBackgroundColor(ColorUtils.blendARGB(mBarParams.statusBarColorContentView,
                mBarParams.statusBarColorContentViewTransform, mBarParams.statusBarContentViewAlpha));
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
            throw new IllegalArgumentException("View参数不能为空");
        }
        mBarParams.statusBarViewByHeight = view;
        return this;
    }

    /**
     * 通过状态栏高度动态设置状态栏布局,只能在Activity中使用
     *
     * @param viewId the view id
     * @return the immersion bar
     */
    public ImmersionBar statusBarView(@IdRes int viewId) {
        View view = mActivity.findViewById(viewId);
        if (view == null) {
            throw new IllegalArgumentException("未找到viewId");
        }
        return statusBarView(view);
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
        View view = rootView.findViewById(viewId);
        if (view == null) {
            throw new IllegalArgumentException("未找到viewId");
        }
        return statusBarView(view);
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
            throw new IllegalArgumentException("View参数不能为空");
        }
        return titleBar(view, true);
    }

    /**
     * 解决状态栏与布局顶部重叠又多了种方法
     * Title bar immersion bar.
     *
     * @param view          the view
     * @param statusBarFlag the status bar flag 默认为true false表示状态栏不支持变色，true表示状态栏支持变色
     * @return the immersion bar
     */
    public ImmersionBar titleBar(View view, boolean statusBarFlag) {
        if (view == null) {
            throw new IllegalArgumentException("View参数不能为空");
        }
        mBarParams.titleBarView = view;
        mBarParams.statusBarFlag = statusBarFlag;
        setTitleBar();
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
        View view = mActivity.findViewById(viewId);
        if (view == null) {
            throw new IllegalArgumentException("参数错误");
        }
        return titleBar(view, true);
    }

    /**
     * Title bar immersion bar.
     *
     * @param viewId        the view id
     * @param statusBarFlag the status bar flag
     * @return the immersion bar
     */
    public ImmersionBar titleBar(@IdRes int viewId, boolean statusBarFlag) {
        View view = mActivity.findViewById(viewId);
        if (view == null) {
            throw new IllegalArgumentException("参数错误");
        }
        return titleBar(view, statusBarFlag);
    }

    /**
     * Title bar immersion bar.
     *
     * @param viewId   the view id
     * @param rootView the root view
     * @return the immersion bar
     */
    public ImmersionBar titleBar(@IdRes int viewId, View rootView) {
        View view = rootView.findViewById(viewId);
        if (view == null) {
            throw new IllegalArgumentException("参数错误");
        }
        return titleBar(view, true);
    }

    /**
     * 解决状态栏与布局顶部重叠又多了种方法，支持任何view
     * Title bar immersion bar.
     *
     * @param viewId        the view id
     * @param rootView      the root view
     * @param statusBarFlag the status bar flag 默认为true false表示状态栏不支持变色，true表示状态栏支持变色
     * @return the immersion bar
     */
    public ImmersionBar titleBar(@IdRes int viewId, View rootView, boolean statusBarFlag) {
        View view = rootView.findViewById(viewId);
        if (view == null) {
            throw new IllegalArgumentException("参数错误");
        }
        return titleBar(view, statusBarFlag);
    }

    /**
     * 绘制标题栏距离顶部的高度为状态栏的高度
     * Title bar margin top immersion bar.
     *
     * @param viewId the view id   标题栏资源id
     * @return the immersion bar
     */
    public ImmersionBar titleBarMarginTop(@IdRes int viewId) {
        return titleBarMarginTop(mActivity.findViewById(viewId));
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
            throw new IllegalArgumentException("参数错误");
        }
        mBarParams.titleBarViewMarginTop = view;
        if (!mBarParams.titleBarViewMarginTopFlag)
            setTitleBarMarginTop();
        return this;
    }

    /**
     * Status bar color transform enable immersion bar.
     *
     * @param statusBarFlag the status bar flag
     * @return the immersion bar
     */
    public ImmersionBar statusBarColorTransformEnable(boolean statusBarFlag) {
        mBarParams.statusBarFlag = statusBarFlag;
        return this;
    }

    /**
     * 一键重置所有参数
     * Reset immersion bar.
     *
     * @return the immersion bar
     */
    public ImmersionBar reset() {
        BarParams barParamsTemp = mBarParams;
        mBarParams = new BarParams();
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT || OSUtils.isEMUI3_1()) {
            mBarParams.statusBarView = barParamsTemp.statusBarView;
            mBarParams.navigationBarView = barParamsTemp.navigationBarView;
        }
        mBarParams.keyboardPatch = barParamsTemp.keyboardPatch;
        mMap.put(mImmersionBarName, mBarParams);
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
        tag = mActivityName + "_TAG_" + tag;
        if (!isEmpty(tag)) {
            BarParams barParams = mBarParams.clone();
            mTagMap.put(tag, barParams);
            ArrayList<String> tagList = mTagKeyMap.get(mActivityName);
            if (tagList != null) {
                if (!tagList.contains(tag))
                    tagList.add(tag);
            } else {
                tagList = new ArrayList<>();
                tagList.add(tag);
            }
            mTagKeyMap.put(mActivityName, tagList);
        }
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
        if (!isEmpty(tag)) {
            BarParams barParams = mTagMap.get(mActivityName + "_TAG_" + tag);
            if (barParams != null) {
                mBarParams = barParams.clone();
            }
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
        return keyboardEnable(enable, WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
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
        if (mBarParams.onKeyboardListener == null)
            mBarParams.onKeyboardListener = onKeyboardListener;
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
     * 当xml里使用android:fitsSystemWindows="true"属性时，
     * 解决4.4和emui3.1手机底部有时会出现多余空白的问题 ，已过时，代码中没用的此处
     * Fix margin atbottom immersion bar.
     *
     * @param fixMarginAtBottom the fix margin atbottom
     * @return the immersion bar
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
        mMap.put(mImmersionBarName, mBarParams);
        initBar();   //初始化沉浸式
        setStatusBarView();  //通过状态栏高度动态设置状态栏布局
        transformView();  //变色view
        keyboardEnable();  //解决软键盘与底部输入框冲突问题
        registerEMUI3_x();  //解决华为emui3.1或者3.0导航栏手动隐藏的问题
    }

    /**
     * 当Activity/Fragment/Dialog关闭的时候调用
     */
    public void destroy() {
        unRegisterEMUI3_x();
        if (mBarParams.keyboardPatch != null) {
            mBarParams.keyboardPatch.disable(mBarParams.keyboardMode);  //取消监听
            mBarParams.keyboardPatch = null;
        }
        if (mDecorView != null)
            mDecorView = null;
        if (mContentView != null)
            mContentView = null;
        if (mConfig != null)
            mConfig = null;
        if (mWindow != null)
            mWindow = null;
        if (mDialog != null)
            mDialog = null;
        if (mActivity != null)
            mActivity = null;
        if (!isEmpty(mImmersionBarName)) {
            if (mBarParams != null)
                mBarParams = null;
            ArrayList<String> tagList = mTagKeyMap.get(mActivityName);
            if (tagList != null && tagList.size() > 0) {
                for (String tag : tagList) {
                    mTagMap.remove(tag);
                }
                mTagKeyMap.remove(mActivityName);
            }
            mMap.remove(mImmersionBarName);
        }
    }

    /**
     * 初始化状态栏和导航栏
     */
    private void initBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;  //防止系统栏隐藏时内容区域大小发生变化
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !OSUtils.isEMUI3_1()) {
                uiFlags = initBarAboveLOLLIPOP(uiFlags); //初始化5.0以上，包含5.0
                uiFlags = setStatusBarDarkFont(uiFlags); //android 6.0以上设置状态栏字体为暗色
                supportActionBar();
            } else {
                initBarBelowLOLLIPOP(); //初始化5.0以下，4.4以上沉浸式
                solveNavigation();  //解决android4.4有导航栏的情况下，activity底部被导航栏遮挡的问题和android 5.0以下解决状态栏和布局重叠问题
            }
            uiFlags = hideBar(uiFlags);  //隐藏状态栏或者导航栏
            mWindow.getDecorView().setSystemUiVisibility(uiFlags);
        }
        if (OSUtils.isMIUI6Later())
            setMIUIStatusBarDarkFont(mWindow, mBarParams.darkFont);         //修改miui状态栏字体颜色
        if (OSUtils.isFlymeOS4Later()) {          // 修改Flyme OS状态栏字体颜色
            if (mBarParams.flymeOSStatusBarFontColor != 0) {
                FlymeOSStatusBarFontUtils.setStatusBarDarkIcon(mActivity, mBarParams.flymeOSStatusBarFontColor);
            } else {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
                    FlymeOSStatusBarFontUtils.setStatusBarDarkIcon(mActivity, mBarParams.darkFont);
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
        uiFlags |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;  //Activity全屏显示，但状态栏不会被隐藏覆盖，状态栏依然可见，Activity顶端布局部分会被状态栏遮住。
        if (mBarParams.fullScreen && mBarParams.navigationBarEnable) {
            uiFlags |= View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION; //Activity全屏显示，但导航栏不会被隐藏覆盖，导航栏依然可见，Activity底部布局部分会被导航栏遮住。
        }
        mWindow.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (mConfig.hasNavigtionBar()) {  //判断是否存在导航栏
            mWindow.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        mWindow.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);  //需要设置这个才能设置状态栏颜色
        if (mBarParams.statusBarFlag)
            mWindow.setStatusBarColor(ColorUtils.blendARGB(mBarParams.statusBarColor,
                    mBarParams.statusBarColorTransform, mBarParams.statusBarAlpha));  //设置状态栏颜色
        else
            mWindow.setStatusBarColor(ColorUtils.blendARGB(mBarParams.statusBarColor,
                    Color.TRANSPARENT, mBarParams.statusBarAlpha));  //设置状态栏颜色
        if (mBarParams.navigationBarEnable)
            mWindow.setNavigationBarColor(ColorUtils.blendARGB(mBarParams.navigationBarColor,
                    mBarParams.navigationBarColorTransform, mBarParams.navigationBarAlpha));  //设置导航栏颜色
        return uiFlags;
    }

    /**
     * 初始化android 4.4和emui3.1状态栏和导航栏
     */
    private void initBarBelowLOLLIPOP() {
        mWindow.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//透明状态栏
        setupStatusBarView(); //创建一个假的状态栏
        if (mConfig.hasNavigtionBar()) {  //判断是否存在导航栏，是否禁止设置导航栏
            if (mBarParams.navigationBarEnable && mBarParams.navigationBarWithKitkatEnable)
                mWindow.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);//透明导航栏，设置这个，如果有导航栏，底部布局会被导航栏遮住
            else
                mWindow.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            setupNavBarView();   //创建一个假的导航栏
        }
    }

    /**
     * 设置一个可以自定义颜色的状态栏
     */
    private void setupStatusBarView() {
        if (mBarParams.statusBarView == null) {
            mBarParams.statusBarView = new View(mActivity);
        }
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                mConfig.getStatusBarHeight());
        params.gravity = Gravity.TOP;
        mBarParams.statusBarView.setLayoutParams(params);
        if (mBarParams.statusBarFlag)
            mBarParams.statusBarView.setBackgroundColor(ColorUtils.blendARGB(mBarParams.statusBarColor,
                    mBarParams.statusBarColorTransform, mBarParams.statusBarAlpha));
        else
            mBarParams.statusBarView.setBackgroundColor(ColorUtils.blendARGB(mBarParams.statusBarColor,
                    Color.TRANSPARENT, mBarParams.statusBarAlpha));
        mBarParams.statusBarView.setVisibility(View.VISIBLE);
        ViewGroup viewGroup = (ViewGroup) mBarParams.statusBarView.getParent();
        if (viewGroup != null)
            viewGroup.removeView(mBarParams.statusBarView);
        mDecorView.addView(mBarParams.statusBarView);
    }

    /**
     * 设置一个可以自定义颜色的导航栏
     */
    private void setupNavBarView() {
        if (mBarParams.navigationBarView == null) {
            mBarParams.navigationBarView = new View(mActivity);
        }
        FrameLayout.LayoutParams params;
        if (mConfig.isNavigationAtBottom()) {
            params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, mConfig.getNavigationBarHeight());
            params.gravity = Gravity.BOTTOM;
        } else {
            params = new FrameLayout.LayoutParams(mConfig.getNavigationBarWidth(), FrameLayout.LayoutParams.MATCH_PARENT);
            params.gravity = Gravity.END;
        }
        mBarParams.navigationBarView.setLayoutParams(params);
        if (mBarParams.navigationBarEnable && mBarParams.navigationBarWithKitkatEnable) {
            if (!mBarParams.fullScreen && (mBarParams.navigationBarColorTransform == Color.TRANSPARENT)) {
                mBarParams.navigationBarView.setBackgroundColor(ColorUtils.blendARGB(mBarParams.navigationBarColor,
                        Color.BLACK, mBarParams.navigationBarAlpha));
            } else {
                mBarParams.navigationBarView.setBackgroundColor(ColorUtils.blendARGB(mBarParams.navigationBarColor,
                        mBarParams.navigationBarColorTransform, mBarParams.navigationBarAlpha));
            }
        } else
            mBarParams.navigationBarView.setBackgroundColor(Color.TRANSPARENT);
        mBarParams.navigationBarView.setVisibility(View.VISIBLE);
        ViewGroup viewGroup = (ViewGroup) mBarParams.navigationBarView.getParent();
        if (viewGroup != null)
            viewGroup.removeView(mBarParams.navigationBarView);
        mDecorView.addView(mBarParams.navigationBarView);
    }

    /**
     * 解决安卓4.4和EMUI3.1导航栏与状态栏的问题，以及系统属性fitsSystemWindows的坑
     */
    private void solveNavigation() {
        for (int i = 0, count = mContentView.getChildCount(); i < count; i++) {
            View childView = mContentView.getChildAt(i);
            if (childView instanceof ViewGroup) {
                if (childView instanceof DrawerLayout) {
                    View childAt1 = ((DrawerLayout) childView).getChildAt(0);
                    if (childAt1 != null) {
                        mBarParams.systemWindows = childAt1.getFitsSystemWindows();
                        if (mBarParams.systemWindows) {
                            mContentView.setPadding(0, 0, 0, 0);
                            return;
                        }
                    }
                } else {
                    mBarParams.systemWindows = childView.getFitsSystemWindows();
                    if (mBarParams.systemWindows) {
                        mContentView.setPadding(0, 0, 0, 0);
                        return;
                    }
                }
            }

        }
        // 解决android4.4有导航栏的情况下，activity底部被导航栏遮挡的问题
        if (mConfig.hasNavigtionBar() && !mBarParams.fullScreenTemp && !mBarParams.fullScreen) {
            if (mConfig.isNavigationAtBottom()) { //判断导航栏是否在底部
                if (!mBarParams.isSupportActionBar) { //判断是否支持actionBar
                    if (mBarParams.navigationBarEnable && mBarParams.navigationBarWithKitkatEnable) {
                        if (mBarParams.fits)
                            mContentView.setPadding(0, mConfig.getStatusBarHeight(),
                                    0, mConfig.getNavigationBarHeight()); //有导航栏，获得rootView的根节点，然后设置距离底部的padding值为导航栏的高度值
                        else
                            mContentView.setPadding(0, 0, 0, mConfig.getNavigationBarHeight());
                    } else {
                        if (mBarParams.fits)
                            mContentView.setPadding(0, mConfig.getStatusBarHeight(), 0, 0);
                        else
                            mContentView.setPadding(0, 0, 0, 0);
                    }
                } else {
                    //支持有actionBar的界面
                    if (mBarParams.navigationBarEnable && mBarParams.navigationBarWithKitkatEnable)
                        mContentView.setPadding(0, mConfig.getStatusBarHeight() +
                                mConfig.getActionBarHeight() + 10, 0, mConfig.getNavigationBarHeight());
                    else
                        mContentView.setPadding(0, mConfig.getStatusBarHeight() +
                                mConfig.getActionBarHeight() + 10, 0, 0);
                }
            } else {
                if (!mBarParams.isSupportActionBar) {
                    if (mBarParams.navigationBarEnable && mBarParams.navigationBarWithKitkatEnable) {
                        if (mBarParams.fits)
                            mContentView.setPadding(0, mConfig.getStatusBarHeight(),
                                    mConfig.getNavigationBarWidth(), 0); //不在底部，设置距离右边的padding值为导航栏的宽度值
                        else
                            mContentView.setPadding(0, 0, mConfig.getNavigationBarWidth(), 0);
                    } else {
                        if (mBarParams.fits)
                            mContentView.setPadding(0, mConfig.getStatusBarHeight(), 0, 0);
                        else
                            mContentView.setPadding(0, 0, 0, 0);
                    }
                } else {
                    //支持有actionBar的界面
                    if (mBarParams.navigationBarEnable && mBarParams.navigationBarWithKitkatEnable)
                        mContentView.setPadding(0, mConfig.getStatusBarHeight() +
                                mConfig.getActionBarHeight() + 10, mConfig.getNavigationBarWidth(), 0);
                    else
                        mContentView.setPadding(0, mConfig.getStatusBarHeight() +
                                mConfig.getActionBarHeight() + 10, 0, 0);
                }
            }
        } else {
            if (!mBarParams.isSupportActionBar) {
                if (mBarParams.fits)
                    mContentView.setPadding(0, mConfig.getStatusBarHeight(), 0, 0);
                else
                    mContentView.setPadding(0, 0, 0, 0);
            } else {
                //支持有actionBar的界面
                mContentView.setPadding(0, mConfig.getStatusBarHeight() + mConfig.getActionBarHeight() + 10, 0, 0);
            }
        }
    }

    /**
     * 注册emui3.x导航栏监听函数
     * Register emui 3 x.
     */
    private void registerEMUI3_x() {
        if ((OSUtils.isEMUI3_1() || OSUtils.isEMUI3_0()) && mConfig.hasNavigtionBar()
                && mBarParams.navigationBarEnable && mBarParams.navigationBarWithKitkatEnable) {
            if (mBarParams.navigationStatusObserver == null && mBarParams.navigationBarView != null) {
                mBarParams.navigationStatusObserver = new ContentObserver(new Handler()) {
                    @Override
                    public void onChange(boolean selfChange) {
                        int navigationBarIsMin = Settings.System.getInt(mActivity.getContentResolver(),
                                NAVIGATIONBAR_IS_MIN, 0);
                        if (navigationBarIsMin == 1) {
                            //导航键隐藏了
                            mBarParams.navigationBarView.setVisibility(View.GONE);
                            mContentView.setPadding(0, mContentView.getPaddingTop(), 0, 0);
                        } else {
                            //导航键显示了
                            mBarParams.navigationBarView.setVisibility(View.VISIBLE);
                            if (!mBarParams.systemWindows) {
                                if (mConfig.isNavigationAtBottom())
                                    mContentView.setPadding(0, mContentView.getPaddingTop(), 0, mConfig.getNavigationBarHeight());
                                else
                                    mContentView.setPadding(0, mContentView.getPaddingTop(), mConfig.getNavigationBarWidth(), 0);
                            } else
                                mContentView.setPadding(0, mContentView.getPaddingTop(), 0, 0);
                        }
                    }
                };
            }
            mActivity.getContentResolver().registerContentObserver(Settings.System.getUriFor
                    (NAVIGATIONBAR_IS_MIN), true, mBarParams.navigationStatusObserver);
        }
    }

    /**
     * 取消注册emui3.x导航栏监听函数
     * Un register emui 3 x.
     */
    private void unRegisterEMUI3_x() {
        if ((OSUtils.isEMUI3_1() || OSUtils.isEMUI3_0()) && mConfig.hasNavigtionBar()
                && mBarParams.navigationBarEnable && mBarParams.navigationBarWithKitkatEnable) {
            if (mBarParams.navigationStatusObserver != null && mBarParams.navigationBarView != null)
                mActivity.getContentResolver().unregisterContentObserver(mBarParams.navigationStatusObserver);
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
            }
        }
        return uiFlags | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
    }

    /**
     * Sets status bar dark font.
     * 设置状态栏字体颜色，android6.0以上
     */
    private int setStatusBarDarkFont(int uiFlags) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && mBarParams.darkFont) {
            return uiFlags | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        } else {
            return uiFlags;
        }
    }

    /**
     * 变色view
     * <p>
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
                    if (Math.abs(mBarParams.viewAlpha - 0.0f) == 0)
                        view.setBackgroundColor(ColorUtils.blendARGB(colorBefore, colorAfter, mBarParams.statusBarAlpha));
                    else
                        view.setBackgroundColor(ColorUtils.blendARGB(colorBefore, colorAfter, mBarParams.viewAlpha));
                }
            }
        }
    }


    /**
     * 通过状态栏高度动态设置状态栏布局
     */
    private void setStatusBarView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && mBarParams.statusBarViewByHeight != null) {
            ViewGroup.LayoutParams params = mBarParams.statusBarViewByHeight.getLayoutParams();
            params.height = mConfig.getStatusBarHeight();
            mBarParams.statusBarViewByHeight.setLayoutParams(params);
        }
    }

    /**
     * 重新绘制标题栏高度，解决状态栏与顶部重叠问题
     * Sets title bar.
     */
    private void setTitleBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && mBarParams.titleBarView != null) {
            final ViewGroup.LayoutParams layoutParams = mBarParams.titleBarView.getLayoutParams();
            if (layoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT ||
                    layoutParams.height == ViewGroup.LayoutParams.MATCH_PARENT) {
                mBarParams.titleBarView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        mBarParams.titleBarView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        if (mBarParams.titleBarHeight == 0)
                            mBarParams.titleBarHeight = mBarParams.titleBarView.getHeight() + mConfig.getStatusBarHeight();
                        if (mBarParams.titleBarPaddingTopHeight == 0)
                            mBarParams.titleBarPaddingTopHeight = mBarParams.titleBarView.getPaddingTop()
                                    + mConfig.getStatusBarHeight();
                        layoutParams.height = mBarParams.titleBarHeight;
                        mBarParams.titleBarView.setPadding(mBarParams.titleBarView.getPaddingLeft(),
                                mBarParams.titleBarPaddingTopHeight,
                                mBarParams.titleBarView.getPaddingRight(),
                                mBarParams.titleBarView.getPaddingBottom());
                        mBarParams.titleBarView.setLayoutParams(layoutParams);
                    }
                });
            } else {
                if (mBarParams.titleBarHeight == 0)
                    mBarParams.titleBarHeight = layoutParams.height + mConfig.getStatusBarHeight();
                if (mBarParams.titleBarPaddingTopHeight == 0)
                    mBarParams.titleBarPaddingTopHeight = mBarParams.titleBarView.getPaddingTop()
                            + mConfig.getStatusBarHeight();
                layoutParams.height = mBarParams.titleBarHeight;
                mBarParams.titleBarView.setPadding(mBarParams.titleBarView.getPaddingLeft(),
                        mBarParams.titleBarPaddingTopHeight,
                        mBarParams.titleBarView.getPaddingRight(),
                        mBarParams.titleBarView.getPaddingBottom());
                mBarParams.titleBarView.setLayoutParams(layoutParams);
            }
        }
    }

    /**
     * 绘制标题栏距离顶部的高度为状态栏的高度
     * Sets title bar margin top.
     */
    private void setTitleBarMarginTop() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) mBarParams.titleBarViewMarginTop.getLayoutParams();
            layoutParams.setMargins(layoutParams.leftMargin,
                    layoutParams.topMargin + mConfig.getStatusBarHeight(),
                    layoutParams.rightMargin,
                    layoutParams.bottomMargin);
            mBarParams.titleBarViewMarginTopFlag = true;
        }
    }

    /**
     * 支持actionBar的界面
     * Support action bar.
     */
    private void supportActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !OSUtils.isEMUI3_1()) {
            for (int i = 0, count = mContentView.getChildCount(); i < count; i++) {
                View childView = mContentView.getChildAt(i);
                if (childView instanceof ViewGroup) {
                    mBarParams.systemWindows = childView.getFitsSystemWindows();
                    if (mBarParams.systemWindows) {
                        mContentView.setPadding(0, 0, 0, 0);
                        return;
                    }
                }
            }
            if (mBarParams.isSupportActionBar) {
                mContentView.setPadding(0, mConfig.getStatusBarHeight() + mConfig.getActionBarHeight(), 0, 0);
            } else {
                if (mBarParams.fits)
                    mContentView.setPadding(0, mConfig.getStatusBarHeight(), 0, 0);
                else
                    mContentView.setPadding(0, 0, 0, 0);
            }
        }
    }

    /**
     * 解决底部输入框与软键盘问题
     * Keyboard enable.
     */
    private void keyboardEnable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (mBarParams.keyboardPatch == null) {
                mBarParams.keyboardPatch = KeyboardPatch.patch(mActivity, mWindow);
            }
            mBarParams.keyboardPatch.setBarParams(mBarParams);
            if (mBarParams.keyboardEnable) {  //解决软键盘与底部输入框冲突问题
                mBarParams.keyboardPatch.enable(mBarParams.keyboardMode);
            } else {
                mBarParams.keyboardPatch.disable(mBarParams.keyboardMode);
            }
        }
    }

    /**
     * 设置状态栏字体图标为深色，需要MIUIV6以上
     *
     * @return boolean 成功执行返回true
     */
    private void setMIUIStatusBarDarkFont(Window window, boolean darkFont) {
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (darkFont) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 单独设置标题栏的高度
     * Sets title bar.
     *
     * @param activity the activity
     * @param view     the view
     */
    public static void setTitleBar(final Activity activity, final View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            final ViewGroup.LayoutParams lp = view.getLayoutParams();
            if (lp.height == ViewGroup.LayoutParams.WRAP_CONTENT) {  //解决状态栏高度为warp_content或match_parent问题
                view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        lp.height = view.getHeight() + getStatusBarHeight(activity);
                        view.setPadding(view.getPaddingLeft(), view.getPaddingTop() + getStatusBarHeight(activity),
                                view.getPaddingRight(), view.getPaddingBottom());
                    }
                });
            } else {
                lp.height += getStatusBarHeight(activity);
                view.setPadding(view.getPaddingLeft(), view.getPaddingTop() + getStatusBarHeight(activity),
                        view.getPaddingRight(), view.getPaddingBottom());
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
    public static void setStatusBarView(Activity activity, View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ViewGroup.LayoutParams params = view.getLayoutParams();
            params.height = getStatusBarHeight(activity);
            view.setLayoutParams(params);
        }
    }

    /**
     * 设置标题栏MarginTop值为导航栏的高度
     * Sets title bar margin top.
     *
     * @param activity the activity
     * @param view     the view
     */
    public static void setTitleBarMarginTop(Activity activity, @NonNull View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            layoutParams.setMargins(layoutParams.leftMargin,
                    layoutParams.topMargin + getStatusBarHeight(activity),
                    layoutParams.rightMargin,
                    layoutParams.bottomMargin);
        }
    }

    /**
     * 解决顶部与布局重叠问题
     * Sets fits system windows.
     *
     * @param activity the activity
     */
    public static void setFitsSystemWindows(Activity activity) {
        ViewGroup parent = (ViewGroup) activity.findViewById(android.R.id.content);
        for (int i = 0, count = parent.getChildCount(); i < count; i++) {
            View childView = parent.getChildAt(i);
            if (childView instanceof ViewGroup) {
                childView.setFitsSystemWindows(true);
                ((ViewGroup) childView).setClipToPadding(true);
            }
        }
    }

    /**
     * Has navigtion bar boolean.
     * 判断是否存在导航栏
     *
     * @param activity the activity
     * @return the boolean
     */
    @TargetApi(14)
    public static boolean hasNavigationBar(Activity activity) {
        BarConfig config = new BarConfig(activity);
        return config.hasNavigtionBar();
    }

    /**
     * Gets navigation bar height.
     * 获得导航栏的高度
     *
     * @param activity the activity
     * @return the navigation bar height
     */
    @TargetApi(14)
    public static int getNavigationBarHeight(Activity activity) {
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
    public static int getNavigationBarWidth(Activity activity) {
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
    public static boolean isNavigationAtBottom(Activity activity) {
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
    public static int getStatusBarHeight(Activity activity) {
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
    public static int getActionBarHeight(Activity activity) {
        BarConfig config = new BarConfig(activity);
        return config.getActionBarHeight();
    }

    /**
     * 判断手机支不支持状态栏字体变色
     * Is support status bar dark font boolean.
     *
     * @return the boolean
     */
    public static boolean isSupportStatusBarDarkFont() {
        if (OSUtils.isMIUI6Later() || OSUtils.isFlymeOS4Later()
                || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)) {
            return true;
        } else
            return false;
    }

    /**
     * 隐藏状态栏
     * Hide status bar.
     *
     * @param window the window
     */
    public static void hideStatusBar(Window window) {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * Gets bar params.
     *
     * @return the bar params
     */
    public BarParams getBarParams() {
        return mBarParams;
    }

    public BarParams getTagBarParams(String tag) {
        BarParams barParams = null;
        if (!isEmpty(tag)) {
            barParams = mTagMap.get(mActivityName + "_TAG_" + tag);
        }
        return barParams;
    }

    private static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }
}
