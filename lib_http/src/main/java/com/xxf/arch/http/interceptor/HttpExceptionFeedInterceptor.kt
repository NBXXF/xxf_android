package com.xxf.arch.http.interceptor

import android.util.Log
import com.xxf.arch.http.interceptor.internal.isProbablyUtf8
import kotlin.Throws
import okhttp3.*
import okhttp3.internal.http.promisesBody
import okio.Buffer
import okio.GzipSource
import java.io.IOException
import java.lang.StringBuilder
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description 专注日常http异常汇报, 由于header太多, 日志系统显示不全, 所以汇报精简日志
 */
open class HttpExceptionFeedInterceptor : Interceptor {
    @Throws(IOException::class)
    final override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val startNs = System.nanoTime()
        val response: Response = try {
            chain.proceed(request)
        } catch (e: Throwable) {
            val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)
            onFeedHttpException(request, response = null, throwable = e, tookMs = tookMs)
            throw e
        }
        val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)
        onFeedHttpException(request, response = response, throwable = null, tookMs)
        return response
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
        if (throwable != null || (response != null && !response.isSuccessful)) {
            val sb = StringBuilder("http fail")
            sb.append("\n")
            sb.append("requestUrl:[${request.method}] - ${request.url} - (${tookMs}ms)")
            sb.append("\n")
            sb.append("requestHeaders:\n${request.headers}")
            if (request.body != null) {
                sb.append("requestBody:\n${logRequestBody(request)}")
            } else {
                sb.append("requestBody:")
            }
            sb.append("\n")
            sb.append("responseHeaders:\n${response?.headers}")
            sb.append("responseBody:\n${response?.let { logResponseBody(it) }}")
            sb.append("\n")
            sb.append("throwable:${Log.getStackTraceString(throwable)}")
            onFeedHttpException(sb.toString())
        }
    }

    open fun onFeedHttpException(string: String) {

    }

    private fun logRequestBody(request: Request): String {
        val requestBody = request.body ?: return ""
        when {
            bodyHasUnknownEncoding(request.headers) -> {
                return "(encoded body omitted)"
            }
            requestBody.isDuplex() -> {
                return "(duplex request body omitted)"
            }
            requestBody.isOneShot() -> {
                return "(one-shot body omitted)"
            }
            else -> {
                val buffer = Buffer()
                requestBody.writeTo(buffer)

                val contentType = requestBody.contentType()
                val charset: Charset =
                    contentType?.charset(StandardCharsets.UTF_8) ?: StandardCharsets.UTF_8

                return if (buffer.isProbablyUtf8()) {
                    buffer.readString(charset)
                } else {
                    "body omitted"
                }
            }
        }
    }

    private fun logResponseBody(response: Response): String {
        if (!response.promisesBody()) {
            return "<-- END HTTP"
        } else if (bodyHasUnknownEncoding(response.headers)) {
            return "<-- END HTTP (encoded body omitted)"
        } else {
            val responseBody = response.body ?: return ""
            val source = responseBody.source()
            source.request(Long.MAX_VALUE) // Buffer the entire body.
            var buffer = source.buffer

            val headers = response.headers
            var gzippedLength: Long? = null
            if ("gzip".equals(headers["Content-Encoding"], ignoreCase = true)) {
                gzippedLength = buffer.size
                GzipSource(buffer.clone()).use { gzippedResponseBody ->
                    buffer = Buffer()
                    buffer.writeAll(gzippedResponseBody)
                }
            }

            val contentType = responseBody.contentType()
            val charset: Charset =
                contentType?.charset(StandardCharsets.UTF_8) ?: StandardCharsets.UTF_8

            if (!buffer.isProbablyUtf8()) {
                return "(binary ${buffer.size}-byte body omitted)"
            }

            val contentLength = responseBody.contentLength()
            if (contentLength != 0L) {
                return buffer.clone().readString(charset)
            }
            return if (gzippedLength != null) {
                "$gzippedLength-gzipped-byte body)"
            } else {
                "${buffer.size}-byte body)"
            }
        }
    }

    private fun bodyHasUnknownEncoding(headers: Headers): Boolean {
        val contentEncoding = headers["Content-Encoding"] ?: return false
        return !contentEncoding.equals("identity", ignoreCase = true) &&
                !contentEncoding.equals("gzip", ignoreCase = true)
    }
}