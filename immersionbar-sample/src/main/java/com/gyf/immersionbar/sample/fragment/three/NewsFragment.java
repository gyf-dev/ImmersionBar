package com.gyf.immersionbar.sample.fragment.three;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.widget.TextView;

import com.gyf.immersionbar.sample.R;
import com.gyf.immersionbar.sample.fragment.BaseFragment;

import butterknife.BindView;

/**
 * @author geyifeng
 * @date 2019-05-05 17:35
 */
public class NewsFragment extends BaseFragment {

    @BindView(R.id.tv_content)
    TextView tvContent;

    private String mTitle = "";

    public static NewsFragment newInstance(String title) {
        NewsFragment newsFragment = new NewsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        newsFragment.setArguments(bundle);
        return newsFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mTitle = arguments.getString("title");
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_news;
    }

    @Override
    protected void initView() {
        super.initView();
        tvContent.setText(mTitle);
    }
}
