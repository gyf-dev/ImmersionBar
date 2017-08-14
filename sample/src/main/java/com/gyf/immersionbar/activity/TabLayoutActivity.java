package com.gyf.immersionbar.activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.gyf.barlibrary.ImmersionBar;
import com.gyf.immersionbar.R;

import java.util.ArrayList;
import java.util.List;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by geyifeng on 2017/7/4.
 */

public class TabLayoutActivity extends SwipeBackActivity {

    private List<String> mData;
    private Toolbar toolbar;
    private ImmersionBar mImmersionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_layout);
        initData(1);
        initView();
        mImmersionBar = ImmersionBar.with(this);
        ImmersionBar.with(this)
                .statusBarView(R.id.view)
                .init();
    }

    private void initData(int pager) {
        mData = new ArrayList<>();
        for (int i = 1; i < 50; i++) {
            mData.add("pager" + pager + " 第" + i + "个item");
        }
    }

    private void initView() {
        //设置ToolBar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        //setSupportActionBar(toolbar);//替换系统的actionBar

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
                mAdapter.notifyDataSetChanged();
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
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }

    //RecyclerView Adapter
    private RecyclerView.Adapter mAdapter = new RecyclerView.Adapter<MyViewHolder>() {


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHolder(getLayoutInflater().inflate(R.layout.item_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.tv.setText(mData.get(position));
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
    };

    class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv_info);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mImmersionBar.destroy();
    }
}
