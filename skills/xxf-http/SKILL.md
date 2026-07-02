---
name: xxf-http
description: :lib_http 的外部接入和维护说明，包含 kpower/http 3.x 缓存注解迁移规则。
---

# :lib_http

## What It Provides

`lib_http` provides HTTP and network helpers on top of Retrofit, OkHttp, RxJava, and kpower/http.

## Dependencies

- `lib_application`
- `lib_ktx`
- kpower/http

## Public Usage

Use the public APIs exposed by this artifact. Keep the dependency on the published module only; do not rely on repository-internal build commands or local source paths.

## References

Before making changes to HTTP demos or cache annotations, read:

- `skills/xxf-http/references/login-api-service.md`

That reference is the canonical example for:

- `@Tag CacheType` cache typing
- `@Headers("cache:5000")` fixed cache time
- `@Header("cache") long cacheTime` dynamic cache time
- `@RxHttpCacheConfig` and interceptor setup in the demo module

## Maintenance Rules

- For normal implementation changes, keep edits scoped to `lib_http` and its demo module unless the caller explicitly requests wider cleanup.
- Demo code lives under `lib_http/httpdemo`.
- In Android Gradle Plugin 8+, do not set `package` on `AndroidManifest.xml`; use `android.namespace` in the module `build.gradle`.
- If a source package uses `com.xxf.http.demo`, keep `lib_http/httpdemo` namespace aligned with that package so relative manifest class names and generated `R` references resolve correctly.

## kpower/http 3.x Cache Migration

kpower/http 3.x no longer uses `retrofit2.http.Cache` for dynamic cache type parameters.

Use Retrofit standard `@Tag`:

```java
import retrofit2.CacheType;
import retrofit2.http.Tag;

@GET("user/info")
Observable<UserInfo> getUserInfo(@Tag CacheType cacheType);
```

Do not use:

```java
import retrofit2.http.Cache;

Observable<UserInfo> getUserInfo(@Cache CacheType cacheType);
```

## Cache Configuration Patterns

Cache type:

- Dynamic cache type: `@Tag CacheType cacheType`
- Fixed method cache type: `@RxHttpCache(CacheType.ifCache)`

Cache time:

- Fixed cache time: `@Headers("cache:5000")`
- Dynamic cache time: `@Header("cache") long cacheTime`
- Cache time is in milliseconds.

The two concerns can be combined:

```java
@GET("user/info")
@Headers("cache:5000")
Observable<UserInfo> getUserInfoWithFixedCacheTime(@Tag CacheType cacheType);

@GET("user/info")
Observable<UserInfo> getUserInfoWithDynamicCacheTime(
    @Header("cache") long cacheTime,
    @Tag CacheType cacheType
);
```

## Verification

After changing `lib_http` or `lib_http/httpdemo`, prefer the narrowest relevant Gradle task first:

```bash
./gradlew :lib_http:compileDebugKotlin
./gradlew :lib_http:httpdemo:compileDebugKotlin :lib_http:httpdemo:compileDebugJavaWithJavac
```
