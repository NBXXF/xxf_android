package com.xxf.download.component

/**
 * @Author: XGod  xuanyouwu@163.com https://github.com/NBXXF  https://blog.csdn.net/axuanqq
 * Date: 1/8/19 12:07 PM
 * Description: 下载信息
 */
class DownloadInfo(
    val status: DownloadStatus,
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