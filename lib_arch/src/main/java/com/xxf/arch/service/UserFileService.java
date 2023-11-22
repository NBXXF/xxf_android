package com.xxf.arch.service;

import android.os.Environment;

import com.xxf.application.ApplicationInitializer;
import com.xxf.arch.XXF;
import com.xxf.utils.FileUtils;

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
 * 1、内部存储
 * 内部存储：手机内部存储的根目录
 * 对应路径：Environment.getDataDirectory().getParentFile()
 * <p>
 * 方法	路径
 * Environment.getDataDirectory()	/data
 * Environment.getDownloadCacheDirectory()	/cache
 * Environment.getRootDirectory()	/system
 * 2、外部存储
 * 外部存储：分为 SD 卡和扩展卡内存
 * SD 卡路径获取方法：Environment.getExternalStorageDirectory() /storage/sdcard0
 * <p>
 * SD 卡的九大公有路径如下：
 * <p>
 * 方法	路径
 * Environment.getExternalStoragePublicDirectory(DIRECTORY_ALARMS)	/storage/sdcard0/Alarms
 * Environment.getExternalStoragePublicDirectory(DIRECTORY_DCIM)	/storage/sdcard0/DCIM
 * Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS)	/storage/sdcard0/Download
 * Environment.getExternalStoragePublicDirectory(DIRECTORY_MOVIES)	/storage/sdcard0/Movies
 * Environment.getExternalStoragePublicDirectory(DIRECTORY_MUSIC)	/storage/sdcard0/Music
 * Environment.getExternalStoragePublicDirectory(DIRECTORY_NOTIFICATIONS)	/storage/sdcard0/Notifications
 * Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES)	/storage/sdcard0/Pictures
 * Environment.getExternalStoragePublicDirectory(DIRECTORY_PODCASTS)	/storage/sdcard0/Podcasts
 * Environment.getExternalStoragePublicDirectory(DIRECTORY_RINGTONES)	/storage/sdcard0/Ringtones
 * 注：Google官方建议我们数据应该存储在私有目录下，不建议存储在公有目录下或其他地方。
 * <p>
 * 3、权限相关
 * 方法	路径	权限
 * Context.getFilesDir	/data/user/0/应用包名/files	默认可读写
 * Context.getCacheDir	/data/user/0/应用包名/cache	默认可读写
 * Context.CodeCacheDir	/data/user/0/应用包名/code_cache	默认可读写
 * Context.getDatabasePath	/data/user/0/应用包名/databases/参数名	默认可读写
 * Context.getDir	/data/user/0/应用包名/app_参数名	默认可读写
 * Context.getFileStreamPath	/data/data/应用包名/files/download	默认可读写
 * Context.getObbDir	/storage/emulated/0/Android/obb/应用包名	默认可读写
 * Context.getExternalFilesDir	/storage/emulated/0/Android/data/应用包名/files/	默认可读写
 * Context.getExternalCacheDir	/storage/emulated/0/Android/data/应用包名/cache	默认可读写
 * Environment.getExternalStorageDirectory	/storage/emulated/0	6.0和以后需要申请权限
 * Environment.getExternalStoragePublicDirectory	/storage/emulated/0/Download	6.0和以后需要申请权限
 * Environment.getDownloadCacheDirectory	/cache	6.0和以后需要申请权限
 * Context.getRootDirectory	/system	不可以读写，需要 root 权限
 * <!--添加外部存储的读/写权限 -->
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
 * <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
 * <p>
 * 可参考https://www.123si.org/android/article/android-file-directory-and-permissions/
 */
public interface UserFileService {
    /**
     * 不需要权限
     * 规则 如果sd卡挂载 就用sd 否则用私有区域
     * differUser=false data/data/app包名/files/     或者/mnt/sdcard/Android/data/app包名/files/
     * differUser=true data/data/app包名/files/userId/    或者/mnt/sdcard/Android/data/app包名/files/userId/
     *
     * @param differUser      区分用户
     * @param forceInnerFiles 是否强制使用私有区域存储
     * @return
     */
    default Observable<File> getFilesDir(boolean differUser, boolean forceInnerFiles) {
        return Observable.fromCallable(new Callable<File>() {
            @Override
            public File call() throws Exception {
                File dir = ApplicationInitializer.applicationContext.getFilesDir();
                if (!forceInnerFiles && Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                    dir = ApplicationInitializer.applicationContext.getExternalFilesDir(null);
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
                File dir = ApplicationInitializer.applicationContext.getCacheDir();
                if (!forceInnerCache && Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                    dir = ApplicationInitializer.applicationContext.getExternalCacheDir();
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
