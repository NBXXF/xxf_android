package com.xxf.view.draggable

import android.view.View

interface DraggableListener {

    fun onPositionChanged(view: View)

    fun onLongPress(view: View)

}