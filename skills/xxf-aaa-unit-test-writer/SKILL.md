---
name: xxf-aaa-unit-test-writer
description: 为 xxf_android 改动补最小有效回归测试。用于 Kotlin/Java 逻辑、权限/文件/网络边界、数据转换、线程调度和公共 API 行为变更。
---

# 单测补写

## 何时补测

- bugfix 有明确复现条件。
- 新增或修改逻辑分支、错误处理、数据转换、边界条件。
- 公共 API 行为改变，需要防止调用方回归。
- 权限、文件、下载、网络、数据库、线程调度存在可隔离逻辑。

## 写法

- 优先使用现有测试框架和目录；没有测试入口时先建立最小 `test` 目录，不引入重型新框架。
- Android 框架强依赖逻辑优先抽出纯 Kotlin/Java 可测单元。
- 设备能力、相机、相册、权限弹窗等不可稳定单测的路径，用 demo assemble 或手工验证说明补足。

## 验证

- 单测优先跑 `./gradlew :module:testDebugUnitTest`。
- 如果模块没有单测任务或依赖 Android runtime，退回 `:module:assembleDebug`，并说明未覆盖的运行时风险。
