package com.gyf.immersionbar.fragment.five;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import com.apkfuns.logutils.LogUtils;
import com.gyf.immersionbar.R;

import butterknife.BindView;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by geyifeng on 2017/8/12.
 */

public class MainFragment extends BaseFiveFragment implements View.OnClickListener {

    @BindView(R.id.ll_home)
    LinearLayout ll_home;
    @BindView(R.id.ll_category)
    LinearLayout ll_category;
    @BindView(R.id.ll_service)
    LinearLayout ll_service;
    @BindView(R.id.ll_mine)
    LinearLayout ll_mine;

    private SupportFragment[] mFragments = new SupportFragment[4];

    public static final int HOME = 0;
    public static final int CATEGORY = 1;
    public static final int SERVICE = 2;
    public static final int MINE = 3;

    public static MainFragment newInstance() {
        Bundle args = new Bundle();
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_fragment_two;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SupportFragment homeFiveFragment = findChildFragment(HomeFiveFragment.class);
        if (homeFiveFragment == null) {
            mFragments[HOME] = HomeFiveFragment.newInstance();
            mFragments[CATEGORY] = CategoryFiveFragment.newInstance();
            mFragments[SERVICE] = ServiceFiveFragment.newInstance();
            mFragments[MINE] = MineFiveFragment.newInstance();

            loadMultipleRootFragment(R.id.content, HOME,
                    mFragments[HOME],
                    mFragments[CATEGORY],
                    mFragments[SERVICE],
                    mFragments[MINE]);
        } else {
            // 这里库已经做了Fragment恢复,所有不需要额外的处理了, 不会出现重叠问题
            // 这里我们需要拿到mFragments的引用,也可以通过getChildFragmentManager.findFragmentByTag自行进行判断查找(效率更高些),用下面的方法查找更方便些
            mFragments[HOME] = homeFiveFragment;
            mFragments[CATEGORY] = findChildFragment(CategoryFiveFragment.class);
            mFragments[SERVICE] = findChildFragment(ServiceFiveFragment.class);
            mFragments[MINE] = findChildFragment(MineFiveFragment.class);
        }
    }

    @Override
    protected void initView() {
        tabSelected(ll_home);
    }

    @Override
    protected void setListener() {
        ll_home.setOnClickListener(this);
        ll_category.setOnClickListener(this);
        ll_service.setOnClickListener(this);
        ll_mine.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_home:
                showHideFragment(mFragments[HOME]);
                tabSelected(ll_home);
                break;
            case R.id.ll_category:
                showHideFragment(mFragments[CATEGORY]);
                tabSelected(ll_category);
                break;
            case R.id.ll_service:
                showHideFragment(mFragments[SERVICE]);
                tabSelected(ll_service);
                break;
            case R.id.ll_mine:
                showHideFragment(mFragments[MINE]);
                tabSelected(ll_mine);
                break;
        }
    }

    private void tabSelected(LinearLayout linearLayout) {
        ll_home.setSelected(false);
        ll_category.setSelected(false);
        ll_service.setSelected(false);
        ll_mine.setSelected(false);
        linearLayout.setSelected(true);
    }
}
