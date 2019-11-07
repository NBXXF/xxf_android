
package com.xxf.arch.http.adapter.rxjava2;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.annotations.Nullable;
import com.xxf.arch.http.cache.RxHttpCache;
import retrofit2.CallAdapter;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 增加 rxcache
 */
public final class RxJava2CallAdapterFactory extends CallAdapter.Factory {
    /**
     * Returns an instance which creates synchronous observables that do not operate on any scheduler
     * by default.
     */
    public static RxJava2CallAdapterFactory create(RxHttpCache rxHttpCache) {
        return new RxJava2CallAdapterFactory(null, false, rxHttpCache);
    }

    /**
     * Returns an instance which creates asynchronous observables. Applying
     * {@link Observable#subscribeOn} has no effect on stream types created by this factory.
     */
    public static RxJava2CallAdapterFactory createAsync(RxHttpCache rxHttpCache) {
        return new RxJava2CallAdapterFactory(null, true, rxHttpCache);
    }

    /**
     * Returns an instance which creates synchronous observables that
     * {@linkplain Observable#subscribeOn(Scheduler) subscribe on} {@code scheduler} by default.
     */
    @SuppressWarnings("ConstantConditions") // Guarding public API nullability.
    public static RxJava2CallAdapterFactory createWithScheduler(Scheduler scheduler) {
        if (scheduler == null) throw new NullPointerException("scheduler == null");
        return new RxJava2CallAdapterFactory(scheduler, false, null);
    }

    private final @Nullable
    Scheduler scheduler;
    private final boolean isAsync;
    private RxHttpCache rxHttpCache;

    private RxJava2CallAdapterFactory(@Nullable Scheduler scheduler, boolean isAsync, RxHttpCache rxHttpCache) {
        this.scheduler = scheduler;
        this.isAsync = isAsync;
        this.rxHttpCache = rxHttpCache;
    }

    @Override
    public @Nullable
    CallAdapter<?, ?> get(
            Type returnType, Annotation[] annotations, Retrofit retrofit) {
        com.xxf.arch.annotation.RxHttpCache.CacheType rxCacheType = com.xxf.arch.annotation.RxHttpCache.CacheType.onlyRemote;
        if (annotations != null) {
            for (Annotation annotation : annotations) {
                if (annotation == null) {
                    continue;
                }
                //找到第一个注解的RxCache
                if (annotation.annotationType() == com.xxf.arch.annotation.RxHttpCache.class) {
                    rxCacheType = ((com.xxf.arch.annotation.RxHttpCache) annotation).value();
                    break;
                }
            }
        }


        Class<?> rawType = getRawType(returnType);

        if (rawType == Completable.class) {
            // Completable is not parameterized (which is what the rest of this method deals with) so it
            // can only be created with a single configuration.
            return new RxJava2CallAdapter(Void.class, scheduler, isAsync, this.rxHttpCache, rxCacheType, false, true, false, false,
                    false, true);
        }

        boolean isFlowable = rawType == Flowable.class;
        boolean isSingle = rawType == Single.class;
        boolean isMaybe = rawType == Maybe.class;
        if (rawType != Observable.class && !isFlowable && !isSingle && !isMaybe) {
            return null;
        }

        boolean isResult = false;
        boolean isBody = false;
        Type responseType;
        if (!(returnType instanceof ParameterizedType)) {
            String name = isFlowable ? "Flowable"
                    : isSingle ? "Single"
                    : isMaybe ? "Maybe" : "Observable";
            throw new IllegalStateException(name + " return type must be parameterized"
                    + " as " + name + "<Foo> or " + name + "<? extends Foo>");
        }

        Type observableType = getParameterUpperBound(0, (ParameterizedType) returnType);
        Class<?> rawObservableType = getRawType(observableType);
        if (rawObservableType == Response.class) {
            if (!(observableType instanceof ParameterizedType)) {
                throw new IllegalStateException("Response must be parameterized"
                        + " as Response<Foo> or Response<? extends Foo>");
            }
            responseType = getParameterUpperBound(0, (ParameterizedType) observableType);
        } else if (rawObservableType == Result.class) {
            if (!(observableType instanceof ParameterizedType)) {
                throw new IllegalStateException("Result must be parameterized"
                        + " as Result<Foo> or Result<? extends Foo>");
            }
            responseType = getParameterUpperBound(0, (ParameterizedType) observableType);
            isResult = true;
        } else {
            responseType = observableType;
            isBody = true;
        }

        return new RxJava2CallAdapter(responseType, scheduler, isAsync, this.rxHttpCache, rxCacheType, isResult, isBody, isFlowable,
                isSingle, isMaybe, false);
    }
}
