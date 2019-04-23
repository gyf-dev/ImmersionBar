package com.gyf.immersionbar.simple.activity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.BarParams;
import com.gyf.immersionbar.ImmersionBar;
import com.gyf.immersionbar.simple.AppManager;
import com.gyf.immersionbar.simple.BuildConfig;
import com.gyf.immersionbar.simple.R;
import com.gyf.immersionbar.simple.adapter.MainAdapter;
import com.gyf.immersionbar.simple.bean.FunBean;
import com.gyf.immersionbar.simple.event.NetworkEvent;
import com.gyf.immersionbar.simple.fragment.SplashFragment;
import com.gyf.immersionbar.simple.model.DataUtils;
import com.gyf.immersionbar.simple.service.NetworkService;
import com.gyf.immersionbar.simple.utils.DensityUtil;
import com.gyf.immersionbar.simple.utils.GlideUtils;
import com.gyf.immersionbar.simple.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import jp.wasabeef.blurry.Blurry;

/**
 * @author geyifeng
 */
public class MainActivity extends BaseActivity implements DrawerLayout.DrawerListener {

    @BindView(R.id.drawer)
    DrawerLayout drawer;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.iv_bg)
    ImageView ivBg;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.mRv)
    RecyclerView mRv;
    private MainAdapter mMainAdapter;

    private int mBannerHeight;
    private Disposable mSubscribe;
    private Intent mNetworkIntent;
    private ImageView mIvHeader;
    private long mFirstPressedTime;
    private SplashFragment mSplashFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        ImmersionBar.with(this).titleBar(R.id.toolbar).init();
    }

    @Override
    protected void initData() {
        super.initData();
        EventBus.getDefault().register(this);
        mNetworkIntent = new Intent(this, NetworkService.class);
        startService(mNetworkIntent);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        showSplash();
        setSidePic();
        mMainAdapter = new MainAdapter();
        tvVersion.setText("当前版本：" + BuildConfig.VERSION_NAME);
        mMainAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mMainAdapter.isFirstOnly(false);
        mRv.setAdapter(mMainAdapter);
        mMainAdapter.setNewData(DataUtils.getMainData(this));
        addHeaderView();
        mBannerHeight = DensityUtil.dip2px(this, 180) - ImmersionBar.getActionBarHeight(this)
                - ImmersionBar.getStatusBarHeight(this);
    }

    @Override
    protected void setListener() {
        super.setListener();
        drawer.addDrawerListener(this);
        mRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int totalDy = 0;

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Glide.with(mActivity).resumeRequests();
                } else {
                    Glide.with(mActivity).pauseRequests();
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalDy += dy;
                if (totalDy <= mBannerHeight) {
                    float alpha = (float) totalDy / mBannerHeight;
                    mToolbar.setBackgroundColor(ColorUtils.blendARGB(Color.TRANSPARENT
                            , ContextCompat.getColor(mActivity, R.color.colorPrimary), alpha));
                } else {
                    mToolbar.setBackgroundColor(ColorUtils.blendARGB(Color.TRANSPARENT
                            , ContextCompat.getColor(mActivity, R.color.colorPrimary), 1));
                }

            }
        });
        mMainAdapter.setOnItemClickListener((adapter, view, position) -> {
            FunBean funBean = (FunBean) adapter.getData().get(position);
            Intent intent = null;
            switch (position) {
                case 0:
                    intent = new Intent(this, ParamsActivity.class);
                    intent.putExtra("title", funBean.getName());
                    break;
                case 1:
                    intent = new Intent(this, KotlinActivity.class);
                    intent.putExtra("title", funBean.getName());
                    break;
                case 2:
                    intent = new Intent(this, PicAndColorActivity.class);
                    break;
                case 3:
                    intent = new Intent(this, PicActivity.class);
                    break;
                case 4:
                    intent = new Intent(this, ColorActivity.class);
                    break;
                case 5:
                    intent = new Intent(this, ShapeActivity.class);
                    break;
                case 6:
                    intent = new Intent(this, BackActivity.class);
                    break;
                case 7:
                    intent = new Intent(this, FragmentActivity.class);
                    break;
                case 8:
                    intent = new Intent(this, DialogActivity.class);
                    break;
                case 9:
                    drawer.openDrawer(Gravity.START);
                    break;
                case 10:
                    intent = new Intent(this, CoordinatorActivity.class);
                    break;
                case 11:
                    intent = new Intent(this, TabLayoutActivity.class);
                    break;
                case 12:
                    intent = new Intent(this, TabLayout2Activity.class);
                    break;
                case 13:
                    intent = new Intent(this, WebActivity.class);
                    break;
                case 14:
                    intent = new Intent(this, ActionBarActivity.class);
                    break;
                case 15:
                    intent = new Intent(this, FlymeActivity.class);
                    break;
                case 16:
                    intent = new Intent(this, OverActivity.class);
                    break;
                case 17:
                    intent = new Intent(this, KeyBoardActivity.class);
                    break;
                case 18:
                    intent = new Intent(this, AllEditActivity.class);
                    break;
                case 19:
                    intent = new Intent(this, LoginActivity.class);
                    break;
                case 20:
                    intent = new Intent(this, WhiteBarActivity.class);
                    break;
                case 21:
                    intent = new Intent(this, AutoDarkModeActivity.class);
                    break;
                case 22:
                    ImmersionBar.with(this).hideBar(BarHide.FLAG_HIDE_STATUS_BAR).init();
                    break;
                case 23:
                    if (ImmersionBar.hasNavigationBar(this)) {
                        ImmersionBar.with(this).hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR).init();
                    } else {
                        Toast.makeText(this, "当前设备没有导航栏或者导航栏已经被隐藏或者低于4.4系统", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 24:
                    ImmersionBar.with(this).hideBar(BarHide.FLAG_HIDE_BAR).init();
                    break;
                case 25:
                    ImmersionBar.with(this).hideBar(BarHide.FLAG_SHOW_BAR).init();
                    break;
                case 26:
                    if (ImmersionBar.hasNavigationBar(this)) {
                        BarParams barParams = ImmersionBar.with(this).getBarParams();
                        if (barParams.fullScreen) {
                            ImmersionBar.with(this).fullScreen(false).navigationBarColor(R.color.colorPrimary).init();
                        } else {
                            ImmersionBar.with(this).fullScreen(true).transparentNavigationBar().init();
                        }
                    } else {
                        Toast.makeText(this, "当前设备没有导航栏或者导航栏已经被隐藏或者低于4.4系统", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 27:
                    if (ImmersionBar.isSupportStatusBarDarkFont()) {
                        ImmersionBar.with(this).statusBarDarkFont(true).init();
                    } else {
                        Toast.makeText(this, "当前设备不支持状态栏字体变色", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 28:
                    ImmersionBar.with(this).statusBarDarkFont(false).init();
                    break;
                default:
                    break;
            }
            if (intent != null) {
                startActivity(intent);
            }
        });
    }


    @OnClick({R.id.ll_github, R.id.ll_jianshu})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.ll_github:
                intent = new Intent(this, BlogActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("blog", "github");
                intent.putExtra("bundle", bundle);
                break;
            case R.id.ll_jianshu:
                intent = new Intent(this, BlogActivity.class);
                Bundle bundle2 = new Bundle();
                bundle2.putString("blog", "jianshu");
                intent.putExtra("bundle", bundle2);
                break;
            default:
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }

    private void addHeaderView() {
        View view = LayoutInflater.from(this).inflate(R.layout.item_image, mRv, false);
        mIvHeader = view.findViewById(R.id.iv_header);
        GlideUtils.load(Utils.getPic(), mIvHeader);
        mMainAdapter.addHeaderView(view);
    }

    /**
     * 展示Splash
     */
    private void showSplash() {
        mSplashFragment = (SplashFragment) getSupportFragmentManager().findFragmentByTag(SplashFragment.class.getSimpleName());
        if (mSplashFragment == null) {
            mSplashFragment = SplashFragment.newInstance();
            mSplashFragment.setOnSplashListener((time, totalTime) -> {
                if (time != 0) {
                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                } else {
                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                }
            });
        }
        getSupportFragmentManager().beginTransaction().add(R.id.fl_content, mSplashFragment, SplashFragment.class.getSimpleName()).commitAllowingStateLoss();
    }

    private void switchPicture() {
        if (Utils.isNetworkConnected(this)) {
            if (mIvHeader != null) {
                mIvHeader.post(() -> {
                    if (mSubscribe == null) {
                        mSubscribe = Observable.interval(10, TimeUnit.SECONDS)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(aLong -> {
                                    ObjectAnimator.ofFloat(mIvHeader, "alpha", 0, 1).setDuration(800).start();
                                    GlideUtils.load(Utils.getPic(), mIvHeader);
                                });
                    }
                });
            }
        }
    }

    private void setSidePic() {
        Glide.with(this).asBitmap().load(Utils.getPic())
                .apply(new RequestOptions().placeholder(R.mipmap.test).error(R.mipmap.test).centerCrop())
                .into(new BitmapImageViewTarget(ivBg) {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        super.onResourceReady(resource, transition);
                        Blurry.with(MainActivity.this).from(resource).into(ivBg);
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        Blurry.with(MainActivity.this).from(BitmapFactory.decodeResource(getResources(), R.mipmap.test)).into(ivBg);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        drawer.removeDrawerListener(this);
        disposedSubscribe();
        stopService(mNetworkIntent);
        EventBus.getDefault().unregister(this);
    }

    private void disposedSubscribe() {
        if (mSubscribe != null && !mSubscribe.isDisposed()) {
            mSubscribe.dispose();
            mSubscribe = null;
        }
    }

    @Override
    public void onDrawerSlide(@NonNull View view, float v) {

    }

    @Override
    public void onDrawerOpened(@NonNull View view) {

    }

    @Override
    public void onDrawerClosed(@NonNull View view) {
        setSidePic();
    }

    @Override
    public void onDrawerStateChanged(int i) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNetworkEvent(NetworkEvent networkEvent) {
        if (networkEvent.isAvailable()) {
            switchPicture();
        } else {
            disposedSubscribe();
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (mSplashFragment != null) {
                if (mSplashFragment.isFinish()) {
                    if (System.currentTimeMillis() - mFirstPressedTime < 2000) {
                        super.onBackPressed();
                        AppManager.getInstance().removeAllActivity();
                    } else {
                        Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                        mFirstPressedTime = System.currentTimeMillis();
                    }
                } else {
                    super.onBackPressed();
                    AppManager.getInstance().removeAllActivity();
                }
            } else {
                super.onBackPressed();
                AppManager.getInstance().removeAllActivity();
            }
        }
    }
}
