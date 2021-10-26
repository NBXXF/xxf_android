package com.xxf.objectbox

import com.xxf.hash.toMurmurHash
import io.objectbox.Box
import io.objectbox.Property
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Unique
import io.objectbox.query.Query

/**
 * @Description: objectBox
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2018/7/16 17:34
 *
 * object box 扩展
 */

/**
 * 字符串转成objectBox id
 */
fun String.toObjectBoxId(): Long {
    return this.toMurmurHash()
}

//合并block定义
typealias DbMergeBlock<T> = (insertData: List<T>, box: Box<T>) -> List<T>;

/**
 * 替换表的全部数据行,最终保留 ->insertData
 * @param entities
 */
@Throws(Throwable::class)
inline fun <reified T> Box<T>.replaceTable(insertData: List<T>) {
    this.replaceTable(insertData) { insertData: List<T>, box: Box<T> ->
        insertData
    }
}

/**
 * 替换表的全部数据行,最终保留 ->insertData
 */
@Throws(Throwable::class)
inline fun <reified T> Box<T>.replaceTable(insertData: List<T>, mergeBlock: DbMergeBlock<T>) {
    val mergeResult = mergeBlock(insertData, this);
    store.runInTx {
        removeAll()
        put(mergeResult);
    }
}

/**
 * 替换表的全部数据行,最终保留 ->insertData
 * @param mergeBlock 合并
 * @param deleteList  删除的数据
 */
@Throws(Throwable::class)
inline fun <reified T> Box<T>.replaceTable(
    insertData: List<T>,
    mergeBlock: DbMergeBlock<T>,
    deleteList: List<T>
) {
    val mergeResult = mergeBlock(insertData, this);
    store.runInTx {
        remove(deleteList)
        put(mergeResult);
    }
}

/**
 * 提供合并扩展
 */
@Throws(Throwable::class)
inline fun <reified T> Box<T>.put(
    insertData: List<T>,
    mergeBlock: DbMergeBlock<T>
) {
    put(mergeBlock(insertData, this));
}



