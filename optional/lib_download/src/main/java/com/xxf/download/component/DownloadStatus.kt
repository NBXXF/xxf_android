package com.xxf.download.component

enum class DownloadStatus(val value: Long) {
    CONNECT(1L),
    START(2L),
    PROGRESS(3L),
    COMPLETED(4L),
    CANCEL(5L),
    ERROR(6L)
}