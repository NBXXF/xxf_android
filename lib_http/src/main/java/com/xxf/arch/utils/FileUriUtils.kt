package com.xxf.arch.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import java.net.URLConnection
import java.util.UUID

object FileUriUtils {
    /**
     * 生成对应类型的 随机名称
     */
    internal fun generateRandomTypeFileName(context: Context, uri: Uri): String {
        return "random_${UUID.randomUUID().toString().replace("-", "")}.${
            getFileExtension(
                context,
                uri
            )
        }"
    }

    internal fun getFileExtension(context: Context, uri: Uri): String? {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(getMimeType(context, uri))?: URLConnection.guessContentTypeFromName(uri.lastPathSegment?:"")
    }

    @SuppressLint("Range")
    internal fun getFileName(context: Context, uri: Uri): String? {
        val lastPathSegment = uri.lastPathSegment
        if (lastPathSegment != null && lastPathSegment.lastIndexOf(".") > 0) {
            return lastPathSegment
        }

        try {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            if (cursor!!.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return null
    }

    internal fun getMimeType(context: Context, uri: Uri): String? {
        return context.contentResolver.getType(uri)
    }
}