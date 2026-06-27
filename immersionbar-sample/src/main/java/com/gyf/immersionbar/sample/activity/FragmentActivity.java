package com.gyf.immersionbar.sample.activity;

import android.content.Intent;
import android.view.View;

import com.gyf.immersionbar.ImmersionBar;
import com.gyf.immersionbar.sample.R;


/**
 * @author geyifeng
 * @date 2017/7/19
 */
public class FragmentActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_fragment;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        ImmersionBar.with(this).titleBar(R.id.toolbar).init();
    }
    @Override
    protected void setListener() {
        findViewById(R.id.btn_one).setOnClickListener(this::onClick);
        findViewById(R.id.btn_two).setOnClickListener(this::onClick);
        findViewById(R.id.btn_three).setOnClickListener(this::onClick);
        findViewById(R.id.btn_four).setOnClickListener(this::onClick);
        findViewById(R.id.btn_five).setOnClickListener(this::onClick);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_one:
                startActivity(new Intent(this, FragmentOneActivity.class));
                break;
            case R.id.btn_two:
                startActivity(new Intent(this, FragmentTwoActivity.class));
                break;
            case R.id.btn_three:
                startActivity(new Intent(this, FragmentThreeActivity.class));
                break;
            case R.id.btn_four:
                startActivity(new Intent(this, FragmentFourActivity.class));
                break;
            case R.id.btn_five:
                startActivity(new Intent(this, FragmentFiveActivity.class));
                break;
            default:
                break;
        }
    }
}
