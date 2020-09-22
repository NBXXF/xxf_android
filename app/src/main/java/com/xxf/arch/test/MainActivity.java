package com.xxf.arch.test;

import android.Manifest;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.JsonObject;
import com.xxf.arch.XXF;
import com.xxf.arch.activity.XXFActivity;
import com.xxf.arch.core.activityresult.ActivityResult;
import com.xxf.arch.presenter.XXFLifecyclePresenter;
import com.xxf.arch.presenter.XXFNetwrokPresenter;
import com.xxf.arch.test.http.LoginApiService;
import com.xxf.arch.utils.ToastUtils;
import com.xxf.view.cardview.CardView;
import com.xxf.view.utils.ResourcesUtil;
import com.xxf.view.utils.StatusBarUtils;

import java.util.Arrays;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.functions.BiPredicate;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
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


        double d = 0.0000f;
        XXF.getLogger().d(String.format("===========>d:%s==0  %s", d, String.valueOf(d == 0)));

        Integer integer = Double.valueOf("0.09111").intValue();
        XXF.getLogger().d("===========>ssss" + integer);

        new XXFNetwrokPresenter(this, null) {
            @Override
            protected void onNetworkAvailable(Network network) {
                XXF.getLogger().d("===========>net yes1:");
            }

            @Override
            protected void onNetworkLost(Network network) {
                XXF.getLogger().d("===========>net no1:");
            }
        };
        XXF.registerNetworkCallback(new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                super.onAvailable(network);
                XXF.getLogger().d("===========>net yes:");
            }

            @Override
            public void onLost(Network network) {
                super.onLost(network);
                XXF.getLogger().d("===========>net no:");
            }
        });
        setContentView(R.layout.activity_main);
        StatusBarUtils.setTransparentForWindow(this);
        StatusBarUtils.setStatusBarCustomerView(this, findViewById(R.id.statusbarLayout));

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
                        Observable.fromCallable(new Callable<Object>() {
                            @Override
                            public Object call() throws Exception {
                                ResourcesUtil.checkResources(Arrays.asList(
                                        R.string.block_canary_delete,
                                        R.string.leak_canary_delete,
                                        R.string.leak_canary_display_activity_label,
                                        R.string.leak_canary_shortcut_label,
                                        R.string.block_canary_share_with,
                                        R.string.leak_canary_share_with,
                                        R.string.leak_canary_share_with,
                                        R.string.block_canary_delete_all,
                                        R.string.leak_canary_delete_all,
                                        R.string.abc_searchview_description_search,
                                        R.string.search_menu_title,
                                        R.string.leak_canary_generating_hq_bitmap_toast_failure_notice,
                                        R.string.leak_canary_generating_hq_bitmap_toast_notice,
                                        R.string.leak_canary_notification_message));
                                return true;
                            }
                        }).subscribeOn(Schedulers.io())
                                .doOnError(new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        System.out.println("===========" + throwable.getMessage());
                                    }
                                })
                                .subscribe();
                        //startActivity(new Intent(view.getContext(), StateActivity.class));
                        //ToastUtils.showToast("hello" + System.currentTimeMillis(), ToastUtils.ToastType.SUCCESS);
                    }
                });
        findViewById(R.id.bt_http)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        XXF.getApiService(LoginApiService.class)
                                .getCity(CacheType.firstCache)
                                .subscribe(new Consumer<JsonObject>() {
                                    @Override
                                    public void accept(JsonObject jsonObject) throws Exception {
                                        XXF.getLogger().d("==========>retry ye:" + jsonObject);
                                    }
                                }, new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        XXF.getLogger().d("==========>retry no:" + throwable);
                                    }
                                });
                    }
                });
        findViewById(R.id.file)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String fileName = "xxxx/test3.text";
                        XXF.getFileService().putPrivateFile(fileName, "xx\n" + System.currentTimeMillis(), true)
                                .compose(XXF.bindToErrorNotice())
                                .subscribe();
                        XXF.getFileService().getPrivateFile(fileName)
                                .compose(XXF.bindToErrorNotice())
                                .subscribe(new Consumer<String>() {
                                    @Override
                                    public void accept(String s) throws Exception {
                                        ToastUtils.showToast("yes:" + s, ToastUtils.ToastType.ERROR);
                                    }
                                });
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

                        ARouter.getInstance().build("/activity/test?name=1&age=" + System.currentTimeMillis()).navigation();
                 /*       XXF.startActivityForResult("/activity/test", bundle, 1000)
                                .compose(XXF.bindToErrorNotice())
                                .take(1)
                                .subscribe(new Consumer<ActivityResult>() {
                                    @Override
                                    public void accept(ActivityResult activityResult) throws Exception {
                                        Log.d("=======>result:", activityResult.getData().getStringExtra(ACTIVITY_RESULT));
                                    }
                                });*/
                    }
                });
        //FragmentUtils.addFragment(getSupportFragmentManager(), new TestFragment(), R.id.contentPanel);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Toast.makeText(this,"abcd",Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showToast("testxxx", ToastUtils.ToastType.ERROR);
            }
        }, 2000);
        XXF.getLogger().d("=============isBack stop:" + XXF.getActivityStackProvider().isBackground());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("======>onActResult:", "" + this + "_");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        XXF.getLogger().d("=============isBack:" + XXF.getActivityStackProvider().isBackground());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
