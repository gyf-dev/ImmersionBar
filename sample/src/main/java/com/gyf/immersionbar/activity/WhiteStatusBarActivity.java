package com.gyf.immersionbar.activity;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.gyf.immersionbar.R;

import butterknife.BindView;

/**
 * @author geyifeng
 * @date 2017/6/2
 */
public class WhiteStatusBarActivity extends BaseActivity {

    @BindView(R.id.btn)
    Button btn;
    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.top_view)
    View view;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_white_status_bar;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        ImmersionBar.with(this).statusBarView(view).init();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void setListener() {
        btn.setOnClickListener(v -> {
            ImmersionBar.with(WhiteStatusBarActivity.this)
                    .statusBarDarkFont(true, 0.2f)
                    .init();
            text.setText("A：重点在于statusBarDarkFont(true,0.2f)这个方法，" +
                    "原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，" +
                    "如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度。");
            btn.setVisibility(View.GONE);
        });
    }
}
