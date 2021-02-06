package com.xxf.arch.service;

import android.os.Environment;

import com.xxf.arch.XXF;
import com.xxf.arch.utils.FileUtils;

import java.io.File;
import java.util.concurrent.Callable;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;


/**
 * @Description: java类作用描述
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/6/14 13:04
 */

/**
 * Environment.getDataDirectory() = /data
 * Environment.getDownloadCacheDirectory() = /cache
 * Environment.getExternalStorageDirectory() = /mnt/sdcard
 * Environment.getExternalStoragePublicDirectory(“test”) = /mnt/sdcard/test
 * Environment.getRootDirectory() = /system
 * getPackageCodePath() = /data/app/com.my.app-1.apk
 * getPackageResourcePath() = /data/app/com.my.app-1.apk
 * getCacheDir() = /data/data/com.my.app/cache
 * getDatabasePath(“test”) = /data/data/com.my.app/databases/test
 * getDir(“test”, Context.MODE_PRIVATE) = /data/data/com.my.app/app_test
 * getExternalCacheDir() = /mnt/sdcard/Android/data/com.my.app/cache
 * getExternalFilesDir(“test”) = /mnt/sdcard/Android/data/com.my.app/files/test
 * getExternalFilesDir(null) = /mnt/sdcard/Android/data/com.my.app/files
 * getFilesDir() = /data/data/com.my.app/files
 */
public interface UserFileService {
    /**
     * 不需要权限
     * 规则 如果sd卡挂载 就用sd 否则用私有区域
     * differUser=false data/data/app包名/files/     或者/mnt/sdcard/Android/data/app包名/files/Download
     * differUser=true data/data/app包名/files/userId/    或者/mnt/sdcard/Android/data/app包名/files/Download/userId/
     *
     * @param differUser      区分用户
     * @param forceInnerFiles 是否强制使用私有区域存储
     * @return
     */
    default Observable<File> getFilesDir(boolean differUser, boolean forceInnerFiles) {
        return Observable.fromCallable(new Callable<File>() {
            @Override
            public File call() throws Exception {
                File dir = XXF.getApplication().getFilesDir();
                if (!forceInnerFiles && Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                    dir = XXF.getApplication().getExternalFilesDir(null);
                }
                FileUtils.createOrExistsDir(dir);
                if (differUser) {
                    dir = new File(dir, XXF.getUserInfoProvider().getUserId());
                    FileUtils.createOrExistsDir(dir);
                }
                return dir;
            }
        }).subscribeOn(Schedulers.io());
    }

    /**
     * 不需要权限
     * differUser=false data/data/app包名/cache/     或者/mnt/sdcard/Android/data/app包名/cache
     * differUser=true data/data/app包名/cache/userId/    或者/mnt/sdcard/Android/data/app包名/cache/userId/
     *
     * @param differUser      区分用户
     * @param forceInnerCache 是否强制使用私有区域存储
     * @return
     */
    default Observable<File> getCacheDir(boolean differUser, boolean forceInnerCache) {
        return Observable.fromCallable(new Callable<File>() {
            @Override
            public File call() throws Exception {
                File dir = XXF.getApplication().getCacheDir();
                if (!forceInnerCache && Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                    dir = XXF.getApplication().getExternalCacheDir();
                }
                FileUtils.createOrExistsDir(dir);
                if (differUser) {
                    dir = new File(dir, XXF.getUserInfoProvider().getUserId());
                    FileUtils.createOrExistsDir(dir);
                }
                return dir;
            }
        }).subscribeOn(Schedulers.io());
    }


}
