---
name: xxf-aaa-test-strategy
description: 为 xxf_android 选择最小有效测试和 Gradle 验证范围。用于 Kotlin/Java/Android 资源/Gradle 改动后的验证决策。
---

# 测试与验证策略

## 最小验证

- 单模块 Kotlin 改动：`./gradlew :module:compileDebugKotlin`
- Java 或 Android 资源改动：`./gradlew :module:assembleDebug`
- app/demo/sample 改动：运行对应 assemble
- 注解处理、ObjectBox、Room、Glide kapt 改动：运行对应 kapt/assemble，不只跑 compileKotlin
- 公共 API 改动：同时验证直接依赖方或 `:libs:assembleDebug`

## 必须扩大范围

- 改动 `lib_arch`、`lib_application`、`lib_ktx`、`lib_utils`、`lib_rxjava`、`lib_view`、`lib_permission` 等基础层时，检查上层依赖或聚合库。
- 改动 `optional/lib_album`、`optional/lib_camera_wechat`、`optional/lib_qrcode`、`optional/lib_mlkit` 等设备能力模块时，至少跑模块 assemble；涉及实际设备能力时说明未做真机验证。
- 改动下载、网络、缓存、文件路径、权限时补充边界用例或手工验证说明。

## 无法验证时

说明阻塞原因，例如私有 Maven 凭证、网络、签名、设备/模拟器缺失，并给出已完成的静态检查或编译范围。
