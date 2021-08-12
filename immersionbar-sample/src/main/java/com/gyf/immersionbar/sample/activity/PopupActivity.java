package com.gyf.immersionbar.sample.activity;

import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import com.gyf.immersionbar.ImmersionBar;
import com.gyf.immersionbar.OSUtils;
import com.gyf.immersionbar.sample.R;
import com.gyf.immersionbar.sample.utils.Utils;

import butterknife.OnClick;

/**
 * 结合popupWindow使用
 * The type Popup activity.
 *
 * @author geyifeng
 */
public class PopupActivity extends BaseActivity {

    private View mPopupView;
    private PopupWindow mPopupWindow;
    private int mCurPosition;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_popup;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        ImmersionBar.with(this)
                .titleBar(R.id.toolbar)
                //华为手机能手动隐藏导航栏，所以也要做些适配
                .setOnNavigationBarListener((show, type) -> updatePopupView())
                .init();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //适配横竖屏切换
        updatePopupWindow();
        updatePopupView();
    }

    /**
     * On view click.
     *
     * @param view the view
     */
    @OnClick({R.id.btn_full, R.id.btn_top, R.id.btn_bottom, R.id.btn_left, R.id.btn_right})
    public void onViewClick(View view) {
        Integer[] widthAndHeight = Utils.getWidthAndHeight(getWindow());
        mCurPosition = view.getId();
        switch (view.getId()) {
            case R.id.btn_full:
                showPopup(Gravity.NO_GRAVITY, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, R.style.RightAnimation);
                break;
            case R.id.btn_top:
                showPopup(Gravity.TOP, ViewGroup.LayoutParams.MATCH_PARENT, widthAndHeight[1] / 2, R.style.TopAnimation);
                break;
            case R.id.btn_bottom:
                showPopup(Gravity.BOTTOM, ViewGroup.LayoutParams.MATCH_PARENT, widthAndHeight[1] / 2, R.style.BottomAnimation);
                break;
            case R.id.btn_left:
                showPopup(Gravity.START, widthAndHeight[0] / 2, ViewGroup.LayoutParams.MATCH_PARENT, R.style.LeftAnimation);
                break;
            case R.id.btn_right:
                showPopup(Gravity.END, widthAndHeight[0] / 2, ViewGroup.LayoutParams.MATCH_PARENT, R.style.RightAnimation);
                break;
            default:
                break;
        }
    }

    /**
     * 弹出popupWindow
     * Show popup.
     *
     * @param gravity        the gravity
     * @param width          the width
     * @param height         the height
     * @param animationStyle the animation style
     */
    private void showPopup(int gravity, int width, int height, int animationStyle) {
        mPopupView = LayoutInflater.from(mActivity).inflate(R.layout.popup_demo, null);
        mPopupWindow = new PopupWindow(mPopupView, width, height);
        //以下属性响应空白处消失和实体按键返回消失popup
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //沉浸式模式下，以下两个属性并不起作用
        mPopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //重点，此方法可以让布局延伸到状态栏和导航栏
        mPopupWindow.setClippingEnabled(false);
        //设置动画
        mPopupWindow.setAnimationStyle(animationStyle);
        //弹出
        mPopupWindow.showAtLocation(getWindow().getDecorView(), gravity, 0, 0);
        //弹出后背景alpha值
        backgroundAlpha(0.5f);
        //消失后恢复背景alpha值
        mPopupWindow.setOnDismissListener(() -> backgroundAlpha(1f));
        //适配弹出popup后布局被状态栏和导航栏遮挡问题
        updatePopupView();
    }

    /**
     * 调整popupWindow位置以及宽高，不然横竖屏切换会导致显示位置改变以及宽高变化
     * Update popup window.
     */
    private void updatePopupWindow() {
        Integer[] widthAndHeight = Utils.getWidthAndHeight(getWindow());
        switch (mCurPosition) {
            case R.id.btn_top:
            case R.id.btn_bottom:
                mPopupWindow.update(0, 0, ViewGroup.LayoutParams.MATCH_PARENT, widthAndHeight[1] / 2);
                break;
            case R.id.btn_left:
            case R.id.btn_right:
                mPopupWindow.update(0, 0, widthAndHeight[0] / 2, ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            default:
                break;
        }
    }

    /**
     * 调整popupWindow里view的Margins值来适配布局被导航栏遮挡问题，因为要适配横竖屏切换，所以代码有点多
     * Update popup view.
     */
    private void updatePopupView() {
        int navigationBarHeight = ImmersionBar.getNavigationBarHeight(this);
        int navigationBarWidth = ImmersionBar.getNavigationBarWidth(this);
        if (mPopupView != null) {
            ImmersionBar.setTitleBar(this, mPopupView.findViewById(R.id.toolbar));
            View rlContent = mPopupView.findViewById(R.id.rlContent);
            mPopupView.post(() -> {
                boolean isPortrait;
                boolean isLandscapeLeft;
                int rotation = getWindowManager().getDefaultDisplay().getRotation();
                if (rotation == Surface.ROTATION_90) {
                    isPortrait = false;
                    isLandscapeLeft = true;
                } else if (rotation == Surface.ROTATION_270) {
                    isPortrait = false;
                    isLandscapeLeft = false;
                } else {
                    isPortrait = true;
                    isLandscapeLeft = false;
                }
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) rlContent.getLayoutParams();
                switch (mCurPosition) {
                    case R.id.btn_full:
                    case R.id.btn_top:
                    case R.id.btn_bottom:
                        if (isPortrait) {
                            layoutParams.setMargins(0, 0, 0, navigationBarHeight);
                        } else {
                            if (isLandscapeLeft) {
                                layoutParams.setMargins(0, 0, navigationBarWidth, 0);
                            } else {
                                if (OSUtils.isEMUI3_x()) {
                                    layoutParams.setMargins(0, 0, navigationBarWidth, 0);
                                } else {
                                    layoutParams.setMargins(navigationBarWidth, 0, 0, 0);
                                }
                            }
                        }
                        break;
                    case R.id.btn_left:
                        if (isPortrait) {
                            layoutParams.setMargins(0, 0, 0, navigationBarHeight);
                        } else {
                            if (isLandscapeLeft) {
                                layoutParams.setMargins(0, 0, 0, 0);
                            } else {
                                if (OSUtils.isEMUI3_x()) {
                                    layoutParams.setMargins(0, 0, navigationBarWidth, 0);
                                } else {
                                    layoutParams.setMargins(navigationBarWidth, 0, 0, 0);
                                }
                            }
                        }
                        break;
                    case R.id.btn_right:
                        if (isPortrait) {
                            layoutParams.setMargins(0, 0, 0, navigationBarHeight);
                        } else {
                            if (isLandscapeLeft) {
                                layoutParams.setMargins(0, 0, navigationBarWidth, 0);
                            } else {
                                if (OSUtils.isEMUI3_x()) {
                                    layoutParams.setMargins(0, 0, navigationBarWidth, 0);
                                } else {
                                    layoutParams.setMargins(0, 0, 0, 0);
                                }
                            }
                        }
                        break;
                    default:
                        break;
                }
                rlContent.setLayoutParams(layoutParams);
            });
        }
    }

    /**
     * 设置弹出popup，背景alpha值
     *
     * @param bgAlpha 0f - 1f
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }
}
