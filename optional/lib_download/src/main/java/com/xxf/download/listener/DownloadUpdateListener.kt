package com.xxf.download.listener

import com.xxf.download.IDownloadEntity
import com.xxf.download.component.DownloadInfo
import com.xxf.download.component.DownloadStatus

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080
 * Date: 1/8/19 12:07 PM
 * Description: 下载聚合监听
 */
@Suppress("UNCHECKED_CAST")
abstract class DownloadUpdateListener<T : IDownloadEntity> : DownloadConvertListener<T>() {

    protected abstract fun updateDownload(task: T?, info: DownloadInfo)


    override fun started(task: T?) {
        updateDownload(
            task, DownloadInfo(
                DownloadStatus.START
            )
        )
    }

    override fun connected(task: T?, blockCount: Int, currentOffset: Long, totalLength: Long) {
        updateDownload(
            task, DownloadInfo(
                DownloadStatus.CONNECT,
                blockCount = blockCount,
                currentOffset = currentOffset,
                totalLength = totalLength
            )
        )
    }

    override fun progress(task: T?, currentOffset: Long, totalLength: Long) {
        updateDownload(
            task, DownloadInfo(
                DownloadStatus.PROGRESS, currentOffset = currentOffset, totalLength = totalLength
            )
        )
    }

    override fun completed(task: T?) {
        updateDownload(
            task, DownloadInfo(
                DownloadStatus.COMPLETED
            )
        )
    }

    override fun canceled(task: T?) {
        updateDownload(
            task, DownloadInfo(
                DownloadStatus.CANCEL
            )
        )
    }

    override fun error(task: T?, e: Exception?) {
        updateDownload(
            task, DownloadInfo(
                DownloadStatus.ERROR,
                error = e
            )
        )
    }

    override fun warn(task: T?) {
    }

}
