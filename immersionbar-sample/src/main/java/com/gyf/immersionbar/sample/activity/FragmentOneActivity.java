package com.gyf.immersionbar.sample.activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import com.gyf.immersionbar.sample.R;
import com.gyf.immersionbar.sample.databinding.ActivityFragmentOneBinding;
import com.gyf.immersionbar.sample.fragment.one.CategoryOneFragment;
import com.gyf.immersionbar.sample.fragment.one.HomeOneFragment;
import com.gyf.immersionbar.sample.fragment.one.MineOneFragment;
import com.gyf.immersionbar.sample.fragment.one.ServiceOneFragment;
import com.gyf.immersionbar.sample.view.CustomViewPager;

import java.util.ArrayList;


/**
 * @author geyifeng
 * @date 2017/5/8
 */
public class FragmentOneActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private ActivityFragmentOneBinding binding;
        LinearLayout llHome;
        LinearLayout llCategory;
        LinearLayout llService;
        LinearLayout llMine;
    private ArrayList<Fragment> mFragments;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_fragment_one;
    }

    @Override
    protected void initData() {
        mFragments = new ArrayList<>();
        HomeOneFragment homeOneFragment = new HomeOneFragment();
        CategoryOneFragment categoryOneFragment = new CategoryOneFragment();
        ServiceOneFragment serviceOneFragment = new ServiceOneFragment();
        MineOneFragment mineOneFragment = new MineOneFragment();
        mFragments.add(homeOneFragment);
        mFragments.add(categoryOneFragment);
        mFragments.add(serviceOneFragment);
        mFragments.add(mineOneFragment);
    }

    @Override
    protected void initView() {
        binding.viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        binding.viewPager.setOffscreenPageLimit(3);
        llHome.setSelected(true);
    }

    @Override
    protected void setListener() {
        llHome.setOnClickListener(this);
        llCategory.setOnClickListener(this);
        llService.setOnClickListener(this);
        llMine.setOnClickListener(this);
        binding.viewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_home:
                binding.viewPager.setCurrentItem(0);
                tabSelected(llHome);
                break;
            case R.id.ll_category:
                binding.viewPager.setCurrentItem(1);
                tabSelected(llCategory);
                break;
            case R.id.ll_service:
                binding.viewPager.setCurrentItem(2);
                tabSelected(llService);
                break;
            case R.id.ll_mine:
                binding.viewPager.setCurrentItem(3);
                tabSelected(llMine);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                tabSelected(llHome);
                break;
            case 1:
                tabSelected(llCategory);
                break;
            case 2:
                tabSelected(llService);
                break;
            case 3:
                tabSelected(llMine);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void tabSelected(LinearLayout linearLayout) {
        llHome.setSelected(false);
        llCategory.setSelected(false);
        llService.setSelected(false);
        llMine.setSelected(false);
        linearLayout.setSelected(true);
    }

    private class MyAdapter extends FragmentPagerAdapter {
        MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }
    @Override
    protected void initViewBinding() {
        binding = ActivityFragmentOneBinding.bind(getContentView());
        llHome = findViewById(R.id.ll_home);
        llCategory = findViewById(R.id.ll_category);
        llService = findViewById(R.id.ll_service);
        llMine = findViewById(R.id.ll_mine);
    }

}
