package com.xxf.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.InnerViewHolder;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080
 * Date: 3/16/21 2:49 PM
 * Description: 适合 ConcatAdapter 做header footer
 * <p>
 * 实例如下:
 * ConcatAdapter totalAdapter = new ConcatAdapter();
 * totalAdapter.addAdapter(new XXFViewAdapter(new TextView(null)))
 * totalAdapter.addAdapter(new XXFViewAdapter(new TextView(null)))
 * totalAdapter.addAdapter(new XXFViewAdapter(new TextView(null)))
 */
public class XXFViewAdapter extends RecyclerView.Adapter<InnerViewHolder> {

    @NonNull
    public View itemView;

    /**
     * 注意 必须 LayoutInflater 指定parent为对应的recyclerview
     *
     * @param itemView
     */
    public XXFViewAdapter(@NonNull View itemView) {
        this.itemView = itemView;
    }

    public XXFViewAdapter(@LayoutRes int id, RecyclerView recyclerView) {
        this.itemView = LayoutInflater.from(recyclerView.getContext())
                .inflate(id, recyclerView, false);
    }

    @NonNull
    @Override
    public InnerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (itemView.getParent() != null) {
            ((ViewGroup) itemView.getParent()).removeView(itemView);
        }
        return new InnerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerViewHolder holder, int position) {

    }

    @Override
    public long getItemId(int position) {
        return Objects.hash(itemView);
    }

    @Override
    public int getItemCount() {
        return 1;
    }
}
