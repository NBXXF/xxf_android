package com.xxf.view.popupwindow

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import com.nbxxf.kpower.json.migration.Color
import com.xxf.ktx.findActivity

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2018/9/7
 * Description ://初步解决返回 背景等问题
 */
open class XXFPopupWindow : PopupWindow {
    val context: Context

    constructor(context: Context) : super(context) {
        this.context = context
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        this.context = context
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        this.context = context
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        this.context = context
    }

    constructor(contentView: View) : super(contentView) {
        this.context = contentView.context
    }

    constructor(contentView: View, width: Int, height: Int) : super(contentView, width, height) {
        this.context = contentView.context
    }

    constructor(
        contentView: View,
        width: Int,
        height: Int,
        focusable: Boolean
    ) : super(contentView, width, height, focusable) {
        this.context = contentView.context
    }

    /**
     * 范围从1.0(全黑)到0.0(全亮)。
     */
    open var dimAmount = 0.3F

    /**
     * 要应用于整个窗口的 alpha 值。alpha 为 1.0 表示完全不透明，0.0 表示完全透明
     */
    open var alpha = 0.7F
    private var originDimAmount = -1.0f
    private var originAlpha = -1.0f
    override fun showAsDropDown(anchor: View?, xoff: Int, yoff: Int, gravity: Int) {
        recordWindowConfig()
        handleWindowConfig(this.dimAmount, this.alpha)
        super.showAsDropDown(anchor, xoff, yoff, gravity)
    }

    override fun showAtLocation(parent: View?, gravity: Int, x: Int, y: Int) {
        recordWindowConfig()
        handleWindowConfig(this.dimAmount, this.alpha)
        super.showAtLocation(parent, gravity, x, y)
    }

    private fun recordWindowConfig() {
        val findActivity = context.findActivity()!!
        val lp: WindowManager.LayoutParams = findActivity.window.attributes
        originDimAmount = lp.dimAmount
        originAlpha = lp.alpha
    }

    @JvmOverloads
    protected fun handleWindowConfig(dimAmount: Float, alpha: Float) {
        val findActivity = context.findActivity()!!
        val lp: WindowManager.LayoutParams = findActivity.window.attributes
        if (dimAmount in 0.0f..1.0f) {
            lp.dimAmount = dimAmount
        }
        if (alpha in 0.0f..1.0f) {
            lp.alpha = alpha
        }
        findActivity.window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        findActivity.window.setAttributes(lp)
    }

    private fun resetWindowConfig() {
        handleWindowConfig(originDimAmount, originAlpha)
    }

    override fun setContentView(contentView: View?) {
        initConfig()
        super.setContentView(contentView)
    }


    private fun initConfig() {
        //可以响应返回键
        isFocusable = true
        if (background == null) {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun dismiss() {
        resetWindowConfig()
        super.dismiss()
    }
}