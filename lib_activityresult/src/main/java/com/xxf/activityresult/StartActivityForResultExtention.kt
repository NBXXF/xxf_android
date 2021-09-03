package com.xxf.activityresult

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import java.lang.IllegalArgumentException
import java.lang.RuntimeException

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2021/9/2
 * Description : activity forResult 扩展   支持activity fragment LifecycleOwner 挂载对象访问
 */

/**
 * 等价于startActivityForResult  可以结合内敛函数topFragmentActivity
 */
inline fun <reified T : FragmentActivity> T.startActivityForResultObservable(
    intent: Intent,
    requestCode: Int,
    options: Bundle? = null
): Observable<ActivityResult> {
    return Observable.defer {
        RxActivityResultCompact.startActivityForResult(this, intent, requestCode, options)
    }.subscribeOn(AndroidSchedulers.mainThread())
}

/**
 * 等价于startActivityForResult  可以结合内敛函数topFragmentActivity
 */
inline fun <reified T : Fragment> T.startActivityForResultObservable(
    intent: Intent,
    requestCode: Int,
    options: Bundle? = null
): Observable<ActivityResult> {
    return Observable.defer {
        RxActivityResultCompact.startActivityForResult(this, intent, requestCode, options)
    }.subscribeOn(AndroidSchedulers.mainThread())
}

/**
 * 等价于startActivityForResult  可以结合内敛函数topFragmentActivity
 */
inline fun <reified T : LifecycleOwner> T.startActivityForResultObservable(
    intent: Intent,
    requestCode: Int,
    options: Bundle? = null
): Observable<ActivityResult> {
    return Observable.defer {
        if (this is FragmentActivity) {
            return@defer RxActivityResultCompact.startActivityForResult(
                this,
                intent,
                requestCode,
                options
            )
        } else if (this is Fragment) {
            return@defer RxActivityResultCompact.startActivityForResult(
                this,
                intent,
                requestCode,
                options
            )
        }
        return@defer Observable.error(IllegalArgumentException("不支持的类型!"))
    }.subscribeOn(AndroidSchedulers.mainThread())
}