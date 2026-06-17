package com.xxf.arch.http.request

import com.xxf.arch.http.body.impl.GzipRequestBody
import okhttp3.Request

fun Request.Builder.gzipBodyIfNeeded(
    request: Request
): Request.Builder {
    val originBody = request.body ?: return this

    ///避免重复编码
    if (request.header("Content-Encoding") != null) {
        return this
    }

    return this
        .header("Content-Encoding", "gzip")
        .method(request.method, GzipRequestBody.wrap(originBody))
}