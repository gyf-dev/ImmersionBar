package com.gyf.immersionbar.sample.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;


import com.gyf.immersionbar.sample.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author geyifeng
 * @date 2017/5/8
 */
public class OverActivity extends BaseActivity {

    @BindView(R.id.text)
    TextView textView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_over;
    }

    @Override
    protected void initView() {
        String text = "请看这个页面的Toolbar和状态栏重叠啦，怎么解决呢？" +
                "不急不急，先说说沉浸式的原理吧！" +
                "原理：其实沉浸式就是把整个布局拉伸到全屏显示，这样自然而然就会使得布局的最顶端和状态栏重合了，" +
                "好吧，以下给出五种解决方案，大家根据项目需求自己看着使用哦，不可结合使用。";
        textView.setText(text);
    }

    @OnClick({R.id.btn_one, R.id.btn_two, R.id.btn_three, R.id.btn_four, R.id.btn_five, R.id.btn_six})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_one:
                startActivity(new Intent(this, Over1Activity.class));
                break;
            case R.id.btn_two:
                startActivity(new Intent(this, Over2Activity.class));
                break;
            case R.id.btn_three:
                startActivity(new Intent(this, Over3Activity.class));
                break;
            case R.id.btn_four:
                startActivity(new Intent(this, Over4Activity.class));
                break;
            case R.id.btn_five:
                startActivity(new Intent(this, Over5Activity.class));
                break;
            case R.id.btn_six:
                startActivity(new Intent(this, Over6Activity.class));
                break;
            default:
                break;
        }
    }
}
