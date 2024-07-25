package com.xxf.download.utils

import com.liulishuo.okdownload.DownloadTask
import com.liulishuo.okdownload.OkDownload
import com.xxf.download.listener.DownloadBaseListener
import com.xxf.download.model.SimpleDownloadTask
import com.xxf.download.model.taskModel
import java.io.File

/**
 * @Author: XGod  xuanyouwu@163.com https://github.com/NBXXF  https://blog.csdn.net/axuanqq
 * Date: 1/8/19 12:07 PM
 * Description: 封装简单请求,独立于队列的存在!!! 不会持久化!!!
 */
object DownloadUtils {

    /**
     * 下载
     * @param downloadUrl
     * @param downloadPath
     * @param headers
     * @param listener 监听(回调在调用线程)
     * @return 记录id 可用于取消
     */
    fun download(
        downloadUrl: String,
        headers: Map<String, List<String>> = emptyMap(),
        downloadPath: String,
        listener: DownloadBaseListener<SimpleDownloadTask>
    ): Int {
        return download(
            SimpleDownloadTask(
                downloadUrl = downloadUrl,
                headers = headers,
                downloadPath = downloadPath
            ), listener
        )
    }

    /**
     * 下载
     * @param task 任务
     * @param listener 监听(回调在调用线程)
     * @return 记录id 可用于取消
     */
    fun <T : SimpleDownloadTask> download(
        task: T,
        listener: DownloadBaseListener<T>
    ): Int {
        return DownloadTask.Builder(
            task.downloadUrl,
            File(task.downloadPath)
        ).setConnectionCount(1)
            .setHeaderMapFields(task.headers)
            .setAutoCallbackToUIThread(false)
            .setReadBufferSize(DownloadTask.Builder.DEFAULT_READ_BUFFER_SIZE * 2)
            .setFlushBufferSize(DownloadTask.Builder.DEFAULT_FLUSH_BUFFER_SIZE * 2)
            .build()
            .apply {
                this.taskModel = task
                this.execute(listener)
            }.id
    }

    /**
     * 取消下载
     */
    fun cancel(id: Int) {
        OkDownload.with()
            .downloadDispatcher()
            .cancel(id)
    }
}