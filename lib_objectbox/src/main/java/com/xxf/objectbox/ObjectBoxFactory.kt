package com.xxf.objectbox

import android.app.Application
import io.objectbox.BoxStore
import io.objectbox.BoxStoreBuilder
import io.objectbox.exception.DbException
import java.io.File
import java.util.concurrent.ConcurrentHashMap

/**
 * @Description: objectBox
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2018/7/16 17:34
 */
internal object ObjectBoxFactory {
    private val boxStoreMap: MutableMap<String, BoxStore> = ConcurrentHashMap()

    /**
     * 创建box
     *
     * @param boxStoreBuilder
     * @param objectStoreDirectory 路径
     * @return
     */
    @Synchronized
    fun getBoxStore(
        boxStoreBuilder: BoxStoreBuilder,
        objectStoreDirectory: File
    ): BoxStore? {
        var boxStore: BoxStore? = null
        try {
            boxStore = boxStoreMap[objectStoreDirectory.absolutePath]
            if (boxStore == null) {
                boxStoreMap[objectStoreDirectory.absolutePath] =
                    buildBox(boxStoreBuilder, objectStoreDirectory).also { boxStore = it }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            println("=============>box init fail:$e")
            try {
                /**
                 * fix https://github.com/objectbox/objectbox-java/issues/610
                 */
                BoxStore.deleteAllFiles(objectStoreDirectory)
                boxStoreMap[objectStoreDirectory.absolutePath] =
                    buildBox(boxStoreBuilder, objectStoreDirectory).also { boxStore = it }
            } catch (retryEx: Exception) {
                retryEx.printStackTrace()
                println("=============>box init retry fail:$e")
            }
        }
        return boxStore
    }

    /**
     * 创建box
     *
     * @param application
     * @param boxStoreBuilder
     * @param dbName          数据库名字 非路径
     * @return
     */
    @Synchronized
    fun getBoxStore(
        application: Application,
        boxStoreBuilder: BoxStoreBuilder,
        dbName: String?
    ): BoxStore? {
        return getBoxStore(boxStoreBuilder, File(application.filesDir, dbName))
    }

    /**
     * 构建数据库
     *
     * @param objectStoreDirectory
     * @return
     * @throws io.objectbox.exception.DbException
     */
    @Synchronized
    @Throws(DbException::class)
    private fun buildBox(boxStoreBuilder: BoxStoreBuilder, objectStoreDirectory: File): BoxStore {
        return boxStoreBuilder
            .directory(objectStoreDirectory)
            .build()
    }
}