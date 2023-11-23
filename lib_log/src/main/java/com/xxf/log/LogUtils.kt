package com.xxf.log

import com.xxf.log.impl.AndroidLogger
/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.1
 * @Description  日志
 * @date createTime：2018/9/7
 */
object LogUtils:Logger{
    /**
     * 配置
     */
    data class Config(val isDebug: Boolean=true,val logger:Logger=AndroidLogger())
    /**
     * 执行配置
     */
    var config:Config=Config()
    override fun logV(tag: String?, log: () -> Any) {
        if(!config.isDebug){
            return
        }
        config.logger.logV(tag,log)
    }

    override fun logI(tag: String?, log: () -> Any) {
        if(!config.isDebug){
            return
        }
        config.logger.logI(tag,log)
    }

    override fun logD(tag: String?, log: () -> Any) {
        if(!config.isDebug){
            return
        }
        config.logger.logD(tag,log)
    }

    override fun logE(tag: String?, log: () -> Any) {
        if(!config.isDebug){
            return
        }
        config.logger.logE(tag,log)
    }

    override fun logW(tag: String?, log: () -> Any) {
        if(!config.isDebug){
            return
        }
        config.logger.logW(tag,log)
    }

    override fun logJson(tag: String?, log: () -> String) {
        if(!config.isDebug){
            return
        }
        config.logger.logJson(tag,log)
    }

}