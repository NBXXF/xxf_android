package com.xxf.arch.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.xxf.annotation.Router;
import com.xxf.arch.XXF;
import com.xxf.arch.rxjava.transformer.ProgressHUDTransformerImpl;
import com.xxf.arch.activity.XXFActivity;
import com.xxf.arch.annotation.BindVM;
import com.xxf.arch.annotation.BindView;
import com.xxf.arch.test.databinding.ActivityTestBinding;
import com.xxf.arch.viewmodel.XXFViewModel;
import com.xxf.arch.widget.progresshud.ProgressHUD;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

/**
 * @author youxuan  E-mail:youxuan@icourt.cc
 * @version 2.3.1
 * @Description
 * @Company Beijing icourt
 * @date createTime：2018/9/9
 */
@Router(path = "/user/test")
@BindView(R.layout.activity_test)
@BindVM(XXFViewModel.class)
public class TestActivity extends XXFActivity {
    BaseFragmentAdapter baseFragmentAdapter;
    ActivityTestBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getBinding();
        baseFragmentAdapter = new BaseFragmentAdapter(getSupportFragmentManager());
        binding.pager.setAdapter(baseFragmentAdapter);
        baseFragmentAdapter.bindData(true, Arrays.asList(new TestFragment(), new TestFragment(), new TestFragment()));


        findViewById(R.id.bt_setResult)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                Toast.makeText(TestActivity.this,"xxxx2019999",Toast.LENGTH_LONG).show();
                            }
                        }).start();
//                        Intent intent = getIntent();
//                        intent.putExtra("data", "hello 2019");
//                        setResult(Activity.RESULT_OK, intent);
//                        Log.d("======>finish:", "" + this);
//                        //finish();
//
//                        Log.d("======>isFinishing:", "" + isFinishing());
//                        Log.d("======>isDestroyed:", "" + isDestroyed());
                    }
                });
//        Observable.interval(1, TimeUnit.MILLISECONDS)
//                .compose(XXF.<Long>bindUntilEvent(this, Lifecycle.Event.ON_PAUSE))
//                .subscribe(new Consumer<Long>() {
//                    @Override
//                    public void accept(Long aLong) throws Exception {
//                        Log.d("========>data:", "" + aLong);
//                    }
//                });


    }

    private void test() {
        Observable.timer(10, TimeUnit.SECONDS)
                .compose(XXF.bindToProgressHud(new ProgressHUDTransformerImpl.Builder(this)))
                .subscribe();

    }

    @Override
    public ProgressHUD progressHUD() {
        return super.progressHUD();
    }

}
