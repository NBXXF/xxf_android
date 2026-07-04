# 语言切换方案对比

本文对比当前项目里的自定义多语言方案，与 Android 官方推荐的 per-app language 方案。

## 现状：当前项目怎么做

当前仓库里已经有一套自定义语言封装，核心在：

- [`core/lib_ktx/src/main/java/com/xxf/ktx/MultiLanguage.kt`](/Users/xxf/Documents/developer/android/work_space/xxf_android/core/lib_ktx/src/main/java/com/xxf/ktx/MultiLanguage.kt)

它的主要做法是：

- 用 `SharedPreferences` 保存当前应用语言
- 用 `appLanguage` 作为全局语言状态
- 通过 `Context.wrap(locale)` / `Context.attachLanguage()` 包装 `Context`
- 手动调用 `Locale.setDefault(locale)` 和 `createConfigurationContext(...)`

## 官方方案：AndroidX per-app language

Android 官方当前推荐的是：

- `AppCompatDelegate.setApplicationLocales(LocaleListCompat)`
- Android 13+ 可同步到系统设置里的“应用语言”
- Android 12 及以下由 AndroidX 做兼容

官方文档：

- [Per-app language preferences](https://developer.android.com/guide/topics/resources/app-languages)
- [AppCompatDelegate API](https://developer.android.com/reference/androidx/appcompat/app/AppCompatDelegate)
- [AppCompat release notes](https://developer.android.com/jetpack/androidx/releases/appcompat)

## 对比结论

| 维度 | 当前自定义方案 | 官方 per-app language |
|---|---|---|
| 代码量 | 中等，自己维护状态、存储、Context 包装 | 更少，直接调用官方 API |
| 接入复杂度 | 需要在 BaseApplication / BaseActivity / Context 链路上手工接入 | 入口更集中，标准化更强 |
| 低版本兼容 | 可以支持 Android 6+，但要自己保证每个入口都包对 Context | Android 6+ 可用，AndroidX 负责兼容 |
| Android 13+ 系统语言菜单 | 不能自动进入系统语言设置 | 支持系统 per-app language |
| 资源同步 | 完全手工管理 | 可配合 AGP 自动生成 `LocaleConfig` |
| 稳定性 | 容易出现漏包 Context、语言状态分叉 | 官方维护，行为一致性更好 |
| 可维护性 | 项目内规则多，后续改动容易漏 | 接口统一，后续维护成本更低 |

## 用法是否更简单

**是，更简单。**

### 当前方案的复杂点

- 要自己维护 `SharedPreferences`
- 要自己维护 `appLanguageCache`
- 要自己决定哪些 `Context` 需要包一层
- 要自己处理 `Activity` 重建、页面刷新、启动恢复
- 要避免网络层、工具类、Service 读取到旧语言

### 官方方案的简化点

- 只需要设置一次应用语言
- AppCompat 统一负责语言状态同步
- Android 13+ 自动和系统设置联动
- 低版本仍然可以复用同一套 API

## 版本限制

### 当前自定义方案

- `minSdk = 23`，所以 Android 5.x 及以下本来就不支持
- Android 6+ 可以用，但需要你自己保证：
  - 每个页面都正确接入 `attachLanguage()`
  - 每个需要语言敏感的 `Context` 都按统一方式创建
  - 重启 / 切后台 / 配置变更后语言状态仍然一致

### 官方方案

- **Android 6 - 12L**
  - 支持应用内切换
  - 不能在系统设置里看到应用语言菜单
- **Android 13+**
  - 支持系统级 per-app language
  - 可以出现在系统设置中
- **`compileSdk < 33`**
  - 不支持 AGP 自动生成 `LocaleConfig`
- **`androidx.appcompat < 1.6`**
  - 不支持 `AppCompatDelegate.setApplicationLocales(...)`

## 健壮性对比

### 当前自定义方案的风险

- 容易出现“部分页面是新语言，部分页面还是旧语言”
- 容易遗漏非 Activity 场景
- 容易和第三方库自己的 locale 逻辑冲突
- 手工维护成本高，回归风险更依赖测试覆盖

### 官方方案的优势

- 语言状态由 AndroidX 统一管理
- 低版本和高版本的行为更一致
- 更容易和系统语言设置打通
- 对新版本 Android 的兼容性更好

## 迁移后的实际约束

迁移到官方方案后，需要接受这些限制：

- 调用 `setApplicationLocales(...)` 通常会触发 `Activity` 重建
- Android 12 及以下需要注意语言存储和初始化时机
- 如果启用自动 per-app language support，项目里不能再手工保留冲突的 `LocaleConfig`
- 如果项目里仍然有自定义语言存储，需要做一次性迁移，不能长期双轨并行

官方文档明确提到：

- 调用 `setApplicationLocales` 可能会重建 `Activity`
- Android 12 及以下建议使用 AndroidX 支持库做兼容
- 自动生成 `LocaleConfig` 需要 `compileSdkVersion 33+`

## 对这个项目的建议

结合当前仓库状态，建议迁移到官方方案，原因是：

- 项目已经是 `AppCompatActivity` 体系
- `compileSdk = 33`，满足自动 per-app language 的基础条件
- 当前方案已经有封装，但没有看到统一接入点，后续容易分叉

如果继续保留自定义方案，建议至少把它收敛成一层薄封装，不要让业务层继续直接操作 `Configuration` / `Locale`。

如果迁移到官方方案，建议保留：

- 语言选择 UI
- 用户选择结果持久化
- `acceptedLanguage` 这类网络请求头逻辑

建议收敛掉：

- `Context.wrap(locale)` 的手工全局注入
- 自己维护的 `appLanguageCache`
- 所有重复的 locale 写入逻辑

## 一句话结论

- **当前方案**：能用，但更依赖人工维护，健壮性一般。
- **官方方案**：更简单、更稳，和系统集成更好。
- **限制**：Android 13+ 才有系统级应用语言菜单；Android 6-12 只能做应用内语言切换。

