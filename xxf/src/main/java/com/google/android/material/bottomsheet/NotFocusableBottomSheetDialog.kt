package com.google.android.material.bottomsheet

import android.content.Context
import android.content.DialogInterface
import com.xxf.arch.R

/**
 * 外层阴影可以穿透
 * touch_outside 高度设置为0
 * 整体布局是wrap_content
 */
open class NotFocusableBottomSheetDialog : InnerBottomSheetDialog {
    constructor(context: Context) : super(context)
    constructor(context: Context, theme: Int) : super(context, theme)
    constructor(
        context: Context,
        cancelable: Boolean,
        cancelListener: DialogInterface.OnCancelListener?
    ) : super(context, cancelable, cancelListener)

    override fun getContainerId(): Int {
        return R.layout.xxf_not_focusable_design_bottom_sheet_dialog
    }
}