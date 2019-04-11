package com.xxf.arch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.xxf.arch.http.LoginApiService;
import com.xxf.arch.http.XXFHttp;

import org.reactivestreams.Subscriber;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Intent intent=new Intent(this,TestActivity.class);
//        intent.putExtra("name","xxx");
//        intent.putExtra("age","12");
//        intent.putExtra("desc","124");
//        startActivity(intent);

        XXFHttp.registerApiService(LoginApiService.class, "http://api.map.baidu.com/telematics/", null);

        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.d("========>error:", Log.getStackTraceString(throwable));
            }
        });

        XXFHttp.getApiService(LoginApiService.class)
                .getCity()
                .subscribe(new Consumer<JsonObject>() {
                    @Override
                    public void accept(JsonObject jsonObject) throws Exception {
                        Log.d("=======>res:", jsonObject.toString());
                    }
                });


        final String TAG = "=======>";
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                emitter.onNext(null);
            }
        }).subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.i(TAG, "onCompleted");
            }

            @Override
            public void onNext(Object o) {
                Log.i(TAG, "onNext: " + o);
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError: " + e);
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
