package com.xxf.download.m3u8.demo

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.UriUtil
import androidx.media3.exoplayer.hls.playlist.DefaultHlsPlaylistParserFactory
import androidx.media3.exoplayer.hls.playlist.HlsMediaPlaylist
import androidx.media3.exoplayer.hls.playlist.HlsMultivariantPlaylist
import com.google.gson.GsonBuilder
import com.xxf.download.DownloadService.Companion.startService

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        parseM3u8MasterPlayList()
        parseM3u8PlayList()

        M3U8DownloadServiceImpl::class.java.startService(this, listOf(DownloadModel().apply {
            downloadUrl = "https://sf1-cdn-tos.huoshanstatic.com/obj/media-fe/xgplayer_doc_video/hls/xgplayer-demo.m3u8"
            downloadUrl = "https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8"
        }))
    }

    @OptIn(UnstableApi::class)
    private fun parseM3u8MasterPlayList() {
        val playlistParser = DefaultHlsPlaylistParserFactory().createPlaylistParser()
        val uri = Uri.parse("https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8")
        val openRawResource = resources.openRawResource(R.raw.master_play_list)
        val parse = playlistParser.parse(uri, openRawResource)
        (parse as? HlsMultivariantPlaylist)?.let { it ->
            val variants = it.variants.joinToString("\n") {
                "${it.url}:${it.format.toJson()}"
            }
            println("=======>variants:\n$variants")
        }
        println("=================>parseM3u8MasterPlayList:$parse")
    }

    @OptIn(UnstableApi::class)
    private fun parseM3u8PlayList() {
        val playlistParser = DefaultHlsPlaylistParserFactory().createPlaylistParser()
        val uri =
            Uri.parse("https://playertest.longtailvideo.com/adaptive/bipbop/gear4/prog_index.m3u8")
        val openRawResource = resources.openRawResource(R.raw.play_list)
        val parse = playlistParser.parse(uri, openRawResource)
        (parse as? HlsMediaPlaylist)?.let {
            val firstOrNull = it.segments.first()
            val segmentUri = UriUtil.resolveToUri(parse.baseUri, firstOrNull.url).toString()
            println(
                "=================>parseM3u8PlayList:\n${
                    it.toJson()
                }"
            )
        }

    }

    private fun <T : Any> T.toJson(): String? {
        return GsonBuilder()
            .setPrettyPrinting()
            .create()
            .toJson(this)
    }


}