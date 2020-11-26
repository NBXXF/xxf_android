# xxf架构
xxf架构是一种MVVM架构,让MVVM更加简洁,规范
1. 去除RxLife,用Android自带的lifecycle来管理RxJava的生命周期
2. viewmodel中也可以使用rxjava bind生命周期,跟activity一样,可以处理rxjava的生命周期
3. 权限请求可以用RxJava链式调用,不用写复杂的回调
4. startActivityForResult可以用RxJava链式调用,不用写复杂的回调
5. 简单配置http,轻松完成网络请求
6. http双缓存,多种策略保证数据及时交互

#### 用法
##### 引入项目
 ```
    //必选
    implementation 'com.github.NBXXF:xxf_android:2.5.x'//主要lib
 ```
 
##### Activity / Fragment / DialogFaragment
需要继承XXFactivity
绑定布局 用@BindView
绑定vm  用@BindVM

    @BindView(R.layout.other_activity_devloper_helper_setting)
    @BindVM(DeveloperHelperSettingVM.class)
    public class DeveloperHelperSettingActivity
            extends XXFActivity {

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getBinding().setVariable(BR.vm, getVm());
        }
    }

##### http请求
      ``` 1. http接口interface声明（与retrofit十分类似)

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
   
   
         ```2. api 请求方式,并绑定loading对话框
       XXF.getApiService(BackupApiService.class)
                .backupUpConfigQuestionQuery()
                .map(new ResponseDTOSimpleFunction<List<SecurityQuestionDTO>>())
                .compose(XXF.<List<SecurityQuestionDTO>>bindToProgressHud(new ProgressHUDTransformerImpl.Builder(this)))//绑定progress loadingdialog
                .subscribe(new Consumer<List<SecurityQuestionDTO>>() {
                    @Override
                    public void accept(List<SecurityQuestionDTO> securityQuestionDTOS) throws Exception {
                     
                    }
                });
   ```

##### RxJava生命周期管理
在Activity,Fragment,DialogFragment与ViewModel中都可以使用RxJava管理生命周期,语法保持一致,如下:


      io.reactivex.Observable.interval(1, TimeUnit.SECONDS)
                    .compose(XXF.<Long>bindToLifecycle(GrContactsFragment.this))//绑定生命周期
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            LogUtils.AndroidD("--------->ex:" + aLong);
                        }
                    });


##### 权限使用Rx链式调用

      //请求授权
      XXF.requestPermission(MainActivity.this, Manifest.permission.CAMERA)
                                .subscribe(new Consumer<Boolean>() {
                                    @Override
                                    public void accept(Boolean aBoolean) throws Exception {
                                        ToastUtils.showToast(v.getContext(), "Manifest.permission.CAMERA:" + aBoolean);
                                    }
                                });
                                
      //获取是否授权                          
      ToastUtils.showToast(v.getContext(), "Manifest.permission.CAMERA:" + XXF.isGrantedPermission(MainActivity.this, Manifest.permission.CAMERA));
            

##### startActivityForResult Rx链式调用

     XXF.startActivityForResult(MainActivity.this, new Intent(MainActivity.this, TestActivity.class), 1001)
                                .subscribe(new Consumer<ActivityResult>() {
                                    @Override
                                    public void accept(ActivityResult activityResult) throws Exception {
                                        ToastUtils.showToast(v.getContext(), "activityResult:reqcode:" + activityResult.getRequestCode() + ";resCode" + activityResult.getResultCode() + ";data:" + activityResult.getData().getStringExtra("data"));

                                    }
                                });                                     

