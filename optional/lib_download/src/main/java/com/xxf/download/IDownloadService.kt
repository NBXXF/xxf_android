package com.xxf.download

import com.liulishuo.okdownload.DownloadListener
import com.nbxxf.kpower.database.model.BasePageInfoDTO
import com.nbxxf.kpower.database.query.BaseQueryBuilder
import com.nbxxf.kpower.database.repository.BaseRepository
import com.xxf.download.model.IDownloadEntity

/**
 * @Author: XGod  xuanyouwu@163.com https://github.com/NBXXF  https://blog.csdn.net/axuanqq
 * Date: 1/8/19 12:07 PM
 * Description: 下载定义接口
 */
interface ICacheTaskDownloadService<T : IDownloadEntity> {

    /**
     * 获取数据库层service
     */
    fun getCacheService(): BaseRepository<Long, T, BaseQueryBuilder<T, *>>

    /**
     * 获取已经入库的任务
     */
    fun getTasks(pageNum: Long, pageSize: Long, desc: Boolean): BasePageInfoDTO<T>

    /**
     * 按状态获取下载任务
     */
    fun getTasks(pageNum: Long, pageSize: Long, desc: Boolean, status: Long): BasePageInfoDTO<T>

}

interface IDownloadService<T : IDownloadEntity> : ICacheTaskDownloadService<T> {

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
     * 恢复指定任务
     */
    fun resumeTask(tasks: List<T>)

    /**
     * 暂停全部任务
     */
    fun pauseTasks()

    /**
     * 暂停指定任务
     */
    fun pauseTask(tasks: List<T>)

    /**
     * 移除任务
     */
    fun removeTask(tasks: List<T>)


}