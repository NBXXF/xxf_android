package com.xxf.ktx.standard

/**
 * 由于Kotlin 三目运算 只能写if
 * 提供功能函数简化
 */
inline fun <T, R> T.runIf(
    predicate: Boolean,
    predicateNext: (T) -> R
): R? {
    return this.runIf({ predicate }, predicateNext)
}

/**
 * 由于Kotlin 三目运算 只能写if
 * 提供功能函数简化
 */
inline fun <T, R> T.runIf(
    predicate: (T) -> Boolean,
    predicateNext: (T) -> R
): R? {
    return this.runIfElse(predicate, predicateNext) {
        null
    }
}

/**
 * 由于Kotlin 三目运算 只能写if
 * 提供功能函数简化
 */
inline fun <T, R> T.runIfElse(
    predicate: (T) -> Boolean,
    predicateNext: (T) -> R,
    predicateElse: (T) -> R
): R {
    return if (predicate(this)) {
        predicateNext(this)
    } else {
        predicateElse(this)
    }
}

/**
 * 由于Kotlin 三目运算 只能写if
 * 提供功能函数简化
 */
inline fun <T, R> T.runIfElse(
    predicate: Boolean,
    predicateNext: (T) -> R,
    predicateElse: (T) -> R
): R {
    return if (predicate) {
        predicateNext(this)
    } else {
        predicateElse(this)
    }
}