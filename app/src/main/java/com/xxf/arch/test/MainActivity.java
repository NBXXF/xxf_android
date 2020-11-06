package com.xxf.arch.test;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavCallback;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.JsonObject;
import com.xxf.arch.XXF;
import com.xxf.arch.activity.XXFActivity;
import com.xxf.arch.json.JsonUtils;
import com.xxf.arch.json.typeadapter.format.FormatDemoModel;
import com.xxf.arch.presenter.XXFLifecyclePresenter;
import com.xxf.arch.presenter.XXFNetwrokPresenter;
import com.xxf.arch.test.http.LoginApiService;
import com.xxf.arch.utils.ToastUtils;
import com.xxf.view.cardview.CardView;
import com.xxf.view.utils.StatusBarUtils;
import com.xxf.view.view.ReverseFrameLayout;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

    class TestModel {
        float p;

        @Override
        public String toString() {
            return "TestModel{" +
                    "p=" + p +
                    '}';
        }
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
        Log.d("========>t:", getResources().getResourceEntryName(R.id.card) + "    " + getResources().getResourceName(R.id.card));
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
                        Log.d("==========>yes:", "" + JsonUtils.toBean(" {\n" +
                                "    \"p\": \"51.2%\"\n" +
                                "  }", TestModel.class));

                        ReverseFrameLayout layout = findViewById(R.id.grayLayout);
                        layout.toggleColor();

                        //   String url = "qweqwe";
                        String url = null;
                        ARouter.getInstance().build(url).navigation(view.getContext(), new NavCallback() {
                            @Override
                            public void onArrival(Postcard postcard) {

                            }

                            @Override
                            public void onLost(Postcard postcard) {
                                super.onLost(postcard);
                                XXF.getLogger().d("=============>跳转失败" + postcard.getPath());
                            }
                        });

                        String json = "{\n" +
                                "  \"num\": \"1948367743.1273676543\",\n" +
                                "  \"num1\": 1948367743.1273676543,\n" +
                                "  \"percent\": \"0.1273676543\",\n" +
                                "  \"percent2\": 15.1273676543,\n" +
                                "  \"percent3\": -35.1273676543,\n" +
                                "  \"percent4\": 71.1273676543,\n" +
                                "  \"time\": \"1604631895000\",\n" +
                                "  \"money\": 3456435.32674335\n" +
                                "}";
                        FormatDemoModel testModel = JsonUtils.toBean(json, FormatDemoModel.class);
                        XXF.getLogger().d("===============>t:" + testModel);
                        // startActivity(new Intent(view.getContext(), StateActivity.class));
                        //ToastUtils.showToast("hello" + System.currentTimeMillis(), ToastUtils.ToastType.SUCCESS);
                    }
                });
        findViewById(R.id.bt_http)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        XXF.getApiService(LoginApiService.class)
                                .getCity(CacheType.lastCache)
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
                        String fileName = "aaa.text";
                        String s = "99% FC\n" +
                                "98% FA\n" +
                                "97% F7\n" +
                                "96% F5\n" +
                                "95% F2\n" +
                                "94% F0\n" +
                                "93% ED\n" +
                                "92% EB\n" +
                                "91% E8\n" +
                                "90% E6\n" +
                                "89% E3\n" +
                                "88% E0\n" +
                                "87% DE\n" +
                                "86% DB\n" +
                                "85% D9\n" +
                                "84% D6\n" +
                                "83% D4\n" +
                                "82% D1\n" +
                                "81% CF\n" +
                                "80% CC\n" +
                                "79% C9\n" +
                                "78% C7\n" +
                                "77% C4\n" +
                                "76% C2\n" +
                                "75% BF\n" +
                                "74% BD\n" +
                                "73% BA\n" +
                                "72% B8\n" +
                                "71% B5\n" +
                                "70% B3\n" +
                                "69% B0\n" +
                                "68% AD\n" +
                                "67% AB\n" +
                                "66% A8\n" +
                                "65% A6\n" +
                                "64% A3\n" +
                                "63% A1\n" +
                                "62% 9E\n" +
                                "61% 9C\n" +
                                "60% 99\n" +
                                "59% 96\n" +
                                "57% 94\n" +
                                "56% 91\n" +
                                "56% 8F\n" +
                                "55% 8C\n" +
                                "54% 8A\n" +
                                "53% 87\n" +
                                "52% 85\n" +
                                "51% 82\n" +
                                "50% 80\n" +
                                "49% 7D\n" +
                                "48% 7A\n" +
                                "47% 78\n" +
                                "46% 75\n" +
                                "45% 73\n" +
                                "44% 70\n" +
                                "43% 6E\n" +
                                "42% 6B\n" +
                                "41% 69\n" +
                                "40% 66\n" +
                                "39% 63\n" +
                                "38% 61\n" +
                                "37% 5E\n" +
                                "36% 5C\n" +
                                "35% 59\n" +
                                "34% 57\n" +
                                "33% 54\n" +
                                "32% 52\n" +
                                "31% 4F\n" +
                                "30% 4D\n" +
                                "29% 4A\n" +
                                "28% 47\n" +
                                "27% 45\n" +
                                "26% 42\n" +
                                "25% 40\n" +
                                "24% 3D\n" +
                                "23% 3B\n" +
                                "22% 38\n" +
                                "21% 36\n" +
                                "20% 33\n" +
                                "19% 30\n" +
                                "18% 2E\n" +
                                "17% 2B\n" +
                                "16% 29\n" +
                                "15% 26\n" +
                                "14% 24\n" +
                                "13% 21\n" +
                                "12% 1F\n" +
                                "11% 1C\n" +
                                "10% 1A\n" +
                                "9% 17\n" +
                                "8% 14\n" +
                                "7% 12\n" +
                                "6% 0F\n" +
                                "5% 0D\n" +
                                "4% 0A\n" +
                                "3% 08\n" +
                                "2% 05\n" +
                                "1% 03\n" +
                                "0% 00";
                        XXF.getFileService().putPrivateFile(fileName, s, false)
                                .compose(XXF.bindToErrorNotice())
                                .subscribe(new Consumer<File>() {
                                    @SuppressLint("CheckResult")
                                    @Override
                                    public void accept(File file) throws Exception {
                                        StringBuffer stringBuffer = new StringBuffer();
                                        try {
                                            FileReader fr = new FileReader(file.getAbsolutePath());
                                            BufferedReader bf = new BufferedReader(fr);
                                            String str;
                                            /**
                                             *  <string name="wallet_address_manager_01">请输入区块链地址</string>
                                             */
                                            // 按行读取字符串
                                            while ((str = bf.readLine()) != null) {
                                                String s = "<string name=\"_alpha_%s\">%s</string>";
                                                String[] s1 = str.split("%");
                                                stringBuffer.append(String.format(s, s1[0].trim(), "#" + s1[1].trim()));
                                            }
                                            bf.close();
                                            fr.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        XXF.getFileService().putPrivateFile("bbb.txt", stringBuffer.toString(), false)
                                                .subscribe();
                                        XXF.getLogger().d("============>yes:" + stringBuffer.toString());
                                    }
                                }, new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        XXF.getLogger().d("============>no:" + throwable);
                                    }
                                });
                    }
                });

        findViewById(R.id.bt_permission_req)
                .setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("CheckResult")
                    @Override
                    public void onClick(final View v) {
                        String fileName = "replace.text";
                        String s = "";
                        XXF.getFileService().putPrivateFile(fileName, s, false)
                                .subscribe(new Consumer<File>() {
                                    @Override
                                    public void accept(File file) throws Exception {
                                        List<String> list = new ArrayList<>();
                                        List<String> files = new ArrayList<>();
                                        try {
                                            FileReader fr = new FileReader(file.getAbsolutePath());
                                            BufferedReader bf = new BufferedReader(fr);
                                            String str;
                                            /**
                                             *  <string name="wallet_address_manager_01">请输入区块链地址</string>
                                             */
                                            // 按行读取字符串
                                            while ((str = bf.readLine()) != null) {
                                                list.add(str.substring(str.indexOf("#"), str.indexOf("</")));
                                                files.add(str.substring(str.indexOf("name="), str.indexOf(">#")));
                                            }
                                            bf.close();
                                            fr.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                        for (String item : list) {
                                            String s = "<string name=\"%s_alpha_%s\">%s</string>";
                                        }
                                    }
                                });


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
