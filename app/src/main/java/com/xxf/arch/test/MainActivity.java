package com.xxf.arch.test;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.xxf.annotation.Router;
import com.xxf.arch.XXF;
import com.xxf.arch.core.activityresult.ActivityResult;
import com.xxf.arch.json.GsonFactory;
import com.xxf.arch.json.JsonUtils;
import com.xxf.arch.test.http.LoginApiService;
import com.xxf.arch.utils.ToastUtils;
import com.xxf.view.actiondialog.BottomPicSelectDialog;

import java.util.List;

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

    class TestModel
    {
        String account;
        Long time;
    }
    class TE{
        List<TestModel> lockVOS;
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
                        try {
                            TE testModel = GsonFactory.createGson().fromJson("{\n" +
                                    "    \"rank\": null,\n" +
                                    "    \"balance\": 0E-8,\n" +
                                    "    \"interest\": null,\n" +
                                    "    \"lockVOS\": [\n" +
                                    "      {\n" +
                                    "        \"account\": \"w*****1@163.com\",\n" +
                                    "        \"amount\": 7121283.43,\n" +
                                    "        \"coinType\": null,\n" +
                                    "        \"time\": null\n" +
                                    "      },\n" +
                                    "      {\n" +
                                    "        \"account\": \"177****45\",\n" +
                                    "        \"amount\": 4381276.47,\n" +
                                    "        \"coinType\": null,\n" +
                                    "        \"time\": 1000\n" +
                                    "      },\n" +
                                    "      {\n" +
                                    "        \"account\": \"z*****l@126.com\",\n" +
                                    "        \"amount\": 3837386.7364,\n" +
                                    "        \"coinType\": null,\n" +
                                    "        \"time\": null\n" +
                                    "      },\n" +
                                    "      {\n" +
                                    "        \"account\": \"g****u@163.com\",\n" +
                                    "        \"amount\": 3574783.241,\n" +
                                    "        \"coinType\": null,\n" +
                                    "        \"time\": null\n" +
                                    "      },\n" +
                                    "      {\n" +
                                    "        \"account\": \"134****43\",\n" +
                                    "        \"amount\": 2394127.534,\n" +
                                    "        \"coinType\": null,\n" +
                                    "        \"time\": 2000\n" +
                                    "      },\n" +
                                    "      {\n" +
                                    "        \"account\": \"4****5@qq.com\",\n" +
                                    "        \"amount\": 1733347.2142,\n" +
                                    "        \"coinType\": null,\n" +
                                    "        \"time\": null\n" +
                                    "      },\n" +
                                    "      {\n" +
                                    "        \"account\": \"158******73\",\n" +
                                    "        \"amount\": 1512989.05003628000000000000,\n" +
                                    "        \"coinType\": null,\n" +
                                    "        \"time\": null\n" +
                                    "      },\n" +
                                    "      {\n" +
                                    "        \"account\": \"133****16\",\n" +
                                    "        \"amount\": 1472523.41,\n" +
                                    "        \"coinType\": null,\n" +
                                    "        \"time\": null\n" +
                                    "      },\n" +
                                    "      {\n" +
                                    "        \"account\": \"u****t@163.com\",\n" +
                                    "        \"amount\": 1235529.34,\n" +
                                    "        \"coinType\": null,\n" +
                                    "        \"time\": null\n" +
                                    "      },\n" +
                                    "      {\n" +
                                    "        \"account\": \"y*****q@gmail.com\",\n" +
                                    "        \"amount\": 1221684.5854,\n" +
                                    "        \"coinType\": null,\n" +
                                    "        \"time\": null\n" +
                                    "      },\n" +
                                    "      {\n" +
                                    "        \"account\": \"108******73\",\n" +
                                    "        \"amount\": 973535.52650000000000000000,\n" +
                                    "        \"coinType\": null,\n" +
                                    "        \"time\": null\n" +
                                    "      },\n" +
                                    "      {\n" +
                                    "        \"account\": \"s****1@naver.com\",\n" +
                                    "        \"amount\": 973367.50840000000000000000,\n" +
                                    "        \"coinType\": null,\n" +
                                    "        \"time\": null\n" +
                                    "      },\n" +
                                    "      {\n" +
                                    "        \"account\": \"158****88\",\n" +
                                    "        \"amount\": 953432.3456,\n" +
                                    "        \"coinType\": null,\n" +
                                    "        \"time\": null\n" +
                                    "      },\n" +
                                    "      {\n" +
                                    "        \"account\": \"186******71\",\n" +
                                    "        \"amount\": 662425.67510000000000000000,\n" +
                                    "        \"coinType\": null,\n" +
                                    "        \"time\": null\n" +
                                    "      },\n" +
                                    "      {\n" +
                                    "        \"account\": \"166******01\",\n" +
                                    "        \"amount\": 611907.48380242403000000000,\n" +
                                    "        \"coinType\": null,\n" +
                                    "        \"time\": null\n" +
                                    "      },\n" +
                                    "      {\n" +
                                    "        \"account\": \"d****1@naver.com\",\n" +
                                    "        \"amount\": 451620.97500000000000000000,\n" +
                                    "        \"coinType\": null,\n" +
                                    "        \"time\": null\n" +
                                    "      },\n" +
                                    "      {\n" +
                                    "        \"account\": \"s****6@gmail.com\",\n" +
                                    "        \"amount\": 441506.22894100000000000000,\n" +
                                    "        \"coinType\": null,\n" +
                                    "        \"time\": null\n" +
                                    "      },\n" +
                                    "      {\n" +
                                    "        \"account\": \"150******09\",\n" +
                                    "        \"amount\": 380309.22681041000000000000,\n" +
                                    "        \"coinType\": null,\n" +
                                    "        \"time\": null\n" +
                                    "      },\n" +
                                    "      {\n" +
                                    "        \"account\": \"153******01\",\n" +
                                    "        \"amount\": 371551.03088928000000000000,\n" +
                                    "        \"coinType\": null,\n" +
                                    "        \"time\": null\n" +
                                    "      },\n" +
                                    "      {\n" +
                                    "        \"account\": \"177******70\",\n" +
                                    "        \"amount\": 352306.51399064000000000000,\n" +
                                    "        \"coinType\": null,\n" +
                                    "        \"time\": null\n" +
                                    "      },\n" +
                                    "      {\n" +
                                    "        \"account\": \"138******80\",\n" +
                                    "        \"amount\": 342466.56003870000000000000,\n" +
                                    "        \"coinType\": null,\n" +
                                    "        \"time\": null\n" +
                                    "      },\n" +
                                    "      {\n" +
                                    "        \"account\": \"180******34\",\n" +
                                    "        \"amount\": 323751.03134120000000000000,\n" +
                                    "        \"coinType\": null,\n" +
                                    "        \"time\": null\n" +
                                    "      },\n" +
                                    "      {\n" +
                                    "        \"account\": \"131******54\",\n" +
                                    "        \"amount\": 316598.18557532000000000000,\n" +
                                    "        \"coinType\": null,\n" +
                                    "        \"time\": null\n" +
                                    "      },\n" +
                                    "      {\n" +
                                    "        \"account\": \"g****1@163.com\",\n" +
                                    "        \"amount\": 311511.59331026000000000000,\n" +
                                    "        \"coinType\": null,\n" +
                                    "        \"time\": null\n" +
                                    "      },\n" +
                                    "      {\n" +
                                    "        \"account\": \"a****1@gmail.com\",\n" +
                                    "        \"amount\": 308728.68106233000000000000,\n" +
                                    "        \"coinType\": null,\n" +
                                    "        \"time\": null\n" +
                                    "      },\n" +
                                    "      {\n" +
                                    "        \"account\": \"185******67\",\n" +
                                    "        \"amount\": 303251.38580698000000000000,\n" +
                                    "        \"coinType\": null,\n" +
                                    "        \"time\": null\n" +
                                    "      },\n" +
                                    "      {\n" +
                                    "        \"account\": \"181******32\",\n" +
                                    "        \"amount\": 291914.02753400000000000000,\n" +
                                    "        \"coinType\": null,\n" +
                                    "        \"time\": null\n" +
                                    "      },\n" +
                                    "      {\n" +
                                    "        \"account\": \"186******88\",\n" +
                                    "        \"amount\": 258056.19374332000000000000,\n" +
                                    "        \"coinType\": null,\n" +
                                    "        \"time\": null\n" +
                                    "      },\n" +
                                    "      {\n" +
                                    "        \"account\": \"155******43\",\n" +
                                    "        \"amount\": 256633.93006900000000000000,\n" +
                                    "        \"coinType\": null,\n" +
                                    "        \"time\": null\n" +
                                    "      },\n" +
                                    "      {\n" +
                                    "        \"account\": \"139******88\",\n" +
                                    "        \"amount\": 245985.19003420000000000000,\n" +
                                    "        \"coinType\": null,\n" +
                                    "        \"time\": null\n" +
                                    "      }\n" +
                                    "    ]\n" +
                                    "  }", TE.class);

                            Log.d("=================>","yes:::"+testModel.lockVOS.size());
                            List<TestModel> lockVOS = testModel.lockVOS;
                            int i=0;
                            for(TestModel model:lockVOS)
                            {
                                Log.d("=================>","item:"+(i++)+"   " +model.account+" "+model.time+"  ");
                            }

                        }catch (Throwable e)
                        {
                            e.printStackTrace();
                            Log.d("==========>error:","",e);
                        }


//                        User<String> user = new User<>("xxx");
//                        String s = JsonUtils.toJsonString(user);
//                        User<String> userDes = JsonUtils.toBean(s, User.class, String.class);
//                        Log.d("=========>d:", "" + userDes);
//
//                        Intent intent = new Intent(MainActivity.this, TestActivity.class);
//                        startActivityForResult(intent, 1001);
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

                        new Handler()
                                .postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MainActivity.this,"xxxx2019",Toast.LENGTH_LONG).show();
                                    }
                                },1000);
                        finish();
//                        new BottomPicSelectDialog(MainActivity.this, new Consumer<String>() {
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
    protected void onDestroy() {
        super.onDestroy();

//        findViewById(R.id.bt_startActivityForResult).postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//
//                    }
//                },1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("======>onActResult:", "" + this + "_" + data.getStringExtra("data"));
    }
}
