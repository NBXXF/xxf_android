package com.xxf.download

interface DownloadModel {
    /**
     * 下载网络地址
     */
    fun getDownloadUrl(): String

    /**
     * 下载本地地址
     */
    fun getDownloadPath(): String
}