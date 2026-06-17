---
name: xxf-download
description: :optional:lib_download 模块规则（optional library，当前 settings.gradle 启用）。修改该模块、调用方、依赖或验证入口时使用。
---

# :optional:lib_download

## Scope

- Gradle path: `:optional:lib_download`
- Directory: `optional/lib_download`
- Status: enabled in settings.gradle
- Type: optional library
- Namespace: `com.xxf.download`
- Plugins: `com.android.library, kotlin-android`
- Build features touched in Gradle: `buildConfig`
- Published with `publish_maven.gradle`; preserve `publishVersion`, `publishGroup`, `moduleName`, and relative script path.

## Dependency Boundary

Project dependencies:

- `:lib_log`
- `:lib_adapter`

Rules:

- Keep changes inside this module unless callers, demos, resources, Manifest, or published API require synchronized updates.
- Use `api` only when the dependency is part of this module public API; otherwise prefer `implementation`.
- Demo/sample modules verify usage and must not become required by library modules.
- This is an optional module; avoid introducing dependencies from core modules back into optional features.

## Verification

- 优先运行 `./gradlew :optional:lib_download:compileDebugKotlin`；任务不存在时退回 `./gradlew :optional:lib_download:assembleDebug`。
- If public API changes, also compile the closest direct callers or the affected demo/sample module.
- For publishing changes, inspect generated POM/dependency exposure before release.

## Risk Notes

- Check network errors, retries, cancellation, and threading.
- Check UI rendering, RecyclerView/list performance, and resource compatibility.
