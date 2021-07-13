package com.xxf.objectbox

import io.objectbox.Box

/**
 * @Description: objectBox
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2018/7/16 17:34
 *
 * object box 扩展
 */

/**
 * 替换表的全部数据行,最终保留 ->insertData
 * @param entities
 */
@Throws(Throwable::class)
inline fun <reified T> Box<T>.replaceTable(insertData: List<T>) {
    store.runInTx {
        removeAll()
        put(insertData);
    }
}

/**
 * 替换表的全部数据行,最终保留 ->insertData
 */
@Throws(Throwable::class)
inline fun <reified T> Box<T>.replaceTable(insertData: List<T>, mergeBlock: (insertData: List<T>, box: Box<T>) -> List<T>) {
    val mergeResult = mergeBlock(insertData, this);
    this.replaceTable(mergeResult);
}



