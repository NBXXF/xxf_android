package com.xxf.arch.http.body

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.net.Uri
import android.os.ParcelFileDescriptor
import com.xxf.arch.http.body.impl.AssetFileDescriptorRequestBody
import com.xxf.arch.http.body.impl.ByteArrayRequestBody
import com.xxf.arch.http.body.impl.FileDescriptorRequestBody
import com.xxf.arch.http.body.impl.FileRequestBody
import com.xxf.arch.http.body.impl.InputStreamRequestBody
import com.xxf.arch.http.body.impl.ParcelFileDescriptorRequestBody
import com.xxf.arch.http.body.impl.PathRequestBody
import com.xxf.arch.http.body.impl.UriRequestBody
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import java.io.File
import java.io.FileDescriptor
import java.io.InputStream

/**
 * 拓展requestBody
 */
fun Uri.toRequestBody(context: Context, contentType: MediaType? = null): RequestBody {
    return UriRequestBody(this, contentType, context)
}

fun File.toRequestBody(contentType: MediaType? = null): RequestBody {
    return FileRequestBody(this, contentType)
}

fun InputStream.toRequestBody(contentType: MediaType? = null): RequestBody {
    return InputStreamRequestBody(this, contentType)
}

fun ParcelFileDescriptor.toRequestBody(contentType: MediaType? = null): RequestBody {
    return ParcelFileDescriptorRequestBody(this, contentType)
}

fun AssetFileDescriptor.toRequestBody(contentType: MediaType? = null): RequestBody {
    return AssetFileDescriptorRequestBody(this, contentType)
}

fun FileDescriptor.toRequestBody(contentType: MediaType? = null): RequestBody {
    return FileDescriptorRequestBody(this, contentType)
}

fun Path.toRequestBody(
    fileSystem: FileSystem,
    contentType: MediaType? = null,
): RequestBody {
    return PathRequestBody(this, contentType, fileSystem)
}


fun String.toFileRequestBody(contentType: MediaType? = null): RequestBody {
    return this.toPath().toRequestBody(FileSystem.SYSTEM, contentType)
}

fun ByteArray.toRequestBody(contentType: MediaType? = null): RequestBody {
    return ByteArrayRequestBody(this, contentType)
}


fun String.toJsonRequestBody(): RequestBody {
    return this.toRequestBody(MediaType.JsonUTF8)
}