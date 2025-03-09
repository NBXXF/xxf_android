package com.xxf.mlkit.detector


import android.graphics.Bitmap
import android.graphics.Rect
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.common.Barcode
import com.xxf.ktx.dp
import com.xxf.mlkit.model.BarcodeAnalyzerResult
import com.xxf.mlkit.model.filterAnalyzerResult
import com.xxf.mlkit.model.sortAnalyzerResult
import com.xxf.utils.BitmapUtils
import me.devilsen.czxing.code.BarcodeDecoder
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


    /**
     * 解析结果
     */
    private fun decodeBitmapResultList(toBitmap: Bitmap): List<BarcodeAnalyzerResult> {
        try {
            return barcodeDecoder.decodeBitmap(toBitmap).map {
                BarcodeAnalyzerResult(
                    Rect(it.points[0], it.points[1], it.points[2], it.points[3]), it.text
                )
            }
        } catch (e: Throwable) {
            e.printStackTrace()
            return emptyList();
        } finally {
            BitmapUtils.recycle(toBitmap);
        }
    }


    /**
     * 尝试图片清晰化再解析
     */
    private fun decodeBitmapResultListBySharpen(toBitmap: Bitmap): List<BarcodeAnalyzerResult> {
        try {
            val ameliorateBitmap = BitmapUtils.sharpenImageAmeliorate(
                toBitmap
            )
            return decodeBitmapResultList(ameliorateBitmap!!);
        } catch (e: Throwable) {
            e.printStackTrace()
            return emptyList();
        }
    }


    override fun wrapper(
        task: Task<List<Barcode>>, bitmapProxy: () -> Bitmap
    ): Task<List<BarcodeAnalyzerResult>> {
        return task.continueWith(executor) { it ->
            val rawResult: List<BarcodeAnalyzerResult> = convertAnalyzerResult(it.result.orEmpty());
            val handleResult =
                if (rawResult.isEmpty() || rawResult.filterAnalyzerResult().isEmpty()) {
                    /**
                     * 整体二次识别
                     */
                    var decodeBitmapResultList =
                        decodeBitmapResultList(bitmapProxy()).filterAnalyzerResult()
                            .sortAnalyzerResult()

                    /**
                     * 尝试图片清晰化再解析
                     */
                    if (decodeBitmapResultList.isEmpty()) {
                        val rawBitmap: Bitmap = bitmapProxy();
                        decodeBitmapResultList =
                            decodeBitmapResultListBySharpen(rawBitmap).filterAnalyzerResult()
                                .sortAnalyzerResult()
                    }
                    decodeBitmapResultList
                } else {
                    var rawBitmap: Bitmap? = null;

                    val decoderSecondTimeList: List<BarcodeAnalyzerResult> =
                        rawResult.map { resultItem ->
                            /**
                             * 局部二次识别
                             */
                            if (resultItem.displayValue.isEmpty()) {
                                if (rawBitmap == null || rawBitmap?.isRecycled == true) {
                                    rawBitmap = bitmapProxy();
                                }
                                try {
                                    val cropBitmap = BitmapUtils.crop(
                                        rawBitmap!!,
                                        resultItem.boundingBox,
                                        scanPadding
                                    );
                                    var decodeBitmapResultList =
                                        decodeBitmapResultList(cropBitmap).filterAnalyzerResult()
                                            .sortAnalyzerResult()
                                    /**
                                     * 尝试优化图片
                                     */
                                    if (decodeBitmapResultList.isEmpty()) {
                                        val cropBitmap = BitmapUtils.crop(
                                            rawBitmap!!,
                                            resultItem.boundingBox,
                                            scanPadding
                                        );
                                        decodeBitmapResultList =
                                            decodeBitmapResultListBySharpen(cropBitmap).filterAnalyzerResult()
                                                .sortAnalyzerResult()
                                    }
                                    /**
                                     * 取第一张
                                     */
                                    BarcodeAnalyzerResult(
                                        resultItem.boundingBox,
                                        decodeBitmapResultList.firstOrNull()?.displayValue.orEmpty()
                                    )
                                } catch (e: Throwable) {
                                    e.printStackTrace()
                                    resultItem;
                                }
                            } else {
                                resultItem
                            }
                        }
                    BitmapUtils.recycle(rawBitmap);
                    /**
                     *  下游不要接收到 displayValue为空的情况
                     */
                    decoderSecondTimeList.filterAnalyzerResult().sortAnalyzerResult()
                }

            return@continueWith handleResult;
        }
    }

}