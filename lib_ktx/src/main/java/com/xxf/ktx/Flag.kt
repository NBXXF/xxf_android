package com.xxf.ktx

/**
 * 是否包含某个flag
 */
fun Int.hashFlag(flag: Int): Boolean {
    return this and flag != 0
}