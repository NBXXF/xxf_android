package com.xxf.mlkit.detector


import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.Image
import com.google.android.gms.common.Feature
import com.google.android.gms.common.api.OptionalModuleApi
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.interfaces.Detector
import com.xxf.ktx.dp
import com.xxf.ktx.toByteArray
import com.xxf.mlkit.imageproxy.AndroidImageProxy
import com.xxf.mlkit.imageproxy.ByteBufferImageProxy
import com.xxf.mlkit.model.FaceAnalyzerResult
import com.xxf.utils.BitmapUtils
import java.nio.ByteBuffer
import java.util.concurrent.Executor


/**
 * @ProjectName: android
 * @ClassName: FaceAnalyzerDetector
 * @Description: 人臉识别
 * @Author: xuanyouwu@163.com 17611639080
 * @Date: 2023/8/25 09:58
 */
open class FaceAnalyzerDetector(
    open val barcodeScanner: FaceDetector,
    open val executor: Executor,
    open val scanPadding: Int = 40.dp
) :
    Detector<List<FaceAnalyzerResult>>,
    OptionalModuleApi {
    override fun close() {
        barcodeScanner.close()
    }

    override fun getDetectorType(): Int {
        return barcodeScanner.detectorType
    }

    override fun process(image: Bitmap, rotation: Int): Task<List<FaceAnalyzerResult>> {
        return wrapper(barcodeScanner.process(image, rotation)) {
            val matrix = Matrix()
            matrix.postRotate(rotation.toFloat())
            Bitmap.createBitmap(
                image, 0, 0, image.getWidth(),
                image.getHeight(), matrix, false
            )
        };
    }

    override fun process(image: Image, rotation: Int): Task<List<FaceAnalyzerResult>> {
        return wrapper(barcodeScanner.process(image, rotation)) {
            AndroidImageProxy(image, rotation).toBitmap()
        };
    }

    override fun process(
        image: Image,
        rotation: Int,
        matrix: Matrix
    ): Task<List<FaceAnalyzerResult>> {
        return wrapper(barcodeScanner.process(image, rotation, matrix)) {
            AndroidImageProxy(image, rotation).toBitmap()
        };
    }

    override fun process(
        byte: ByteBuffer,
        rotation: Int,
        width: Int,
        height: Int,
        format: Int
    ): Task<List<FaceAnalyzerResult>> {
        return wrapper(barcodeScanner.process(byte, rotation, width, height, format)) {
            ByteBufferImageProxy(byte, rotation, width, height, format).toBitmap()
        }
    }

    override fun getOptionalFeatures(): Array<Feature> {
        return barcodeScanner.optionalFeatures;
    }


    protected open fun wrapper(
        task: Task<List<Face>>,
        bitmapProxy: () -> Bitmap
    ): Task<List<FaceAnalyzerResult>> {
        return task.continueWith(executor) { it ->
            val result = if (it.result.isNotEmpty()) {
                val toBitmap = bitmapProxy();
                val cropBitmap = BitmapUtils.cropCompose(
                    toBitmap,
                    it.result.map { face ->
                        face.boundingBox
                    },
                    scanPadding
                )
                val faceBitmap: ByteArray =
                    cropBitmap.toByteArray()!!
                BitmapUtils.recycle(toBitmap)
                BitmapUtils.recycle(cropBitmap)
                it.result.map {
                    FaceAnalyzerResult(it.boundingBox, faceBitmap)
                }
            } else {
                emptyList()
            }
            return@continueWith result;
        }
    }


}