package com.xxf.download

import com.liulishuo.okdownload.DownloadListener
import com.liulishuo.okdownload.DownloadTask
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo
import com.liulishuo.okdownload.core.cause.EndCause
import com.liulishuo.okdownload.core.cause.ResumeFailedCause
import com.xxf.log.logD


/**
 * @Author: XGod  xuanyouwu@163.com https://github.com/NBXXF  https://blog.csdn.net/axuanqq
 * Date: 1/8/19 12:07 PM
 * Description: 包装分发1对多
 */
open class DownloaderListenerWrapper(private val listeners: MutableList<DownloadListener> = mutableListOf<DownloadListener>()) :
    DownloadListener {
    companion object {
        private const val TAG = "Download"
        private const val LOG_PREFIX = "=====>task "
    }

    fun addListener(listener: DownloadListener): Boolean {
        synchronized(listeners) {
            listeners.remove(listener)
            return listeners.add(listener)
        }
    }

    fun removeListener(listener: DownloadListener): Boolean {
        synchronized(listeners) {
            return listeners.remove(listener)
        }
    }

    override fun taskStart(task: DownloadTask) {
        synchronized(listeners) {
            listeners.forEach {
                it.taskStart(task)
            }
        }
    }

    override fun connectTrialStart(
        task: DownloadTask,
        requestHeaderFields: MutableMap<String, MutableList<String>>
    ) {
        logD(TAG) { LOG_PREFIX + "connectTrialStart:" + task.url + "  requestHeaderFields:" + requestHeaderFields }
        synchronized(listeners) {
            listeners.forEach {
                it.connectTrialStart(task, requestHeaderFields)
            }
        }
    }

    override fun connectTrialEnd(
        task: DownloadTask,
        responseCode: Int,
        responseHeaderFields: MutableMap<String, MutableList<String>>
    ) {
        logD(TAG) { LOG_PREFIX + "connectTrialEnd:" + task.url + "  responseHeaderFields:" + responseHeaderFields }
        synchronized(listeners) {
            listeners.forEach {
                it.connectTrialEnd(task, responseCode, responseHeaderFields)
            }
        }
    }

    override fun downloadFromBeginning(
        task: DownloadTask,
        info: BreakpointInfo,
        cause: ResumeFailedCause
    ) {
        logD(TAG) { LOG_PREFIX + "downloadFromBeginning:" + task.url + " cause:" + cause }
        synchronized(listeners) {
            listeners.forEach {
                it.downloadFromBeginning(task, info, cause)
            }
        }
    }

    override fun downloadFromBreakpoint(task: DownloadTask, info: BreakpointInfo) {
        logD(TAG) { LOG_PREFIX + "downloadFromBreakpoint:" + task.url }
        synchronized(listeners) {
            listeners.forEach {
                it.downloadFromBreakpoint(task, info)
            }
        }
    }

    override fun connectStart(
        task: DownloadTask,
        blockIndex: Int,
        requestHeaderFields: MutableMap<String, MutableList<String>>
    ) {
        logD(TAG) { LOG_PREFIX + "connectStart:" + task.url }
        synchronized(listeners) {
            listeners.forEach {
                it.connectStart(task, blockIndex, requestHeaderFields)
            }
        }
    }

    override fun connectEnd(
        task: DownloadTask,
        blockIndex: Int,
        responseCode: Int,
        responseHeaderFields: MutableMap<String, MutableList<String>>
    ) {
        logD(TAG) { LOG_PREFIX + "connectEnd:" + task.url + "  responseHeaderFields:" + responseHeaderFields }
        synchronized(listeners) {
            listeners.forEach {
                it.connectEnd(task, blockIndex, responseCode, responseHeaderFields)
            }
        }
    }

    override fun fetchStart(task: DownloadTask, blockIndex: Int, contentLength: Long) {
        logD(TAG) { LOG_PREFIX + "fetchStart:" + task.url + " blockIndex:" + blockIndex + " contentLength:" + contentLength }
        synchronized(listeners) {
            listeners.forEach {
                it.fetchStart(task, blockIndex, contentLength)
            }
        }
    }

    override fun fetchProgress(task: DownloadTask, blockIndex: Int, increaseBytes: Long) {
        logD(TAG) {
            val total: Long = task.info?.totalLength ?: 0;
            val downloaded: Long = task.info?.totalOffset ?: 0;
            val progress = (downloaded.toFloat() / total.toFloat());
            LOG_PREFIX + "fetchProgress:" + task.url + " progress:" + progress + " blockIndex:" + blockIndex + " increaseBytes:" + increaseBytes
        };
        synchronized(listeners) {
            listeners.forEach {
                it.fetchProgress(task, blockIndex, increaseBytes)
            }
        }
    }

    override fun fetchEnd(task: DownloadTask, blockIndex: Int, contentLength: Long) {
        logD(TAG) { LOG_PREFIX + "fetchEnd:" + task.url }
        synchronized(listeners) {
            listeners.forEach {
                it.fetchEnd(task, blockIndex, contentLength)
            }
        }
    }

    override fun taskEnd(task: DownloadTask, cause: EndCause, realCause: Exception?) {
        logD(TAG) { LOG_PREFIX + "taskEnd:" + task.url + "  endCause:$cause  exception:$realCause" }
        synchronized(listeners) {
            listeners.forEach {
                it.taskEnd(task, cause, realCause)
            }
        }
    }
}