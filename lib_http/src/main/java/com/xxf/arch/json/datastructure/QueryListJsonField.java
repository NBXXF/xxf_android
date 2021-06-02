package com.xxf.arch.json.datastructure;

import androidx.annotation.NonNull;

import com.xxf.arch.json.JsonUtils;

import java.util.ArrayList;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：6/2/21
 * Description ://专门为 @Query xxBean 提供的模型 因为Query 是走的toString 方式
 */
public class QueryListJsonField<T> extends ArrayList<T> {
    @NonNull
    @Override
    public final String toString() {
        /**
         * 专门为 @Query xxBean 提供的模型 因为Query 是走的toString 方式
         */
        return JsonUtils.toJsonString(this);
    }
}
