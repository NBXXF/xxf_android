package com.xxf.rxjava

import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import autodispose2.AutoDispose
import autodispose2.AutoDisposeConverter
import autodispose2.android.AutoDisposeAndroidPlugins
import autodispose2.android.ViewScopeProvider
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider
import io.reactivex.rxjava3.functions.BooleanSupplier

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：8/7/21
 * Description ://重构
 */
object RxLifecycle {

    /**
     * 绑定生命周期的时候 设置是否检查线程（框架默认是检查了主线程） 可以通过返回为true 来设置不检查线程
     */
    fun setOnCheckMainThread(s: BooleanSupplier) {
        AutoDisposeAndroidPlugins.setOnCheckMainThread (s);
    }
    /**
     * 自动取消
     * 不同于截流
     * 用法:observable.as(XXF.bindLifecycle(this))
     *
     * @param lifecycleOwner
     * @param <T>
     * @return
    </T> */
    fun <T> bindLifecycle(lifecycleOwner: LifecycleOwner, untilEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY): AutoDisposeConverter<T> {
        return AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner,untilEvent))
    }

    /**
     * 绑定view的生命周期
     */
    fun <T>bindLifecycle(view:View):AutoDisposeConverter<T> {
        return AutoDispose.autoDisposable(ViewScopeProvider.from(view));
    }
}