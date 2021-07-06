package com.xxf.view.snackbar;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.behavior.SwipeDismissBehavior;

import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorListenerAdapter;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


public final class Snackbar {

    public static abstract class Callback {

        public static final int DISMISS_EVENT_SWIPE = 0;

        public static final int DISMISS_EVENT_ACTION = 1;

        public static final int DISMISS_EVENT_TIMEOUT = 2;

        public static final int DISMISS_EVENT_MANUAL = 3;

        public static final int DISMISS_EVENT_CONSECUTIVE = 4;

        @IntDef({
                DISMISS_EVENT_SWIPE, DISMISS_EVENT_ACTION, DISMISS_EVENT_TIMEOUT,
                DISMISS_EVENT_MANUAL, DISMISS_EVENT_CONSECUTIVE
        })

        @Retention(RetentionPolicy.SOURCE)
        public @interface DismissEvent {

        }

        public void onDismissed(Snackbar snackbar, @DismissEvent int event) {

        }

        public void onShown(Snackbar snackbar) {

        }
    }

    @IntDef({LENGTH_INDEFINITE, LENGTH_SHORT, LENGTH_LONG})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Duration {
    }

    public static final int LENGTH_INDEFINITE = -2;

    public static final int LENGTH_SHORT = -1;

    public static final int LENGTH_LONG = 0;

    private static final int ANIMATION_DURATION = 250;
    private static final int ANIMATION_FADE_DURATION = 180;

    private static final Handler sHandler;
    private static final int MSG_SHOW = 0;
    private static final int MSG_DISMISS = 1;

    static {
        sHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                switch (message.what) {
                    case MSG_SHOW:
                        ((Snackbar) message.obj).showView();
                        return true;
                    case MSG_DISMISS:
                        ((Snackbar) message.obj).hideView(message.arg1);
                        return true;
                }
                return false;
            }
        });
    }

    private final ViewGroup mParent;
    private final Context mContext;
    private final SnackbarLayout mView;
    private int mDuration;
    private Callback mCallback;

    private Snackbar(ViewGroup parent) {
        mParent = parent;
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        mView = (SnackbarLayout) inflater.inflate(R.layout.xxf_snackbar_layout, mParent, false);
    }

    @NonNull
    public static Snackbar make(@NonNull View view, @NonNull CharSequence text, @Duration int duration) {
        Snackbar snackbar = new Snackbar(findSuitableParent(view));
        snackbar.setText(text);
        snackbar.setDuration(duration);
        return snackbar;
    }

    @NonNull
    public static Snackbar make(@NonNull View view, @StringRes int resId, @Duration int duration) {
        return make(view, view.getResources()
                .getText(resId), duration);
    }

    private static ViewGroup findSuitableParent(View view) {
        ViewGroup fallback = null;
        do {
            if (view instanceof CoordinatorLayout) {
                return (ViewGroup) view;
            } else if (view instanceof FrameLayout) {
                if (view.getId() == android.R.id.content) {
                    return (ViewGroup) view;
                } else {
                    fallback = (ViewGroup) view;
                }
            } else if (view instanceof androidx.appcompat.widget.Toolbar || view instanceof Toolbar) {
                /*
                    If we return the toolbar here, the toast will be attached inside the toolbar.
                    So we need to find a some sibling ViewGroup to the toolbar that comes after the toolbar
                    If we don't find such view, the toast will be attached to the root activity view
                 */
                if (view.getParent() instanceof ViewGroup) {
                    ViewGroup parent = (ViewGroup) view.getParent();

                    // check if there's something else beside toolbar
                    if (parent.getChildCount() > 1) {
                        int childrenCnt = parent.getChildCount();
                        int toolbarIdx = 0;
                        for (int i = 0; i < childrenCnt; i++) {
                            // find the index of toolbar in the layout (most likely 0, but who knows)
                            if (parent.getChildAt(i) == view) {
                                toolbarIdx = i;
                                // check if there's something else after the toolbar in the layout
                                if (toolbarIdx < childrenCnt - 1) {
                                    //try to find some ViewGroup where you can attach the toast
                                    while (i < childrenCnt) {
                                        i++;
                                        View v = parent.getChildAt(i);
                                        if (v instanceof ViewGroup) return (ViewGroup) v;
                                    }
                                }
                                break;
                            }
                        }
                    }
                }

//                return (ViewGroup) view;
            }

            if (view != null) {
                final ViewParent parent = view.getParent();
                view = parent instanceof View ? (View) parent : null;
            }
        } while (view != null);

        return fallback;
    }

    @Deprecated
    public Snackbar addIcon(int resource_id, int size) {
        final TextView tv = mView.getMessageView();

        tv.setCompoundDrawablesWithIntrinsicBounds(new BitmapDrawable(Bitmap.createScaledBitmap(((BitmapDrawable) (mContext.getResources()
                .getDrawable(resource_id))).getBitmap(), size, size, true)), null, null, null);

        return this;
    }

    public Snackbar setIconPadding(int padding) {
        final TextView tv = mView.getMessageView();
        tv.setCompoundDrawablePadding(padding);
        return this;
    }

    public Snackbar setIconLeft(@DrawableRes int drawableRes, float sizeDp) {
        final TextView tv = mView.getMessageView();
        Drawable drawable = ContextCompat.getDrawable(mContext, drawableRes);
        if (drawable != null) {
            drawable = fitDrawable(drawable, (int) convertDpToPixel(sizeDp, mContext));
        } else {
            throw new IllegalArgumentException("resource_id is not a valid drawable!");
        }
        final Drawable[] compoundDrawables = tv.getCompoundDrawables();
        tv.setCompoundDrawables(drawable, compoundDrawables[1], compoundDrawables[2], compoundDrawables[3]);
        return this;
    }

    public Snackbar setIconRight(@DrawableRes int drawableRes, float sizeDp) {
        final TextView tv = mView.getMessageView();
        Drawable drawable = ContextCompat.getDrawable(mContext, drawableRes);
        if (drawable != null) {
            drawable = fitDrawable(drawable, (int) convertDpToPixel(sizeDp, mContext));
        } else {
            throw new IllegalArgumentException("resource_id is not a valid drawable!");
        }
        final Drawable[] compoundDrawables = tv.getCompoundDrawables();
        tv.setCompoundDrawables(compoundDrawables[0], compoundDrawables[1], drawable, compoundDrawables[3]);
        return this;
    }

    /**
     * Overrides the max width of this snackbar's layout. This is typically not necessary; the snackbar
     * width will be according to Google's Material guidelines. Specifically, the max width will be
     * <p>
     * To allow the snackbar to have a width equal to the parent view, set a value <= 0.
     *
     * @param maxWidth the max width in pixels
     * @return this TSnackbar
     */
    public Snackbar setMaxWidth(int maxWidth) {
        mView.mMaxWidth = maxWidth;

        return this;
    }

    private Drawable fitDrawable(Drawable drawable, int sizePx) {
        if (drawable.getIntrinsicWidth() != sizePx || drawable.getIntrinsicHeight() != sizePx) {

            if (drawable instanceof BitmapDrawable) {

                drawable = new BitmapDrawable(mContext.getResources(), Bitmap.createScaledBitmap(getBitmap(drawable), sizePx, sizePx, true));
            }
        }
        drawable.setBounds(0, 0, sizePx, sizePx);

        return drawable;
    }

    private static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static Bitmap getBitmap(VectorDrawable vectorDrawable) {
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }

    private static Bitmap getBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof VectorDrawable) {
            return getBitmap((VectorDrawable) drawable);
        } else {
            throw new IllegalArgumentException("unsupported drawable type");
        }
    }

    @NonNull
    public Snackbar setAction(@StringRes int resId, View.OnClickListener listener) {
        return setAction(mContext.getText(resId), listener);
    }

    @NonNull
    public Snackbar setAction(CharSequence text, final View.OnClickListener listener) {
        return setAction(text, true, listener);
    }

    @NonNull
    public Snackbar setAction(CharSequence text, final boolean shouldDismissOnClick, final View.OnClickListener listener) {
        final TextView tv = mView.getActionView();

        if (TextUtils.isEmpty(text) || listener == null) {
            tv.setVisibility(View.GONE);
            tv.setOnClickListener(null);
        } else {
            tv.setVisibility(View.VISIBLE);
            tv.setText(text);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(view);
                    if(shouldDismissOnClick) {
                        dispatchDismiss(Callback.DISMISS_EVENT_ACTION);
                    }
                }
            });
        }
        return this;
    }

    @NonNull
    public Snackbar setActionTextColor(ColorStateList colors) {
        final TextView tv = mView.getActionView();
        tv.setTextColor(colors);
        return this;
    }

    @NonNull
    public Snackbar setActionTextColor(@ColorInt int color) {
        final TextView tv = mView.getActionView();
        tv.setTextColor(color);
        return this;
    }


    @NonNull
    public Snackbar setText(@NonNull CharSequence message) {
        final TextView tv = mView.getMessageView();
        tv.setText(message);
        return this;
    }


    @NonNull
    public Snackbar setText(@StringRes int resId) {
        return setText(mContext.getText(resId));
    }

    @NonNull
    public Snackbar setDuration(@Duration int duration) {
        mDuration = duration;
        return this;
    }

    @Duration
    public int getDuration() {
        return mDuration;
    }

    @NonNull
    public View getView() {
        return mView;
    }

    public void show() {
        SnackbarManager.getInstance()
                .show(mDuration, mManagerCallback);
    }

    public void dismiss() {
        dispatchDismiss(Callback.DISMISS_EVENT_MANUAL);
    }

    private void dispatchDismiss(@Callback.DismissEvent int event) {
        SnackbarManager.getInstance()
                .dismiss(mManagerCallback, event);
    }

    @NonNull
    public Snackbar setCallback(Callback callback) {
        mCallback = callback;
        return this;
    }

    public boolean isShown() {
        return SnackbarManager.getInstance()
                .isCurrent(mManagerCallback);
    }

    public boolean isShownOrQueued() {
        return SnackbarManager.getInstance()
                .isCurrentOrNext(mManagerCallback);
    }

    private final SnackbarManager.Callback mManagerCallback = new SnackbarManager.Callback() {
        @Override
        public void show() {
            sHandler.sendMessage(sHandler.obtainMessage(MSG_SHOW, Snackbar.this));
        }

        @Override
        public void dismiss(int event) {
            sHandler.sendMessage(sHandler.obtainMessage(MSG_DISMISS, event, 0, Snackbar.this));
        }
    };

    final void showView() {
        if (mView.getParent() == null) {
            final ViewGroup.LayoutParams lp = mView.getLayoutParams();

            if (lp instanceof CoordinatorLayout.LayoutParams) {
                final Behavior behavior = new Behavior();
                behavior.setStartAlphaSwipeDistance(0.1f);
                behavior.setEndAlphaSwipeDistance(0.6f);
                behavior.setSwipeDirection(SwipeDismissBehavior.SWIPE_DIRECTION_START_TO_END);
                behavior.setListener(new SwipeDismissBehavior.OnDismissListener() {
                    @Override
                    public void onDismiss(View view) {
                        dispatchDismiss(Callback.DISMISS_EVENT_SWIPE);
                    }

                    @Override
                    public void onDragStateChanged(int state) {
                        switch (state) {
                            case SwipeDismissBehavior.STATE_DRAGGING:
                            case SwipeDismissBehavior.STATE_SETTLING:

                                SnackbarManager.getInstance()
                                        .cancelTimeout(mManagerCallback);
                                break;
                            case SwipeDismissBehavior.STATE_IDLE:

                                SnackbarManager.getInstance()
                                        .restoreTimeout(mManagerCallback);
                                break;
                        }
                    }
                });
                ((CoordinatorLayout.LayoutParams) lp).setBehavior(behavior);
            }
            mParent.addView(mView);
        }

        mView.setOnAttachStateChangeListener(new SnackbarLayout.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                if (isShownOrQueued()) {
                    sHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            onViewHidden(Callback.DISMISS_EVENT_MANUAL);
                        }
                    });
                }
            }
        });

        if (ViewCompat.isLaidOut(mView)) {
            animateViewIn();
        } else {
            mView.setOnLayoutChangeListener(new SnackbarLayout.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View view, int left, int top, int right, int bottom) {
                    animateViewIn();
                    mView.setOnLayoutChangeListener(null);
                }
            });
        }
    }

    private void animateViewIn() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            ViewCompat.setTranslationY(mView, -mView.getHeight());
            ViewCompat.animate(mView)
                    .translationY(0f)
                    .setInterpolator(com.xxf.view.snackbar.AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR)
                    .setDuration(ANIMATION_DURATION)
                    .setListener(new ViewPropertyAnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(View view) {
                            mView.animateChildrenIn(ANIMATION_DURATION - ANIMATION_FADE_DURATION,
                                    ANIMATION_FADE_DURATION);
                        }

                        @Override
                        public void onAnimationEnd(View view) {
                            if (mCallback != null) {
                                mCallback.onShown(Snackbar.this);
                            }
                            SnackbarManager.getInstance()
                                    .onShown(mManagerCallback);
                        }
                    })
                    .start();
        } else {
            Animation anim = AnimationUtils.loadAnimation(mView.getContext(),
                    R.anim.xxf_sanckbar_top_in);
            anim.setInterpolator(com.xxf.view.snackbar.AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
            anim.setDuration(ANIMATION_DURATION);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    if (mCallback != null) {
                        mCallback.onShown(Snackbar.this);
                    }
                    SnackbarManager.getInstance()
                            .onShown(mManagerCallback);
                }

                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            mView.startAnimation(anim);
        }
    }

    private void animateViewOut(final int event) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            ViewCompat.animate(mView)
                    .translationY(-mView.getHeight())
                    .setInterpolator(com.xxf.view.snackbar.AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR)
                    .setDuration(ANIMATION_DURATION)
                    .setListener(new ViewPropertyAnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(View view) {
                            mView.animateChildrenOut(0, ANIMATION_FADE_DURATION);
                        }

                        @Override
                        public void onAnimationEnd(View view) {
                            onViewHidden(event);
                        }
                    })
                    .start();
        } else {
            Animation anim = AnimationUtils.loadAnimation(mView.getContext(), R.anim.xxf_sanckbar_top_out);
            anim.setInterpolator(com.xxf.view.snackbar.AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
            anim.setDuration(ANIMATION_DURATION);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    onViewHidden(event);
                }

                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            mView.startAnimation(anim);
        }
    }

    final void hideView(int event) {
        if (mView.getVisibility() != View.VISIBLE || isBeingDragged()) {
            onViewHidden(event);
        } else {
            animateViewOut(event);
        }
    }

    private void onViewHidden(int event) {

        SnackbarManager.getInstance()
                .onDismissed(mManagerCallback);

        if (mCallback != null) {
            mCallback.onDismissed(this, event);
        }

        final ViewParent parent = mView.getParent();
        if (parent instanceof ViewGroup) {
            ((ViewGroup) parent).removeView(mView);
        }
    }

    private boolean isBeingDragged() {
        final ViewGroup.LayoutParams lp = mView.getLayoutParams();

        if (lp instanceof CoordinatorLayout.LayoutParams) {
            final CoordinatorLayout.LayoutParams cllp = (CoordinatorLayout.LayoutParams) lp;
            final CoordinatorLayout.Behavior behavior = cllp.getBehavior();

            if (behavior instanceof SwipeDismissBehavior) {
                return ((SwipeDismissBehavior) behavior).getDragState()
                        != SwipeDismissBehavior.STATE_IDLE;
            }
        }
        return false;
    }


    final class Behavior extends SwipeDismissBehavior<SnackbarLayout> {
        @Override
        public boolean canSwipeDismissView(View child) {
            return child instanceof SnackbarLayout;
        }

        @Override
        public boolean onInterceptTouchEvent(CoordinatorLayout parent, SnackbarLayout child,
                                             MotionEvent event) {


            if (parent.isPointInChildBounds(child, (int) event.getX(), (int) event.getY())) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        SnackbarManager.getInstance()
                                .cancelTimeout(mManagerCallback);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        SnackbarManager.getInstance()
                                .restoreTimeout(mManagerCallback);
                        break;
                }
            }

            return super.onInterceptTouchEvent(parent, child, event);
        }
    }
}