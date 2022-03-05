package com.xxf.arch.json.datastructure;

import androidx.annotation.NonNull;

import com.xxf.arch.json.JsonUtils;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：6/2/21
 * Description ://专门为 @Query xxBean 提供的模型 因为Query 是走的toString 方式
 * 将 target 转换成json
 */
public final class QueryJsonField<T> {

    /**
     * 静态创建方法
     *
     * @param t
     * @param <T>
     * @return
     */
    public static final <T> QueryJsonField<T> create(T t) {
        return new QueryJsonField<T>(t);
    }

    private T target;

    public T getTarget() {
        return target;
    }

    public QueryJsonField(T target) {
        this.target = target;
    }

    @NonNull
    @Override
    public final String toString() {
        if (target == null) {
            return null;
        }
        /**
         * 专门为 @Query xxBean 提供的模型 因为Query 是走的toString 方式
         */
        return JsonUtils.toJsonString(target,true);
    }
}
