package com.xxf.arch.tracker.converter.impl

import com.xxf.arch.exceptions.ResponseException
import com.xxf.arch.tracker.ChanelTracker
import com.xxf.json.Json

class ResponseExceptionTrackerConverter : ThrowableTrackerConverter() {
    companion object {
        val KEY_RESPONSE_BODY: String = "response_body"
    }

    override fun convert(data: Any, extra: MutableMap<Any, Any>, chanel: ChanelTracker): String? {
        return super.convert(data, extra, chanel)?.also {
            if (data is ResponseException) {
                extra[KEY_RESPONSE_BODY] = Json.toJson(data.body)
            }
        }
    }
}