package com.xxf.download.m3u8.model

import com.xxf.download.IDownloadEntity

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080
 * Date: 22/7/24 12:07 PM
 * Description: m3u8下载模型
 */
interface M3u8DownloadEntity : IDownloadEntity {
    /**
     * m3u8 清单,业务不用主动塞
     */
    var hlsMediaPlaylistUrl: String?
}