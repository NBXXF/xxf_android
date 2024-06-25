package com.liulishuo.okdownload

import java.util.ArrayList

internal class InnerDownloadSerialQueue(val listener:DownloadListener, val taskList: ArrayList<DownloadTask>): DownloadSerialQueue(listener,taskList) {

    @Synchronized
    fun cancel(tasks: List<DownloadTask>){
        OkDownload.with()
            .downloadDispatcher()
            .cancel(tasks.toTypedArray())
        val associateBy = tasks.associateBy { it.id }
        taskList.removeAll {
            associateBy.contains(it.id)
        }
    }
}