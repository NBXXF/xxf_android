package com.xxf.arch.core.permission;

import androidx.annotation.NonNull;

import com.xxf.arch.exception.PermissionDeniedException;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.ObservableTransformer;
import io.reactivex.rxjava3.functions.Function;


/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @version 2.3.1
 * @Description 未授权 转换成错误信号
 * @Company Beijing icourt
 * @date createTime：2018/9/3
 */
public class RxPermissionTransformer implements ObservableTransformer<Boolean, Boolean> {
    final String permission;

    public RxPermissionTransformer(@NonNull String permission) {
        this.permission = permission;
    }

    @Override
    public ObservableSource<Boolean> apply(Observable<Boolean> upstream) {
        return upstream.observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Boolean, Boolean>() {
                    @Override
                    public Boolean apply(Boolean granted) throws Exception {
                        if (!granted) {
                            throw new PermissionDeniedException(permission);
                        }
                        return granted;
                    }
                });
    }
}
