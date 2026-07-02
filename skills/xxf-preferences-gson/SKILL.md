---
name: xxf-preferences-gson
description: :lib_preferencesGson 的外部接入说明。用于指导 Maven 消费方如何为偏好委托补充 Gson 序列化能力。
---

# :lib_preferencesGson

## What It Provides

`lib_preferencesGson` adds `useGson()` to the preferences delegate chain so custom objects can be stored as JSON strings.

## Required Dependency

This extension builds on `lib_preferences`.

## Core API

- `PrefsDelegate<P, out V>.useGson()`

## Basic Usage

```kotlin
data class User(val name: String? = null)

object AppPreferences : SharedPreferencesOwner {
    var user: User by preferencesBinding("user", User()).useGson()
}
```

`null` and `JsonNull` clear the stored value.
