package com.xxf.download.ob

import com.xxf.download.DownloadService
import io.objectbox.Box
import java.io.File

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF  https://blog.csdn.net/axuanqq
 * @Description 下载结合objectbox做缓存
 */
abstract class ObDownloadService<T : ObDownloadModel> : DownloadService<T>() {

    protected abstract fun getBox(): Box<T>


    override fun onDeleteTask(tasks: List<T>) {
        getBox().remove(tasks)
        tasks.forEach {
            File(it.getDownloadPath()).deleteRecursively()
        }
    }

    override fun onSaveTasks(tasks: List<T>) {
        tasks.forEach {
            it._insertTime = System.currentTimeMillis()
        }
        getBox().put(tasks);
    }
}