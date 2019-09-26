package com.gyf.immersionbar.sample.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gyf.immersionbar.sample.R;
import com.gyf.immersionbar.sample.utils.GlideUtils;

import java.util.ArrayList;

/**
 * @author geyifeng
 * @date 2019-04-24 15:08
 */
public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerViewHolder> {

    private ArrayList<String> mData;

    public BannerAdapter(ArrayList<String> data) {
        this.mData = data;
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_banner_pic, viewGroup, false);
        return new BannerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
        String s = mData.get(position % mData.size());
        GlideUtils.load(s, holder.ivPic);
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    public ArrayList<String> getData() {
        return mData;
    }

    public void setData(ArrayList<String> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    static class BannerViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivPic;

        BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPic = itemView.findViewById(R.id.iv_pic);
        }
    }
}
