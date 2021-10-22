package com.gyf.immersionbar.sample.activity;

import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gyf.immersionbar.ImmersionBar;
import com.gyf.immersionbar.sample.R;
import com.gyf.immersionbar.sample.utils.Utils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author gyf
 * @date 2016/10/24
 */
public class PicAndColorActivity extends BaseActivity implements SeekBar.OnSeekBarChangeListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.seek_bar)
    SeekBar seekBar;
    @BindView(R.id.mIv)
    ImageView mIv;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pic_color;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        ImmersionBar.with(this).statusBarView(R.id.top_view)
                .navigationBarColor(R.color.colorPrimary)
                .fullScreen(true)
                .addTag("PicAndColor")
                .init();
    }

    @Override
    protected void initView() {
        super.initView();
        Glide.with(this).asBitmap().load(Utils.getPic())
                .apply(new RequestOptions().placeholder(R.mipmap.test))
                .into(mIv);
    }

    @Override
    protected void setListener() {
        seekBar.setOnSeekBarChangeListener(this);
    }

    @OnClick({R.id.btn_status_color, R.id.btn_navigation_color, R.id.btn_color})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_status_color:
                ImmersionBar.with(this).statusBarColor(R.color.colorAccent).init();
                break;
            case R.id.btn_navigation_color:
                if (ImmersionBar.hasNavigationBar(this)) {
                    ImmersionBar.with(this).navigationBarColor(R.color.colorAccent).init();
                } else {
                    Toast.makeText(this, "当前设备没有导航栏", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_color:
                ImmersionBar.with(this).getTag("PicAndColor").init();
                break;
            default:
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        float alpha = (float) progress / 100;
        ImmersionBar.with(this)
                .statusBarColorTransform(R.color.orange)
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
