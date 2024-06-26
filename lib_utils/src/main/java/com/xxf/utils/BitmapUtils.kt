package com.xxf.utils

import android.graphics.*
import android.graphics.drawable.Drawable
import android.media.ExifInterface
import android.media.MediaMetadataRetriever
import android.util.Base64
import android.util.Log
import android.util.Size
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.webkit.WebView
import android.widget.ScrollView
import androidx.annotation.CheckResult
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer
import kotlin.math.max
import kotlin.math.min

/**
 * @Description: bitmap处理工具类
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/10/13 11:14
 */
object BitmapUtils {
    /**
     * 解码 图片文件的属性
     * 0,0 代表没有宽度高度
     * @param path
     * @return
     */
    fun decodeSize(path: String?): Size {
        var decodeSize = Size(0, 0)
        val mimeType = FileUtils.getMimeType(path)
        if (mimeType?.startsWith("video") == true) {
            try {
                val retriever = MediaMetadataRetriever()
                retriever.setDataSource(path)
                val width =
                    retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH) //宽

                val height =
                    retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT) //高

                val rotation =
                    retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION) //视频的方向角度
                retriever.close()

                return if (rotation == "90" || rotation == "270") {
                    Size(height?.toIntOrNull() ?: 0, width?.toIntOrNull() ?: 0)
                } else {
                    Size(width?.toIntOrNull() ?: 0, height?.toIntOrNull() ?: 0)
                }
                //vivo x20 8.1 aused by: java.lang.NoSuchMethodError: No virtual method close()V in class Landroid/media/MediaMetadataRetriever
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        } else {
            try {
                val exifInterface = ExifInterface(path ?: "");
                val rotation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL
                );
                val width = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, 0);
                val height = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, 0);

                decodeSize = Size(width, height);
                when (rotation) {
                    ExifInterface.ORIENTATION_ROTATE_90, ExifInterface.ORIENTATION_ROTATE_270 -> {
                        decodeSize = Size(decodeSize.height, decodeSize.width)
                    }
                }
            } catch (e: Throwable) {
                e.printStackTrace()
            }
            if (decodeSize.width <= 0 || decodeSize.height <= 0) {
                try {
                    val options = BitmapFactory.Options()
                    options.inJustDecodeBounds = true //这个参数设置为true才有效，
                    val bmp = BitmapFactory.decodeFile(path, options) //这里的bitmap是个空
                    if (options.outWidth > 0 && options.outHeight > 0) {
                        decodeSize = Size(options.outWidth, options.outHeight)
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }
        return decodeSize
    }

    /**
     * 将图片变灰
     *
     * @param bitmap
     * @return
     */
    @JvmStatic
    @CheckResult
    fun grey(bitmap: Bitmap?): Bitmap? {
        if (bitmap != null) {
            try {
                val width = bitmap.width
                val height = bitmap.height
                val faceIconGreyBitmap = Bitmap
                    .createBitmap(width, height, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(faceIconGreyBitmap)
                val paint = Paint()
                val colorMatrix = ColorMatrix()
                colorMatrix.setSaturation(0f)
                val colorMatrixFilter = ColorMatrixColorFilter(
                    colorMatrix
                )
                paint.colorFilter = colorMatrixFilter
                canvas.drawBitmap(bitmap, 0f, 0f, paint)
                return faceIconGreyBitmap
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
        return null
    }

    /**
     * 图片缩放
     *
     * @param bitmap
     * @param dst_w
     * @param dst_h
     * @return
     */
    @JvmStatic
    @CheckResult
    fun scale(bitmap: Bitmap?, dst_w: Int, dst_h: Int): Bitmap? {
        try {
            val src_w = bitmap!!.width
            val src_h = bitmap.height
            val scale_w = dst_w.toFloat() / src_w
            val scale_h = dst_h.toFloat() / src_h
            val matrix = Matrix()
            matrix.postScale(scale_w, scale_h)
            return Bitmap.createBitmap(
                bitmap, 0, 0, src_w, src_h, matrix,
                true
            )
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * Drawable转换成一个Bitmap
     *
     * @param drawable drawable对象
     * @return
     */
    @CheckResult
    fun drawableToBitmap(drawable: Drawable): Bitmap? {
        try {
            val bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth, drawable.intrinsicHeight,
                if (drawable.opacity != PixelFormat.OPAQUE) Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
            drawable.draw(canvas)
            return bitmap
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 着色
     *
     * @param drawable
     * @param tintColor
     * @return
     */
    @CheckResult
    fun tint(drawable: Drawable?, tintColor: Int): Drawable? {
        try {
            val wrappedDrawable = DrawableCompat.wrap(drawable!!)
            DrawableCompat.setTint(wrappedDrawable, tintColor)
            return wrappedDrawable
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 着色
     *
     * @param bitmap
     * @param tintColor
     * @return
     */
    @CheckResult
    fun tint(bitmap: Bitmap?, tintColor: Int): Bitmap? {
        if (bitmap == null) {
            return null
        }
        try {
            val outputBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)
            val canvas = Canvas(outputBitmap)
            val paint = Paint()
            paint.colorFilter = PorterDuffColorFilter(tintColor, PorterDuff.Mode.SRC_IN)
            canvas.drawBitmap(bitmap, 0f, 0f, paint)
            return outputBitmap
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 转换byte
     *
     * @param bitmap
     * @return
     */
    @JvmStatic
    @CheckResult
    fun toByte(bitmap: Bitmap?): ByteArray? {
        try {
            val bytes = bitmap!!.byteCount
            val buffer = ByteBuffer.allocate(bytes)
            bitmap.copyPixelsToBuffer(buffer) //Move the byte data to the buffer
            return buffer.array()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 释放bitmap
     *
     * @param bitmap
     */
    @JvmStatic
    fun recycle(bitmap: Bitmap?) {
        try {
            if (bitmap != null && !bitmap.isRecycled) {
                bitmap.recycle()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 自动测量 适合recyclerView这种组件
     */
    fun makeMeasureJustView(view: View, maxWidth: Int, maxHeight: Int) {
        view.measure(
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        view.layout(
            0,
            0,
            Math.min(view.getMeasuredWidth(), maxWidth),
            Math.min(view.getMeasuredHeight(), maxHeight)
        )
    }

    /**
     * 创建bitmap
     * 适合未显示在UI界面上了的View
     *
     * @param view
     * @param backgroundColor
     * @param width  具体值
     * @param height 具体值
     * @return
     */
    @CheckResult
    fun createBitmap(
        view: View,
        width: Int,
        height: Int,
        backgroundColor: Int = Color.TRANSPARENT
    ): Bitmap? {
        try {
            val measuredWidth =
                View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY)
            val measuredHeight =
                View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
            view.measure(measuredWidth, measuredHeight)
            view.layout(0, 0, view.measuredWidth, view.measuredHeight)
            val bmp = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
            val c = Canvas(bmp)
            if (backgroundColor != Color.TRANSPARENT) {
                c.drawColor(backgroundColor)
            }
            view.draw(c)
            return bmp
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 创建bitmap
     * 适合已经显示在UI界面上了的View
     *
     * @param v
     * @return
     */
    @CheckResult
    fun createBitmap(v: View, backgroundColor: Int = Color.TRANSPARENT): Bitmap? {
        try {
            val bitmap = Bitmap.createBitmap(
                v.width, v.height,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            if (backgroundColor != Color.TRANSPARENT) {
                canvas.drawColor(backgroundColor)
            }
            v.draw(canvas)
            return bitmap
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 截取WebView的图片
     *
     * @param webView
     * @return
     */
    @CheckResult
    fun createBitmap(webView: WebView, backgroundColor: Int = Color.TRANSPARENT): Bitmap? {
        try {
            //指定测量规则
            webView.measure(0, 0)
            //获取webView宽高
            val width = webView.measuredWidth
            val height = webView.measuredHeight
            //设置缓存机制开启
            webView.isDrawingCacheEnabled = true
            //创建缓存
            webView.buildDrawingCache()
            //根据webView宽高创建bitmap
            val bm = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            //创建画布
            val bigCanvas = Canvas(bm)
            if (backgroundColor != Color.TRANSPARENT) {
                bigCanvas.drawColor(backgroundColor)
            }

            //绘制内容
            webView.draw(bigCanvas)
            return bm
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 截取ScrollView 截图
     *
     * @param scrollView
     * @return
     */
    @CheckResult
    fun createBitmap(scrollView: ScrollView, backgroundColor: Int = Color.TRANSPARENT): Bitmap? {
        try {
            var h = 0
            var bitmap: Bitmap? = null
            /**
             * 一般来说只有一个child
             */
            for (i in 0 until scrollView.childCount) {
                val childAt = scrollView.getChildAt(i)
                h += childAt.height
                if (childAt.layoutParams is MarginLayoutParams) {
                    val marginLayoutParams = childAt.layoutParams as MarginLayoutParams
                    h += marginLayoutParams.topMargin + marginLayoutParams.bottomMargin
                }
            }
            h += scrollView.paddingTop + scrollView.paddingBottom
            bitmap = Bitmap.createBitmap(
                scrollView.width, h,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap) //把创建的bitmap放到画布中去
            if (backgroundColor != Color.TRANSPARENT) {
                canvas.drawColor(backgroundColor)
            }
            scrollView.draw(canvas) //绘制canvas 画布
            return bitmap
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }


    /**
     *从base64 上面创建图片
     */
    fun createBitmapFromBase64(base64: String): Bitmap? {
        //data:image/jpeg;base64,/9j/4AAQSkZJRgA..........
        try {

            if (base64.contains("base64,")) {
                val decode = Base64.decode(base64.split(",")[1], Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.size);
                return bitmap
            } else if (base64.isNotEmpty()) {
                val decode = Base64.decode(base64, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.size);
                return bitmap
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 截取NestedScrollView 截图
     *
     * @param scrollView
     * @return
     */
    @CheckResult
    fun createBitmap(
        scrollView: NestedScrollView,
        backgroundColor: Int = Color.TRANSPARENT
    ): Bitmap? {
        try {
            var h = 0
            var bitmap: Bitmap? = null
            /**
             * 一般来说只有一个child
             */
            for (i in 0 until scrollView.childCount) {
                val childAt = scrollView.getChildAt(i)
                h += childAt.height
                if (childAt.layoutParams is MarginLayoutParams) {
                    val marginLayoutParams = childAt.layoutParams as MarginLayoutParams
                    h += marginLayoutParams.topMargin + marginLayoutParams.bottomMargin
                }
            }
            h += scrollView.paddingTop + scrollView.paddingBottom
            bitmap = Bitmap.createBitmap(
                scrollView.width, h,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap) //把创建的bitmap放到画布中去
            if (backgroundColor != Color.TRANSPARENT) {
                canvas.drawColor(backgroundColor)
            }
            scrollView.draw(canvas) //绘制canvas 画布
            return bitmap
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 把两个位图覆盖合成为一个位图，上下拼接
     *
     * @param topBitmap
     * @param bottomBitmap
     * @param isBaseMax    是否以高度大的位图为准，true则小图等比拉伸，false则大图等比压缩
     * @return
     */
    @CheckResult
    fun mergeBitmapVertical(
        topBitmap: Bitmap?,
        bottomBitmap: Bitmap?,
        isBaseMax: Boolean
    ): Bitmap? {
        try {
            if (topBitmap == null || topBitmap.isRecycled
                || bottomBitmap == null || bottomBitmap.isRecycled
            ) {
                Log.d("merge", "topBitmap=$topBitmap;bottomBitmap=$bottomBitmap")
                return null
            }
            var width = 0
            width = if (isBaseMax) {
                if (topBitmap.width > bottomBitmap.width) topBitmap.width else bottomBitmap.width
            } else {
                if (topBitmap.width < bottomBitmap.width) topBitmap.width else bottomBitmap.width
            }
            var tempBitmapT: Bitmap = topBitmap
            var tempBitmapB: Bitmap = bottomBitmap
            if (topBitmap.width != width) {
                tempBitmapT = Bitmap.createScaledBitmap(
                    topBitmap,
                    width,
                    (topBitmap.height * 1f / topBitmap.width * width).toInt(),
                    false
                )
            } else if (bottomBitmap.width != width) {
                tempBitmapB = Bitmap.createScaledBitmap(
                    bottomBitmap,
                    width,
                    (bottomBitmap.height * 1f / bottomBitmap.width * width).toInt(),
                    false
                )
            }
            val height = tempBitmapT.height + tempBitmapB.height
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            val topRect = Rect(0, 0, tempBitmapT.width, tempBitmapT.height)
            val bottomRect = Rect(0, 0, tempBitmapB.width, tempBitmapB.height)
            val bottomRectT = Rect(0, tempBitmapT.height, width, height)
            canvas.drawBitmap(tempBitmapT, topRect, topRect, null)
            canvas.drawBitmap(tempBitmapB, bottomRect, bottomRectT, null)
            return bitmap
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 渲染背景
     *
     * @param backgroundColor
     * @param orginBitmap
     * @return
     */
    @CheckResult
    fun drawBitmapBackground(orginBitmap: Bitmap?, backgroundColor: Int): Bitmap? {
        try {
            if (orginBitmap == null) {
                return null
            }
            val paint = Paint()
            paint.color = backgroundColor
            val bitmap = Bitmap.createBitmap(
                orginBitmap.width,
                orginBitmap.height, orginBitmap.config
            )
            val canvas = Canvas(bitmap)
            canvas.drawRect(
                0f,
                0f,
                orginBitmap.width.toFloat(),
                orginBitmap.height.toFloat(),
                paint
            )
            canvas.drawBitmap(orginBitmap, 0f, 0f, paint)
            return bitmap
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 保存图片
     */
    fun bitmapToFile(bitmap: Bitmap, file: File): Boolean {
        try {
            val fos = FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            return true
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * @param bitmap
     * @param rect
     * @param padding 上下左右偏移量
     */
    fun crop(bitmap: Bitmap, rect: Rect, padding: Int): Bitmap {
        return this.cropCompose(bitmap, listOfNotNull(rect), padding);
    }


    /**
     * 裁切 整个包含的最大部分
     * @param bitmap
     * @param rects
     * @param padding 上下左右偏移量
     */
    fun cropCompose(bitmap: Bitmap, rects: List<Rect>, padding: Int): Bitmap {
        try {
            val temp = Rect(rects.first());
            if (rects.size > 1) {
                rects.forEach {
                    temp.left = min(it.left, temp.left)
                    temp.top = min(it.top, temp.top)
                    temp.right = max(it.right, temp.right)
                    temp.bottom = max(it.bottom, temp.bottom)
                }
            }

            //处理边距 且不超过图片本身大小
            temp.left = max(temp.left - padding, 0)
            temp.top = max(temp.top - padding, 0)
            temp.right = min(temp.right + padding, bitmap.width);
            temp.bottom = min(temp.bottom + padding, bitmap.height);

            return Bitmap.createBitmap(bitmap, temp.left, temp.top, temp.width(), temp.height())
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return bitmap;
    }
}