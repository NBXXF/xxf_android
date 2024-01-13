package com.xxf.arch.http.body.impl


import okhttp3.MediaType
import okio.BufferedSink
import okio.source
import java.io.FileDescriptor
import java.io.FileInputStream

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.1
 * @Description 支持文件
 * @date createTime：2024/1/13
 */
open class FileDescriptorRequestBody(
    dataSource: FileDescriptor?,
    contentType: MediaType? = null
) : FileSourceRequestBody<FileDescriptor?>(dataSource, contentType) {
    override fun writeTo(sink: BufferedSink) {
        dataSource?.let {
            FileInputStream(it).use { it ->
                sink.buffer.writeAll(it.source())
            }
        }
    }
}