package com.gyf.immersionbar.sample.activity;

import android.annotation.SuppressLint;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gyf.immersionbar.ImmersionBar;
import com.gyf.immersionbar.sample.R;
import com.gyf.immersionbar.sample.databinding.ActivityCoordinatorBinding;
import com.gyf.immersionbar.sample.utils.Utils;


/**
 * @author geyifeng
 * @date 2017/5/30
 */
public class CoordinatorActivity extends BaseActivity {

    private ActivityCoordinatorBinding binding;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_coordinator;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        ImmersionBar.with(this).titleBar(binding.detailToolbar).init();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        super.initView();
        setSupportActionBar(binding.detailToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        binding.text.setText("关于Snackbar在4.4和emui3.1上高度显示不准确的问题是由于沉浸式使用了系统的" +
                "WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS或者WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION" +
                "属性造成的，目前尚不知有什么解决办法");
        Glide.with(this).asBitmap().load(Utils.getPic())
                .apply(new RequestOptions().placeholder(R.mipmap.test))
                .into(binding.iv);
    }

    @Override
    protected void setListener() {
        binding.fab.setOnClickListener(view -> Snackbar.make(view, "我是Snackbar", Snackbar.LENGTH_LONG).show());
        //toolbar返回按钮监听
        binding.detailToolbar.setNavigationOnClickListener(v -> finish());
    }
    @Override
    protected void initViewBinding() {
        binding = ActivityCoordinatorBinding.bind(getContentView());
    }

}
