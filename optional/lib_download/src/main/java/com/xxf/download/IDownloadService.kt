package com.xxf.download

import com.liulishuo.okdownload.DownloadListener
import io.objectbox.Box

interface IDownloadService<T:DownloadModel> {

    fun getBox(): Box<T>

    fun getDownloadModelClass():Class<T>

    /**
     * 添加监听
     */
    fun addListener(l: DownloadListener)

    /**
     * 移除监听
     */
    fun removeListener(l: DownloadListener)

    /**
     * 添加任务
     */
    fun addTask(tasks: List<T>)

    /**
     * 恢复全部任务
     */
    fun resumeTasks()

    /**
     * 获取已经入库的任务
     */
    fun getTasks(offset: Long, limit: Long): List<T>
}