package com.xxf.download.ob

import com.xxf.download.IDownloadModel

interface ObDownloadModel : IDownloadModel {
    /**
     * 唯一主键
     * 子类覆盖 添加注解 @io.objectbox.annotation.Id
     */
    var _id: Long

    /**
     * 下载加入队列的时间
     */
    var _insertTime: Long
}
