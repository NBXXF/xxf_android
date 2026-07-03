package com.xxf.log
/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.1
 * @Description  日志
 * @date createTime：2018/9/7
 */
interface Logger {

    fun logV(tag:String?=null,log: () -> Any);

    fun logI(tag:String?=null,log: () -> Any);

    fun logD(tag:String?=null,log: () -> Any);

    fun logE(tag:String?=null,log: () -> Any)

    fun logW(tag:String?=null,log: () -> Any);

    fun logJson(tag:String?=null,log: () -> Any)
}