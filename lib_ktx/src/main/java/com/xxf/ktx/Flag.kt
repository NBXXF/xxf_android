package com.xxf.ktx

/**
 * 是否包含某个flag
 */
fun Int.hasFlag(flag: Int): Boolean {
    return this and flag != 0
}

/**
 * 去除某个标记位
 */
fun Int.excludeFlag(valueRemoveFlag: Int): Int {
    val resultAfterRemoval: Int = this and valueRemoveFlag.inv()
    return resultAfterRemoval
}