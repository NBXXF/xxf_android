package com.xxf.arch.http.body

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.net.Uri
import android.os.ParcelFileDescriptor
import com.xxf.arch.utils.FileUriUtils
import okhttp3.MediaType
import okhttp3.MultipartBody
import java.io.File
import java.io.FileDescriptor
import java.io.InputStream


fun File.toPart(
    partName: String,
    partFilename: String? = this.name,
    contentType: MediaType? = null
): MultipartBody.Part {
    return MultipartBody.Part.createFormData(
        partName,
        partFilename,
        this.toRequestBody(contentType)
    )
}

fun Uri.toPart(
    context: Context,
    partName: String,
    partFilename: String? = FileUriUtils.getFileName(context, this)
        ?: FileUriUtils.generateRandomTypeFileName(context, this),
    contentType: MediaType? = null
): MultipartBody.Part {
    return MultipartBody.Part.createFormData(
        partName,
        partFilename,
        this.toRequestBody(context, contentType)
    )
}

fun InputStream.toPart(
    partName: String,
    partFilename: String? = null,
    contentType: MediaType? = null
): MultipartBody.Part {
    return MultipartBody.Part.createFormData(
        partName,
        partFilename,
        this.toRequestBody(contentType)
    )
}

fun ParcelFileDescriptor.toPart(
    partName: String,
    partFilename: String? = null,
    contentType: MediaType? = null
): MultipartBody.Part {
    return MultipartBody.Part.createFormData(
        partName,
        partFilename,
        this.toRequestBody(contentType)
    )
}


fun AssetFileDescriptor.toPart(
    partName: String,
    partFilename: String? = null,
    contentType: MediaType? = null
): MultipartBody.Part {
    return MultipartBody.Part.createFormData(
        partName,
        partFilename,
        this.toRequestBody(contentType)
    )
}

fun FileDescriptor.toPart(
    partName: String,
    partFilename: String? = null,
    contentType: MediaType? = null
): MultipartBody.Part {
    return MultipartBody.Part.createFormData(
        partName,
        partFilename,
        this.toRequestBody(contentType)
    )
}

fun String.toPart(
    name: String
): MultipartBody.Part {
    return MultipartBody.Part.createFormData(name, this)
}

