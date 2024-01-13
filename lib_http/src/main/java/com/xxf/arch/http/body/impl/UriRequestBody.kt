package com.xxf.arch.http.body.impl

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import com.xxf.ktx.mimeType
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okio.BufferedSink
import okio.IOException
import okio.source

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.1
 * @Description 支持uri
 * @date createTime：2024/1/13
 */
class UriRequestBody(
    dataSource: Uri?,
    contentType: MediaType? = null,
    private val contentResolver: ContentResolver,
) : FileSourceRequestBody<Uri?>(dataSource, contentType) {

    constructor(
        dataSource: Uri?,
        contentType: MediaType? = null,
        context: Context,
    ) : this(dataSource, contentType, context.contentResolver)

    override fun contentType(): MediaType? {
        return super.contentType() ?: dataSource?.mimeType?.toMediaTypeOrNull()
    }


    override fun writeTo(sink: BufferedSink) {
        val inputStream = dataSource?.let { contentResolver.openInputStream(it) }
            ?: throw IOException("Couldn't open content URI for reading: $dataSource")

        inputStream.source().use { source ->
            sink.writeAll(source)
        }
    }
}