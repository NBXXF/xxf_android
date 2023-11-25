package com.xxf.log.parser.impl

import android.util.Log
import com.xxf.log.parser.Parser

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.1
 * @Description 解析器
 * @date createTime：2018/9/7
 */
internal class ThrowableParser : Parser<Throwable> {
    override fun parseClassType(): Class<Throwable> {
        return Throwable::class.java
    }

    override fun parseString(throwable: Throwable): String? {
        return Log.getStackTraceString(throwable)
    }
}