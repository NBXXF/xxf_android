package com.xxf.arch.component

import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior

interface BottomSheetComponent : WindowComponent {

    fun getBottomSheetView(): FrameLayout?

    fun getBehavior(): BottomSheetBehavior<FrameLayout>?
}