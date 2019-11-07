# xxf http架构
增加缓存模式
 ```
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
         * 只从本地缓存中拿取,没有缓存 会抛 {@link NullPointerException}
         */
        onlyCache,

        /**
         * 如果本地存在就返回本地的,否则返回网络的数据
         */
        ifCache,
 ```
##### 快速使用
 ```
    api 'com.github.NBXXF:xxf_android_http:2.5.0'  
  ```
 
     
##### http请求
 [参考](/xxf_http/README.md).

      ``` 1. http接口interface声明（与retrofit十分类似)
      @BaseUrl(com.tokentm.businesscard.config.BuildConfig.API_URL) //必选;baseurl 为retrofit的基础路由,比如:http://www.baidu.com/
      @Interceptor({TokenInterceptor.class})  //可选;拦截器 继承自okhttp3.Interceptor
      @GsonInterceptor(GlobalGsonConvertInterceptor.class) //可选; json或者gson转换拦截器 继承自com.xxf.arch.http.converter.gson.GsonConvertInterceptor
      public interface BackupApiService {
      
      
       @GET("config/question")
       Observable<ResponseDTO<List<SecurityQuestionDTO>>> backupUpConfigQuestionQuery();
       
       
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
   
##### 全部用法 参考demo     
  ```
@BaseUrl("http://api.map.baidu.com/")
@RxHttpCacheProvider(DefaultRxHttpCacheDirectoryProvider.class)
public interface LoginApiService {
    @GET("http://api.map.baidu.com/telematics/v3/weather?location=%E5%98%89%E5%85%B4&output=json&ak=5slgyqGDENN7Sy7pw29IUvrZ")
    Observable<JsonObject> getCity();

    @GET("http://api.map.baidu.com/telematics/v3/weather?location=%E5%98%89%E5%85%B4&output=json&ak=5slgyqGDENN7Sy7pw29IUvrZ")
    @RxHttpCache(RxHttpCache.CacheType.firstCache)
    Observable<JsonObject> getCityFirstCache();


    @GET("http://api.map.baidu.com/telematics/v3/weather?location=%E5%98%89%E5%85%B4&output=json&ak=5slgyqGDENN7Sy7pw29IUvrZ")
    @RxHttpCache(RxHttpCache.CacheType.firstRemote)
    Observable<JsonObject> getCityFirstRemote();

    @GET("http://api.map.baidu.com/telematics/v3/weather?location=%E5%98%89%E5%85%B4&output=json&ak=5slgyqGDENN7Sy7pw29IUvrZ")
    @RxHttpCache(RxHttpCache.CacheType.ifCache)
    Observable<JsonObject> getCityIfCache();

    @GET("http://api.map.baidu.com/telematics/v3/weather?location=%E5%98%89%E5%85%B4&output=json&ak=5slgyqGDENN7Sy7pw29IUvrZ")
    @RxHttpCache(RxHttpCache.CacheType.onlyCache)
    Observable<JsonObject> getCityOnlyCache();

}
 ```