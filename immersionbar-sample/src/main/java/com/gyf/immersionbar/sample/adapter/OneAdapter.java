package com.gyf.immersionbar.sample.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gyf.immersionbar.sample.R;

/**
 * @author geyifeng
 * @date 2017/6/3
 */
public class OneAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public OneAdapter() {
        super(R.layout.item_one);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.text, item);
    }
}
