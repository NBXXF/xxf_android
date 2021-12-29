package com.xxf.arch.test;

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.xxf.utils.BitmapUtils

/**
 * 滚动截图
 */
abstract class ScrollShotting(
    val recyclerView: RecyclerView,
    val delayTime: Long = 200,
    backgroundColor: Int = Color.TRANSPARENT
) {
    private val mHandler = Handler(Looper.getMainLooper())
    private val bitmapCache = mutableMapOf<Int, Bitmap>()
    private var startY = 0
    private var pageIndex = 0

    private val shotTask: Runnable = object : Runnable {
        override fun run() {
            val newStartY = recyclerView.computeVerticalScrollOffset()
            val height = recyclerView.computeVerticalScrollExtent()
            val width = recyclerView.width
            var createBitmap = Bitmap.createBitmap(
                width,
                height,
                Bitmap.Config.RGB_565
            )
            val c = Canvas(createBitmap)
            if (backgroundColor != Color.TRANSPARENT) {
                c.drawColor(backgroundColor)
            }
            recyclerView.draw(c)


            val distance = newStartY - startY
            /**
             * 不满一屏 截图
             */
            if (distance > 0 && distance < height) {
                //LogUtils.d("============>shot: cut   " + pageIndex + "  " + recyclerView.computeVerticalScrollOffset())
                createBitmap = Bitmap.createBitmap(
                    createBitmap,
                    0,
                    height - distance,
                    width,
                    distance
                )
            }
            val isLast = bitmapCache.containsKey(newStartY)
            if (!bitmapCache.containsKey(newStartY)) {
                bitmapCache.put(newStartY, createBitmap)
            }
           // LogUtils.d("============>shot:  " + pageIndex + "  " + recyclerView.computeVerticalScrollOffset() + "   isLast:" + isLast)

            if (!isLast && recyclerView.canScrollVertically(1)) {
                startY = recyclerView.computeVerticalScrollOffset()
                recyclerView.stopScroll()
                recyclerView.scrollBy(0, height)
                pageIndex++
                mHandler.postDelayed(this, delayTime)
            } else {
                val toList = bitmapCache.values.toList()
                var first: Bitmap? = toList.first()
                for (i in 1 until toList.size) {
                    first = BitmapUtils.mergeBitmapVertical(first, toList.get(i), true)
                }
                onShot(first!!)
            }
        }
    }


    fun start() {
        bitmapCache.clear()
        mHandler.removeCallbacksAndMessages(null)
        if (false && recyclerView.computeVerticalScrollOffset() > 0) {
            recyclerView.scrollBy(0, -recyclerView.computeVerticalScrollOffset())
            mHandler.postDelayed(Runnable {
                startY = recyclerView.computeVerticalScrollOffset()
                pageIndex = 0;
                recyclerView.post(shotTask)
            }, 200)
        } else {
            pageIndex = 0
            startY = recyclerView.computeVerticalScrollOffset()
            mHandler.post(shotTask)
        }
    }

    abstract fun onShot(bitmap: Bitmap)
}
