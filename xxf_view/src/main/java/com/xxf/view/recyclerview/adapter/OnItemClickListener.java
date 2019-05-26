package com.xxf.view.recyclerview.adapter;

import android.view.View;

/**
 * Description
 * Company Beijing icourt
 * author  youxuan  E-mail:xuanyouwu@163.com
 * date createTime：2017/10/3
 * version 2.1.0
 */
public interface OnItemClickListener {

    /**
     * @param adapter
     * @param holder
     * @param itemView
     * @param index    相对于List容器的位置
     */
    void onItemClick(BaseRecyclerAdapter adapter, BaseViewHolder holder, View itemView, int index);
}
