package com.xxf.rxjava.schedulers

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * 直接执行到调度任务上
 * @param scheduler 参考[io.reactivex.rxjava3.schedulers.Schedulers.io]
 */
fun scheduleDirect(scheduler: Scheduler, run: Runnable) {
    scheduler.scheduleDirect(run)
}


/**
 * 延时执行到调度任务上
 * @param scheduler 参考[io.reactivex.rxjava3.schedulers.Schedulers.io]
 */
fun scheduleDirect(scheduler: Scheduler, run: Runnable, delay: Long, unit: TimeUnit) {
    scheduler.scheduleDirect(run, delay, unit)
}


/**
 * ************************实现***************************
 */
fun scheduleDirectOnComputation(run: Runnable) {
    scheduleDirect(Schedulers.computation(), run)
}

fun scheduleDirectOnComputation(run: Runnable, delay: Long, unit: TimeUnit) {
    scheduleDirect(Schedulers.computation(), run, delay, unit)
}

fun scheduleDirectOnIO(run: Runnable) {
    scheduleDirect(Schedulers.io(), run)
}

fun scheduleDirectOnIO(run: Runnable, delay: Long, unit: TimeUnit) {
    scheduleDirect(Schedulers.io(), run, delay, unit)
}


fun scheduleDirectOnNewThread(run: Runnable) {
    scheduleDirect(Schedulers.newThread(), run)
}

fun scheduleDirectOnNewThread(run: Runnable, delay: Long, unit: TimeUnit) {
    scheduleDirect(Schedulers.newThread(), run, delay, unit)
}

fun scheduleDirectOnSingle(run: Runnable) {
    scheduleDirect(Schedulers.single(), run)
}

fun scheduleDirectOnSingle(run: Runnable, delay: Long, unit: TimeUnit) {
    scheduleDirect(Schedulers.single(), run, delay, unit)
}

fun scheduleDirectOnTrampoline(run: Runnable) {
    scheduleDirect(Schedulers.trampoline(), run)
}

fun scheduleDirectOnTrampoline(run: Runnable, delay: Long, unit: TimeUnit) {
    scheduleDirect(Schedulers.trampoline(), run, delay, unit)
}

fun scheduleDirectOnMain(run: Runnable) {
    scheduleDirect(AndroidSchedulers.mainThread(), run)
}

fun scheduleDirectOnMain(run: Runnable, delay: Long, unit: TimeUnit) {
    scheduleDirect(AndroidSchedulers.mainThread(), run, delay, unit)
}