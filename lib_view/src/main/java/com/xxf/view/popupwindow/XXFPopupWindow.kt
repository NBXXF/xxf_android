package com.xxf.view.popupwindow

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import com.xxf.json.migration.Color
import com.xxf.ktx.findActivity

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2018/9/7
 * Description ://
 */
open class XXFPopupWindow : PopupWindow {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    constructor() : super()
    constructor(contentView: View?) : super(contentView)
    constructor(width: Int, height: Int) : super(width, height)
    constructor(contentView: View?, width: Int, height: Int) : super(contentView, width, height)
    constructor(
        contentView: View?,
        width: Int,
        height: Int,
        focusable: Boolean
    ) : super(contentView, width, height, focusable)

    /**
     * 范围从1.0(全黑)到0.0(全亮)。
     */
    open var dimAmount = 0.3F
    private var originDimAmount = -1.0f
    override fun showAsDropDown(anchor: View?, xoff: Int, yoff: Int, gravity: Int) {
        recordDimAmount()
        handleDimAmount(this.dimAmount)
        super.showAsDropDown(anchor, xoff, yoff, gravity)
    }

    override fun showAtLocation(parent: View?, gravity: Int, x: Int, y: Int) {
        recordDimAmount()
        handleDimAmount(this.dimAmount)
        super.showAtLocation(parent, gravity, x, y)
    }

    private fun recordDimAmount() {
        val findActivity = requireNotNull(contentView).context.findActivity()!!
        val lp: WindowManager.LayoutParams = findActivity.window.attributes
        originDimAmount = lp.dimAmount
    }

    private fun handleDimAmount(dimAmount: Float) {
        if (dimAmount in 0.0f..1.0f) {
            val findActivity = requireNotNull(contentView).context.findActivity()!!
            val lp: WindowManager.LayoutParams = findActivity.window.attributes
            lp.dimAmount = dimAmount
            findActivity.window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            findActivity.window.setAttributes(lp)
        }
    }

    override fun setContentView(contentView: View?) {
        initConfig()
        super.setContentView(contentView)
    }

    private fun resetDimAmount() {
        handleDimAmount(originDimAmount)
    }

    private fun initConfig() {
        //可以响应返回键
        isFocusable = true
        if (background == null) {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun dismiss() {
        resetDimAmount()
        super.dismiss()
    }
}