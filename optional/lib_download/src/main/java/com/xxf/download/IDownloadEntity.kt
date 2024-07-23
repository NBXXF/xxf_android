package com.xxf.download

import com.nbxxf.kpower.database.entity.BaseTable

interface IDownloadEntity : BaseTable<Long> {
    /**
     * 下载状态
     */
    var downloadStatus: Long

    /**
     * 下载总文件大小
     */
    var downloadTotalLength: Long


    /**
     * 下载网络地址
     */
    var downloadUrl: String


    /**
     * 下载本地地址
     */
    var downloadPath:String
}