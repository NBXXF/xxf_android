package com.xxf.view.recyclerview

import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import com.xxf.view.recyclerview.adapter.XXFUIAdapterObserver

fun <VH : RecyclerView.ViewHolder> RecyclerView.Adapter<VH>.doAdapterDataObserver(block: () -> Unit): RecyclerView.AdapterDataObserver {
    return object : XXFUIAdapterObserver() {
        override fun updateUI() {
            block()
        }
    }.also {
        this.registerAdapterDataObserver(it)
    }
}

fun <VH : RecyclerView.ViewHolder> ConcatAdapter.containsAdapter(childAdapter: RecyclerView.Adapter<VH>): Boolean {
    return adapters.contains(childAdapter)
}

fun <VH : RecyclerView.ViewHolder> ConcatAdapter.indexOfAdapter(childAdapter: RecyclerView.Adapter<VH>): Int {
    return adapters.indexOf(childAdapter)
}
