package com.xxf.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import androidx.recyclerview.widget.InnerViewHolder;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xxf.view.recyclerview.adapter.IEdgeEffectViewHolder;

import org.jetbrains.annotations.NotNull;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080
 * Date: 3/16/21 2:49 PM
 * Description: 适合 ConcatAdapter 做header footer
 * <p>
 * 实例如下:
 * ConcatAdapter totalAdapter = new ConcatAdapter();
 * totalAdapter.addAdapter(new XXFViewAdapter(headerView))
 * totalAdapter.addAdapter(new XXFViewAdapter(headerView))
 * totalAdapter.addAdapter(new XXFViewAdapter(footerView))
 */
public class XXFViewAdapter extends RecyclerView.Adapter<InnerViewHolder> {

    @NonNull
    public View itemView;
    private long itemId;

    /**
     * 注意 必须 LayoutInflater 指定parent为对应的recyclerview
     *
     * @param itemView
     */
    public XXFViewAdapter(@NonNull View itemView) {
        this(itemView, RecyclerView.NO_ID);
    }

    /**
     * @param itemView
     * @param itemId   结合 setHasStableIds() 避免重复创建view
     */
    public XXFViewAdapter(@NonNull View itemView, long itemId) {
        this.itemView = itemView;
        this.itemId = itemId;
    }

    public XXFViewAdapter(@LayoutRes int id, RecyclerView recyclerView) {
        this(id, recyclerView, RecyclerView.NO_ID);
    }

    /**
     * @param id
     * @param recyclerView
     * @param itemId       结合 setHasStableIds() 避免重复创建view
     */
    public XXFViewAdapter(@LayoutRes int id, RecyclerView recyclerView, long itemId) {
        this.itemView = LayoutInflater.from(recyclerView.getContext())
                .inflate(id, recyclerView, false);
        this.itemId = itemId;
    }

    @NonNull
    @Override
    public InnerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (itemView.getParent() != null && itemView.getParent() instanceof ViewGroup) {
            ((ViewGroup) itemView.getParent()).removeView(itemView);
        }
        return new InnerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerViewHolder holder, int position) {

    }

    @Override
    public long getItemId(int position) {
        return this.itemId;
    }

    @Override
    public int getItemCount() {
        return 1;
    }
}
