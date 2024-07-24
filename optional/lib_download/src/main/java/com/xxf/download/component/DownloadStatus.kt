package com.xxf.download.component

/**
 * @Author: XGod  xuanyouwu@163.com https://github.com/NBXXF  https://blog.csdn.net/axuanqq
 * Date: 1/8/19 12:07 PM
 * Description: 下载更新状态
 */
enum class DownloadStatus(val value: Long) {
    START(1L),
    CONNECT(2L),
    PROGRESS(3L),
    COMPLETED(4L),
    CANCEL(5L),
    ERROR(6L)
}