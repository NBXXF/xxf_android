---
name: xxf-glide
description: :optional:lib_glide 模块规则（optional library，当前 settings.gradle 启用）。修改该模块、调用方、依赖或验证入口时使用。
---

# :optional:lib_glide

## Scope

- Gradle path: `:optional:lib_glide`
- Directory: `optional/lib_glide`
- Status: enabled in settings.gradle
- Type: optional library
- Namespace: `com.xxf.images.glide`
- Plugins: `com.android.library, kotlin-android, kotlin-kapt`
- Build features touched in Gradle: `viewBinding, buildConfig`
- Published with `publish_maven.gradle`; preserve `publishVersion`, `publishGroup`, `moduleName`, and relative script path.
- Uses kapt or annotation processing; validate with assemble when generated sources may be affected.

## Dependency Boundary

Project dependencies:

- `:lib_application`

Rules:

- Keep changes inside this module unless callers, demos, resources, Manifest, or published API require synchronized updates.
- Use `api` only when the dependency is part of this module public API; otherwise prefer `implementation`.
- Demo/sample modules verify usage and must not become required by library modules.
- This is an optional module; avoid introducing dependencies from core modules back into optional features.

## Verification

- 优先运行 `./gradlew :optional:lib_glide:assembleDebug`；任务不存在时退回 `./gradlew :optional:lib_glide:assembleDebug`。
- If public API changes, also compile the closest direct callers or the affected demo/sample module.
- For publishing changes, inspect generated POM/dependency exposure before release.

## Risk Notes

- Check network errors, retries, cancellation, and threading.
- Check UI rendering, RecyclerView/list performance, and resource compatibility.
- Check file URI exposure, media loading, and host app integration.
