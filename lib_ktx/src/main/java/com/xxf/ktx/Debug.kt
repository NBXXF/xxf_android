package com.xxf.ktx

import com.xxf.ktx.BuildConfig.DEBUG

/**
 * 仅仅debug模式下才会执行
 */
@Deprecated(message = "暂未实现完全")
inline fun runDebugging(block: () -> Unit) {
    if (DEBUG) {
        block()
    }
}