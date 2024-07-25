package com.xxf.download.model

import com.nbxxf.kpower.database.entity.BaseTable

/**
 * @Author: XGod  xuanyouwu@163.com https://github.com/NBXXF  https://blog.csdn.net/axuanqq
 * Date: 1/8/19 12:07 PM
 * Description: 下载约束模型
 */
interface IDownloadEntity : BaseTable<Long> {
    /**
     * 下载状态
     */
    var downloadStatus: Long


    /**
     * 下载总文件大小
     */
    var downloadTotalLength: Long


    /**
     * 下载网络地址
     */
    var downloadUrl: String


    /**
     * 下载本地地址
     */
    var downloadPath: String


    /**
     * 下载错误次数记录,业务不要主动塞！！
     */
    var downloadErrorTimes: Long
}