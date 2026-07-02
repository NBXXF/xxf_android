---
name: xxf-libs
description: :libs 的外部接入说明，面向 Maven 消费方。
---

# :libs

## What It Provides

`libs` provides aggregate core library bundle. Use the published Maven artifact from your app or feature module.

## Dependencies

- `lib_activityresult`
- `lib_adapter`
- `lib_application`
- `lib_arch`
- `lib_effect`
- `lib_fileprovider`
- `lib_http`
- `lib_ktx`
- `lib_log`
- `lib_permission`
- `lib_rxjava`
- `lib_snackbar`
- `lib_utils`
- `lib_view`
- `lib_view_gradient`
- `lib_view_ratio`
- `lib_view_round`
- `lib_viewbinding`

## Basic Usage

Use the public APIs exposed by this artifact. Keep the dependency on the published module only; do not rely on repository-internal build commands or local source paths.
