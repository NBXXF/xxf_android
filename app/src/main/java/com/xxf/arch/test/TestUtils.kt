package com.xxf.arch.test

import com.xxf.rxjava.combineLatestDelayError
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit

object TestUtils {
    fun format(fileName: String): String {
        return fileName.replace("[<>|:\"*?/.\\\\]".toRegex(), "")
    }

    fun test() {
        Observable.interval(1, TimeUnit.SECONDS)
            .combineLatestDelayError(Observable.interval(2, TimeUnit.SECONDS), ::Pair)
            .subscribe {
                println("================>out:${it.first}-${it.second}")
            }
    }
}