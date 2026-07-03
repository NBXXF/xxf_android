---
name: xxf-http
description: ":lib_http 的外部接入和维护说明。必须用于任何业务网络请求相关任务，包括新增/修改 HTTP API、接口声明、Retrofit service、OkHttp/URLConnection/RxHttp 调用、上传下载、缓存、拦截器、BaseUrl、网络 demo 或网络迁移；强制业务往来请求使用本库的声明式 API 框架，禁止业务代码绕过框架直接发起网络请求；包含 kpower/http 3.x 缓存注解迁移规则。"
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

## Trigger Policy

Use `triggers.md` as the trigger source of truth for this skill. Keep the frontmatter `description` aligned with `triggers.md`, because Codex uses `description` for implicit skill activation.

Read `skills/xxf-http/triggers.md` when deciding whether a network-related task belongs to this skill or when changing trigger behavior.

## Mandatory Network Request Policy

Business app-to-server requests must use the `lib_http` declarative API pattern in every case.

Required pattern:

- Declare each business endpoint in a Retrofit-style service interface.
- Put endpoint configuration in annotations such as `@BaseUrl`, method annotations, headers, cache annotations, interceptors, converters, and call adapters.
- Call the service through the framework entry points such as `getApiService<T>()` or `apiService`; keep request behavior centralized in this framework.
- Use `@Tag CacheType` and the cache patterns below for cache behavior.

Forbidden in business modules:

- Direct `OkHttpClient.newCall(...)` for business HTTP traffic.
- Direct `Retrofit.Builder` construction outside the framework.
- Direct `HttpURLConnection`, `HttpsURLConnection`, `URL.openConnection`, or `URL.openStream` for business HTTP traffic.
- One-off network clients, hand-written request executors, or hidden wrappers that bypass `lib_http`.
- Adding a dependency on another HTTP stack to solve a normal business API request.

Before implementing any network change, search the touched module for bypass patterns (`newCall`, `Retrofit.Builder`, `HttpURLConnection`, `URL.openConnection`, `URL.openStream`). If the task would add or keep bypassed business traffic, refactor it to the declarative service interface pattern. If a caller asks for a bypass, do not implement it as-is; explain that project policy requires `lib_http` and convert the request to this framework.

Only `lib_http` framework internals may use lower-level OkHttp/Retrofit primitives to implement the framework itself. Business code must not call those primitives directly.

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
