---
name: xxf-preview
description: :optional:lib_preview 模块规则（optional library，当前 settings.gradle 启用）。修改该模块、调用方、依赖或验证入口时使用。
---

# :optional:lib_preview

## Scope

- Gradle path: `:optional:lib_preview`
- Directory: `optional/lib_preview`
- Status: enabled in settings.gradle
- Type: optional library
- Namespace: `com.xxf.media.preview`
- Plugins: `com.android.library, kotlin-android, kotlin-kapt`
- Build features touched in Gradle: `viewBinding, buildConfig`
- Published with `publish_maven.gradle`; preserve `publishVersion`, `publishGroup`, `moduleName`, and relative script path.
- Uses kapt or annotation processing; validate with assemble when generated sources may be affected.
- Has `compileOnly` dependencies; do not accidentally convert optional host dependencies into transitive runtime dependencies.

## Dependency Boundary

- No project dependencies were detected from `build.gradle`, or the module directory is missing.

Rules:

- Keep changes inside this module unless callers, demos, resources, Manifest, or published API require synchronized updates.
- Use `api` only when the dependency is part of this module public API; otherwise prefer `implementation`.
- Demo/sample modules verify usage and must not become required by library modules.
- This is an optional module; avoid introducing dependencies from core modules back into optional features.

## Verification

- If behavior changes are user-visible, also assemble the related demo/sample module listed below.
- 优先运行 `./gradlew :optional:lib_preview:assembleDebug`；任务不存在时退回 `./gradlew :optional:lib_preview:assembleDebug`。
- For publishing changes, inspect generated POM/dependency exposure before release.

## Risk Notes

- Check UI rendering, RecyclerView/list performance, and resource compatibility.
- Check file URI exposure, media loading, and host app integration.

## Related Demo / Sample Modules

- `:optional:lib_preview:lib_previewer_demo` at `optional/lib_preview/lib_previewer_demo` is for verification/example only; do not expose it as an installable skill.

