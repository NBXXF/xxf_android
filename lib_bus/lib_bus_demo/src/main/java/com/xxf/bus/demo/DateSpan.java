package com.xxf.bus.demo;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.text.style.ReplacementSpan;
import android.util.Log;


/**
 * @author liuboyu  E-mail:545777678@qq.com
 * @Date 2021/6/5
 * @Description 日期span
 */
public class DateSpan extends ReplacementSpan {
    private String mText;

    private int mTextColor = Color.RED;

    public DateSpan(String text) {
        if (text.equals("@")) {
            this.mText = "@指定日期";
        } else {
            this.mText = text;
        }
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        return (int) paint.measureText(mText);
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        paint.setColor(mTextColor);
        canvas.drawText(mText, x, y, paint);
        Log.d("=========>draw", "x" + x + "y:" + y);
    }
}
