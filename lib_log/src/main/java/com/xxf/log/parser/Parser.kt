package com.xxf.log.parser

internal val LINE_SEPARATOR=System.lineSeparator()
/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.1
 * @Description 解析器
 * @date createTime：2018/9/7
 */
interface Parser<T>{
    fun parseClassType(): Class<T>
    fun parseString(t: T): String?
}