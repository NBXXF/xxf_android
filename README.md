# xxf架构


    implementation 'com.android.support:appcompat-v7:28.0.0-rc02'
    implementation 'android.arch.lifecycle:extensions:1.1.1'
    implementation 'com.trello.rxlifecycle2:rxlifecycle:2.2.2'

1. 去除RxAndroid,用Android自带的lifecycle来管理RxJava的生命周期
2. viewmodel中也可以使用rxjava bind生命周期,跟activity一样
