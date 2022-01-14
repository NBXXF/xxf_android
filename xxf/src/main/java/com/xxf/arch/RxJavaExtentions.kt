package com.xxf.arch

import android.view.Gravity
import androidx.lifecycle.LifecycleOwner
import com.xxf.application.activity.topFragmentActivity
import com.xxf.arch.rxjava.transformer.ProgressHUDTransformerImpl
import com.xxf.arch.rxjava.transformer.UIErrorTransformer
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2021/9/2
 * Description : 增加扩展
 */

//***************************Observable**************************//
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
 * 默认绑定 topFragmentActivity
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

//***************************Flowable**************************//

inline fun <reified T> Flowable<T>.bindProgressHud(
    lifecycleOwner: LifecycleOwner,
    loadingNotice: String? = "",
    successNotice: String? = "",
    errorNotice: String? = null
): Flowable<T> {
    val progressHUDTransformerImpl = ProgressHUDTransformerImpl<T>(lifecycleOwner)
    progressHUDTransformerImpl.setLoadingNotice(loadingNotice)
    progressHUDTransformerImpl.setSuccessNotice(successNotice)
    progressHUDTransformerImpl.setErrorNotice(errorNotice)
    return this.compose(progressHUDTransformerImpl);
}

/**
 * 添加加载圈loading
 * 默认绑定 topFragmentActivity
 */
inline fun <reified T> Flowable<T>.bindProgressHud(
    loadingNotice: String? = "",
    successNotice: String? = "",
    errorNotice: String? = null
): Flowable<T> {
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

//***************************Flowable**************************//

inline fun <reified T> Maybe<T>.bindProgressHud(
    lifecycleOwner: LifecycleOwner,
    loadingNotice: String? = "",
    successNotice: String? = "",
    errorNotice: String? = null
): Maybe<T> {
    val progressHUDTransformerImpl = ProgressHUDTransformerImpl<T>(lifecycleOwner)
    progressHUDTransformerImpl.setLoadingNotice(loadingNotice)
    progressHUDTransformerImpl.setSuccessNotice(successNotice)
    progressHUDTransformerImpl.setErrorNotice(errorNotice)
    return this.compose(progressHUDTransformerImpl);
}

/**
 * 添加加载圈loading
 * 默认绑定 topFragmentActivity
 */
inline fun <reified T> Maybe<T>.bindProgressHud(
    loadingNotice: String? = "",
    successNotice: String? = "",
    errorNotice: String? = null
): Maybe<T> {
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

//***************************流错误提示toast**************************//


/**
 * 在流发生错误的时候增加提示
 * @param toastFlag 对应toast的类型
 */
inline fun <reified T> Observable<T>.bindErrorNotice(toastFlag: Int = Gravity.CENTER): @NonNull Observable<T> {
    val uiErrorTransformer = UIErrorTransformer<T>(XXF.getErrorHandler(), toastFlag)
    return this.compose(uiErrorTransformer);
}

/**
 * 在流发生错误的时候增加提示
 * @param toastFlag  对应toast的类型
 */
inline fun <reified T> Flowable<T>.bindErrorNotice(toastFlag: Int = Gravity.CENTER): @NonNull Flowable<T> {
    val uiErrorTransformer = UIErrorTransformer<T>(XXF.getErrorHandler(), toastFlag)
    return this.compose(uiErrorTransformer);
}

/**
 * 在流发生错误的时候增加提示
 * @param toastFlag 对应toast的类型
 */
inline fun <reified T> Maybe<T>.bindErrorNotice(toastFlag: Int = Gravity.CENTER): @NonNull Maybe<T> {
    val uiErrorTransformer = UIErrorTransformer<T>(XXF.getErrorHandler(), toastFlag)
    return this.compose(uiErrorTransformer);
}