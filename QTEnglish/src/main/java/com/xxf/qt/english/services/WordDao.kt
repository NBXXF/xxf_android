package com.xxf.qt.english.services

import com.nbxxf.kpower.database.objectbox.dao.BaseDao
import com.xxf.objectbox.buildSingle
import com.xxf.qt.english.models.MyObjectBox
import com.xxf.qt.english.models.WordInfo
import io.objectbox.Box

class WordDao:BaseDao<WordInfo> {
    private val mBox:Box<WordInfo> by lazy {
         MyObjectBox.builder()
            .buildSingle(WordInfo::class.simpleName!!,allowMainThreadOperation = true)
            .boxFor(WordInfo::class.java)
    }

    override var box: Box<WordInfo>
        get() = mBox
        set(value) {}
}