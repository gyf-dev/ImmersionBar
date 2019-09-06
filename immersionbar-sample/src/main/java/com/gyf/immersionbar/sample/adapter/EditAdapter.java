package com.gyf.immersionbar.sample.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gyf.immersionbar.sample.R;

/**
 * @author geyifeng
 * @date 2018/12/24 6:22 PM
 */
public class EditAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public EditAdapter() {
        super(R.layout.item_edit);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {

    }
}
