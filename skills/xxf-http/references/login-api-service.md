# LoginApiService Reference

This reference is based on `lib_http/httpdemo/src/main/java/com/xxf/http/demo/LoginApiService.java`.

## What This Demo Shows

`LoginApiService` demonstrates the full kpower/http + Retrofit usage pattern:

- `@BaseUrl` for the API root
- `@Dispatcher` for request concurrency
- `@RxHttpCacheConfig` for cache directory setup
- `@Interceptor` for OkHttp interceptors
- `@RxJavaInterceptor` for the RxJava call adapter
- `@Tag CacheType` for cache strategy
- `@Headers("cache:5000")` and `@Header("cache") long cacheTime` for cache time

## Class-Level Setup

```java
@BaseUrl("http://api.map.baidu.com/")
@Dispatcher(maxRequests = 1, maxRequestsPerHost = 2)
@RxHttpCacheConfig(DefaultRxHttpCacheDirectoryProvider.class)
@Interceptor({MyLoggerInterceptor.class, MyLoggerInterceptor2.class})
@RxJavaInterceptor(DefaultCallAdapter.class)
public interface LoginApiService {
}
```

## Cache Type

The cache type is passed with Retrofit's standard `@Tag`.

```java
Observable<ListOrSingle<Weather>> getCity(@Tag CacheType cacheType);
Observable<BaseResponseDTO> getCityModel(@Tag CacheType cacheType);
```

Use this when the caller decides the cache strategy at runtime.

## Cache Time

There are two cache time patterns in the demo.

### Fixed cache time

Set a fixed cache time in the method annotation.

```java
@Headers("cache:5000")
Observable<JsonObject> getCityWithFixedCacheTime(@Tag CacheType cacheType);
```

Use this when the cache duration is part of the API contract.

### Dynamic cache time

Pass the cache duration from the caller.

```java
Observable<JsonObject> getCityWithDynamicCacheTime(
    @Header("cache") long cacheTime,
    @Tag CacheType cacheType
);
```

Use this when different call sites need different cache durations.

## Other Examples In The Demo

```java
Observable<JsonObject> getCity();
Observable<JsonObject> getCityOnlyCache();
Observable<JsonArray> getCity(@JsonString @Query("test") TestQueryJsonField queryJsonField);
```

The `getCity(@JsonString @Query(...))` example shows how to send a structured object as a serialized query parameter.

## Call Examples

```kotlin
// Cache type only
apiService.getCity(CacheType.firstCache)

// Fixed cache time + cache type
apiService.getCityWithFixedCacheTime(CacheType.firstCache)

// Dynamic cache time + cache type
apiService.getCityWithDynamicCacheTime(5000, CacheType.firstCache)
```

## Migration Notes

- Do not use `retrofit2.http.Cache`.
- Use `retrofit2.http.Tag` for cache type.
- Keep `CacheType` and cache time as two separate concerns.
- If the codebase uses `com.xxf.http.demo`, keep the demo module namespace aligned with that package.
