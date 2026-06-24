---
name: xxf-preferences
description: :lib_preferences 的外部接入说明。用于指导 Maven 消费方如何使用偏好存储委托与 SharedPreferences owner。
---

# :lib_preferences

## What It Provides

`lib_preferences` 提供一套轻量的键值委托接口，用来把属性直接映射到 `SharedPreferences`。

## Core APIs

- `IPreferencesOwner`
- `SharedPreferencesOwner`
- `CustomPreferencesOwner`
- `PrefsDelegate`
- `preferencesBinding(...)`
- `writeAsync()`
- `observable(...)`

## Basic Usage

```kotlin
object AppPreferences : SharedPreferencesOwner {
    var username: String by preferencesBinding("username", "")
    var isLoggedIn: Boolean by preferencesBinding("is_logged_in", false)
}
```

Use `CustomPreferencesOwner` when you want each owner object to use its own preference file name.

## Notes

- `SharedPreferencesOwner` is the default owner.
- `preferencesBinding(...)` supports primitive values, `Set<String>`, `JSONObject`, and `JSONArray`.
- `writeAsync()` and `observable(...)` can be chained on top of the delegate.
