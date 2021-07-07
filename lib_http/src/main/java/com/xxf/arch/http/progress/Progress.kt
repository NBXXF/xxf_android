package com.xxf.arch.http.progress

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.1
 * @Description
 * @date createTime：2018/9/7
 */
class Progress {
    var progress //当前进度 0-100
            = 0
    var currentSize //当前已完成的字节大小
            : Long = 0
    var totalSize //总字节大小
            : Long = 0

    constructor() {}
    constructor(progress: Int, currentSize: Long, totalSize: Long) {
        this.progress = progress
        this.currentSize = currentSize
        this.totalSize = totalSize
    }

    override fun toString(): String {
        return "Progress{" +
                "progress=" + progress +
                ", currentSize=" + currentSize +
                ", totalSize=" + totalSize +
                '}'
    }
}