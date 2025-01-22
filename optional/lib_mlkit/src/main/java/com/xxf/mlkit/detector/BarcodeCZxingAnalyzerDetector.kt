package com.xxf.mlkit.detector


import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.Rect
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
import com.xxf.utils.BitmapUtils
import me.devilsen.czxing.code.BarcodeDecoder
import java.nio.ByteBuffer
import java.util.concurrent.Executor


/**
 * @ProjectName: android
 * @ClassName: ScanDetector
 * @Description: 二维码识别
 * @Author: xuanyouwu@163.com 17611639080
 * @Date: 2023/8/25 09:58
 */
open class BarcodeCZxingAnalyzerDetector(
    open val barcodeDecoder: BarcodeDecoder,
    barcodeScanner: BarcodeScanner,
    executor: Executor,
    scanPadding: Int = 40.dp
) : BarcodeAnalyzerDetector(barcodeScanner, executor, scanPadding) {

    override fun close() {
        super.close()
        barcodeDecoder.destroy()
    }

    override fun wrapper(
        task: Task<List<Barcode>>,
        bitmapProxy: () -> Bitmap
    ): Task<List<BarcodeAnalyzerResult>> {
        return task.continueWith(executor) { it ->
            try {
                if (it.result.isEmpty()) {
                    val toBitmap = bitmapProxy();
                    return@continueWith barcodeDecoder.decodeBitmap(toBitmap).map {
                        BarcodeAnalyzerResult(
                            Rect(it.points[0], it.points[1], it.points[2], it.points[3]),
                            it.text
                        )
                    }
                }
            } catch (e: Throwable) {
                e.printStackTrace()
            }
            val map = it.result.orEmpty().map {
                if (it.displayValue.isNullOrEmpty()) {
                    val toBitmap = bitmapProxy();
                    val cropBitmap =
                        BitmapUtils.sharpenImageAmeliorate(
                            BitmapUtils.crop(
                                toBitmap,
                                requireNotNull(it.boundingBox),
                                scanPadding
                            )
                        )
                    try {
                        val decodeBitmap = barcodeDecoder.decodeBitmap(cropBitmap)
                        BarcodeAnalyzerResult(
                            it.boundingBox!!,
                            decodeBitmap.firstOrNull()?.text.orEmpty()
                        )
                    } catch (e: Throwable) {
                        e.printStackTrace()
                        BarcodeAnalyzerResult(it.boundingBox!!, it.displayValue.orEmpty())
                    } finally {
                        BitmapUtils.recycle(toBitmap)
                        BitmapUtils.recycle(cropBitmap)
                    }
                } else {
                    BarcodeAnalyzerResult(it.boundingBox!!, it.displayValue.orEmpty())
                }
            }
            return@continueWith map
        }
    }

}