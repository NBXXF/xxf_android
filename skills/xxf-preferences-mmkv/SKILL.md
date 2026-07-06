---
name: xxf-preferences-mmkv
description: :lib_preferencesMMKV 的外部接入说明。用于指导 Maven 消费方如何用 MMKV 作为偏好存储后端。
---

# :lib_preferencesMMKV

## What It Provides

`lib_preferencesMMKV` provides `MMKVPreferencesOwner` for MMKV-backed key-value storage.

## Required Dependency

This extension builds on `lib_preferences`.

## Core APIs

- `MMKVPreferencesOwner`

## Basic Usage

```kotlin
object AppPreferences : MMKVPreferencesOwner {
    var token: String by preferencesBinding("token", "")
}
```
