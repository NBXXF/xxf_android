package com.xxf.activityresult

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2021/9/2
 * Description : activity forResult 扩展
 */

/**
 * 等价于startActivityForResult
 */
inline fun <reified T : FragmentActivity> T.startActivityForObservableResult(
    intent: Intent,
    requestCode: Int,
    options: Bundle? = null
): Observable<ActivityResult> {
    return Observable.defer {
        RxActivityResultCompact.startActivityForResult(this, intent, requestCode, options)
    }.subscribeOn(AndroidSchedulers.mainThread())
}

/**
 * 等价于startActivityForResult
 */
inline fun <reified T : Fragment> T.startActivityForObservableResult(
    intent: Intent,
    requestCode: Int,
    options: Bundle? = null
): Observable<ActivityResult> {
    return Observable.defer {
        RxActivityResultCompact.startActivityForResult(this, intent, requestCode, options)
    }.subscribeOn(AndroidSchedulers.mainThread())
}