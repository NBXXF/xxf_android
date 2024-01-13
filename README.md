Table of Contents
=================

* [Android技术中台](#android技术中台)
    * [用法](#用法)
    * [引入方式](#引入方式)
    * [Application 与Activity 管理](#application-与activity-管理)
    * [http请求](#http请求)
    * [RxJava生命周期管理](#rxjava生命周期管理)
    * [权限](#权限)
    * [startActivityForResult](#startactivityforresult)
    * [事件通信框架](#事件通信框架)
    * [Uri 授权](#uri-授权)
    * [Json安全](#json安全)
    * [效率提升](#效率提升)
    * [委托属性](#委托属性)
    * [自定义相册](#自定义相册)
    * [自定义相机仿微信](#自定义相机仿微信)
    * [二维码生成](#二维码生成)
    * [RecyclerView 分割线](#recyclerview-分割线)
    * [圆角组件](#圆角组件)
    * [带渐变背景的组件](#带渐变背景的组件)
    * [设置宽高比例的组件](#设置宽高比例的组件)

# Android技术中台

xxf架构封装常用组件与用法,且符合函数式和流式编程

1. 去除RxLife,用Android自带的lifecycle来管理RxJava的生命周期
2. viewModel中也可以使用rxjava bind生命周期,跟activity一样,可以处理rxjava的生命周期
3. 权限请求可以用RxJava链式调用,不用写复杂的回调 内部使用ActivityResultLauncher 且可以免注册
4. startActivityForResult可以用RxJava链式调用,不用写复杂的回调 内部使用ActivityResultLauncher 且可以免注册
5. 简单配置http,轻松完成网络请求
6. http双缓存(避免服务器不处理etag),多种策略保证数据及时交互
7. 时间和货币格式化全权交割给框架处理
8. 各种工具类Number,Time,File,Toast,Zip,Arrays....等15种
9. 全面转向kotlin 扩展函数超 400 个 全部开箱即可使用
10. 封装常见自定义View
    TitleBar,Loading,ScaleFrameLayout,MaxHeightView,SoftKeyboardSizeWatchLayout...等20种
11. 数据库在objectbox上进行封装以及监听,以及生成long id

#### 用法

##### 引入方式

代码已经80%转换成kotlin 请注意用法改变,新版本使用方式

```groovy
//请在build.gradle中配置
allprojects {
    repositories {

        maven {
            url 'https://maven.aliyun.com/repository/public'
        }
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
        // 实时检查 Snapshot 更新
        resolutionStrategy.cacheChangingModulesFor 0, 'seconds'
    }
}
```

 ```
    //新版本使用方式,需要添加上面的权限
    implementation 'com.NBXXF.xxf_android:libs:5.2.2.1-SNAPSHOT'
 ```

 ```
    //老版本使用方式,无权限
    implementation 'com.github.NBXXF.xxf_android:lib_view:5.2.1.0'//主要lib
 ```

##### Application 与Activity 管理

提供以下[直接访问]的内敛函数，其他组件扩展 400个函数 参考lib_ktx

 ```
val applicationContext: Application

val application: Application

val activityList: List<Activity> 

val topActivity: Activity

val topActivityOrNull: Activity?

val topFragmentActivityOrNull: FragmentActivity? 

val topActivityOrApplication: Context
 
 ```

##### http请求

1. http接口interface声明（与retrofit十分类似) 全部采用注解式,灵活插拔,无client概念

 ```
/**
 * 提供基础路由
 */
@BaseUrl("http://api.map.baidu.com/")

/**
 * 提供缓存目录设置
 */
@RxHttpCacheProvider(DefaultRxHttpCacheDirectoryProvider.class)
/**
 * 声明拦截器
 */
@Interceptor({MyLoggerInterceptor.class, MyLoggerInterceptor2.class})

/**
 * 声明rxJava拦截器
 */
@RxJavaInterceptor(DefaultCallAdapter.class)
public interface LoginApiService {

    /**
     * 声明接口 跟retrofit一致
     *
     * @return
     */
    @GET("telematics/v3/weather?location=%E5%98%89%E5%85%B4&output=json&ak=5slgyqGDENN7Sy7pw29IUvrZ")
    Observable<JsonObject> getCity();

    /**
     * 在retrofit上面扩展了 @Cache 设置缓存类型
     *
     * @param cacheType
     * @return
     */
    @GET("telematics/v3/weather?location=%E5%98%89%E5%85%B4&output=json&ak=5slgyqGDENN7Sy7pw29IUvrZ")
    Observable<JsonObject> getCity(@Cache CacheType cacheType);

    /**
     * 缓存5s
     * 添加在方法上     @Headers("cache:5000")
     *
     * @param cacheType
     * @return
     */
    @GET("telematics/v3/weather?location=%E5%98%89%E5%85%B4&output=json&ak=5slgyqGDENN7Sy7pw29IUvrZ")
    @Headers("cache:5000")
    Observable<JsonObject> getCity2(@Cache CacheType cacheType);

    /**
     * 缓存
     * 添加在参数上 @Header("cache") long time
     *
     * @param cacheType
     * @return
     */
    @GET("telematics/v3/weather?location=%E5%98%89%E5%85%B4&output=json&ak=5slgyqGDENN7Sy7pw29IUvrZ")
    Observable<JsonObject> getCity3(@Header("cache") long time, @Cache CacheType cacheType);


    @GET("telematics/v3/weather?location=%E5%98%89%E5%85%B4&output=json&ak=5slgyqGDENN7Sy7pw29IUvrZ")
    @RxHttpCache(CacheType.onlyCache)
    Observable<JsonObject> getCityOnlyCache();

}
 ```

缓存模式

 ```
public enum CacheType {
    /**
     * 先从本地缓存拿取,然后从服务器拿取,可能会onNext两次,如果本地没有缓存 最少执行oNext一次
     */
    firstCache,
    /**
     * 先从服务器获取,没有网络 读取本地缓存
     */
    firstRemote,
    /**
     * 只从服务器拿取
     */
    onlyRemote,
    /**
     * 只从本地缓存中拿取,没有缓存 执行逻辑同Observable.empty()
     */
    onlyCache,

    /**
     * 如果本地存在就返回本地的,否则返回网络的数据
     */
    ifCache,

    /**
     * 读取上次的缓存,上次没有缓存就返回网络的数据,然后同步缓存;
     * 上次有缓存,也会同步网络数据 但不会onNext
     */
    lastCache;
}
   ```

2. api 请求方式,并绑定loading对话框

```
       BackupApiService::class.java.apiService()
                .backupUpConfigQuestionQuery()
                .map(new ResponseDTOSimpleFunction<List<SecurityQuestionDTO>>())
                .bindProgressHud(this))//绑定progress loadingdialog
                .subscribe(new Consumer<List<SecurityQuestionDTO>>() {
                    @Override
                    public void accept(List<SecurityQuestionDTO> securityQuestionDTOS) throws Exception {
                     
                    }
                });
```

kotlin 方式

```
       BackupApiService::class.apiService()
       
       getApiService<BackupApiService>()
        
```

拓展上传的文件类型 </br>
支持7种标识文件的方式,注意一般的服务器都支持 不传filename在表单,这个取决于你的服务器</br>
也就是协议中的 Content-Disposition: form-data; name="file" 与   Content-Disposition: form-data; name="file"; filename="1705161084857114.jpeg" 的区别</br>
或者我提供了很多拓展uri.toPart("filename"),file.toPart("filename")...</br>

1. File
2. ByteArray
3. inputStream
4. FileDescriptor
5. ParcelFileDescriptor
6. AssetFileDescriptor 
7. Uri
如下demo

```
@POST("api/rentalAreaPad/uploadFile")
@Multipart
fun uploadRentalAreaPadFile2(
@Query("companyId") companyId: String,
@Part("file") fileUri: Uri,
): Observable<BaseResultDTO<String>>
```

##### RxJava生命周期管理

在Activity,Fragment,DialogFragment中都可以使用RxJava管理生命周期,语法保持一致,如下:

  ```
      io.reactivex.Observable.interval(1, TimeUnit.SECONDS)
                    .bindLifecycle(GrContactsFragment.this))//绑定生命周期
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            LogUtils.AndroidD("--------->ex:" + aLong);
                        }
                    });

  ```

##### 权限

权限请求使用Rx链式调用(内部使用ActivityResultLauncher 且使用方免注册)  
常规为了避免断链式调用,那么需要attach隐藏的fragment,而这里的实现为站位ActivityResultLauncher
更节约内存,且解决了并行的问题,也能免注册调用

      //请求授权
        ``` 
      支持activity fragment LifecycleOwner 挂载对象访问   如果是在其他类对象中 可以访问全局内敛函数 topFragmentActivity?.requestPermissionsObservable
      requestPermission(Manifest.permission.CAMERA)
                                .subscribe(new Consumer<Boolean>() {
                                    @Override
                                    public void accept(Boolean aBoolean) throws Exception {
                                        ToastUtils.showToast(v.getContext(), "Manifest.permission.CAMERA:" + aBoolean);
                                    }
                                });
                                
      //获取是否授权                          
      ToastUtils.showToast(v.getContext(), "Manifest.permission.CAMERA:" + checkSelfPermissions(Manifest.permission.CAMERA));
     ```

##### startActivityForResult

Rx链式调用(内部使用ActivityResultLauncher 且使用方免注册)  
常规为了避免断链式调用,那么需要attach隐藏的fragment,而这里的实现为站位ActivityResultLauncher
更节约内存,且解决了并行的问题,也能免注册调用

  ``` 
     支持activity fragment LifecycleOwner 挂载对象访问 如果是在其他类对象中 可以访问全局内敛函数 topFragmentActivity?.startActivityForResultObservable
     startActivityForResult(new Intent(MainActivity.this, TestActivity.class))
                                .subscribe(new Consumer<ActivityResult>() {
                                    @Override
                                    public void accept(ActivityResult activityResult) throws Exception {
                                        ToastUtils.showToast(v.getContext(), "activityResult:reqcode:" + activityResult.getRequestCode() + ";resCode" + activityResult.getResultCode() + ";data:" + activityResult.getData().getStringExtra("data"));

                                    }
                                });   
  ```                                  

##### 事件通信框架

``` 
以字符串作为通信的模型举例：

//订阅消息
String.javaClass.subscribeEvent()
.subscribe {

            }
 //发送消息           
"测试".postEvent();
        
 //其他模型       
  TestEvent::class.java.subscribeEvent()
                .subscribe {
            System.out.println("=====>"+"收到"+it);
        }
  TestEvent().postEvent();      
``` 

##### Uri 授权

Uri 授权每个app都去注册 十分麻烦,这里采用自动注册FileProvider

``` 
File.toAuthorizedUri
``` 

##### Json安全

更加安全的JsonTypeAdapter,应对若语言类的服务器开发工程师,比如 把int 返回双引号空,框架内部
兼容了int,bool,long,double,float,number,bigDecimal等常用类型的安全性

``` 
GsonBuilder()
    .registerTypeAdapterFactory(new SafeTypeAdapterFactory())
    .build()
``` 

这里同时也推荐我的另外一款code-gen工具
对gson提升10倍速度,比市面上的任何序列化工具都优秀,请参考[gson_plugin](https://github.com/NBXXF/gson_plugin)

##### 效率提升

1. 增加避免装箱拆箱的LongHashMap 比如SparseArray查询效率更快,相比hashmap 提升50%,以及LongHashSet等组件
2. 增加MurmurHash 比jdk自带hash 更快 200%提升
3. 增加city hash 对于大数据 hash 效率更高

##### 委托属性

###### 获取viewbinding 可以 使用 by viewBinding()加载

``` 
   private val binding by viewBinding(ActivitySettingBinding::bind);

```

###### activity intent和fragment Fragment参数绑定 可以使用 by argumentBinding("xxx")

``` 
    private val withNfcId: String? by argumentBinding("withNfcId");//携带了NFC卡号
``` 

###### SharedPreference 可以采用 by

读写 key value 委托,内部具体用什么缓存 由IPreferencesOwner决定,十分方便扩展,可以扩展为MMKV,sqlite,file等等

``` 
object PreferencesDemo : SharedPreferencesOwner {

    data class User(val name: String? = null)

    var name: String by preferencesBinding("key", "xxx")

    //可以监听
    var name2: String by preferencesBinding("key2", "xxx").observable { property, newValue ->
        println("=============>PrefsDemo3:$newValue")
    }
    var user: User by preferencesBinding("key3", User()).useGson()

    fun test() {
        println("=============>PrefsDemo:$name")
        name = randomUUIDString32;
        println("=============>PrefsDemo2:$name")
        name2 = randomUUIDString32;
        println("=============>PrefsDemo4:$name2")

        println("=============>PrefsDemoUserBefore:$user")
        user = User("张三 ${System.currentTimeMillis()}")
        println("=============>PrefsDemoUser:$user")
    }
}

inline fun <P : IPreferencesOwner, reified V> PrefsDelegate<P, out V>.useGson(): KeyValueDelegate<P, V> {
    return object : KeyValueDelegate<P, V>(this.key, this.default) {
        private val stringDelegate by lazyUnsafe {
            PrefsDelegate<P, String>(this.key, "", String::class);
        }

        override fun getValue(thisRef: P, property: KProperty<*>): V {
            val value = stringDelegate.getValue(thisRef, property)
            return if (value.isEmpty()) {
                default
            } else {
                Json.fromJson<V>(value) ?: default
            }
        }

        override fun setValue(thisRef: P, property: KProperty<*>, value: V) {
            stringDelegate.setValue(thisRef, property, Json.toJson(value));
        }
    }
}
``` 

##### 自定义相册

```
    //需额外加依赖
    implementation 'com.NBXXF.xxf_android:lib_album:xxxx'
```

```
   AlbumLauncher.from(SampleActivity.this)
                        .choose(MimeType.ofImage(), false)
                        .countable(true)
                        .capture(true)
                        .maxSelectable(9)
                        .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                        .gridExpectedSize(
                                getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                        .thumbnailScale(0.85f)
                        .imageEngine(new GlideEngine())
                        .setOnSelectedListener((uriList, pathList) -> {
                            Log.e("onSelected", "onSelected: pathList=" + pathList);
                        })
                        .showSingleMediaType(true)
                        .originalEnable(true)
                        .maxOriginalSize(10)
                        .autoHideToolbarOnSingleTap(true)
                        .setOnCheckedListener(isChecked -> {
                            Log.e("isChecked", "onCheck: isChecked=" + isChecked);
                        })
                        .forResult()
                        .subscribe(new Consumer<AlbumResult>() {
                            @Override
                            public void accept(AlbumResult albumResult) throws Throwable {
                                mAdapter.setData(albumResult.getUris(), albumResult.getPaths());
                            }
                        });
```

#### 自定义相机仿微信

```
    //需额外加依赖
    implementation 'com.NBXXF.xxf_android:lib_camera_wechat:xxxx'
```

```
 CameraLauncher.instance
                    //.openPreCamera()// 是否打开为前置摄像头
                    .allowPhoto(true)// 是否允许拍照 默认允许
                    .allowRecord(true)// 是否允许录像 默认允许
                    .setMaxRecordTime(3)//最长录像时间 秒
                    .forResult(this)
                    .subscribe {
                        if (it.isImage) {
                            text.text = "Image Path：\n${it.path}"
                        } else {
                            text.text = "Video Path：\n${it.path}"
                        }
                    }
```

#### 二维码生成

```
QRCodeProviders.of(content)
                .setOutputSize(new Size(width, height))
                .setContentMargin(Integer.valueOf(margin))
                .setContentColor(color_black)
                .setBackgroundColor(color_white)
                .setLogo(logoBitmap)
                .setContentFillImg(blackBitmap)
                .build();
```

##### RecyclerView 分割线

1. DividerDecorationFactory 工厂模式
2. LinearItemDecoration 支持 水平 垂直 和格子

##### 圆角组件

(app:radius="8dp",app:radius="360dp" 为圆形 详细参考下面每个类的 类注释！！！)

1. XXFRoundButton
2. XXFRoundCheckedTextView
3. XXFRoundEditText
4. XXFRoundImageView
5. XXFRoundTextView
6. XXFRoundLayout
7. XXFRoundLinearLayout
8. XXFRoundRelativeLayout

##### 带渐变背景的组件

(app:start_color app:end_color 详细参考下面每个类的 类注释！！！)

1. XXFGradientCompatButton
2. XXFGradientCompatCheckedTextView
3. XXFGradientCompatEditText
4. XXFGradientCompatImageView
5. XXFGradientCompatTextView
6. XXFGradientFrameLayout
7. XXFGradientLinearLayout
8. XXFGradientRelativeLayout

##### 设置宽高比例的组件

(app:widthRatio app:heightRatio 详细参考下面每个类的 类注释！！！)

1. XXFRationCompatButton
2. XXFRationCompatCheckedTextView
3. XXFRationCompatEditText
4. XXFRationCompatImageView
5. XXFRationCompatTextView
6. XXFRationFrameLayout
7. XXFRationLinearLayout
8. XXFRationtRelativeLayout
