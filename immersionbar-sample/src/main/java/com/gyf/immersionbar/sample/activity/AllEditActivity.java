package com.gyf.immersionbar.sample.activity;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;

import com.gyf.immersionbar.ImmersionBar;
import com.gyf.immersionbar.sample.R;
import com.gyf.immersionbar.sample.adapter.EditAdapter;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * @author geyifeng
 */
public class AllEditActivity extends BaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private ArrayList<String> mData = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_all_edit;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        ImmersionBar.with(this).titleBar(R.id.toolbar).keyboardEnable(true).init();
    }

    @Override
    protected void initData() {
        super.initData();
        for (int i = 0; i < 20; i++) {
            mData.add(String.valueOf(i));
        }
    }

    @Override
    protected void initView() {
        super.initView();
        EditAdapter adapter = new EditAdapter();
        adapter.addHeaderView(LayoutInflater.from(this).inflate(R.layout.item_edit_header, recyclerView, false));
        adapter.addFooterView(LayoutInflater.from(this).inflate(R.layout.item_edit_footer, recyclerView, false));
        adapter.setNewData(mData);
        recyclerView.setAdapter(adapter);
    }
}
