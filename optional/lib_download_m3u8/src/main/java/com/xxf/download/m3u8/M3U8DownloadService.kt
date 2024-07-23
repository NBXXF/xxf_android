package com.xxf.download.m3u8

import android.annotation.SuppressLint
import androidx.media3.common.util.UriUtil
import androidx.media3.exoplayer.hls.playlist.HlsMediaPlaylist
import androidx.media3.exoplayer.hls.playlist.HlsMultivariantPlaylist
import com.xxf.download.DownloadService
import com.xxf.download.IDownloadEntity
import com.xxf.download.component.DownloadInfo
import com.xxf.download.component.DownloadStatus
import com.xxf.ktx.mkParentDirs
import com.xxf.ktx.randomUUIDString32
import com.xxf.log.logD

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080
 * Date: 22/7/24 12:07 PM
 * Description: m3u8下载
 */
abstract class M3U8DownloadService<T : IDownloadEntity> : DownloadService<T>() {

    @SuppressLint("UnsafeOptInUsageError")
    override fun updateDownload(task: T?, info: DownloadInfo) {
        super.updateDownload(task, info)
        if (info.status == DownloadStatus.COMPLETED) {
            val taskModel = requireNotNull(task)
            val downloadUrl = taskModel.downloadUrl
            if (!downloadUrl.endsWith(".ts")) {
                //可能是m3u8
                val playlist =
                    M3U8Parser.parse(downloadUrl, taskModel.getDownloadPath())
                if (playlist is HlsMultivariantPlaylist) {
                    if (playlist.variants.isNotEmpty()) {
                        @Suppress("UNCHECKED_CAST") val clone = task.clone() as T
                        val baseUri: String = playlist.baseUri
                        val segmentUri =
                            UriUtil.resolve(baseUri, playlist.variants.first().url.toString())
                        clone.downloadUrl = segmentUri
                        addTask(listOf(clone))
                        test(segmentUri)
                    }
                } else if (playlist is HlsMediaPlaylist) {
                    test(playlist.baseUri)
                    addTask(playlist.segments.map {
                        @Suppress("UNCHECKED_CAST") val clone = task.clone() as T
                        val baseUri: String = playlist.baseUri
                        val segmentUri = UriUtil.resolve(baseUri, it.url)
                        clone.downloadUrl = segmentUri
                        clone
                    })
                }
            }
        }
    }

    private fun test(m3u8url: String) {
        try {
            val dir = application.cacheDir.resolve("temp")
            dir.mkParentDirs()
           val result= M3U8Utils.convertMp4(m3u8url, dir.resolve("$randomUUIDString32.mp4").absolutePath)
            logD { "============>result:$result" }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

}