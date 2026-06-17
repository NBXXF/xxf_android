---
name: xxf-camera-wechat
description: :optional:lib_camera_wechat 模块规则（optional library，当前 settings.gradle 启用）。修改该模块、调用方、依赖或验证入口时使用。
---

# :optional:lib_camera_wechat

## Scope

- Gradle path: `:optional:lib_camera_wechat`
- Directory: `optional/lib_camera_wechat`
- Status: enabled in settings.gradle
- Type: optional library
- Namespace: `com.xxf.camera.wechat`
- Plugins: `com.android.library, kotlin-android, kotlin-kapt`
- Build features touched in Gradle: `viewBinding, dataBinding, buildConfig`
- Published with `publish_maven.gradle`; preserve `publishVersion`, `publishGroup`, `moduleName`, and relative script path.
- Uses kapt or annotation processing; validate with assemble when generated sources may be affected.

## Dependency Boundary

Project dependencies:

- `:lib_permission`

Rules:

- Keep changes inside this module unless callers, demos, resources, Manifest, or published API require synchronized updates.
- Use `api` only when the dependency is part of this module public API; otherwise prefer `implementation`.
- Demo/sample modules verify usage and must not become required by library modules.
- This is an optional module; avoid introducing dependencies from core modules back into optional features.

## Verification

- If behavior changes are user-visible, also assemble the related demo/sample module listed below.
- 优先运行 `./gradlew :optional:lib_camera_wechat:assembleDebug`；任务不存在时退回 `./gradlew :optional:lib_camera_wechat:assembleDebug`。
- If public API changes, also compile the closest direct callers or the affected demo/sample module.
- For publishing changes, inspect generated POM/dependency exposure before release.

## Risk Notes

- Check permissions, device capability, and Android version behavior.
- Check network errors, retries, cancellation, and threading.
- Check UI rendering, RecyclerView/list performance, and resource compatibility.

## Related Demo / Sample Modules

- `:optional:lib_camera_wechat:camera_demo` at `optional/lib_camera_wechat/camera_demo` is for verification/example only; do not expose it as an installable skill.

