package com.xxf.mlkit.camera

import android.annotation.SuppressLint
import android.util.Log
import android.util.Size
import androidx.camera.core.Camera
import androidx.camera.core.CameraInfo
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.core.resolutionselector.ResolutionSelector.PREFER_HIGHER_RESOLUTION_OVER_CAPTURE_RATE
import androidx.camera.core.resolutionselector.ResolutionStrategy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import com.google.common.util.concurrent.ListenableFuture
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
open class CameraAnalyzerManager(
    open val lifecycleOwner: LifecycleOwner,
    open val previewView: PreviewView,
    open val analyzer: ImageAnalysis.Analyzer,
    open val executor: Executor,
    open val resolutionStrategy: ResolutionStrategy = ResolutionStrategy(
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
    private var processCameraProvider: ProcessCameraProvider? = null
    private val processCameraProviderFuture by lazy {
        onCreateProcessCameraProviderFuture()
    }

    ///提供复写模式,低版本依赖,打包有问题
    protected open fun onCreateProcessCameraProviderFuture(): ListenableFuture<ProcessCameraProvider> {
        return ProcessCameraProvider.getInstance(previewView.context)
    }

    init {
        this.lifecycleOwner.doOnDestroy {
            close()
        }
    }

    fun startCamera() {
        if (processCameraProvider != null) {
            setCameraConfig(processCameraProvider!!)
        } else {
            processCameraProviderFuture.addListener(this, executor)
        }
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun setCameraConfig(cameraProvider: ProcessCameraProvider) {
        try {
            val cameraSelector = getOptimalCameraSelector(cameraProvider);
            setCameraConfig(cameraProvider, cameraSelector)
        } catch (e: Throwable) {
            onCameraConfigError(e);
        }
    }

    open fun onCameraConfigError(error: Throwable) {
        Log.e(TAG, "==========>onCameraConfigError", error)
    }

    /**
     * 获取最优的Camera相机
     */
    @SuppressLint("UnsafeOptInUsageError", "RestrictedApi")
    private fun getOptimalCameraSelector(cameraProvider: ProcessCameraProvider): CameraSelector {
        Log.i(
            TAG, "==========>availableCameras:${
                cameraProvider.availableCameraInfos.joinToString(separator = System.lineSeparator()) {
                    "it.lensFacing:${it.lensFacing}  ${it.implementationType}"
                }
            }"
        )
        val groupedByFacing = cameraProvider.availableCameraInfos
            .filter { it.lensFacing != null }
            .groupBy { it.lensFacing }

        // 按优先级：前置 > 外接 > 后置
        val preferredCameraInfo = when {
            groupedByFacing.containsKey(CameraSelector.LENS_FACING_FRONT) ->
                groupedByFacing[CameraSelector.LENS_FACING_FRONT]!!.first()

            groupedByFacing.containsKey(CameraSelector.LENS_FACING_EXTERNAL) ->
                groupedByFacing[CameraSelector.LENS_FACING_EXTERNAL]!!.first()

            groupedByFacing.containsKey(CameraSelector.LENS_FACING_BACK) ->
                groupedByFacing[CameraSelector.LENS_FACING_BACK]!!.first()

            else ->
                cameraProvider.availableCameraInfos.firstOrNull()
                    ?: throw IllegalStateException("No cameras available on the device.")
        }

        val lensFacing = preferredCameraInfo.lensFacing
            ?: throw IllegalStateException("Selected camera has null lensFacing")

        return CameraSelector.Builder()
            .requireLensFacing(lensFacing)
            .build()
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
                preview.surfaceProvider = previewView.surfaceProvider
            } catch (e: Exception) {
                onCameraConfigError(e);
                Log.e(TAG, "==========>Use case binding failed", e)
            }
        }
    }

    fun changeCameraSelector(graphicOverlay: GraphicOverlay) {
        processCameraProvider?.unbindAll()
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
                processCameraProvider?.unbindAll()
            }
        }
        kotlin.runCatching {
            imageAnalyzer.clearAnalyzer()
        }
    }

    override fun run() {
        processCameraProvider = processCameraProviderFuture.get()
        processCameraProvider?.let {
            setCameraConfig(it)
        }
    }

}