package com.xxf.arch.tracker.converter.impl

import com.xxf.arch.http.interceptor.internal.isProbablyUtf8
import com.xxf.arch.tracker.ChanelTracker
import com.xxf.arch.tracker.converter.TrackerConverter
import okhttp3.Headers
import okhttp3.Request
import okio.Buffer
import java.lang.StringBuilder
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

class RequestTrackerConverter : TrackerConverter {
    companion object {
        val KEY_REQUEST_METHOD: String = "http_method"
        val KEY_REQUEST_URL: String = "http_url"
    }

    override fun convert(data: Any, extra: MutableMap<Any, Any>, chanel: ChanelTracker): String? {
        if (data is Request) {
            try {
                val methodUrl = "[${data.method}]-${data.url}";
                val sb =
                    StringBuilder(methodUrl)
                sb.append("\n")
                sb.append("requestHeaders:\n${data.headers}")
                if (data.body != null) {
                    sb.append("requestBody:\n${logRequestBody(data)}")
                } else {
                    sb.append("requestBody:")
                }
                return sb.toString().apply {
                    extra[KEY_REQUEST_METHOD] = data.method
                    extra[KEY_REQUEST_URL] = data.url.toString()
                }
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

}