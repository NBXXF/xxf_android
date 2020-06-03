package com.xxf.arch.test;

import android.Manifest;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;

import com.google.gson.JsonObject;
import com.xxf.annotation.Router;
import com.xxf.arch.XXF;
import com.xxf.arch.presenter.XXFLifecyclePresenter;
import com.xxf.arch.test.http.LoginApiService;
import com.xxf.arch.utils.ToastUtils;
import com.xxf.view.cardview.CardView;

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

    class Presenter extends XXFLifecyclePresenter<Object> {

        public Presenter(@NonNull LifecycleOwner lifecycleOwner, Object view) {
            super(lifecycleOwner, view);
        }

        @Override
        public void onCreate() {
            super.onCreate();
            Observable.just(1)
                    .compose(XXF.bindToLifecycle(getLifecycleOwner()));
            Log.d("================>p", "onCreate");
        }

        @Override
        public void onPause() {
            super.onPause();
            Log.d("================>p", "onPause");
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            Log.d("================>p", "onDestroy");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CardView cardView = findViewById(R.id.card);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardView.setShadowColor(ColorStateList.valueOf(Color.BLUE), Color.RED, Color.YELLOW);
            }
        });
        new Presenter(this, this);
        new XXFLifecyclePresenter<Object>(MainActivity.this, null);

        XXF.getApiService(LoginApiService.class)
                .getCity()
                .subscribe(new Consumer<JsonObject>() {
                    @Override
                    public void accept(JsonObject jsonObject) throws Exception {
                        Log.d("============>", "d:" + jsonObject);
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
                        startActivity(new Intent(view.getContext(), StateActivity.class));

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
//                        new ActionSheetDialog(MainActivity.this, new Consumer<String>() {
//                            @Override
//                            public void accept(String s) throws Exception {
//                                ToastUtils.showToast("yes:" + s);
//                            }
//                        }).show();
                        //ToastUtils.showToast("Manifest.permission.CAMERA:" + XXF.isGrantedPermission(MainActivity.this, Manifest.permission.CAMERA));
                    }
                });


        findViewById(R.id.bt_startActivityForResult)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                    }
                });
        //FragmentUtils.addFragment(getSupportFragmentManager(), new TestFragment(), R.id.contentPanel);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("======>onActResult:", "" + this + "_" + data.getStringExtra("data"));
    }
}
