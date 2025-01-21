package com.xxf.mlkit

import com.google.mlkit.vision.interfaces.Detector

/**
 * 检查是否存在结果或者执行异常
 */
fun <T> com.xxf.mlkit.MlKitFastAnalyzer.Result.containsDetector(detector: Detector<T>?): Boolean {
    return mValues.contains(detector) || mThrowables.contains(detector);
}

/**
 * 获取识别结果
 * 相比于[com.xxf.mlkit.MlKitFastAnalyzer.Result.getValue] 没有检查
 */
@Suppress("UNCHECKED_CAST")
fun <T> com.xxf.mlkit.MlKitFastAnalyzer.Result.getValueNullable(detector: Detector<T>?): T? {
    return mValues[detector] as T?
}

/**
 * 获取异常结果
 * 相比于[com.xxf.mlkit.MlKitFastAnalyzer.Result.getThrowable] 没有检查
 */
@Suppress("UNCHECKED_CAST")
fun <T> com.xxf.mlkit.MlKitFastAnalyzer.Result.getThrowableNullable(detector: Detector<T>?): Throwable? {
    return mThrowables[detector]
}