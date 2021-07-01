package com.xxf.objectbox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @Description: 合并
 * @Author: XGod
 * @CreateDate: 2020/7/25 23:00
 */
public interface MergeFunction<T> {
    T apply(@NonNull T insert, @Nullable T inserted) throws Exception;
}
