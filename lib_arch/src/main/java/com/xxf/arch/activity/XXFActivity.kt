package com.xxf.arch.activity

import android.R
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.xxf.arch.component.FragmentComponent
import com.xxf.arch.component.WindowComponent
import com.xxf.view.round.CornerUtil.clipViewRadius


/**
 * @version 2.3.1
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description
 * @date createTime：2018/9/7
 */
open class XXFActivity : AppCompatActivity, WindowComponent, FragmentComponent {
    private var mCancelable = true
    private var mSavedInstanceState: Bundle? = null

    constructor()

    constructor(@LayoutRes contentLayoutId: Int) : super(contentLayoutId)


    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        mSavedInstanceState = savedInstanceState
        super.onCreate(savedInstanceState)
        this.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                super<DefaultLifecycleObserver>.onDestroy(owner)
                onDestroyView()
            }
        })
    }

    @CallSuper
    override fun setContentView(view: View) {
        super.setContentView(view)
        dispatchViewCreated()
    }

    @CallSuper
    override fun setContentView(view: View, params: ViewGroup.LayoutParams) {
        super.setContentView(view, params)
        dispatchViewCreated()
    }

    @CallSuper
    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        dispatchViewCreated()
    }


    override fun setWindowSize(width: Int, height: Int) {
        val window = window
        if (window != null) {
            val attributes = window.attributes
            attributes.width = width
            attributes.height = height
            window.attributes = attributes
        }
    }

    override fun setWindowWidth(width: Int) {
        val window = window
        if (window != null) {
            val attributes = window.attributes
            attributes.width = width
            window.attributes = attributes
        }
    }

    override fun setWindowHeight(height: Int) {
        val window = window
        if (window != null) {
            val attributes = window.attributes
            attributes.height = height
            window.attributes = attributes
        }
    }


    override fun getDecorView(): FrameLayout? {
        val window = window
        if (window != null) {
            return window.decorView as FrameLayout
        }
        return null
    }

    override fun getContentParent(): FrameLayout? {
        val window = window
        if (window != null) {
            return window.findViewById<View>(R.id.content) as FrameLayout
        }
        return null
    }

    override fun setWindowDimAmount(amount: Float) {
        val window = window
        if (window != null) {
            /**
             * activity 需要强制设置 主题默认是false
             * R.styleable.Window_backgroundDimEnabled
             */
            setWindowBackgroundDimEnabled(amount > 0)
            window.setDimAmount(amount)
        }
    }

    override fun setWindowGravity(gravity: Int) {
        val window = window
        window?.setGravity(gravity)
    }

    override fun setWindowBackground(drawable: Drawable) {
        val window = window
        window?.setBackgroundDrawable(drawable)
    }

    override fun setWindowBackground(color: Int) {
        val window = window
        window?.setBackgroundDrawable(ColorDrawable(color))
    }

    override fun setWindowBackgroundDimEnabled(enabled: Boolean) {
        val window = window
        if (window != null) {
            if (enabled) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            } else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            }
        }
    }

    override fun setWindowRadius(radius: Float) {
        val decorView = getDecorView()
        if (decorView != null) {
            clipViewRadius(decorView, radius)
        }
    }

    override fun setCanceledOnTouchOutside(cancel: Boolean) {
        if (cancel && !mCancelable) {
            mCancelable = true
        }
        this.setFinishOnTouchOutside(cancel)
    }


    override fun setCancelable(flag: Boolean) {
        mCancelable = flag
    }

    /**
     * activity 内部的是否能返回请用 OnBackPressedDispatcher
     */
    override fun onBackPressed() {
        if (mCancelable) {
            super.onBackPressed()
        }
    }


    private fun dispatchViewCreated() {
        onViewCreated(
            (findViewById<View>(R.id.content) as ViewGroup)
                .getChildAt(0), mSavedInstanceState
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    }

    override fun onDestroyView() {
    }
}
