# XXF Android 技术中台

xxf架构封装常用组件与用法，符合函数式和流式编程风格。

## 目录

- [特性概览](#特性概览)
- [引入方式](#引入方式)
- [模块总览](#模块总览)
- [核心模块](#核心模块)
  - [lib_application - 应用管理](#1-lib_application---应用管理)
  - [lib_arch - 架构基类](#2-lib_arch---架构基类)
  - [lib_http - 网络请求](#3-lib_http---网络请求)
  - [lib_rxjava - RxJava支持](#4-lib_rxjava---rxjava支持)
  - [lib_activityresult - ActivityResult封装](#5-lib_activityresult---activityresult封装)
  - [lib_permission - 权限请求](#6-lib_permission---权限请求)
  - [lib_adapter - RecyclerView适配器](#7-lib_adapter---recyclerview适配器)
  - [lib_view - 视图组件](#8-lib_view---视图组件)
  - [lib_viewbinding - ViewBinding委托](#9-lib_viewbinding---viewbinding委托)
  - [lib_ktx - Kotlin扩展函数](#10-lib_ktx---kotlin扩展函数)
  - [lib_utils - 工具类库](#11-lib_utils---工具类库)
  - [lib_log - 日志工具](#12-lib_log---日志工具)
  - [lib_fileprovider - 文件共享](#13-lib_fileprovider---文件共享)
  - [lib_snackbar - Snackbar增强](#14-lib_snackbar---snackbar增强)
  - [lib_effect - 动画效果](#15-lib_effect---动画效果)
- [自定义View模块](#自定义view模块)
  - [lib_view_round - 圆角组件](#1-lib_view_round---圆角组件)
  - [lib_view_gradient - 渐变组件](#2-lib_view_gradient---渐变组件)
  - [lib_view_ratio - 比例组件](#3-lib_view_ratio---比例组件)
- [可选模块](#可选模块)
  - [lib_album - 相册选择](#1-lib_album---相册选择)
  - [lib_camera_wechat - 微信相机](#2-lib_camera_wechat---微信相机)
  - [lib_qrcode - 二维码](#3-lib_qrcode---二维码)
  - [lib_glide - 图片加载](#4-lib_glide---图片加载)
  - [lib_preview - 图片预览](#5-lib_preview---图片预览)
  - [lib_download - 文件下载](#6-lib_download---文件下载)
  - [lib_download_m3u8 - M3U8下载](#7-lib_download_m3u8---m3u8下载)
  - [lib_objectbox - 本地数据库](#8-lib_objectbox---本地数据库)
  - [lib_mlkit - ML Kit扫描](#9-lib_mlkit---ml-kit扫描)
  - [lib_pinyin - 拼音转换](#10-lib_pinyin---拼音转换)
  - [lib_wechat - 微信SDK](#11-lib_wechat---微信sdk)
  - [lib_flycoTabLayout - TabLayout](#12-lib_flycotablayout---tablayout)
  - [lib_viewPager - ViewPager](#13-lib_viewpager---viewpager)
  - [lib_draggableView - 可拖拽View](#14-lib_draggableview---可拖拽view)
  - [lib_drouter - 路由框架](#15-lib_drouter---路由框架)
  - [blockcanary - 卡顿检测](#16-blockcanary---卡顿检测)
  - [lib_profileinstaller - 性能优化](#17-lib_profileinstaller---性能优化)
- [效率提升工具](#效率提升工具)

---

## 特性概览

| 特性 | 说明 |
|------|------|
| RxJava 生命周期 | 使用 Android 自带 lifecycle 管理 RxJava 生命周期 |
| ViewModel 支持 | ViewModel 中也可使用 rxjava bind 生命周期 |
| 权限请求 | RxJava 链式调用，内部使用 ActivityResultLauncher 且免注册 |
| startActivityForResult | RxJava 链式调用，内部使用 ActivityResultLauncher 且免注册 |
| HTTP 请求 | 简单配置，支持双缓存和多种策略 |
| 扩展函数 | 400+ Kotlin 扩展函数，开箱即用 |
| 自定义 View | 20+ 常用自定义组件 |
| 架构基类 | XXFActivity/Fragment/Dialog 基类 |
| RecyclerView | 适配器、分割线、拖拽排序一站式解决 |

---

## 引入方式

### 1. 配置仓库

```groovy
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
        jcenter()
        maven { url 'https://maven.aliyun.com/repository/public' }
        maven {
            credentials {
                username '654f4d888f25556ebb4ed790'
                password 'OsVOuR6WZFK='
            }
            url 'https://packages.aliyun.com/maven/repository/2433389-release-RMv0jP/'
        }
        maven {
            credentials {
                username '654f4d888f25556ebb4ed790'
                password 'OsVOuR6WZFK='
            }
            url 'https://packages.aliyun.com/maven/repository/2433389-snapshot-Kqt8ID/'
        }
    }
    configurations.all {
        resolutionStrategy.cacheChangingModulesFor 0, 'seconds'
    }
}
```

### 2. 添加依赖

```groovy
// 核心库聚合（包含所有核心模块）
implementation 'com.NBXXF.xxf_android:libs:5.2.2.1-SNAPSHOT'

// 或单独引入
implementation 'com.NBXXF.xxf_android:lib_arch:版本号'
implementation 'com.NBXXF.xxf_android:lib_adapter:版本号'
// ... 其他模块
```

---

## 模块总览

### 核心模块（libs 聚合包含）

| 模块 | 命名空间 | 说明 |
|------|---------|------|
| `lib_application` | `com.xxf.application` | 应用生命周期、Activity栈管理 |
| `lib_arch` | `com.xxf.arch` | 架构基类、Dialog、Toast、Navigation |
| `lib_http` | `com.xxf.arch.http` | HTTP 网络请求封装 |
| `lib_rxjava` | `com.xxf.rxjava` | RxJava3 + AutoDispose |
| `lib_activityresult` | `com.xxf.activityresult` | ActivityResult API 封装 |
| `lib_permission` | `com.xxf.permission` | 权限申请框架 |
| `lib_adapter` | `com.xxf.adapter` | RecyclerView 适配器框架 |
| `lib_view` | `com.xxf.view` | 自定义视图组件 |
| `lib_viewbinding` | `com.xxf.viewbinding` | ViewBinding 委托 |
| `lib_ktx` | `com.xxf.ktx` | 400+ Kotlin 扩展函数 |
| `lib_utils` | `com.xxf.utils` | 通用工具类库 |
| `lib_log` | `com.xxf.log` | 日志工具 |
| `lib_fileprovider` | `com.xxf.fileprovider` | FileProvider 支持 |
| `lib_snackbar` | `com.xxf.snackbar` | Snackbar 增强 |
| `lib_effect` | `com.xxf.effect` | 动画效果库 |
| `lib_view_round` | `com.xxf.view.round` | 圆角组件 |
| `lib_view_gradient` | `com.xxf.view.gradient` | 渐变组件 |
| `lib_view_ratio` | `com.xxf.view.ratio` | 比例组件 |

### 可选模块（按需引入）

| 模块 | 依赖 | 说明 |
|------|------|------|
| `lib_album` | `com.NBXXF.xxf_android:lib_album:版本号` | 相册选择 |
| `lib_camera_wechat` | `com.NBXXF.xxf_android:lib_camera_wechat:版本号` | 微信风格相机 |
| `lib_qrcode` | `com.NBXXF.xxf_android:lib_qrcode:版本号` | 二维码生成/识别 |
| `lib_glide` | `com.NBXXF.xxf_android:lib_glide:版本号` | Glide 图片加载 |
| `lib_preview` | `com.NBXXF.xxf_android:lib_preview:版本号` | 图片预览 (PhotoView) |
| `lib_download` | `com.NBXXF.xxf_android:lib_download:版本号` | 文件下载 (OkDownload) |
| `lib_download_m3u8` | `com.NBXXF.xxf_android:lib_download_m3u8:版本号` | M3U8 流媒体下载 |
| `lib_objectbox` | `com.NBXXF.xxf_android:lib_objectbox:版本号` | ObjectBox 数据库 |
| `lib_mlkit` | `com.NBXXF.xxf_android:lib_mlkit:版本号` | ML Kit + CameraX |
| `lib_pinyin` | `com.NBXXF.xxf_android:lib_pinyin:版本号` | 拼音转换 |
| `lib_wechat` | `com.NBXXF.xxf_android:lib_wechat:版本号` | 微信 SDK |
| `lib_flycoTabLayout` | `com.NBXXF.xxf_android:lib_flycoTabLayout:版本号` | FlycoTabLayout |
| `lib_viewPager` | `com.NBXXF.xxf_android:lib_viewPager:版本号` | ViewPager 工具 |
| `lib_draggableView` | `com.NBXXF.xxf_android:lib_draggableView:版本号` | 可拖拽 View |
| `lib_drouter` | `com.NBXXF.xxf_android:lib_drouter:版本号` | DRouter 路由 |
| `blockcanary` | `com.NBXXF.xxf_android:blockcanary-android:版本号` | 卡顿检测 |
| `lib_profileinstaller` | `com.NBXXF.xxf_android:lib_profileinstaller:版本号` | AOT 编译优化 |

---

## 核心模块

### 1. lib_application - 应用管理

提供全局访问 Application 和 Activity 的内联函数。

#### API 列表

| 函数/类 | 返回类型 | 说明 |
|---------|---------|------|
| `applicationContext` | `Application` | 全局 Application Context |
| `application` | `Application` | Application 实例 |
| `activityList` | `List<Activity>` | 当前所有 Activity 列表 |
| `topActivity` | `Activity` | 栈顶 Activity（不存在时抛异常） |
| `topActivityOrNull` | `Activity?` | 栈顶 Activity（可为空） |
| `topFragmentActivityOrNull` | `FragmentActivity?` | 栈顶 FragmentActivity |
| `topActivityOrApplication` | `Context` | 栈顶 Activity 或 Application |
| `AndroidActivityStackProvider` | - | Activity 栈管理器 |
| `SimpleActivityLifecycleCallbacks` | - | Activity 生命周期回调简化类 |
| `ViewLifecycleOwner` | - | View 生命周期所有者 |
| `QuickClicksHandler` | - | 快速点击处理器 |

#### 使用示例

```kotlin
// 获取 Application
val app = application

// 获取栈顶 Activity
val activity = topActivityOrNull

// 获取所有 Activity
activityList.forEach { println(it.localClassName) }

// 快速点击处理
QuickClicksHandler.setClickInterval(500) // 设置点击间隔
view.setOnClickListener(QuickClicksHandler.wrap {
    // 防止重复点击
})
```

---

### 2. lib_arch - 架构基类

提供基础的 Activity、Fragment、Dialog 基类及通用组件。

#### 2.1 基类组件

| 类名 | 说明 |
|------|------|
| `XXFActivity` | 基础 Activity 类 |
| `XXFFragment` | 基础 Fragment 类 |
| `XXFDialogFragment` | Dialog Fragment 基类 |
| `XXFBottomSheetDialogFragment` | BottomSheet Dialog 基类 |
| `XXFAlertDialog` | AlertDialog 实现 |
| `XXFBottomSheetDialog` | BottomSheet Dialog 实现 |

#### XXFActivity API

| 方法 | 参数 | 说明 |
|------|------|------|
| `showProgressHUD()` | `message: String?` | 显示加载对话框 |
| `dismissProgressHUD()` | - | 隐藏加载对话框 |
| `bindProgressHud()` | `Observable` 扩展 | 绑定加载对话框到 Observable |

#### 使用示例

```kotlin
class MainActivity : XXFActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 显示加载
        showProgressHUD("加载中...")

        // 绑定到 RxJava
        apiService.getData()
            .bindProgressHud(this)
            .subscribe { data -> }
    }
}

class MyDialog : XXFDialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.dialog_my, container, false)
    }
}
```

#### 2.2 Toast 工具

| 方法 | 参数 | 说明 |
|------|------|------|
| `ToastUtils.showToast()` | `context: Context, message: String` | 显示 Toast |
| `ToastUtils.showToast()` | `context: Context, @StringRes resId: Int` | 显示 Toast |
| `ToastFactory.create()` | `context: Context` | 创建自定义 Toast |

```kotlin
// 显示 Toast
ToastUtils.showToast(context, "操作成功")

// 扩展函数
"操作成功".showToast()
```

#### 2.3 ProgressHUD 加载框

| 方法 | 参数 | 说明 |
|------|------|------|
| `ProgressHUD.show()` | `context: Context, message: String?` | 显示加载框 |
| `ProgressHUD.dismiss()` | - | 隐藏加载框 |
| `Observable.bindProgressHud()` | `owner: LifecycleOwner` | 绑定加载框 |

```kotlin
// 手动显示/隐藏
ProgressHUD.show(context, "加载中...")
ProgressHUD.dismiss()

// 绑定到 Observable
apiService.getData()
    .bindProgressHud(this)
    .subscribe { }
```

#### 2.4 BottomSheet 组件

| 类名 | 说明 |
|------|------|
| `XXFBottomSheetDialogFragment` | BottomSheet DialogFragment |
| `XXFBottomSheetDialog` | BottomSheet Dialog |
| `BottomSheetViewPager` | 支持 BottomSheet 的 ViewPager |
| `BottomSheetBehavior` | 行为控制 |

```kotlin
class MyBottomSheet : XXFBottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 设置展开高度
        behavior?.peekHeight = 300.dp
        behavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }
}
```

#### 2.5 Fragment 分页适配器

| 类名 | 说明 |
|------|------|
| `BaseFragmentPagerAdapter` | FragmentPagerAdapter 基类 |
| `BaseFragmentStatePagerAdapter` | FragmentStatePagerAdapter 基类 |

```kotlin
class MyPagerAdapter(fm: FragmentManager) : BaseFragmentPagerAdapter(fm) {

    private val fragments = listOf(HomeFragment(), ProfileFragment())
    private val titles = listOf("首页", "我的")

    override fun getCount() = fragments.size
    override fun getItem(position: Int) = fragments[position]
    override fun getPageTitle(position: Int) = titles[position]
}
```

#### 2.6 应用前后台监听

```kotlin
// 注册监听
application.registerActivityLifecycleCallbacks(
    AppBackgroundLifecycleCallbacks { isBackground ->
        if (isBackground) {
            // 应用进入后台
        } else {
            // 应用回到前台
        }
    }
)
```

---

### 3. lib_http - 网络请求

基于 Retrofit 扩展，使用注解配置 baseUrl、拦截器、RxJava 适配和缓存策略。

#### 3.1 类级别配置

| 注解 | 参数 | 说明 |
|------|------|------|
| `@BaseUrl` | `value: String` | 设置基础 URL |
| `@RxHttpCacheConfig` | `value: Class` | 设置缓存目录提供者 |
| `@Interceptor` | `value: Class[]` | 声明 OkHttp 拦截器 |
| `@RxJavaInterceptor` | `value: Class` | 声明 RxJava 拦截器 |

#### 3.2 缓存配置

缓存配置分两类：缓存类型和缓存时间。两者可以组合使用。

| 配置 | 位置 | 说明 |
|------|------|------|
| `@Tag CacheType cacheType` | 参数 | 动态设置缓存类型 |
| `@RxHttpCache(CacheType.xxx)` | 方法 | 固定设置缓存类型 |
| `@Headers("cache:5000")` | 方法 | 固定设置缓存时间，单位毫秒 |
| `@Header("cache") long cacheTime` | 参数 | 动态设置缓存时间，单位毫秒 |

#### 3.3 kpower/http 3.0 迁移

kpower/http 3.0 使用 Retrofit 标准 `@Tag` 传递缓存类型：

```java
import retrofit2.CacheType;
import retrofit2.http.Tag;

@GET("user/info")
Observable<UserInfo> getUserInfo(@Tag CacheType cacheType);
```

旧写法 `@Cache CacheType cacheType` 已废弃，不要再导入 `retrofit2.http.Cache`。

#### 3.4 CacheType 缓存模式

| 枚举值 | 说明 |
|--------|------|
| `firstCache` | 先缓存后网络，可能 onNext 两次 |
| `firstRemote` | 先网络，无网络时读缓存 |
| `onlyRemote` | 只从服务器获取 |
| `onlyCache` | 只从缓存获取 |
| `ifCache` | 有缓存返回缓存，否则返回网络 |
| `lastCache` | 返回上次缓存，同时更新缓存 |

#### 3.5 完整示例

```java
@BaseUrl("http://api.example.com/")
@RxHttpCacheConfig(DefaultRxHttpCacheDirectoryProvider.class)
@Interceptor({LogInterceptor.class})
public interface ApiService {

    @GET("user/info")
    Observable<UserInfo> getUserInfo();

    @GET("user/info")
    Observable<UserInfo> getUserInfo(@Tag CacheType cacheType);

    @GET("user/info")
    @Headers("cache:5000")
    Observable<UserInfo> getUserInfoWithFixedCacheTime(@Tag CacheType cacheType);

    @GET("user/info")
    Observable<UserInfo> getUserInfoWithDynamicCacheTime(
        @Header("cache") long cacheTime,
        @Tag CacheType cacheType
    );

    @GET("user/info")
    @RxHttpCache(CacheType.ifCache)
    Observable<UserInfo> getUserInfoIfCache();
}
```

#### 3.6 调用方式

```kotlin
// Kotlin 方式
ApiService::class.apiService()
    .getUserInfo()
    .bindProgressHud(this)
    .subscribe { user -> }

// 或
getApiService<ApiService>()
    .getUserInfo(CacheType.firstCache)
    .subscribe { user -> }

// 动态缓存时间, 5000ms
getApiService<ApiService>()
    .getUserInfoWithDynamicCacheTime(5000, CacheType.firstCache)
    .subscribe { user -> }
```

#### 3.7 文件上传

支持 7 种文件类型：`File`、`ByteArray`、`InputStream`、`FileDescriptor`、`ParcelFileDescriptor`、`AssetFileDescriptor`、`Uri`

```kotlin
@POST("upload")
@Multipart
fun uploadFile(
    @Part("file") fileUri: Uri
): Observable<UploadResult>

// 扩展函数
uri.toPart("filename")
file.toPart("filename")
```

---

### 4. lib_rxjava - RxJava支持

RxJava3 集成和 Lifecycle 绑定。

#### API 列表

| 函数 | 说明 |
|------|------|
| `Observable.bindLifecycle(owner)` | 绑定生命周期，自动取消订阅 |
| `Observable.bindProgressHud(owner)` | 绑定加载对话框 |
| `LifecycleOwner.doOnLifecycle()` | 监听生命周期各阶段 |

#### doOnLifecycle 参数

| 参数 | 类型 | 说明 |
|------|------|------|
| `onCreate` | `(() -> Unit)?` | onCreate 回调 |
| `onStart` | `(() -> Unit)?` | onStart 回调 |
| `onResume` | `(() -> Unit)?` | onResume 回调 |
| `onPause` | `(() -> Unit)?` | onPause 回调 |
| `onStop` | `(() -> Unit)?` | onStop 回调 |
| `onDestroy` | `(() -> Unit)?` | onDestroy 回调 |

#### 使用示例

```kotlin
// 绑定生命周期
Observable.interval(1, TimeUnit.SECONDS)
    .bindLifecycle(this)
    .subscribe { count -> Log.d("TAG", "count: $count") }

// 监听生命周期
this.doOnLifecycle(
    onCreate = { Log.d("TAG", "onCreate") },
    onDestroy = { Log.d("TAG", "onDestroy") }
)
```

---

### 5. lib_activityresult - ActivityResult封装

封装 ActivityResult API，提供多种系统功能 Contract。

#### 5.1 核心 API

| 函数 | 返回值 | 说明 |
|------|--------|------|
| `startActivityForResult(intent)` | `Observable<ActivityResult>` | 启动 Activity 获取结果 |
| `startActivityForResult(contract, input)` | `Observable<O>` | 使用 Contract 启动 |

#### 5.2 内置 Contract

| Contract | 功能 | 返回值 |
|----------|------|--------|
| `EnableBluetoothContract` | 启用蓝牙 | `Boolean` |
| `EnableWifiContract` | 启用 WiFi | `Boolean` |
| `EnableLocationContract` | 启用定位 | `Boolean` |
| `EnableNFCContract` | 启用 NFC | `Boolean` |
| `EnableNotificationContract` | 通知权限 | `Boolean` |
| `EnableFileManageContract` | 文件管理权限 | `Boolean` |
| `PickImageFromGalleryContract` | 选择图片 | `Uri?` |
| `PickContentContract` | 选择内容 | `Uri?` |
| `SystemSettingsContract` | 打开系统设置 | `ActivityResult` |

#### 使用示例

```kotlin
// 启动 Activity
startActivityForResult(Intent(this, TargetActivity::class.java))
    .subscribe { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data?.getStringExtra("key")
        }
    }

// 使用内置 Contract
startActivityForResult(EnableBluetoothContract(), Unit)
    .subscribe { enabled ->
        if (enabled) {
            // 蓝牙已启用
        }
    }

// 选择图片
startActivityForResult(PickImageFromGalleryContract(), "image/*")
    .subscribe { uri ->
        uri?.let { imageView.setImageURI(it) }
    }
```

---

### 6. lib_permission - 权限请求

RxJava 链式调用请求权限。

#### API 列表

| 函数 | 参数 | 返回值 | 说明 |
|------|------|--------|------|
| `requestPermission()` | `vararg permission: String` | `Observable<Boolean>` | 请求权限 |
| `requestPermissionForResult()` | `vararg permission: String` | `Observable<Map<String, Boolean>>` | 请求权限详细结果 |
| `checkSelfPermission()` | `permissions: List<String>` | `Boolean` | 检查权限 |
| `checkSelfPermissionForResult()` | `vararg permission: String` | `Map<String, Boolean>` | 检查权限详细 |

#### 使用示例

```kotlin
// 请求单个权限
requestPermission(Manifest.permission.CAMERA)
    .subscribe { granted ->
        if (granted) { /* 已授权 */ }
    }

// 请求多个权限
requestPermission(
    Manifest.permission.CAMERA,
    Manifest.permission.RECORD_AUDIO
).subscribe { allGranted -> }

// 获取详细结果
requestPermissionForResult(
    Manifest.permission.CAMERA,
    Manifest.permission.RECORD_AUDIO
).subscribe { resultMap ->
    val cameraGranted = resultMap[Manifest.permission.CAMERA]
}

// 检查权限
val hasCamera = checkSelfPermission(listOf(Manifest.permission.CAMERA))
```

---

### 7. lib_adapter - RecyclerView适配器

RecyclerView 适配器框架和装饰器。

#### 7.1 适配器类

| 类名 | 说明 |
|------|------|
| `XXFRecyclerAdapter<T, VH>` | 通用 RecyclerView 适配器 |
| `XXFRecyclerListAdapter<T, VH>` | ListAdapter 版本（支持 DiffUtil） |
| `BaseAdapter` | 基础适配器 |
| `MultiViewEntity` | 多类型 Item 支持接口 |

#### XXFRecyclerAdapter API

| 方法 | 参数 | 说明 |
|------|------|------|
| `setData(list)` | `List<T>` | 设置数据 |
| `addData(list)` | `List<T>` | 添加数据 |
| `removeAt(position)` | `Int` | 删除指定位置 |
| `clear()` | - | 清空数据 |
| `setOnItemClickListener()` | `listener` | 设置点击监听 |
| `setOnItemLongClickListener()` | `listener` | 设置长按监听 |

#### 使用示例

```kotlin
class MyAdapter : XXFRecyclerAdapter<User, MyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(user: User) {
            // 绑定数据
        }
    }
}

// 使用
val adapter = MyAdapter()
recyclerView.adapter = adapter
adapter.setData(userList)

adapter.setOnItemClickListener { position, item ->
    // 点击事件
}
```

#### 7.2 装饰器

| 类名 | 说明 |
|------|------|
| `DividerItemDecoration` | 分割线装饰器 |
| `SectionItemDecoration` | 分组装饰器（带 SectionProvider） |
| `SpacingItemDecoration` | 间距装饰器 |

```kotlin
// 分割线
recyclerView.addItemDecoration(
    DividerItemDecoration(context, DividerItemDecoration.VERTICAL).apply {
        setDrawable(ColorDrawable(Color.GRAY))
    }
)

// 间距
recyclerView.addItemDecoration(
    SpacingItemDecoration(
        horizontalSpacing = 8.dp,
        verticalSpacing = 8.dp
    )
)

// 分组
recyclerView.addItemDecoration(
    SectionItemDecoration(object : SectionProvider {
        override fun getSectionTitle(position: Int): String {
            return data[position].category
        }
    })
)
```

#### 7.3 拖拽排序

```kotlin
val dragHelper = DragItemTouchHelper(object : DragItemTouchHelper.Callback() {
    override fun onMove(from: Int, to: Int): Boolean {
        adapter.moveItem(from, to)
        return true
    }

    override fun onSwiped(position: Int) {
        adapter.removeAt(position)
    }
})
dragHelper.attachToRecyclerView(recyclerView)
```

#### 7.4 其他组件

| 组件 | 说明 |
|------|------|
| `AutoFitGridLayoutManager` | 自适应网格布局 |
| `GridPagerSnapHelper` | 网格分页吸附 |
| `EdgeEffectFactory` | 边界效果工厂 |
| `XXFViewSpringAnimAdapter` | Spring 动画适配器 |

---

### 8. lib_view - 视图组件

自定义视图和 UI 组件库。

#### 组件列表

| 组件 | 说明 |
|------|------|
| RecyclerView 工具 | 建立在 lib_adapter 之上 |
| Lottie 动画 | Lottie 集成支持 |
| SmartRefresh | 下拉刷新集成 |
| ViewBinding | ViewBinding 支持 |

---

### 9. lib_viewbinding - ViewBinding委托

通过属性委托简化 ViewBinding 使用。

#### Activity 中使用

| 函数 | 参数 | 说明 |
|------|------|------|
| `viewBinding(viewBinder)` | `(A) -> T` | 使用 bind 方法 |
| `viewBinding(vbFactory, viewProvider)` | 工厂和提供者 | 自定义创建 |
| `viewBinding(onViewDestroyed, viewBinder)` | 带销毁回调 | 可清理资源 |

```kotlin
class MainActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityMainBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding.textView.text = "Hello"
    }
}
```

#### Fragment 中使用

```kotlin
class HomeFragment : Fragment(R.layout.fragment_home) {
    private val binding by viewBinding(FragmentHomeBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button.setOnClickListener { }
    }
}
```

#### 参数绑定委托

```kotlin
// Activity Intent 参数
private val userId: String? by argumentBinding("user_id")
private val pageIndex: Int by argumentBinding("page_index", 0)

// Fragment Arguments 参数
private val userId: String? by argumentBinding("user_id")
```

#### Preferences 委托

```kotlin
object AppPreferences : SharedPreferencesOwner {
    var username: String by preferencesBinding("username", "")
    var isLoggedIn: Boolean by preferencesBinding("is_logged_in", false)

    // 监听变化
    var token: String by preferencesBinding("token", "")
        .observable { _, newValue -> println("Token: $newValue") }

    // 复杂对象
    data class User(val name: String)
    var user: User by preferencesBinding("user", User("")).useGson()
}
```

---

### 10. lib_ktx - Kotlin扩展函数

400+ Kotlin 扩展函数库。

#### 主要扩展分类

| 文件 | 说明 | 示例函数 |
|------|------|---------|
| `Activity.kt` | Activity 扩展 | `finish()`, `startActivity()` |
| `Fragment.kt` | Fragment 扩展 | `requireActivity()`, `findNavController()` |
| `View.kt` | View 扩展 | `visible()`, `gone()`, `onClick()` |
| `Context.kt` | Context 扩展 | `dp`, `sp`, `getColor()` |
| `String.kt` | 字符串扩展 | `isEmail()`, `isMobile()`, `md5()` |
| `Number.kt` | 数字扩展 | `dp`, `sp`, `format()` |
| `Bitmap.kt` | 图片扩展 | `scale()`, `rotate()`, `toBase64()` |
| `Drawable.kt` | Drawable 扩展 | `toBitmap()`, `tint()` |
| `Keyboard.kt` | 键盘扩展 | `showKeyboard()`, `hideKeyboard()` |
| `Network.kt` | 网络扩展 | `isNetworkConnected`, `isWifi` |
| `StatusBar.kt` | 状态栏扩展 | `setStatusBarColor()`, `setLightStatusBar()` |
| `NavigationBar.kt` | 导航栏扩展 | `setNavigationBarColor()` |
| `WebView.kt` | WebView 扩展 | `loadUrl()`, `evaluateJavascript()` |

#### 常用扩展示例

```kotlin
// View 扩展
view.visible()
view.gone()
view.onClick { /* 点击事件 */ }
view.onLongClick { /* 长按事件 */ }

// dp/sp 转换
val padding = 16.dp
val textSize = 14.sp

// 字符串验证
"test@example.com".isEmail()  // true
"13800138000".isMobile()      // true
"password".md5()              // MD5 哈希

// 键盘控制
editText.showKeyboard()
editText.hideKeyboard()

// 网络状态
if (isNetworkConnected) { /* 有网络 */ }
if (isWifi) { /* WiFi 连接 */ }

// 状态栏
setStatusBarColor(Color.WHITE)
setLightStatusBar(true)

// 剪贴板
"复制内容".copyToClipboard()
val text = getClipboardText()
```

---

### 11. lib_utils - 工具类库

通用工具类库。

#### 工具类列表

| 类名 | 说明 |
|------|------|
| `StringUtils` | 字符串处理 |
| `FileUtils` | 文件操作 |
| `BitmapUtils` | 图片处理 |
| `ScreenUtils` | 屏幕信息 |
| `EncodeUtils` | 编码解码 |
| `EncryptUtils` | 加密解密 |
| `RomUtils` | ROM 识别 |
| `ShellUtils` | Shell 命令 |
| `BarUtils` | 状态栏/导航栏 |
| `ResourcesUtil` | 资源管理 |
| `UriUtils` | Uri 处理 |
| `ZipUtils` | 压缩工具 |
| `ProcessUtils` | 进程处理 |
| `RecyclerViewUtils` | RecyclerView 工具 |
| `Luban` | 图片压缩 |

#### 使用示例

```kotlin
// 文件操作
FileUtils.createFile(path)
FileUtils.deleteFile(path)
FileUtils.copyFile(src, dest)
FileUtils.getFileSize(file)

// 加密
EncryptUtils.encryptMD5(data)
EncryptUtils.encryptSHA256(data)
EncryptUtils.encryptAES(data, key)
EncryptUtils.decryptAES(data, key)

// 屏幕信息
val width = ScreenUtils.getScreenWidth()
val height = ScreenUtils.getScreenHeight()
val density = ScreenUtils.getScreenDensity()

// 压缩
ZipUtils.zipFile(srcFile, destFile)
ZipUtils.unzipFile(zipFile, destDir)

// 图片压缩 (Luban)
Luban.with(context)
    .load(file)
    .setTargetDir(targetDir)
    .get()
```

---

### 12. lib_log - 日志工具

日志工具库。

```kotlin
// 日志输出
LogUtils.d("TAG", "Debug message")
LogUtils.i("TAG", "Info message")
LogUtils.w("TAG", "Warning message")
LogUtils.e("TAG", "Error message")
LogUtils.e("TAG", "Error", throwable)

// JSON 格式化输出
LogUtils.json("TAG", jsonString)
```

---

### 13. lib_fileprovider - 文件共享

自动注册 FileProvider，简化 Uri 授权。

```kotlin
// File 转授权 Uri
val uri = file.toAuthorizedUri

// 分享文件
val intent = Intent(Intent.ACTION_SEND).apply {
    type = "image/*"
    putExtra(Intent.EXTRA_STREAM, uri)
    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
}
startActivity(intent)
```

---

### 14. lib_snackbar - Snackbar增强

Snackbar 增强库。

```kotlin
// 显示 Snackbar
Snackbar.make(view, "消息", Snackbar.LENGTH_SHORT).show()

// 带操作
Snackbar.make(view, "已删除", Snackbar.LENGTH_LONG)
    .setAction("撤销") { /* 撤销操作 */ }
    .show()
```

---

### 15. lib_effect - 动画效果

动画和视觉效果库。

#### 效果类型

| 效果 | 说明 |
|------|------|
| 布局动画 | Layout 过渡动画 |
| 过度滚动 | OverScroll 效果 |
| 属性动画 | 各种 Animator |

```kotlin
// 布局动画
val animator = AnimBuilder()
    .setDuration(300)
    .setInterpolator(AccelerateDecelerateInterpolator())
    .build()
view.animate().apply(animator)
```

---

## 自定义View模块

### 1. lib_view_round - 圆角组件

#### 组件列表

| 组件 | 基类 |
|------|------|
| `XXFRoundButton` | Button |
| `XXFRoundTextView` | TextView |
| `XXFRoundEditText` | EditText |
| `XXFRoundImageView` | ImageView |
| `XXFRoundImageButton` | ImageButton |
| `XXFRoundCheckedTextView` | CheckedTextView |
| `XXFRoundRadioButton` | RadioButton |
| `XXFRoundLayout` | FrameLayout |
| `XXFRoundLinearLayout` | LinearLayout |
| `XXFRoundRelativeLayout` | RelativeLayout |
| `XXFRoundConstraintLayout` | ConstraintLayout |
| `XXFRoundCheckedImageView` | ImageView (可选中) |
| `XXFRoundCheckedImageButton` | ImageButton (可选中) |
| `XXFRoundImageTextView` | 图文组合 |

#### XML 属性

| 属性 | 类型 | 说明 |
|------|------|------|
| `app:radius` | dimension | 四角统一圆角（360dp=圆形） |
| `app:topLeftRadius` | dimension | 左上角圆角 |
| `app:topRightRadius` | dimension | 右上角圆角 |
| `app:bottomLeftRadius` | dimension | 左下角圆角 |
| `app:bottomRightRadius` | dimension | 右下角圆角 |

#### 使用示例

```xml
<!-- 圆角按钮 -->
<com.xxf.view.round.XXFRoundTextView
    android:layout_width="100dp"
    android:layout_height="40dp"
    android:background="#FF5722"
    app:radius="8dp" />

<!-- 圆形头像 -->
<com.xxf.view.round.XXFRoundImageView
    android:layout_width="80dp"
    android:layout_height="80dp"
    app:radius="360dp" />

<!-- 顶部圆角卡片 -->
<com.xxf.view.round.XXFRoundLayout
    android:layout_width="match_parent"
    android:layout_height="100dp"
    app:topLeftRadius="16dp"
    app:topRightRadius="16dp"
    app:bottomLeftRadius="0dp"
    app:bottomRightRadius="0dp" />
```

```kotlin
// 代码设置
roundView.setRadius(16f.dp)
```

---

### 2. lib_view_gradient - 渐变组件

#### 组件列表

| 组件 | 基类 |
|------|------|
| `XXFGradientCompatButton` | Button |
| `XXFGradientCompatTextView` | TextView |
| `XXFGradientCompatEditText` | EditText |
| `XXFGradientCompatImageView` | ImageView |
| `XXFGradientCompatCheckedTextView` | CheckedTextView |
| `XXFGradientCompatRadioButton` | RadioButton |
| `XXFGradientFrameLayout` | FrameLayout |
| `XXFGradientLinearLayout` | LinearLayout |
| `XXFGradientRelativeLayout` | RelativeLayout |
| `XXFGradientConstraintLayout` | ConstraintLayout |

#### XML 属性

| 属性 | 类型 | 说明 |
|------|------|------|
| `app:start_color` | color | 起始颜色 |
| `app:center_color` | color | 中间颜色（可选） |
| `app:end_color` | color | 结束颜色 |
| `app:orientation` | enum | 渐变方向 (0-7) |

#### 渐变方向

| 值 | 方向 |
|----|------|
| 0 | TOP_BOTTOM |
| 1 | TR_BL |
| 2 | RIGHT_LEFT |
| 3 | BR_TL |
| 4 | BOTTOM_TOP |
| 5 | BL_TR |
| 6 | LEFT_RIGHT |
| 7 | TL_BR |

#### 使用示例

```xml
<com.xxf.view.gradient.XXFGradientCompatButton
    android:layout_width="200dp"
    android:layout_height="48dp"
    app:start_color="#FF5722"
    app:end_color="#E91E63"
    app:orientation="6" />
```

#### GradientDrawableBuilder

```kotlin
val drawable = GradientDrawableBuilder()
    .setStartColor(Color.parseColor("#FF5722"))
    .setEndColor(Color.parseColor("#E91E63"))
    .setOrientation(GradientDrawable.Orientation.LEFT_RIGHT)
    .setRadiusCornerTopLeft(16f)
    .setRadiusCornerTopRight(16f)
    .build()

view.background = drawable
```

---

### 3. lib_view_ratio - 比例组件

#### 组件列表

| 组件 | 基类 |
|------|------|
| `XXFRatioButton` | Button |
| `XXFRatioTextView` | TextView |
| `XXFRatioEditText` | EditText |
| `XXFRatioImageView` | ImageView |
| `XXFRatioImageButton` | ImageButton |
| `XXFRatioCheckedTextView` | CheckedTextView |
| `XXFRatioRadioButton` | RadioButton |
| `XXFRatioFrameLayout` | FrameLayout |
| `XXFRatioLinearLayout` | LinearLayout |
| `XXFRatioRelativeLayout` | RelativeLayout |

#### XML 属性

| 属性 | 类型 | 说明 |
|------|------|------|
| `app:widthRatio` | float | 宽度比例 |
| `app:heightRatio` | float | 高度比例 |
| `app:aspectRatio` | float | 宽高比（优先级更高） |
| `app:datumRatio` | enum | 基准模式 |

#### datumRatio 基准模式

| 值 | 说明 |
|----|------|
| `datumAuto` (0) | 自动 |
| `datumWidth` (1) | 以宽度为基准 |
| `datumHeight` (2) | 以高度为基准 |

#### 使用示例

```xml
<!-- 16:9 图片 -->
<com.xxf.view.ratio.XXFRatioImageView
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:widthRatio="16"
    app:heightRatio="9"
    app:datumRatio="datumWidth" />

<!-- 1:1 正方形 -->
<com.xxf.view.ratio.XXFRatioImageView
    android:layout_width="100dp"
    android:layout_height="wrap_content"
    app:aspectRatio="1" />
```

```kotlin
ratioView.setRatio(RatioDatumMode.DATUM_WIDTH, 16f, 9f)
ratioView.setAspectRatio(16f / 9f)
```

---

## 可选模块

### 1. lib_album - 相册选择

```groovy
implementation 'com.NBXXF.xxf_android:lib_album:版本号'
```

#### AlbumLauncher API

| 方法 | 说明 |
|------|------|
| `from(activity/fragment)` | 创建启动器 |
| `choose(mimeTypes, exclusive)` | 选择类型 |

#### SelectionCreator 链式 API

| 方法 | 参数 | 说明 |
|------|------|------|
| `countable()` | Boolean | 显示数量 |
| `maxSelectable()` | Int | 最大选择数 |
| `capture()` | Boolean | 允许拍照 |
| `originalEnable()` | Boolean | 原图选项 |
| `maxOriginalSize()` | Int | 原图最大MB |
| `thumbnailScale()` | Float | 缩略图缩放 |
| `imageEngine()` | ImageEngine | 图片引擎 |
| `forResult()` | - | 返回 Observable |

#### 使用示例

```kotlin
AlbumLauncher.from(this)
    .choose(MimeType.ofImage(), false)
    .countable(true)
    .capture(true)
    .maxSelectable(9)
    .thumbnailScale(0.85f)
    .imageEngine(GlideEngine())
    .originalEnable(true)
    .maxOriginalSize(10)
    .forResult()
    .subscribe { result ->
        val uris = result.uris
        val paths = result.paths
    }
```

---

### 2. lib_camera_wechat - 微信相机

```groovy
implementation 'com.NBXXF.xxf_android:lib_camera_wechat:版本号'
```

#### CameraLauncher API

| 方法 | 参数 | 说明 |
|------|------|------|
| `instance` | - | 获取单例 |
| `openPreCamera()` | - | 前置摄像头 |
| `allowPhoto()` | Boolean | 允许拍照 |
| `allowRecord()` | Boolean | 允许录像 |
| `setMaxRecordTime()` | Int | 最大录制秒数 |
| `setRecordQuality()` | Int (1-100) | 视频质量 |
| `forResult()` | FragmentActivity | 启动相机 |

#### 使用示例

```kotlin
CameraLauncher.instance
    .allowPhoto(true)
    .allowRecord(true)
    .setMaxRecordTime(15)
    .setRecordQuality(80)
    .forResult(this)
    .subscribe { result ->
        if (result.isImage) {
            imageView.load(File(result.path))
        } else {
            videoView.setVideoPath(result.path)
        }
    }
```

---

### 3. lib_qrcode - 二维码

```groovy
implementation 'com.NBXXF.xxf_android:lib_qrcode:版本号'
```

#### QRCodeProviders Builder

| 方法 | 参数 | 默认值 | 说明 |
|------|------|--------|------|
| `setOutputSize()` | Size | 200x200 | 输出尺寸 |
| `setContentColor()` | Int | 黑色 | 二维码颜色 |
| `setBackgroundColor()` | Int | 白色 | 背景颜色 |
| `setContentPadding()` | Int | 1 | 内边距 |
| `setLogo()` | Bitmap | null | Logo图片 |
| `setLogoPercent()` | Float | 0.2f | Logo占比 |
| `setErrorCorrectionLevel()` | Level | M | 容错级别 |
| `build()` | - | Bitmap | 生成二维码 |

#### 容错级别

| 级别 | 容错率 |
|------|--------|
| L | 7% |
| M | 15% |
| Q | 25% |
| H | 35% |

#### 使用示例

```kotlin
val bitmap = QRCodeProviders.of("https://example.com")
    .setOutputSize(Size(300, 300))
    .setContentColor(Color.BLACK)
    .setLogo(logoBitmap)
    .setErrorCorrectionLevel(ErrorCorrectionLevel.H)
    .build()

imageView.setImageBitmap(bitmap)
```

---

### 4. lib_glide - 图片加载

```groovy
implementation 'com.NBXXF.xxf_android:lib_glide:版本号'
```

基于 Glide 封装，支持 OkHttp、WebP、AndroidSVG。

```kotlin
// 基础加载
imageView.load(url)

// 带配置
imageView.load(url) {
    placeholder(R.drawable.placeholder)
    error(R.drawable.error)
    circleCrop()
}

// 圆角
imageView.load(url) {
    roundedCorners(8.dp)
}
```

---

### 5. lib_preview - 图片预览

```groovy
implementation 'com.NBXXF.xxf_android:lib_preview:版本号'
```

基于 PhotoView 的图片预览库。

```kotlin
// 预览单张图片
PreviewLauncher.preview(context, imageUrl)

// 预览多张图片
PreviewLauncher.preview(context, imageUrls, currentIndex)
```

---

### 6. lib_download - 文件下载

```groovy
implementation 'com.NBXXF.xxf_android:lib_download:版本号'
```

基于 OkDownload 的文件下载库。

```kotlin
// 下载文件
DownloadManager.download(url, savePath)
    .subscribe { progress ->
        progressBar.progress = progress.percent
        if (progress.isCompleted) {
            // 下载完成
        }
    }

// 暂停/恢复
DownloadManager.pause(taskId)
DownloadManager.resume(taskId)

// 取消
DownloadManager.cancel(taskId)
```

---

### 7. lib_download_m3u8 - M3U8下载

```groovy
implementation 'com.NBXXF.xxf_android:lib_download_m3u8:版本号'
```

M3U8 流媒体下载，支持 ExoPlayer HLS 和 FFmpeg 转换。

```kotlin
M3U8Downloader.download(m3u8Url, outputPath)
    .subscribe { progress ->
        // 下载进度
    }
```

---

### 8. lib_objectbox - 本地数据库

```groovy
implementation 'com.NBXXF.xxf_android:lib_objectbox:版本号'
```

ObjectBox 本地数据库集成。

```kotlin
// 定义实体
@Entity
data class User(
    @Id var id: Long = 0,
    var name: String = "",
    var age: Int = 0
)

// CRUD 操作
val box = ObjectBox.get().boxFor(User::class.java)

// 插入
box.put(User(name = "张三", age = 25))

// 查询
val users = box.query()
    .equal(User_.name, "张三")
    .build()
    .find()

// 更新
user.age = 26
box.put(user)

// 删除
box.remove(user)
```

---

### 9. lib_mlkit - ML Kit扫描

```groovy
implementation 'com.NBXXF.xxf_android:lib_mlkit:版本号'
```

ML Kit + CameraX 集成，支持人脸检测和条码扫描。

```kotlin
// 条码扫描
BarcodeScannerLauncher.scan(this)
    .subscribe { barcode ->
        println("扫描结果: ${barcode.rawValue}")
    }

// 人脸检测
FaceDetectorLauncher.detect(this, bitmap)
    .subscribe { faces ->
        faces.forEach { face ->
            println("人脸位置: ${face.boundingBox}")
        }
    }
```

---

### 10. lib_pinyin - 拼音转换

```groovy
implementation 'com.NBXXF.xxf_android:lib_pinyin:版本号'
```

基于 TinyPinyin 的汉字拼音转换。

```kotlin
// 转拼音
val pinyin = Pinyin.toPinyin("中国")  // "ZHONGGUO"

// 首字母
val first = Pinyin.toPinyin("中国", separator = "", caseType = CaseType.LOWERCASE)[0]  // 'z'

// 判断是否为汉字
Pinyin.isChinese('中')  // true
```

---

### 11. lib_wechat - 微信SDK

```groovy
implementation 'com.NBXXF.xxf_android:lib_wechat:版本号'
```

微信 SDK 集成（分享、登录、支付）。

```kotlin
// 初始化
WechatSDK.init(appId, appSecret)

// 登录
WechatSDK.login()
    .subscribe { authResult ->
        val code = authResult.code
    }

// 分享文本
WechatSDK.shareText("分享内容", WechatScene.Session)
    .subscribe { success -> }

// 分享图片
WechatSDK.shareImage(bitmap, WechatScene.Timeline)
    .subscribe { success -> }

// 支付
WechatSDK.pay(payRequest)
    .subscribe { payResult -> }
```

---

### 12. lib_flycoTabLayout - TabLayout

```groovy
implementation 'com.NBXXF.xxf_android:lib_flycoTabLayout:版本号'
```

FlycoTabLayout - 功能强大的 TabLayout 库。

```xml
<com.flyco.tablayout.SlidingTabLayout
    android:layout_width="match_parent"
    android:layout_height="48dp"
    app:tl_indicator_color="#FF5722"
    app:tl_indicator_height="3dp"
    app:tl_textSelectColor="#FF5722"
    app:tl_textUnselectColor="#666666" />
```

```kotlin
tabLayout.setViewPager(viewPager, titles)
```

---

### 13. lib_viewPager - ViewPager

```groovy
implementation 'com.NBXXF.xxf_android:lib_viewPager:版本号'
```

ViewPager 相关工具库。

---

### 14. lib_draggableView - 可拖拽View

```groovy
implementation 'com.NBXXF.xxf_android:lib_draggableView:版本号'
```

可拖拽 View 库。

```xml
<com.xxf.view.draggable.DraggableView
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:dragEnabled="true"
    app:stickToEdge="true" />
```

---

### 15. lib_drouter - 路由框架

```groovy
implementation 'com.NBXXF.xxf_android:lib_drouter:版本号'
```

DRouter 路由框架集成。

```kotlin
// 路由跳转
DRouter.build("/user/detail")
    .putExtra("userId", "123")
    .start(context)

// 获取服务
val userService = DRouter.build(IUserService::class.java).getService()
```

---

### 16. blockcanary - 卡顿检测

```groovy
// 完整版
implementation 'com.NBXXF.xxf_android:blockcanary-android:版本号'
// 无操作版（Release）
implementation 'com.NBXXF.xxf_android:blockcanary-android-no-op:版本号'
```

主线程卡顿检测工具。

```kotlin
// 初始化
BlockCanary.install(this, BlockCanaryContext()).start()

// 自定义配置
class AppBlockCanaryContext : BlockCanaryContext() {
    override fun provideBlockThreshold() = 500 // 卡顿阈值 ms
    override fun displayNotification() = true
}
```

---

### 17. lib_profileinstaller - 性能优化

```groovy
implementation 'com.NBXXF.xxf_android:lib_profileinstaller:版本号'
```

ProfileInstaller - AOT 编译优化，安装 baseline.prof 提升启动性能。

---

## 效率提升工具

### 高性能数据结构

| 类名 | 说明 | 性能提升 |
|------|------|---------|
| `LongHashMap` | Long 键 Map | 比 HashMap 快 50% |
| `LongHashSet` | Long 集合 | 比 HashSet 快 50% |

### 高性能 Hash 算法

| 类名 | 说明 | 性能提升 |
|------|------|---------|
| `MurmurHash` | Murmur 哈希 | 比 JDK Hash 快 200% |
| `CityHash` | Google CityHash | 大数据 Hash 更高效 |

### Json 安全

```kotlin
val gson = GsonBuilder()
    .registerTypeAdapterFactory(SafeTypeAdapterFactory())
    .build()
```

兼容弱类型返回：Int、Long、Float、Double、Boolean、Number、BigDecimal

推荐高性能插件：[gson_plugin](https://github.com/NBXXF/gson_plugin)

---

## License

```
Copyright 2024 XXF

Licensed under the Apache License, Version 2.0
```
