package com.xxf.arch.tracker

/**
 * 渠道采集, 通一个 app 可能有多个采集sdk
 * 比如同时有sentry 也可能有bugLy
 */
interface ChanelTracker {
    fun onTracking(data: String,extra: Map<Any, Any>)
}