package com.xxf.arch.http.cache.transformer;

import androidx.annotation.NonNull;

import com.xxf.arch.http.cache.HttpCacheConfigProvider;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @Description: 只从服务器拿取
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/11/24 10:53
 */
public class OnlyRemoteTransformer<R> extends AbsCacheTransformer<R> {


    public OnlyRemoteTransformer(@NonNull Call<R> call, HttpCacheConfigProvider rxHttpCacheConfig) {
        super(call, rxHttpCacheConfig);
    }

    @Override
    public final ObservableSource<Response<R>> apply(Observable<Response<R>> remoteObservable) {
        return remoteObservable;
    }
}
