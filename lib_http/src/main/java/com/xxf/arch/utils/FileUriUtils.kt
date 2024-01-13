package com.xxf.arch.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import java.net.URLConnection
import java.util.UUID

object FileUriUtils {
    private inline val String.mimeType: String?
        get() {
            MimeTypeMap.getSingleton().getMimeTypeFromExtension(this)?.let {
                return it
            }
            return URLConnection.guessContentTypeFromName(this)
        }

    /**
     * 生成对应类型的 随机名称
     */
    internal fun generateRandomTypeFileName(context: Context, uri: Uri): String {
        val mimeType = getMimeType(context, uri)
        val extensionFromMimeType = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
        return "random_${UUID.randomUUID().toString().replace("-", "")}.${
            extensionFromMimeType
        }"
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

    /**
     * 需要动态查询
     */
    internal fun getMimeType(context: Context, uri: Uri): String? {
        return uri.toString().mimeType ?: context.contentResolver.getType(uri)
    }
}