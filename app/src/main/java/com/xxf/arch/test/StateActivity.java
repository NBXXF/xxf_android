package com.xxf.arch.test;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.xxf.arch.XXF;
import com.xxf.arch.activity.XXFActivity;
import com.xxf.arch.test.databinding.ActivityStateBinding;
import com.xxf.arch.test.databinding.ItemTestBinding;
import com.xxf.arch.utils.ToastUtils;
import com.xxf.arch.viewmodel.XXFViewModel;
import com.xxf.view.recyclerview.RecyclerViewUtils;
import com.xxf.view.recyclerview.adapter.XXFRecyclerAdapter;
import com.xxf.view.recyclerview.adapter.XXFViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class StateActivity extends XXFActivity {

    ActivityStateBinding stateBinding;
    TestAdaper testAdaper;

    public static class TestViewModel extends XXFViewModel {

        public TestViewModel(@NonNull Application application) {
            super(application);
            Observable.interval(1, TimeUnit.SECONDS)
                    .compose(XXF.bindToLifecycle(this))
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            XXF.getLogger().d("=======>xxx:long:" + aLong);

                        }
                    });
        }

        @Override
        protected void onCleared() {
            super.onCleared();
            XXF.getLogger().d("=======>xxx:onclear");
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stateBinding = ActivityStateBinding.inflate(getLayoutInflater(), null, false);
        setContentView(stateBinding.getRoot());
        // TestViewModel viewModel = XXF.getViewModel(this, TestViewModel.class);

        stateBinding.recyclerView.setAdapter(testAdaper = new TestAdaper());
        stateBinding.btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //    Bitmap bitmap = getBitmap(stateBinding.recyclerView);
                Bitmap bitmap = RecyclerViewUtils.shotRecyclerViewVisibleItems(stateBinding.recyclerView);

                XXF.getLogger().d("=============>bitmap:" + bitmap);
                XXF.getLogger().d("=============>H:" + stateBinding.recyclerView.getHeight() + "  " + stateBinding.recyclerView.getMeasuredHeight());
                stateBinding.preview.setImageBitmap(bitmap);

                // loadData();
            }
        });
        testAdaper.bindData(true, new ArrayList<>());
        loadData();
    }

    private static Bitmap getBitmap(RecyclerView view) {

        // 把一个View转换成图片
        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        //测量在屏幕上宽和高
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        //确定View的大小和位置的,然后将其绘制出来
        //  view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        if (bitmap != null) {
            return bitmap;
        }
        return convertViewToBitmap(view);
    }

    /**
     * 主要方法：创建一个bitmap放于画布之上进行绘制 （简直如有神助）
     */
    private static Bitmap convertViewToBitmap(View view) {
       /* Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),
                view.getHeight(), Bitmap.Config.ARGB_8888);*/
    /*    view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));*/
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        XXF.getLogger().d("============>rl:pause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Toast.makeText(this,"abcd",Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showToast("test state xxx", ToastUtils.ToastType.ERROR);
            }
        }, 2000);
        XXF.getLogger().d("=============isBack state stop:" + XXF.getActivityStackProvider().isBackground());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        XXF.getLogger().d("============>rl:onDestroy");
    }

    private void loadData() {
        Observable.fromCallable(new Callable<List<String>>() {
            @Override
            public List<String> call() throws Exception {
                List<String> strings = new ArrayList<>();
                int i1 = new Random().nextInt(200);
                for (int i = 0; i < i1; i++) {
                    strings.add("i:" + i);
                }
                return strings;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .to(XXF.bindLifecycle(this))
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(List<String> strings) throws Exception {
                        Log.d("=========", "" + strings);
                        testAdaper.bindData(true, new ArrayList<>());
                    }
                });
    }

    class TestAdaper extends XXFRecyclerAdapter<ItemTestBinding, String> {

        @Override
        protected ItemTestBinding onCreateBinding(LayoutInflater inflater, ViewGroup viewGroup, int viewType) {
            return ItemTestBinding.inflate(inflater, viewGroup, false);
        }

        @Override
        public void onBindHolder(XXFViewHolder<ItemTestBinding, String> holder, @Nullable String s, int index) {
            holder.getBinding().textView.setText(s);
            XXF.getLogger().d("===============>init:" + index);
        }
    }
}
