package com.xxf.arch.http.body.impl

import okhttp3.MediaType
import okio.BufferedSink

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.1
 * @Description 支持文件
 * @date createTime：2024/1/13
 */
class ByteArrayRequestBody(
    dataSource: ByteArray?,
    contentType: MediaType? = null,
    val offset: Int = 0,
    val byteCount: Int = dataSource?.size?:0
) : FileSourceRequestBody<ByteArray?>(dataSource, contentType) {

    override fun contentType() = contentType

    override fun contentLength() = byteCount.toLong()

    override fun writeTo(sink: BufferedSink) {
        dataSource?.let { sink.write(it, offset, byteCount) }
    }
}