package com.xxf.ktx.standard


/**
 * 如果为空 将进行执行block
 */
inline fun <T> T.takeIfNeeded(block: () -> T): T {
    return this ?: block()
}

/**
 * 如果为空 将返回default
 */
fun <T> T.takeIfNeeded(default: T): T {
    return this?.let {
        this
    } ?: default
}

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