package com.xxf.room.demo

import android.view.LayoutInflater
import android.view.ViewGroup
import com.xxf.room.demo.databinding.AdapterItemUserBinding
import com.xxf.room.demo.model.User
import com.xxf.view.recyclerview.adapter.XXFRecyclerAdapter
import com.xxf.view.recyclerview.adapter.XXFViewHolder

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：7/23/21
 */
class UserAdapter: XXFRecyclerAdapter<AdapterItemUserBinding, User>() {
    override fun onCreateBinding(inflater: LayoutInflater?, viewGroup: ViewGroup?, viewType: Int): AdapterItemUserBinding {
       return AdapterItemUserBinding.inflate(inflater!!,viewGroup,false);
    }

    override fun onBindHolder(holder: XXFViewHolder<AdapterItemUserBinding, User>?, item: User?, index: Int) {
       holder!!.binding!!.tvUser.setText("id:${item!!.id}  name:"+item!!.name+" age:"+item.releaseYear);
    }
}