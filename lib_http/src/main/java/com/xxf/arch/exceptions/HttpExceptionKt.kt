package com.xxf.arch.exceptions

import com.xxf.arch.http.interceptor.internal.isProbablyUtf8
import retrofit2.HttpException
import java.nio.charset.StandardCharsets

val HttpException.rawResponse: okhttp3.Response? get() = response()?.raw()
val HttpException.rawResponseRequest: okhttp3.Request? get() = rawResponse?.request
val HttpException.rawResponseHeaders: okhttp3.Headers? get() = rawResponse?.headers
val HttpException.rawResponseBody: okhttp3.ResponseBody? get() = rawResponse?.body
val HttpException.errorBody: okhttp3.ResponseBody? get() = response()?.errorBody()

fun HttpException.readBodyString(): String? {
    try {
        val source = errorBody!!
            .source()
        source.request(Long.MAX_VALUE) // Buffer the entire body.

        val buffer = source.buffer()
        var charset = StandardCharsets.UTF_8
        val contentType = errorBody!!.contentType()
        if (contentType != null) {
            charset = contentType.charset(StandardCharsets.UTF_8)
        }
        if (buffer.isProbablyUtf8()) {
            return buffer.clone().readString(charset!!)
        }
    } catch (e: Throwable) {
        e.printStackTrace()
    }
    return null
}