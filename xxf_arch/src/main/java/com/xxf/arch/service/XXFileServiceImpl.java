package com.xxf.arch.service;

import android.Manifest;
import android.content.Context;
import android.os.Environment;

import androidx.annotation.RequiresPermission;

import com.xxf.arch.XXF;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @Description: 文件服务 实现
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/6/14 12:02
 */
class XXFileServiceImpl implements XXFFileService {
    static volatile XXFileServiceImpl xxFileService;

    public static XXFileServiceImpl getInstance() {
        if (xxFileService == null) {
            synchronized (XXFileServiceImpl.class) {
                if (xxFileService == null) {
                    xxFileService = new XXFileServiceImpl();
                }
            }
        }
        return xxFileService;
    }

    @Override
    public Observable<String> getUserPrivateFile(String childFileName) {
        return getUserPrivateFileDir()
                .flatMap(new Function<File, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(File file) throws Exception {
                        return readFileString(file, childFileName);
                    }
                });
    }

    /**
     * 读取
     *
     * @param dir
     * @param childFileName
     * @return
     */
    public Observable<String> readFileString(File dir, String childFileName) {
        return Observable.fromCallable(new Callable<File>() {
            @Override
            public File call() throws Exception {
                File file = new File(dir, childFileName);
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                if (!file.exists()) {
                    file.createNewFile();
                }
                return file;
            }
        }).map(new Function<File, String>() {
            @Override
            public String apply(File file) throws Exception {
                try (FileReader fr = new FileReader(file)) {
                    char[] bt = new char[1024];
                    StringBuffer sb = new StringBuffer();
                    while (fr.read(bt) != -1) {
                        sb.append(bt);
                        java.util.Arrays.fill(bt, (char) 0);
                    }
                    return sb.toString();
                }
            }
        });
    }

    @RequiresPermission(allOf = Manifest.permission.WRITE_EXTERNAL_STORAGE)
    @Override
    public Observable<String> getUserPublicFile(String childFileName) {
        return getUserPublicFileDir()
                .flatMap(new Function<File, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(File file) throws Exception {
                        return readFileString(file, childFileName);
                    }
                });
    }

    @Override
    public Observable<String> getPrivateFile(String childFileName) {
        return getPrivateFileDir()
                .flatMap(new Function<File, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(File file) throws Exception {
                        return readFileString(file, childFileName);
                    }
                });
    }

    @RequiresPermission(allOf = Manifest.permission.WRITE_EXTERNAL_STORAGE)
    @Override
    public Observable<String> getPublicFile(String childFileName) {
        return getPublicFileDir()
                .flatMap(new Function<File, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(File file) throws Exception {
                        return readFileString(file, childFileName);
                    }
                });
    }

    @Override
    public Observable<File> putUserPrivateFile(String childFileName, String content, boolean append) {
        return getUserPrivateFileDir()
                .flatMap(new Function<File, ObservableSource<File>>() {
                    @Override
                    public ObservableSource<File> apply(File file) throws Exception {
                        return writeFileString(file, childFileName, content, append);
                    }
                });
    }

    @RequiresPermission(allOf = Manifest.permission.WRITE_EXTERNAL_STORAGE)
    @Override
    public Observable<File> putUserPublicFile(String childFileName, String content, boolean append) {
        return getUserPublicFileDir()
                .flatMap(new Function<File, ObservableSource<File>>() {
                    @Override
                    public ObservableSource<File> apply(File file) throws Exception {
                        return writeFileString(file, childFileName, content, append);
                    }
                });
    }

    /**
     * 写
     *
     * @param dir
     * @param childFileName
     * @param content
     * @param append
     * @return
     */
    private Observable<File> writeFileString(File dir, String childFileName, String content, boolean append) {
        return Observable
                .fromCallable(new Callable<File>() {
                    @Override
                    public File call() throws Exception {
                        File file = new File(dir, childFileName);
                        if (!file.getParentFile().exists()) {
                            file.getParentFile().mkdirs();
                        }
                        if (!file.exists()) {
                            file.createNewFile();
                        }
                        return file;
                    }
                }).map(new Function<File, File>() {
                    @Override
                    public File apply(File file) throws Exception {
                        try (FileWriter fw = new FileWriter(file, append)) {
                            fw.write(content);
                            fw.flush();
                        }
                        return file;
                    }
                });
    }

    @Override
    public Observable<File> putPrivateFile(String childFileName, String content, boolean append) {
        return getPrivateFileDir()
                .flatMap(new Function<File, ObservableSource<File>>() {
                    @Override
                    public ObservableSource<File> apply(File file) throws Exception {
                        return writeFileString(file, childFileName, content, append);
                    }
                });
    }

    @RequiresPermission(allOf = Manifest.permission.WRITE_EXTERNAL_STORAGE)
    @Override
    public Observable<File> putPublicFile(String childFileName, String content, boolean append) {
        return getPublicFileDir()
                .flatMap(new Function<File, ObservableSource<File>>() {
                    @Override
                    public ObservableSource<File> apply(File file) throws Exception {
                        return writeFileString(file, childFileName, content, append);
                    }
                });
    }

    @Override
    public Observable<File> getUserPrivateFileDir() {
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

    @RequiresPermission(allOf = Manifest.permission.WRITE_EXTERNAL_STORAGE)
    @Override
    public Observable<File> getUserPublicFileDir() {
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

    @Override
    public Observable<File> getPrivateFileDir() {
        return Observable.fromCallable(new Callable<File>() {
            @Override
            public File call() throws Exception {
                File file = XXF.getApplication().getDir(
                        XXFileServiceImpl.class.getSimpleName(),
                        Context.MODE_PRIVATE);
                if (!file.exists()) {
                    file.mkdirs();
                }
                return file;
            }
        }).subscribeOn(Schedulers.io());
    }

    @RequiresPermission(allOf = Manifest.permission.WRITE_EXTERNAL_STORAGE)
    @Override
    public Observable<File> getPublicFileDir() {
        return Observable.fromCallable(new Callable<File>() {
            @Override
            public File call() throws Exception {
                File file = new File(
                        new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath())
                                .append(File.separator)
                                .append(XXF.getApplication().getPackageName())
                                .append(File.separator)
                                .append(XXFileServiceImpl.class.getSimpleName())
                                .toString());
                if (!file.exists()) {
                    file.mkdirs();
                }
                return file;
            }
        }).subscribeOn(Schedulers.io());
    }
}
