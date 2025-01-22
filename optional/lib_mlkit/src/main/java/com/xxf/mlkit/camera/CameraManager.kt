package com.xxf.mlkit.camera

import android.util.Log
import android.util.Size
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.core.resolutionselector.ResolutionSelector.PREFER_HIGHER_RESOLUTION_OVER_CAPTURE_RATE
import androidx.camera.core.resolutionselector.ResolutionStrategy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import com.google.mlkit.vision.barcode.ZoomSuggestionOptions
import com.xxf.ktx.doOnDestroy
import com.xxf.ktx.runOnUiThread
import com.xxf.ktx.tryOrLog
import com.xxf.mlkit.overlay.GraphicOverlay
import java.io.Closeable
import java.util.concurrent.Executor

/**
 * @ClassName: CameraManager
 * @Description:
 * @Author: xuanyouwu@163.com 17611639080
 * @Date: 2023/8/24 13:42
 */
class CameraManager(
    private val lifecycleOwner: LifecycleOwner,
    private val previewView: PreviewView,
    private val analyzer: ImageAnalysis.Analyzer,
    private val executor: Executor,
    private val resolutionStrategy: ResolutionStrategy = ResolutionStrategy(
        Size(1920, 1080),
        ResolutionStrategy.FALLBACK_RULE_CLOSEST_LOWER_THEN_HIGHER
    )
) : ZoomSuggestionOptions.ZoomCallback, Closeable, Runnable {
    companion object {
        private const val TAG = "CameraXBasic"
    }

    private val imageAnalyzer: ImageAnalysis by lazy {
        ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .setResolutionSelector(
                ResolutionSelector.Builder()
                    .setResolutionStrategy(
                        // ResolutionStrategy.HIGHEST_AVAILABLE_STRATEGY
                        resolutionStrategy
                    )
                    .setAllowedResolutionMode(PREFER_HIGHER_RESOLUTION_OVER_CAPTURE_RATE)
                    .build()
            )
            .build()
            .apply {
                setAnalyzer(executor, analyzer)
            }
    }
    private val preview: Preview by lazy {
        Preview.Builder()
            .setResolutionSelector(
                ResolutionSelector.Builder()
                    .setResolutionStrategy(
                        //  ResolutionStrategy.HIGHEST_AVAILABLE_STRATEGY,
                        resolutionStrategy
                    )
                    .setAllowedResolutionMode(PREFER_HIGHER_RESOLUTION_OVER_CAPTURE_RATE)
                    .build()
            )
            .build()
    }
    private var camera: Camera? = null
    private var cameraSelectorOption = CameraSelector.LENS_FACING_FRONT
    private var cameraProvider: ProcessCameraProvider? = null
    private val processCameraProvider by lazy {
        ProcessCameraProvider.getInstance(previewView.context)
    }

    init {
        this.lifecycleOwner.doOnDestroy {
            close()
        }
    }

    fun startCamera() {
        processCameraProvider.addListener(this, executor)
    }

    private fun setCameraConfig(
        cameraProvider: ProcessCameraProvider?,
        cameraSelector: CameraSelector
    ) {
        previewView.runOnUiThread {
            try {
                cameraProvider?.unbindAll()
                camera = cameraProvider?.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageAnalyzer
                )
                preview.setSurfaceProvider(previewView.surfaceProvider)
            } catch (e: Exception) {
                Log.e(TAG, "Use case binding failed", e)
            }
        }
    }

    fun changeCameraSelector(graphicOverlay: GraphicOverlay) {
        cameraProvider?.unbindAll()
        cameraSelectorOption =
            if (cameraSelectorOption == CameraSelector.LENS_FACING_BACK) CameraSelector.LENS_FACING_FRONT
            else CameraSelector.LENS_FACING_BACK
        graphicOverlay.toggleSelector()
        startCamera()
    }


    override fun setZoom(zoomRatio: Float): Boolean {
        tryOrLog {
            camera?.cameraControl?.setZoomRatio(zoomRatio)
        }
        return true
    }

    override fun close() {
        previewView.runOnUiThread {
            kotlin.runCatching {
                cameraProvider?.unbindAll()
            }
        }
        kotlin.runCatching {
            imageAnalyzer.clearAnalyzer()
        }
    }

    override fun run() {
        cameraProvider = processCameraProvider.get()
        val cameraSelector = CameraSelector.Builder()
            .apply {
                val isFront: Boolean =
                    cameraProvider?.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA)?:false //检测默认前置摄像头
                //有些定制工业平板有问题
                if (isFront) {
                    this.requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                } else {
                    this.requireLensFacing(CameraSelector.LENS_FACING_BACK)
                }
            }
            .build()

        setCameraConfig(cameraProvider, cameraSelector)
    }

}