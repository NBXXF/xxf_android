package com.xxf.arch.test

import com.xxf.arch.model.AppBackgroundEvent
import com.xxf.bus.subscribeEvent
import com.xxf.rxjava.combineLatestDelayError
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit

object TestUtils {
    fun format(fileName: String): String {
        return fileName.replace("[<>|:\"*?/.\\\\]".toRegex(), "")
    }

    fun test() {
        Observable.interval(1, TimeUnit.SECONDS)
            .combineLatestDelayError(Observable.interval(2, TimeUnit.SECONDS),Observable.interval(3, TimeUnit.SECONDS)){t,t1,t2->
                Triple(t,t1,t2)
            }
            .subscribe {
                println("================>out:${it.first}-${it.second}-${it.third}")
            }

        AppBackgroundEvent::class.java.subscribeEvent(false)
            .subscribe {
                System.out.println("=================>app is background:"+it.isBackground)
            }
    }
}