package com.xxf.download.listener

import com.liulishuo.okdownload.DownloadTask
import com.liulishuo.okdownload.core.cause.ResumeFailedCause
import com.liulishuo.okdownload.core.listener.DownloadListener3
import com.xxf.download.IDownloadEntity
import com.xxf.download.taskModel

/**
 * @Author: XGod  xuanyouwu@163.com https://github.com/NBXXF  https://blog.csdn.net/axuanqq
 * Date: 1/8/19 12:07 PM
 * Description: 转换监听
 */
@Suppress("UNCHECKED_CAST")
abstract class DownloadConvertListener<T : IDownloadEntity> : DownloadListener3(),
    TaskDownloaderListener<T> {
    override fun retry(task: DownloadTask, cause: ResumeFailedCause) {

    }

    override fun started(task: DownloadTask) {
        this.started(task.taskModel as? T)
    }

    override fun connected(
        task: DownloadTask,
        blockCount: Int,
        currentOffset: Long,
        totalLength: Long
    ) {
        this.connected(task.taskModel as? T, blockCount, currentOffset, totalLength)
    }

    override fun progress(task: DownloadTask, currentOffset: Long, totalLength: Long) {
        this.progress(task.taskModel as? T, currentOffset, totalLength)
    }

    override fun completed(task: DownloadTask) {
        this.completed(task.taskModel as? T)
    }

    override fun canceled(task: DownloadTask) {
        this.canceled(task.taskModel as? T)
    }

    override fun error(task: DownloadTask, e: java.lang.Exception) {
        this.error(task.taskModel as? T, e)
    }

    override fun warn(task: DownloadTask) {
        this.warn(task.taskModel as? T)
    }
}