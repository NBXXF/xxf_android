---
name: xxf-jbinder-jbinder
description: :optional:jbinder:lib_jbinder 模块规则（optional library，当前 settings.gradle 启用）。修改该模块、调用方、依赖或验证入口时使用。
---

# :optional:jbinder:lib_jbinder

## Scope

- Gradle path: `:optional:jbinder:lib_jbinder`
- Directory: `optional/jbinder/lib_jbinder`
- Status: enabled in settings.gradle
- Type: optional library
- Namespace: `com.xxf.jbinder`
- Plugins: `com.android.library, kotlin-android`
- Published with `publish_maven.gradle`; preserve `publishVersion`, `publishGroup`, `moduleName`, and relative script path.

## Dependency Boundary

- No project dependencies were detected from `build.gradle`, or the module directory is missing.

Rules:

- Keep changes inside this module unless callers, demos, resources, Manifest, or published API require synchronized updates.
- Use `api` only when the dependency is part of this module public API; otherwise prefer `implementation`.
- Demo/sample modules verify usage and must not become required by library modules.
- This is an optional module; avoid introducing dependencies from core modules back into optional features.

## Verification

- 优先运行 `./gradlew :optional:jbinder:lib_jbinder:compileDebugKotlin`；任务不存在时退回 `./gradlew :optional:jbinder:lib_jbinder:assembleDebug`。
- For publishing changes, inspect generated POM/dependency exposure before release.

## Risk Notes

- Check annotation processing, generated code, and IPC compatibility.
