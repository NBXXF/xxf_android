package com.xxf.download

import com.liulishuo.okdownload.DownloadListener
import com.nbxxf.kpower.database.model.BasePageInfoDTO

interface ICacheTaskDownloadService<T : IDownloadModel> {
    /**
     * 获取已经入库的任务
     */
    fun getTasks(pageNum: Long, pageSize: Long, desc: Boolean): BasePageInfoDTO<T>

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
     * 是否仅wifi场景才下载
     */
    fun wifiRequired(required: Boolean)

    /**
     * 设置下载的请求携带header参数
     */
    fun requestHeaders(headerMapFields: Map<String, List<String>>)

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