package com.xxf.arch.test;

import android.Manifest;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.xxf.arch.XXF;
import com.xxf.arch.activity.XXFActivity;
import com.xxf.arch.core.activityresult.ActivityResult;
import com.xxf.arch.presenter.XXFLifecyclePresenter;
import com.xxf.arch.test.http.LoginApiService;
import com.xxf.arch.utils.ToastUtils;
import com.xxf.view.cardview.CardView;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import retrofit2.CacheType;


public class MainActivity extends XXFActivity {
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
                    .compose(XXF.bindToLifecycle(this));
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

    private Observable<Object> getXXData() {
        return Observable.fromCallable(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                return "resppnse";
            }
        }).subscribeOn(Schedulers.io());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        XXF.registerNetworkCallback(new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                super.onAvailable(network);
                XXF.getLogger().d("===========>yes:");
            }

            @Override
            public void onLost(Network network) {
                super.onLost(network);
                XXF.getLogger().d("===========>no:");
            }
        });
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


        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.d("========>error:", Log.getStackTraceString(throwable));
            }
        });


        XXF.getFileService()
                .putPrivateFile("test.txt", "173256abs", false)
                .compose(XXF.bindToErrorNotice())
                .subscribe();


        findViewById(R.id.bt_test)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(view.getContext(), StateActivity.class));
                    }
                });
        findViewById(R.id.bt_http)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        XXF.getApiService(LoginApiService.class)
                                .getCityError(CacheType.onlyRemote)
                                .compose(XXF.bindToErrorNotice())
                                .subscribe(new Consumer<Boolean>() {
                                    @Override
                                    public void accept(Boolean aBoolean) throws Exception {
                                        XXF.getLogger().d("========>x:" + aBoolean);
                                    }
                                });
                    }
                });

        findViewById(R.id.bt_permission_req)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        XXF.requestPermission(Manifest.permission.CAMERA)

                                .subscribe(new Consumer<Boolean>() {
                                    @Override
                                    public void accept(Boolean aBoolean) throws Exception {
                                        Log.d("==========>", "requestPermission:" + aBoolean);
                                        ToastUtils.showToast("Manifest.permission.CAMERA:" + aBoolean, ToastUtils.ToastType.ERROR);
                                    }
                                });
                    }
                });

        findViewById(R.id.bt_permission_get)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        ToastUtils.showToast("yes?" + XXF.isGrantedPermission(Manifest.permission.CAMERA), ToastUtils.ToastType.ERROR);
                    }
                });


        findViewById(R.id.bt_startActivityForResult)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString(ACTIVITY_PARAM, "one");

                        // ARouter.getInstance().build("/activity/test").navigation();
                        XXF.startActivityForResult("/activity/test", bundle, 1000)
                                .compose(XXF.bindToErrorNotice())
                                .take(1)
                                .subscribe(new Consumer<ActivityResult>() {
                                    @Override
                                    public void accept(ActivityResult activityResult) throws Exception {
                                        Log.d("=======>result:", activityResult.getData().getStringExtra(ACTIVITY_RESULT));
                                    }
                                });
                    }
                });
        //FragmentUtils.addFragment(getSupportFragmentManager(), new TestFragment(), R.id.contentPanel);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Toast.makeText(this,"abcd",Toast.LENGTH_SHORT).show();
        ToastUtils.showToast("xxxxx", ToastUtils.ToastType.ERROR);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("======>onActResult:", "" + this + "_");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
