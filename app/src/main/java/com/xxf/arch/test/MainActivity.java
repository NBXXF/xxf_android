package com.xxf.arch.test;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.xxf.annotation.Router;
import com.xxf.arch.XXF;
import com.xxf.arch.core.activityresult.ActivityResult;
import com.xxf.arch.json.JsonUtils;
import com.xxf.arch.json.ListTypeToken;
import com.xxf.arch.json.MapTypeToken;
import com.xxf.arch.test.http.LoginApiService;
import com.xxf.arch.utils.ToastUtils;
import com.xxf.view.actiondialog.BottomPicSelectDialog;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;


@Router(path = "/user/main")
public class MainActivity extends AppCompatActivity {
    public static class User<T> {
        private T t;

        public User(T t) {
            this.t = t;
        }

        @Override
        public String toString() {
            return "User{" +
                    "t=" + t +
                    '}';
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        XXF.getApiService(LoginApiService.class)
                .getCity()
                .subscribe(new Consumer<JsonObject>() {
                    @Override
                    public void accept(JsonObject jsonObject) throws Exception {
                        Log.d("============>","d:"+jsonObject);
                    }
                });

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
                        User<String> user = new User<>("xxx");
                        String s = JsonUtils.toJsonString(user);
                        User<String> userDes = JsonUtils.toBean(s, User.class, String.class);
                        Log.d("=========>d:", "" + userDes);

                        Intent intent = new Intent(MainActivity.this, TestActivity.class);
                        startActivityForResult(intent, 1001);
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
                                        ToastUtils.showToast("Manifest.permission.CAMERA:" + aBoolean);
                                    }
                                });
                    }
                });

        findViewById(R.id.bt_permission_get)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        new BottomPicSelectDialog(MainActivity.this, new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                ToastUtils.showToast("yes:" + s);
                            }
                        }).show();
                        //ToastUtils.showToast("Manifest.permission.CAMERA:" + XXF.isGrantedPermission(MainActivity.this, Manifest.permission.CAMERA));
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
                                        ToastUtils.showToast("activityResult:reqcode:" + activityResult.getRequestCode() + ";resCode" + activityResult.getResultCode() + ";data:" + activityResult.getData().getStringExtra("data"));

                                    }
                                });
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("======>onActResult:", "" + this + "_" + data.getStringExtra("data"));
    }
}
