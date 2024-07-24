package com.xxf.download.listener

import com.xxf.download.IDownloadEntity

/**
 * @Author: XGod  xuanyouwu@163.com https://github.com/NBXXF  https://blog.csdn.net/axuanqq
 * Date: 1/8/19 12:07 PM
 * Description: 下载监听定义
 */
interface DownloadTaskListener<T : IDownloadEntity> {
    fun started(task: T?)
    fun connected(task: T?, blockCount: Int, currentOffset: Long, totalLength: Long)
    fun progress(task: T?, currentOffset: Long, totalLength: Long)
    fun completed(task: T?)
    fun canceled(task: T?)
    fun error(task: T?, e: Exception?)
    fun warn(task: T?)
}