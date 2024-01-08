package com.xxf.permission.transformer

import android.content.Context
import com.xxf.permission.PermissionDeniedException
import io.reactivex.rxjava3.functions.Function

/**
 * @version 2.3.1
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description 将不正确的信号 转换成错误信号给下游 方便统一报错
 * @date createTime：2018/9/3
 */
open class PermissionCheckForResultMapFunction(val context: Context) :
    Function<Map<String, Boolean>, Boolean> {
    override fun apply(t: Map<String, Boolean>): Boolean {
        return t.all { it.value }.also {
            if (!it) {
                throw PermissionDeniedException(context, *t.keys.toTypedArray())
            }
        }
    }

}