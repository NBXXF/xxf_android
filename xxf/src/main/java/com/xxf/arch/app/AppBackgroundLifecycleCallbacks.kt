package com.xxf.arch.app

import android.app.Activity
import androidx.annotation.CallSuper
import com.xxf.application.activity.SimpleActivityLifecycleCallbacks
import com.xxf.arch.model.AppBackgroundEvent
import com.xxf.bus.postEvent

internal object AppBackgroundLifecycleCallbacks : SimpleActivityLifecycleCallbacks() {
    private var visibleCount = 0;

    @CallSuper
    override fun onActivityStarted(activity: Activity) {
        super.onActivityStarted(activity)
        if (visibleCount <= 0) {
            AppBackgroundEvent().apply {
                this.isBackground = false
                this.intent = activity.intent
                this.activityClass = activity::class.java
            }.postEvent()
        }
        visibleCount++
    }

    @CallSuper
    override fun onActivityStopped(activity: Activity) {
        super.onActivityStopped(activity)
        visibleCount--
        if (visibleCount <= 0) {
            AppBackgroundEvent().apply {
                this.isBackground = true
                this.intent = activity.intent
                this.activityClass = activity::class.java
            }.postEvent()
        }
    }
}