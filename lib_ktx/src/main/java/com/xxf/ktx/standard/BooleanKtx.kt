package com.xxf.ktx.standard

/**
 * 由于Kotlin 三目运算 只能写if
 * 提供功能函数简化
 */
inline fun <T> T.runIf(
    predicate: (T) -> Boolean,
    predicateNext: (T) -> Unit
) {
    this.runIfElse(predicate, predicateNext) {
    }
}

/**
 * 由于Kotlin 三目运算 只能写if
 * 提供功能函数简化
 */
inline fun <T> T.runIfElse(
    predicate: (T) -> Boolean,
    predicateNext: (T) -> Unit,
    predicateElse: (T) -> Unit = {}
) {
    if (predicate(this)) {
        predicateNext(this)
    } else {
        predicateElse(this)
    }
}