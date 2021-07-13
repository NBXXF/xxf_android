package com.xxf.objectbox

import io.objectbox.Box
import io.objectbox.Property

/**
 * @Description: objectBox
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2018/7/16 17:34
 *
 * object box 扩展
 */


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
 * 提供合并扩展
 */
@Throws(Throwable::class)
inline fun <reified T : UniqueIndexMergePo<T>> Box<T>.put(insertData: List<T>, mergeBlock: DbMergeBlock<T>) {
    put(mergeBlock(insertData, this));
}


/**
 * 按唯一索引进行合并行合并
 */
@Throws(Throwable::class)
inline fun <reified T : UniqueIndexMergePo<T>> Box<T>.putMergePo(insertData: List<T>) {
    put(insertData) { insertData: List<T>, box: Box<T> ->
        var p:Property<T>?=null;
        val map = insertData.map {
            val toIdFromUniqueIndex = it.getMergeUniqueIndex();
            p = toIdFromUniqueIndex.first;
            toIdFromUniqueIndex.second;
        }
        val localIndDb= mutableMapOf<String,T>()
        box.query().`in`(p!!, map.toTypedArray()).build().find().forEach {
          localIndDb.put(it.getMergeUniqueIndex().second,it);
        }
        insertData.forEach {
            val itemInDb = localIndDb.get(it.getMergeUniqueIndex().second);
            if(itemInDb!=null) {
                it.setMergeId(getId(itemInDb));
            }
        }
        insertData;
    };
}


