---
name: xxf-permission
description: :lib_permission 模块规则（core library，当前 settings.gradle 启用）。修改该模块、调用方、依赖或验证入口时使用。
---

# :lib_permission

## Scope

- Gradle path: `:lib_permission`
- Directory: `lib_permission`
- Status: enabled in settings.gradle
- Type: core library
- Namespace: `com.xxf.permission`
- Plugins: `com.android.library, kotlin-android, kotlin-kapt`
- Build features touched in Gradle: `buildConfig`
- Published with `publish_maven.gradle`; preserve `publishVersion`, `publishGroup`, `moduleName`, and relative script path.
- Uses kapt or annotation processing; validate with assemble when generated sources may be affected.

## Dependency Boundary

Project dependencies:

- `:lib_activityresult`

Rules:

- Keep changes inside this module unless callers, demos, resources, Manifest, or published API require synchronized updates.
- Use `api` only when the dependency is part of this module public API; otherwise prefer `implementation`.
- Demo/sample modules verify usage and must not become required by library modules.

## Verification

- If behavior changes are user-visible, also assemble the related demo/sample module listed below.
- 优先运行 `./gradlew :lib_permission:compileDebugKotlin`；任务不存在时退回 `./gradlew :lib_permission:assembleDebug`。
- If public API changes, also compile the closest direct callers or the affected demo/sample module.
- For publishing changes, inspect generated POM/dependency exposure before release.

## Risk Notes

- Check permissions, device capability, and Android version behavior.

## Related Demo / Sample Modules

- `:lib_permission:demo` at `lib_permission/demo` is for verification/example only; do not expose it as an installable skill.

