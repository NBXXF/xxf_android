package com.xxf.media.preview.demo

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.xxf.media.preview.demo.databinding.TestAdapterItemBinding
import com.xxf.media.preview.model.url.ImageUrl
import com.xxf.view.recyclerview.adapter.XXFRecyclerAdapter
import com.xxf.view.recyclerview.adapter.XXFViewHolder

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2021/9/13
 * Description ://TODO
 */
class TestImageAdapter: XXFRecyclerAdapter<TestAdapterItemBinding, ImageUrl>() {
    override fun onCreateBinding(
        inflater: LayoutInflater,
        viewGroup: ViewGroup,
        viewType: Int
    ): TestAdapterItemBinding {
        return TestAdapterItemBinding.inflate(inflater!!,viewGroup,false)
    }

    override fun onBindHolder(
        holder: XXFViewHolder<TestAdapterItemBinding, ImageUrl>,
        item: ImageUrl?,
        index: Int
    ) {
       Glide.with(holder!!.binding!!.image)
           .load(item!!.url)
           .into(holder.binding!!.image)
    }
}