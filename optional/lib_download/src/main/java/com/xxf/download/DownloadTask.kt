package com.xxf.download

import com.liulishuo.okdownload.DownloadTask

val DownloadTask.tagModelId: Int get() = 1024
var DownloadTask.taskModel: Any?
    get() {
        return getTag(tagModelId) as? DownloadTask
    }
    set(value) {
        this.addTag(tagModelId, value)
    }