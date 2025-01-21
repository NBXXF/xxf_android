package com.xxf.mlkit.model

import android.graphics.Rect

open class FaceAnalyzerResult(val boundingBox: Rect, val faceBitmap: ByteArray)