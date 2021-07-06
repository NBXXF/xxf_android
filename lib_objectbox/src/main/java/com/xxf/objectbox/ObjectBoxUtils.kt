package com.xxf.objectbox

import androidx.annotation.WorkerThread
import com.xxf.objectbox.id.MurmurHash
import io.objectbox.Box
import java.util.*

/**
 * @Description: objectBox 扩展
 * @Author: XGod
 * @CreateDate: 2020/7/25 22:55
 */
object ObjectBoxUtils {
    /**
     * 生成id
     *
     * @param id
     * @return
     */
    fun generateId(id: String): Long {
        return MurmurHash.hash32(id).toLong()
    }

    /**
     * 替换合并
     *
     * @param box
     * @param insertData
     * @param mergeFunction
     * @param <T>
     * @throws Exception
    </T> */
    @WorkerThread
    @Throws(Throwable::class)
    fun <T> put(box: Box<T>, insertData: List<T>, mergeFunction: ObjectBoxMergeFunction<T>) {
        Objects.requireNonNull(box)
        Objects.requireNonNull(insertData)
        Objects.requireNonNull(mergeFunction)
        val ids: MutableList<Long> = ArrayList()
        for (t in insertData) {
            ids.add(box.getId(t))
        }
        val insertedMap = box.getMap(ids)
        val apply = mergeFunction.apply(insertData, insertedMap)
        box.put(apply)
    }

    /**
     * 替换合并
     *
     * @param box
     * @param insertData
     * @param mergeFunction
     * @param <T>
     * @throws Exception
    </T> */
    @WorkerThread
    @Throws(Throwable::class)
    fun <T> put(box: Box<T>, insertData: T, mergeFunction: ObjectBoxMergeFunction<T>) {
        put(box, Arrays.asList(insertData), mergeFunction)
    }

    /**
     * 清除表所有数据 替换全部
     *
     * @param box
     * @param insertData
     * @param mergeFunction
     * @param <T>
    </T> */
    @WorkerThread
    @Throws(Throwable::class)
    fun <T> replaceAll(box: Box<T>, insertData: List<T>, mergeFunction: ObjectBoxMergeFunction<T>) {
        Objects.requireNonNull(box)
        Objects.requireNonNull(insertData)
        Objects.requireNonNull(mergeFunction)
        val ids: MutableList<Long> = ArrayList()
        for (t in insertData) {
            ids.add(box.getId(t))
        }
        val insertedMap = box.getMap(ids)
        val apply = mergeFunction.apply(insertData, insertedMap)
        box.store.runInTx {
            box.removeAll()
            box.put(apply)
        }
    }
}