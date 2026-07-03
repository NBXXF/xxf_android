package com.xxf.toast

import android.annotation.SuppressLint
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.Gravity
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner


/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2021/4/27
 * version 1.0.
 * toast相关拓展
 */


/**
 * 修复bug
 */
fun Toast.fixBadTokenException(): Toast = apply {
    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1) {
        try {
            @SuppressLint("DiscouragedPrivateApi")
            val tnField = Toast::class.java.getDeclaredField("mTN")
            tnField.isAccessible = true
            val tn = tnField.get(this)

            val handlerField = tnField.type.getDeclaredField("mHandler")
            handlerField.isAccessible = true
            val handler = handlerField.get(tn) as Handler

            val looper = checkNotNull(Looper.myLooper()) {
                "Can't toast on a thread that has not called Looper.prepare()"
            }
            handlerField.set(tn, object : Handler(looper) {
                override fun handleMessage(msg: Message) {
                    try {
                        handler.handleMessage(msg)
                    } catch (ignored: WindowManager.BadTokenException) {
                    }
                }
            })
        } catch (ignored: Throwable) {
        }
    }
}


fun LifecycleOwner.showToast(
    msg: CharSequence,
    type: ToastType = ToastType.NORMAL,
    flag: Int = Gravity.CENTER,
    createCallBack: ((toast: Toast) -> Toast)? = null,
) {
    ToastUtils.showToast(msg, type, flag)?.let {
        createCallBack?.invoke(it)
    }
}

fun LifecycleOwner.showLongToast(
    msg: CharSequence,
    type: ToastType = ToastType.NORMAL,
    flag: Int = Gravity.CENTER,
    createCallBack: ((toast: Toast) -> Toast)? = null,
) {
    ToastUtils.showToast(msg, type, flag)?.let {
        it.duration = Toast.LENGTH_LONG
        createCallBack?.invoke(it)
    }
}


fun showToast(
    msg: CharSequence,
    type: ToastType = ToastType.NORMAL,
    flag: Int = Gravity.CENTER,
    createCallBack: ((toast: Toast) -> Toast)? = null
) {
    ToastUtils.showToast(msg, type, flag)?.let {
        createCallBack?.invoke(it)
    }
}

fun showLongToast(
    msg: CharSequence,
    type: ToastType = ToastType.NORMAL,
    flag: Int = Gravity.CENTER,
    createCallBack: ((toast: Toast) -> Toast)? = null
) {
    ToastUtils.showToast(msg, type, flag)?.let {
        it.duration = Toast.LENGTH_LONG
        createCallBack?.invoke(it)
    }
}
