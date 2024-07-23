package com.xxf.download.m3u8.demo.db

import com.nbxxf.kpower.database.objectbox.dao.BaseDao
import com.xxf.download.m3u8.demo.DownloadModel
import com.xxf.download.m3u8.demo.MyObjectBox
import com.xxf.objectbox.buildSingle
import io.objectbox.Box

class DownloadModelDao(
    override var box: Box<DownloadModel> = MyObjectBox.builder()
        .buildSingle("DownloadModel.objectbox", true)
        .boxFor(DownloadModel::class.java)
) : BaseDao<DownloadModel> {
}