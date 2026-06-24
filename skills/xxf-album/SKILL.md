---
name: xxf-album
description: :lib_album 的外部接入说明，面向 Maven 消费方。
---

# :lib_album

## What It Provides

`lib_album` provides album and picker helpers. Use the published Maven artifact from your app or feature module.

## Dependencies

- `lib_permission`
- `lib_fileprovider`
- `lib_utils`
- `lib_rxjava`

## Basic Usage

Use the public APIs exposed by this artifact. Keep the dependency on the published module only; do not rely on repository-internal build commands or local source paths.
