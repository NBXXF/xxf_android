package com.xxf.arch.http.body

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink
import okio.IOException
import okio.source

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.1
 * @Description 支持uri
 * @date createTime：2024/1/13
 */
class ContentUriRequestBody(
    private val contentResolver: ContentResolver,
    private val contentUri: Uri,
    private val contentType: MediaType? = null
) : RequestBody() {

    constructor(
        context: Context,
        contentUri: Uri,
        contentType: MediaType? = null
    ) : this(context.contentResolver, contentUri,contentType)

    override fun contentType(): MediaType? {
        if (this.contentType != null) {
            return contentType
        }
        val contentType = contentResolver.getType(contentUri) ?: return null
        return contentType.toMediaTypeOrNull()
    }


    override fun writeTo(sink: BufferedSink) {
        val inputStream = contentResolver.openInputStream(contentUri)
            ?: throw IOException("Couldn't open content URI for reading: $contentUri")

        inputStream.source().use { source ->
            sink.writeAll(source)
        }
    }
}