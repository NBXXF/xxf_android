package com.xxf.view.loading;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.LayoutTransition;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.xxf.view.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description loading..
 */
public class XXFLoading {

    private static final String TAG = "AlphaLoading";

    public static final int STATE_FREE = 0;
    public static final int STATE_LOADING = 1;
    public static final int STATE_RESULTING = 2;
    public static final int STATE_DISMISSING = 3;

    private static int sDefaultLoadingDrawable = R.drawable.alpha_loading;
    private static int sDefaultOkIcon = R.drawable.alpha_ic_ok;
    private static int sDefaultFailIcon = R.drawable.alpha_ic_fail;
    private static boolean sDefaultCancelable = false;
    private static long sDefaultResultDuration = 1000;

    private final Dialog mDialog;

    public Dialog getmDialog() {
        return mDialog;
    }

    private final ImageView mIconView;
    private final TextView mMsgView;
    private final DialogInterface.OnDismissListener mDismissListener;
    private Handler mHandler;
    @State
    private int mState;
    @DrawableRes
    private final int mLoadingDrawable;
    @DrawableRes
    private final int mOkDrawableRes;
    @DrawableRes
    private final int mFailDrawableRes;
    private final long mResultDuration;
    private boolean mReshowingWhileDismissing;

    public static void setDefaultLoadingDrawable(@DrawableRes int defaultLoadingDrawable) {
        sDefaultLoadingDrawable = defaultLoadingDrawable;
    }

    public static void setDefaultOkIcon(@DrawableRes int defaultOkIcon) {
        sDefaultOkIcon = defaultOkIcon;
    }

    public static void setDefaultFailIcon(@DrawableRes int defaultFailIcon) {
        sDefaultFailIcon = defaultFailIcon;
    }

    public static void setDefaultCancelable(boolean defaultCancelable) {
        sDefaultCancelable = defaultCancelable;
    }

    public static void setDefaultResultDuration(long defaultResultDuration) {
        if (defaultResultDuration >= 0) {
            sDefaultResultDuration = defaultResultDuration;
        } else {
            sDefaultResultDuration = 1000;
        }
    }

    private XXFLoading(Builder b) {
        mState = STATE_FREE;

        mOkDrawableRes = b.okIcon;
        mFailDrawableRes = b.failIcon;

        Dialog dialog = new Dialog(b.context, R.style.xxf_loading_dialog_style);
        dialog.setContentView(R.layout.xxf_dialog_loading);

        ImageView iconView = (ImageView) dialog.findViewById(R.id.alpha_iv_icon);
        TextView msgView = (TextView) dialog.findViewById(R.id.alpha_tv_message);
        this.mIconView = iconView;
        this.mMsgView = msgView;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            LayoutTransition lt = new LayoutTransition();
            lt.enableTransitionType(LayoutTransition.CHANGING);
            ((ViewGroup) dialog.findViewById(R.id.alpha_content_view)).setLayoutTransition(lt);
        }

        setMessage(b.message);

        mLoadingDrawable = b.loadingDrawable;
        try {
            iconView.setImageResource(mLoadingDrawable);
        } catch (OutOfMemoryError ignored) {
        }

        dialog.setCancelable(b.cancelable);
        dialog.setCanceledOnTouchOutside(b.cancelable);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                release();
            }
        });
        dialog.setOnDismissListener(mDismissListener = new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                release();
                if (mReshowingWhileDismissing) {
                    mReshowingWhileDismissing = false;
                    show();
                }
            }
        });

        Window window = dialog.getWindow();
        if (window != null) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.dimAmount = 0.3f;
            window.setAttributes(lp);
        }

        this.mResultDuration = b.resultDuration;
        this.mDialog = dialog;
    }

    /**
     * 更新消息
     *
     * @param message 消息
     */
    public void setMessage(String message) {
        if (TextUtils.isEmpty(message)) {
            mMsgView.setVisibility(View.GONE);
            mMsgView.setText(null);
        } else {
            mMsgView.setVisibility(View.VISIBLE);
            mMsgView.setText(message);
        }
    }

    /**
     * 显示loading
     */
    public void show() {
        if (mState == STATE_FREE) {
            mState = STATE_LOADING;

            mDialog.show();
            try {
                mIconView.setImageResource(mLoadingDrawable);
            } catch (OutOfMemoryError ignored) {
            }
            startLoadingAnimation();

            if (mHandler == null) {
                mHandler = new Handler(Looper.getMainLooper());
            }
        } else if (mState == STATE_DISMISSING && !mReshowingWhileDismissing) {
            mReshowingWhileDismissing = true;
        }
    }

    /**
     * 立马结束loading
     */
    public void dismissImmediately() {
        if (mState == STATE_LOADING) {
            mState = STATE_DISMISSING;
            try {
                mDialog.dismiss();
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * 以正确状态结束，结束的时间为预先配置的值
     *
     * @param okMsg 结束消息
     */
    public void dismissOk(String okMsg) {
        dismissWithResult(okMsg, mOkDrawableRes);
    }

    public void dismissFail(String failMsg) {
        dismissWithResult(failMsg, mFailDrawableRes);
    }

    public void dismissOk(String okMsg, Runnable endAction) {
        dismissWithResult(okMsg, mOkDrawableRes, endAction);
    }

    public void dismissFail(String failMsg, Runnable endAction) {
        dismissWithResult(failMsg, mFailDrawableRes, endAction);
    }

    public void dismissWithResult(String msg, @DrawableRes final int resultIconRes) {
        dismissWithResult(msg, resultIconRes, null);
    }

    public void dismissWithResult(String msg, @DrawableRes final int resultIconRes, final Runnable endAction) {
        if (mState == STATE_LOADING) {
            mState = STATE_RESULTING;
            setMessage(msg);
            
            mIconView.animate().alpha(0).setDuration(200).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    stopLoadingAnimation();
                    try {
                        mIconView.setImageResource(resultIconRes);
                    } catch (OutOfMemoryError ignored) {
                    }
                    mIconView.animate().alpha(1).setListener(null).start();
                }
            }).start();
            if (mHandler != null) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (endAction == null) {
                            try {
                                mState = STATE_DISMISSING;
                                mDialog.dismiss();
                            } catch (Throwable ignored) {
                            }
                        } else {
                            try {
                                mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {
                                        try {
                                            mDialog.setOnDismissListener(mDismissListener);
                                        } finally {
                                            release();
                                        }
                                        if (endAction != null) {
                                            endAction.run();
                                        }
                                    }
                                });
                                mState = STATE_DISMISSING;
                                mDialog.dismiss();
                            } catch (Throwable ignored) {
                            }
                        }
                    }
                }, 200 + mResultDuration);
            }
        }
    }

    private void stopLoadingAnimation() {
        Drawable drawable = mIconView.getDrawable();
        if (drawable != null && drawable instanceof Animatable) {
            if (((Animatable) drawable).isRunning()) {
                ((Animatable) drawable).stop();
            }
        }
    }

    private void startLoadingAnimation() {
        Drawable drawable = mIconView.getDrawable();
        if (drawable != null && drawable instanceof Animatable) {
            if (!((Animatable) drawable).isRunning()) {
                ((Animatable) drawable).start();
            }
        }
    }

    private void release() {
        if (mState == STATE_FREE) {
            return;
        }

        releaseResource();
        mState = STATE_FREE;
    }

    private void releaseResource() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }

        if (mState == STATE_LOADING) {
            stopLoadingAnimation();
        } else if (mState == STATE_RESULTING) {
            mIconView.animate().cancel();
        }
    }

    /**
     * 安全dismiss，会导致endAction不回掉，动画提前结束
     * <p>
     * 适合用在dialog附带的activity/fragment销毁的时候调用
     */
    public void dismissImmediatelyLossState() {
        if (mState == STATE_LOADING || mState == STATE_RESULTING) {
            releaseResource();
            mReshowingWhileDismissing = false;
            mState = STATE_DISMISSING;
            mDialog.setOnDismissListener(mDismissListener);
            try {
                mDialog.dismiss();
            } catch (Exception ignored) {
            }
        }
    }

    @State
    public int getState() {
        return mState;
    }

    /**
     * 只有在free和dismissing状态才能show
     * <p>
     * {@link #show()}
     */
    public boolean isShowing() {
        return mState == STATE_LOADING || mState == STATE_RESULTING;
    }

    public static class Builder {

        private final Context context;
        private String message;
        private boolean cancelable;
        private long resultDuration;
        private int okIcon, failIcon;
        private int loadingDrawable;

        public Builder(@NonNull Context context) {
            this.context = context;
            this.cancelable = sDefaultCancelable;
            this.resultDuration = sDefaultResultDuration;
            this.okIcon = sDefaultOkIcon;
            this.failIcon = sDefaultFailIcon;
            this.loadingDrawable = sDefaultLoadingDrawable;
        }

        /**
         * @param message 消息
         */
        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder message(@StringRes int messageRes) {
            try {
                this.message = context.getResources().getString(messageRes);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return this;
        }

        /**
         * @param cancelable 是否可以手动取消, 默认不可以
         */
        public Builder cancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        /**
         * @param resultDuration 结果动画的时常, 负数和默认都是1000
         */
        public Builder resultDuration(long resultDuration) {
            if (resultDuration >= 0) {
                this.resultDuration = resultDuration;
            } else {
                this.resultDuration = sDefaultResultDuration;
            }
            return this;
        }

        public Builder okIcon(@DrawableRes int drawableRes) {
            okIcon = drawableRes;
            return this;
        }

        public Builder failIcon(@DrawableRes int drawableRes) {
            this.failIcon = drawableRes;
            return this;
        }

        public Builder loadingDrawable(@DrawableRes int drawableRes) {
            this.loadingDrawable = drawableRes;
            return this;
        }

        public XXFLoading create() {
            return new XXFLoading(this);
        }
    }


    @IntDef({STATE_FREE, STATE_LOADING, STATE_RESULTING})
    @Retention(RetentionPolicy.CLASS)
    public @interface State {
    }

}
