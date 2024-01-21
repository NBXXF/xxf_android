package com.xxf.ktx

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener

/**
 * 对象类型 对应的key
 */
inline fun <reified T> fragmentResultKey(): String {
    return "${T::class.java.name}_${T::class.java.genericSuperclass}"
}


/**
 * 更加面向对象的接口 泛型化
 */
inline fun <reified T> Fragment.setFragmentResultListener(
    requestKey: String = fragmentResultKey<T>(),
    crossinline listener: (t: T?) -> Unit
) {
    this.setFragmentResultListener(requestKey) { _, bundle ->
        listener(bundle.get(requestKey) as? T)
    }
}

/**
 * 更加面向对象的接口 泛型化
 */
inline fun <reified T> Fragment.setFragmentResult(result: T) {
    val requestKey = fragmentResultKey<T>()
    this.setFragmentResult(fragmentResultKey<T>(), bundleOf(requestKey to result))
}