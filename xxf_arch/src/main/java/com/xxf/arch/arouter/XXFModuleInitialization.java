package com.xxf.arch.arouter;

import android.content.Context;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 模块初始化
 * <p>
 * 使用方法:继承此类 并添加 @Interceptor(priority = 8, name = "测试用拦截器")注解
 * priority 不能相同,priority 值越低,越先执行
 */
public abstract class XXFModuleInitialization implements IInterceptor {

    /**
     * 模块的初始化，会在sdk初始化的时候调用该方法，仅会调用一次
     *
     * @param context
     */
    @Override
    public abstract void init(Context context);

    @Override
    public final void process(Postcard postcard, InterceptorCallback callback) {
        callback.onContinue(postcard);
    }
}
