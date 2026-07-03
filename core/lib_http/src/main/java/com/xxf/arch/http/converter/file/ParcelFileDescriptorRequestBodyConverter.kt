package com.xxf.arch.http.converter.file

import android.os.ParcelFileDescriptor
import com.xxf.arch.http.body.impl.ParcelFileDescriptorRequestBody
import okhttp3.RequestBody
import retrofit2.Converter
import java.io.IOException

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.1
 * @Description 支持文件
 * @date createTime：2024/1/13
 */
internal class ParcelFileDescriptorRequestBodyConverter :
    Converter<ParcelFileDescriptor?, RequestBody> {
    @Throws(IOException::class)
    override fun convert(value: ParcelFileDescriptor?): RequestBody {
        return ParcelFileDescriptorRequestBody(value, null)
    }
}