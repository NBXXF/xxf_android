package com.xxf.arch.service;

import com.xxf.arch.http.OkHttpClientBuilder;
import com.xxf.arch.model.DownloadTask;
import com.xxf.arch.utils.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Callable;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;


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
    public Observable<File> download(String url, String filePath) {
        return Observable
                .fromCallable(new Callable<Response>() {
                    @Override
                    public Response call() throws Exception {
                        return new OkHttpClientBuilder()
                                .build().newCall(new Request.Builder()
                                        .url(url)
                                        .build()).execute();
                    }
                })
                .subscribeOn(Schedulers.io())
                .map(new Function<Response, File>() {
                    @Override
                    public File apply(Response response) throws Throwable {
                        File file = new File(filePath);
                        FileUtils.writeFileFromIS(file, response.body().byteStream(), false);
                        return file;
                    }
                });
    }

    @Override
    public Observable<DownloadTask> downloadTask(String url, String filePath) {
        return Observable
                .create(new ObservableOnSubscribe<DownloadTask>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<DownloadTask> emitter) throws Throwable {
                        Call call = new OkHttpClientBuilder().build().newCall(new Request.Builder()
                                .url(url)
                                .build()
                        );
                        Response response = call.execute();
                        long currentLength = 0;
                        OutputStream os = null;
                        InputStream is = null;
                        try {
                            is = response.body().byteStream();
                            long totalLength = response.body().contentLength();
                            FileUtils.createOrExistsFile(filePath);
                            os = new FileOutputStream(filePath);
                            int len;
                            long step = totalLength / 100;
                            if (step > 524288) {
                                step = 524288;
                            } else if (step < 0) {
                                step = totalLength;
                            }
                            byte[] buff = new byte[(int) step];
                            DownloadTask downloadTask = new DownloadTask(url, filePath, totalLength);
                            while ((len = is.read(buff)) != -1) {
                                if (emitter.isDisposed()) {
                                    return;
                                }
                                os.write(buff, 0, len);
                                currentLength += len;
                                downloadTask.setCurrent(currentLength);
                                emitter.onNext(downloadTask.clone());
                            }
                            emitter.onComplete();
                        } finally {
                            if (os != null) {
                                try {
                                    os.close();
                                } catch (Exception ignored) {
                                }
                            }
                            try {
                                is.close();
                                is = null;
                            } catch (Exception ignored) {
                            }
                        }
                    }
                }).subscribeOn(Schedulers.io());
    }
}


