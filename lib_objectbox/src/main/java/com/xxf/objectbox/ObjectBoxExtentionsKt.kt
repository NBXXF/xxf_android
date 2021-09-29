package com.xxf.objectbox

import com.xxf.hash.toMurmurHash
import io.objectbox.Box
import io.objectbox.Property
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Unique

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
 *按唯一索引合并block类定义
 *
 * 模型需要实现 UniqueIndexMergePojie接口
 * 用法 getBox(context).put(users,UniqueIndexMergeBlock());
 */
open class UniqueIndexMergeBlock<T : UniqueIndexMergePo<T>> : DbMergeBlock<T> {
    override fun invoke(insertData: List<T>, box: Box<T>): List<T> {
        var p: Property<T>? = null;
        val map = insertData.map {
            val toIdFromUniqueIndex = it.getMergeUniqueIndex();
            p = toIdFromUniqueIndex.first;
            toIdFromUniqueIndex.second;
        }
        val localIndDb = mutableMapOf<String, T>()
        box.query().`in`(p!!, map.toTypedArray()).build().find().forEach {
            localIndDb.put(it.getMergeUniqueIndex().second, it);
        }
        insertData.forEach {
            val itemInDb = localIndDb.get(it.getMergeUniqueIndex().second);
            if (itemInDb != null) {
                it.setMergeId(box.getId(itemInDb));
            }
        }
        return insertData;
    }
}

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
inline fun <reified T> Box<T>.put(
    insertData: List<T>,
    mergeBlock: DbMergeBlock<T>
) {
    put(mergeBlock(insertData, this));
}



