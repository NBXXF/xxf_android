package com.xxf.arch.test

import com.xxf.arch.model.AppBackgroundEvent
import com.xxf.bus.subscribeEvent
import com.xxf.rxjava.combineLatestDelayError
import com.xxf.rxjava.retryDelay
import io.reactivex.rxjava3.core.Observable
import java.lang.RuntimeException
import java.util.concurrent.TimeUnit

object TestUtils {
    fun format(fileName: String): String {
        return fileName.replace("[<>|:\"*?/.\\\\]".toRegex(), "")
    }

    var i = 0
    fun test() {
        Observable.fromCallable {
            println("===============>retryDelay i:$i")
            i++
        }.doOnNext {
            if (it < 5) {
                throw RuntimeException("")
            }
        }
            .retryDelay(10, 2000)
            .doOnSubscribe {
                println("===============>retryDelay sub:$it")
            }
            .doFinally {
                println("===============>retryDelay final")
            }
            .subscribe {
                println("===============>retryDelay 收到:$it")
            }

        Observable.interval(1, TimeUnit.SECONDS)
            .combineLatestDelayError(
                Observable.interval(2, TimeUnit.SECONDS),
                Observable.interval(3, TimeUnit.SECONDS)
            ) { t, t1, t2 ->
                Triple(t, t1, t2)
            }
            .subscribe {
                println("================>out:${it.first}-${it.second}-${it.third}")
            }

        AppBackgroundEvent::class.java.subscribeEvent(false)
            .subscribe {
                System.out.println("=================>app is background:" + it.isBackground)
            }
    }
}