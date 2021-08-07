package com.xxf.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.util.LruCache
import android.view.View
import androidx.annotation.CheckResult
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import java.util.*

/**
 * @Description: RecyclerViewView 工具类
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/7/30 13:57
 */
object RecyclerViewUtils {
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
                    topMargin = marginLayoutParams.topMargin + linearLayoutManager!!.getTopDecorationHeight(childAt)
                    bottomMargin = marginLayoutParams.bottomMargin + linearLayoutManager.getBottomDecorationHeight(childAt)
                }
                val topY = height + topMargin
                topIndex.add(topY)
                val bitmapFromView = createBitmap(childAt)
                bitmapCache.put(i, bitmapFromView)
                height = topY + childAt.height + bottomMargin
            }
            height += recyclerView.paddingBottom
            val bigBitmap = Bitmap.createBitmap(recyclerView.measuredWidth, height, Bitmap.Config.ARGB_8888)
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
                bigCanvas.drawBitmap(bitmap, if (x > 0) x.toFloat() else 0.toFloat(), topIndex[i].toFloat(), paint)
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