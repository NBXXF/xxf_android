---
name: xxf-aaa-coding-arch
description: xxf_android 架构约束。用于判断 Android library 模块边界、依赖方向、公共 API、optional 模块、聚合 libs 和 demo/sample 关系。
---

# Android 架构约束

## 模块边界

- 只有可发布 library 模块建立 installable skill；demo/app/sample 只作为验证入口写入对应 library skill。
- 核心库不得依赖 `optional/*`，可选能力通过 optional library 单独发布。
- `libs` 是核心聚合库；新增/移除核心发布库时检查 `libs/build.gradle`。
- demo/sample 不反向成为 library 的依赖，也不定义公共 API。

## 依赖方向

- 公共 API 暴露的类型才使用 `api`。
- 内部实现使用 `implementation`。
- 宿主可选能力、三方 SDK、图片引擎、MLKit、FFmpeg、微信 SDK 等优先 `compileOnly`。
- 禁止为了 demo 方便把 optional 或 sample 依赖塞进核心库。

## 发布边界

- 发布库必须保持 `publishVersion`、`publishGroup`、`moduleName` 和 `publish_maven.gradle` 路径正确。
- 变更 Maven 坐标、依赖暴露、Manifest provider、资源名前缀时必须进入风险门禁。
