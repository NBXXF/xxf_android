package com.xxf.view.snackbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.view.ViewCompat;

public class SnackbarLayout extends LinearLayout {
    private TextView mMessageView;
    private Button mActionView;

    public int mMaxWidth;
    private int mMaxInlineActionWidth;

    interface OnLayoutChangeListener {
        void onLayoutChange(View view, int left, int top, int right, int bottom);
    }

    interface OnAttachStateChangeListener {
        void onViewAttachedToWindow(View v);

        void onViewDetachedFromWindow(View v);
    }

    private OnLayoutChangeListener mOnLayoutChangeListener;
    private OnAttachStateChangeListener mOnAttachStateChangeListener;

    public SnackbarLayout(Context context) {
        this(context, null);
    }

    public SnackbarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SnackbarLayout);
        mMaxWidth = a.getDimensionPixelSize(R.styleable.SnackbarLayout_android_maxWidth, -1);
        mMaxInlineActionWidth = a.getDimensionPixelSize(
                R.styleable.SnackbarLayout_maxActionInlineWidth, -1);
        if (a.hasValue(R.styleable.SnackbarLayout_elevation)) {
            ViewCompat.setElevation(this, a.getDimensionPixelSize(
                    R.styleable.SnackbarLayout_elevation, 0));
        }
        a.recycle();

        setClickable(true);


        LayoutInflater.from(context)
                .inflate(R.layout.xxf_snackbar_layout_include, this);

        ViewCompat.setAccessibilityLiveRegion(this,
                ViewCompat.ACCESSIBILITY_LIVE_REGION_POLITE);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mMessageView = (TextView) findViewById(R.id.snackbar_text);
        mActionView = (Button) findViewById(R.id.snackbar_action);
    }

    TextView getMessageView() {
        return mMessageView;
    }

    Button getActionView() {
        return mActionView;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (mMaxWidth > 0 && getMeasuredWidth() > mMaxWidth) {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(mMaxWidth, MeasureSpec.EXACTLY);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

        final int multiLineVPadding = getResources().getDimensionPixelSize(
                R.dimen.design_snackbar_padding_vertical_2lines);
        final int singleLineVPadding = getResources().getDimensionPixelSize(
                R.dimen.design_snackbar_padding_vertical);
        final boolean isMultiLine = mMessageView.getLayout()
                .getLineCount() > 1;

        boolean remeasure = false;
        if (isMultiLine && mMaxInlineActionWidth > 0
                && mActionView.getMeasuredWidth() > mMaxInlineActionWidth) {
            if (updateViewsWithinLayout(VERTICAL, multiLineVPadding,
                    multiLineVPadding - singleLineVPadding)) {
                remeasure = true;
            }
        } else {
            final int messagePadding = isMultiLine ? multiLineVPadding : singleLineVPadding;
            if (updateViewsWithinLayout(HORIZONTAL, messagePadding, messagePadding)) {
                remeasure = true;
            }
        }

        if (remeasure) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    void animateChildrenIn(int delay, int duration) {
        ViewCompat.setAlpha(mMessageView, 0f);
        ViewCompat.animate(mMessageView)
                .alpha(1f)
                .setDuration(duration)
                .setStartDelay(delay)
                .start();

        if (mActionView.getVisibility() == VISIBLE) {
            ViewCompat.setAlpha(mActionView, 0f);
            ViewCompat.animate(mActionView)
                    .alpha(1f)
                    .setDuration(duration)
                    .setStartDelay(delay)
                    .start();
        }
    }

    void animateChildrenOut(int delay, int duration) {
        ViewCompat.setAlpha(mMessageView, 1f);
        ViewCompat.animate(mMessageView)
                .alpha(0f)
                .setDuration(duration)
                .setStartDelay(delay)
                .start();

        if (mActionView.getVisibility() == VISIBLE) {
            ViewCompat.setAlpha(mActionView, 1f);
            ViewCompat.animate(mActionView)
                    .alpha(0f)
                    .setDuration(duration)
                    .setStartDelay(delay)
                    .start();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (mOnLayoutChangeListener != null) {
            mOnLayoutChangeListener.onLayoutChange(this, l, t, r, b);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mOnAttachStateChangeListener != null) {
            mOnAttachStateChangeListener.onViewAttachedToWindow(this);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mOnAttachStateChangeListener != null) {
            mOnAttachStateChangeListener.onViewDetachedFromWindow(this);
        }
    }

    void setOnLayoutChangeListener(OnLayoutChangeListener onLayoutChangeListener) {
        mOnLayoutChangeListener = onLayoutChangeListener;
    }

    void setOnAttachStateChangeListener(OnAttachStateChangeListener listener) {
        mOnAttachStateChangeListener = listener;
    }

    private boolean updateViewsWithinLayout(final int orientation,
                                            final int messagePadTop, final int messagePadBottom) {
        boolean changed = false;
        if (orientation != getOrientation()) {
            setOrientation(orientation);
            changed = true;
        }
        if (mMessageView.getPaddingTop() != messagePadTop
                || mMessageView.getPaddingBottom() != messagePadBottom) {
            updateTopBottomPadding(mMessageView, messagePadTop, messagePadBottom);
            changed = true;
        }
        return changed;
    }

    private static void updateTopBottomPadding(View view, int topPadding, int bottomPadding) {
        if (ViewCompat.isPaddingRelative(view)) {
            ViewCompat.setPaddingRelative(view,
                    ViewCompat.getPaddingStart(view), topPadding,
                    ViewCompat.getPaddingEnd(view), bottomPadding);
        } else {
            view.setPadding(view.getPaddingLeft(), topPadding,
                    view.getPaddingRight(), bottomPadding);
        }
    }
}