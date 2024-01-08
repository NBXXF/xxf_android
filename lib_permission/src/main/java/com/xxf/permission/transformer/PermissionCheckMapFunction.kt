package com.xxf.permission.transformer

import android.content.Context
import com.xxf.permission.PermissionDeniedException
import io.reactivex.rxjava3.functions.Function

/**
 * @version 2.3.1
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description 将不正确的信号 转换成错误信号
 * @date createTime：2018/9/3
 */
open class PermissionCheckMapFunction(val context: Context, vararg val permission: String) :
    Function<Boolean, Boolean> {

    constructor(context: Context, permissions: List<String>) : this(
        context,
        *permissions.toTypedArray()
    )

    override fun apply(t: Boolean): Boolean {
        return t.also {
            if (!it) {
                throw PermissionDeniedException(context, *permission)
            }
        }
    }
}