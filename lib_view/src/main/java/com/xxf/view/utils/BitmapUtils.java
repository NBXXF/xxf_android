package com.xxf.view.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Size;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ScrollView;

import androidx.annotation.CheckResult;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.xxf.arch.XXF;
import com.xxf.view.recyclerview.RecyclerViewUtils;

import java.nio.ByteBuffer;

/**
 * @Description: bitmap处理工具类
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/10/13 11:14
 */
public class BitmapUtils {

    /**
     * 解码 图片文件的属性
     *
     * @param path
     * @return
     */
    @Nullable
    @CheckResult
    public static Size decodeSize(String path) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;//这个参数设置为true才有效，
            Bitmap bmp = BitmapFactory.decodeFile(path, options);//这里的bitmap是个空
            if (options.outWidth > 0 && options.outHeight > 0) {
                return new Size(options.outWidth, options.outHeight);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将图片变灰
     *
     * @param bitmap
     * @return
     */
    @Nullable
    @CheckResult
    public static final Bitmap grey(@Nullable Bitmap bitmap) {
        if (bitmap != null) {
            try {
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();

                Bitmap faceIconGreyBitmap = Bitmap
                        .createBitmap(width, height, Bitmap.Config.ARGB_8888);

                Canvas canvas = new Canvas(faceIconGreyBitmap);
                Paint paint = new Paint();
                ColorMatrix colorMatrix = new ColorMatrix();
                colorMatrix.setSaturation(0);
                ColorMatrixColorFilter colorMatrixFilter = new ColorMatrixColorFilter(
                        colorMatrix);
                paint.setColorFilter(colorMatrixFilter);
                canvas.drawBitmap(bitmap, 0, 0, paint);
                return faceIconGreyBitmap;
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 图片缩放
     *
     * @param bitmap
     * @param dst_w
     * @param dst_h
     * @return
     */
    @Nullable
    @CheckResult
    public static Bitmap scale(@Nullable Bitmap bitmap, int dst_w, int dst_h) {
        try {
            int src_w = bitmap.getWidth();
            int src_h = bitmap.getHeight();
            float scale_w = ((float) dst_w) / src_w;
            float scale_h = ((float) dst_h) / src_h;
            Matrix matrix = new Matrix();
            matrix.postScale(scale_w, scale_h);
            Bitmap dstbmp = Bitmap.createBitmap(bitmap, 0, 0, src_w, src_h, matrix,
                    true);
            return dstbmp;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Drawable转换成一个Bitmap
     *
     * @param drawable drawable对象
     * @return
     */
    @Nullable
    @CheckResult
    public static final Bitmap drawableToBitmap(Drawable drawable) {
        try {
            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                    drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 着色
     *
     * @param drawable
     * @param tintColor
     * @return
     */
    @Nullable
    @CheckResult
    public static Drawable tint(Drawable drawable, int tintColor) {
        try {
            Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(wrappedDrawable, tintColor);
            return wrappedDrawable;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 着色
     *
     * @param bitmap
     * @param tintColor
     * @return
     */
    @Nullable
    @CheckResult
    public static Bitmap tint(Bitmap bitmap, int tintColor) {
        if (bitmap == null) {
            return null;
        }
        try {
            Bitmap outputBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
            Canvas canvas = new Canvas(outputBitmap);
            Paint paint = new Paint();
            paint.setColorFilter(new PorterDuffColorFilter(tintColor, PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, 0, 0, paint);
            return outputBitmap;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 转换byte
     *
     * @param bitmap
     * @return
     */
    @Nullable
    @CheckResult
    public static byte[] toByte(@Nullable Bitmap bitmap) {
        try {
            int bytes = bitmap.getByteCount();
            ByteBuffer buffer = ByteBuffer.allocate(bytes);
            bitmap.copyPixelsToBuffer(buffer); //Move the byte data to the buffer
            byte[] data = buffer.array(); //Get the bytes array of the bitma
            return data;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 释放bitmap
     *
     * @param bitmap
     */
    public static void recycle(@Nullable Bitmap bitmap) {
        try {
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建bitmap
     * 适合未显示在UI界面上了的View
     *
     * @param view
     * @param width
     * @param height
     * @return
     */
    @Nullable
    @CheckResult
    public static Bitmap createBitmap(View view, int width, int height) {
        try {
            int measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
            int measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
            view.measure(measuredWidth, measuredHeight);
            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
            Bitmap bmp = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(bmp);
            view.draw(c);
            return bmp;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 创建bitmap
     * 适合已经显示在UI界面上了的View
     * <p>
     * 支持 显示在界面上的任意非滚动布局
     * <p>
     * 支持滚动布局支持如下:
     * RecyclerView
     * WebView
     * NestedScrollView
     * ScrollView
     * <p>
     * 注意:recyclerView 截取是在缓存中的item拼接的图片
     *
     * @param v
     * @return
     */
    @Nullable
    @CheckResult
    public static Bitmap createBitmap(View v) {
        if (v instanceof WebView) {
            return createBitmap((WebView) v);
        } else if (v instanceof RecyclerView) {
            return RecyclerViewUtils.shotRecyclerViewVisibleItems((RecyclerView) v);
        } else if (v instanceof NestedScrollView) {
            return createBitmap((NestedScrollView) v);
        } else if (v instanceof ScrollView) {
            return createBitmap((ScrollView) v);
        }
        try {
            Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
                    Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            v.draw(canvas);
            return bitmap;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 截取WebView的图片
     *
     * @param webView
     * @return
     */
    @Nullable
    @CheckResult
    public static Bitmap createBitmap(WebView webView) {
        try {
            //指定测量规则
            webView.measure(0, 0);
            //获取webView宽高
            int width = webView.getMeasuredWidth();
            int height = webView.getMeasuredHeight();
            //设置缓存机制开启
            webView.setDrawingCacheEnabled(true);
            //创建缓存
            webView.buildDrawingCache();
            //根据webView宽高创建bitmap
            Bitmap bm = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
            //创建画布
            Canvas bigCanvas = new Canvas(bm);
            //绘制内容
            webView.draw(bigCanvas);
            return bm;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 截取ScrollView 截图
     *
     * @param scrollView
     * @return
     */
    @Nullable
    @CheckResult
    public static Bitmap createBitmap(ScrollView scrollView) {
        try {
            int h = 0;
            Bitmap bitmap = null;
            /**
             * 一般来说只有一个child
             */
            for (int i = 0; i < scrollView.getChildCount(); i++) {
                View childAt = scrollView.getChildAt(i);
                h += childAt.getHeight();
                if (childAt.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) childAt.getLayoutParams();
                    h += marginLayoutParams.topMargin + marginLayoutParams.bottomMargin;
                }
            }
            h += scrollView.getPaddingTop() + scrollView.getPaddingBottom();
            bitmap = Bitmap.createBitmap(
                    scrollView.getWidth(), h,
                    Bitmap.Config.RGB_565
            );
            Canvas canvas = new Canvas(bitmap);//把创建的bitmap放到画布中去
            scrollView.draw(canvas);//绘制canvas 画布
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 截取NestedScrollView 截图
     *
     * @param scrollView
     * @return
     */
    @Nullable
    @CheckResult
    public static Bitmap createBitmap(NestedScrollView scrollView) {
        try {
            int h = 0;
            Bitmap bitmap = null;
            /**
             * 一般来说只有一个child
             */
            for (int i = 0; i < scrollView.getChildCount(); i++) {
                View childAt = scrollView.getChildAt(i);
                h += childAt.getHeight();
                if (childAt.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) childAt.getLayoutParams();
                    h += marginLayoutParams.topMargin + marginLayoutParams.bottomMargin;
                }
            }
            h += scrollView.getPaddingTop() + scrollView.getPaddingBottom();
            bitmap = Bitmap.createBitmap(
                    scrollView.getWidth(), h,
                    Bitmap.Config.RGB_565
            );
            Canvas canvas = new Canvas(bitmap);//把创建的bitmap放到画布中去
            scrollView.draw(canvas);//绘制canvas 画布
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
    @Nullable
    public static Bitmap mergeBitmapVertical(Bitmap topBitmap, Bitmap bottomBitmap, boolean isBaseMax) {
        try {
            if (topBitmap == null || topBitmap.isRecycled()
                    || bottomBitmap == null || bottomBitmap.isRecycled()) {
                XXF.getLogger().d("merge" + "topBitmap=" + topBitmap + ";bottomBitmap=" + bottomBitmap);
                return null;
            }
            int width = 0;
            if (isBaseMax) {
                width = topBitmap.getWidth() > bottomBitmap.getWidth() ? topBitmap.getWidth() : bottomBitmap.getWidth();
            } else {
                width = topBitmap.getWidth() < bottomBitmap.getWidth() ? topBitmap.getWidth() : bottomBitmap.getWidth();
            }
            Bitmap tempBitmapT = topBitmap;
            Bitmap tempBitmapB = bottomBitmap;

            if (topBitmap.getWidth() != width) {
                tempBitmapT = Bitmap.createScaledBitmap(topBitmap, width, (int) (topBitmap.getHeight() * 1f / topBitmap.getWidth() * width), false);
            } else if (bottomBitmap.getWidth() != width) {
                tempBitmapB = Bitmap.createScaledBitmap(bottomBitmap, width, (int) (bottomBitmap.getHeight() * 1f / bottomBitmap.getWidth() * width), false);
            }

            int height = tempBitmapT.getHeight() + tempBitmapB.getHeight();

            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);

            Rect topRect = new Rect(0, 0, tempBitmapT.getWidth(), tempBitmapT.getHeight());
            Rect bottomRect = new Rect(0, 0, tempBitmapB.getWidth(), tempBitmapB.getHeight());

            Rect bottomRectT = new Rect(0, tempBitmapT.getHeight(), width, height);

            canvas.drawBitmap(tempBitmapT, topRect, topRect, null);
            canvas.drawBitmap(tempBitmapB, bottomRect, bottomRectT, null);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 渲染背景
     *
     * @param backgroundColor
     * @param orginBitmap
     * @return
     */
    @CheckResult
    @Nullable
    public static Bitmap drawBitmapBackground(Bitmap orginBitmap, int backgroundColor) {
        try {
            if (orginBitmap == null) {
                return null;
            }
            Paint paint = new Paint();
            paint.setColor(backgroundColor);
            Bitmap bitmap = Bitmap.createBitmap(orginBitmap.getWidth(),
                    orginBitmap.getHeight(), orginBitmap.getConfig());
            Canvas canvas = new Canvas(bitmap);
            canvas.drawRect(0, 0, orginBitmap.getWidth(), orginBitmap.getHeight(), paint);
            canvas.drawBitmap(orginBitmap, 0, 0, paint);
            return bitmap;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }
}
