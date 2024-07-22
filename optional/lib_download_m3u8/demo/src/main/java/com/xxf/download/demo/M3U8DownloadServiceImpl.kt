package com.xxf.download.demo

import com.nbxxf.kpower.database.query.BaseQueryBuilder
import com.nbxxf.kpower.database.service.BaseService
import com.xxf.download.demo.db.DownloadModelDbService
import com.xxf.download.m3u8.M3U8DownloadService

class M3U8DownloadServiceImpl : M3U8DownloadService<DownloadModel>() {
    private val dbService by lazy{
        DownloadModelDbService()
    }
    @Suppress("UNCHECKED_CAST")
    override fun getCacheService(): BaseService<Long, DownloadModel, BaseQueryBuilder<DownloadModel, *>> {
        return dbService as BaseService<Long, DownloadModel, BaseQueryBuilder<DownloadModel, *>>
    }
}