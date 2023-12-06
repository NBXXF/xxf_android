package com.xxf.arch.tracker.converter.impl

import com.xxf.arch.tracker.ChanelTracker
import com.xxf.arch.tracker.converter.TrackerConverter

class StringTrackerConverter : TrackerConverter {
    override fun convert(data: Any, extra: MutableMap<Any, Any>, chanel: ChanelTracker): String? {
        if (data is String) {
            return data
        }
        return "$data"
    }
}