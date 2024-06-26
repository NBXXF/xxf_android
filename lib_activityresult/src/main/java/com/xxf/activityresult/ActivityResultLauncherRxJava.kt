package com.xxf.activityresult

import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.LifecycleOwner
import io.reactivex.rxjava3.core.Observable

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.1
 * @Description  用新方式来 处理activityForResult和 permissionForResult
 * @date createTime：2020/9/4
 */


/**
 *  [rxjava 方式]
 *  startActivityForResult API封装 包括权限的请求方式(新的android sdk 兼容了 权限)
 *  跳转其他页面获取结果
 *  例子：
 *  this.startActivityForResult(ActivityResultContracts.RequestPermission(),Manifest.permission.CAMERA).subscribe {
 *             Toast.makeText(this, "结果:$it",Toast.LENGTH_LONG).show();
 *         }
 *
 *  @param contact 参考 [androidx.activity.result.contract.ActivityResultContracts]里面的静态类
 *  @param input
 *  @param options
 */
@JvmOverloads
fun <I, O:Any> LifecycleOwner.startActivityForResult(
    contact: ActivityResultContract<I, O>,
    input: I,
    options: ActivityOptionsCompat? = null,
): Observable<O> {
    return ActivityResultContractObservable<I, O>(this, contact, input, options)
}


@JvmOverloads
fun <O : Any> LifecycleOwner.startActivityForResult(
    contact: ActivityResultContract<Unit, O>,
    options: ActivityOptionsCompat? = null,
): Observable<O> {
    return ActivityResultContractObservable<Unit, O>(this, contact, Unit, options)
}


/**
 *  [rxjava 方式]
 *
 *  startActivityForResult API封装 包括权限的请求方式(新的android sdk 兼容了 权限)
 *  跳转其他页面获取结果
 *  @param input
 *  @param options
 */
@JvmOverloads
fun LifecycleOwner.startActivityForResult(
    input: Intent,
    options: ActivityOptionsCompat? = null,
): Observable<ActivityResult> {
    return this.startActivityForResult(
        ActivityResultContracts.StartActivityForResult(),
        input,
        options
    )
}