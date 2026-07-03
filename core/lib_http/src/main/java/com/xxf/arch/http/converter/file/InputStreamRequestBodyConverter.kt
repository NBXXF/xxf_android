package com.xxf.arch.http.converter.file

import com.xxf.arch.http.body.impl.InputStreamRequestBody
import okhttp3.RequestBody
import retrofit2.Converter
import java.io.IOException
import java.io.InputStream

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.1
 * @Description 支持文件
 * @date createTime：2024/1/13
 */
internal class InputStreamRequestBodyConverter : Converter<InputStream?, RequestBody> {
    @Throws(IOException::class)
    override fun convert(value: InputStream?): RequestBody {
        return InputStreamRequestBody(value!!, null)
    }
}