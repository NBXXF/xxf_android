package com.xxf.objectbox;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.functions.BiFunction;


/**
 * @Description: 合并
 * @Author: XGod
 * @CreateDate: 2020/7/25 23:00
 */
public interface ObjectBoxMergeFunction<T> extends BiFunction<List<T>, Map<Long, T>,List<T>> {
    @Override
    List<T> apply(List<T> insertList, Map<Long, T> localDataInDb) throws Throwable;
}
