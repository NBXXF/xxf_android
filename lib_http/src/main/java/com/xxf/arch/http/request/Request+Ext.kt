package com.xxf.arch.http.request

import com.xxf.arch.http.body.impl.GzipRequestBody
import okhttp3.Request

fun Request.Builder.gzipBodyIfNeeded(
    request: Request,
    minGzipSize: Long = GzipRequestBody.DEFAULT_MIN_GZIP_SIZE
): Request.Builder {
    val originBody = request.body ?: return this

    ///避免重复编码
    if (request.header("Content-Encoding") != null) {
        return this
    }

    // 小 body 的 gzip 收益有限，低于阀门时不设置 Content-Encoding，也不包装 body。
    if (!GzipRequestBody.shouldGzip(originBody, minGzipSize)) {
        return this
    }

    return this
        .header("Content-Encoding", "gzip")
        .method(request.method, GzipRequestBody.wrap(originBody, minGzipSize))
}
