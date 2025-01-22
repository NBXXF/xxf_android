package com.xxf.mlkit.recorder

import java.util.concurrent.atomic.AtomicInteger

/**
 * @ClassName: RetryRecorder
 * @Description:多线程重试计数
 * @Author: xuanyouwu@163.com 17611639080
 * @Date: 2023/8/25 10:37
 *
 */
class RetryRecorder(private val limit: Int) {
    private var times: AtomicInteger = AtomicInteger(0);

    /**
     * 重置 清0
     */
    fun reset() {
        times.set(0)
    }

    fun setOver() {
        times.set(limit)
    }

    /**
     * 获取
     */
    fun get(): Int {
        return times.get()
    }

    /**
     * 自增1
     */
    fun increment() {
        times.incrementAndGet()
    }

    /**
     * 是否超过了
     */
    fun isOver(): Boolean {
        val temp = get();
        return temp >= this.limit;
    }

}