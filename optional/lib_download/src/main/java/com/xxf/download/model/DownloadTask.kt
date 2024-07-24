package com.xxf.download.model

import com.liulishuo.okdownload.DownloadTask

val DownloadTask.tagModelId: Int get() = 1024
var DownloadTask.taskModel: Any?
    get() {
        return getTag(tagModelId)
    }
    set(value) {
        this.addTag(tagModelId, value)
    }