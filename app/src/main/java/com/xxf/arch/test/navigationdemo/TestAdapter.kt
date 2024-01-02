package com.xxf.arch.test.navigationdemo

import android.view.LayoutInflater
import android.view.ViewGroup
import com.xxf.arch.test.databinding.ItemTestBinding
import com.xxf.view.recyclerview.adapter.XXFRecyclerAdapter
import com.xxf.view.recyclerview.adapter.XXFViewHolder

class TestAdapter : XXFRecyclerAdapter<ItemTestBinding, String>() {
    override fun onCreateBinding(
        inflater: LayoutInflater,
        viewGroup: ViewGroup,
        viewType: Int
    ): ItemTestBinding {
        return ItemTestBinding.inflate(inflater!!, viewGroup, false)
    }

    override fun onBindHolder(
        holder: XXFViewHolder<ItemTestBinding, String>,
        item: String?,
        index: Int
    ) {
        holder!!.binding?.textView?.setText("" + item)
    }
}