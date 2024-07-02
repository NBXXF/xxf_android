package com.xxf.download

import com.liulishuo.okdownload.DownloadListener
import com.liulishuo.okdownload.DownloadTask
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo
import com.liulishuo.okdownload.core.cause.EndCause
import com.liulishuo.okdownload.core.cause.ResumeFailedCause
import com.xxf.log.logD

/**
 * 包装分发1对多
 */
open class DownloaderListenerWrapper(private val listeners: MutableList<DownloadListener> = mutableListOf<DownloadListener>()) :
    DownloadListener {
    companion object {
        private const val TAG = "DownloaderListenerWrapper"
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

    override fun taskStart(p0: DownloadTask) {
        listeners.forEach {
            it.taskStart(p0)
        }
    }

    override fun connectTrialStart(p0: DownloadTask, p1: MutableMap<String, MutableList<String>>) {
        logD(TAG) { "=====>connectTrialStart:" + p0.url }
        listeners.forEach {
            it.connectTrialStart(p0, p1)
        }
    }

    override fun connectTrialEnd(
        p0: DownloadTask,
        p1: Int,
        p2: MutableMap<String, MutableList<String>>
    ) {
        logD(TAG) { "=====>connectTrialEnd:" + p0.url }
        listeners.forEach {
            it.connectTrialEnd(p0, p1, p2)
        }
    }

    override fun downloadFromBeginning(
        p0: DownloadTask,
        p1: BreakpointInfo,
        p2: ResumeFailedCause
    ) {
        logD(TAG) { "=====>downloadFromBeginning:" + p0.url + "" }
        listeners.forEach {
            it.downloadFromBeginning(p0, p1, p2)
        }
    }

    override fun downloadFromBreakpoint(p0: DownloadTask, p1: BreakpointInfo) {
        logD(TAG) { "=====>downloadFromBreakpoint:" + p0.url }
        listeners.forEach {
            it.downloadFromBreakpoint(p0, p1)
        }
    }

    override fun connectStart(
        p0: DownloadTask,
        p1: Int,
        p2: MutableMap<String, MutableList<String>>
    ) {
        logD(TAG) { "=====>connectStart:" + p0.url }
        listeners.forEach {
            it.connectStart(p0, p1, p2)
        }
    }

    override fun connectEnd(
        p0: DownloadTask,
        p1: Int,
        p2: Int,
        p3: MutableMap<String, MutableList<String>>
    ) {
        logD(TAG) { "=====>connectEnd:" + p0.url }
        listeners.forEach {
            it.connectEnd(p0, p1, p2, p3)
        }
    }

    override fun fetchStart(p0: DownloadTask, p1: Int, p2: Long) {
        logD(TAG) { "=====>fetchStart:" + p0.url + " localPth:" + p0.file?.absolutePath }
        listeners.forEach {
            it.fetchStart(p0, p1, p2)
        }
    }

    override fun fetchProgress(p0: DownloadTask, p1: Int, p2: Long) {
        var total: Long = p0.info?.totalLength ?: 0;
        var downloaded: Long = p0.info?.totalOffset ?: 0;
        val progress = (downloaded.toFloat() / total.toFloat());
        logD(TAG) { "=====>fetchProgress:" + progress + "  " + p0.url + "  b:" + p1 + "  i:" + p2 };
        listeners.forEach {
            it.fetchProgress(p0, p1, p2)
        }
    }

    override fun fetchEnd(p0: DownloadTask, p1: Int, p2: Long) {
        logD(TAG) { "=====>fetchEnd:" + p0.url }
        listeners.forEach {
            it.fetchEnd(p0, p1, p2)
        }
    }

    override fun taskEnd(p0: DownloadTask, p1: EndCause, p2: Exception?) {
        logD(TAG) { "=====>taskEnd:" + p0.url }
        listeners.forEach {
            it.taskEnd(p0, p1, p2)
        }
    }
}