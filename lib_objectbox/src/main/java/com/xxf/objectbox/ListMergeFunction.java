package com.xxf.objectbox;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Map;


/**
 * @Description: 合并
 * @Author: XGod
 * @CreateDate: 2020/7/25 23:00
 */
public interface ListMergeFunction<T> {
    List<T> apply(@NonNull List<T> insert, @NonNull Map<Long, T> inserted) throws Exception;
}
