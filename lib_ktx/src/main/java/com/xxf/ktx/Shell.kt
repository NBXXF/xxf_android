@file:Suppress("unused")

package com.xxf.ktx


import androidx.annotation.WorkerThread
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader


/**
 *将日志记录到文件
 * 请在子线程调用
 */
@WorkerThread
fun recordLogFile(logFile: File) {
    executeCmd("logcat -c")
    executeCmd("logcat -f " + logFile.absolutePath)
}


fun executeCmd(command: String): String {
    val process = Runtime.getRuntime().exec(command)

    val resultReader = BufferedReader(InputStreamReader(process.inputStream))
    val resultBuilder = StringBuilder()
    var resultLine = resultReader.readLine()
    while (resultLine != null) {
        resultBuilder.append(resultLine)
        resultLine = resultReader.readLine()
    }

    val errorReader = BufferedReader(InputStreamReader(process.errorStream))
    val errorBuilder = StringBuilder()
    var errorLine = errorReader.readLine()
    while (errorLine != null) {
        errorBuilder.append(errorLine)
        errorLine = errorReader.readLine()
    }

    return "$resultBuilder\n$errorBuilder"
}
