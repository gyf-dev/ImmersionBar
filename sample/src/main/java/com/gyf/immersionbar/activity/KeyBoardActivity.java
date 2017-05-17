package com.gyf.immersionbar.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.LinearLayout;

import com.gyf.barlibrary.KeyboardPatch;
import com.gyf.immersionbar.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by geyifeng on 2017/5/8.
 */

public class KeyBoardActivity extends BaseActivity {

    @BindView(R.id.line)
    LinearLayout linearLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_board);
        ButterKnife.bind(this);
        KeyboardPatch.patch(this, linearLayout).enable(); //解决底部EditText和软键盘的问题
    }
}