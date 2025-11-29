package com.xxf.qt.english.services

import com.nbxxf.kpower.database.objectbox.service.BaseServiceImpl
import com.xxf.qt.english.models.WordInfo

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：7/15/21
 * Description :
 */
object WordDbService: BaseServiceImpl<WordInfo, WordDao>() {
    init {
        dao= WordDao()
    }
}