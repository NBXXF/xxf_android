---
name: xxf-blockcanary-blockcanary-android
description: :optional:blockcanary:blockcanary-android 模块规则（optional library，当前 settings.gradle 启用）。修改该模块、调用方、依赖或验证入口时使用。
---

# :optional:blockcanary:blockcanary-android

## Scope

- Gradle path: `:optional:blockcanary:blockcanary-android`
- Directory: `optional/blockcanary/blockcanary-android`
- Status: enabled in settings.gradle
- Type: optional library
- Namespace: `com.github.moduth.blockcanary`
- Plugins: `com.android.library, kotlin-android`
- Build features touched in Gradle: `buildConfig`
- Published with `publish_maven.gradle`; preserve `publishVersion`, `publishGroup`, `moduleName`, and relative script path.

## Dependency Boundary

Project dependencies:

- `:optional:blockcanary:blockcanary-analyzer`

Rules:

- Keep changes inside this module unless callers, demos, resources, Manifest, or published API require synchronized updates.
- Use `api` only when the dependency is part of this module public API; otherwise prefer `implementation`.
- Demo/sample modules verify usage and must not become required by library modules.
- This is an optional module; avoid introducing dependencies from core modules back into optional features.

## Verification

- 优先运行 `./gradlew :optional:blockcanary:blockcanary-android:compileDebugKotlin`；任务不存在时退回 `./gradlew :optional:blockcanary:blockcanary-android:assembleDebug`。
- If public API changes, also compile the closest direct callers or the affected demo/sample module.
- For publishing changes, inspect generated POM/dependency exposure before release.

## Risk Notes

- Check published API compatibility and downstream module compilation.
