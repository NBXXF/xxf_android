package com.xxf.download.model

import com.xxf.hash.toCityHash64
import java.util.Date

/**
 * @Author: XGod  xuanyouwu@163.com https://github.com/NBXXF  https://blog.csdn.net/axuanqq
 * Date: 1/8/19 12:07 PM
 * Description: 简化下载约束模型
 */
open class SimpleDownloadTask(
    override var downloadUrl: String,
    var headers: Map<String, List<String>> = emptyMap(),
    override var downloadPath: String,
    override var downloadStatus: Long = -1,
    override var downloadTotalLength: Long = -1,
    override var downloadErrorTimes: Long = 0,
    override var createAt: Date = Date(),
    override var updateAt: Date = Date(),
) : IDownloadEntity {
    override fun id(): Long {
        return downloadUrl.toCityHash64()
    }
}