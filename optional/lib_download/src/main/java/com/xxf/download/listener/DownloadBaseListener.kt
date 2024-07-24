package com.xxf.download.listener

import androidx.annotation.CallSuper
import com.liulishuo.okdownload.DownloadTask
import com.liulishuo.okdownload.core.cause.ResumeFailedCause
import com.liulishuo.okdownload.core.listener.DownloadListener3
import com.xxf.download.IDownloadEntity
import com.xxf.download.taskModel

/**
 * @Author: XGod  xuanyouwu@163.com https://github.com/NBXXF  https://blog.csdn.net/axuanqq
 * Date: 1/8/19 12:07 PM
 * Description: 将DownloadListener3 转换成DownloadTaskListener
 */
@Suppress("UNCHECKED_CAST")
abstract class DownloadBaseListener<T : IDownloadEntity> : DownloadListener3(),
    DownloadTaskListener<T> {

    override fun retry(task: DownloadTask, cause: ResumeFailedCause) {

    }

    @CallSuper
    @Deprecated(
        "Deprecated",
        ReplaceWith("this.started(task)", "com.xxf.download.taskModel")
    )
    override fun started(task: DownloadTask) {
        this.started(task.taskModel as? T)
    }

    @CallSuper
    @Deprecated(
        "Deprecated",
        ReplaceWith(
            "this.connected(task,blockCount,currentOffset,totalLength)",
            "com.xxf.download.taskModel"
        )
    )
    override fun connected(
        task: DownloadTask,
        blockCount: Int,
        currentOffset: Long,
        totalLength: Long
    ) {
        this.connected(task.taskModel as? T, blockCount, currentOffset, totalLength)
    }

    @CallSuper
    @Deprecated(
        "Deprecated",
        ReplaceWith("this.progress(task,currentOffset,totalLength)", "com.xxf.download.taskModel")
    )
    override fun progress(task: DownloadTask, currentOffset: Long, totalLength: Long) {
        this.progress(task.taskModel as? T, currentOffset, totalLength)
    }

    @CallSuper
    @Deprecated(
        "Deprecated",
        ReplaceWith("this.completed(task)", "com.xxf.download.taskModel")
    )
    override fun completed(task: DownloadTask) {
        this.completed(task.taskModel as? T)
    }

    @CallSuper
    @Deprecated(
        "Deprecated",
        ReplaceWith("this.canceled(task)", "com.xxf.download.taskModel")
    )
    override fun canceled(task: DownloadTask) {
        this.canceled(task.taskModel as? T)
    }

    @CallSuper
    @Deprecated(
        "Deprecated",
        ReplaceWith("this.error(task,e)", "com.xxf.download.taskModel")
    )
    override fun error(task: DownloadTask, e: java.lang.Exception) {
        this.error(task.taskModel as? T, e)
    }

    @CallSuper
    @Deprecated(
        "Deprecated",
        ReplaceWith("this.warn(task)", "com.xxf.download.taskModel")
    )
    override fun warn(task: DownloadTask) {
        this.warn(task.taskModel as? T)
    }
}