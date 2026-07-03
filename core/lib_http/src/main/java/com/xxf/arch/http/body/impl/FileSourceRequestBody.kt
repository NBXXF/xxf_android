package com.xxf.arch.http.body.impl

import okhttp3.MediaType
import okhttp3.RequestBody

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.1
 * @Description 支持文件
 * @date createTime：2024/1/13
 */
abstract class FileSourceRequestBody<T>(
    open val dataSource: T,
    open val contentType: MediaType? = null
):RequestBody() {

    override fun contentType(): MediaType?=contentType

}