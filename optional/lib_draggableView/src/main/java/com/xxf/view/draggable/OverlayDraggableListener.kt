package com.xxf.view.draggable

import android.view.WindowManager

interface OverlayDraggableListener {
    fun onParamsChanged(updatedParams: WindowManager.LayoutParams)
}