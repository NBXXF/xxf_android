package com.xxf.arch.rxjava.transformer.filter

import io.reactivex.rxjava3.functions.Predicate

/**
 * 不过滤
 */
object ErrorNoFilter : Predicate<Throwable> {
    override fun test(t: Throwable?): Boolean {
        return true
    }
}