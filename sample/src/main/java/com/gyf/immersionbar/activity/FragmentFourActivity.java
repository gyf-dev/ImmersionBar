package com.gyf.immersionbar.activity;

import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.gyf.immersionbar.R;
import com.gyf.immersionbar.fragment.four.CategoryFourFragment;
import com.gyf.immersionbar.fragment.four.HomeFourFragment;
import com.gyf.immersionbar.fragment.four.MineFourFragment;
import com.gyf.immersionbar.fragment.four.ServiceFourFragment;
import com.gyf.immersionbar.fragment.two.MineTwoFragment;
import com.gyf.immersionbar.fragment.two.HomeTwoFragment;
import com.gyf.immersionbar.fragment.two.ServiceTwoFragment;
import com.gyf.immersionbar.fragment.two.CategoryTwoFragment;

import butterknife.BindView;

/**
 * Created by geyifeng on 2017/7/20.
 */

public class FragmentFourActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.content)
    FrameLayout content;
    @BindView(R.id.ll_home)
    LinearLayout ll_home;
    @BindView(R.id.ll_category)
    LinearLayout ll_category;
    @BindView(R.id.ll_service)
    LinearLayout ll_service;
    @BindView(R.id.ll_mine)
    LinearLayout ll_mine;
    private HomeFourFragment homeFourFragment;
    private CategoryFourFragment categoryFourFragment;
    private ServiceFourFragment serviceFourFragment;
    private MineFourFragment mineFourFragment;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_fragment_two;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.keyboardEnable(true).init();
    }

    @Override
    protected void initView() {
        selectedFragment(0);
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
                selectedFragment(0);
                tabSelected(ll_home);
                mImmersionBar.fitsSystemWindows(false).transparentStatusBar().init();
                break;
            case R.id.ll_category:
                selectedFragment(1);
                tabSelected(ll_category);
                mImmersionBar.fitsSystemWindows(true).statusBarColor(R.color.colorPrimary).init();
                break;
            case R.id.ll_service:
                selectedFragment(2);
                tabSelected(ll_service);
                mImmersionBar.fitsSystemWindows(true).statusBarColor(R.color.colorPrimary).init();
                break;
            case R.id.ll_mine:
                selectedFragment(3);
                tabSelected(ll_mine);
                mImmersionBar.fitsSystemWindows(true).statusBarColor(R.color.colorPrimary).init();
                break;
        }
    }

    private void selectedFragment(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideFragment(transaction);
        switch (position) {
            case 0:
                if (homeFourFragment == null) {
                    homeFourFragment = new HomeFourFragment();
                    transaction.add(R.id.content, homeFourFragment);
                } else
                    transaction.show(homeFourFragment);
                break;
            case 1:
                if (categoryFourFragment == null) {
                    categoryFourFragment = new CategoryFourFragment();
                    transaction.add(R.id.content, categoryFourFragment);
                } else
                    transaction.show(categoryFourFragment);
                break;
            case 2:
                if (serviceFourFragment == null) {
                    serviceFourFragment = new ServiceFourFragment();
                    transaction.add(R.id.content, serviceFourFragment);
                } else
                    transaction.show(serviceFourFragment);
                break;
            case 3:
                if (mineFourFragment == null) {
                    mineFourFragment = new MineFourFragment();
                    transaction.add(R.id.content, mineFourFragment);
                } else
                    transaction.show(mineFourFragment);
                break;
        }
        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (homeFourFragment != null)
            transaction.hide(homeFourFragment);
        if (categoryFourFragment != null)
            transaction.hide(categoryFourFragment);
        if (serviceFourFragment != null)
            transaction.hide(serviceFourFragment);
        if (mineFourFragment != null)
            transaction.hide(mineFourFragment);
    }

    private void tabSelected(LinearLayout linearLayout) {
        ll_home.setSelected(false);
        ll_category.setSelected(false);
        ll_service.setSelected(false);
        ll_mine.setSelected(false);
        linearLayout.setSelected(true);
    }
}
