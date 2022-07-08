package com.xxf.arch.component

import android.view.View
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior

interface BottomSheetComponent : ContainerComponent {

    fun getBottomSheetView(): FrameLayout?

    fun getBehavior(): BottomSheetBehavior<FrameLayout>?
}