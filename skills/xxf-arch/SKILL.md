---
name: xxf-arch
description: :lib_arch 的外部接入说明，面向 Maven 消费方。
---

# :lib_arch

## What It Provides

`lib_arch` provides base architecture helpers. Use the published Maven artifact from your app or feature module.

## Dependencies

- `lib_application`
- `lib_http`
- `lib_utils`
- `lib_log`
- `lib_permission`
- `lib_snackbar`
- `lib_view_round`

## Basic Usage

Use the public APIs exposed by this artifact. Keep the dependency on the published module only; do not rely on repository-internal build commands or local source paths.
