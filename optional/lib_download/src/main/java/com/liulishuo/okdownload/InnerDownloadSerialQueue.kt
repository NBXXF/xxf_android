package com.liulishuo.okdownload

import com.xxf.download.model.IDownloadEntity
import java.util.ArrayList

internal class InnerDownloadSerialQueue(
    private val listener: DownloadListener,
    val taskList: ArrayList<DownloadTask>
) : DownloadSerialQueue(listener, taskList) {

    constructor(listener: DownloadListener) : this(listener, arrayListOf())

    fun contains(task: IDownloadEntity): Boolean {
        synchronized(taskList) {
            return taskList.indexOfFirst {
                task.downloadUrl == it.url
            } >= 0
        }
    }

    fun contains(task: DownloadTask): Boolean {
        synchronized(taskList) {
            return taskList.indexOfFirst {
                task.url == it.url
            } >= 0
        }
    }

    fun <T : IDownloadEntity> remove(task: List<T>) {
        synchronized(taskList) {
            val associateBy = task.associateBy { it.downloadUrl }
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
    }

    @Synchronized
    fun cancel(tasks: List<DownloadTask>) {
        synchronized(taskList) {
            OkDownload.with()
                .downloadDispatcher()
                .cancel(tasks.toTypedArray())
            val associateBy = tasks.associateBy { it.id }
            taskList.removeAll {
                associateBy.contains(it.id)
            }
        }
    }
}