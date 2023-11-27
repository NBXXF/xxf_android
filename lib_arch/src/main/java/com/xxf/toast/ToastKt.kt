package com.xxf.toast

import android.view.Gravity
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner


/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2021/4/27
 * version 1.0.
 * toast相关拓展
 */



fun LifecycleOwner.showToast(
    msg: CharSequence,
    type: ToastType = ToastType.NORMAL,
    flag: Int = Gravity.CENTER,
    createCallBack: ((toast: Toast) -> Toast)? = null,
) {
    ToastUtils.showToast(msg, type, flag)?.let {
        createCallBack?.invoke(it)
    }}

fun LifecycleOwner.showLongToast(
    msg: CharSequence,
    type: ToastType = ToastType.NORMAL,
    flag: Int = Gravity.CENTER,
    createCallBack: ((toast: Toast) -> Toast)? = null,
) {
    ToastUtils.showToast(msg, type, flag)?.let {
        it.duration=Toast.LENGTH_LONG
        createCallBack?.invoke(it)
    }
}


fun showToast(
    msg:CharSequence,
    type: ToastType = ToastType.NORMAL,
    flag:Int= Gravity.CENTER,
    createCallBack: ((toast: Toast) -> Toast)? = null
    ) {
    ToastUtils.showToast(msg, type, flag)?.let {
        createCallBack?.invoke(it)
    }
}

fun showLongToast(
    msg:CharSequence,
    type: ToastType = ToastType.NORMAL,
    flag:Int= Gravity.CENTER,
    createCallBack: ((toast: Toast) -> Toast)? = null
) {
    ToastUtils.showToast(msg, type, flag)?.let {
        it.duration=Toast.LENGTH_LONG
        createCallBack?.invoke(it)
    }
}
