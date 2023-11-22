package com.xxf.arch.service;


import com.xxf.application.initializer.ApplicationInitializer;
import com.xxf.utils.FileUtils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.functions.Supplier;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * @Description: 文件服务
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/6/14 12:01
 */
public interface StringFileService extends UserFileService {
    /**
     * 读取
     *
     * @return
     */
    default Observable<String> readFileString(File file) {
        return Observable
                .defer(new Supplier<ObservableSource<? extends String>>() {
                    @Override
                    public ObservableSource<? extends String> get() throws Throwable {
                        if (FileUtils.isFileExists(ApplicationInitializer.applicationContext,file)) {
                            try (FileReader fr = new FileReader(file)) {
                                char[] bt = new char[1024];
                                StringBuffer sb = new StringBuffer();
                                while (fr.read(bt) != -1) {
                                    sb.append(bt);
                                    java.util.Arrays.fill(bt, (char) 0);
                                }
                                return Observable.just(sb.toString());
                            }
                        }
                        return Observable.empty();
                    }
                }).subscribeOn(Schedulers.io());
    }

    /**
     * 写
     *
     * @param file
     * @param content
     * @param append
     * @return
     */
    default Observable<File> writeFileString(File file, String content, boolean append) {
        return Observable
                .defer(new Supplier<ObservableSource<? extends File>>() {
                    @Override
                    public ObservableSource<? extends File> get() throws Throwable {
                        FileUtils.createOrExistsFile(file);
                        try (FileWriter fw = new FileWriter(file, append)) {
                            fw.write(content);
                            fw.flush();
                        }
                        return Observable.just(file);
                    }
                }).subscribeOn(Schedulers.io());

    }

    /**
     * 获取文件
     *
     * @param childFileName   ！！！注意是子文件eg. xx.doc； "doc/xxx.doc" 不要包含根路径
     * @param differUser      是否区分用户
     * @param forceInnerFiles 是否强制使用私有区域存储
     * @return
     */
    default Observable<String> getFilesString(String childFileName, boolean differUser, boolean forceInnerFiles) {
        return getFilesDir(differUser, forceInnerFiles)
                .flatMap(new Function<File, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(File file) throws Exception {
                        return readFileString(new File(file, childFileName));
                    }
                });
    }


    /**
     * 添加文件
     *
     * @param childFileName   ！！！注意是子文件eg. xx.doc； "doc/xxx.doc" 不要包含根路径
     * @param differUser      是否区分用户
     * @param content
     * @param append          是否追加模式,否则覆盖
     * @param forceInnerFiles 是否强制使用私有区域存储
     * @return
     */
    default Observable<File> putFilesString(String childFileName, String content, boolean append, boolean differUser, boolean forceInnerFiles) {
        return getFilesDir(differUser, forceInnerFiles)
                .flatMap(new Function<File, ObservableSource<File>>() {
                    @Override
                    public ObservableSource<File> apply(File file) throws Throwable {
                        return writeFileString(new File(file, childFileName), content, append);
                    }
                });
    }


    /**
     * 获取文件
     *
     * @param childFileName   ！！！注意是子文件eg. xx.doc； "doc/xxx.doc" 不要包含根路径
     * @param differUser      是否区分用户
     * @param forceInnerCache 是否强制使用私有区域存储
     * @return
     */
    default Observable<String> getCacheString(String childFileName, boolean differUser, boolean forceInnerCache) {
        return getCacheDir(differUser, forceInnerCache)
                .flatMap(new Function<File, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(File file) throws Exception {
                        return readFileString(new File(file, childFileName));
                    }
                });
    }


    /**
     * 添加文件
     *
     * @param childFileName   ！！！注意是子文件eg. xx.doc； "doc/xxx.doc" 不要包含根路径
     * @param differUser      是否区分用户
     * @param content
     * @param append          是否追加模式,否则覆盖
     * @param forceInnerCache 是否强制使用私有区域存储
     * @return
     */
    default Observable<File> putCacheString(String childFileName, String content, boolean append, boolean differUser, boolean forceInnerCache) {
        return getCacheDir(differUser, forceInnerCache)
                .flatMap(new Function<File, ObservableSource<File>>() {
                    @Override
                    public ObservableSource<File> apply(File file) throws Throwable {
                        return writeFileString(new File(file, childFileName), content, append);
                    }
                });
    }


}
