package com.xxf.download.demo.db

import com.nbxxf.kpower.database.objectbox.dao.BaseDao
import com.xxf.download.demo.DownloadModel
import com.xxf.download.demo.MyObjectBox
import com.xxf.objectbox.buildSingle
import io.objectbox.Box

class DownloadModelDao(
    override var box: Box<DownloadModel> = MyObjectBox.builder()
        .buildSingle("DownloadModel.objectbox", true)
        .boxFor(DownloadModel::class.java)
) : BaseDao<DownloadModel> {
}