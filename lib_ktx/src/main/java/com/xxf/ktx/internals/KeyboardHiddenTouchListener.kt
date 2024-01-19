package com.xxf.ktx.internals

import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import com.xxf.ktx.hideKeyboard

/**
 * 触摸事件 关闭键盘
 */
class KeyboardHiddenTouchListener : View.OnTouchListener {
    private var gestureDetector: GestureDetector? = null

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (gestureDetector == null) {
            gestureDetector = GestureDetector(v!!.context!!, object : SimpleOnGestureListener() {
                override fun onShowPress(e: MotionEvent?) {
                    super.onShowPress(e)
                    v.hideKeyboard()
                }

                override fun onSingleTapUp(e: MotionEvent?): Boolean {
                    v.hideKeyboard()
                    return super.onSingleTapUp(e)
                }

                override fun onScroll(
                    e1: MotionEvent?,
                    e2: MotionEvent?,
                    distanceX: Float,
                    distanceY: Float
                ): Boolean {
                    v.hideKeyboard()
                    return super.onScroll(e1, e2, distanceX, distanceY)
                }
            })
        }
        gestureDetector!!.onTouchEvent(event)
        return false
    }
}