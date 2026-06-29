---
name: xxf-aaa-delivery-loop
description: 处理 XXF Android 项目中的通用编码任务交付流程。用于 bugfix、功能开发、重构、回归修复等日常 coding 请求；负责自动串起模块 skill、补测、Gradle 验证、代码审查与风险门禁。
---

# 交付总控

## 触发场景

- 普通 Android/Kotlin/Java/Gradle 编码任务
- bugfix、功能开发、局部重构、回归修复
- 用户没有显式要求测试、review 或风险评估，但改动本身需要闭环

## 默认工作流

1. 读受影响可发布 library 模块对应的 `xxf-*` 模块 skill，定位路径、发布脚本、依赖边界、关联 demo 和验证入口。
2. 读 `xxf-aaa-coding-style` 与 `xxf-aaa-coding-arch`，按本仓库 Kotlin/Java/XML/Gradle 约束实现最小改动。
3. 涉及 Activity/Fragment/View/ViewModel/Adapter 等类型声明时，读 `xxf-aaa-class-declaration-guidelines`。
4. 涉及 VO/DTO/PO/DO/BO/Entity/Query/Command 等领域模型命名时，读 `xxf-aaa-model-naming-guidelines`。
5. 需要判断测试范围时读 `xxf-aaa-test-strategy`；需要补测时读 `xxf-aaa-unit-test-writer`。
6. 完成修改后进入 `xxf-aaa-auto-test-orchestrator`，运行最小相关 Gradle 验证。
7. 改动跨模块、触及公共 API、生命周期、线程、权限、存储、网络、图片、发布配置时，读 `xxf-aaa-code-reviewer` 和 `xxf-aaa-risk-gate`。
8. 根据 Figma/Figama、MasterGo、蓝湖、摹客、截图或设计稿实现/修复 UI 时，读 `xxf-aaa-ui-design-alignment`。
9. 改动触及 UI 渲染、列表、图片、启动、主线程、下载/网络热路径时，读 `xxf-aaa-android-performance-gate`。
10. 方案不确定、结论冲突、技术选型分歧、业务规则无法从代码事实推导，或用户目标与架构/发布/兼容约束冲突时，读 `xxf-aaa-clarify-question`，先让用户抉择，并把决策记录到对应模块的 `vibe-coding-clarify.md`。

## 验证优先级

- 单库 Kotlin 改动：优先 `./gradlew :module:compileDebugKotlin`
- Java/Android 资源改动：优先 `./gradlew :module:assembleDebug`
- 关联 demo 改动：不建立独立 skill，按所属 library skill 中的 Related Demo / Sample Modules 执行 assemble
- 单测存在时：优先对应 `testDebugUnitTest`
- 发布脚本、POM、混淆、依赖暴露变更：至少跑受影响库 assemble，并检查 `publish_maven.gradle`/`ext` 配置

## 升级澄清条件

- 需要私有 Maven 凭证、签名、生产服务、真实设备能力且无法替代
- 是否正确属于产品/业务规则而非代码事实
- 改动会影响多个公开库的 API 兼容性或发布坐标
- 存在多个可行实现，且选择会影响 API、兼容性、发布、依赖方向、验证范围或后续维护成本
- 用户目标与现有架构边界、optional 模块拆分、隐私权限、性能要求或发布策略冲突

进入澄清时，按 `xxf-aaa-clarify-question` 输出用户决策 prompt；用户确认后写入对应模块的 `vibe-coding-clarify.md` 再继续实现。

## 输出要求

最终说明：改了什么、跑了什么验证、是否补测、主要残余风险。
