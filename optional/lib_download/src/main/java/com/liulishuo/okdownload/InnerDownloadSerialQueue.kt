package com.liulishuo.okdownload

import java.util.ArrayList

internal class InnerDownloadSerialQueue(
    private val listener: DownloadListener,
    private val taskList: ArrayList<DownloadTask>
) : DownloadSerialQueue(listener, taskList) {

    constructor(listener: DownloadListener) : this(listener, arrayListOf())

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