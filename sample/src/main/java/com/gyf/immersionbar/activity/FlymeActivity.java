package com.gyf.immersionbar.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gyf.barlibrary.ImmersionBar;
import com.gyf.immersionbar.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by geyifeng on 2017/5/31.
 */

public class FlymeActivity extends BaseActivity {
    @BindView(R.id.et)
    EditText et;
    @BindView(R.id.btn)
    Button btn;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_flyme;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.statusBarView(R.id.top_view).init();
    }

    @Override
    protected void setListener() {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = "#" + et.getText().toString();
                if (s.length() == 7)
                    mImmersionBar.flymeOSStatusBarFontColor(s).init();
                else
                    Toast.makeText(FlymeActivity.this, "请正确输入6位颜色值", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
