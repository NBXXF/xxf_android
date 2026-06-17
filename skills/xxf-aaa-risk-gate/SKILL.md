---
name: xxf-aaa-risk-gate
description: 为 xxf_android 改动给出合并/发布风险结论。用于公共库发布、基础模块、权限隐私、网络下载、数据库、图片相机、Gradle 配置等风险判断。
---

# 风险门禁

## 结论等级

- Pass：验证覆盖主要风险，可合并。
- Warn：可合并但有明确残余风险，需要记录或灰度。
- Block：存在未解决 correctness/API/发布/安全风险，不应合并或发布。

## 必查风险

- 发布风险：`publishVersion`、`publishGroup`、`moduleName`、Maven 坐标、依赖暴露变化。
- 兼容风险：minSdk、targetSdk、Android 版本行为差异、二进制/API 兼容。
- 宿主风险：Manifest 合并、资源冲突、ProGuard/R8、ABI、FileProvider authority。
- 隐私权限：相机、相册、存储、定位、录音、剪贴板、设备标识。
- 稳定性：主线程阻塞、内存泄漏、下载/网络重试、数据库迁移、RxJava dispose。

最终给出 `Pass/Warn/Block` 和一句理由。
