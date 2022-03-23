package com.xxf.arch.test;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import com.google.gson.annotations.JsonAdapter;
import com.xxf.arch.XXF;
import com.xxf.arch.activity.XXFActivity;
import com.xxf.arch.json.typeadapter.format.formatobject.NumberFormatObject;
import com.xxf.arch.json.typeadapter.format.impl.number.Number_KM_FormatTypeAdapter;
import com.xxf.arch.lifecycle.XXFLifecycleObserver;
import com.xxf.arch.presenter.XXFNetwrokPresenter;
import com.xxf.arch.presenter.XXFPresenter;
import com.xxf.arch.utils.ToastUtils;
import com.xxf.bus.ActionTypeEvent;
import com.xxf.utils.DateUtils;
import com.xxf.view.round.XXFRoundImageTextView;
import com.xxf.view.utils.StatusBarUtils;
import com.xxf.view.utils.SystemUtils;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import io.reactivex.rxjava3.schedulers.Schedulers;
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


    class Presenter extends XXFPresenter<Object> {

        public Presenter(@NonNull LifecycleOwner lifecycleOwner, Object view) {
            super(lifecycleOwner, view);
        }

        @Override
        public void onCreate() {
            super.onCreate();
            Log.d("================>p", "onCreate");
        }

        @Override
        public void onStart() {
            super.onStart();
            Log.d("================>p", "onStart");
        }

        @Override
        public void onResume() {
            super.onResume();
            Log.d("================>p", "onResume");
        }

        @Override
        public void onPause() {
            super.onPause();
            Log.d("================>p", "onPause");
        }

        @Override
        public void onStop() {
            super.onStop();
            Log.d("================>p", "onStop");
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
        @JsonAdapter(Number_KM_FormatTypeAdapter.class)
        NumberFormatObject p;
        BigDecimal bigDecimal;

        @Override
        public String toString() {
            return "TestModel{" +
                    "p=" + p +
                    ", bigDecimal=" + bigDecimal +
                    '}';
        }
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getLifecycle().addObserver(new DefaultLifecycleObserver() {
            @Override
            public void onCreate(@NonNull LifecycleOwner owner) {

            }

            @Override
            public void onResume(@NonNull LifecycleOwner owner) {
                Log.d("TAG", "onResume: "+owner);
            }

            @Override
            public void onPause(@NonNull LifecycleOwner owner) {
                Log.d("TAG", "onPause: "+owner);
            }
        });

        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Throwable {
                emitter.onNext(1);//第一次不发送
                emitter.onNext(-1);//第一次不发送
                Thread.sleep(400);
                emitter.onNext(2);
                emitter.onNext(200);
                Thread.sleep(400);
                emitter.onNext(3);
                Thread.sleep(900);
                emitter.onNext(4);
                Thread.sleep(400);
                emitter.onNext(5);
                Thread.sleep(700);
                emitter.onNext(6);
                Thread.sleep(900);
                emitter.onNext(7);

            }
        }).debounce(1000, TimeUnit.MILLISECONDS).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Throwable {
                Log.e("--------rxjava", integer.toString());
            }
        });
        this.getLifecycle().addObserver(
                new XXFLifecycleObserver() {
                });
        this.getLifecycle().addObserver(new LifecycleEventObserver() {
            @Override
            public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                Log.d("======>test:", "" + event);
            }
        });

        ActionTypeEvent actionTypeEvent = ActionTypeEvent.create("hello", "test");
        ActionTypeEvent actionTypeEvent2 = ActionTypeEvent.create("hello", "test");
        if (actionTypeEvent == actionTypeEvent2) {
            Log.d("=======>", " actionTypeEvent equals");
        }
        Log.d("=======>", " actionTypeEvent:" + actionTypeEvent);
        Log.d("=======>", " actionTypeEvent2:" + actionTypeEvent2);

        XXF.subscribeEvent(String.class)
                .observeOn(AndroidSchedulers.mainThread())
                .to(XXF.bindLifecycle(this, Lifecycle.Event.ON_PAUSE))
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d("", "==============>收到事件:" + s + "  thread:" + Thread.currentThread().getName());
                    }
                });

        double d = 0.0000f;
        Log.d("", String.format("===========>d:%s==0  %s", d, String.valueOf(d == 0)));

        Integer integer = Double.valueOf("0.09111").intValue();
        Log.d("", "===========>ssss" + integer);

        new XXFNetwrokPresenter(this, null) {
            @Override
            protected void onNetworkAvailable(Network network) {
                Log.d("", "===========>net yes1:");
            }

            @Override
            protected void onNetworkLost(Network network) {
                Log.d("", "===========>net no1:");
            }
        };
        XXF.registerNetworkCallback(new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                super.onAvailable(network);
                Log.d("", "===========>net yes:");
            }

            @Override
            public void onLost(Network network) {
                super.onLost(network);
                Log.d("", "===========>net no:");
            }
        });
        setContentView(R.layout.activity_main);
        XXFRoundImageTextView imageTextView = findViewById(R.id.textImage);
//        imageTextView.getTextView().setText("xxxxHGFDGHDFGHGH");
//        imageTextView.getImageView().setImageResource(R.drawable.xxf_ic_toast_success);

        StatusBarUtils.setTransparentForWindow(this);
        StatusBarUtils.setStatusBarCustomerView(this, findViewById(R.id.statusbarLayout));

        new Presenter(this, this);


        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.d("", "========>error:", throwable);
            }
        });


        XXF.getFileService()
                .putFilesString("test.txt", "173256abs", false, false, false)
                .compose(XXF.bindToErrorNotice())
                .to(XXF.bindLifecycle(this))
                .subscribe();


        findViewById(R.id.bt_test)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //ToastUtils.showSnackBar(view,"hello" + System.currentTimeMillis(), ToastUtils.ToastType.ERROR);
                        ToastUtils.showSnackBar("hello" + System.currentTimeMillis(), ToastUtils.ToastType.ERROR);
                        if (true) {
                            return;
                        }
                        TestDialogFragment test = new TestDialogFragment();
                        test.getComponentObservable().subscribe(new Consumer<Pair<DialogFragment, String>>() {
                            @Override
                            public void accept(Pair<DialogFragment, String> dialogFragmentStringPair) throws Throwable {
                                Log.d("", "========>订阅:" + dialogFragmentStringPair.second);
                                dialogFragmentStringPair.first.dismissAllowingStateLoss();
                            }
                        });
                        test.show(getSupportFragmentManager(), TestDialogFragment.class.getName());
                        if (true) {
                            return;
                        }
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                XXF.postEvent("发送:" + System.currentTimeMillis());
                            }
                        }).start();

                        String url = "/activity/test";

                        String json = "{\n" +
                                "  \"num\": \"1948367743.1273676543\",\n" +
                                "  \"num1\": 1948367743.1273676543,\n" +
                                "  \"percent\": \"0.1273676543\",\n" +
                                "  \"percent2\": 15.1273676543,\n" +
                                "  \"percent3\": -35.1273676543,\n" +
                                "  \"percent4\": 71.1273676543,\n" +
                                "  \"time\": \"2020-12-05T07:35:57\",\n" +
                                "  \"money\": 3456435.32674335\n" +
                                "}";
                          /*   // startActivity(new Intent(view.getContext(), StateActivity.class));
                        //ToastUtils.showToast("hello" + System.currentTimeMillis(), ToastUtils.ToastType.SUCCESS);
                        System.out.println("============>f2:" + NumberUtils.divide(10.2, 2) + "  " + (new BigDecimal(10.2).divide(new BigDecimal(2))));
                        System.out.println("============>f:" + NumberUtils.formatRoundUp("5.5", 0, 0));
                        System.out.println("============>f:" + NumberUtils.formatRoundUp("0", 0, 0));
                        System.out.println("============>f:" + NumberUtils.formatRoundUp("-10.24", 0, 0));
                        System.out.println("============>f:" + NumberUtils.formatRoundDown("5.5", 0, 0));
                        System.out.println("============>f:" + NumberUtils.formatRoundHalfUp("5.5", 0, 0));
                        System.out.println("============>f:" + NumberUtils.format("325.5", 0, 5, RoundingMode.HALF_UP, 3));
                        System.out.println("============>f:" + NumberUtils.format("325.5", 0, 5, RoundingMode.HALF_UP, 4));
                        System.out.println("============>f:" + NumberUtils.format("325.5", 0, 5, RoundingMode.HALF_UP, 5));
                        System.out.println("============>f:" + NumberUtils.format("325.578435643", 0, 5, RoundingMode.HALF_UP, 6));
                        System.out.println("============>f:" + NumberUtils.format("-325.578435643", 0, 5, RoundingMode.HALF_UP, true));
                        System.out.println("============>f:" + NumberUtils.format("0", 0, 5, RoundingMode.HALF_UP, true));
                        System.out.println("============>f:" + NumberUtils.format("325.578435643", 0, 5, RoundingMode.HALF_UP, true));
                        System.out.println("============>f2:" + NumberUtils.add(10, 20));
                        System.out.println("============>f2:" + NumberUtils.add(10.5, 11.5));
                        System.out.println("============>f2:" + NumberUtils.add(10.2, 10.1));
                        System.out.println("============>f2:" + NumberUtils.multiply(10.2, 2).doubleValue());
                        System.out.println("============>f2:" + NumberUtils.divide(10.2, 2).doubleValue());
                        System.out.println("============>f2:" + NumberUtils.subtract(10.2, 1.1));
                        System.out.println("============>f3:" + NumberUtils.min(1, 3));
                        System.out.println("============>f3:" + NumberUtils.min(new BigDecimal(1), new BigDecimal(2)));
                        System.out.println("============>f3:" + NumberUtils.max(new BigDecimal(1), new BigDecimal(2)));
                        System.out.println("============>f3:" + NumberUtils.inClosedRange(new BigDecimal(1), new BigDecimal(1), new BigDecimal(2)));
                        System.out.println("============>f3:" + NumberUtils.inOpenedRange(new BigDecimal(1), new BigDecimal(1), new BigDecimal(2)));
                        System.out.println("============>f4:" + NumberUtils.compare(1, 3.5));
                        System.out.println("============>f4:" + NumberUtils.max(new BigDecimal(1), new BigDecimal(3.5), new BigDecimal(2.5)));
                        System.out.println("============>f4:" + NumberUtils.min(new BigDecimal(1), new BigDecimal(-3.5), new BigDecimal(2.5)));
                        float d = 1.9f;
                        System.out.println("============>fdd00:" + new BigDecimal(d).scale());
                        System.out.println("============>fdd01:" + new BigDecimal("1.98387").scale());
                        System.out.println("============>fdd002:" + 1.12534564365654365436543654654356436565436543654);
                        System.out.println("============>fdd:" + NumberUtils.formatRoundDown("1.9", 0, 8));
                        System.out.println("============>fdd2:" + NumberUtils.formatRoundDown(d, 0, 8));
                        System.out.println("============>fdd3:" + formatNumberDynamicScaleNoGroup(d, 11, 0, 8, true));


                        String picFilePath = "aaa.png";
                        Bitmap cacheBitmap = BitmapUtils.createBitmap((View) findViewById(R.id.scrollView));
                        File shareFile = new File(Environment.getExternalStorageDirectory(), picFilePath);
                        try {
                            if (!shareFile.exists()) {
                                shareFile.createNewFile();
                            }
                            saveImage(shareFile, cacheBitmap, 100);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                        SystemUtils.shareText(MainActivity.this, "http://www.baidu.com").subscribe();*/
                    }
                });
        findViewById(R.id.bt_http)
                .setOnClickListener(new View.OnClickListener() {
                    boolean first = true;

                    @Override
                    public void onClick(View view) {
                        Log.d("======>hell", "========");
                        ((TextView) view).setText("http " + System.currentTimeMillis());
                    }
                });
        findViewById(R.id.file)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivityForResult(new Intent(v.getContext(), PdfMainActivity.class), 1001);
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
                        XXF.getFileService().putFilesString(fileName, s, false, false, false)
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
                                        XXF.getFileService().putFilesString("bbb.txt", stringBuffer.toString(), false, false, false)
                                                .subscribe();
                                        XXF.getFileService().putCacheString("bbb.txt", stringBuffer.toString(), false, false, false)
                                                .subscribe();
                                        Log.d("", "============>yes:" + stringBuffer.toString());
                                    }
                                }, new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        Log.d("", "============>no:" + throwable);
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
                        XXF.getFileService().putFilesString(fileName, s, false, false, false)
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


                        XXF.requestPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)

                                .subscribe(new Consumer<Boolean>() {
                                    @Override
                                    public void accept(Boolean aBoolean) throws Exception {
                                        Log.d("==========>", "requestPermission:" + aBoolean);
                                        ToastUtils.showToast("Manifest.permission.CAMERA:" + aBoolean, ToastUtils.ToastType.ERROR);
                                    }
                                });
                        TestDialogFragment dialogFragment = new TestDialogFragment();
                        dialogFragment.show(getSupportFragmentManager(), "hello");
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
                        SystemUtils.selectFile(MainActivity.this)
                                .subscribe(new Consumer<String>() {
                                    @Override
                                    public void accept(String s) throws Throwable {
                                        Log.d("=====>path", s);
                                    }
                                });

//                        SystemUtils.takeVideo(MainActivity.this, new Bundle())
//                                .subscribe(new Consumer<String>() {
//                                    @Override
//                                    public void accept(String s) throws Throwable {
//                                        Log.d("=====>video path", s);
//                                    }
//                                });
                        Bundle bundle = new Bundle();
                        bundle.putString("ACTIVITY_PARAM", "one");

                        //  ARouter.getInstance().build("/activity/test?name=1&age=" + System.currentTimeMillis()).navigation();
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

        findViewById(R.id.bt_sp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), SpActivity.class));
            }
        });
    }

    public void jumpState(View v) {
        startActivity(new Intent(this, StateActivity.class));
    }

    public static void saveImage(File file, Bitmap bitmap, int quality) throws IOException {
        if (bitmap == null || file == null) {
            return;
        }
        FileOutputStream fos = new FileOutputStream(file);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
        byte[] bytes = stream.toByteArray();
        fos.write(bytes);
        fos.close();
    }

    public static Double log(double value, double base) {
        double logBase = Math.log(base);
        if (logBase == 0) {
            return null;
        }
        return Math.log(value) / Math.log(base);
    }

    public static Double log10(double value) {
        return log(value, 10.0);
    }

    /**
     * @param number
     * @param maxLength 数字总长度
     * @param minScale  最小小数位
     * @param maxScale  最大小数位
     *                  该方法可能造成精度丢失
     * @return
     */
    @Deprecated
    public static String formatNumberDynamicScaleNoGroup(Number number, int maxLength, int minScale, int maxScale, boolean down) {
        NumberFormat nf9 = NumberFormat.getNumberInstance();
        nf9.setGroupingUsed(false);
        if (down) {
            nf9.setRoundingMode(RoundingMode.DOWN);
        }
        if (number == null || number.doubleValue() == 0) {
            return minScale != 0 ? "0.00" : "0";
        }
        //计算小数位数
        double log10 = log10(number.doubleValue());
        int log10Int = (int) log10 + 1;
        if (log10Int + maxScale > maxLength) {
            maxScale = maxLength - log10Int;
        }
        if (log10Int + minScale > maxLength) {
            minScale = maxLength - log10Int;
        }
        if (maxScale < 0) {
            maxScale = 0;
        }
        if (minScale < 0) {
            minScale = 0;
        }
        nf9.setMaximumFractionDigits(maxScale);
        nf9.setMinimumFractionDigits(minScale);
        Log.d("", "========>min:" + minScale + "  " + maxScale);
        return nf9.format(number);
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
        Log.d("", "=============isBack stop:" + XXF.getActivityStackProvider().isBackground());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("======>onActResult:", "" + this + "_");
    }

    @Override
    protected void onResume() {
        super.onResume();

        new Thread(new Runnable() {
            @Override
            public void run() {
                long start = System.currentTimeMillis();
                for (int i = 0; i < 100000; i++) {
                    long time = System.currentTimeMillis();
                    String s = DateFormat.format("yyyy-M-d H:mm", time).toString();
                }
                System.out.println("=============>java DateFormat:" + (System.currentTimeMillis() - start));

                start = System.currentTimeMillis();
                for (int i = 0; i < 100000; i++) {
                    long time = System.currentTimeMillis();
                    String format = DateUtils.INSTANCE.format("YYYY-M-d H:mm", time);
                }
                System.out.println("=============>java FastDateFormat:" + (System.currentTimeMillis() - start));
            }
        }).start();
    }

}
