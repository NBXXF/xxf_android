package com.liulishuo.okdownload

import com.xxf.download.IDownloadEntity
import java.util.ArrayList

internal class InnerDownloadSerialQueue(
    private val listener: DownloadListener,
    val taskList: ArrayList<DownloadTask>
) : DownloadSerialQueue(listener, taskList) {

    constructor(listener: DownloadListener) : this(listener, arrayListOf())

    fun <T : IDownloadEntity> contains(task: T): Boolean {
        return taskList.indexOfFirst {
            task.getDownloadUrl() == it.url
        } >= 0
    }

    fun <T : IDownloadEntity> remove(task: List<T>) {
        val associateBy = task.associateBy { it.getDownloadUrl() }
        taskList.removeAll {
            if (associateBy.contains(it.url)) {
                try {
                    it.cancel()
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
                true
            } else {
                false
            }
        }
    }

    @Synchronized
    fun cancel(tasks: List<DownloadTask>) {
        OkDownload.with()
            .downloadDispatcher()
            .cancel(tasks.toTypedArray())
        val associateBy = tasks.associateBy { it.id }
        taskList.removeAll {
            associateBy.contains(it.id)
        }
    }
}