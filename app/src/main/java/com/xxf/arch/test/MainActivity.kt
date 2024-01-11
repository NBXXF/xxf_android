package com.xxf.arch.test

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import android.os.Handler
import android.text.format.DateFormat
import android.util.Log
import android.util.Size
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.accessibility.AccessibilityEvent
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.google.gson.annotations.JsonAdapter
import com.xxf.arch.XXF
import com.xxf.arch.activity.XXFActivity
import com.xxf.arch.presenter.XXFNetwrokPresenter
import com.xxf.arch.test.prefs.PreferencesDemo
import com.xxf.toast.ToastType
import com.xxf.toast.ToastUtils
import com.xxf.toast.showToast
import com.xxf.bus.ActionTypeEvent.Companion.create
import com.xxf.json.typeadapter.format.formatobject.NumberFormatObject
import com.xxf.json.typeadapter.format.impl.number.Number_KM_FormatTypeAdapter
import com.xxf.log.logD
import com.xxf.utils.DateUtils.format
import com.xxf.utils.FileUtils
import com.xxf.utils.HandlerUtils.mainHandler
import com.xxf.view.recyclerview.layoutmanager.AutoFitGridLayoutManager
import com.xxf.view.round.XXFRoundImageTextView
import com.xxf.view.utils.StatusBarUtils
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.FileReader
import java.io.IOException
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.Calendar
import java.util.concurrent.Callable
import java.util.concurrent.TimeUnit

class MainActivity() : XXFActivity() {
    class User<T>(private val t: T) {
        override fun toString(): String {
            return ("User{" +
                    "t=" + t +
                    '}')
        }
    }

    private val xXData: Observable<Any>
        private get() = Observable.fromCallable(object : Callable<Any> {
            @Throws(Exception::class)
            override fun call(): Any {
                return "resppnse"
            }
        }).subscribeOn(Schedulers.io())

    internal inner class TestModel() {
        @JsonAdapter(Number_KM_FormatTypeAdapter::class)
        var p: NumberFormatObject? = null
        var bigDecimal: BigDecimal? = null
        override fun toString(): String {
            return ("TestModel{" +
                    "p=" + p +
                    ", bigDecimal=" + bigDecimal +
                    '}')
        }
    }

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PreferencesDemo.test()

        AutoFitGridLayoutManager.Builder(this, Size(199,100)).setSpacing(Size(10,10)).build()
        logD { "======================>xxxgffdd" }

        //ApplicationKtKt.launchAppDetailsSettings(this.getApplication(),this.getApplication().getPackageName());
        // ApplicationKtKt.launchSettings(this.getApplication());


//        setWindowSize((int)(ScreenUtils.getScreenWidth()*0.5),(int)(ScreenUtils.getScreenWidth() *0.5));

        // new TestDialog(this).show();
        TestNumber.test()
        TestUtils.test()
        val fileName = "x/x/xdgg\\ds..png"
        // String fileName="usgfgdf.pdf";
        val format = FileUtils.formatFileName(fileName)
        //  String format= TestUtils.INSTANCE.format(fileName);
        println(
            "===============>format:" + format + "   " + fileName + "  " + FileUtils.isLegalFileName(
                fileName
            )
        )
        println("============>time:" + format("yyyy-MM-dd", timeOfWeekStart))
        println("============>time2:" + format("yyyy-MM-dd", times))
        println("============>time3:" + format("yyyy-MM-dd", times3))
        this.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) {}
            override fun onResume(owner: LifecycleOwner) {
                Log.d("TAG", "onResume: $owner")
            }

            override fun onPause(owner: LifecycleOwner) {
                Log.d("TAG", "onPause: $owner")
            }
        })
        Observable.create(ObservableOnSubscribe<Int> { emitter ->
            emitter.onNext(1) //第一次不发送
            emitter.onNext(-1) //第一次不发送
            Thread.sleep(400)
            emitter.onNext(2)
            emitter.onNext(200)
            Thread.sleep(400)
            emitter.onNext(3)
            Thread.sleep(900)
            emitter.onNext(4)
            Thread.sleep(400)
            emitter.onNext(5)
            Thread.sleep(700)
            emitter.onNext(6)
            Thread.sleep(900)
            emitter.onNext(7)
        }).debounce(1000, TimeUnit.MILLISECONDS).subscribe(object : Consumer<Int> {
            @Throws(Throwable::class)
            override fun accept(integer: Int) {
                Log.e("--------rxjava", integer.toString())
            }
        })
        this.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                Log.d("======>test:", "" + event)
            }
        })
        val actionTypeEvent = create("hello", "test")
        val actionTypeEvent2 = create("hello", "test")
        if (actionTypeEvent == actionTypeEvent2) {
            Log.d("=======>", " actionTypeEvent equals")
        }
        Log.d("=======>", " actionTypeEvent:$actionTypeEvent")
        Log.d("=======>", " actionTypeEvent2:$actionTypeEvent2")
        val integer = java.lang.Double.valueOf("0.09111").toInt()
        Log.d("", "===========>ssss$integer")
        object : XXFNetwrokPresenter<Any?>(this, null) {
            override fun onNetworkAvailable(network: Network) {
                Log.d("", "===========>net yes1:")
            }

            override fun onNetworkLost(network: Network) {
                Log.d("", "===========>net no1:")
            }
        }
        XXF.registerNetworkCallback(object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                Log.d("", "===========>net yes:")
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                Log.d("", "===========>net no:")
            }
        })
        setContentView(R.layout.activity_main)
        val imageTextView = findViewById<XXFRoundImageTextView>(R.id.textImage)
        //        imageTextView.getTextView().setText("xxxxHGFDGHDFGHGH");
//        imageTextView.getImageView().setImageResource(R.drawable.xxf_ic_toast_success);
        StatusBarUtils.setTransparentForWindow(this)
        StatusBarUtils.setStatusBarCustomerView(this, findViewById(R.id.statusbarLayout))
        RxJavaPlugins.setErrorHandler(object : Consumer<Throwable?> {
            @Throws(Exception::class)
            override fun accept(throwable: Throwable?) {
                Log.d("", "========>error:", throwable)
            }
        })
        XXF.getFileService()
            .putFilesString("test.txt", "173256abs", false, false, false)
            .subscribe()
        findViewById<View>(R.id.bt_test)
            .setOnClickListener(object : View.OnClickListener {
                override fun onClick(view: View) {
                    startActivity(Intent(view.context, ReciveContentActivity::class.java))
                    //ToastUtils.showSnackBar(view,"hello" + System.currentTimeMillis(), ToastUtils.ToastType.ERROR);
//                        ToastUtils.showSnackBar("hello" + System.currentTimeMillis(), ToastUtils.ToastType.ERROR);
//                        TestDialogFragment test = TestDialogFragment.newInstance(new OnCallDataListener() {
//                            @Override
//                            public boolean test() {
//                                System.out.println("===========>test 发送:"+true+"  "+this.hashCode());
//                                return  true;
//                            }
//                        });
////                        test.getComponentObservable().subscribe(new Consumer<Pair<DialogFragment, String>>() {
////                            @Override
////                            public void accept(Pair<DialogFragment, String> dialogFragmentStringPair) throws Throwable {
////                                Log.d("", "========>订阅:" + dialogFragmentStringPair.second);
////                                dialogFragmentStringPair.first.dismissAllowingStateLoss();
////                            }
////                        });
//                        test.show(getSupportFragmentManager(), TestDialogFragment.class.getName());
//                        if (true) {
//                            return;
//                        }
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                XXF.postEvent("发送:" + System.currentTimeMillis());
//                            }
//                        }).start();
//
//                        String url = "/activity/test";
//
//                        String json = "{\n" +
//                                "  \"num\": \"1948367743.1273676543\",\n" +
//                                "  \"num1\": 1948367743.1273676543,\n" +
//                                "  \"percent\": \"0.1273676543\",\n" +
//                                "  \"percent2\": 15.1273676543,\n" +
//                                "  \"percent3\": -35.1273676543,\n" +
//                                "  \"percent4\": 71.1273676543,\n" +
//                                "  \"time\": \"2020-12-05T07:35:57\",\n" +
//                                "  \"money\": 3456435.32674335\n" +
//                                "}";
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
            })
        findViewById<View>(R.id.bt_http)
            .setOnClickListener(object : View.OnClickListener {
                override fun onClick(view: View) {
                    showToast("xxxxxxgfd")
                    //jumpPermissionSettingPage(this@MainActivity)
                }
            })
        findViewById<View>(R.id.file)
            .setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View) {
                    startActivityForResult(Intent(v.context, PdfMainActivity::class.java), 1001)
                    val fileName = "aaa.text"
                    val s = ("99% FC\n" +
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
                            "0% 00")
                    XXF.getFileService().putFilesString(fileName, s, false, false, false)
                        .subscribe(object : Consumer<File> {
                            @SuppressLint("CheckResult")
                            @Throws(Exception::class)
                            override fun accept(file: File) {
                                val stringBuffer = StringBuffer()
                                try {
                                    val fr = FileReader(file.absolutePath)
                                    val bf = BufferedReader(fr)
                                    var str: String
                                    /**
                                     * <string name="wallet_address_manager_01">请输入区块链地址</string>
                                     */
                                    // 按行读取字符串
                                    while ((bf.readLine().also { str = it }) != null) {
                                        val s = "<string name=\"_alpha_%s\">%s</string>"
                                        val s1 =
                                            str.split("%".toRegex()).dropLastWhile { it.isEmpty() }
                                                .toTypedArray()
                                        stringBuffer.append(
                                            String.format(
                                                s,
                                                s1[0].trim { it <= ' ' },
                                                "#" + s1[1].trim { it <= ' ' })
                                        )
                                    }
                                    bf.close()
                                    fr.close()
                                } catch (e: IOException) {
                                    e.printStackTrace()
                                }
                                XXF.getFileService().putFilesString(
                                    "bbb.txt",
                                    stringBuffer.toString(),
                                    false,
                                    false,
                                    false
                                )
                                    .subscribe()
                                XXF.getFileService().putCacheString(
                                    "bbb.txt",
                                    stringBuffer.toString(),
                                    false,
                                    false,
                                    false
                                )
                                    .subscribe()
                                Log.d("", "============>yes:$stringBuffer")
                            }
                        }, object : Consumer<Throwable> {
                            @Throws(Exception::class)
                            override fun accept(throwable: Throwable) {
                                Log.d("", "============>no:$throwable")
                            }
                        })
                }
            })
        findViewById<View>(R.id.bt_permission_req)
            .setOnClickListener(object : View.OnClickListener {
                @SuppressLint("CheckResult")
                override fun onClick(v: View) {
                    val fileName = "replace.text"
                    val s = ""
                    XXF.getFileService().putFilesString(fileName, s, false, false, false)
                        .subscribe(object : Consumer<File> {
                            @Throws(Exception::class)
                            override fun accept(file: File) {
                                val list: MutableList<String> = ArrayList()
                                val files: MutableList<String> = ArrayList()
                                try {
                                    val fr = FileReader(file.absolutePath)
                                    val bf = BufferedReader(fr)
                                    var str: String
                                    /**
                                     * <string name="wallet_address_manager_01">请输入区块链地址</string>
                                     */
                                    // 按行读取字符串
                                    while ((bf.readLine().also { str = it }) != null) {
                                        list.add(str.substring(str.indexOf("#"), str.indexOf("</")))
                                        files.add(
                                            str.substring(
                                                str.indexOf("name="),
                                                str.indexOf(">#")
                                            )
                                        )
                                    }
                                    bf.close()
                                    fr.close()
                                } catch (e: IOException) {
                                    e.printStackTrace()
                                }
                                for (item: String? in list) {
                                    val s = "<string name=\"%s_alpha_%s\">%s</string>"
                                }
                            }
                        })


//                        XXF.requestPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
//
//                                .subscribe(new Consumer<Boolean>() {
//                                    @Override
//                                    public void accept(Boolean aBoolean) throws Exception {
//                                        Log.d("==========>", "requestPermission:" + aBoolean);
//                                        ToastUtils.showToast("Manifest.permission.CAMERA:" + aBoolean, ToastUtils.ToastType.ERROR);
//                                    }
//                                });
                    val dialogFragment = TestDialogFragment()
                    dialogFragment.show(supportFragmentManager, "hello")
                }
            })
        findViewById<View>(R.id.bt_permission_get)
            .setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View) {}
            })
        findViewById<View>(R.id.bt_startActivityForResult)
            .setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View) {

//                        SystemUtils.takeVideo(MainActivity.this, new Bundle())
//                                .subscribe(new Consumer<String>() {
//                                    @Override
//                                    public void accept(String s) throws Throwable {
//                                        Log.d("=====>video path", s);
//                                    }
//                                });
                    val bundle = Bundle()
                    bundle.putString("ACTIVITY_PARAM", "one")

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
            })
        //FragmentUtils.addFragment(getSupportFragmentManager(), new TestFragment(), R.id.contentPanel);
        findViewById<View>(R.id.bt_sp).setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                startActivity(Intent(v.context, SpActivity::class.java))
            }
        })
    }

    fun jumpState(v: View?) {
        startActivity(Intent(this, StateActivity::class.java))
    }

    override fun onStop() {
        super.onStop()
        // Toast.makeText(this,"abcd",Toast.LENGTH_SHORT).show();
        Handler().postDelayed(object : Runnable {
            override fun run() {
                ToastUtils.showToast("testxxx", ToastType.ERROR)
            }
        }, 2000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("======>onActResult:", "" + this + "_")
    }

    override fun onResume() {
        super.onResume()
        mainHandler.postDelayed(object : Runnable {
            override fun run() {
                setWindowBackground(Color.RED)
            }
        }, 400)
        Thread(object : Runnable {
            override fun run() {
                var start = System.currentTimeMillis()
                for (i in 0..99999) {
                    val time = System.currentTimeMillis()
                    val s = DateFormat.format("yyyy-M-d H:mm", time).toString()
                }
                println("=============>java DateFormat:" + (System.currentTimeMillis() - start))
                start = System.currentTimeMillis()
                for (i in 0..99999) {
                    val time = System.currentTimeMillis()
                    val format = format("YYYY-M-d H:mm", time)
                }
                println("=============>java FastDateFormat:" + (System.currentTimeMillis() - start))
            }
        }).start()
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        println("=============>touch:" + (System.currentTimeMillis()))
        return super.dispatchTouchEvent(ev)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        println("=============>touch2:" + (System.currentTimeMillis()))
        return super.onKeyDown(keyCode, event)
    }

    override fun onKeyMultiple(keyCode: Int, repeatCount: Int, event: KeyEvent): Boolean {
        println("=============>touch3:" + (System.currentTimeMillis()))
        return super.onKeyMultiple(keyCode, repeatCount, event)
    }

    override fun dispatchGenericMotionEvent(ev: MotionEvent): Boolean {
        println("=============>touch5:" + (System.currentTimeMillis()))
        return super.dispatchGenericMotionEvent(ev)
    }

    override fun dispatchPopulateAccessibilityEvent(event: AccessibilityEvent): Boolean {
        println("=============>touch7:" + (System.currentTimeMillis()))
        return super.dispatchPopulateAccessibilityEvent(event)
    }

    override fun dispatchTrackballEvent(ev: MotionEvent): Boolean {
        println("=============>touch8:" + (System.currentTimeMillis()))
        return super.dispatchTrackballEvent(ev)
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        println("=============>touch4:" + (System.currentTimeMillis()))
        return super.dispatchKeyEvent(event)
    }

    companion object {
        val timeOfWeekStart: Long
            get() {
                val ca = Calendar.getInstance()
                ca[Calendar.HOUR_OF_DAY] = 0
                ca.clear(Calendar.MINUTE)
                ca.clear(Calendar.SECOND)
                ca.clear(Calendar.MILLISECOND)
                ca[Calendar.DAY_OF_WEEK] = ca.firstDayOfWeek
                return ca.timeInMillis
            }
        private val times: Long
            private get() {
                // 获取指定日期所在周的第一天(周一)
                val calendar = Calendar.getInstance()
                // 设置周一是第一天, getFirstDayOfWeek获取的默认是周日
                calendar.firstDayOfWeek = Calendar.MONDAY
                calendar[Calendar.DAY_OF_WEEK] = calendar.firstDayOfWeek
                return calendar.timeInMillis
            }
        private val times3: Long
            private get() {
                val weekStart = Calendar.getInstance()
                weekStart.firstDayOfWeek = Calendar.MONDAY
                weekStart[Calendar.HOUR_OF_DAY] = 0
                weekStart[Calendar.MINUTE] = 0
                weekStart[Calendar.SECOND] = 0
                weekStart[Calendar.DAY_OF_WEEK] = Calendar.MONDAY
                return weekStart.timeInMillis
            }

        @Throws(IOException::class)
        fun saveImage(file: File?, bitmap: Bitmap?, quality: Int) {
            if (bitmap == null || file == null) {
                return
            }
            val fos = FileOutputStream(file)
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream)
            val bytes = stream.toByteArray()
            fos.write(bytes)
            fos.close()
        }

        fun log(value: Double, base: Double): Double? {
            val logBase = Math.log(base)
            return if (logBase == 0.0) {
                null
            } else Math.log(value) / Math.log(base)
        }

        fun log10(value: Double): Double {
            return (log(value, 10.0))!!
        }

        /**
         * @param number
         * @param maxLength 数字总长度
         * @param minScale  最小小数位
         * @param maxScale  最大小数位
         * 该方法可能造成精度丢失
         * @return
         */
        @Deprecated("")
        fun formatNumberDynamicScaleNoGroup(
            number: Number?,
            maxLength: Int,
            minScale: Int,
            maxScale: Int,
            down: Boolean
        ): String {
            var minScale = minScale
            var maxScale = maxScale
            val nf9 = NumberFormat.getNumberInstance()
            nf9.isGroupingUsed = false
            if (down) {
                nf9.roundingMode = RoundingMode.DOWN
            }
            if (number == null || number.toDouble() == 0.0) {
                return if (minScale != 0) "0.00" else "0"
            }
            //计算小数位数
            val log10 = log10(number.toDouble())
            val log10Int = log10.toInt() + 1
            if (log10Int + maxScale > maxLength) {
                maxScale = maxLength - log10Int
            }
            if (log10Int + minScale > maxLength) {
                minScale = maxLength - log10Int
            }
            if (maxScale < 0) {
                maxScale = 0
            }
            if (minScale < 0) {
                minScale = 0
            }
            nf9.maximumFractionDigits = maxScale
            nf9.minimumFractionDigits = minScale
            Log.d("", "========>min:$minScale  $maxScale")
            return nf9.format(number)
        }
    }
}