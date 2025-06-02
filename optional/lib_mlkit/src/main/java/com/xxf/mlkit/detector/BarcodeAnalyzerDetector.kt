package com.xxf.mlkit.detector


import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.Image
import com.google.android.gms.common.Feature
import com.google.android.gms.common.api.OptionalModuleApi
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.interfaces.Detector
import com.xxf.ktx.dp
import com.xxf.mlkit.imageproxy.AndroidImageProxy
import com.xxf.mlkit.imageproxy.ByteBufferImageProxy
import com.xxf.mlkit.model.BarcodeAnalyzerResult
import com.xxf.mlkit.model.filterAnalyzerResult
import com.xxf.mlkit.model.sortAnalyzerResult
import com.xxf.utils.BitmapUtils
import java.nio.ByteBuffer
import java.util.concurrent.Executor
import java.util.concurrent.RejectedExecutionException


/**
 * @ProjectName: android
 * @ClassName: BarcodeAnalyzerDetector
 * @Description: 二维码识别
 * @Author: xuanyouwu@163.com 17611639080
 * @Date: 2023/8/25 09:58
 */
open class BarcodeAnalyzerDetector(
    open val barcodeScanner: BarcodeScanner,
    open val executor: Executor,
    open val scanPadding: Int = 40.dp
) : Detector<List<BarcodeAnalyzerResult>>, OptionalModuleApi {
    override fun close() {
        barcodeScanner.close()
    }

    override fun getDetectorType(): Int {
        return barcodeScanner.detectorType
    }

    override fun process(image: Bitmap, rotation: Int): Task<List<BarcodeAnalyzerResult>> {
        return wrapper(barcodeScanner.process(image, rotation)) {
            val matrix = Matrix()
            matrix.postRotate(rotation.toFloat())
            Bitmap.createBitmap(
                image, 0, 0, image.getWidth(), image.getHeight(), matrix, false
            )
        };
    }

    override fun process(image: Image, rotation: Int): Task<List<BarcodeAnalyzerResult>> {
        return wrapper(barcodeScanner.process(image, rotation)) {
            BitmapUtils.rotateBitmap(
                AndroidImageProxy(image, rotation).toBitmap(),
                rotation,
                flipX = false,
                flipY = false
            )
        };
    }

    override fun process(
        image: Image, rotation: Int, matrix: Matrix
    ): Task<List<BarcodeAnalyzerResult>> {
        return wrapper(barcodeScanner.process(image, rotation, matrix)) {
            BitmapUtils.rotateBitmap(
                AndroidImageProxy(image, rotation).toBitmap(),
                rotation,
                flipX = false,
                flipY = false
            )
        };
    }

    override fun process(
        byte: ByteBuffer, rotation: Int, width: Int, height: Int, format: Int
    ): Task<List<BarcodeAnalyzerResult>> {
        return wrapper(barcodeScanner.process(byte, rotation, width, height, format)) {
            BitmapUtils.rotateBitmap(
                ByteBufferImageProxy(
                    byte, rotation, width, height, format
                ).toBitmap(), rotation, flipX = false, flipY = false
            )
        }
    }

    override fun getOptionalFeatures(): Array<Feature> {
        return barcodeScanner.optionalFeatures;
    }

    /**
     * 只是转换
     */
    protected fun convertAnalyzerResult(result: List<Barcode>): List<BarcodeAnalyzerResult> {
        return result.map {
            BarcodeAnalyzerResult(it.boundingBox!!, it.displayValue.orEmpty())
        }
    }

    protected open fun wrapper(
        task: Task<List<Barcode>>, bitmapProxy: () -> Bitmap
    ): Task<List<BarcodeAnalyzerResult>> {
        try {
            return task.continueWith(executor) { it ->

                return@continueWith barcodeAnalyzerResults(it)
            }
        } catch (e: RejectedExecutionException) {
            return task.continueWith { it ->
                return@continueWith barcodeAnalyzerResults(it)
            }
        }
    }

    /**
     *  下游不要接收到 displayValue为空的情况
     *  且按面积排序
     */
    private fun barcodeAnalyzerResults(it: Task<List<Barcode>>) =
        convertAnalyzerResult(it.result).filterAnalyzerResult().sortAnalyzerResult()


}