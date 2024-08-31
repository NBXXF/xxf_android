package com.xxf.ktx

import android.content.Context
import android.util.Log
import androidx.annotation.WorkerThread
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


/**
 * 导出当前调用之前的lineCount行日志
 * @param logLevel 最大日志行数
 * @param logLevel 默认所有,为空也为所有 其值为 [android.util.Log.VERBOSE] [android.util.Log.DEBUG] ..
 */
fun <T : Context> T.logcatExport(
    lineCount: Int = 300,
    logLevel: Set<Int> = emptySet(),
    vararg tagLabel: String = arrayOf(this.packageName)
): String {
    return tryOrLogNull {
        var cmdString = "logcat -d -t $lineCount"
        val logLevelCommand = convertLogLevelCommand(logLevel)
        if (logLevelCommand.isNotEmpty()) {
            cmdString += " *:${logLevelCommand.joinToString(",")}"
        }
        if (tagLabel.isNotEmpty()) {
            /**
             * adb logcat -d | grep -E '(标签1|标签2|标签3)'
             * 这里 -E 参数是告诉 grep 使用扩展的正则表达式，以下括号 () 内的部分表示“或”关系，即匹配括号内的任意一个标签。
             */
            cmdString += " | grep -E '(${tagLabel.joinToString("|")})'}"
        }
        Runtime.getRuntime().execCmd(cmdString, false).successMsg
    }.orEmpty()
}

/**
 *将日志记录到文件,实时监控
 * 请在子线程调用
 * -- "-s"选项 : 设置输出日志的标签, 只显示该标签的日志
 * --"-f"选项 : 将日志输出到文件, 默认输出到标准输出流中, -f 参数执行不成功
 * --"-r"选项 : 按照每千字节输出日志, 需要 -f 参数, 不过这个命令没有执行成功
 * --"-n"选项 : 设置日志输出的最大数目, 需要 -r 参数, 这个执行 感觉 跟 adb logcat 效果一样
 * --"-v"选项 : 设置日志的输出格式, 注意只能设置一项
 * --"-c"选项 : 清空所有的日志缓存信息
 * --"-d"选项 : 将缓存的日志输出到屏幕上, 并且不会阻塞
 * --"-t"选项 : 输出最近的几行日志, 输出完退出, 不阻塞
 * --"-g"选项 : 查看日志缓冲区信息
 * --"-b"选项 : 加载一个日志缓冲区, 默认是 main, 下面详解
 * --"-B"选项 : 以二进制形式输出日志*
 *
 * 过滤项格式 : <tag>[:priority] , 标签:日志等级, 默认的日志过滤项是 " *:I "
 * -- V : Verbose (明细)
 * -- D : Debug (调试)
 * -- I : Info (信息)
 * -- W : Warn (警告)
 * -- E : Error (错误)
 * -- F: Fatal (严重错误)
 * -- S : Silent(Super all output) (最高的优先级, 可能不会记载东西)
 * 可多个 eg. *:E,W
 *
 * @param logFile 写入的文件
 * @param logLevel 默认所有,为空也为所有 其值为 [android.util.Log.VERBOSE] [android.util.Log.DEBUG] ..
 */
@WorkerThread
fun <T : Context> T.logcatRecord(
    logFile: File = this.filesDir
        .resolve("logcat")
        .resolve(
            Date().toString() + "${
                SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(Date())
            }.logcat"
        ), logLevel: Set<Int> = emptySet()
) {
    logFile.mkParentDirs()
    Runtime.getRuntime().execCmd("logcat -c", false)
    val logLevelCommand = convertLogLevelCommand(logLevel)
    var writeCmd = "logcat -f " + logFile.absolutePath
    if (logLevelCommand.isNotEmpty()) {
        writeCmd += " *:${logLevelCommand.joinToString(",")}"
    }
    Runtime.getRuntime().execCmd(writeCmd, false)
}

private fun convertLogLevelCommand(logLevel: Set<Int> = emptySet()): List<String> {
    val mapOf = mapOf(
        Log.VERBOSE to Log::VERBOSE.name.first().toString(),
        Log.DEBUG to Log::DEBUG.name.first().toString(),
        Log.INFO to Log::INFO.name.first().toString(),
        Log.WARN to Log::WARN.name.first().toString(),
        Log.ERROR to Log::ERROR.name.first().toString(),
        Log.ASSERT to Log::ASSERT.name.first().toString()
    )
    val logLevelCommand = logLevel.mapNotNull {
        mapOf[it]
    }
    return logLevelCommand
}
