package com.gyf.immersionbar.sample.activity;

import androidx.fragment.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.gyf.immersionbar.ImmersionBar;
import com.gyf.immersionbar.sample.R;
import com.gyf.immersionbar.sample.fragment.four.CategoryFourFragment;
import com.gyf.immersionbar.sample.fragment.four.HomeFourFragment;
import com.gyf.immersionbar.sample.fragment.four.MineFourFragment;
import com.gyf.immersionbar.sample.fragment.four.ServiceFourFragment;

import butterknife.BindView;

/**
 * @author geyifeng
 * @date 2017/7/20
 */
public class FragmentFourActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.content)
    FrameLayout content;
    @BindView(R.id.ll_home)
    LinearLayout llHome;
    @BindView(R.id.ll_category)
    LinearLayout llCategory;
    @BindView(R.id.ll_service)
    LinearLayout llService;
    @BindView(R.id.ll_mine)
    LinearLayout llMine;
    private HomeFourFragment homeFourFragment;
    private CategoryFourFragment categoryFourFragment;
    private ServiceFourFragment serviceFourFragment;
    private MineFourFragment mineFourFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_fragment_two;
    }

    @Override
    protected void initView() {
        selectedFragment(0);
        tabSelected(llHome);
    }

    @Override
    protected void setListener() {
        llHome.setOnClickListener(this);
        llCategory.setOnClickListener(this);
        llService.setOnClickListener(this);
        llMine.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_home:
                selectedFragment(0);
                tabSelected(llHome);
                ImmersionBar.with(this).keyboardEnable(false).statusBarDarkFont(false).navigationBarColor(R.color.colorPrimary).init();
                break;
            case R.id.ll_category:
                selectedFragment(1);
                tabSelected(llCategory);
                ImmersionBar.with(this).keyboardEnable(true).statusBarDarkFont(true, 0.2f).navigationBarColor(R.color.btn1).init();
                break;
            case R.id.ll_service:
                selectedFragment(2);
                tabSelected(llService);
                ImmersionBar.with(this).keyboardEnable(false).statusBarDarkFont(true, 0.2f).navigationBarColor(R.color.btn2).init();
                break;
            case R.id.ll_mine:
                selectedFragment(3);
                tabSelected(llMine);
                ImmersionBar.with(this).keyboardEnable(true).statusBarDarkFont(false).navigationBarColor(R.color.btn7).init();
                break;
            default:
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
                } else {
                    transaction.show(homeFourFragment);
                }
                break;
            case 1:
                if (categoryFourFragment == null) {
                    categoryFourFragment = new CategoryFourFragment();
                    transaction.add(R.id.content, categoryFourFragment);
                } else {
                    transaction.show(categoryFourFragment);
                }
                break;
            case 2:
                if (serviceFourFragment == null) {
                    serviceFourFragment = new ServiceFourFragment();
                    transaction.add(R.id.content, serviceFourFragment);
                } else {
                    transaction.show(serviceFourFragment);
                }
                break;
            case 3:
                if (mineFourFragment == null) {
                    mineFourFragment = new MineFourFragment();
                    transaction.add(R.id.content, mineFourFragment);
                } else {
                    transaction.show(mineFourFragment);
                }
                break;
            default:
                break;
        }
        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (homeFourFragment != null) {
            transaction.hide(homeFourFragment);
        }
        if (categoryFourFragment != null) {
            transaction.hide(categoryFourFragment);
        }
        if (serviceFourFragment != null) {
            transaction.hide(serviceFourFragment);
        }
        if (mineFourFragment != null) {
            transaction.hide(mineFourFragment);
        }
    }

    private void tabSelected(LinearLayout linearLayout) {
        llHome.setSelected(false);
        llCategory.setSelected(false);
        llService.setSelected(false);
        llMine.setSelected(false);
        linearLayout.setSelected(true);
    }
}
