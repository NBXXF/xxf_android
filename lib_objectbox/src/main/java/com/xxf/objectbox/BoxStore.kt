package com.xxf.objectbox

import android.app.Application
import android.os.Looper
import com.xxf.application.applicationContext
import com.xxf.objectbox.exception.DbThreadException
import io.objectbox.BoxStore
import io.objectbox.BoxStoreBuilder
import io.objectbox.exception.DbException
import io.reactivex.rxjava3.core.Observable
import java.io.File

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2018/7/16 17:34
 * Description :
 */

/**
 * 观察整个数据库
 */
fun BoxStore.observable(): Observable<Class<Any>> {
    return RxBoxStore.observable(this)
}

/**
 * 观察某个模型 就是某张表
 */
fun <T> BoxStore.observable(tClass: Class<T>): Observable<Class<T>> {
    return RxBoxStore.observable(this, tClass)
}

/**
 * 构建box store
 * 内部单例
 * @param dbName  数据库名字
 * @param allowMainThreadOperation 是否允许主线程操作
 */
fun BoxStoreBuilder.buildSingle(dbName: String, allowMainThreadOperation: Boolean = false): BoxStore {
    if (!allowMainThreadOperation) {
        checkMainThread()
    }
    return ObjectBoxFactory.getBoxStore(applicationContext as Application,this, dbName)!!
}

/**
 * 构建box store
 * 内部单例
 * @param dbFile  数据库文件
 * @param allowMainThreadOperation 是否允许主线程操作
 */
fun BoxStoreBuilder.buildSingle(dbFile: File, allowMainThreadOperation: Boolean = false): BoxStore {
    if (!allowMainThreadOperation) {
        checkMainThread()
    }
    return ObjectBoxFactory.getBoxStore(this, dbFile)!!
}

private fun checkMainThread() {
    if (Looper.myLooper() == Looper.getMainLooper()) {
        throw DbThreadException("不允许在主线程操作")
    }
}