package com.xxf.download.m3u8

import android.annotation.SuppressLint
import androidx.media3.common.util.UriUtil
import androidx.media3.exoplayer.hls.playlist.HlsMediaPlaylist
import androidx.media3.exoplayer.hls.playlist.HlsMultivariantPlaylist
import com.xxf.download.DownloadService
import com.xxf.download.IDownloadEntity
import com.xxf.download.component.DownloadInfo
import com.xxf.download.component.DownloadStatus
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
                    }
                } else if (playlist is HlsMediaPlaylist) {
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

}