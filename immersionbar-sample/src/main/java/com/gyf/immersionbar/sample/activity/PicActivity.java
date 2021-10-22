package com.gyf.immersionbar.sample.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gyf.immersionbar.ImmersionBar;
import com.gyf.immersionbar.sample.AppManager;
import com.gyf.immersionbar.sample.R;
import com.gyf.immersionbar.sample.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * @author gyf
 * @date 2016/10/24
 */
public class PicActivity extends SwipeBackActivity {
    @BindView(R.id.text_view)
    TextView textView;
    @BindView(R.id.seek_bar)
    SeekBar seekBar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.mIv)
    ImageView mIv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getInstance().addActivity(this);
        setContentView(R.layout.activity_pic);
        ButterKnife.bind(this);
        ImmersionBar.with(this)
                .titleBar(toolbar, false)
                .transparentBar()
                .init();
        Glide.with(this).asBitmap().load(Utils.getFullPic())
                .apply(new RequestOptions().placeholder(R.drawable.pic_all))
                .into(mIv);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float alpha = (float) progress / 100;
                textView.setText("透明度:" + alpha + "f");
                ImmersionBar.with(PicActivity.this)
                        .addViewSupportTransformColor(toolbar, R.color.colorPrimary)
                        .navigationBarColorTransform(R.color.orange)
                        .barAlpha(alpha)
                        .init();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getInstance().removeActivity(this);
    }
}
