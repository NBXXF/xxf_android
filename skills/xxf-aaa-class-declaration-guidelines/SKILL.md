---
name: xxf-aaa-class-declaration-guidelines
description: xxf_android 类型声明与文件组织规则。修改 Activity、Fragment、View、Adapter、ViewModel、Manager、Provider、扩展工具类时使用。
---

# 类型声明规则

## 文件组织

- 一个 Kotlin/Java 文件优先承载一个主类型；避免把多个无关类塞进 `Utils` 或 `Manager`。
- 文件名与主类型同名；扩展函数按接收者或职责命名，例如 `ViewExt.kt`、`FragmentExt.kt`。
- Android 组件按职责拆分：UI 初始化、事件、数据绑定、权限回调、列表适配器不要混成超长文件。

## 类型设计

- Activity/Fragment 只做页面编排，业务逻辑下沉到可测试类或现有架构层。
- View/Adapter 不持有长生命周期 Context；回调要可解绑。
- 单例必须明确线程安全、初始化时机和是否持有 Context。
- Provider/SDK 适配类只暴露项目协议或封装 API，不把三方 SDK 类型扩散到核心模块公共 API，除非模块本身就是 provider。

## 兼容性

- 公共类、方法、构造参数变更要检查调用方和 Maven 二进制兼容。
- Java 调用 Kotlin API 时注意 `@JvmStatic`、`@JvmOverloads`、nullable 注解和默认参数兼容。
