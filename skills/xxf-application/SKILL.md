---
name: xxf-application
description: :lib_application 的外部接入说明，面向 Maven 消费方。
---

# :lib_application

## What It Provides

`lib_application` provides application and context helpers. Use the published Maven artifact from your app or feature module.

## Dependencies

- `lib_ktx`

## Basic Usage

Use the public APIs exposed by this artifact. Keep the dependency on the published module only; do not rely on repository-internal build commands or local source paths.
