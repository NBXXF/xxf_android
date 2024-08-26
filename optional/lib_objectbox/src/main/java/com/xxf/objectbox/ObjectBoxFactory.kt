package com.xxf.objectbox

import android.app.Application
import android.util.Log
import com.xxf.application.application
import com.xxf.ktx.isAppDebug
import com.xxf.ktx.mkParentDirs
import io.objectbox.BoxStore
import io.objectbox.BoxStoreBuilder
import io.objectbox.android.Admin
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
     * @param objectStoreDir 路径
     * @return
     */
    @Synchronized
    fun getBoxStore(
        boxStoreBuilder: BoxStoreBuilder, objectStoreDir: File
    ): BoxStore? {
        var boxStore: BoxStore? = null
        try {
            boxStore = boxStoreMap[objectStoreDir.absolutePath]
            if (boxStore == null) {
                boxStoreMap[objectStoreDir.absolutePath] =
                    buildBox(boxStoreBuilder, objectStoreDir).also { boxStore = it }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            println("=============>box init fail:$e")
            try {
                /**
                 * fix https://github.com/objectbox/objectbox-java/issues/610
                 */
                BoxStore.deleteAllFiles(objectStoreDir)
                boxStoreMap[objectStoreDir.absolutePath] =
                    buildBox(boxStoreBuilder, objectStoreDir).also { boxStore = it }
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
        application: Application, boxStoreBuilder: BoxStoreBuilder, dbName: String
    ): BoxStore? {
        return getBoxStore(
            boxStoreBuilder,
            application.filesDir.resolve(BoxStoreBuilder.DEFAULT_NAME).resolve(dbName)
        )
    }

    /**
     * 构建数据库
     *
     * @param objectStoreDir
     * @return
     * @throws io.objectbox.exception.DbException
     */
    @Synchronized
    @Throws(DbException::class)
    private fun buildBox(boxStoreBuilder: BoxStoreBuilder, objectStoreDir: File): BoxStore {
        objectStoreDir.mkParentDirs()
        return boxStoreBuilder.directory(objectStoreDir).build().also {
            try {
                //https://docs.objectbox.io/data-browser#admin-for-android
                /**
                 * 它会将访问 Web 应用程序的 URL 打印到日志中，例如：
                 * ObjectBox Admin running at URL: http://127.0.0.1:8090/index.html
                 *
                 * 您的开发机器上，使用 ADB 命令将端口（或您喜欢的任何端口）转发到设备的该端口。如果使用默认端口 8090，则命令如下所示：
                 * adb forward tcp:8090 tcp:8090
                 */
                if (application.isAppDebug) {
                    val started = Admin(it).start(application)
                    Log.i(
                        BoxStoreBuilder.DEFAULT_NAME,
                        "admin Started: $started in ${objectStoreDir.absolutePath}"
                    )
                }
            } catch (e: Throwable) {
                e.printStackTrace()
                Log.e(
                    BoxStoreBuilder.DEFAULT_NAME,
                    "admin Started error: $e in ${objectStoreDir.absolutePath}"
                )
            }
        }
    }
}