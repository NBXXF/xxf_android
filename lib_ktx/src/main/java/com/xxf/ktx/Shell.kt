@file:Suppress("unused")

package com.xxf.ktx


import android.os.Build
import androidx.annotation.RequiresApi
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit

/**
 * @param command
 * @param waitFor 是否阻塞线程 直到进程结束,一般监听类需要 实时记录文件需要 等待结果需要
 */
@Throws(IOException::class)
fun Runtime.exec(command: String, waitFor: Boolean): Process {
    val process = Runtime.getRuntime().exec(command)
    return process.also {
        if (waitFor) {
            it.waitFor()
        }
    }
}

/**
 * @param command
 * @param waitForTimeout 阻塞线程时间
 * @param unit 阻塞线程时间单位
 */
@RequiresApi(Build.VERSION_CODES.O)
@Throws(IOException::class)
fun Runtime.exec(command: String, waitForTimeout: Long, unit: TimeUnit): Process {
    val process = Runtime.getRuntime().exec(command)
    return process.also {
        it.waitFor(waitForTimeout, unit)
    }
}

fun executeCmd(command: String): String {
    val process = Runtime.getRuntime().exec(command)

    val resultReader = BufferedReader(InputStreamReader(process.inputStream))
    val resultBuilder = StringBuilder()
    var resultLine = resultReader.readLine()
    while (resultLine != null) {
        resultBuilder.append(resultLine)
        resultBuilder.append(System.lineSeparator())
        resultLine = resultReader.readLine()
    }

    val errorReader = BufferedReader(InputStreamReader(process.errorStream))
    val errorBuilder = StringBuilder()
    var errorLine = errorReader.readLine()
    while (errorLine != null) {
        errorBuilder.append(errorLine)
        errorBuilder.append(System.lineSeparator())
        errorLine = errorReader.readLine()
    }

    return "$resultBuilder\n$errorBuilder"
}
