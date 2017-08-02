package com.gyf.immersionbar.activity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gyf.barlibrary.BarParams;
import com.gyf.barlibrary.ImmersionBar;
import com.gyf.immersionbar.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by geyifeng on 2017/5/8.
 */

public class Over3Activity extends BaseActivity {

    @BindView(R.id.text)
    TextView textView;
    @BindView(R.id.btn)
    Button button;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_over3;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.fitsSystemWindows(true)
                .statusBarColor(R.color.colorPrimary)
                .navigationBarColor(R.color.colorPrimary)
                .keyboardEnable(true)
                .init();
    }

    @Override
    protected void initView() {
        textView.setText("使用ImmersionBar的fitsSystemWindows方法" +
                "指定fitsSystemWindows为true，然后指定状态栏的颜色，不然状态栏为透明色，很难看，详情参看此页面的实现");
    }

    @OnClick({R.id.btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn:
                BarParams barParams = ImmersionBar.with(this).getBarParams();
                if (barParams.fits){
                    ImmersionBar.with(this).fitsSystemWindows(false).transparentStatusBar().init();
                    button.setText("fitsSystemWindows动态演示:false");
                }
                else{
                    ImmersionBar.with(this).fitsSystemWindows(true).statusBarColor(R.color.colorPrimary).init();
                    button.setText("fitsSystemWindows动态演示:true");
                }
                break;
        }
    }
}
