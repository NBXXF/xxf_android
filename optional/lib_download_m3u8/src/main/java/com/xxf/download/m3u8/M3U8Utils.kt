package com.xxf.download.m3u8

import com.arthenica.mobileffmpeg.FFmpeg
import com.xxf.ktx.appendBytes
import com.xxf.ktx.appendFrom
import com.xxf.ktx.application
import com.xxf.ktx.mkParentDirs
import com.xxf.ktx.randomUUIDString32
import com.xxf.ktx.rename
import com.xxf.log.logD
import java.io.File
import java.io.FilenameFilter
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


    fun test() {
        try {
            val tsFile =
                application.cacheDir.listFiles(FilenameFilter { _, name -> name.endsWith(".ts") })
                    .toList()

            val dir = application.cacheDir.resolve("temp")
            dir.mkParentDirs()

            val mergeTs = mergeTs(tsFile, dir.resolve("total.ts").absolutePath)

            val result = M3U8Utils.convertMp4(
                requireNotNull(mergeTs).absolutePath,
                dir.resolve("$randomUUIDString32.mp4").absolutePath
            )
            logD { "============>result:$result" }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    fun convertMp4(m3u8Url: String, outputFilePath: String): Int {
        // 构建FFmpeg命令
        val command = arrayOf<String>("-i", m3u8Url, "-c", "copy", outputFilePath)
        return FFmpeg.execute(command)
    }
}
