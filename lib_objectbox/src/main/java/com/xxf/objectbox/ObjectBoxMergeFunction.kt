package com.xxf.objectbox

import io.reactivex.rxjava3.functions.BiFunction

/**
 * @Description: 合并
 * @Author: XGod
 * @CreateDate: 2020/7/25 23:00
 */
interface ObjectBoxMergeFunction<T> : BiFunction<List<T>?, Map<Long?, T>?, List<T>?> {
    @Throws(Throwable::class)
    override fun apply(insertList: List<T>?, localDataInDb: Map<Long?, T>?): List<T>?
}