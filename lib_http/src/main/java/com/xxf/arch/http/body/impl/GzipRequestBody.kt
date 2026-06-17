package com.xxf.arch.http.body.impl

import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink
import okio.GzipSink
import okio.buffer

/**
 * RequestBody 的 gzip 包装器，只在写出请求体时流式压缩原始内容。
 *
 * 压缩后的长度需要完整写完才能确定，所以这里固定返回 -1，让 OkHttp 使用
 * chunked transfer。这样可以避免为了计算 Content-Length 先把整个 body 压到内存里。
 *
 * 作为库 API 对外提供包装能力即可，不建议 open 给外部继承，避免子类改变 gzip 写入语义。
 *
 * 如果是拦截器里面搞,可以先判断一下
 * if (originalBody == null || original.header("Content-Encoding") != null) {
 *     return chain.proceed(original)
 * }
 */
public class GzipRequestBody private constructor(
    private val originBody: RequestBody
) : RequestBody() {

    /**
     * gzip 只改变传输编码，不改变业务内容类型，因此沿用原始 body 的 Content-Type。
     */
    override fun contentType(): MediaType? = originBody.contentType()

    /**
     * gzip 后的字节数只能在完整压缩完成后知道。
     * 这里返回 -1，让 OkHttp 不发送固定 Content-Length，改用流式写出。
     */
    override fun contentLength() = -1L

    /**
     * 保留原始 body 的一次性语义。
     * 例如文件流、输入流等不可重复读取的 body，被 gzip 包装后仍然不可重复写入。
     */
    override fun isOneShot(): Boolean = originBody.isOneShot()

    /**
     * 保留原始 body 的 duplex 语义，避免包装后改变 OkHttp 对请求/响应并发传输的判断。
     */
    override fun isDuplex(): Boolean = originBody.isDuplex()

    /**
     * 写出时把下游 sink 包成 GzipSink，原始 body 写入的数据会被实时压缩后发送。
     */
    override fun writeTo(sink: BufferedSink) {
        // use 会 close GzipSink，从而写入 gzip footer 并 flush 到下游 sink。
        GzipSink(sink).buffer().use { gzipSink ->
            originBody.writeTo(gzipSink)
        }
    }

    companion object {

        /**
         * 默认 gzip 阀门。已知原始长度小于 1KB 时，压缩收益通常抵不过 gzip 头和 CPU 成本。
         */
        public const val DEFAULT_MIN_GZIP_SIZE: Long = 1024L

        /**
         * 判断 body 是否已经被本包装器处理过，用于避免重复 gzip。
         */
        public fun isGzipped(body: RequestBody): Boolean = body is GzipRequestBody

        /**
         * 判断 body 是否需要 gzip。
         *
         * 已知长度且小于阀门时跳过 gzip；未知长度返回 -1，无法提前判断大小，仍保持流式 gzip。
         */
        public fun shouldGzip(
            body: RequestBody,
            minGzipSize: Long = DEFAULT_MIN_GZIP_SIZE
        ): Boolean {
            if (isGzipped(body)) {
                return true
            }
            val contentLength = body.contentLength()
            return contentLength !in 0L..<minGzipSize
        }

        /**
         * 幂等包装：已是 GzipRequestBody 时直接返回；低于 gzip 阀门时返回原始 body；其余情况创建新的 gzip 包装体。
         */
        public fun wrap(
            body: RequestBody,
            minGzipSize: Long = DEFAULT_MIN_GZIP_SIZE
        ): RequestBody {
            if (!shouldGzip(body, minGzipSize)) {
                return body
            }
            return body as? GzipRequestBody ?: GzipRequestBody(body)
        }
    }
}
