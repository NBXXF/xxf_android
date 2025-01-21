package com.xxf.mlkit

import com.google.mlkit.vision.interfaces.Detector

/**
 * 检查是否存在结果或者执行异常
 */
fun <T> com.xxf.mlkit.MlKitFastAnalyzer.Result.containsDetector(detector: Detector<T>?): Boolean {
    return mValues.contains(detector) || mThrowables.contains(detector);
}