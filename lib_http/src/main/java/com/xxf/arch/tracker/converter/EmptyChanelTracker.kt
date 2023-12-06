package com.xxf.arch.tracker.converter

import com.xxf.arch.tracker.ChanelTracker

class EmptyChanelTracker : ChanelTracker {
    final override fun onTracking(data: String, extra: Map<Any, Any>) {
    }
}