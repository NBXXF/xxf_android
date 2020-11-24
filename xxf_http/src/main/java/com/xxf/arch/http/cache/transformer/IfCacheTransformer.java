package com.xxf.arch.http.cache.transformer;

import androidx.annotation.NonNull;

import com.xxf.arch.http.cache.RxHttpCache;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @Description: 如果本地存在就返回本地的, 否则返回网络的数据
 * @Author: XGod
 * @CreateDate: 2020/11/24 10:53
 */
public class IfCacheTransformer<R> extends AbsCacheTransformer<R> {

    public IfCacheTransformer(@NonNull Call<R> call, RxHttpCache rxHttpCache) {
        super(call, rxHttpCache);
    }

    @Override
    public final ObservableSource<Response<R>> apply(Observable<Response<R>> remoteObservable) {
        return getCacheOrEmpty().switchIfEmpty(cacheAfter(remoteObservable));
    }
}
