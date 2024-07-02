package com.xxf.download

import com.liulishuo.okdownload.DownloadTask
import com.liulishuo.okdownload.core.cause.ResumeFailedCause
import com.liulishuo.okdownload.core.listener.DownloadListener3
import com.xxf.download.component.DownloadInfo
import com.xxf.download.component.DownloadStatus

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080
 * Date: 1/8/19 12:07 PM
 * Description: 下载聚合监听
 */
@Suppress("UNCHECKED_CAST")
abstract class DownloadUpdateListener<T : IDownloadEntity> : DownloadListener3() {
    override fun retry(task: DownloadTask, cause: ResumeFailedCause) {

    }

    override fun connected(
        task: DownloadTask,
        blockCount: Int,
        currentOffset: Long,
        totalLength: Long
    ) {
        updateDownload(
            task.taskModel as? T, DownloadInfo(
                DownloadStatus.CONNECT,
                blockCount = blockCount,
                currentOffset = currentOffset,
                totalLength = totalLength
            )
        )
    }

    override fun progress(task: DownloadTask, currentOffset: Long, totalLength: Long) {
        updateDownload(
            task.taskModel as? T, DownloadInfo(
                DownloadStatus.PROGRESS, currentOffset = currentOffset, totalLength = totalLength
            )
        )
    }

    override fun started(task: DownloadTask) {
        updateDownload(
            task.taskModel as? T, DownloadInfo(
                DownloadStatus.START
            )
        )
    }

    override fun completed(task: DownloadTask) {
        updateDownload(
            task.taskModel as? T, DownloadInfo(
                DownloadStatus.COMPLETED
            )
        )
    }

    override fun canceled(task: DownloadTask) {
        updateDownload(
            task.taskModel as? T, DownloadInfo(
                DownloadStatus.CANCEL
            )
        )
    }

    override fun error(task: DownloadTask, e: java.lang.Exception) {
        updateDownload(
            task.taskModel as? T, DownloadInfo(
                DownloadStatus.ERROR,
                error = e
            )
        )
    }

    override fun warn(task: DownloadTask) {
    }

    protected abstract fun updateDownload(task: T?, info: DownloadInfo)
}