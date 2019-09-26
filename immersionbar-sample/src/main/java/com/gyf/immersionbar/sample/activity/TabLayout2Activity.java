package com.gyf.immersionbar.sample.activity;

import android.os.Bundle;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gyf.immersionbar.ImmersionBar;
import com.gyf.immersionbar.sample.AppManager;
import com.gyf.immersionbar.sample.R;
import com.gyf.immersionbar.sample.adapter.TabAdapter;
import com.gyf.immersionbar.sample.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * @author geyifeng
 * @date 2017/5/30
 */
public class TabLayout2Activity extends SwipeBackActivity {

    private List<String> mData;
    private TabAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getInstance().addActivity(this);
        setContentView(R.layout.activity_tab_layout_two);
        initData(1);
        initView();
        ImmersionBar.with(this)
                .statusBarView(R.id.view)
                .navigationBarColor(android.R.color.white)
                .autoDarkModeEnable(true, 0.2f)
                .init();
        Glide.with(this).asBitmap().load(Utils.getPic())
                .apply(new RequestOptions().placeholder(R.mipmap.test))
                .into((ImageView) findViewById(R.id.mIv));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getInstance().removeActivity(this);
    }

    private void initData(int pager) {
        mData = new ArrayList<>();
        for (int i = 1; i < 50; i++) {
            mData.add("pager" + pager + " 第" + i + "个item");
        }
    }

    private void initView() {
        //设置ToolBar
        ImmersionBar.setTitleBar(this, findViewById(R.id.toolbar));

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.post(() -> {
            int offHeight = collapsingToolbarLayout.getHeight() - ImmersionBar.getStatusBarHeight(this);
            appBarLayout.addOnOffsetChangedListener((appBarLayout1, i) -> {
                ImmersionBar.with(this).statusBarDarkFont(Math.abs(i) >= offHeight, 0.2f).init();
            });
        });


        //设置TabLayout
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        for (int i = 1; i < 7; i++) {
            tabLayout.addTab(tabLayout.newTab().setText("TAB" + i));
        }
        //TabLayout的切换监听
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //切换的时候更新RecyclerView
                initData(tab.getPosition() + 1);
                mAdapter.setNewData(mData);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        //设置RecycleView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mAdapter = new TabAdapter();
        recyclerView.setAdapter(mAdapter);
        mAdapter.setNewData(mData);
    }
}
