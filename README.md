# xxf架构
xxf架构是一种MVVM架构,让MVVM更加简洁,规范
1. 去除RxLife,用Android自带的lifecycle来管理RxJava的生命周期
2. viewmodel中也可以使用rxjava bind生命周期,跟activity一样,可以处理rxjava的生命周期
3. 权限请求可以用RxJava链式调用,不用写复杂的回调
4. startActivityForResult可以用RxJava链式调用,不用写复杂的回调
5. 简单配置http,轻松完成网络请求

#### 用法
##### 引入项目
    //必选
    implementation 'com.github.axuan2015:xxf:1.3.0'//主要lib
    implementation 'com.android.support:appcompat-v7:28.0.0-rc02'
    implementation 'android.arch.lifecycle:extensions:1.1.1'
    
    //可选
    implementation 'io.reactivex.rxjava2:rxandroid:2.0.1'
    implementation 'io.reactivex.rxjava2:rxjava:2.0.1'
    implementation 'android.arch.lifecycle:extensions:1.1.1'
    implementation 'com.squareup.retrofit2:retrofit:2.5.0'
    implementation 'com.squareup.retrofit2:adapter-rxjava2:2.5.0'
    implementation 'com.google.code.gson:gson:2.8.2'

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

