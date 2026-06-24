---
name: xxf-preferences-mmkv
description: :lib_preferencesMMKV 模块规则（core library，当前 settings.gradle 启用）。修改该模块、调用方、依赖或验证入口时使用。
---

# :lib_preferencesMMKV

## Scope

- Gradle path: `:lib_preferencesMMKV`
- Directory: `lib_preferencesMMKV`
- Status: enabled in settings.gradle
- Type: core library
- Namespace: `com.xxf.preferences.mmkv`
- Plugins: `com.android.library, kotlin-android, kotlin-kapt`
- Build features touched in Gradle: `buildConfig`
- Published with `publish_maven.gradle`; preserve `publishVersion`, `publishGroup`, `moduleName`, and relative script path.
- Uses kapt or annotation processing; validate with assemble when generated sources may be affected.

## Dependency Boundary

Project dependencies:

- `:lib_preferences`
- `com.tencent:mmkv`

Rules:

- Keep changes inside this module unless callers, demos, resources, Manifest, or published API require synchronized updates.
- Use `api` only when the dependency is part of this module public API; otherwise prefer `implementation`.
- Demo/sample modules verify usage and must not become required by library modules.

## Verification

- 优先运行 `./gradlew :lib_preferencesMMKV:compileDebugKotlin`；任务不存在时退回 `./gradlew :lib_preferencesMMKV:assembleDebug`。
- If public API changes, also compile the closest direct callers or the affected demo/sample module.
- For publishing changes, inspect generated POM/dependency exposure before release.

## Risk Notes

- Check published API compatibility and downstream module compilation.
