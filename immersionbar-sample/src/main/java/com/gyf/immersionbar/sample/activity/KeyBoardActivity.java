package com.gyf.immersionbar.sample.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.gyf.immersionbar.ImmersionBar;
import com.gyf.immersionbar.sample.R;
import com.gyf.immersionbar.sample.databinding.ActivityKeyBoardBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author geyifeng
 * @date 2017/5/8
 */
public class KeyBoardActivity extends BaseActivity {

    private ActivityKeyBoardBinding binding;
    private List<Map<String, Object>> mapList;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_key_board;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        ImmersionBar.with(this).titleBar(binding.toolbar)
                //解决软键盘与底部输入框冲突问题
                .keyboardEnable(true)
                .init();
    }

    @Override
    protected void initData() {
        mapList = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("desc", "我是假数据" + i);
            mapList.add(map);
        }
    }

    @Override
    protected void initView() {
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        binding.listView.setAdapter(new SimpleAdapter(this, mapList, R.layout.item_simple, new String[]{"desc"}, new int[]{R.id.text}));
    }

    @Override
    protected void setListener() {
        //toolbar返回按钮监听
        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }
    @Override
    protected void initViewBinding() {
        binding = ActivityKeyBoardBinding.bind(getContentView());
    }

}
