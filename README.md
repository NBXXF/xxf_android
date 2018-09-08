# xxf架构
xxf架构是一种MVVM架构,让MVVM更加简洁,规范

    implementation 'com.android.support:appcompat-v7:28.0.0-rc02'
    implementation 'android.arch.lifecycle:extensions:1.1.1'
    implementation 'com.trello.rxlifecycle2:rxlifecycle:2.2.2'

1. 去除RxAndroid,用Android自带的lifecycle来管理RxJava的生命周期
2. viewmodel中也可以使用rxjava bind生命周期,跟activity一样

#### 用法
##### Activity
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

##### Fragment
与Activity类似,继承RXFragment即可;

##### DialogFaragment
与Activity类似,继承RXDialogFragment即可;