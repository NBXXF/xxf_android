---
name: xxf-blockcanary-blockcanary-android
description: :blockcanary-android 的外部接入说明，面向 Maven 消费方。
---

# :blockcanary-android

## What It Provides

`blockcanary-android` provides BlockCanary runtime. Use the published Maven artifact from your app or feature module.

## Dependencies

- `blockcanary-analyzer`

## Basic Usage

Use the public APIs exposed by this artifact. Keep the dependency on the published module only; do not rely on repository-internal build commands or local source paths.
