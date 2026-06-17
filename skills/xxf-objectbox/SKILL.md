---
name: xxf-objectbox
description: :optional:lib_objectbox 模块规则（optional library，当前 settings.gradle 启用）。修改该模块、调用方、依赖或验证入口时使用。
---

# :optional:lib_objectbox

## Scope

- Gradle path: `:optional:lib_objectbox`
- Directory: `optional/lib_objectbox`
- Status: enabled in settings.gradle
- Type: optional library
- Namespace: `com.xxf.objectbox`
- Plugins: `com.android.library, kotlin-android, kotlin-kapt, io.objectbox`
- Build features touched in Gradle: `buildConfig`
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

- If behavior changes are user-visible, also assemble the related demo/sample module listed below.
- 优先运行 `./gradlew :optional:lib_objectbox:compileDebugKotlin`；任务不存在时退回 `./gradlew :optional:lib_objectbox:assembleDebug`。
- If public API changes, also compile the closest direct callers or the affected demo/sample module.
- For publishing changes, inspect generated POM/dependency exposure before release.

## Risk Notes

- Check database schema, generated code, and migration behavior.

## Related Demo / Sample Modules

- `:optional:lib_objectbox:lib_objectbox_demo` at `optional/lib_objectbox/lib_objectbox_demo` is for verification/example only; do not expose it as an installable skill.

