package com.xxf.download.m3u8.demo.db

import com.nbxxf.kpower.database.objectbox.repository.BaseRepositoryImpl
import com.xxf.download.m3u8.demo.DownloadModel

class DownloadModelDbService: BaseRepositoryImpl<DownloadModel, DownloadModelDao>() {
    init {
        this.dao= DownloadModelDao()
    }
}