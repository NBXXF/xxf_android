package com.xxf.log.parser.impl

import android.os.Message
import com.xxf.log.parser.LINE_SEPARATOR
import com.xxf.log.parser.Parser

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.1
 * @Description 解析器
 * @date createTime：2018/9/7
 */
internal class MessageParser : Parser<Message> {
    override fun parseClassType(): Class<Message> {
        return Message::class.java
    }

    override fun parseString(message: Message): String? {
        return message.javaClass.name + " [" + LINE_SEPARATOR + String.format(
            "%s = %s",
            "what",
            message.what
        ) + LINE_SEPARATOR + String.format(
            "%s = %s",
            "when",
            message.getWhen()
        ) + LINE_SEPARATOR + String.format(
            "%s = %s",
            "arg1",
            message.arg1
        ) + LINE_SEPARATOR + String.format(
            "%s = %s",
            "arg2",
            message.arg2
        ) + LINE_SEPARATOR + String.format(
            "%s = %s",
            "data",
            BundleParser().parseString(message.data)
        ) + LINE_SEPARATOR +
                java.lang.String.format(
                    "%s = %s",
                    "obj",
                    "${message.obj}"
                ) + LINE_SEPARATOR +
                "]"
    }
}