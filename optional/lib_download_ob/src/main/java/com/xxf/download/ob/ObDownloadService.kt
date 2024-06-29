package com.xxf.download.ob

import com.nbxxf.kpower.database.objectbox.service.BaseServiceImpl
import com.xxf.download.DownloadService
import java.io.File
import java.util.Date

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF  https://blog.csdn.net/axuanqq
 * @Description 下载结合objectbox做缓存
 */
abstract class ObDownloadService<E : ObDownloadModel> :
    DownloadService<E>() {

    protected abstract fun getService(): BaseServiceImpl<E, *>


    override fun onDeleteTask(tasks: List<E>) {
        getService().deleteById(tasks.map { it.id })
        tasks.forEach {
            File(it.getDownloadPath()).deleteRecursively()
        }
    }

    override fun onSaveTasks(tasks: List<E>) {
        tasks.forEach {
            it.createDate = Date()
        }
        getService().insertOrUpdate(tasks)
    }
}