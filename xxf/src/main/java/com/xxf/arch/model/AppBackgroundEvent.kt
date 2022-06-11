package com.xxf.arch.model

class AppBackgroundEvent : java.io.Serializable {
    /**
     * 应用是否在后台
     */
    var isBackground: Boolean = true

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AppBackgroundEvent) return false

        if (isBackground != other.isBackground) return false

        return true
    }

    override fun hashCode(): Int {
        return isBackground.hashCode()
    }

    override fun toString(): String {
        return "AppBackgroundEvent(isBackground=$isBackground)"
    }
}