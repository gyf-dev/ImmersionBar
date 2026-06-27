package com.gyf.immersionbar.sample.activity;

import android.annotation.SuppressLint;
import androidx.appcompat.widget.Toolbar;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;
import com.gyf.immersionbar.sample.R;
import com.gyf.immersionbar.sample.databinding.ActivityColorBinding;


/**
 * @author geyifeng
 * @date 2017/5/8
 */
public class ColorActivity extends BaseActivity {

    private ActivityColorBinding binding;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_color;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        ImmersionBar.with(this)
                .statusBarView(R.id.top_view)
                .statusBarColor(R.color.colorPrimary)
                .navigationBarColor(R.color.btn8)
                .init();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void setListener() {
        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float alpha = (float) progress / 100;
                binding.textView.setText("透明度:" + alpha + "f");
                ImmersionBar.with(ColorActivity.this).barAlpha(alpha)
                        .statusBarColorTransform(R.color.btn14)
                        .navigationBarColorTransform(R.color.btn3)
                        .addViewSupportTransformColor(binding.toolbar)
                        .addViewSupportTransformColor(binding.btn1, R.color.btn1, R.color.btn4)
                        .addViewSupportTransformColor(binding.btn2, R.color.btn3, R.color.btn12)
                        .addViewSupportTransformColor(binding.btn3, R.color.btn5, R.color.btn10)
                        .addViewSupportTransformColor(binding.line1, R.color.darker_gray, R.color.btn5)
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
    protected void initViewBinding() {
        binding = ActivityColorBinding.bind(getContentView());
    }

}
