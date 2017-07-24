package com.gyf.immersionbar.activity;

import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.gyf.immersionbar.R;
import com.gyf.immersionbar.fragment.two.MineTwoFragment;
import com.gyf.immersionbar.fragment.two.HomeTwoFragment;
import com.gyf.immersionbar.fragment.two.ServiceTwoFragment;
import com.gyf.immersionbar.fragment.two.CategoryTwoFragment;

import butterknife.BindView;

/**
 * Created by geyifeng on 2017/7/20.
 */

public class FragmentTwoActivity extends BaseActivity implements View.OnClickListener {

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
    private HomeTwoFragment homeTwoFragment;
    private CategoryTwoFragment categoryTwoFragment;
    private ServiceTwoFragment serviceTwoFragment;
    private MineTwoFragment mineTwoFragment;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_fragment_two;
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
                break;
            case R.id.ll_category:
                selectedFragment(1);
                tabSelected(ll_category);
                break;
            case R.id.ll_service:
                selectedFragment(2);
                tabSelected(ll_service);
                break;
            case R.id.ll_mine:
                selectedFragment(3);
                tabSelected(ll_mine);
                break;
        }
    }

    private void selectedFragment(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideFragment(transaction);
        switch (position) {
            case 0:
                if (homeTwoFragment == null) {
                    homeTwoFragment = new HomeTwoFragment();
                    transaction.add(R.id.content, homeTwoFragment);
                } else
                    transaction.show(homeTwoFragment);
                break;
            case 1:
                if (categoryTwoFragment == null) {
                    categoryTwoFragment = new CategoryTwoFragment();
                    transaction.add(R.id.content, categoryTwoFragment);
                } else
                    transaction.show(categoryTwoFragment);
                break;
            case 2:
                if (serviceTwoFragment == null) {
                    serviceTwoFragment = new ServiceTwoFragment();
                    transaction.add(R.id.content, serviceTwoFragment);
                } else
                    transaction.show(serviceTwoFragment);
                break;
            case 3:
                if (mineTwoFragment == null) {
                    mineTwoFragment = new MineTwoFragment();
                    transaction.add(R.id.content, mineTwoFragment);
                } else
                    transaction.show(mineTwoFragment);
                break;
        }
        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (homeTwoFragment != null)
            transaction.hide(homeTwoFragment);
        if (categoryTwoFragment != null)
            transaction.hide(categoryTwoFragment);
        if (serviceTwoFragment != null)
            transaction.hide(serviceTwoFragment);
        if (mineTwoFragment != null)
            transaction.hide(mineTwoFragment);
    }

    private void tabSelected(LinearLayout linearLayout) {
        ll_home.setSelected(false);
        ll_category.setSelected(false);
        ll_service.setSelected(false);
        ll_mine.setSelected(false);
        linearLayout.setSelected(true);
    }
}
