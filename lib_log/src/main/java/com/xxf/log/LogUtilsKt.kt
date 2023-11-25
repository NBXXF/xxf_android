package com.xxf.log
/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.1
 * @Description  日志 快捷扩展
 * @date createTime：2018/9/7
 */

inline fun logDefaultTag(): String {
    val stackTraceElement = Thread.currentThread().stackTrace[2]
    return stackTraceElement.fileName
}

inline fun logV(tag:String?=null, noinline log: () -> Any){
    if(!LogUtils.config.isDebug){
        return
    }
    LogUtils.logV(tag?:logDefaultTag(),log)
}

inline fun logI(tag:String?=null,noinline log: () -> Any){
    if(!LogUtils.config.isDebug) {
        return
    }
    LogUtils.logV(tag?:logDefaultTag(),log)
}

inline fun logD(tag:String?=null,noinline log: () -> Any){
    if(!LogUtils.config.isDebug) {
        return
    }
    LogUtils.logV(tag?:logDefaultTag(),log)
}

inline fun logE(tag:String?=null,noinline log: () -> Any){
    if(!LogUtils.config.isDebug){
        return
    }
    LogUtils.logV(tag?:logDefaultTag(),log)
}

inline fun logW(tag:String?=null,noinline log: () -> Any){
    if(!LogUtils.config.isDebug){
        return
    }
    LogUtils.logV(tag?:logDefaultTag(),log)
}

inline fun logJson(tag:String?=null,noinline log: () -> Any){
    if(!LogUtils.config.isDebug){
        return
    }
    LogUtils.logJson (tag?:logDefaultTag(),log)
}