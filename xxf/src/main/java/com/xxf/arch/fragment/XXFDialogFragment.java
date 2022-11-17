package com.xxf.arch.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.xxf.application.lifecycle.ViewLifecycleOwner;
import com.xxf.arch.component.ObservableComponent;
import com.xxf.arch.component.WindowComponent;
import com.xxf.arch.dialog.TouchListenDialog;
import com.xxf.utils.RAUtils;
import com.xxf.view.round.CornerUtil;

import org.jetbrains.annotations.NotNull;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;

/**
 * @version 2.3.1
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description
 * @date createTime：2018/9/7
 */
public class XXFDialogFragment<E> extends AppCompatDialogFragment implements ObservableComponent<DialogFragment, E>, WindowComponent {
    private final String TAG_PREFIX = "show_rau_";
    @LayoutRes
    private int mContentLayoutId;
    private final Subject<Object> componentSubject = PublishSubject.create().toSerialized();

    public XXFDialogFragment() {

    }

    public XXFDialogFragment(@LayoutRes int contentLayoutId) {
        this.mContentLayoutId = contentLayoutId;
        // super(contentLayoutId);
    }

    @Override
    public Observable<Pair<DialogFragment, E>> getComponentObservable() {
        return componentSubject.ofType(Object.class)
                .map(new Function<Object, Pair<DialogFragment, E>>() {
                    @Override
                    public Pair<DialogFragment, E> apply(Object o) throws Throwable {
                        return Pair.create(XXFDialogFragment.this, (E) o);
                    }
                });
    }

    @Override
    public void setComponentResult(E result) {
        if (result != null) {
            componentSubject.onNext(result);
        }
    }

    @CallSuper
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new TouchListenDialog(getContext(), getTheme()) {
            @Override
            protected void onDialogTouchOutside(MotionEvent event) {
                XXFDialogFragment.this.onDialogTouchOutside(event);
            }
        };
    }

    /**
     * 外部点击
     */
    protected void onDialogTouchOutside(MotionEvent event) {
    }

    /**
     * 会重复调用 禁止复写
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (this.mContentLayoutId != 0) {
            return inflater.inflate(this.mContentLayoutId, container, false);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * 会重复调用
     *
     * @param view
     * @param savedInstanceState
     */
    @CallSuper
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewLifecycleOwner.set(view,this);
        /**
         * 检查是否实现了WindowComponent协议
         */
        if (getShowsDialog() && !(getDialog() instanceof WindowComponent)) {
            throw new RuntimeException("dialog must extends from WindowComponent");
        }
    }

    /**
     * 需要调用父类的方法,否则影响XXF.startActivityForResult
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @CallSuper
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 需要调用父类的方法,否则影响XXF.requestPermission
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @CallSuper
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 会重复调用 禁止复写
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @CallSuper
    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public int show(@NonNull FragmentTransaction transaction, @Nullable String tag) {
        if(this.isAdded()){
            return -1;
        }
        if (RAUtils.INSTANCE.isLegal(TAG_PREFIX + this.getClass().getName(), RAUtils.DURATION_DEFAULT)) {
            return super.show(transaction, tag);
        }
        return -1;
    }

    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        try {
            manager.executePendingTransactions();
        }catch (Throwable throwable){
        }
        if(this.isAdded()){
            return;
        }
        if (RAUtils.INSTANCE.isLegal(TAG_PREFIX + this.getClass().getName(), RAUtils.DURATION_DEFAULT)) {
            super.show(manager, tag);
        }
    }

    @Override
    public void showNow(@NonNull FragmentManager manager, @Nullable String tag) {
        try {
            manager.executePendingTransactions();
        }catch (Throwable throwable){
        }
        if(this.isAdded()){
            return;
        }
        if (RAUtils.INSTANCE.isLegal(TAG_PREFIX + this.getClass().getName(), RAUtils.DURATION_DEFAULT)) {
            super.showNow(manager, tag);
        }
    }

    @Override
    public void setWindowSize(int width, int height) {
        if(getDialog() instanceof WindowComponent){
            ((WindowComponent)getDialog()).setWindowSize(width,height);
        }
    }

    @Override
    public void setWindowWidth(int width) {
        if(getDialog() instanceof WindowComponent){
            ((WindowComponent)getDialog()).setWindowWidth(width);
        }
    }

    @Override
    public void setWindowHeight(int height) {
        if(getDialog() instanceof WindowComponent){
            ((WindowComponent)getDialog()).setWindowHeight(height);
        }
    }

    @Nullable
    @Override
    public Window getWindow() {
        if(getDialog() instanceof WindowComponent){
            return ((WindowComponent)getDialog()).getWindow();
        }
        return null;
    }

    @Nullable
    @Override
    public FrameLayout getDecorView() {
        if(getDialog() instanceof WindowComponent){
            return  ((WindowComponent)getDialog()).getDecorView();
        }
        return null;
    }

    @Nullable
    @Override
    public FrameLayout getContentParent() {
        if(getDialog() instanceof WindowComponent){
            return  ((WindowComponent)getDialog()).getContentParent();
        }
        return null;
    }

    @Override
    public void setWindowDimAmount(float amount) {
        if(getDialog() instanceof WindowComponent){
            ((WindowComponent)getDialog()).setWindowDimAmount(amount);
        }
    }

    @Override
    public void setWindowGravity(int gravity) {
        if(getDialog() instanceof WindowComponent){
            ((WindowComponent)getDialog()).setWindowGravity(gravity);
        }
    }

    @Override
    public void setWindowBackground(@NotNull Drawable drawable) {
        if(getDialog() instanceof WindowComponent){
            ((WindowComponent)getDialog()).setWindowBackground(drawable);
        }
    }

    @Override
    public void setWindowBackground(int color) {
        if(getDialog() instanceof WindowComponent){
            ((WindowComponent)getDialog()).setWindowBackground(color);
        }
    }

    @Override
    public void setWindowBackgroundDimEnabled(boolean enabled) {
        if(getDialog() instanceof WindowComponent){
            ((WindowComponent)getDialog()).setWindowBackgroundDimEnabled(enabled);
        }
    }

    @Override
    public void setCanceledOnTouchOutside(boolean cancel) {
        if(getDialog() instanceof WindowComponent){
            ((WindowComponent)getDialog()).setCanceledOnTouchOutside(cancel);
        }
    }

    @Override
    public void setWindowRadius(float radius) {
        if(getDialog() instanceof WindowComponent){
            ((WindowComponent)getDialog()).setWindowRadius(radius);
        }
    }
}
