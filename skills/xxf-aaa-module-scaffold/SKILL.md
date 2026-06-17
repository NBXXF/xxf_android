---
name: xxf-aaa-module-scaffold
description: 为 xxf_android 新增或拆分 Android library/app/demo 模块时使用。覆盖 settings.gradle、build.gradle、发布配置、聚合 libs 和 demo 验证约定。
---

# Android 模块脚手架

## 新增库模块

1. 在 `settings.gradle` include 新模块。
2. 创建 `build.gradle`，优先沿用邻近模块配置：
   - `com.android.library`
   - `kotlin-android`
   - 需要注解处理才加 `kotlin-kapt`
   - `compileSdk project.COMPILE_SDK_VERSION.toInteger()`
   - `minSdkVersion project.MIN_SDK_VERSION.toInteger()`
   - `targetSdkVersion project.TARGET_SDK_VERSION.toInteger()`
   - Java/Kotlin target 1.8
3. 设置唯一 `namespace`。
4. 发布库补齐 `ext` 发布字段并按层级 `apply from: '../publish_maven.gradle'`。
5. 如属于核心聚合能力，同步 `libs/build.gradle`。
6. 加 demo/sample 时放在模块下或 `optional` 对应目录，demo 不发布。

## 依赖规则

- 公共 API 暴露的依赖才用 `api`。
- 内部实现依赖用 `implementation`。
- 可选宿主依赖、图片引擎、MLKit、FFmpeg、微信 SDK 等优先 `compileOnly`，避免强绑宿主。
- `optional/*` 不应反向污染核心库。

## 验证

新模块至少运行：

```bash
./gradlew :new_module:assembleDebug
```

如果加入 `libs`，再运行：

```bash
./gradlew :libs:assembleDebug
```
