

package com.xxf.download.m3u8

import android.annotation.SuppressLint
import android.net.Uri
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.hls.playlist.DefaultHlsPlaylistParserFactory
import androidx.media3.exoplayer.hls.playlist.HlsPlaylist
import androidx.media3.exoplayer.upstream.ParsingLoadable
import com.xxf.ktx.standard.doLazy
import java.io.File
import java.io.InputStream

@SuppressLint("UnsafeOptInUsageError")
object M3U8Parser {

    @JvmStatic
    var parser: ParsingLoadable.Parser<HlsPlaylist> by doLazy {
        DefaultHlsPlaylistParserFactory().createPlaylistParser()
    }

    /**
     * 解析m3u8清单
     * @param uri 原始地址
     * @param inputStream m3u8文件流
     */
    fun parse(uri: String, inputStream: InputStream): HlsPlaylist? {
        return kotlin.runCatching {
            parser.parse(Uri.parse(uri), inputStream)
        }.getOrNull()
    }


    /**
     * 解析m3u8清单
     * @param uri 原始地址
     * @param m3u8File m3u8文件
     */
    fun parse(uri: String, m3u8File: File): HlsPlaylist? {
        return kotlin.runCatching {
            parser.parse(Uri.parse(uri), m3u8File.inputStream())
        }.getOrNull()
    }

    /**
     * 解析m3u8清单
     * @param uri 原始地址
     * @param m3u8File m3u8文件
     */
    fun parse(uri: String, m3u8File: String): HlsPlaylist? {
        return kotlin.runCatching {
            parser.parse(Uri.parse(uri), File(m3u8File).inputStream())
        }.getOrNull()
    }
}