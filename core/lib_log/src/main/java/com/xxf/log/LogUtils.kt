package com.xxf.log

import android.util.Log
import com.nbxxf.kpower.json.Json
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
object LogUtils : Logger {

    /**
     * 配置
     */
    data class Config(
        @Deprecated("过时了", replaceWith = ReplaceWith("logInterceptor"))
        var isDebug: Boolean = true,
        var logInterceptor: LogInterceptor? = null,
        var logger: Logger = AndroidLogger(),
        var parsers: List<Parser<*>> = listOf(
            BundleParser(),
            CollectionParser(),
            IntentParser(),
            JsonParser(),
            MapParser(),
            MessageParser(),
            ThrowableParser()
        )
    )

    private fun Config.dispatchParser(log: () -> Any): Any {
        val dataSource: Any = log.invoke()
        try {
            return ParseUtils.dispatch(this.parsers, dataSource)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return "$dataSource"
    }

    /**
     * 执行配置
     */
    val config: Config = Config()

    private val StackTraceElement.isIgnorable: Boolean
        get() = isNativeMethod || className == Thread::class.java.name || className == Logger::class.java.name


    private fun String.limitLength(length: Int): String =
        if (this.length <= length) this else substring(0, length)

    private val StackTraceElement.simpleClassName
        get() = className.split(".").run {
            if (isNotEmpty()) last().limitLength(23) else null
        }
    private inline val TAG: String
        get() = Thread.currentThread().stackTrace
            .find { !it.isIgnorable }?.simpleClassName.orEmpty()

    /**
     * 兼容
     */
    private fun Config.isDebugCompat(level: Int): Boolean {
        return if (logInterceptor != null) {
            !requireNotNull(logInterceptor).onIntercept(level)
        } else {
            isDebug
        }
    }

    override fun logV(tag: String?, log: () -> Any) {
        if (!config.isDebugCompat(Log.VERBOSE)) {
            return
        }
        config.logger.logV(tag ?: TAG) {
            config.dispatchParser(log)
        }
    }

    override fun logI(tag: String?, log: () -> Any) {
        if (!config.isDebugCompat(Log.INFO)) {
            return
        }
        config.logger.logI(tag ?: TAG) {
            config.dispatchParser(log)
        }
    }

    override fun logD(tag: String?, log: () -> Any) {
        if (!config.isDebugCompat(Log.DEBUG)) {
            return
        }
        config.logger.logD(tag ?: TAG) {
            config.dispatchParser(log)
        }
    }

    override fun logE(tag: String?, log: () -> Any) {
        if (!config.isDebugCompat(Log.ERROR)) {
            return
        }
        config.logger.logE(tag ?: TAG) {
            config.dispatchParser(log)
        }
    }

    override fun logW(tag: String?, log: () -> Any) {
        if (!config.isDebugCompat(Log.WARN)) {
            return
        }
        config.logger.logW(tag ?: TAG) {
            config.dispatchParser(log)
        }
    }

    override fun logJson(tag: String?, log: () -> Any) {
        if (!config.isDebugCompat(Log.INFO)) {
            return
        }
        config.logger.logJson(tag ?: TAG) {
            return@logJson try {
                Json.toJson(log())
            } catch (e: Throwable) {
                log()
            }
        }
    }

}