package com.xxf.download.m3u8

import com.arthenica.mobileffmpeg.FFmpeg
import com.xxf.ktx.appendBytes
import com.xxf.ktx.mkParentDirs
import com.xxf.ktx.randomUUIDString32
import com.xxf.ktx.rename
import java.io.File
import java.io.IOException

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080
 * Date: 22/7/24 12:07 PM
 * Description: m3u8工具类
 */
object M3U8Utils {
    /**
     * 现在主流播放器都可以播放ts了
     * 合并ts文件
     * @param tsFileList 文件列表
     * @param toFile   合并之后的文件
     */
    @Throws(IOException::class)
    fun mergeTs(tsFileList: List<File>, toFile: String): File? {
        val targetFile = File(toFile)
        if (targetFile.exists()) {
            return targetFile
        }
        val tempFile = File(targetFile.parentFile, randomUUIDString32)
        try {
            targetFile.mkParentDirs()
            tempFile.appendBytes(tsFileList)
            //操作完全成功才命名过去
            tempFile.rename(targetFile.name)
            return targetFile
        } catch (e: Throwable) {
            e.printStackTrace()
            return null
        } finally {
            tempFile.delete()
        }
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
