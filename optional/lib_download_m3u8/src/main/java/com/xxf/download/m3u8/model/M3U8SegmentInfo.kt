package com.xxf.download.m3u8.model

import java.io.File

/**
 * @Author: XGod  xuanyouwu@163.com https://github.com/NBXXF  https://blog.csdn.net/axuanqq
 * Date: 22/7/24 12:07 PM
 * Description: m3u8 ts解密合并信息
 */
class M3U8SegmentInfo(
    /**
     * ts文件
     */
    val tsFile: File,
    /**
     * 解密key
     */
    val keyFile: File?,
    /**
     * 解密偏移量
     */
    val encryptionIV: String?
)