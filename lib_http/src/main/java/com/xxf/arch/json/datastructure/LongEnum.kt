package com.xxf.arch.json.datastructure

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2021/9/26
 * Description ://是数字的枚举
 *
 *
 * 配合      @SerializedName("1")  自动序列化成整数[框架默认序列化成字符串]
 */
interface LongEnum{
    val value:Long
}