package com.xxf.arch.tracker.converter.impl

import android.util.Log
import com.xxf.arch.tracker.ChanelTracker
import com.xxf.arch.tracker.converter.TrackerConverter

class ThrowableTrackerConverter : TrackerConverter {
    companion object {
        val KEY_THROWABLE_NAME: String = ThrowableTrackerConverter::class.java.simpleName
    }

    override fun convert(data: Any, extra: MutableMap<Any, Any>, chanel: ChanelTracker): String? {
        if (data is Throwable) {
            try {
                return Log.getStackTraceString(data).apply {
                    extra[KEY_THROWABLE_NAME] = data::class.java.name
                }
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
        return null
    }
}