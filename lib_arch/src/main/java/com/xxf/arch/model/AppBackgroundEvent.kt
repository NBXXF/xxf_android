package com.xxf.arch.model

import android.app.Activity
import android.content.Intent

class AppBackgroundEvent : java.io.Serializable {
    /**
     * 应用是否在后台
     */
    var isBackground: Boolean = true

    var intent: Intent? = null

    var activityClass: Class<*>? = null

    var startSource: String? = null

    override fun toString(): String {
        return "AppBackgroundEvent(isBackground=$isBackground)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AppBackgroundEvent

        if (isBackground != other.isBackground) return false
        if (intent != other.intent) return false
        if (activityClass != other.activityClass) return false
        if (startSource != other.startSource) return false

        return true
    }

    override fun hashCode(): Int {
        var result = isBackground.hashCode()
        result = 31 * result + (intent?.hashCode() ?: 0)
        result = 31 * result + (activityClass?.hashCode() ?: 0)
        result = 31 * result + (startSource?.hashCode() ?: 0)
        return result
    }
}