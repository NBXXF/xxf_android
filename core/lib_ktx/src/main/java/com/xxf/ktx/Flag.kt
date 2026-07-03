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

/**
 * 添加某个标记位
 * 默认会判断有没有
 */
fun Int.addFlag(flag: Int): Int {
    return if (!hasFlag(flag)) {
        this or flag
    } else {
        this
    }
}