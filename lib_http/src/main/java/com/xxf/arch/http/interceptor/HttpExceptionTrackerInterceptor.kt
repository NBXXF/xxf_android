package com.xxf.arch.http.interceptor

import android.os.SystemClock
import com.xxf.arch.tracker.Tracker
import com.xxf.arch.tracker.converter.EmptyChanelTracker
import com.xxf.arch.tracker.converter.impl.ThrowableTrackerConverter
import com.xxf.arch.tracker.converter.impl.RequestTrackerConverter
import com.xxf.arch.tracker.converter.impl.ResponseTrackerConverter
import kotlin.Throws
import okhttp3.*
import java.io.IOException
import java.lang.StringBuilder
import java.util.concurrent.TimeUnit

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description 专注日常http异常汇报, 由于header太多, 日志系统显示不全, 所以汇报精简日志
 */
open class HttpExceptionTrackerInterceptor : Interceptor {
    @Throws(IOException::class)
    final override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val startNs = SystemClock.elapsedRealtimeNanos()
        val response: Response = try {
            chain.proceed(request)
        } catch (e: Throwable) {
            e.run {
                val tookMs =
                    TimeUnit.NANOSECONDS.toMillis(SystemClock.elapsedRealtimeNanos() - startNs)
                onFeedHttpException(request, response = null, throwable = e, tookMs = tookMs)
            }
            throw e
        }
        return response.also {
            if (!it.isSuccessful) {
                val tookMs =
                    TimeUnit.NANOSECONDS.toMillis(SystemClock.elapsedRealtimeNanos() - startNs)
                onFeedHttpException(request, response = response, throwable = null, tookMs = tookMs)
            }
        }
    }

    /**
     * 是否应该上报异常
     */
    open fun shouldFeedHttpException(
        request: Request,
        response: Response?,
        throwable: Throwable?
    ): Boolean {
        return (throwable != null || (response != null && !response.isSuccessful))
    }

    /**
     * 发现异常
     */
    open fun onFeedHttpException(
        request: Request,
        response: Response?,
        throwable: Throwable?,
        tookMs: Long
    ) {
        if (shouldFeedHttpException(request, response, throwable)) {
            val mutableMapOf = mutableMapOf<Any, Any>()
            val sb = StringBuilder().apply {
                this.append(
                    RequestTrackerConverter().convert(
                        request,
                        mutableMapOf,
                        EmptyChanelTracker()
                    )
                )
                this.append("\n")
                this.append(
                    ResponseTrackerConverter().convert(
                        request,
                        mutableMapOf,
                        EmptyChanelTracker()
                    )
                )
                this.append("\n")
                this.append(
                    "throwable:${
                        throwable?.let {
                            ThrowableTrackerConverter().convert(
                                it,
                                mutableMapOf,
                                EmptyChanelTracker()
                            )
                        }
                    }"
                )
            }
            Tracker.track(sb.toString(), mutableMapOf.apply {
                this["takeTime"] = tookMs
            })
        }
    }

}