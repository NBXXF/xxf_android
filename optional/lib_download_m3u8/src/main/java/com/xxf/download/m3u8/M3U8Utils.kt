package com.xxf.download.m3u8

import android.util.Base64
import com.arthenica.mobileffmpeg.FFmpeg
import com.xxf.download.m3u8.model.M3U8SegmentInfo
import com.nbxxf.kpower.ktx.mkParentDirs
import com.nbxxf.kpower.ktx.randomUUIDString32
import com.nbxxf.kpower.ktx.rename
import okio.ByteString.Companion.decodeHex
import java.io.File
import java.io.IOException
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


/**
 * @Author: XGod  xuanyouwu@163.com  17611639080
 * Date: 22/7/24 12:07 PM
 * Description: m3u8工具类
 */
object M3U8Utils {
    /**
     * 现在主流播放器都可以播放ts了
     * 合并ts文件
     * @param segmentList 文件列表
     * @param toFile   合并之后的文件
     */
    @Throws(IOException::class)
    fun mergeTs(
        segmentList: List<M3U8SegmentInfo>,
        toFile: File
    ): File? {
        if (toFile.exists()) {
            return toFile
        }
        val tempFile = File(toFile.parentFile, randomUUIDString32)
        try {
            tempFile.mkParentDirs()
            tempFile.outputStream().use { out ->
                segmentList.forEach {
                    out.write(decrypt(it))
                }
            }
            //操作完全成功才命名过去
            tempFile.rename(toFile.name)
            return toFile
        } catch (e: Throwable) {
            e.printStackTrace()
            return null
        } finally {
            tempFile.delete()
        }
    }

    /**
     * 解密m3u8
     */
    @JvmOverloads
    fun decrypt(info: M3U8SegmentInfo): ByteArray {
        return if (info.keyFile?.exists() == true && !info.encryptionIV.isNullOrBlank()) {
            decrypt(
                info.tsFile.readBytes(),
                info.keyFile.readText(),
                info.encryptionIV.decodeHex().string(Charsets.UTF_8)
            )
        } else {
            info.tsFile.readBytes()
        }
    }

    /**
     * AES 解密操作
     * @param content
     * @param key 秘钥
     * @param encryptionIV 偏移量
     */
    private fun decrypt(content: ByteArray, key: String, encryptionIV: String): ByteArray {
        if (content.isEmpty()) {
            return content
        }
        try {
            val cipher = Cipher.getInstance("CBC_PKCS5_PADDING")
            val zeroIv = IvParameterSpec(encryptionIV.toByteArray())
            val keySpec = SecretKeySpec(key.toByteArray(), "AES")
            cipher.init(Cipher.DECRYPT_MODE, keySpec, zeroIv)
            val result =
                cipher.doFinal(Base64.decode(content, Base64.DEFAULT))
            return result
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return content
    }

    /**
     * 用到地方需要先引ffmpeg
     * 本库不会打包ffmpeg
     *   implementation 'com.arthenica:mobile-ffmpeg-full-gpl:4.4.LTS'
     */
    @JvmOverloads
    fun convertMp4(mergeTsFile: String, outputFilePath: String): Int {
        val command = arrayOf<String>("-i", mergeTsFile, "-c", "copy", outputFilePath)
        return FFmpeg.execute(command)
    }
}
