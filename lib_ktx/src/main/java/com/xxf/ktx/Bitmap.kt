package com.xxf.ktx

import android.graphics.Bitmap
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * 转二进制
 */
fun Bitmap?.toByteArray(
    format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
    quality: Int = 100
): ByteArray? {
    var outputStream: ByteArrayOutputStream? = null
    try {
        this?.let {
            outputStream = ByteArrayOutputStream()
            it.compress(format, quality, outputStream)
            outputStream?.flush()
            outputStream?.close()
            return outputStream?.toByteArray()
        }
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        try {
            outputStream?.flush()
            outputStream?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    return null
}

/**
 * 写入文件
 */
fun Bitmap.writeToFile(
    format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
    quality: Int = 100,
    file: File
) {
    FileOutputStream(file).use {
        this.compress(format, quality, it);
        it.flush()
    }
}