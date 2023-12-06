package com.xxf.arch.tracker.converter

import com.xxf.arch.tracker.ChanelTracker

interface TrackerConverter {
    /**
     * 将 data 转换成string 可以转换成日志
     * @param data
     * return 为null代表自己无能为力,交给下一个转换器
     */
    fun convert(data: Any,extra:MutableMap<Any,Any>,chanel: ChanelTracker): String?
}