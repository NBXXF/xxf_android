
package com.xxf.media.album.internal.ui;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.xxf.media.album.R;
import com.xxf.media.album.internal.entity.Item;
import com.xxf.media.album.internal.entity.SelectionSpec;
import com.xxf.media.album.internal.utils.PhotoMetadataUtils;
import com.xxf.media.album.listener.OnFragmentInteractionListener;
import com.xxf.rxjava.RxLifecycle;
import com.xxf.utils.RomUtils;

import java.util.concurrent.Callable;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase;

public class PreviewItemFragment extends Fragment {

    private static final String ARGS_ITEM = "args_item";
    private OnFragmentInteractionListener mListener;

    public static PreviewItemFragment newInstance(Item item) {
        PreviewItemFragment fragment = new PreviewItemFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARGS_ITEM, item);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_preview_item, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Item item = getArguments().getParcelable(ARGS_ITEM);
        if (item == null) {
            return;
        }

        View videoPlayButton = view.findViewById(R.id.video_play_button);
        if (item.isVideo()) {
            videoPlayButton.setVisibility(View.VISIBLE);
            videoPlayButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(item.uri, "video/*");
                    try {
                        startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(getContext(), R.string.error_no_video_activity, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            videoPlayButton.setVisibility(View.GONE);
        }

        ImageViewTouch image = (ImageViewTouch) view.findViewById(R.id.image_view);
        image.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);

        image.setSingleTapListener(new ImageViewTouch.OnImageViewTouchSingleTapListener() {
            @Override
            public void onSingleTapConfirmed() {
                if (mListener != null) {
                    mListener.onClick();
                }
            }
        });

        //华为 鸿蒙会闪退 https://github.com/zhihu/Matisse/issues/726
        //目前尝试异步获取大小
        if (RomUtils.isHuawei()) {
            Observable
                    .fromCallable(new Callable<Point>() {
                        @Override
                        public Point call() throws Exception {
                            Point size = PhotoMetadataUtils.getBitmapSize(item.getContentUri(), getActivity());
                            return size;
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .to(RxLifecycle.INSTANCE.bindLifecycle(this, Lifecycle.Event.ON_DESTROY))
                    .subscribe(new Consumer<Point>() {
                        @Override
                        public void accept(Point size) throws Throwable {
                            loadImageWithItem(item, size, image);
                        }
                    });
        } else {
            Point size = PhotoMetadataUtils.getBitmapSize(item.getContentUri(), getActivity());
            loadImageWithItem(item, size, image);
        }
    }

    void loadImageWithItem(Item item, Point size, ImageViewTouch image) {
        try {
            if (item.isGif()) {
                SelectionSpec.getInstance().imageEngine.loadGifImage(getContext(), size.x, size.y, image,
                        item.getContentUri());
            } else {
                SelectionSpec.getInstance().imageEngine.loadImage(getContext(), size.x, size.y, image,
                        item.getContentUri());
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void resetView() {
        if (getView() != null) {
            ((ImageViewTouch) getView().findViewById(R.id.image_view)).resetMatrix();
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
