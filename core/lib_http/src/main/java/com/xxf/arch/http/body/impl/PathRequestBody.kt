package com.xxf.arch.http.body.impl

import okhttp3.MediaType
import okio.BufferedSink
import okio.FileSystem
import okio.Path

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.1
 * @Description 支持文件
 * @date createTime：2024/1/13
 */
class PathRequestBody(
    dataSource: Path?,
    contentType: MediaType? = null,
    val fileSystem: FileSystem = FileSystem.SYSTEM
) : FileSourceRequestBody<Path?>(dataSource, contentType) {
    override fun contentLength() =
        dataSource?.let { fileSystem.metadata(it).size } ?: super.contentLength()

    override fun writeTo(sink: BufferedSink) {
        dataSource?.let {
            fileSystem.source(it).use { source -> sink.writeAll(source) }
        }
    }
}