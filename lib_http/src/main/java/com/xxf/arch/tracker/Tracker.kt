package com.xxf.arch.tracker

import com.xxf.arch.tracker.converter.TrackerConverter
import com.xxf.arch.tracker.converter.impl.ThrowableTrackerConverter
import com.xxf.arch.tracker.converter.impl.RequestTrackerConverter
import com.xxf.arch.tracker.converter.impl.ResponseTrackerConverter
import com.xxf.arch.tracker.converter.impl.StringTrackerConverter
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * 采集数据
 * 采集异常
 * 采集日志
 */
object Tracker {
    private val trackerConverters by lazy {
        mutableListOf<TrackerConverter>(
            RequestTrackerConverter(),
            ResponseTrackerConverter(),
            ThrowableTrackerConverter(),
            StringTrackerConverter()
        )
    }
    private val chanelBatchTrackers by lazy {
        mutableListOf<ChanelTracker>()
    }

    /**
     * 转换器
     */
    fun <T : TrackerConverter> registerConverter(converter: T) {
        trackerConverters.add(0, converter)
    }

    /**
     * 采集渠道
     */
    fun <T : ChanelTracker> registerChanelTracker(chanelTracker: T) {
        chanelBatchTrackers.add(0, chanelTracker)
    }

    /**
     * 采集
     * @param data 原始数据
     * @param extra 用于配置参数等 控制不同sdk 或者 不同渠道
     */
    fun track(data: Any, extra: MutableMap<Any, Any> = mutableMapOf()) {
        chanelBatchTrackers.forEach { chanel ->
            var convert: String? = null
            var tempExtra: MutableMap<Any, Any>? = null
            trackerConverters.firstOrNull {
                tempExtra = HashMap<Any, Any>(extra)
                convert = it.convert(data, tempExtra!!, chanel)
                convert != null
            }
            convert?.let {
                chanel.onTracking(it, tempExtra ?: extra)
            }
        }
    }

    /**
     * 采集
     * @param data 原始数据
     * @param extra 用于配置参数等 控制不同sdk 或者 不同渠道
     */
    fun trackAsync(data: Any, extra: MutableMap<Any, Any> = mutableMapOf()) {
        Schedulers.io().scheduleDirect {
            track(data, extra)
        }
    }

}