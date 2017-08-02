package com.gyf.immersionbar.fragment.one;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.barlibrary.ImmersionBar;
import com.gyf.immersionbar.R;
import com.gyf.immersionbar.adapter.OneAdapter;
import com.gyf.immersionbar.fragment.BaseLazyFragment;
import com.gyf.immersionbar.utils.GlideImageLoader;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by geyifeng on 2017/5/12.
 */

public class HomeOneFragment extends BaseLazyFragment {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.rv)
    RecyclerView mRv;
    @BindView(R.id.refreshLayout)
    TwinklingRefreshLayout refreshLayout;
    private OneAdapter mOneAdapter;
    private List<String> mItemList = new ArrayList<>();
    private List<String> mImages = new ArrayList<>();
    private int bannerHeight;
    private View headView;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImmersionBar.setTitleBar(getActivity(), mToolbar);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_one_home;
    }

    @Override
    protected void initData() {
        for (int i = 1; i <= 20; i++) {
            mItemList.add("item" + i);
        }
        mImages.add("http://desk.zol.com.cn/showpic/1024x768_63850_14.html");
        mImages.add("http://desk.zol.com.cn/showpic/1024x768_63850_14.html");
        mImages.add("http://desk.zol.com.cn/showpic/1024x768_63850_14.html");
        mImages.add("http://desk.zol.com.cn/showpic/1024x768_63850_14.html");
    }

    @Override
    protected void initView() {
        refreshLayout.setEnableLoadmore(false);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        mRv.setLayoutManager(linearLayoutManager);
        mOneAdapter = new OneAdapter();
        mOneAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mRv.setAdapter(mOneAdapter);
        addHeaderView();
        mOneAdapter.setPreLoadNumber(1);
        mOneAdapter.setNewData(mItemList);
    }

    private void addHeaderView() {
        if (mImages != null && mImages.size() > 0) {
            headView = LayoutInflater.from(mActivity).inflate(R.layout.item_banner, (ViewGroup) mRv.getParent(), false);
            Banner banner = (Banner) headView.findViewById(R.id.banner);
            banner.setImages(mImages)
                    .setImageLoader(new GlideImageLoader())
                    .setDelayTime(5000)
                    .start();
            mOneAdapter.addHeaderView(headView);
            ViewGroup.LayoutParams bannerParams = banner.getLayoutParams();
            ViewGroup.LayoutParams titleBarParams = mToolbar.getLayoutParams();
            bannerHeight = bannerParams.height - titleBarParams.height - ImmersionBar.getStatusBarHeight(getActivity());
        }
    }

    @Override
    protected void setListener() {
        mRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int totalDy = 0;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalDy += dy;
                if (totalDy <= bannerHeight) {
                    float alpha = (float) totalDy / bannerHeight;
                    mToolbar.setBackgroundColor(ColorUtils.blendARGB(Color.TRANSPARENT
                            , ContextCompat.getColor(mActivity, R.color.colorPrimary), alpha));
                } else {
                    mToolbar.setBackgroundColor(ColorUtils.blendARGB(Color.TRANSPARENT
                            , ContextCompat.getColor(mActivity, R.color.colorPrimary), 1));
                }
                //在Fragment里使用的时候，并且加载Fragment的Activity配置了android:configChanges="orientation|keyboardHidden|screenSize"属性时，
                //不建议使用ImmersionBar里的addViewSupportTransformColor()方法实现标题滑动渐变
                //原因是会导致影响其他页面的沉浸式效果，除非每个页面的沉浸式参数都一样
//                mImmersionBar.addViewSupportTransformColor(mToolbar, R.color.colorPrimary);
//                if (totalDy <= bannerHeight) {
//                    float alpha = (float) totalDy / bannerHeight;
//                    mImmersionBar.statusBarAlpha(alpha)
//                            .init();
//                } else {
//                    mImmersionBar.statusBarAlpha(1.0f)
//                            .init();
//                }
            }
        });
        mOneAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mOneAdapter.addData(addData());
                        if (mItemList.size() == 100) {
                            mOneAdapter.loadMoreEnd();
                        } else
                            mOneAdapter.loadMoreComplete();
                    }
                }, 2000);
            }
        }, mRv);
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mItemList.clear();
                        mItemList.addAll(newData());
                        mOneAdapter.setNewData(mItemList);
                        refreshLayout.finishRefreshing();
                        mToolbar.setVisibility(View.VISIBLE);
                        mImmersionBar.statusBarDarkFont(false).init();
                    }
                }, 2000);
            }

            @Override
            public void onPullingDown(TwinklingRefreshLayout refreshLayout, float fraction) {
                mToolbar.setVisibility(View.GONE);
                mImmersionBar.statusBarDarkFont(true).init();
            }

            @Override
            public void onPullDownReleasing(TwinklingRefreshLayout refreshLayout, float fraction) {
                if (Math.abs(fraction - 1.0f) > 0) {
                    mToolbar.setVisibility(View.VISIBLE);
                    mImmersionBar.statusBarDarkFont(false).init();
                } else {
                    mToolbar.setVisibility(View.GONE);
                    mImmersionBar.statusBarDarkFont(true).init();
                }
            }
        });
    }

    private List<String> addData() {
        List<String> data = new ArrayList<>();
        for (int i = mItemList.size() + 1; i <= mItemList.size() + 20; i++) {
            data.add("item" + i);
        }
        return data;
    }

    private List<String> newData() {
        List<String> data = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            data.add("item" + i);
        }
        return data;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.statusBarColorTransformEnable(false)
                .navigationBarColor(R.color.colorPrimary)
                .init();
    }
}
