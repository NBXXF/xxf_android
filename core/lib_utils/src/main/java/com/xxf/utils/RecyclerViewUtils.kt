package com.xxf.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.LruCache
import android.view.View
import androidx.annotation.CheckResult
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import java.lang.Integer.min
import java.util.*

/**
 * @Description: RecyclerViewView 工具类
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/7/30 13:57
 */
object RecyclerViewUtils {
    /**
     * 滚动截图
     */
    abstract class ScrollShotting(val recyclerView: RecyclerView, val delayTime: Long = 200) {
        private val mHandler = Handler(Looper.getMainLooper())
        private val bitmapCache = mutableListOf<Bitmap>()
        private var startY = 0

        private val shotTask: Runnable = object : Runnable {
            override fun run() {
                val newStartY = recyclerView.computeVerticalScrollOffset()
                var createBitmap = Bitmap.createBitmap(
                    recyclerView.width,
                    recyclerView.height,
                    Bitmap.Config.ARGB_8888
                )
                val c = Canvas(createBitmap)
                recyclerView.draw(c)

                val distance = newStartY - startY
                /**
                 * 不满一屏 截图
                 */
                if (distance != 0 && distance < recyclerView.height) {
                    createBitmap = Bitmap.createBitmap(
                        createBitmap,
                        0,
                        recyclerView.height - distance,
                        createBitmap.width,
                        distance
                    )
                }
                bitmapCache.add(createBitmap)


                if (recyclerView.canScrollVertically(1)) {
                    startY = recyclerView.computeVerticalScrollOffset()
                    recyclerView.stopScroll()
                    recyclerView.scrollBy(0, recyclerView.computeVerticalScrollExtent())
                    mHandler.postDelayed(this, delayTime)
                } else {
                    val toList = bitmapCache
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
            startY = 0
            mHandler.removeCallbacksAndMessages(null)
            if (recyclerView.computeVerticalScrollOffset() > 0) {
                recyclerView.scrollBy(0, -recyclerView.computeVerticalScrollOffset())
                recyclerView.postDelayed(shotTask, delayTime)
            } else {
                recyclerView.post(shotTask)
            }
        }

        private fun createBitmap(v: View): Bitmap {
            val bmp = Bitmap.createBitmap(v.width, v.height, Bitmap.Config.ARGB_8888)
            val c = Canvas(bmp)
            v.draw(c)
            return bmp
        }

        abstract fun onShot(bitmap: Bitmap)
    }

    /**
     * 清除动画
     *
     * @param recyclerView
     */
    fun clearItemAnimator(recyclerView: RecyclerView?) {
        if (recyclerView == null) {
            return
        }
        if (recyclerView.itemAnimator != null) {
            recyclerView.itemAnimator!!.addDuration = 0
            recyclerView.itemAnimator!!.changeDuration = 0
            recyclerView.itemAnimator!!.moveDuration = 0
            recyclerView.itemAnimator!!.removeDuration = 0
        }
        if (recyclerView.itemAnimator is SimpleItemAnimator) {
            (recyclerView.itemAnimator as SimpleItemAnimator?)!!.supportsChangeAnimations = false
        }
    }

    private fun createBitmap(v: View): Bitmap {
        val bmp = Bitmap.createBitmap(v.width, v.height, Bitmap.Config.ARGB_8888)
        val c = Canvas(bmp)
        v.draw(c)
        return bmp
    }

//
//    public interface OnShotListener {
//        fun onShot(bitmap: Bitmap)
//    }
//
//    /**
//     * 滚动截屏
//     */
//    fun scrollShot(recyclerView: RecyclerView, listener: OnShotListener) {
//        /**
//         * 滚动到最开始的位置
//         */
//        recyclerView.scrollBy(0, -recyclerView.computeVerticalScrollOffset())
//        val bitmapCache = mutableMapOf<Int, Bitmap>()
//        val computeVerticalScrollExtent = recyclerView.computeVerticalScrollExtent()
//
//        /**
//         * RecyclerView.canScrollVertically(1); // false表示已经滚动到底部
//        RecyclerView.canScrollVertically(-1); // false表示已经滚动到顶部
//         */
//        bitmapCache.put(0, createBitmap(recyclerView))
//        while (recyclerView.canScrollVertically(1)) {
//            val startY = recyclerView.computeVerticalScrollOffset()
//            recyclerView.stopScroll()
//            recyclerView.scrollBy(0, recyclerView.computeVerticalScrollExtent())
//            val newStartY = recyclerView.computeVerticalScrollOffset()
//
//            var createBitmap = Bitmap.createBitmap(
//                recyclerView.width,
//                computeVerticalScrollExtent,
//                Bitmap.Config.ARGB_8888
//            )
//            val c = Canvas(createBitmap)
//            recyclerView.draw(c)
//
//            val distance = newStartY - startY
//            /**
//             * 不满一屏 截图
//             */
//            if (distance < computeVerticalScrollExtent) {
//                createBitmap = Bitmap.createBitmap(
//                    createBitmap,
//                    0,
//                    computeVerticalScrollExtent - distance,
//                    createBitmap.width,
//                    distance
//                )
//            }
//            bitmapCache.put(recyclerView.computeVerticalScrollOffset(), createBitmap)
//        }
//        val toList = bitmapCache.values.toList()
//        var first: Bitmap? = toList.first()
//        for (i in 1 until toList.size) {
//            first = BitmapUtils.mergeBitmapVertical(first, toList.get(i), true)
//        }
//        listener.onShot(first!!)
//    }


    /**
     * 截取屏幕 可见的部分
     * 如果用recyclerView直接截图会 最上最下的Item展示不全的问题
     *
     * @param recyclerView
     * @return
     */
    @CheckResult
    fun shotRecyclerViewVisibleItems(recyclerView: RecyclerView): Bitmap? {
        return shotRecyclerViewVisibleItems(recyclerView, -1)
    }

    /**
     * 截取屏幕 可见的部分
     *
     * @param recyclerView
     * @param backgroundColor -1 代表不设置背景
     * @return
     */
    @CheckResult
    fun shotRecyclerViewVisibleItems(recyclerView: RecyclerView, backgroundColor: Int): Bitmap? {
        if (!checkShotRecyclerView(recyclerView)) {
            return null
        }
        try {
            val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
            val paint = Paint()
            val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
            // Use 1/8th of the available memory for this memory cache.
            val cacheSize = maxMemory / 8
            val bitmapCache = LruCache<Int, Bitmap>(cacheSize)
            var height = recyclerView.paddingTop
            val topIndex: MutableList<Int> = ArrayList()
            for (i in 0 until recyclerView.childCount) {
                val childAt = recyclerView.getChildAt(i)
                val layoutParams = childAt.layoutParams
                var topMargin = 0
                var bottomMargin = 0
                if (layoutParams is RecyclerView.LayoutParams) {
                    val marginLayoutParams = layoutParams
                    topMargin =
                        marginLayoutParams.topMargin + linearLayoutManager!!.getTopDecorationHeight(
                            childAt
                        )
                    bottomMargin =
                        marginLayoutParams.bottomMargin + linearLayoutManager.getBottomDecorationHeight(
                            childAt
                        )
                }
                val topY = height + topMargin
                topIndex.add(topY)
                val bitmapFromView = createBitmap(childAt)
                bitmapCache.put(i, bitmapFromView)
                height = topY + childAt.height + bottomMargin
            }
            height += recyclerView.paddingBottom
            val bigBitmap =
                Bitmap.createBitmap(recyclerView.measuredWidth, height, Bitmap.Config.ARGB_8888)
            val bigCanvas = Canvas(bigBitmap)
            val lBackground = recyclerView.background
            if (backgroundColor != -1) {
                bigCanvas.drawColor(backgroundColor)
            } else if (lBackground is ColorDrawable) {
                val lColor = lBackground.color
                bigCanvas.drawColor(lColor)
            }
            for (i in 0 until bitmapCache.size()) {
                val bitmap = bitmapCache[i]
                val x = (bigBitmap.width - bitmap.width) / 2
                bigCanvas.drawBitmap(
                    bitmap,
                    if (x > 0) x.toFloat() else 0.toFloat(),
                    topIndex[i].toFloat(),
                    paint
                )
                bitmap.recycle()
            }
            return bigBitmap
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 是否支持截图
     *
     * @param recyclerView
     * @return
     */
    private fun checkShotRecyclerView(recyclerView: RecyclerView?): Boolean {
        return (recyclerView != null && recyclerView.layoutManager is LinearLayoutManager
                && (recyclerView.layoutManager as LinearLayoutManager?)!!.orientation == LinearLayoutManager.VERTICAL)
    }
}