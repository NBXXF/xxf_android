package com.xxf.view.recyclerview

import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.xxf.view.recyclerview.adapter.XXFRecyclerAdapter
import com.xxf.view.recyclerview.adapter.XXFRecyclerListAdapter
import com.xxf.view.recyclerview.adapter.XXFUIAdapterObserver
import com.xxf.view.recyclerview.adapter.XXFViewHolder

fun <VH : RecyclerView.ViewHolder, T : RecyclerView.Adapter<VH>> T.doAdapterDataObserver(block: T.() -> Unit): RecyclerView.AdapterDataObserver {
    return object : XXFUIAdapterObserver() {
        override fun updateUI() {
            this@doAdapterDataObserver.apply(block)
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

@Suppress("UNCHECKED_CAST")
fun <V : ViewBinding, T, A : XXFRecyclerAdapter<V, T>> A.findViewHolderForAdapterPosition(position: Int): XXFViewHolder<V, T>? {
    return this.recyclerView?.findViewHolderForAdapterPosition(position) as? XXFViewHolder<V, T>
}

@Suppress("UNCHECKED_CAST")
fun <V : ViewBinding, T, A : XXFRecyclerAdapter<V, T>> A.findViewHolderForLayoutPosition(position: Int): XXFViewHolder<V, T>? {
    return this.recyclerView?.findViewHolderForLayoutPosition(position) as? XXFViewHolder<V, T>
}

@Suppress("UNCHECKED_CAST")
fun <V : ViewBinding, T, A : XXFRecyclerAdapter<V, T>> A.findViewHolderForItemId(
    id: Long
): XXFViewHolder<V, T>? {
    return this.recyclerView?.findViewHolderForItemId(id) as? XXFViewHolder<V, T>
}


@Suppress("UNCHECKED_CAST")
fun <V : ViewBinding, T, A : XXFRecyclerListAdapter<V, T>> A.findViewHolderForAdapterPosition(
    position: Int
): XXFViewHolder<V, T>? {
    return this.recyclerView?.findViewHolderForAdapterPosition(position) as? XXFViewHolder<V, T>
}

@Suppress("UNCHECKED_CAST")
fun <V : ViewBinding, T, A : XXFRecyclerListAdapter<V, T>> A.findViewHolderForLayoutPosition(
    position: Int
): XXFViewHolder<V, T>? {
    return this.recyclerView?.findViewHolderForLayoutPosition(position) as? XXFViewHolder<V, T>
}

@Suppress("UNCHECKED_CAST")
fun <V : ViewBinding, T, A : XXFRecyclerListAdapter<V, T>> A.findViewHolderForItemId(
    id: Long
): XXFViewHolder<V, T>? {
    return this.recyclerView?.findViewHolderForItemId(id) as? XXFViewHolder<V, T>
}