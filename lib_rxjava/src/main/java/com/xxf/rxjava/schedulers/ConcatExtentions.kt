package com.xxf.rxjava.schedulers

import io.reactivex.rxjava3.core.Observable


/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2021/8/30
 * Description ://链接操作符扩展
 */

/**
 *
 * 第一次执行中断 不会影响 第二次执行 但是下游报错后无法处理了,且上游无法感知下游报错
 * 避免 onError 覆盖onNext 需要用DelayError操作符
 *
 *
 * .concatDelayError
 * 顺序执行
 *
 *  concatEagerDelayError
 *  同时发射，但是按照连接顺序依次接收
 *
 *  concatDelayError与concatEagerDelayError 都需要observeOn(xxx,true)
 *  参考:https://github.com/ReactiveX/RxJava/issues/3908
 *
 *
 *  然而
 *  concatMapDelayError （串行)
 *  与concatMapEagerDelayError(并行,按照连接顺序依次接收) 更优秀
 *
 *  参考:https://stackoverflow.com/questions/55139062/how-to-concateagerdelayerror-in-rxjava2
 *
 *
 */

/**
 * 线程并行（前提是每个Observable不是指定在同一线程） 合并有序(添加Observable的顺序) 且延迟错误
 */
inline fun <reified T> Observable<Observable<T>>.concatMapEagerDelayError(): Observable<T> {
    return this.concatMapEagerDelayError({ t -> t }, true)
}

/**
 * 线程串行 合并有序(添加Observable的顺序) 且延迟错误
 */
inline fun <reified T> Observable<Observable<T>>.concatMapDelayError(): Observable<T> {
    return this.concatMapDelayError { t -> t }
}
