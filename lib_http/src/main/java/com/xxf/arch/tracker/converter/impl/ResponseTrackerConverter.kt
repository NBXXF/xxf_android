package com.xxf.arch.tracker.converter.impl

import com.xxf.arch.http.interceptor.internal.isProbablyUtf8
import com.xxf.arch.tracker.ChanelTracker
import com.xxf.arch.tracker.converter.TrackerConverter
import okhttp3.Headers
import okhttp3.Response
import okhttp3.internal.http.promisesBody
import okio.Buffer
import okio.GzipSource
import java.lang.StringBuilder
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

internal class ResponseTrackerConverter : TrackerConverter {
    override fun convert(data: Any, extra: MutableMap<Any, Any>, chanel: ChanelTracker): String? {
        if (data is Response) {
            try {
                val sb =
                    StringBuilder("")
                sb.append("responseHeaders:\n${data?.headers}")
                sb.append("\nresponseCode:${data?.code ?: Int.MIN_VALUE}")
                sb.append("\nresponseMessage:${data?.message}")
                sb.append(
                    "responseBody:\n${logResponseBody(data)}"
                )
                return sb.toString()
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
        return null
    }

    private fun bodyHasUnknownEncoding(headers: Headers): Boolean {
        val contentEncoding = headers["Content-Encoding"] ?: return false
        return !contentEncoding.equals("identity", ignoreCase = true) &&
                !contentEncoding.equals("gzip", ignoreCase = true)
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

}