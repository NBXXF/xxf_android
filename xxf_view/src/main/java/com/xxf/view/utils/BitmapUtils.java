package com.xxf.view.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;

import androidx.annotation.CheckResult;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.DrawableCompat;

import java.nio.ByteBuffer;

/**
 * @Description: bitmap处理工具类
 * @Author: XGod
 * @CreateDate: 2020/10/13 11:14
 */
public class BitmapUtils {


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
}
