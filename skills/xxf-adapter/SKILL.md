---
name: xxf-adapter
description: :lib_adapter 模块规则（core library，当前 settings.gradle 启用）。修改该模块、调用方、依赖或验证入口时使用。
---

# :lib_adapter

## Scope

- Gradle path: `:lib_adapter`
- Directory: `lib_adapter`
- Status: enabled in settings.gradle
- Type: core library
- Namespace: `com.xxf.adapter`
- Plugins: `com.android.library, kotlin-android, kotlin-kapt`
- Build features touched in Gradle: `viewBinding, buildConfig`
- Published with `publish_maven.gradle`; preserve `publishVersion`, `publishGroup`, `moduleName`, and relative script path.
- Uses kapt or annotation processing; validate with assemble when generated sources may be affected.

## Dependency Boundary

Project dependencies:

- `:lib_ktx`

Rules:

- Keep changes inside this module unless callers, demos, resources, Manifest, or published API require synchronized updates.
- Use `api` only when the dependency is part of this module public API; otherwise prefer `implementation`.
- Demo/sample modules verify usage and must not become required by library modules.

## Verification

- If behavior changes are user-visible, also assemble the related demo/sample module listed below.
- 优先运行 `./gradlew :lib_adapter:assembleDebug`；任务不存在时退回 `./gradlew :lib_adapter:assembleDebug`。
- If public API changes, also compile the closest direct callers or the affected demo/sample module.
- For publishing changes, inspect generated POM/dependency exposure before release.

## Risk Notes

- Check UI rendering, RecyclerView/list performance, and resource compatibility.

## Related Demo / Sample Modules

- `:lib_adapter:lib_adapter_demo` at `lib_adapter/lib_adapter_demo` is for verification/example only; do not expose it as an installable skill.

