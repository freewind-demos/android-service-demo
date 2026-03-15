# Android Service 服务演示

## 简介

本 Demo 演示 Android Service 的基本用法，展示如何在后台运行任务而不阻塞主线程。

## 基本原理

Service 是 Android 四大组件之一，专门用于在后台执行长时间运行的任务。它不提供用户界面，运行在主线程（UI线程）中，但可以在后台执行耗时操作。

Service 有两种启动方式：

1. **startService()** - 启动后自行运行，直到调用 stopSelf() 或 stopService()
2. **bindService()** - 绑定到组件，可以进行交互

Service 生命周期：
- onCreate() - 创建服务
- onStartCommand() - 每次启动时调用
- onBind() - 绑定时调用（返回 IBinder）
- onDestroy() - 服务销毁时调用

## 启动和使用

### 环境要求
- Android Studio
- JDK 17
- Gradle 8.x

### 安装和运行

1. 用 Android Studio 打开项目
2. 连接 Android 设备或模拟器
3. 点击 Run 运行

### 使用方法
- 点击"启动服务"按钮启动后台服务
- 点击"停止服务"按钮停止服务
- 可以在 Logcat 中查看服务日志

## 教程

### 什么是 Service？

Service 是 Android 中用于在后台执行任务的组件。与 Activity 不同，Service 不提供 UI，在用户切换到其他应用时仍然可以继续运行。

Service 适用于以下场景：
- 播放音乐
- 上传/下载文件
- 执行网络请求
- 定时任务

### 为什么需要 Service？

在 Android 中，主线程（UI线程）负责处理用户交互。如果在主线程执行耗时操作（如网络请求、文件读写），会导致界面卡顿甚至ANR（Application Not Responding）错误。

Service 让我们可以将耗时操作放到后台执行，但需要注意：
- Service 默认在主线程运行，仍需手动创建子线程
- 对于真正的后台任务，应结合 WorkManager 或 JobIntentService 使用

### 创建第一个 Service

定义一个类继承 Service，并重写必要的方法：

```kotlin
class MyService : Service() {
    // 绑定时调用，返回 null 表示不绑定
    override fun onBind(intent: Intent?): IBinder? = null

    // 每次启动 Service 时调用
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // 在这里执行后台任务
        return START_STICKY  // 如果被杀死，会重新创建
    }

    // Service 销毁时调用
    override fun onDestroy() {
        super.onDestroy()
    }
}
```

### 在 AndroidManifest 中注册

所有 Service 都必须在 AndroidManifest.xml 中声明：

```xml
<application ...>
    <service
        android:name=".MyService"
        android:enabled="true"
        android:exported="false" />
</application>
```

### 启动和停止 Service

在 Activity 中通过 Intent 启动和停止服务：

```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 启动服务
        findViewById<Button>(R.id.startBtn).setOnClickListener {
            startService(Intent(this, MyService::class.java))
        }

        // 停止服务
        findViewById<Button>(R.id.stopBtn).setOnClickListener {
            stopService(Intent(this, MyService::class.java))
        }
    }
}
```

### 返回值的含义

onStartCommand() 的返回值决定了 Service 被杀死后的行为：

- **START_NOT_STICKY** - 被杀死后不自动重启
- **START_STICKY** - 被杀死后会重启，但不会传递之前的 Intent
- **START_REDELIVER_INTENT** - 被杀死后会重启，并重新传递之前的 Intent

### 注意事项

1. Service 不会自动创建子线程，如果执行耗时操作需要自行创建线程
2. 从 Android 8.0 起，后台 Service 有诸多限制，应使用前台 Service
3. 前台 Service 需要在通知栏显示通知
4. 使用完 Service 后要及时停止，避免资源浪费

## 关键代码详解

### MyService.kt

```kotlin
class MyService : Service() {
    // 绑定时调用，返回 IBinder 用于与组件通信
    // 返回 null 表示不提供绑定服务
    override fun onBind(intent: Intent?): IBinder? = null

    // 每次调用 startService() 时触发
    // flags: 启动标志，startId: 启动ID
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // START_STICKY: 如果服务被系统杀死，会自动重启
        return START_STICKY
    }

    // 服务销毁时调用，用于清理资源
    override fun onDestroy() {
        super.onDestroy()
    }
}
```

### MainActivity.kt

```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 启动 Service：创建 Intent，指定要启动的 Service 类
        findViewById<Button>(R.id.startBtn).setOnClickListener {
            startService(Intent(this, MyService::class.java))
        }

        // 停止 Service：传入相同的 Intent 来停止
        findViewById<Button>(R.id.stopBtn).setOnClickListener {
            stopService(Intent(this, MyService::class.java))
        }
    }
}
```
