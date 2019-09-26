package com.gyf.immersionbar.sample.activity;

import android.annotation.SuppressLint;
import androidx.appcompat.app.ActionBar;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gyf.immersionbar.ImmersionBar;
import com.gyf.immersionbar.sample.R;
import com.gyf.immersionbar.sample.utils.Utils;

import butterknife.BindView;

/**
 * @author geyifeng
 * @date 2017/6/2
 */

public class ActionBarActivity extends BaseActivity {

    @BindView(R.id.btn)
    Button btn;
    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.mIv)
    ImageView mIv;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_action_bar;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        ImmersionBar.with(this).keyboardEnable(true).init();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("结合actionBar使用");
        }
        text.setText("上面图片被actionBar遮挡住了,我想使布局从actionBar下面开始绘制，怎么办？");
        Glide.with(this).asBitmap().load(Utils.getPic())
                .apply(new RequestOptions().placeholder(R.mipmap.test))
                .into(mIv);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void setListener() {
        btn.setOnClickListener(v -> {
            ImmersionBar.with(this)
                    .supportActionBar(true)
                    .statusBarColor(R.color.colorPrimary)
                    .init();
            text.setText("哈哈哈！解决啦！就问你惊不惊喜，意不意外，刺不刺激！！！" +
                    "重点是这个方法supportActionBar(true)，实现原理，当为true时，布局距离顶部的" +
                    "padding值为状态栏的高度+ActionBar的高度");
            btn.setText("解决啦");
        });
    }
}
