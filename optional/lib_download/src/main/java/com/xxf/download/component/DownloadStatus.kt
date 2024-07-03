package com.xxf.download.component

enum class DownloadStatus(val value: Long) {
    START(1L),
    CONNECT(2L),
    PROGRESS(3L),
    COMPLETED(4L),
    CANCEL(5L),
    ERROR(6L)
}