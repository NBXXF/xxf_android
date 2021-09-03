package com.xxf.arch

import androidx.lifecycle.LifecycleOwner
import com.xxf.application.activity.topActivity
import com.xxf.application.activity.topFragmentActivity
import com.xxf.arch.rxjava.transformer.ProgressHUDTransformerImpl
import com.xxf.arch.rxjava.transformer.UIErrorTransformer
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Observable

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2021/9/2
 * Description : 增加扩展
 */

/**
 * 添加加载圈loading
 */
inline fun <reified T> Observable<T>.bindProgressHud(
    lifecycleOwner: LifecycleOwner,
    loadingNotice: String? = "",
    successNotice: String? = "",
    errorNotice: String? = null
): Observable<T> {
    val progressHUDTransformerImpl = ProgressHUDTransformerImpl<T>(lifecycleOwner)
    progressHUDTransformerImpl.setLoadingNotice(loadingNotice)
    progressHUDTransformerImpl.setSuccessNotice(successNotice)
    progressHUDTransformerImpl.setErrorNotice(errorNotice)
    return this.compose(progressHUDTransformerImpl);
}

/**
 * 添加加载圈loading
 */
inline fun <reified T> Observable<T>.bindProgressHud(
    loadingNotice: String? = "",
    successNotice: String? = "",
    errorNotice: String? = null
): Observable<T> {
    val topActivity = topFragmentActivity
    if (topActivity != null) {
        var progressHUDTransformerImpl = ProgressHUDTransformerImpl<T>(topActivity)
        progressHUDTransformerImpl.setLoadingNotice(loadingNotice)
        progressHUDTransformerImpl.setSuccessNotice(successNotice)
        progressHUDTransformerImpl.setErrorNotice(errorNotice)
        return this.compose(progressHUDTransformerImpl);
    }
    return this
}

/**
 * 在流发生错误的时候增加提示
 */
inline fun <reified T> Observable<T>.bindErrorNotice(): @NonNull Observable<T>? {
    val uiErrorTransformer = UIErrorTransformer<T>(XXF.getErrorHandler())
    return this.compose(uiErrorTransformer);
}