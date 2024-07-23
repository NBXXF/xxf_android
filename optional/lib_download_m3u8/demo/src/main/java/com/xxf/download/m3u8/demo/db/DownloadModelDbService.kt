package com.xxf.download.m3u8.demo.db

import com.nbxxf.kpower.database.objectbox.service.BaseServiceImpl
import com.xxf.download.m3u8.demo.DownloadModel

class DownloadModelDbService: BaseServiceImpl<DownloadModel, DownloadModelDao>() {
    init {
        this.dao= DownloadModelDao()
    }
}