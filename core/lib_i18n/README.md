# lib_i18n

`lib_i18n` 是项目内的应用内多语言模块，主包名为 `com.xxf.android.i18n`。它基于 AndroidX AppCompat 的 per-app language 能力，额外补充了语言缓存、启动恢复、全局资源读取和 HTTP `Accept-Language` 生成。

## 1. 结论

普通业务接入时只需要关注三件事：

1. 业务模块显式依赖 `lib_i18n`。
2. 应用启动早期调用 `restoreCachedAppLanguage()`。
3. 用户切换语言时调用 `setAppLanguage(...)`、设置 `appLanguage`，或调用 `resetAppLanguage()` 恢复跟随系统。

普通业务 Activity 不需要默认覆写 `attachBaseContext`。

### Demo

模块内提供独立 demo：`lib_i18n:demo`。

Demo 页面会演示：

- `setAppLanguage(...)`
- `resetAppLanguage()`
- `restoreCachedAppLanguage()`
- `getLocalizedString(...)`
- `Int.resString()`
- `getLocalizedStringArray(...)`
- `getLocalizedQuantityString(...)`

Kotlin import 使用新主包名：

```kotlin
import com.xxf.android.i18n.getLocalizedString
import com.xxf.android.i18n.resString
import com.xxf.android.i18n.restoreCachedAppLanguage
import com.xxf.android.i18n.setAppLanguage
```

## 2. 依赖要求

### 必须依赖

- `androidx.appcompat:appcompat:1.6.0+`
  - 必须。`AppCompatDelegate.setApplicationLocales(...)` 从 AppCompat 1.6.0 开始提供。
  - 当前模块固定依赖 `androidx.appcompat:appcompat:1.6.1`。
- `androidx.core:core-ktx`
  - 当前模块依赖它来使用 `LocaleListCompat` 等兼容 API。
- `lib_preferences`
  - 当前模块使用 `SharedPreferencesOwner` 缓存语言 tag。

### Activity 继承要求

业务页面需要继承 `AppCompatActivity` 或其子类。项目里的 `XXFActivity` 已满足这一点：

```kotlin
class MainActivity : XXFActivity()
```

或：

```kotlin
class MainActivity : AppCompatActivity()
```

## 3. Android 版本行为

### Android 13+

- 系统原生支持 per-app language。
- AppCompatDelegate 可以和系统“应用语言”能力配合。
- 用户可能在系统设置中修改应用语言。

### Android 12 及以下

- 系统没有“应用语言”入口。
- `AppCompatDelegate.setApplicationLocales(...)` 仍然可以在 `AppCompatActivity` 体系内做应用内语言切换。
- `applicationContext.getString(...)`、`applicationContext.resources.getStringArray(...)` 等全局资源读取不保证自动按 AppCompat 语言返回。
- Application、Service、Worker、BroadcastReceiver、通知、Toast、全局工具类等场景请使用 `getLocalizedXxx(...)` 或 `localizedContext()`。

## 4. 方案对比

| 方案 | 优点 | 缺点 | 适用场景 |
| --- | --- | --- | --- |
| `lib_i18n` 当前封装 | 基于 AppCompat 官方兼容能力；支持 Android 12 及以下应用内切换；有 `SharedPreferencesOwner` 缓存；提供全局资源 `getLocalizedXxx(...)`；文档和 API 收敛在独立模块 | 业务需要显式依赖新模块；启动早期要调用 `restoreCachedAppLanguage()`；非 UI 全局资源仍需要改用封装 API | 当前项目推荐方案 |
| 纯 `AppCompatDelegate.setApplicationLocales(...)` | 官方标准 API；Activity/Fragment/View 主链路简单；Android 13+ 可配合系统应用语言 | Android 12 及以下的 `applicationContext` 全局资源不保证自动切换；语言缓存和全局资源读取要业务自己补 | 只关心 UI 页面文本，且全局资源使用很少的 App |
| 旧版手动 `Context.wrap(locale)` / `attachBaseContext` | 全局资源可控；不强依赖 AppCompat 语言实现 | 需要自己维护 `Configuration`、`Locale`、缓存和每个入口；容易漏包 Context；Android 13+ 系统应用语言集成弱 | 不建议继续扩展，仅作为历史方案理解 |
| Android 13+ `LocaleManager` | 系统原生 per-app language；和系统设置集成最好 | 只支持 Android 13+；不能覆盖 Android 12 及以下；仍要处理兼容分支 | 只面向 Android 13+，或作为 AppCompat 内部/补充能力 |

结论：

- 当前项目长期推荐使用 `lib_i18n`。
- 普通 UI 交给 AppCompatDelegate。
- 全局资源读取统一走 `getLocalizedXxx(...)`。
- 不再把默认方案建立在 Activity 全量覆写 `attachBaseContext` 上。

## 5. 推荐接入流程

### 5.1 应用启动恢复语言

在 Application `onCreate` 或首个 Activity `onCreate` 早期调用：

```kotlin
restoreCachedAppLanguage()
```

推荐放在 Application：

```kotlin
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        restoreCachedAppLanguage()
    }
}
```

说明：

- 本模块会把用户选择的语言缓存到 `SharedPreferencesOwner`。
- `restoreCachedAppLanguage()` 会把缓存语言恢复到 AppCompatDelegate。
- 如果没有缓存语言，该方法不会做任何事。

### 5.2 用户切换语言

使用 BCP-47 language tag：

```kotlin
setAppLanguage("zh-CN")
setAppLanguage("en")
setAppLanguage("en-US")
```

也可以直接设置 `Locale`：

```kotlin
appLanguage = Locale.SIMPLIFIED_CHINESE
```

恢复跟随系统语言：

```kotlin
resetAppLanguage()
```

说明：

- 调用 `setAppLanguage(...)` 或设置 `appLanguage` 后，AppCompat 通常会重建当前 `AppCompatActivity`。
- 已经缓存到变量里的字符串不会自动变化，需要重新读取。

## 6. UI 场景用法

Activity、Fragment、View、Adapter 中优先使用当前 UI Context：

```kotlin
getString(R.string.title)
view.context.getString(R.string.title)
```

这些 UI Context 会由 AppCompatDelegate 处理。普通业务不需要额外包装 Activity Context。

不要把 `XXFActivity` 或所有业务 Activity 默认改成：

```kotlin
override fun attachBaseContext(newBase: Context) {
    super.attachBaseContext(newBase.attachLanguage())
}
```

这不是本模块的常规接入方式。

## 7. 全局资源用法

Application、Service、Worker、BroadcastReceiver、通知、Toast、全局工具类等场景不要直接依赖：

```kotlin
application.getString(R.string.title)
application.resources.getStringArray(R.array.tabs)
```

应该使用本模块提供的 localized resource API：

```kotlin
application.getLocalizedString(R.string.title)
application.getLocalizedString(R.string.message, name)
application.getLocalizedStringArray(R.array.tabs)
application.getLocalizedTextArray(R.array.labels)
application.getLocalizedQuantityString(R.plurals.items, count, count)
```

也可以先获取 localized context：

```kotlin
val localizedContext = context.localizedContext()
localizedContext.getString(R.string.title)
localizedContext.resources.getStringArray(R.array.tabs)
```

### 已封装的资源读取方法

- `getLocalizedString(...)`
- `getLocalizedText(...)`
- `getLocalizedStringArray(...)`
- `getLocalizedTextArray(...)`
- `getLocalizedQuantityString(...)`
- `getLocalizedIntArray(...)`
- `getLocalizedBoolean(...)`
- `getLocalizedInteger(...)`
- `getLocalizedDimension(...)`
- `getLocalizedDimensionPixelSize(...)`
- `getLocalizedDimensionPixelOffset(...)`
- `getLocalizedColor(...)`
- `getLocalizedColorStateList(...)`
- `getLocalizedDrawable(...)`
- `getLocalizedFont(...)`
- `getLocalizedXml(...)`
- `openLocalizedRawResource(...)`
- `getLocalizedFraction(...)`

### Int 资源扩展

如果业务更喜欢 `Int.resXxx()` 的调用方式，可以直接使用 `ResourceExt.kt`：

```kotlin
import com.xxf.android.i18n.resString
import com.xxf.android.i18n.resDrawable
import com.xxf.android.i18n.resColor
```

示例：

```kotlin
val title = R.string.app_name.resString()
val icon = R.drawable.ic_launcher.resDrawable()
val color = R.color.primary.resColor()
```

说明：

- 这些方法没有使用 `@StringRes`、`@ArrayRes` 等官方注解限制参数，方便业务在封装层、动态资源 ID、跨模块资源场景中使用。
- 调用方仍然需要传入正确类型的资源 ID，否则 Android Resources 会按原生行为抛异常。
- 性能上，`getLocalizedXxx(...)` 会复用 `localizedContext()`；如果当前 Context 已经匹配应用语言，会直接使用当前 Context；如果不匹配，会按语言 tag 缓存一个 application 级 localized Context，不会每次都创建新的 Configuration Context。

## 8. 高级 Context API

`attachLanguage()` 是高级/特殊场景 API，不是 Activity 的默认必接入口。

仅在以下场景考虑使用：

- 页面不是 `AppCompatActivity`。
- 第三方页面或特殊 Context 链路没有走 AppCompatDelegate。
- Service、Receiver 等特殊入口明确需要一个 `ContextWrapper`。
- 业务确认 `localizedContext()` 或 `getLocalizedXxx(...)` 无法覆盖当前场景。

示例：

```kotlin
override fun attachBaseContext(newBase: Context) {
    super.attachBaseContext(newBase.attachLanguage())
}
```

优先级建议：

1. UI 页面：优先依赖 AppCompatDelegate。
2. 全局资源读取：优先使用 `getLocalizedXxx(...)`。
3. 需要传递 Context：使用 `localizedContext()`。
4. 明确需要 ContextWrapper：最后再使用 `attachLanguage()`。

## 9. HTTP Accept-Language

```kotlin
val header = acceptedLanguage
```

规则：

- 如果应用内指定了语言，优先使用应用语言。
- 如果没有指定应用语言，使用系统首选语言列表。

## 10. API 说明

### 语言状态

- `systemLanguage`：当前系统语言。
- `appLanguage`：当前应用语言；未指定应用语言时返回系统语言。
- `appLanguages`：当前应用语言列表。
- `cachedAppLanguages`：本模块通过 `SharedPreferencesOwner` 缓存的语言列表。

### 语言操作

- `setAppLanguage(languageTag)`：按 BCP-47 language tag 设置应用语言。
- `resetAppLanguage()`：清除应用语言，恢复跟随系统。
- `restoreCachedAppLanguage()`：从本模块缓存恢复 AppCompat 语言。

### Context 和资源

- `localizedContext()`：创建按当前应用语言读取资源的 Context。
- `attachLanguage()`：高级 API，按当前应用语言包装 Context。
- `wrap(locale)`：使用指定 Locale 临时包装 Context，不修改当前应用语言缓存。
- `getLocalizedXxx(...)`：按当前应用语言读取全局资源。

### 网络

- `acceptedLanguages`：当前可接受语言列表。
- `acceptedLanguage`：HTTP `Accept-Language` 请求头值。

## 11. 发布和迁移注意

- 旧版 `MultiLanguage.kt` 已从 `lib_ktx` 移到独立模块，业务需要显式依赖 `lib_i18n`。
- `lib_ktx` 没有继续 `api` 导出本模块，因为会形成依赖环：`lib_i18n -> lib_preferences -> lib_application -> lib_ktx -> lib_i18n`。
- 本模块是新设计，不处理旧版 `longan_language` / `longan_country` 缓存迁移。
- 如果业务在启动期缓存了字符串、数组、通知文案等语言敏感数据，切换语言后需要重新读取。
