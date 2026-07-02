---
name: xxf-view
description: :lib_view 的外部接入说明，面向 Maven 消费方。
---

# :lib_view

## What It Provides

`lib_view` provides view helpers. Use the published Maven artifact from your app or feature module.

## Dependencies

- `lib_adapter`
- `lib_fileprovider`
- `lib_utils`
- `lib_ktx`
- `lib_arch`
- `lib_application`

## Basic Usage

Use the public APIs exposed by this artifact. Keep the dependency on the published module only; do not rely on repository-internal build commands or local source paths.
