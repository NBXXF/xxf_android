package com.xxf.arch.json.datastructure;

import androidx.annotation.NonNull;

import com.xxf.arch.json.JsonUtils;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2021/5/10
 * Description ://专门为 @Query xxBean 提供的模型 因为Query 是走的toString 方式
 * 使用方式 继承此类
 */
public class QueryJsonField {
    @NonNull
    @Override
    public final String toString() {
        /**
         * 专门为 @Query xxBean 提供的模型 因为Query 是走的toString 方式
         */
        return JsonUtils.toJsonString(this);
    }
}
