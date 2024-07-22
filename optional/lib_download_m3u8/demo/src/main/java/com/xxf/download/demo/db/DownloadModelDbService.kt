package com.xxf.download.demo.db

import com.nbxxf.kpower.database.objectbox.service.BaseServiceImpl
import com.xxf.download.demo.DownloadModel

class DownloadModelDbService: BaseServiceImpl<DownloadModel, DownloadModelDao>() {
    init {
        this.dao=DownloadModelDao()
    }
}