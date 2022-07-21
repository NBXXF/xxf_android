package androidx.viewpager.widget

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.bottomsheet.BottomSheetUtils

/**
 * 自动处理 bottomsheet 和viewpager的嵌套手势问题
 */
open class BottomSheetViewPager:ViewPager {
    constructor(context: Context) : super(context){
        BottomSheetUtils.setupViewPager(this)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs){
        BottomSheetUtils.setupViewPager(this)
    }
}