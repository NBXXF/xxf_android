package com.xxf.arch.test;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.xxf.annotation.Router;
import com.xxf.arch.XXF;
import com.xxf.arch.core.activityresult.ActivityResult;
import com.xxf.arch.json.JsonUtils;
import com.xxf.arch.json.ListTypeToken;
import com.xxf.arch.json.MapTypeToken;
import com.xxf.arch.utils.ToastUtils;
import com.xxf.view.actiondialog.BottomPicSelectDialog;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;


@Router(path = "/user/main")
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
                        List<String> strs = Arrays.asList("1", "2");
                        String s = JsonUtils.toJsonString(strs);
                        List<String> strings = JsonUtils.toBeanList(s, new ListTypeToken<String>());
                        Log.d("==========>res:", "" + Arrays.toString(strings.toArray()));


                        Map<String, String> map = new HashMap<>();
                        map.put("1", "bbb");
                        map.put("2", "aaa");
                        String mapStr = JsonUtils.toJsonString(map);
                        Map<String, String> mapres = JsonUtils.toMap(mapStr, new MapTypeToken<String, String>());
                        Log.d("==========>res2:", "" + mapres);
                    /*    new BottomPicSelectDialog(MainActivity.this, new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                ToastUtils.showToast("url:" + s);
                            }
                        }).show();*/
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

}
