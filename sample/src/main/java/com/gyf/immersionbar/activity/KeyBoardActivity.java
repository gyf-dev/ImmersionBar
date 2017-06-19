package com.gyf.immersionbar.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.gyf.barlibrary.BarHide;
import com.gyf.barlibrary.ImmersionBar;
import com.gyf.barlibrary.KeyboardPatch;
import com.gyf.immersionbar.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.name;

/**
 * Created by geyifeng on 2017/5/8.
 */

public class KeyBoardActivity extends BaseActivity {

    @BindView(R.id.line)
    LinearLayout layout;
    @BindView(R.id.list_view)
    ListView listView;
    private List<Map<String, Object>> mapList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_board);
        ButterKnife.bind(this);
        ImmersionBar.with(this)
                .keyboardEnable(true)  //解决软键盘与底部输入框冲突问题
                .init();
        //或者使用以下方法，任选其一
//        KeyboardPatch.patch(this).enable();
//        KeyboardPatch.patch(this,layout).enable();  //layout指的是当前布局的根节点
        mapList = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("desc", "我是假数据" + i);
            mapList.add(map);
        }
        listView.setAdapter(new SimpleAdapter(this, mapList, R.layout.item_simple, new String[]{"desc"}, new int[]{R.id.text}));
    }
}