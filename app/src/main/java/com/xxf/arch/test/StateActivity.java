package com.xxf.arch.test;

import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.viewbinding.ViewBinding;

import com.xxf.arch.XXF;
import com.xxf.arch.activity.XXFActivity;
import com.xxf.arch.model.DownloadTask;
import com.xxf.arch.rxjava.transformer.ProgressHUDTransformerImpl;
import com.xxf.arch.test.databinding.ActivityStateBinding;
import com.xxf.arch.test.databinding.ItemTest2Binding;
import com.xxf.arch.test.databinding.ItemTestBinding;
import com.xxf.arch.utils.DensityUtil;
import com.xxf.arch.viewmodel.XXFViewModel;
import com.xxf.view.recyclerview.RecyclerViewUtils;
import com.xxf.view.recyclerview.adapter.OnItemClickListener;
import com.xxf.view.recyclerview.adapter.XXFRecyclerAdapter;
import com.xxf.view.recyclerview.adapter.XXFViewHolder;
import com.xxf.view.recyclerview.itemdecorations.GridItemDecoration;
import com.xxf.view.recyclerview.touchhelper.SimpleItemTouchHelperCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
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
        new ItemTouchHelper(new SimpleItemTouchHelperCallback(testAdaper)).attachToRecyclerView(stateBinding.recyclerView);
        stateBinding.recyclerView.addItemDecoration(new GridItemDecoration(DensityUtil.dip2px(5)));
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
        stateBinding.btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  SystemUtils.shareText(StateActivity.this, "XXX http://www.baidu.com",
                        "com.instagram.android")
                        .subscribe();*/


                loadData();
                sharedToIns();
            }
        });
        testAdaper.bindData(true, new ArrayList<>());
        loadData();


        stateBinding.btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  XXF.getFileService()
                        .getPrivateFileDir()
                        .flatMap(new Function<File, ObservableSource<File>>() {
                            @Override
                            public ObservableSource<File> apply(File file) throws Throwable {
                                return XXF.getFileService()
                                        .download("http://vfx.mtime.cn/Video/2019/02/04/mp4/190204084208765161.mp4", new File(file, "test.mp4").getAbsolutePath());
                            }
                        })
                        .compose(XXF.bindToProgressHud())
                        .subscribe();*/

                XXF.getFileService()
                        .getFilesDir(false, false)
                        .flatMap(new Function<File, Observable<DownloadTask>>() {
                            @Override
                            public Observable<DownloadTask> apply(File file) throws Throwable {
                                return XXF.getFileService()
                                        .downloadTask("http://vfx.mtime.cn/Video/2019/02/04/mp4/190204084208765161.mp4", new File(file, "test.mp4").getAbsolutePath());
                            }
                        })
                        .compose(new ProgressHUDTransformerImpl<DownloadTask>(StateActivity.this) {
                            @Override
                            public void onNext(DownloadTask downloadTask) {
                                super.onNext(downloadTask);
                                getSafeProgressHUD().updateStateText((downloadTask.getCurrent() * 1.0f / downloadTask.getDuration()) * 100 + "%");
                            }
                        }.setDismissOnNext(false))
                        .to(XXF.bindLifecycle(StateActivity.this))
                        .subscribe(new Consumer<DownloadTask>() {
                            @Override
                            public void accept(DownloadTask downloadTask) throws Throwable {
                                XXF.getLogger().d("=========>task2:" + downloadTask);
                            }
                        });

            }
        });
    }


    /**
     * 分享到Ins(本地图片)
     */
    private void sharedToIns() {
        String type = "*/*";
        Uri uri =
                Uri.parse("android.resource://" + getPackageName() + "/" + R.mipmap.ic_launcher1);
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType(type);
        share.putExtra(Intent.EXTRA_STREAM, uri);
//        startActivity(Intent.createChooser(share, "Share to"))
        share.putExtra(Intent.EXTRA_TITLE, "share oneHope");
        share.setPackage("com.instagram.android");
        startActivity(share);
    }

    private void loadData() {
        Observable.fromCallable(new Callable<List<Integer>>() {
            @Override
            public List<Integer> call() throws Exception {
                List<Integer> strings = new ArrayList<>();
                int i1 = new Random().nextInt(200);
                for (int i = 0; i < i1; i++) {
                    strings.add(i);
                }
                return strings;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .to(XXF.bindLifecycle(this))
                .subscribe(new Consumer<List<Integer>>() {
                    @Override
                    public void accept(List<Integer> strings) throws Exception {
                        testAdaper.bindData(true, strings);
                    }
                });
    }

    class TestAdaper extends XXFRecyclerAdapter<ViewBinding, Integer> {


        @Override
        public int getViewType(int index) {
            if (getItem(index).intValue() % 2 == 0) {
                return 1;
            }
            return super.getViewType(index);
        }

        @Override
        protected ViewBinding onCreateBinding(LayoutInflater inflater, ViewGroup viewGroup, int viewType) {
            if (viewType == 1) {
                return ItemTest2Binding.inflate(inflater, viewGroup, false);
            }
            return ItemTestBinding.inflate(inflater, viewGroup, false);
        }

        @Override
        public void onBindHolder(XXFViewHolder<ViewBinding, Integer> holder, @Nullable Integer item, int index) {
            if (holder.getBinding() instanceof ItemTestBinding) {
                ((ItemTestBinding) holder.getBinding()).textView.setText("s:" + item);
            } else if (holder.getBinding() instanceof ItemTest2Binding) {
                ((ItemTest2Binding) holder.getBinding()).textView.setText("vs:" + item);
            }
        }
    }
}
