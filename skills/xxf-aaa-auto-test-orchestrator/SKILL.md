---
name: xxf-aaa-auto-test-orchestrator
description: 发现并执行 xxf_android 当前改动最相关的 Gradle 验证，优先最小范围，汇总失败根因与残余风险。
---

# Gradle 验证编排

## 默认行为

1. 读取受影响 library skill 的 Verification 与 Related Demo / Sample Modules。
2. 运行前用 `./gradlew tasks --all` 或失败日志确认任务真实存在；模块 skill 可能因工程调整滞后。
3. 优先运行最小相关命令，不直接全量构建。
4. 失败时定位第一个有效失败，区分当前改动、历史问题和环境问题。
5. 修复或收敛后重新运行同一最小范围。

## 命令选择

- Kotlin library：`./gradlew :module:compileDebugKotlin`
- 资源、Manifest、DataBinding/ViewBinding、注解处理：`./gradlew :module:assembleDebug`
- 单测：`./gradlew :module:testDebugUnitTest`
- 关联 demo：只在对应 library skill 声明的 demo 路径上 assemble。
- 公共 API 或聚合变更：补跑直接依赖方或 `./gradlew :libs:assembleDebug`。
- 仅改 `skills/`、`AGENTS.md`、安装脚本：不跑 Gradle，改做 skill frontmatter、目录名、模块覆盖静态检查。

## 阻塞说明

需要私有 Maven 凭证、网络、签名、设备/模拟器、真实相机/相册/蓝牙/录音能力时，说明阻塞原因和已完成的替代验证。
