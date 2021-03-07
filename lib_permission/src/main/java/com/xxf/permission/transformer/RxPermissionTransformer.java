package com.xxf.permission.transformer;

import android.content.Context;

import androidx.annotation.NonNull;

import com.xxf.permission.PermissionDeniedException;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.ObservableTransformer;
import io.reactivex.rxjava3.functions.Function;


/**
 * @version 2.3.1
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description 将不正确的信号 转换成错误信号
 * @date createTime：2018/9/3
 */
public class RxPermissionTransformer implements ObservableTransformer<Boolean, Boolean> {
    final String[] permission;
    final Context context;

    public RxPermissionTransformer(Context context, @NonNull String... permission) {
        this.permission = permission;
        this.context = context.getApplicationContext();
    }

    @Override
    public ObservableSource<Boolean> apply(Observable<Boolean> upstream) {
        return upstream.observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Boolean, Boolean>() {
                    @Override
                    public Boolean apply(Boolean granted) throws Exception {
                        if (!granted) {
                            throw new PermissionDeniedException(context,permission);
                        }
                        return granted;
                    }
                });
    }
}
