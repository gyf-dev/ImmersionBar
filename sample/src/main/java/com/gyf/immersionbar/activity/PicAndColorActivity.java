package com.gyf.immersionbar.activity;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.gyf.barlibrary.ImmersionBar;
import com.gyf.immersionbar.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by gyf on 2016/10/24.
 */
public class PicAndColorActivity extends BaseActivity implements SeekBar.OnSeekBarChangeListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.seek_bar)
    SeekBar seekBar;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_pic_color;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.statusBarView(R.id.top_view)
                .navigationBarColor(R.color.colorPrimary)
                .fullScreen(true)
                .addTag("PicAndColor")  //给上面参数打标记，以后可以通过标记恢复
                .init();
    }

    @Override
    protected void setListener() {
        seekBar.setOnSeekBarChangeListener(this);
    }

    @OnClick({R.id.btn_status_color, R.id.btn_navigation_color, R.id.btn_color})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_status_color:
                mImmersionBar.statusBarColor(R.color.colorAccent).init();
                break;
            case R.id.btn_navigation_color:
                if (ImmersionBar.hasNavigationBar(this))
                    mImmersionBar.navigationBarColor(R.color.colorAccent).init();
                else
                    Toast.makeText(this, "当前设备没有导航栏", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_color:
                mImmersionBar.getTag("PicAndColor").init(); //根据tag标记来恢复
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        float alpha = (float) progress / 100;
        mImmersionBar.statusBarColorTransform(R.color.orange)
                .navigationBarColorTransform(R.color.tans)
                .addViewSupportTransformColor(toolbar)
                .barAlpha(alpha)
                .init();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
