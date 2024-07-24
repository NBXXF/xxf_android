package com.xxf.download.m3u8

import android.annotation.SuppressLint
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.UriUtil
import androidx.media3.exoplayer.hls.playlist.HlsMediaPlaylist
import androidx.media3.exoplayer.hls.playlist.HlsMultivariantPlaylist
import com.xxf.download.DownloadService
import com.xxf.download.component.DownloadInfo
import com.xxf.download.component.DownloadStatus
import com.xxf.download.m3u8.model.M3U8SegmentInfo
import com.xxf.download.m3u8.model.M3u8DownloadEntity
import com.xxf.hash.toCityHash64
import java.io.File

/**
 * @Author: XGod  xuanyouwu@163.com https://github.com/NBXXF  https://blog.csdn.net/axuanqq
 * Date: 22/7/24 12:07 PM
 * Description: m3u8下载
 * m3u8 格式参考 https://blog.csdn.net/weixin_39399492/article/details/131687865
 */
abstract class M3U8DownloadService<T : M3u8DownloadEntity> : DownloadService<T>() {
    companion object {
        /**
         * 合并后的总ts 名称
         * _merged_playlist.ts
         * 固定名字不能变
         */
        fun String.getMergedPlayListTsName(): String {
            return "${this.toCityHash64()}_merged_playlist.ts"
        }
    }

    @SuppressLint("UnsafeOptInUsageError")
    override fun updateDownload(task: T?, info: DownloadInfo) {
        super.updateDownload(task, info)
        if (info.status == DownloadStatus.COMPLETED) {
            val taskModel = requireNotNull(task)
            val downloadUrl = taskModel.downloadUrl
            if (!downloadUrl.endsWith(".ts")) {
                //可能是m3u8
                val playlist =
                    M3U8Parser.parse(downloadUrl, taskModel.downloadPath)
                if (playlist is HlsMultivariantPlaylist) {
                    if (playlist.variants.isNotEmpty()) {
                        val baseUri: String = playlist.baseUri
                        val segmentUri =
                            UriUtil.resolve(baseUri, playlist.variants.first().url.toString())
                        val cloneWithUrl = cloneFromOriginModel(task, segmentUri, baseUri)
                        addTask(listOf(cloneWithUrl))
                    }
                } else if (playlist is HlsMediaPlaylist) {
                    addTask(playlist.segments.flatMap {
                        buildList<T> {
                            val baseUri: String = playlist.baseUri
                            if (!it.fullSegmentEncryptionKeyUri.isNullOrBlank()) {
                                val keyUri = UriUtil.resolve(
                                    baseUri,
                                    it.fullSegmentEncryptionKeyUri
                                )
                                val keyTask =
                                    cloneFromOriginModel(task, keyUri, baseUri)
                                add(keyTask)
                            }
                            val segmentUri = UriUtil.resolve(baseUri, it.url)
                            val tsTask =
                                cloneFromOriginModel(task, segmentUri, baseUri)
                            add(tsTask)
                        }
                    })
                }
            } else {
                //ts 下载完成 判断是否都下载完了
                val playListModel = findPlayListModel(taskModel.hlsMediaPlaylistUrl)
                val playlist =
                    M3U8Parser.parse(downloadUrl, playListModel?.downloadPath.orEmpty())
                if (playlist is HlsMediaPlaylist) {
                    val tsFileList = getM3U8SegmentInfo(task, playlist)
                    if (tsFileList.all { it.tsFile.exists() }) {
                        val hlsMediaPlaylistUrl = taskModel.hlsMediaPlaylistUrl
                        if (!hlsMediaPlaylistUrl.isNullOrBlank()) {
                            val findRootModel = findRootModel(hlsMediaPlaylistUrl)
                            if (findRootModel != null) {
                                val mergePlaylistFile =
                                    requireNotNull(File(findRootModel.downloadPath).parentFile).resolve(
                                        findRootModel.downloadUrl.getMergedPlayListTsName()
                                    )
                                M3U8Utils.mergeTs(tsFileList, mergePlaylistFile)
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 获取片段和秘钥
     */
    @UnstableApi
    private fun getM3U8SegmentInfo(task: T, playlist: HlsMediaPlaylist): List<M3U8SegmentInfo> {
        return playlist.segments.map {
            val baseUri: String = playlist.baseUri
            val segmentUri = UriUtil.resolve(baseUri, it.url)
            val tsFile =
                File(cloneFromOriginModel(task, segmentUri, baseUri).downloadPath)

            var keyFile: File? = null
            if (!it.fullSegmentEncryptionKeyUri.isNullOrBlank()) {
                val keyUri = UriUtil.resolve(
                    baseUri,
                    it.fullSegmentEncryptionKeyUri
                )
                keyFile = File(
                    cloneFromOriginModel(
                        task,
                        keyUri,
                        baseUri
                    ).downloadPath
                ).takeIf { it -> it.exists() }
            }
            M3U8SegmentInfo(tsFile, keyFile, it.encryptionIV)
        }
    }

    /**
     * 找到下载清单
     */
    private fun findPlayListModel(hlsMediaPlaylistUrl: String?): T? {
        return if (!hlsMediaPlaylistUrl.isNullOrBlank()) {
            getCacheService().selectFirst { it ->
                it.equal(M3u8DownloadEntity::downloadUrl, hlsMediaPlaylistUrl)
                it
            }
        } else {
            null
        }
    }

    /**
     * 找到最顶层的m3u8下载项
     */
    private fun findRootModel(hlsMediaPlaylistUrl: String): T? {
        var rootModel: T? = null
        //最多三层 避免死循环
        repeat(3) {
            val url = rootModel?.hlsMediaPlaylistUrl.takeIf {
                !it.isNullOrBlank()
            } ?: hlsMediaPlaylistUrl
            val parent = getCacheService().selectFirst { it ->
                it.equal(M3u8DownloadEntity::downloadUrl, url)
                it
            }
            if (parent == null) {
                return rootModel
            }
            rootModel = parent
        }
        return rootModel
    }

    /**
     * @param from
     * @param downloadUrl 新的下载地址
     * @param hlsMediaPlaylistUrl 主列表或者播放清单
     */
    @Suppress("UNCHECKED_CAST")
    protected open fun cloneFromOriginModel(
        from: T,
        downloadUrl: String,
        hlsMediaPlaylistUrl: String
    ): T {
        val clone = from.clone() as T
        clone.downloadUrl = downloadUrl
        clone.hlsMediaPlaylistUrl = hlsMediaPlaylistUrl
        return clone
    }
}