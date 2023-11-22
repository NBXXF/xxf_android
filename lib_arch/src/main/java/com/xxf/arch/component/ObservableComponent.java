package com.xxf.arch.component;

import android.util.Pair;

import io.reactivex.rxjava3.core.Observable;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2018/9/7
 * Description ://适合 子组件和父组件通信
 * C: 本身组件
 * E:事件
 */
public interface ObservableComponent<C, E> {
    /**
     * 获取 component事件
     *
     * @return
     */
    Observable<Pair<C, E>> getComponentObservable();

    /**
     * 设置结果 事件 发送到Observable事件流中
     *  可多次
     *  本质是onNext
     * @param result
     * @return
     */
    void setComponentResult(E result);
}
