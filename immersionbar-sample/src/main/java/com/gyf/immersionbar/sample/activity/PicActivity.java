package com.gyf.immersionbar.sample.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
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
import com.gyf.immersionbar.sample.databinding.ActivityPicBinding;
import com.gyf.immersionbar.sample.utils.Utils;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * @author gyf
 * @date 2016/10/24
 */
public class PicActivity extends SwipeBackActivity {

    private ActivityPicBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getInstance().addActivity(this);
        setContentView(R.layout.activity_pic);
        binding = ActivityPicBinding.bind(getContentView());
        ImmersionBar.with(this)
                .titleBar(binding.toolbar, false)
                .transparentBar()
                .init();
        Glide.with(this).asBitmap().load(Utils.getFullPic())
                .apply(new RequestOptions().placeholder(R.drawable.pic_all))
                .into(binding.iv);
        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float alpha = (float) progress / 100;
                binding.textView.setText("透明度:" + alpha + "f");
                ImmersionBar.with(PicActivity.this)
                        .addViewSupportTransformColor(binding.toolbar, R.color.colorPrimary)
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

    private View getContentView() {
        ViewGroup content = (ViewGroup) findViewById(android.R.id.content);
        return content.getChildAt(0);
    }
}
