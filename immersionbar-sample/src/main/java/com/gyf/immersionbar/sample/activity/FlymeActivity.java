package com.gyf.immersionbar.sample.activity;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gyf.immersionbar.ImmersionBar;
import com.gyf.immersionbar.sample.R;
import com.gyf.immersionbar.sample.databinding.ActivityFlymeBinding;


/**
 * @author geyifeng
 * @date 2017/5/31
 */
public class FlymeActivity extends BaseActivity {

    private ActivityFlymeBinding binding;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_flyme;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        ImmersionBar.with(this).statusBarView(R.id.top_view).init();
    }

    @Override
    protected void setListener() {
        binding.btn.setOnClickListener(v -> {
            String s = "#" + binding.et.getText().toString();
            if (s.length() == 7) {
                ImmersionBar.with(this).flymeOSStatusBarFontColor(s).init();
            } else {
                Toast.makeText(FlymeActivity.this, "请正确输入6位颜色值", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void initViewBinding() {
        binding = ActivityFlymeBinding.bind(getContentView());
    }

}
