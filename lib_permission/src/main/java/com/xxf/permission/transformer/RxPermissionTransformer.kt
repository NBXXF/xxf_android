package com.xxf.permission.transformer

import android.content.Context
import com.xxf.permission.PermissionDeniedException
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableSource
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.functions.Function

/**
 * @version 2.3.1
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description 将不正确的信号 转换成错误信号
 * @date createTime：2018/9/3
 */
open class RxPermissionTransformer(val context: Context, vararg val permission: String) :
    ObservableTransformer<Boolean, Boolean> {

    override fun apply(upstream: Observable<Boolean>): ObservableSource<Boolean> {
        return upstream
            .map(Function { granted ->
                if (!granted) {
                    throw PermissionDeniedException(context, *permission)
                }
                granted
            })
    }
}