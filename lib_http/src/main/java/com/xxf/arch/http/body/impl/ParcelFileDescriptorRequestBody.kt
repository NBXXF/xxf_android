package com.xxf.arch.http.body.impl

import android.os.ParcelFileDescriptor
import okhttp3.MediaType
import okio.BufferedSink
/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.1
 * @Description 支持文件
 * @date createTime：2024/1/13
 */
class ParcelFileDescriptorRequestBody(
    dataSource: ParcelFileDescriptor?,
    override val contentType: MediaType? = null
) : FileSourceRequestBody<ParcelFileDescriptor?>(dataSource, contentType) {
    private val proxy = FileDescriptorRequestBody(dataSource?.fileDescriptor, contentType);

    override fun writeTo(sink: BufferedSink) {
        proxy.writeTo(sink)
    }
}