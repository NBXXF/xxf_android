package com.xxf.arch.tracker.converter.impl

import android.util.Log
import com.xxf.arch.exceptions.ResponseException
import com.xxf.arch.tracker.ChanelTracker
import com.xxf.arch.tracker.converter.TrackerConverter
import com.xxf.json.Json

open class ThrowableTrackerConverter : TrackerConverter {
    companion object {
        val KEY_THROWABLE_NAME: String = "throwable_name"
    }

    override fun convert(data: Any, extra: MutableMap<Any, Any>, chanel: ChanelTracker): String? {
        if (data is Throwable) {
            try {
                return Log.getStackTraceString(data).run {
                    if (data is ResponseException) {
                        this + " for body:" + Json.toJson(data.body)
                    } else {
                        this
                    }
                }.also {
                    extra[KEY_THROWABLE_NAME] = data::class.java.name
                }
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
        return null
    }
}