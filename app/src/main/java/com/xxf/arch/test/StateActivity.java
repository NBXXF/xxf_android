package com.xxf.arch.test;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ContentInfoCompat;
import androidx.core.view.OnReceiveContentListener;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewbinding.ViewBinding;

import com.ezydev.bigscreenshot.BigScreenshot;
import com.xxf.arch.XXF;
import com.xxf.arch.model.DownloadTask;
import com.xxf.arch.rxjava.transformer.ProgressHUDTransformerImpl;
import com.xxf.arch.test.databinding.ActivityStateBinding;
import com.xxf.arch.test.databinding.ItemTest2Binding;
import com.xxf.arch.test.databinding.ItemTestBinding;
import com.xxf.arch.utils.ToastUtils;
import com.xxf.fileprovider.FileProvider7;
import com.xxf.utils.BitmapUtils;
import com.xxf.utils.DensityUtil;
import com.xxf.view.recyclerview.adapter.XXFRecyclerAdapter;
import com.xxf.view.recyclerview.adapter.XXFViewHolder;
import com.xxf.view.recyclerview.itemdecorations.DividerDecoration;
import com.xxf.view.utils.SystemUtils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class StateActivity extends AppCompatActivity implements BigScreenshot.ProcessScreenshot {

    ActivityStateBinding stateBinding;
    TestAdaper testAdaper;
    boolean checked;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<Observable<Boolean>> datas = new ArrayList<>();
        datas.add(Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                throw new RuntimeException("xxx");
            }
        }));
        datas.add(Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return true;
            }
        }));
        Observable.concatDelayError(datas)
                .observeOn(AndroidSchedulers.mainThread(), true)
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d("=======>data error:", "" + throwable);
                    }
                })
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Throwable {
                        Log.d("=======>data:", "" + aBoolean);
                    }
                });
        stateBinding = ActivityStateBinding.inflate(getLayoutInflater(), null, false);

        //stateBinding.grayLayout.setGrayColor(true);

        setContentView(stateBinding.getRoot());
        // TestViewModel viewModel = XXF.getViewModel(this, TestViewModel.class);

        stateBinding.recyclerView.setAdapter(testAdaper = new TestAdaper());
        //  new ItemTouchHelper(new SimpleItemTouchHelperCallback(testAdaper)).attachToRecyclerView(stateBinding.recyclerView);
        // stateBinding.recyclerView.addItemDecoration(new GridItemDecoration(DensityUtil.dip2px(5)));
        stateBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //stateBinding.recyclerView.addItemDecoration(ItemDecorationFactory.createHorizontalItemDecoration(new HorizontalDividerItemDecoration.Builder(this).size(DensityUtil.dip2px(20)).color(Color.RED)));
        DividerDecoration dividerItemDecoration = new DividerDecoration(this, Color.YELLOW, DensityUtil.dip2px(20));
        stateBinding.recyclerView.addItemDecoration(dividerItemDecoration);
        stateBinding.btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(),ReciveContentActivity.class));
                //  ShareUtil.shareQQ(StateActivity.this, "xxxx");
//                SystemUtils.shareText(
//                        StateActivity.this,
//                        "xxxx",
//                        //null
//                        //SystemUtils.SHARE_WEIBO_CIRCLE_COMPONENT
//                        SystemUtils.SHARE_QQ_FRIEND_COMPONENT
//                ).compose(XXF.bindToErrorNotice())
//                        .subscribe();
            }
        });
        stateBinding.btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                                Log.d("", "=========>task2:" + downloadTask);
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
        Observable
                .fromCallable(new Callable<List<Integer>>() {
                    @Override
                    public List<Integer> call() throws Exception {
                        List<Integer> strings = new ArrayList<>();
                        //   int i1 = new Random().nextInt(205);
                        int i1 = 20;
                        for (int i = 0; i < i1; i++) {
                            strings.add(i);
                        }
                        return strings;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .to(XXF.bindLifecycle(this))
                .subscribe(new Consumer<List<Integer>>() {
                    @Override
                    public void accept(List<Integer> strings) throws Exception {
                        testAdaper.bindData(true, strings);
                    }
                });
    }

    @Override
    public void getScreenshot(Bitmap bitmap) {

        ToastUtils.showToast("xxxx");
        Log.d("=========>", "====>bitmap:" + bitmap);
        SystemUtils.saveImageToAlbum(this, "" + System.currentTimeMillis() + ".png", bitmap)
                .subscribe(new Consumer<File>() {
                    @Override
                    public void accept(File file) throws Throwable {
                        Log.d("=========>", "====>bitmap2:" + file);
                    }
                });
    }

    class TestAdaper extends XXFRecyclerAdapter<ViewBinding, Integer> {


        @Override
        public int getItemViewType(int position) {
            if (getItem(position).intValue() % 2 == 0) {
                return 1;
            }
            return super.getItemViewType(position);
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
