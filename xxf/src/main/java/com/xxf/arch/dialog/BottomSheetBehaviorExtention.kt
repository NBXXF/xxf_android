package com.xxf.arch.dialog

import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2021/9/11
 * Description ://BottomSheetBehavior 扩展
 */

/**
 * 清除BottomSheetBehavior 默认背景
 */
inline fun <reified T : View> BottomSheetBehavior<T>.clearShapeDrawable() {
    try {
        val declaredField = this::class.java.getDeclaredField("materialShapeDrawable")
        declaredField.isAccessible = true
        declaredField.set(this, null);
    } catch (e: Throwable) {
        e.printStackTrace()
    }
}
