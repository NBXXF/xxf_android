package com.xxf.download

import com.liulishuo.okdownload.DownloadListener

interface ICacheTaskDownloadService<T : IDownloadModel> {
    /**
     * 获取已经入库的任务
     */
    fun getTasks(offset: Long, limit: Long): List<T>

    /**
     * 内部执行保存
     */
    fun onSaveTasks(tasks: List<T>)

    /**
     * 执行删除
     */
    fun onDeleteTask(tasks: List<T>)
}

interface IDownloadService<T : IDownloadModel> : ICacheTaskDownloadService<T> {

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
     * 暂停全部任务
     */
    fun pauseTasks()

    /**
     * 移除任务
     */
    fun removeTask(tasks: List<T>)

}