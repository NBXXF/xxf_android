package com.xxf.log

import com.xxf.json.GsonFactory
import com.xxf.log.impl.AndroidLogger
import com.xxf.log.parser.Parser
import com.xxf.log.parser.impl.BundleParser
import com.xxf.log.parser.impl.CollectionParser
import com.xxf.log.parser.impl.IntentParser
import com.xxf.log.parser.impl.JsonParser
import com.xxf.log.parser.impl.MapParser
import com.xxf.log.parser.impl.MessageParser
import com.xxf.log.parser.impl.ThrowableParser

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
    data class Config(var isDebug: Boolean=true,
                      var logger:Logger=AndroidLogger(),
                      var parsers:List<Parser<*>> = listOf(
                          BundleParser(),
                          CollectionParser(),
                          IntentParser(),
                          JsonParser(),
                          MapParser(),
                          MessageParser(),
                          ThrowableParser()
    ))

    private fun Config.dispatchParser(log: () -> Any):Any{
        val dataSource:Any = log.invoke()
        try {
            return ParseUtils.dispatch(this.parsers,dataSource)
        }catch (e:Throwable) {
            e.printStackTrace()
        }
        return "$dataSource"
    }
    /**
     * 执行配置
     */
    val config:Config=Config()
    override fun logV(tag: String?, log: () -> Any) {
        if(!config.isDebug){
            return
        }
        config.logger.logV(tag){
            config.dispatchParser(log)
        }
    }

    override fun logI(tag: String?, log: () -> Any) {
        if(!config.isDebug){
            return
        }
        config.logger.logI(tag){
            config.dispatchParser(log)
        }
    }

    override fun logD(tag: String?, log: () -> Any) {
        if(!config.isDebug){
            return
        }
        config.logger.logD(tag){
            config.dispatchParser(log)
        }
    }

    override fun logE(tag: String?, log: () -> Any) {
        if(!config.isDebug){
            return
        }
        config.logger.logE(tag){
            config.dispatchParser(log)
        }
    }

    override fun logW(tag: String?, log: () -> Any) {
        if(!config.isDebug){
            return
        }
        config.logger.logW(tag){
            config.dispatchParser(log)
        }
    }

    override fun logJson(tag: String?, log: () -> Any) {
        if(!config.isDebug){
            return
        }
        config.logger.logJson(tag){
            GsonFactory.createGson(false,false).toJson(log())
        }
    }

}