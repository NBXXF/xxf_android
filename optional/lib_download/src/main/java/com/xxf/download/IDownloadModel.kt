package com.xxf.download

import java.io.Serializable

interface IDownloadModel: Serializable {
    /**
     * 下载网络地址
     */
    fun getDownloadUrl(): String

    /**
     * 下载本地地址
     */
    fun getDownloadPath(): String
}