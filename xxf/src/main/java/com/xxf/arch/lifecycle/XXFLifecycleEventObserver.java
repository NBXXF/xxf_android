
package com.xxf.arch.lifecycle;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.1
 * @Description lifecycle观察
 * @date createTime：2018/9/7
 * Deprecated
 * This annotation required the usage of code generation or reflection, which should be avoided. Use DefaultLifecycleObserver or LifecycleEventObserver instead.
 *
 * DefaultLifecycleObserver  LifecycleEventObserver
 */
@Deprecated
public class XXFLifecycleEventObserver implements LifecycleEventObserver{

    @Override
    public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {

    }
}
