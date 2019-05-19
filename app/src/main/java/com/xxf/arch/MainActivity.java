package com.xxf.arch;

import android.Manifest;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.xxf.arch.core.activityresult.ActivityResult;
import com.xxf.arch.http.LoginApiService;
import com.xxf.arch.http.XXFHttp;
import com.xxf.arch.utils.ToastUtils;

import org.reactivestreams.Subscriber;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("==========>act1:", "" + this);
        super.onCreate(savedInstanceState);
        Log.d("==========>act2:", "" + this);
        setContentView(R.layout.activity_main);


        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.d("========>error:", Log.getStackTraceString(throwable));
            }
        });

        findViewById(R.id.bt_test)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Observable
                                .fromCallable(new Callable<Object>() {
                                    @Override
                                    public Object call() throws Exception {
                                        return null;
                                    }
                                })
                                .compose(XXF.bindToErrorNotice())
                                .subscribe();
                        Intent intent = new Intent(view.getContext(), TestActivity.class);
                        intent.putExtra("name", "xxx");
                        intent.putExtra("age", "12");
                        intent.putExtra("desc", "124");
                        startActivity(intent);
                    }
                });

        findViewById(R.id.bt_permission_req)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        XXF.requestPermission(MainActivity.this, Manifest.permission.CAMERA)
                                .subscribe(new Consumer<Boolean>() {
                                    @Override
                                    public void accept(Boolean aBoolean) throws Exception {
                                        ToastUtils.showToast(v.getContext(), "Manifest.permission.CAMERA:" + aBoolean);
                                    }
                                });
                    }
                });

        findViewById(R.id.bt_permission_get)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        ToastUtils.showToast(v.getContext(), "Manifest.permission.CAMERA:" + XXF.isGrantedPermission(MainActivity.this, Manifest.permission.CAMERA));
                    }
                });


        findViewById(R.id.bt_startActivityForResult)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        XXF.startActivityForResult(MainActivity.this, new Intent(MainActivity.this, TestActivity.class), 1001)
                                .subscribe(new Consumer<ActivityResult>() {
                                    @Override
                                    public void accept(ActivityResult activityResult) throws Exception {
                                        ToastUtils.showToast(v.getContext(), "activityResult:reqcode:" + activityResult.getRequestCode() + ";resCode" + activityResult.getResultCode() + ";data:" + activityResult.getData().getStringExtra("data"));

                                    }
                                });
                    }
                });
    }

}
