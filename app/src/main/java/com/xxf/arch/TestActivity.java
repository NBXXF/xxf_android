package com.xxf.arch;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.xxf.arch.permission.CameraPermissionTransformer;
import com.xxf.arch.permission.XXFPermissions;
import com.xxf.arch.utils.ToastUtils;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * @author youxuan  E-mail:youxuan@icourt.cc
 * @version 2.3.1
 * @Description
 * @Company Beijing icourt
 * @date createTime：2018/9/9
 */
public class TestActivity extends FragmentActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Disposable subscribe = new XXFPermissions(this)
                .request(Manifest.permission.CAMERA)
                .compose(new CameraPermissionTransformer(this,false))
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        ToastUtils.showToast(TestActivity.this, "permission:" + aBoolean);
                    }
                });
    }
}
