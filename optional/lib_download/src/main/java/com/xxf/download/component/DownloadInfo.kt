package com.xxf.download.component

import com.liulishuo.okdownload.DownloadTask

/**
 * 下载信息
 */
class DownloadInfo(
    val status: DownloadStatus,
    val task: DownloadTask,
    /**
     * 仅仅 CONNECT状态有值
     */
    val blockCount: Int? = null,
    /**
     * 仅仅 CONNECT和PROGRESS状态有值
     */
    val currentOffset: Long? = null,
    /**
     * 仅仅 CONNECT和PROGRESS状态有值
     */
    val totalLength: Long? = null,
    /**
     * 仅仅 ERROR状态有值
     */
    val error: Exception? = null
)