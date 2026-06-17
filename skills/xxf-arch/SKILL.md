---
name: xxf-arch
description: :lib_arch 模块规则（core library，当前 settings.gradle 启用）。修改该模块、调用方、依赖或验证入口时使用。
---

# :lib_arch

## Scope

- Gradle path: `:lib_arch`
- Directory: `lib_arch`
- Status: enabled in settings.gradle
- Type: core library
- Namespace: `com.xxf.arch`
- Plugins: `com.android.library, kotlin-android, kotlin-kapt`
- Build features touched in Gradle: `viewBinding, dataBinding, buildConfig`
- Published with `publish_maven.gradle`; preserve `publishVersion`, `publishGroup`, `moduleName`, and relative script path.
- Uses kapt or annotation processing; validate with assemble when generated sources may be affected.

## Dependency Boundary

Project dependencies:

- `:lib_application`
- `:lib_http`
- `:lib_utils`
- `:lib_log`
- `:lib_permission`
- `:lib_snackbar`
- `:lib_view_round`

Rules:

- Keep changes inside this module unless callers, demos, resources, Manifest, or published API require synchronized updates.
- Use `api` only when the dependency is part of this module public API; otherwise prefer `implementation`.
- Demo/sample modules verify usage and must not become required by library modules.

## Verification

- 优先运行 `./gradlew :lib_arch:assembleDebug`；任务不存在时退回 `./gradlew :lib_arch:assembleDebug`。
- If public API changes, also compile the closest direct callers or the affected demo/sample module.
- For publishing changes, inspect generated POM/dependency exposure before release.

## Risk Notes

- Check permissions, device capability, and Android version behavior.
- Check network errors, retries, cancellation, and threading.
- Check UI rendering, RecyclerView/list performance, and resource compatibility.
