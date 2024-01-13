package com.xxf.arch.http.body.impl

import com.xxf.ktx.mimeType
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okio.BufferedSink
import okio.source
import java.io.File

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.1
 * @Description 支持文件
 * @date createTime：2024/1/13
 */
class FileRequestBody(
    dataSource: File?,
    contentType: MediaType? = null
) : FileSourceRequestBody<File?>(dataSource, contentType) {

    override fun contentType(): MediaType? {
        return super.contentType() ?: dataSource?.mimeType?.toMediaTypeOrNull()
    }

    override fun contentLength(): Long {
        return dataSource?.length() ?: super.contentLength()
    }

    override fun writeTo(sink: BufferedSink) {
        dataSource?.let {
            it.inputStream().source().use { source ->
                sink.writeAll(source)
            }
        }
    }
}