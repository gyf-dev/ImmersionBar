package com.gyf.immersionbar.sample.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.BarParams;
import com.gyf.immersionbar.BarProperties;
import com.gyf.immersionbar.ImmersionBar;
import com.gyf.immersionbar.sample.AppManager;
import com.gyf.immersionbar.sample.BuildConfig;
import com.gyf.immersionbar.sample.R;
import com.gyf.immersionbar.sample.adapter.BannerAdapter;
import com.gyf.immersionbar.sample.adapter.MainAdapter;
import com.gyf.immersionbar.sample.bean.FunBean;
import com.gyf.immersionbar.sample.event.NetworkEvent;
import com.gyf.immersionbar.sample.fragment.SplashFragment;
import com.gyf.immersionbar.sample.model.DataUtils;
import com.gyf.immersionbar.sample.utils.DensityUtil;
import com.gyf.immersionbar.sample.utils.GlideUtils;
import com.gyf.immersionbar.sample.utils.Utils;
import com.gyf.immersionbar.sample.utils.ViewUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

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

    /**
     * splash页面
     */
    private SplashFragment mSplashFragment;

    private MainAdapter mMainAdapter;
    private BannerAdapter mBannerAdapter;

    private LinearLayoutManager mLayoutManager;
    private View mNetworkView;

    private int mBannerHeight;
    private int mBannerPosition = -1;

    private long mFirstPressedTime;
    private ImageView mIvBanner;
    private ArrayList<FunBean> mMainData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        drawer.removeDrawerListener(this);
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        ImmersionBar.with(this).titleBar(R.id.toolbar).setOnBarListener(this::adjustView).init();
    }

    @Override
    protected void initData() {
        super.initData();
        mMainData = DataUtils.getMainData(this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        showSplash();
        GlideUtils.loadBlurry(ivBg, Utils.getPic());
        mMainAdapter = new MainAdapter();
        tvVersion.setText("当前版本：" + BuildConfig.VERSION_NAME);
        mMainAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mMainAdapter.isFirstOnly(false);
        mRv.setAdapter(mMainAdapter);
        mMainAdapter.setNewData(mMainData);
        addHeaderView();
        mBannerHeight = DensityUtil.dip2px(this, 180)
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
                if (totalDy < 0) {
                    totalDy = 0;
                }
                if (totalDy < mBannerHeight) {
                    float alpha = (float) totalDy / mBannerHeight;
                    mToolbar.setAlpha(alpha);
                } else {
                    mToolbar.setAlpha(1);
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
                    intent = new Intent(this, PopupActivity.class);
                    break;
                case 10:
                    drawer.openDrawer(GravityCompat.START);
                    break;
                case 11:
                    intent = new Intent(this, CoordinatorActivity.class);
                    break;
                case 12:
                    intent = new Intent(this, TabLayoutActivity.class);
                    break;
                case 13:
                    intent = new Intent(this, TabLayout2Activity.class);
                    break;
                case 14:
                    intent = new Intent(this, WebActivity.class);
                    break;
                case 15:
                    intent = new Intent(this, ActionBarActivity.class);
                    break;
                case 16:
                    intent = new Intent(this, FlymeActivity.class);
                    break;
                case 17:
                    intent = new Intent(this, OverActivity.class);
                    break;
                case 18:
                    intent = new Intent(this, KeyBoardActivity.class);
                    break;
                case 19:
                    intent = new Intent(this, AllEditActivity.class);
                    break;
                case 20:
                    intent = new Intent(this, LoginActivity.class);
                    break;
                case 21:
                    intent = new Intent(this, WhiteBarActivity.class);
                    break;
                case 22:
                    intent = new Intent(this, AutoDarkModeActivity.class);
                    break;
                case 23:
                    ImmersionBar.with(this).hideBar(BarHide.FLAG_HIDE_STATUS_BAR).init();
                    break;
                case 24:
                    if (ImmersionBar.hasNavigationBar(this)) {
                        ImmersionBar.with(this).hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR).init();
                    } else {
                        Toast.makeText(this, "当前设备没有导航栏或者导航栏已经被隐藏或者低于4.4系统", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 25:
                    ImmersionBar.with(this).hideBar(BarHide.FLAG_HIDE_BAR).init();
                    break;
                case 26:
                    ImmersionBar.with(this).hideBar(BarHide.FLAG_SHOW_BAR).init();
                    break;
                case 27:
                    if (ImmersionBar.hasNavigationBar(this)) {
                        BarParams barParams = ImmersionBar.with(this).getBarParams();
                        if (barParams.fullScreen) {
                            ImmersionBar.with(this).fullScreen(false).navigationBarColor(R.color.colorPrimary).navigationBarDarkIcon(false).init();
                        } else {
                            ImmersionBar.with(this).fullScreen(true).transparentNavigationBar().navigationBarDarkIcon(true).init();
                        }
                    } else {
                        Toast.makeText(this, "当前设备没有导航栏或者导航栏已经被隐藏或者低于4.4系统", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 28:
                    if (ImmersionBar.isSupportStatusBarDarkFont()) {
                        ImmersionBar.with(this).statusBarDarkFont(true).init();
                    } else {
                        Toast.makeText(this, "当前设备不支持状态栏字体变色", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 29:
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


    /**
     * 展示Splash
     */
    private void showSplash() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        mSplashFragment = (SplashFragment) getSupportFragmentManager().findFragmentByTag(SplashFragment.class.getSimpleName());
        if (mSplashFragment != null) {
            if (mSplashFragment.isAdded()) {
                transaction.show(mSplashFragment).commitAllowingStateLoss();
            } else {
                transaction.remove(mSplashFragment).commitAllowingStateLoss();
                mSplashFragment = SplashFragment.newInstance();
                transaction.add(R.id.fl_content, mSplashFragment, SplashFragment.class.getSimpleName()).commitAllowingStateLoss();
            }
        } else {
            mSplashFragment = SplashFragment.newInstance();
            transaction.add(R.id.fl_content, mSplashFragment, SplashFragment.class.getSimpleName()).commitAllowingStateLoss();
        }
        mSplashFragment.setOnSplashListener((time, totalTime) -> {
            if (time != 0) {
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            } else {
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }
        });
    }

    private void addHeaderView() {
        addBannerView();
        addNetworkView();
    }

    private void addBannerView() {
        View bannerView = LayoutInflater.from(this).inflate(R.layout.item_main_banner, mRv, false);
        mIvBanner = bannerView.findViewById(R.id.iv_banner);
        RecyclerView recyclerView = bannerView.findViewById(R.id.rv_content);
        ViewUtils.increaseViewHeightByStatusBarHeight(this, mIvBanner);
        ImmersionBar.setTitleBarMarginTop(this, recyclerView);

        mLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        mBannerAdapter = new BannerAdapter(Utils.getPics());
        recyclerView.setAdapter(mBannerAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mBannerPosition != mLayoutManager.findFirstVisibleItemPosition()) {
                    mBannerPosition = mLayoutManager.findFirstVisibleItemPosition();
                    ArrayList<String> data = mBannerAdapter.getData();
                    String s = data.get(mBannerPosition % data.size());
                    GlideUtils.loadBlurry(mIvBanner, s);
                }
            }
        });
        mMainAdapter.addHeaderView(bannerView);
    }

    private void addNetworkView() {
        mNetworkView = LayoutInflater.from(this).inflate(R.layout.item_network, mRv, false);
        if (!Utils.isNetworkConnected(this)) {
            mMainAdapter.addHeaderView(mNetworkView);
        }
    }


    /**
     * 适配刘海屏遮挡数据问题
     * Adjust view.
     *
     * @param barProperties the bar properties,ImmersionBar#setOnBarListener
     */
    private void adjustView(BarProperties barProperties) {
        if (barProperties.isNotchScreen()) {
            if (mMainData != null) {
                for (FunBean funBean : mMainData) {
                    if (barProperties.isPortrait()) {
                        funBean.setMarginStart(DensityUtil.dip2px(this, 8));
                        funBean.setMarginEnd(DensityUtil.dip2px(this, 8));
                    } else {
                        if (barProperties.isLandscapeLeft()) {
                            funBean.setMarginStart(DensityUtil.dip2px(this, 8) + barProperties.getNotchHeight());
                            funBean.setMarginEnd(DensityUtil.dip2px(this, 8));
                        } else {
                            funBean.setMarginStart(DensityUtil.dip2px(this, 8));
                            funBean.setMarginEnd(DensityUtil.dip2px(this, 8) + barProperties.getNotchHeight());
                        }
                    }
                }
            }
            if (mMainAdapter != null) {
                mMainAdapter.notifyDataSetChanged();
            }
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
        GlideUtils.loadBlurry(ivBg, Utils.getPic());
    }

    @Override
    public void onDrawerStateChanged(int i) {

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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNetworkEvent(NetworkEvent networkEvent) {
        if (mNetworkView != null) {
            if (networkEvent.isAvailable()) {
                if (mNetworkView.getParent() != null) {
                    mMainAdapter.removeHeaderView(mNetworkView);
                }
                if (mBannerAdapter != null && mBannerPosition != -1) {
                    mBannerAdapter.notifyDataSetChanged();
                    ArrayList<String> data = mBannerAdapter.getData();
                    String s = data.get(mBannerPosition % data.size());
                    GlideUtils.loadBlurry(mIvBanner, s);
                }
            } else {
                if (mNetworkView.getParent() == null) {
                    mMainAdapter.addHeaderView(mNetworkView);
                }
            }
        }
    }
}
