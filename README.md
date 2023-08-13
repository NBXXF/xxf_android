# xxf架构

xxf架构是一种MVVM架构,让MVVM更加简洁,规范

1. 去除RxLife,用Android自带的lifecycle来管理RxJava的生命周期
2. viewModel中也可以使用rxjava bind生命周期,跟activity一样,可以处理rxjava的生命周期
3. 权限请求可以用RxJava链式调用,不用写复杂的回调
4. startActivityForResult可以用RxJava链式调用,不用写复杂的回调
5. 简单配置http,轻松完成网络请求
6. http双缓存,多种策略保证数据及时交互
7. 时间和货币格式化全权交割给框架处理
8. 各种工具类Number,Time,File,Toast,Zip,Arrays....等15种
9. 扩展ARouter支持 path为http 或者路径携带参数/user/login?phone=17611639077
10. 封装常见自定义View TitleBar,Loading,ScaleFrameLayout,MaxHeightView,SoftKeyboardSizeWatchLayout...等20种

#### 用法

##### 引入方式 代码已经80%转换成kotlin 请注意用法改变

 ```
    //必选
    implementation 'com.github.NBXXF.xxf_android:lib_view:5.2.0.2'//主要lib
    最新版本参考[点击](https://github.com/NBXXF/xxf_android/releases)  
 ```

##### Application 与Activity 管理 提供以下[直接访问]的内敛函数

 ```
 /**
 * 全局上下文
 */
val applicationContext by lazy {
    ...
}

/**
 * activity 栈
 */
val activityStack by lazy {
    ....
}

/**
 * 栈顶activity
 */
val topActivity: Activity?

/**
 * 栈顶 fragmentActivity
 */
val topFragmentActivity: FragmentActivity?

/**
 * 根activity
 */
val rootActivity: Activity?

/**
 * 所有Activity
 */
val allActivity: Array<Activity>

/**
 * 重启app 仅activity
 */
inline fun restartApp() {
    ...
}

/**
 *  判断app是否在后台
 */
val isAppBackground: Boolean

 
 ```

##### http请求

1. http接口interface声明（与retrofit十分类似)

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

##### 权限,权限请求使用Rx链式调用

      //请求授权
        ``` 
      支持activity fragment LifecycleOwner 挂载对象访问   如果是在其他类对象中 可以访问全局内敛函数 topFragmentActivity?.requestPermissionsObservable
      requestPermissionsObservable(Manifest.permission.CAMERA)
                                .subscribe(new Consumer<Boolean>() {
                                    @Override
                                    public void accept(Boolean aBoolean) throws Exception {
                                        ToastUtils.showToast(v.getContext(), "Manifest.permission.CAMERA:" + aBoolean);
                                    }
                                });
                                
      //获取是否授权                          
      ToastUtils.showToast(v.getContext(), "Manifest.permission.CAMERA:" + isGrantedPermission(Manifest.permission.CAMERA));
     ```

##### startActivityForResult Rx链式调用

  ``` 
     支持activity fragment LifecycleOwner 挂载对象访问 如果是在其他类对象中 可以访问全局内敛函数 topFragmentActivity?.startActivityForResultObservable
     startActivityForResultObservable(new Intent(MainActivity.this, TestActivity.class), 1001)
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

##### RecyclerView 分割线

1. DividerDecorationFactory 工厂模式
2. DividerDecoration 水平或者分割线
3. GridItemDecoration 格子分割线

##### 圆角组件(app:radius="8dp",app:radius="360dp" 为圆形 详细参考下面每个类的 类注释！！！)

1. XXFRoundButton
2. XXFRoundCheckedTextView
3. XXFRoundEditText
4. XXFRoundImageView
5. XXFRoundTextView
6. XXFRoundLayout
7. XXFRoundLinearLayout
8. XXFRoundRelativeLayout

##### 带渐变背景的组件(app:start_color app:end_color 详细参考下面每个类的 类注释！！！)

1. XXFGradientCompatButton
2. XXFGradientCompatCheckedTextView
3. XXFGradientCompatEditText
4. XXFGradientCompatImageView
5. XXFGradientCompatTextView
6. XXFGradientFrameLayout
7. XXFGradientLinearLayout
8. XXFGradientRelativeLayout

##### 设置宽高比例的组件(app:widthRatio app:heightRatio 详细参考下面每个类的 类注释！！！)

1. XXFRationCompatButton
2. XXFRationCompatCheckedTextView
3. XXFRationCompatEditText
4. XXFRationCompatImageView
5. XXFRationCompatTextView
6. XXFRationFrameLayout
7. XXFRationLinearLayout
8. XXFRationtRelativeLayout
