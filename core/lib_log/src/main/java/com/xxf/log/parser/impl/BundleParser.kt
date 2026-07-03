package com.xxf.log.parser.impl

import android.os.Bundle
import com.xxf.log.parser.LINE_SEPARATOR
import com.xxf.log.parser.Parser

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.1
 * @Description 解析器
 * @date createTime：2018/9/7
 */
internal class BundleParser : Parser<Bundle> {
    override fun parseClassType(): Class<Bundle> {
        return Bundle::class.java
    }

    override fun parseString(bundle: Bundle): String? {
        val builder = StringBuilder(bundle.javaClass.name)
        builder.append(" [")
        builder.append(LINE_SEPARATOR)
        for (key in bundle.keySet()) {
            builder.append(
                java.lang.String.format(
                    "'%s' => %s $LINE_SEPARATOR",
                    key, "${bundle[key]}"
                )
            )
        }
        builder.append("]")
        return builder.toString()
    }
}