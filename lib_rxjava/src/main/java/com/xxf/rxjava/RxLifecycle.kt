package com.xxf.rxjava

import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import autodispose2.AutoDispose
import autodispose2.AutoDisposeConverter
import autodispose2.android.ViewScopeProvider
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider
import com.xxf.rxjava.auto.dispose.ScopesFactory

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：8/7/21
 * Description ://重构
 */
object RxLifecycle {
    /**
     * 自动取消
     * 不同于截流
     * 用法:observable.as(XXF.bindLifecycle(this))
     *
     * @param lifecycleOwner
     * @param <T>
     * @return
    </T> */
    fun <T : Any> bindLifecycle(lifecycleOwner: LifecycleOwner, untilEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY): AutoDisposeConverter<T> {
        return AutoDispose.autoDisposable(ScopesFactory.completableOf(AndroidLifecycleScopeProvider.from(lifecycleOwner,untilEvent)))
    }

    /**
     * 绑定view的生命周期
     * 注意 必须是view aattached
     */
    fun <T : Any>bindLifecycle(view:View):AutoDisposeConverter<T> {
        return AutoDispose.autoDisposable(ScopesFactory.completableOf(ViewScopeProvider.from(view)));
    }
}