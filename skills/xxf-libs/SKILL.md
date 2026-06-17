---
name: xxf-libs
description: :libs 模块规则（core aggregate library，当前 settings.gradle 启用）。修改该模块、调用方、依赖或验证入口时使用。
---

# :libs

## Scope

- Gradle path: `:libs`
- Directory: `libs`
- Status: enabled in settings.gradle
- Type: core aggregate library
- Namespace: `com.xxf.libs`
- Plugins: `com.android.library, kotlin-android, kotlin-kapt`
- Build features touched in Gradle: `viewBinding, buildConfig`
- Published with `publish_maven.gradle`; preserve `publishVersion`, `publishGroup`, `moduleName`, and relative script path.
- Uses kapt or annotation processing; validate with assemble when generated sources may be affected.

## Dependency Boundary

Project dependencies:

- `:lib_activityresult`
- `:lib_adapter`
- `:lib_application`
- `:lib_arch`
- `:lib_effect`
- `:lib_fileprovider`
- `:lib_http`
- `:lib_ktx`
- `:lib_log`
- `:lib_permission`
- `:lib_rxjava`
- `:lib_snackbar`
- `:lib_utils`
- `:lib_view`
- `:lib_view_gradient`
- `:lib_view_ratio`
- `:lib_view_round`
- `:lib_viewbinding`

Rules:

- Keep changes inside this module unless callers, demos, resources, Manifest, or published API require synchronized updates.
- Use `api` only when the dependency is part of this module public API; otherwise prefer `implementation`.
- Demo/sample modules verify usage and must not become required by library modules.
- This aggregate module should expose core libraries consistently; update it when core published modules are added or removed.

## Verification

- 优先运行 `./gradlew :libs:assembleDebug`；任务不存在时退回 `./gradlew :libs:assembleDebug`。
- If public API changes, also compile the closest direct callers or the affected demo/sample module.
- For publishing changes, inspect generated POM/dependency exposure before release.

## Risk Notes

- Check permissions, device capability, and Android version behavior.
- Check network errors, retries, cancellation, and threading.
- Check UI rendering, RecyclerView/list performance, and resource compatibility.
- Check file URI exposure, media loading, and host app integration.
