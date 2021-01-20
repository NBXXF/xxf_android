package com.xxf.arch.service;

import android.Manifest;
import android.app.Activity;
import android.os.Environment;

import androidx.lifecycle.LifecycleOwner;

import com.xxf.arch.XXF;
import com.xxf.arch.core.permission.RxPermissionTransformer;

import java.io.File;
import java.util.concurrent.Callable;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Function;
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
     * data/data/app包名/files/
     *不需要权限
     * @return
     */
    default Observable<File> getPrivateFileDir() {
        return Observable.fromCallable(new Callable<File>() {
            @Override
            public File call() throws Exception {
                File file = XXF.getApplication().getFilesDir();
                if (!file.exists()) {
                    file.mkdirs();
                }
                return file;
            }
        }).subscribeOn(Schedulers.io());
    }

    /**
     * data/data/app包名/files/userId/
     * 不需要权限
     * @return
     */
    default Observable<File> getUserPrivateFileDir() {
        return getPrivateFileDir()
                .map(new Function<File, File>() {
                    @Override
                    public File apply(File file) throws Exception {
                        File userFile = new File(file, XXF.getUserInfoProvider().getUserId());
                        if (!userFile.exists()) {
                            userFile.mkdir();
                        }
                        return userFile;
                    }
                });
    }


    /**
     * /mnt/sdcard/app包名/
     * 会自动请求权限
     * @return
     */
    default Observable<File> getPublicFileDir() {
        Activity topActivity = XXF.getActivityStackProvider().getTopActivity();
        if (topActivity instanceof LifecycleOwner) {
            return XXF.requestPermission((LifecycleOwner) topActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .compose(new RxPermissionTransformer(Manifest.permission.WRITE_EXTERNAL_STORAGE))
                    .map(new Function<Boolean, File>() {
                        @Override
                        public File apply(Boolean aBoolean) throws Throwable {
                            File file = new File(
                                    new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath())
                                            .append(File.separator)
                                            .append(XXF.getApplication().getPackageName())
                                            .toString());
                            if (!file.exists()) {
                                file.mkdirs();
                            }
                            return file;
                        }
                    });
        } else {
            return Observable.error(new RuntimeException("top activity must LifecycleOwner"));
        }
    }


    /**
     * /mnt/sdcard/app包名/userId/
     * 会自动请求权限
     * @return
     */
    default Observable<File> getUserPublicFileDir() {
        return getPublicFileDir()
                .map(new Function<File, File>() {
                    @Override
                    public File apply(File file) throws Exception {
                        File userFile = new File(file, XXF.getUserInfoProvider().getUserId());
                        if (!userFile.exists()) {
                            userFile.mkdir();
                        }
                        return userFile;
                    }
                });
    }

}
