package com.xxf.arch.http.body

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.net.Uri
import android.os.ParcelFileDescriptor
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.BufferedSink
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import okio.source
import java.io.File
import java.io.FileDescriptor
import java.io.FileInputStream
import java.io.InputStream

/**
 * 拓展requestBody
 */
fun Uri.toRequestBody(context: Context, contentType: MediaType? = null): RequestBody {
    return ContentUriRequestBody(context, this, contentType)
}

fun File.toRequestBody(contentType: MediaType? = null): RequestBody {
    return this.asRequestBody(contentType)
}

fun InputStream.toRequestBody(contentType: MediaType? = null): RequestBody {
    return object : RequestBody() {
        override fun contentType() = contentType

        override fun isOneShot(): Boolean = true

        override fun writeTo(sink: BufferedSink) {
            this@toRequestBody.use {
                sink.buffer.writeAll(it.source())
            }
        }
    }
}

fun ParcelFileDescriptor.toRequestBody(contentType: MediaType? = null): RequestBody {
    return this.fileDescriptor.toRequestBody(contentType)
}

fun AssetFileDescriptor.toRequestBody(contentType: MediaType? = null): RequestBody {
    return this.fileDescriptor.toRequestBody(contentType)
}

fun FileDescriptor.toRequestBody(contentType: MediaType? = null): RequestBody {
    return object : RequestBody() {
        override fun contentType() = contentType

        override fun isOneShot(): Boolean = true

        override fun writeTo(sink: BufferedSink) {
            FileInputStream(this@toRequestBody).use {
                sink.buffer.writeAll(it.source())
            }
        }
    }
}

fun Path.toRequestBody(
    fileSystem: FileSystem,
    contentType: MediaType? = null,
): RequestBody {
    return object : RequestBody() {
        override fun contentType() = contentType

        override fun contentLength() = fileSystem.metadata(this@toRequestBody).size ?: -1

        override fun writeTo(sink: BufferedSink) {
            fileSystem.source(this@toRequestBody).use { source -> sink.writeAll(source) }
        }
    }
}


fun String.toFileRequestBody(contentType: MediaType? = null): RequestBody {
    return this.toPath().toRequestBody(FileSystem.SYSTEM, contentType)
}

fun ByteArray.toFileRequestBody(contentType: MediaType? = null): RequestBody {
    return this.toRequestBody(contentType)
}


fun String.toJsonRequestBody(): RequestBody {
    return this.toRequestBody("application/json; charset=utf-8".toMediaType())
}