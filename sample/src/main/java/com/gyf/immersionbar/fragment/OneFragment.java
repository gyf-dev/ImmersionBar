package com.gyf.immersionbar.fragment;

import android.os.Handler;
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

public class OneFragment extends BaseFragment {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.rv)
    RecyclerView mRv;
    @BindView(R.id.refreshLayout)
    TwinklingRefreshLayout refreshLayout;
    private OneAdapter mOneAdapter;
    private View headView;
    private List<String> mItemList;
    private List<String> mImages;
    private int bannerHeight = 300;
    private Banner banner;

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_one;
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
    }

    @Override
    protected void initData() {
        mItemList = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            mItemList.add("item" + i);
        }
        mImages = new ArrayList<>();
        mImages.add("http://desk.zol.com.cn/showpic/1024x768_63850_14.html");
        mImages.add("http://desk.zol.com.cn/showpic/1024x768_63850_14.html");
        mImages.add("http://desk.zol.com.cn/showpic/1024x768_63850_14.html");
        mImages.add("http://desk.zol.com.cn/showpic/1024x768_63850_14.html");
        mOneAdapter.setNewData(mItemList);
        addHeaderView();
    }

    private void addHeaderView() {
        headView = LayoutInflater.from(mActivity).inflate(R.layout.item_banner, (ViewGroup) mRv.getParent(), false);
        banner = (Banner) headView.findViewById(R.id.banner);
        banner.setImages(mImages)
                .setImageLoader(new GlideImageLoader())
                .setDelayTime(5000)
                .start();
        mOneAdapter.addHeaderView(headView);
        mOneAdapter.setPreLoadNumber(1);
    }

    @Override
    protected void setListener() {
        ViewGroup.LayoutParams bannerParams = banner.getLayoutParams();
        ViewGroup.LayoutParams titleBarParams = mToolbar.getLayoutParams();
        bannerHeight = bannerParams.height - titleBarParams.height - ImmersionBar.getStatusBarHeight(getActivity());
        mRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int totalDy = 0;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalDy += dy;
                ImmersionBar immersionBar = ImmersionBar.with(OneFragment.this)
                        .addViewSupportTransformColor(mToolbar, R.color.colorPrimary);
                if (totalDy <= bannerHeight) {
                    float alpha = (float) totalDy / bannerHeight;
                    immersionBar.statusBarAlpha(alpha)
                            .init();
                } else {
                    immersionBar.statusBarAlpha(1.0f)
                            .init();
                }
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
                        ImmersionBar.with(OneFragment.this).statusBarDarkFont(false).init();
                    }
                }, 2000);
            }

            @Override
            public void onPullingDown(TwinklingRefreshLayout refreshLayout, float fraction) {
                mToolbar.setVisibility(View.GONE);
                ImmersionBar.with(OneFragment.this).statusBarDarkFont(true).init();
            }

            @Override
            public void onPullDownReleasing(TwinklingRefreshLayout refreshLayout, float fraction) {
                if (Math.abs(fraction - 1.0f) > 0) {
                    mToolbar.setVisibility(View.VISIBLE);
                    ImmersionBar.with(OneFragment.this).statusBarDarkFont(false).init();
                } else {
                    mToolbar.setVisibility(View.GONE);
                    ImmersionBar.with(OneFragment.this).statusBarDarkFont(true).init();
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
    protected void immersionInit() {
        ImmersionBar.with(this)
                .titleBar(mToolbar,false)
                .navigationBarColor(R.color.colorPrimary)
                .init();
    }
}
