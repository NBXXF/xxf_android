package com.xxf.download.component

import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.xxf.download.listener.DownloadUpdateListener
import com.xxf.download.model.IDownloadEntity
import com.xxf.download.IDownloadService
import com.xxf.ktx.runOnUiThread
import com.xxf.view.recyclerview.adapter.XXFRecyclerAdapter
import com.xxf.view.recyclerview.adapter.XXFViewHolder
import com.xxf.view.recyclerview.doWithoutAnimation

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080
 * Date: 1/4/19 6:05 PM
 * Description: 下载队列更新高度抽象Adapter
 */
open abstract class DownloadItemAdapter<V : ViewBinding, T : IDownloadEntity> :
    XXFRecyclerAdapter<V, T>() {


    private var service: IDownloadService<T>? = null

    /**
     * UI绑定 service自动触发更新,空就是解绑
     */
    fun bindDownloadService(svc: IDownloadService<T>?) {
        //先移除老的
        service?.removeListener(mDownloadListener)

        //赋值
        this.service = svc

        //添加新的
        service?.removeListener(mDownloadListener)
        service?.addListener(mDownloadListener)
    }

    //解决嵌套问题 recyclerview+recyclerview
    @CallSuper
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        service?.removeListener(mDownloadListener)
        service?.addListener(mDownloadListener)
    }


    //解决嵌套问题 recyclerview+recyclerview
    @CallSuper
    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        service?.removeListener(mDownloadListener)
    }


    /**
     * 只需要添加监听就行了
     */
    private val mDownloadListener: DownloadUpdateListener<T> =
        object : DownloadUpdateListener<T>() {
            override fun updateDownload(task: T?, info: DownloadInfo) {
                runOnUiThread {
                    this@DownloadItemAdapter.updateDownload(task, info)
                }
            }
        }


    @CallSuper
    override fun onBindViewHolder(
        holder: XXFViewHolder<V, T>,
        position: Int,
        payloads: MutableList<Any>,
    ) {
        val info = payloads.firstOrNull {
            it is DownloadInfo
        } as? DownloadInfo
        if (info != null) {
            onBindDownloadViewHolder(holder, position, payloads, info)
        } else {
            //super会自动调用 this.onBindViewHolder(holder, position);
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    /**
     * 下载状态局部刷新
     */
    abstract fun onBindDownloadViewHolder(
        holder: XXFViewHolder<V, T>,
        position: Int,
        payloads: MutableList<Any>,
        downloadInfo: DownloadInfo
    )

    protected open fun updateDownload(task: T?, info: DownloadInfo) {
        task?.let { it ->
            updateDownload(it) {
                notifyItemChanged(it, info)
            }
        }
    }

    protected open fun updateDownload(task: T, block: (index: Int) -> Unit) {
        val indexOfFirst = currentList.indexOfFirst {
            it.downloadUrl == task.downloadUrl
        }
        if (indexOfFirst >= 0) {
            runOnUiThread {
                this.doWithoutAnimation {
                    block(indexOfFirst)
                }
            }
        }
    }


}