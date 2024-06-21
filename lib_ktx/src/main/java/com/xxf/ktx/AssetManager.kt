package com.xxf.ktx

import android.content.Context
import android.content.res.AssetManager
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * 将Asset文件读取二进制
 */
fun AssetManager.readBytes(fileName: String): ByteArray {
    return open(fileName).use { inputStream ->
        inputStream.readBytes()
    }
}

/**
 * 将Asset文件读取为文本
 */
fun AssetManager.readString(fileName: String): String {
    return open(fileName).use { inputStream ->
        val stringBuilder = StringBuilder()
        BufferedReader(InputStreamReader(inputStream)).use { reader ->
            var line: String?
            while ((reader.readLine().also { line = it }) != null) {
                stringBuilder.append(line)
            }
        }
        stringBuilder.toString()
    }
}

/**
 * 将Asset文件读取为文本
 */
fun Context.readAssetFileString(fileName: String): String {
    return this.assets.readString(fileName)
}

/**
 * 将Asset文件读取为二进制
 */
fun Context.readAssetFile(fileName: String): ByteArray {
    return this.assets.readBytes(fileName)
}