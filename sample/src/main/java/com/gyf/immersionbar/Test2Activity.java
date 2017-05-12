package com.gyf.immersionbar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;

/**
 * Created by gyf on 2016/10/24.
 */
public class Test2Activity extends BaseActivity {
    private TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this).transparentBar().init();
        setContentView(R.layout.activity_test2);
        textView = (TextView) findViewById(R.id.text_view);
        SeekBar seekBar = (SeekBar) findViewById(R.id.seek_bar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float alpha = (float) progress / 100;
                textView.setText("透明度:" + alpha + "f");
                ImmersionBar.with(Test2Activity.this)
                        .barAlpha(alpha)
                        .statusBarColorTransform(R.color.colorPrimary)
                        .navigationBarColorTransform(R.color.orange)
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

}
