package com.xxf.download.component

import androidx.annotation.CallSuper
import androidx.viewbinding.ViewBinding
import com.liulishuo.okdownload.DownloadTask
import com.liulishuo.okdownload.core.cause.ResumeFailedCause
import com.liulishuo.okdownload.core.listener.DownloadListener3
import com.xxf.download.IDownloadEntity
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
open abstract class DownloadItemAdapter<V : ViewBinding, T : IDownloadEntity> : XXFRecyclerAdapter<V, T>() {


    /**
     * UI绑定
     */
    fun <T : IDownloadEntity, O : IDownloadService<T>> bindDownloadService(service: O) {
        service.removeListener(mDownloadListener)
        service.addListener(mDownloadListener)
    }

    /**
     * UI解绑
     */
    fun <T : IDownloadEntity, O : IDownloadService<T>> unbindDownloadService(service: O) {
        service.removeListener(mDownloadListener)
    }


    /**
     * 只需要添加监听就行了
     */
    private val mDownloadListener: DownloadListener3 = object : DownloadListener3() {
        override fun retry(task: DownloadTask, cause: ResumeFailedCause) {

        }

        override fun connected(task: DownloadTask, blockCount: Int, currentOffset: Long, totalLength: Long) {
            updateDownload(task) {
                notifyItemChanged(
                    it, DownloadInfo(
                        DownloadStatus.CONNECT,
                        blockCount = blockCount,
                        currentOffset = currentOffset,
                        totalLength = totalLength
                    )
                )
            }
        }

        override fun progress(task: DownloadTask, currentOffset: Long, totalLength: Long) {
            updateDownload(task) {
                notifyItemChanged(
                    it, DownloadInfo(
                        DownloadStatus.PROGRESS, currentOffset = currentOffset, totalLength = totalLength
                    )
                )
            }
        }

        override fun started(task: DownloadTask) {
            updateDownload(task) {
                notifyItemChanged(
                    it, DownloadInfo(
                        DownloadStatus.START
                    )
                )
            }
        }

        override fun completed(task: DownloadTask) {
            updateDownload(task) {
                notifyItemChanged(
                    it, DownloadInfo(
                        DownloadStatus.COMPLETED
                    )
                )
            }
        }

        override fun canceled(task: DownloadTask) {
            updateDownload(task) {
                notifyItemChanged(
                    it, DownloadInfo(
                        DownloadStatus.CANCEL
                    )
                )
            }
        }

        override fun error(task: DownloadTask, e: java.lang.Exception) {
            updateDownload(task) {
                notifyItemChanged(
                    it, DownloadInfo(
                        DownloadStatus.ERROR,
                        error = e
                    )
                )
            }
        }

        override fun warn(task: DownloadTask) {
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

    protected fun updateDownload(task: DownloadTask, block: (index: Int) -> Unit) {
        val indexOfFirst = currentList.indexOfFirst {
            it.getDownloadUrl() == task.url
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