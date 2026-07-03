package com.xxf.snackbar

import android.annotation.TargetApi
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.*
import android.view.animation.Animation
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.IntDef
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.ViewPropertyAnimatorListenerAdapter
import com.google.android.material.behavior.SwipeDismissBehavior
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

class Snackbar private constructor(private val mParent: ViewGroup?) {
    abstract class Callback {
        @IntDef(DISMISS_EVENT_SWIPE, DISMISS_EVENT_ACTION, DISMISS_EVENT_TIMEOUT, DISMISS_EVENT_MANUAL, DISMISS_EVENT_CONSECUTIVE)
        @Retention(RetentionPolicy.SOURCE)
        annotation class DismissEvent

        fun onDismissed(snackbar: Snackbar?, @DismissEvent event: Int) {}
        fun onShown(snackbar: Snackbar?) {}

        companion object {
            const val DISMISS_EVENT_SWIPE = 0
            const val DISMISS_EVENT_ACTION = 1
            const val DISMISS_EVENT_TIMEOUT = 2
            const val DISMISS_EVENT_MANUAL = 3
            const val DISMISS_EVENT_CONSECUTIVE = 4
        }
    }

    @IntDef(LENGTH_INDEFINITE, LENGTH_SHORT, LENGTH_LONG)
    @Retention(RetentionPolicy.SOURCE)
    annotation class Duration
    companion object {
        const val LENGTH_INDEFINITE = -2
        const val LENGTH_SHORT = -1
        const val LENGTH_LONG = 0
        private const val ANIMATION_DURATION = 250
        private const val ANIMATION_FADE_DURATION = 180
        private var sHandler: Handler? = null
        private const val MSG_SHOW = 0
        private const val MSG_DISMISS = 1
        fun make(view: View, text: CharSequence, @Duration duration: Int): Snackbar {
            val snackbar = Snackbar(findSuitableParent(view))
            snackbar.setText(text)
            snackbar.setDuration(duration)
            return snackbar
        }

        fun make(view: View, @StringRes resId: Int, @Duration duration: Int): Snackbar {
            return make(view, view.resources
                    .getText(resId), duration)
        }

        private fun findSuitableParent(view: View): ViewGroup? {
            var view: View? = view
            var fallback: ViewGroup? = null
            do {
                if (view is CoordinatorLayout) {
                    return view
                } else if (view is FrameLayout) {
                    fallback = if (view.getId() == android.R.id.content) {
                        return view
                    } else {
                        view
                    }
                } else if (view is Toolbar || view is android.widget.Toolbar) {
                    /*
                    If we return the toolbar here, the toast will be attached inside the toolbar.
                    So we need to find a some sibling ViewGroup to the toolbar that comes after the toolbar
                    If we don't find such view, the toast will be attached to the root activity view
                 */
                    if (view.parent is ViewGroup) {
                        val parent = view.parent as ViewGroup

                        // check if there's something else beside toolbar
                        if (parent.childCount > 1) {
                            val childrenCnt = parent.childCount
                            var toolbarIdx = 0
                            var i = 0
                            while (i < childrenCnt) {

                                // find the index of toolbar in the layout (most likely 0, but who knows)
                                if (parent.getChildAt(i) === view) {
                                    toolbarIdx = i
                                    // check if there's something else after the toolbar in the layout
                                    if (toolbarIdx < childrenCnt - 1) {
                                        //try to find some ViewGroup where you can attach the toast
                                        while (i < childrenCnt) {
                                            i++
                                            val v = parent.getChildAt(i)
                                            if (v is ViewGroup) return v
                                        }
                                    }
                                    break
                                }
                                i++
                            }
                        }
                    }

//                return (ViewGroup) view;
                }
                if (view != null) {
                    val parent = view.parent
                    view = if (parent is View) parent else null
                }
            } while (view != null)
            return fallback
        }

        private fun convertDpToPixel(dp: Float, context: Context): Float {
            val resources = context.resources
            val metrics = resources.displayMetrics
            return dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        private fun getBitmap(vectorDrawable: VectorDrawable): Bitmap {
            val bitmap = Bitmap.createBitmap(vectorDrawable.intrinsicWidth,
                    vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
            vectorDrawable.draw(canvas)
            return bitmap
        }

        private fun getBitmap(drawable: Drawable): Bitmap {
            return if (drawable is BitmapDrawable) {
                drawable.bitmap
            } else if (drawable is VectorDrawable) {
                getBitmap(drawable)
            } else {
                throw IllegalArgumentException("unsupported drawable type")
            }
        }

        init {
            sHandler = Handler(Looper.getMainLooper(), Handler.Callback { message ->
                when (message.what) {
                    MSG_SHOW -> {
                        (message.obj as Snackbar).showView()
                        return@Callback true
                    }
                    MSG_DISMISS -> {
                        (message.obj as Snackbar).hideView(message.arg1)
                        return@Callback true
                    }
                }
                false
            })
        }
    }

    private val mContext: Context
    private val mView: SnackbarLayout

    @get:Duration
    var duration = 0
        private set
    private var mCallback: Callback? = null
    @Deprecated("")
    fun addIcon(resource_id: Int, size: Int): Snackbar {
        val tv = mView.messageView
        tv!!.setCompoundDrawablesWithIntrinsicBounds(BitmapDrawable(Bitmap.createScaledBitmap((mContext.resources
                .getDrawable(resource_id) as BitmapDrawable).bitmap, size, size, true)), null, null, null)
        return this
    }

    fun setIconPadding(padding: Int): Snackbar {
        val tv = mView.messageView
        tv!!.compoundDrawablePadding = padding
        return this
    }

    fun setIconLeft(@DrawableRes drawableRes: Int, sizeDp: Float): Snackbar {
        val tv = mView.messageView
        var drawable = ContextCompat.getDrawable(mContext, drawableRes)
        drawable = if (drawable != null) {
            fitDrawable(drawable, convertDpToPixel(sizeDp, mContext).toInt())
        } else {
            throw IllegalArgumentException("resource_id is not a valid drawable!")
        }
        val compoundDrawables = tv!!.compoundDrawables
        tv.setCompoundDrawables(drawable, compoundDrawables[1], compoundDrawables[2], compoundDrawables[3])
        return this
    }

    fun setIconRight(@DrawableRes drawableRes: Int, sizeDp: Float): Snackbar {
        val tv = mView.messageView
        var drawable = ContextCompat.getDrawable(mContext, drawableRes)
        drawable = if (drawable != null) {
            fitDrawable(drawable, convertDpToPixel(sizeDp, mContext).toInt())
        } else {
            throw IllegalArgumentException("resource_id is not a valid drawable!")
        }
        val compoundDrawables = tv!!.compoundDrawables
        tv.setCompoundDrawables(compoundDrawables[0], compoundDrawables[1], drawable, compoundDrawables[3])
        return this
    }

    /**
     * Overrides the max width of this snackbar's layout. This is typically not necessary; the snackbar
     * width will be according to Google's Material guidelines. Specifically, the max width will be
     *
     *
     * To allow the snackbar to have a width equal to the parent view, set a value <= 0.
     *
     * @param maxWidth the max width in pixels
     * @return this TSnackbar
     */
    fun setMaxWidth(maxWidth: Int): Snackbar {
        mView.mMaxWidth = maxWidth
        return this
    }

    private fun fitDrawable(drawable: Drawable, sizePx: Int): Drawable {
        var drawable = drawable
        if (drawable.intrinsicWidth != sizePx || drawable.intrinsicHeight != sizePx) {
            if (drawable is BitmapDrawable) {
                drawable = BitmapDrawable(mContext.resources, Bitmap.createScaledBitmap(getBitmap(drawable), sizePx, sizePx, true))
            }
        }
        drawable.setBounds(0, 0, sizePx, sizePx)
        return drawable
    }

    fun setAction(@StringRes resId: Int, listener: View.OnClickListener?): Snackbar {
        return setAction(mContext.getText(resId), listener)
    }

    fun setAction(text: CharSequence?, listener: View.OnClickListener?): Snackbar {
        return setAction(text, true, listener)
    }

    fun setAction(text: CharSequence?, shouldDismissOnClick: Boolean, listener: View.OnClickListener?): Snackbar {
        val tv: TextView? = mView.actionView
        if (TextUtils.isEmpty(text) || listener == null) {
            tv!!.visibility = View.GONE
            tv.setOnClickListener(null)
        } else {
            tv!!.visibility = View.VISIBLE
            tv.text = text
            tv.setOnClickListener { view ->
                listener.onClick(view)
                if (shouldDismissOnClick) {
                    dispatchDismiss(Callback.DISMISS_EVENT_ACTION)
                }
            }
        }
        return this
    }

    fun setActionTextColor(colors: ColorStateList?): Snackbar {
        val tv: TextView? = mView.actionView
        tv!!.setTextColor(colors)
        return this
    }

    fun setActionTextColor(@ColorInt color: Int): Snackbar {
        val tv: TextView? = mView.actionView
        tv!!.setTextColor(color)
        return this
    }

    fun setText(message: CharSequence): Snackbar {
        val tv = mView.messageView
        tv!!.text = message
        return this
    }

    fun setText(@StringRes resId: Int): Snackbar {
        return setText(mContext.getText(resId))
    }

    fun setDuration(@Duration duration: Int): Snackbar {
        this.duration = duration
        return this
    }

    val view: View
        get() = mView

    fun show() {
        SnackbarManager.instance
                .show(duration, mManagerCallback)
    }

    fun dismiss() {
        dispatchDismiss(Callback.DISMISS_EVENT_MANUAL)
    }

    private fun dispatchDismiss(@Callback.DismissEvent event: Int) {
        SnackbarManager.instance
                .dismiss(mManagerCallback, event)
    }

    fun setCallback(callback: Callback?): Snackbar {
        mCallback = callback
        return this
    }

    val isShown: Boolean
        get() = SnackbarManager.instance
                .isCurrent(mManagerCallback)
    val isShownOrQueued: Boolean
        get() = SnackbarManager.instance
                .isCurrentOrNext(mManagerCallback)
    private val mManagerCallback: SnackbarManager.Callback = object : SnackbarManager.Callback {
        override fun show() {
            sHandler!!.sendMessage(sHandler!!.obtainMessage(MSG_SHOW, this@Snackbar))
        }

        override fun dismiss(event: Int) {
            sHandler!!.sendMessage(sHandler!!.obtainMessage(MSG_DISMISS, event, 0, this@Snackbar))
        }
    }

    fun showView() {
        if (mView.parent == null) {
            val lp = mView.layoutParams
            if (lp is CoordinatorLayout.LayoutParams) {
                val behavior: Behavior = Behavior()
                behavior.setStartAlphaSwipeDistance(0.1f)
                behavior.setEndAlphaSwipeDistance(0.6f)
                behavior.setSwipeDirection(SwipeDismissBehavior.SWIPE_DIRECTION_START_TO_END)
                behavior.listener = object : SwipeDismissBehavior.OnDismissListener {
                    override fun onDismiss(view: View) {
                        dispatchDismiss(Callback.DISMISS_EVENT_SWIPE)
                    }

                    override fun onDragStateChanged(state: Int) {
                        when (state) {
                            SwipeDismissBehavior.STATE_DRAGGING, SwipeDismissBehavior.STATE_SETTLING -> SnackbarManager.instance
                                    .cancelTimeout(mManagerCallback)
                            SwipeDismissBehavior.STATE_IDLE -> SnackbarManager.instance
                                    .restoreTimeout(mManagerCallback)
                        }
                    }
                }
                lp.behavior = behavior
            }
            mParent!!.addView(mView)
        }
        mView.setOnAttachStateChangeListener(object : SnackbarLayout.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View?) {}
            override fun onViewDetachedFromWindow(v: View?) {
                if (isShownOrQueued) {
                    sHandler!!.post { onViewHidden(Callback.DISMISS_EVENT_MANUAL) }
                }
            }
        })
        if (ViewCompat.isLaidOut(mView)) {
            animateViewIn()
        } else {
            mView.setOnLayoutChangeListener(object : SnackbarLayout.OnLayoutChangeListener {
                override fun onLayoutChange(view: View?, left: Int, top: Int, right: Int, bottom: Int) {
                    animateViewIn()
                    mView.setOnLayoutChangeListener(null)
                }
            })
        }
    }

    private fun animateViewIn() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            ViewCompat.setTranslationY(mView, -mView.height.toFloat())
            ViewCompat.animate(mView)
                    .translationY(0f)
                    .setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR)
                    .setDuration(ANIMATION_DURATION.toLong())
                    .setListener(object : ViewPropertyAnimatorListenerAdapter() {
                        override fun onAnimationStart(view: View) {
                            mView.animateChildrenIn(
                                ANIMATION_DURATION - ANIMATION_FADE_DURATION,
                                    ANIMATION_FADE_DURATION
                            )
                        }

                        override fun onAnimationEnd(view: View) {
                            if (mCallback != null) {
                                mCallback!!.onShown(this@Snackbar)
                            }
                            SnackbarManager.instance
                                    .onShown(mManagerCallback)
                        }
                    })
                    .start()
        } else {
            val anim = android.view.animation.AnimationUtils.loadAnimation(mView.context,
                    R.anim.xxf_sanckbar_top_in)
            anim.interpolator = AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR
            anim.duration = ANIMATION_DURATION.toLong()
            anim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationEnd(animation: Animation) {
                    if (mCallback != null) {
                        mCallback!!.onShown(this@Snackbar)
                    }
                    SnackbarManager.instance
                            .onShown(mManagerCallback)
                }

                override fun onAnimationStart(animation: Animation) {}
                override fun onAnimationRepeat(animation: Animation) {}
            })
            mView.startAnimation(anim)
        }
    }

    private fun animateViewOut(event: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            ViewCompat.animate(mView)
                    .translationY(-mView.height.toFloat())
                    .setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR)
                    .setDuration(ANIMATION_DURATION.toLong())
                    .setListener(object : ViewPropertyAnimatorListenerAdapter() {
                        override fun onAnimationStart(view: View) {
                            mView.animateChildrenOut(0, ANIMATION_FADE_DURATION)
                        }

                        override fun onAnimationEnd(view: View) {
                            onViewHidden(event)
                        }
                    })
                    .start()
        } else {
            val anim = android.view.animation.AnimationUtils.loadAnimation(mView.context, R.anim.xxf_sanckbar_top_out)
            anim.interpolator = AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR
            anim.duration = ANIMATION_DURATION.toLong()
            anim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationEnd(animation: Animation) {
                    onViewHidden(event)
                }

                override fun onAnimationStart(animation: Animation) {}
                override fun onAnimationRepeat(animation: Animation) {}
            })
            mView.startAnimation(anim)
        }
    }

    fun hideView(event: Int) {
        if (mView.visibility != View.VISIBLE || isBeingDragged) {
            onViewHidden(event)
        } else {
            animateViewOut(event)
        }
    }

    private fun onViewHidden(event: Int) {
        SnackbarManager.instance
                .onDismissed(mManagerCallback)
        if (mCallback != null) {
            mCallback!!.onDismissed(this, event)
        }
        val parent = mView.parent
        if (parent is ViewGroup) {
            parent.removeView(mView)
        }
    }

    private val isBeingDragged: Boolean
        private get() {
            val lp = mView.layoutParams
            if (lp is CoordinatorLayout.LayoutParams) {
                val behavior = lp.behavior
                if (behavior is SwipeDismissBehavior<*>) {
                    return (behavior.dragState
                            != SwipeDismissBehavior.STATE_IDLE)
                }
            }
            return false
        }

    internal inner class Behavior : SwipeDismissBehavior<SnackbarLayout?>() {
        override fun canSwipeDismissView(child: View): Boolean {
            return child is SnackbarLayout
        }

        override fun onInterceptTouchEvent(parent: CoordinatorLayout, child: SnackbarLayout,
                                           event: MotionEvent): Boolean {
            if (parent.isPointInChildBounds(child!!, event.x.toInt(), event.y.toInt())) {
                when (event.actionMasked) {
                    MotionEvent.ACTION_DOWN -> SnackbarManager.instance
                            .cancelTimeout(mManagerCallback)
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> SnackbarManager.instance
                            .restoreTimeout(mManagerCallback)
                }
            }
            return super.onInterceptTouchEvent(parent, child, event)
        }
    }

    init {
        mContext = mParent!!.context
        val inflater = LayoutInflater.from(mContext)
        mView = inflater.inflate(R.layout.xxf_snackbar_layout, mParent, false) as SnackbarLayout
    }
}