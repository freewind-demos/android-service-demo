# Android Service 服务演示

## 简介

本 Demo 演示 Android Service 的基本用法。

## 基本原理

Service 是后台运行的组件，不提供 UI。

## 教程

```kotlin
startService(Intent(this, MyService::class.java))
stopService(Intent(this, MyService::class.java))
```
