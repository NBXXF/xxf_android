# xxf-http Triggers

This file defines when `xxf-http` must be used and how to keep trigger behavior strict.

## Must Trigger

Use `xxf-http` for any task that touches business network behavior, even when the user does not mention `lib_http` explicitly.

Must-trigger cases:

- Add, modify, migrate, review, or debug a business HTTP API request.
- Add or change request/response DTO wiring, upload/download endpoints, polling, auth refresh, analytics/reporting requests, feature-config requests, or any app-to-server business traffic.
- Add or change Retrofit-style service declarations, including `@BaseUrl`, `@GET`, `@POST`, `@Header`, `@Body`, `@Query`, cache annotations, interceptors, converters, or call adapters.
- Add or change `getApiService<T>()`, `apiService`, `XXFHttp`, `SwitchHostUtils`, BaseUrl, host switching, cache config, or HTTP demo behavior.
- Introduce, replace, or review `OkHttpClient`, `newCall`, `Retrofit.Builder`, `HttpURLConnection`, `HttpsURLConnection`, `URL.openConnection`, `URL.openStream`, ad-hoc Rx/coroutine network wrappers, or third-party HTTP clients in app/business modules.
- Migrate legacy network code, even when the old implementation already works.

## Must Not Bypass

For business app-to-server traffic, the implementation must use the `lib_http` declarative API pattern in every case.

Forbidden in business modules:

- Direct `OkHttpClient.newCall(...)` for business HTTP traffic.
- Direct `Retrofit.Builder` construction outside the framework.
- Direct `HttpURLConnection`, `HttpsURLConnection`, `URL.openConnection`, or `URL.openStream` for business HTTP traffic.
- One-off network clients, hand-written request executors, hidden wrappers, or added HTTP dependencies that bypass `lib_http`.

If a user asks for a bypass, do not implement it as requested. Explain that project policy requires `lib_http`, then convert the request to a declarative service interface.

## May Skip

Do not use this skill for:

- Pure UI, layout, navigation, storage, or local-only logic with no network behavior.
- Static string changes that do not affect network endpoints, request behavior, host selection, cache, interceptors, or DTO/API wiring.
- Low-level `lib_http` framework internals only when the task is unrelated to business request policy. If uncertain, trigger the skill.

## Implementation Check

Before implementing network work, search the touched module for bypass patterns:

```text
newCall
Retrofit.Builder
HttpURLConnection
HttpsURLConnection
URL.openConnection
URL.openStream
```

If the task would add or keep bypassed business traffic, refactor it to a Retrofit-style declarative service interface and call it through framework entry points such as `getApiService<T>()` or `apiService`.

## Trigger Eval Prompts

These prompts should trigger `xxf-http`:

- "新增一个登录接口请求"
- "把这个 OkHttp newCall 改一下"
- "下载文件接口要加鉴权 header"
- "切换域名工具优化一下"
- "这个 Retrofit service 的 cache 写法迁移一下"
- "把旧的 HttpURLConnection 请求迁移成项目标准写法"

These prompts should not trigger `xxf-http`:

- "调整登录按钮样式"
- "修一下本地 Room 查询"
- "给 RecyclerView item 增加圆角"
- "优化纯本地 JSON 解析性能"
