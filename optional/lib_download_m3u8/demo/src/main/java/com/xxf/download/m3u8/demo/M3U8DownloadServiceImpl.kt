package com.xxf.download.m3u8.demo

import com.nbxxf.kpower.database.query.BaseQueryBuilder
import com.nbxxf.kpower.database.repository.BaseRepository
import com.xxf.download.m3u8.demo.db.DownloadModelDbService
import com.xxf.download.m3u8.M3U8DownloadService

class M3U8DownloadServiceImpl : M3U8DownloadService<DownloadModel>() {
    private val dbService by lazy{
        DownloadModelDbService()
    }
    @Suppress("UNCHECKED_CAST")
    override fun getCacheService(): BaseRepository<Long, DownloadModel, BaseQueryBuilder<DownloadModel, *>> {
        return dbService as BaseRepository<Long, DownloadModel, BaseQueryBuilder<DownloadModel, *>>
    }
}