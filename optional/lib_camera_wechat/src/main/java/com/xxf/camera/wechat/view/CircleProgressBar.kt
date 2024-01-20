@file:Suppress("RemoveEmptyClassBody")

package com.xxf.camera.wechat.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.Cap
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.xxf.camera.wechat.R
import kotlin.math.abs


/**
 * @Author mac
 * @Date 5/7/21-2:19 PM
 * @Description chrisSpringSmell@gmail.com
 */
class CircleProgressBar : View {
    // 画圆环的画笔
    private lateinit var ringPaint: Paint

    // 画字体的画笔
    private lateinit var textPaint: Paint

    // 圆环颜色
    private var ringColor = 0

    // 字体颜色
    private var textColor = 0

    // 半径
    private var radius = 0f

    // 圆环宽度
    private var strokeWidth = 0f

    // 字的长度
    private var txtWidth = 0f

    // 字的高度
    private var txtHeight = 0f

    // 总进度
    private val totalProgress = 100

    // 当前进度
    private var currentProgress = 0

    constructor(context: Context) : this(context,null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs,0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        initAttrs(context,attrs)
        initVariable()
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        val typeArray = context.theme.obtainStyledAttributes(attrs, R.styleable.CircleProgressbar, 0, 0)
        radius = typeArray.getDimension(R.styleable.CircleProgressbar_radius, 80f)
        strokeWidth = typeArray.getDimension(R.styleable.CircleProgressbar_strokeWidth, 10f)
        ringColor = typeArray.getColor(R.styleable.CircleProgressbar_ringColor, 0xFF0000)
        textColor = typeArray.getColor(R.styleable.CircleProgressbar_textColor, 0xFFFFFF)
    }

    private fun initVariable() {
        ringPaint = Paint()
        ringPaint.isAntiAlias = true
        ringPaint.isDither = true
        ringPaint.color = ringColor
        ringPaint.style = Paint.Style.STROKE
        ringPaint.strokeCap = Cap.ROUND
        ringPaint.strokeWidth = strokeWidth
        textPaint = Paint()
        textPaint.isAntiAlias = true
        textPaint.style = Paint.Style.FILL
        textPaint.color = textColor
        textPaint.textSize = radius / 2
        val fm: Paint.FontMetrics = textPaint.fontMetrics
        txtHeight = fm.descent + abs(fm.ascent)
    }

    override fun onDraw(canvas: Canvas) {
        if (currentProgress >= 0) {
            ringPaint!!.alpha = (alpha + currentProgress.toFloat() / totalProgress * 230).toInt()
            val oval = RectF(width / 2 - radius, height / 2 - radius, width / 2 + radius, height / 2 + radius)
            canvas.drawArc(oval, 0f, 0f, false, ringPaint)
            canvas.drawArc(oval, -90f, currentProgress.toFloat() / totalProgress * 360, false, ringPaint)
            val txt = "$currentProgress%"
            txtWidth = textPaint!!.measureText(txt, 0, txt.length)
            canvas.drawText(txt, width / 2 - txtWidth / 2, height / 2 + txtHeight / 4, textPaint)
        }
    }

    fun setProgress(progress: Int) {
        currentProgress = progress
        postInvalidate()
    }
}