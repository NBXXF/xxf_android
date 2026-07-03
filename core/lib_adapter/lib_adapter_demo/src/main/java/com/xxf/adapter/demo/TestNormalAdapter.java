package com.xxf.adapter.demo;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.xxf.adapter.demo.databinding.AdapterTestBinding;
import com.xxf.view.recyclerview.adapter.XXFRecyclerAdapter;
import com.xxf.view.recyclerview.adapter.XXFRecyclerListAdapter;
import com.xxf.view.recyclerview.adapter.XXFViewHolder;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2021/9/29
 * Description ://TODO
 */
public class TestNormalAdapter extends XXFRecyclerAdapter<AdapterTestBinding, String> {
    public TestNormalAdapter() {

    }

    @Override
    protected AdapterTestBinding onCreateBinding(LayoutInflater inflater, ViewGroup viewGroup, int viewType) {
        return AdapterTestBinding.inflate(inflater, viewGroup, false);
    }

    @Override
    public void onBindHolder(XXFViewHolder<AdapterTestBinding, String> holder, @Nullable String item, int index) {
        holder.getBinding().text.setTextKeepState("item:" + item);
        System.out.println("==========>onChildViewAttachedToWindow:AdapterPosition:" + holder.getAdapterPosition() + "  LayoutPosition:" + holder.getLayoutPosition() + "  hash:" + holder.itemView + " [[[bind]]]");
    }
}
