package com.xxf.utils

import android.os.SystemClock
import java.util.WeakHashMap

/**
 * @author xuanyouwu
 * @email xuanyouwu@163.com
 * @time 2016-11-10 17:50
 * 防爆点击，截流工具
 */
object RAUtils {
    /**
     * 记录上次生效的时间
     */
    private val effectedMap: WeakHashMap<String, Long> = WeakHashMap()
    const val DURATION_DEFAULT: Long = 500

    /**
     * 阀门是否有效
     * @return 是否有效;true表示应该是合法的,可以通过
     */
    val isLegalDefault: Boolean
        get() = isLegal(DURATION_DEFAULT)

    /**
     * 阀门是否有效
     * @param duration 阀门区间 单位 毫秒
     * @return 是否有效;true表示应该是合法的,可以通过
     */
    fun isLegal(duration: Long): Boolean {
        return isLegal(RAUtils::class.java.name, duration)
    }

    /**
     * 阀门是否有效
     * @param key 唯一标识阀门钥匙
     * @param duration 阀门区间 单位 毫秒
     * @return 是否有效;true表示应该是合法的,可以通过
     */
    fun isLegal(key: String, duration: Long): Boolean {
        val lastEffected = effectedMap.getOrPut(key) { 0L }
        val current = SystemClock.elapsedRealtime()
        if (current - lastEffected >= duration) {
            effectedMap[key] = current
            return true
        }
        return false
    }
}