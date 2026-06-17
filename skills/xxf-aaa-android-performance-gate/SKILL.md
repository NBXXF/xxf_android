---
name: xxf-aaa-android-performance-gate
description: xxf_android 性能风险门禁。用于 UI 渲染、RecyclerView、图片、启动、主线程、下载/网络热路径、数据库和大文件处理改动。
---

# Android 性能门禁

## 自动触发

- UI 布局、View、自定义绘制、RecyclerView/Adapter、动画。
- 图片加载、预览、相册、相机、二维码、MLKit。
- 下载、M3U8、网络请求、数据库、文件 IO。
- Application 初始化、全局单例、主线程调度。

## 检查点

- 主线程是否做网络、数据库、大文件、解码、压缩、扫描。
- 列表是否重复创建对象、重复绑定图片、缺少局部刷新。
- 图片/媒体是否有尺寸、缓存、生命周期和取消策略。
- 下载/网络是否有取消、重试、限流和资源释放。
- 可选 SDK 是否因强依赖增加宿主包体或初始化成本。

## 结论

输出 `Pass/Warn/Block`。Warn/Block 必须说明需要的补充验证，例如 demo 滚动验证、弱网验证、内存观察、启动链路检查或真机设备能力验证。
