package com.xxf.rxjava

import io.reactivex.rxjava3.core.*

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：8/7/21
 * Description : 去重判断
 */

/**
 * 流 有变化 才执行 相近两次去重
 * distinctUntilChanged 默认是equals;这里拓展为hashcode,便于内存数据修改未刷新的问题,更加精确判断内容变化
 * 业务模型需要复写hashCode 方法
 * 典型场景
 * 1.高刷
 * 2.动画避免重复执行
 * 3.结合concat 等组合操作符 firstCache模式 提升页面速度 又避免闪动 重复刷新等
 */
inline fun <reified T> Observable<T>.distinctUntilChangedByHash(): Observable<T> {
    return this.distinctUntilChanged { t1, t2 -> t1.hashCode() == t2.hashCode() }
}

/**
 *  流 有变化 才执行 相近两次去重
 * distinctUntilChanged 默认是equals; 这里拓展为hashcode,便于内存数据修改未刷新的问题,更加精确判断内容变化
 * 业务模型需要复写hashCode 方法
 * 典型场景
 * 1.高刷
 * 2.动画避免重复执行
 * 3.结合concat 等组合操作符 firstCache模式 提升页面速度 又避免闪动 重复刷新等
 */
inline fun <reified T> Flowable<T>.distinctUntilChangedByHash(): Flowable<T> {
    return this.distinctUntilChanged { t1, t2 -> t1.hashCode() == t2.hashCode() }
}
