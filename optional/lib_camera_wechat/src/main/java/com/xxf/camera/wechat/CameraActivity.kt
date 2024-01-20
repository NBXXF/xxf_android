package com.xxf.camera.wechat

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.hardware.camera2.*
import android.media.*
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.util.Size
import android.view.*
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import com.daasuu.mp4compose.Rotation
import com.daasuu.mp4compose.composer.Mp4Composer
import com.xxf.camera.wechat.config.CameraConfig
import com.xxf.camera.wechat.databinding.XxfActivityCameraWechatBinding
import com.xxf.camera.wechat.provider.CameraProvider
import com.xxf.camera.wechat.util.CameraUtil
import com.xxf.camera.wechat.util.ClickUtil
import com.xxf.camera.wechat.util.PermissionUtil
import com.xxf.camera.wechat.util.ScreenUtil
import com.xxf.camera.wechat.view.CircleProgressButton
import java.io.*
import java.lang.Long.signum
import java.util.*
import java.util.concurrent.Semaphore
import java.util.concurrent.TimeUnit
import android.media.MediaRecorder
import android.text.TextUtils
import java.lang.IllegalStateException


class CameraActivity : AppCompatActivity() {
    private lateinit var binding: XxfActivityCameraWechatBinding
    private var mWorkingSurface: Surface? = null

    /**
     * 预览组件监听
     */
    private val surfaceTextureListener = object : TextureView.SurfaceTextureListener {
        override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
            Log.e(TAG, "onSurfaceTextureAvailable: 初始化完毕")
            mWorkingSurface = Surface(surface)
            openCamera()
        }

        override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
        }

        override fun onSurfaceTextureUpdated(surface: SurfaceTexture) = Unit

        override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean = true
    }

    private val MAX_PREVIEW_WIDTH = lazy { (ScreenUtil.getScreenHeight(this)) }
    private val MAX_PREVIEW_HEIGHT = lazy { (ScreenUtil.getScreenWidth(this)) }
    private val MIN_RECORD_WIDHT = 960
    private val MIN_RECORD_HEIGHT = 1280

    private lateinit var cameraManager: CameraManager
    private var previewSession: CameraCaptureSession? = null
    private lateinit var previewRequest: CaptureRequest
    private lateinit var previewBuilder: CaptureRequest.Builder
    private var recordeStart:Boolean=false

    private val cameraStateCallback: CameraDevice.StateCallback = object : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice) {
            //得到相机 显示预览画面
            cameraOpenCloseLock.release()
            cameraDevice = camera
            state.set(STATE_PREVIEW)
            openCameraPreviewSession()
        }

        override fun onDisconnected(camera: CameraDevice) {
            cameraOpenCloseLock.release()
            camera.close()
            cameraDevice = null
        }

        override fun onError(camera: CameraDevice, error: Int) {
            onDisconnected(camera)
            Toast.makeText(this@CameraActivity, getString(R.string.open_camera_error_tip), Toast.LENGTH_SHORT).show()
            finish()
        }
    }
    private lateinit var cameraList: Array<String>
    private var backgroundThread: HandlerThread? = null
    private var backgroundHandler: Handler? = null
    private var cameraDevice: CameraDevice? = null

    /**
     * 传感器定位
     */
    private var sensorOrientation = 0
    private var recordOrientation = 0
    private lateinit var orientationEventListener: OrientationEventListener



    private var isPressRecord:Boolean=false


    //当前视图显示阶段
    var state: ObservableField<Int> = ObservableField(STATE_PREVIEW)
    var captureImage: Image? = null
    private lateinit var recordSize: Size
    private lateinit var previewSize: Size

    /**
     * Record
     */
    private var recordPath: String = ""
    private var comRecordPath: String = ""
    private var mediaRecorder: MediaRecorder? = null
    private lateinit var mediaPlayer: MediaPlayer
    private var mp4Composer: Mp4Composer? = null

    /**
     * Preview
     */
    private lateinit var previewSurface: Surface

    /**
     * Photo
     */
    private lateinit var previewImageReader: ImageReader
    private lateinit var previewImageReaderSurface: Surface


    //操作回调
    private var launchRunnable = Runnable {
        //如果还处于按下状态 表示要录像
        if (isPressRecord && CameraConfig.IS_ALLOW_RECORD) {
            //拍摄开始启动
            binding.mBtnRecord.start()
        }
    }
    private var startRecordRunnable = Runnable {
        if (isPressRecord && CameraConfig.IS_ALLOW_RECORD) {
            startRecord()
        }
    }

    /**
     * 操作信号量
     */
    private val cameraOpenCloseLock = Semaphore(1)
    private val sessionOpenCloseLock = Semaphore(1)

    /**
     * CAMERA 初始化
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 28) {
            //刘海屏 整死我了！！！！！！！！！！！！！
            val lp = window.attributes
            lp?.let {
                lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
                window.attributes = lp
            }
        }
        binding = DataBindingUtil.setContentView<XxfActivityCameraWechatBinding>(this, R.layout.xxf_activity_camera_wechat)
        binding.mBtnRecord.processSec = CameraConfig.MAX_RECORD_TIME
        binding.activity = this
        startBackgroundThread()
        init()
    }

    override fun onBackPressed() {
        when (state.get()) {
            STATE_RECORDING -> {
                //录像状态 禁止退出
            }
            STATE_RECORD_TAKEN -> {
                //播放状态 需要先删除文件后退出
                File(comRecordPath).delete()
                finish()
            }
            else -> {
                //其余状态 删除临时录像文件后退出
                File(recordPath).delete()
                finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        when (state.get()) {
            STATE_RECORD_TAKEN -> {
                //捕获录像状态 继续播放
                mediaPlayer.start()
            }
            STATE_PICTURE_TAKEN -> {
                //捕获照片状态 不做处理
            }
            else -> {
                //预览视图恢复默认
                if (binding.mTextureView.isAvailable) {
                    openCamera()
                } else {
                    binding.mTextureView.surfaceTextureListener = surfaceTextureListener
                }
            }
        }
    }

    override fun onPause() {
        when (state.get()) {
            STATE_RECORDING -> {
                //录像状态 停止录像
                binding.mBtnRecord.stop()
                stopRecord()
            }
            STATE_RECORD_TAKEN -> {
                //捕获录像状态 暂停播放
                mediaPlayer.pause()
            }
            STATE_PREVIEW, STATE_PICTURE_TAKEN -> {
                closeCamera()
            }
        }
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        orientationEventListener.disable()
        if (::previewSurface.isInitialized) {
            previewSurface.release()
        }
        mediaRecorder?.release()
        if (::mediaPlayer.isInitialized) {
            mediaPlayer.release()
        }
        stopBackgroundThread()
    }

    private fun startBackgroundThread() {
        if (backgroundThread == null) {
            backgroundThread = HandlerThread("CameraBackground").also { it.start() }
            backgroundHandler = Handler(backgroundThread!!.looper)
        }
    }

    private fun stopBackgroundThread() {
        backgroundThread?.quitSafely()
        try {
            backgroundThread?.join()
            backgroundThread = null
            backgroundHandler = null
        } catch (e: InterruptedException) {
            Log.e(TAG, e.toString())
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CameraConfig.CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty()) {
                val cameraResult = grantResults[0]//相机权限
                val sdResult = grantResults[1]//sd卡权限
                val audioResult = grantResults[2]//录音权限
                val cameraGranted = cameraResult == PackageManager.PERMISSION_GRANTED//拍照权限
                val sdGranted = sdResult == PackageManager.PERMISSION_GRANTED//sd卡权限
                val audioGranted = audioResult == PackageManager.PERMISSION_GRANTED//录音权限
                if (cameraGranted && sdGranted && audioGranted) {
                    //具有所需权限 继续打开摄像头
                    //openCamera()
                } else {
                    //没有权限
                    Toast.makeText(this, getString(R.string.no_permission_tip), Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    private fun init() {
        binding.mTvRecordTip.text = "${if (CameraConfig.IS_ALLOW_PHOTO) getString(R.string.photo_tip) else ""}${if (CameraConfig.IS_ALLOW_PHOTO && CameraConfig.IS_ALLOW_RECORD) "," else ""}${if (CameraConfig.IS_ALLOW_RECORD) getString(R.string.record_tip) else ""}"
        initCamera()
        initTouchListener()
    }

    /**
     * 初始化摄像头
     */
    private fun initCamera() {
        cameraManager = applicationContext.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        cameraList = cameraManager.cameraIdList
        if (!CameraConfig.init) {
            // 设置正反面摄像头ID
            cameraList.forEach { cId ->
                val cameraCharacteristics = cameraManager.getCameraCharacteristics(cId)
                if (cameraCharacteristics[CameraCharacteristics.LENS_FACING] == CameraCharacteristics.LENS_FACING_FRONT) {
                    CameraConfig.FRONT_CAMERA_ID = cId
                    CameraConfig.FRONT_CAMERA_CHARACTERISTIC = cameraCharacteristics
                } else if (cameraCharacteristics[CameraCharacteristics.LENS_FACING] == CameraCharacteristics.LENS_FACING_BACK) {
                    if (CameraConfig.BACK_CAMERA_ID.isEmpty()) {
                        CameraConfig.BACK_CAMERA_ID = cId
                        Log.e(TAG, "initCamera: ${CameraConfig.BACK_CAMERA_ID}")
                        CameraConfig.BACK_CAMERA_CHARACTERISTIC = cameraCharacteristics
                    }
                }
            }
            CameraConfig.init = true
        }

        //初始化传感器定位
        sensorOrientation = CameraConfig.getCurrentCameraCameraCharacteristics().get(CameraCharacteristics.SENSOR_ORIENTATION)!!
        orientationEventListener = object : OrientationEventListener(this) {
            override fun onOrientationChanged(orientation: Int) {
                sensorOrientation = when (orientation) {
                    in 45..134 -> {
                        90
                    }
                    in 135..224 -> {
                        180
                    }
                    in 225..314 -> {
                        270
                    }
                    else -> {
                        0
                    }
                }
            }
        }
        orientationEventListener.enable()
    }

    @SuppressLint("MissingPermission")
    private fun openCamera() {
        //权限界定
        if (!PermissionUtil.checkPermission(this)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO), CameraConfig.CAMERA_PERMISSION_CODE)
        } else {
            try {
                // 3秒内打开摄像头
                if (!cameraOpenCloseLock.tryAcquire(3000, TimeUnit.MILLISECONDS)) {
                    throw RuntimeException("Time out waiting to lock camera opening.")
                }
                cameraManager.openCamera(CameraConfig.last_camera_id, cameraStateCallback, backgroundHandler)
                Log.e(TAG, "openCamera: 打开相机")
            } catch (e: CameraAccessException) {
                Log.e(TAG, e.toString())
            } catch (e: InterruptedException) {
                throw RuntimeException("Interrupted while trying to lock camera opening.", e)
            }
        }
    }

    private fun closeCamera() {
        try {
            cameraOpenCloseLock.acquire()
            previewSession?.close()
            cameraDevice?.close()
            cameraDevice = null
            if (::previewImageReader.isInitialized) {
                previewImageReader.close()
            }
        } catch (e: Exception) {
            throw RuntimeException("Interrupted while trying to lock camera closing.", e)
        } finally {
            cameraOpenCloseLock.release()
        }
    }

    private fun initPreview() {
        val display = windowManager.defaultDisplay
        val point = Point()
        display.getSize(point)
        //设置预览尺寸
        if (!::previewSize.isInitialized) {
            var size = CameraUtil.getMinOptimalSize(CameraConfig.getCurrentCameraCameraCharacteristics(), SurfaceTexture::class.java,
                    point.x, point.y)
            if (size == null) {
                size = Size(MAX_PREVIEW_WIDTH.value, MAX_PREVIEW_HEIGHT.value)
            }
            previewSize = size
            Log.e(TAG, "initPreview: previewSize=${previewSize}")
        }
        //设置录像尺寸
        if (!::recordSize.isInitialized) {
            val mRecordSize = CameraUtil.getMinOptimalSize(CameraConfig.getCurrentCameraCameraCharacteristics(), SurfaceTexture::class.java,
                    point.x, point.y)
            if (mRecordSize == null) {
                recordSize = Size(MIN_RECORD_WIDHT, MIN_RECORD_HEIGHT)
            } else {
                recordSize = mRecordSize
            }
            Log.e(TAG, "initPreview: recordSize=${recordSize}")
        }
        //创建ImageReader接收拍照数据
        val imageFormat = ImageFormat.JPEG
        val streamConfigurationMap = CameraConfig.getCurrentCameraCameraCharacteristics()[CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP]
        if (streamConfigurationMap?.isOutputSupportedFor(imageFormat) == true) {
            // JPEG is supported
            previewImageReader = ImageReader.newInstance(previewSize.width, previewSize.height, imageFormat, 1)
            previewImageReader.setOnImageAvailableListener(OnJpegImageAvailableListener(), backgroundHandler)
            previewImageReaderSurface = previewImageReader.surface
        }


    }


    fun changeCamera() {
        if (!ClickUtil.isFastClick()) return
        closeCamera()
        when (CameraConfig.last_camera_id) {
            CameraConfig.FRONT_CAMERA_ID -> {
                CameraConfig.last_camera_id = CameraConfig.BACK_CAMERA_ID
            }
            CameraConfig.BACK_CAMERA_ID -> {
                CameraConfig.last_camera_id = CameraConfig.FRONT_CAMERA_ID
            }
        }
        openCamera()
    }

    private fun closePreviewSession() {
        previewSession?.let {
            it.close()
            try {
                it.abortCaptures()
            } catch (ignore: java.lang.Exception) {

            } finally {
                previewSession = null
            }
        }
    }

    /**
     * 开始输出预览信息
     */
    private fun openCameraPreviewSession() {
        //closePreviewSession()
        initPreview()
        //预览视图
        val surfaceTexture = binding.mTextureView.surfaceTexture
        surfaceTexture?.setDefaultBufferSize(previewSize.width, previewSize.height)

        previewBuilder = cameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
        val outputList = mutableListOf<Surface>()

        mWorkingSurface?.let {
            outputList.add(it)
            previewBuilder.addTarget(it)
        }
        // 照片
        if (::previewImageReaderSurface.isInitialized) {
            outputList.add(previewImageReaderSurface)
        }

        previewBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO)
        cameraDevice?.createCaptureSession(outputList, object : CameraCaptureSession.StateCallback() {

            override fun onConfigureFailed(session: CameraCaptureSession) {
                Log.e("onConfigureFailed", "onConfigureFailed: 相机打开失败${session.toString()}")
            }

            override fun onConfigured(session: CameraCaptureSession) {
                //相机未打开
                if (cameraDevice == null) {
                    return
                }
                previewSession = session
                Log.e(TAG, "onConfigured: 拍照")
                previewRequest = previewBuilder.build()
                previewSession?.setRepeatingRequest(previewBuilder.build(), null, backgroundHandler)

            }
        }, backgroundHandler)
    }

    private fun record() {
        closePreviewSession()
        val surfaceTexture = binding.mTextureView.surfaceTexture
        surfaceTexture?.setDefaultBufferSize(recordSize.width, recordSize.height)
        //重置录像
        if (setUpMediaRecorder()) {
            cameraDevice?.let {
                val previewBuilder = it.createCaptureRequest(CameraDevice.TEMPLATE_RECORD)

                val outputList = mutableListOf<Surface>()
                mediaRecorder?.surface?.let { surface ->
                    outputList.add(surface)
                    previewBuilder.addTarget(surface)
                }
                mWorkingSurface?.let { workingSurface ->
                    outputList.add(workingSurface)
                    previewBuilder.addTarget(workingSurface)
                }

                it.createCaptureSession(outputList, object : CameraCaptureSession.StateCallback() {

                    override fun onConfigureFailed(session: CameraCaptureSession) {
                        Log.e("onConfigureFailed", "onConfigureFailed: 相机打开失败${session}")
                    }

                    override fun onConfigured(session: CameraCaptureSession) {
                        //相机未打开

                        previewSession = session
                        previewBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO)
                        try {
                            session.setRepeatingRequest(previewBuilder.build(), null, backgroundHandler)
                        } catch (e: java.lang.Exception) {
                        }
                        try {
                            mediaRecorder?.start()
                            recordeStart=true
                            startTime = System.currentTimeMillis()
                            Log.e(TAG, "onConfigured: 开始录像")
                        } catch (ignore: java.lang.Exception) {
                            Log.e(TAG, "mMediaRecorder.start(): ", ignore)
                        }

                    }
                }, backgroundHandler)
            }
        }

    }

    /**
     * 解除锁定
     */
    fun unlockFocus() {
        //防抖
        if (!ClickUtil.isFastClick()) return
        try {
            // 视频需要删除暂存文件
            if (state.get() == STATE_RECORD_TAKEN) {
                //停止播放
                mediaPlayer.stop()
                mediaPlayer.reset()
                File(comRecordPath).delete()
                //预览视图恢复默认
                val matrix = Matrix()
                binding.mTextureView.getTransform(matrix)
                matrix.setScale(1f, 1f)
                matrix.postTranslate(0f, 0f)
                binding.mTextureView.setTransform(matrix)
                openCamera()
            } else if (state.get() == STATE_RECORD_PROCESS) {
                //还在处理视频 还未开始播放
                mp4Composer?.cancel()

                File(comRecordPath).delete()
                //预览视图恢复默认
                val matrix = Matrix()
                binding.mTextureView.getTransform(matrix)
                matrix.setScale(1f, 1f)
                matrix.postTranslate(0f, 0f)
                binding.mTextureView.setTransform(matrix)
                openCamera()
            } else {
                // Reset the auto-focus trigger
                if (cameraDevice == null) {
                    openCamera()
                } else {
                    previewBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER,
                            CameraMetadata.CONTROL_AF_TRIGGER_CANCEL)
                    state.set(STATE_PREVIEW)
                    previewSession?.setRepeatingRequest(previewRequest, null,
                            backgroundHandler)
                }
            }

        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }

    }

    /**
     * 捕获当前结果
     */
    fun captureResult() {
        //防抖
        if (!ClickUtil.isFastClick()) return
        when (state.get()) {
            STATE_PICTURE_TAKEN -> {
                //捕获图片 先删除录像临时文件
                File(recordPath).delete()
                captureImage?.let { mImage ->
                    val buffer = mImage.planes[0].buffer
                    val data = ByteArray(buffer.remaining())
                    buffer.get(data)
                    val timeStamp = getDate()
                    val filePath = "${CameraConfig.getSaveDir(this@CameraActivity)}/img_$timeStamp.jpg"
                    var isBackCamera = CameraConfig.last_camera_id == CameraConfig.BACK_CAMERA_ID

                    var fos: FileOutputStream? = null
                    var bitmap: Bitmap? = null
                    var matBitmap: Bitmap? = null
                    try {
                        fos = FileOutputStream(filePath)
                        fos!!.write(data, 0, data.size)
                        if (!isBackCamera) {
                            //前置摄像头 图片需要镜像
                            val matrix = Matrix()
                            matrix.setScale(-1f, 1f)
                            matrix.postRotate((sensorOrientation.toFloat() + 90) % 360)
                            fos.close()
                            bitmap = BitmapFactory.decodeFile(filePath)
                            matBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
                            fos = FileOutputStream(filePath)
                            matBitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos)
                        }
                        val intent = Intent()
                        intent.putExtra(CameraLauncher.CAPTURE_RESULT_IS_IMG, true)
                        intent.putExtra(CameraLauncher.CAPTURE_RESULT, filePath)
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    } finally {
                        mImage.close()
                        try {
                            bitmap?.recycle()
                            matBitmap?.recycle()
                            fos?.close()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }
            }
            STATE_RECORD_TAKEN -> {
                //捕获视频
                val intent = Intent()
                intent.putExtra(CameraLauncher.CAPTURE_RESULT_IS_IMG, false)
                intent.putExtra(CameraLauncher.CAPTURE_RESULT, if (!comRecordPath.isNullOrBlank()) comRecordPath else recordPath)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }

    }

    /**
     * ************************************************************* 界面控制
     */
    private fun showBtnLayout() {
        binding.mBtnCancel.startAnimation(leftAction)
        binding.mBtnOK.startAnimation(rightAction)
    }

    private fun initTouchListener() {
        //初始化按钮录像完毕回调
        binding.mBtnRecord.setOnFinishCallBack(object : CircleProgressButton.OnFinishCallback {
            override fun progressStart() {
                Log.e(TAG, "progressStart: 按钮按下")
                backgroundHandler?.post(startRecordRunnable)
            }

            override fun progressFinish() {
                //录像到最大时间 直接结束录像
                isPressRecord=false
                Log.e(TAG, "initTouchListener: stop1")
                unPressRecord()
            }
        })
        //初始化触摸事件
        binding.mBtnRecord.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    isPressRecord=true
                    Log.e(TAG, "initTouchListener: start2")
                    binding.mBtnRecord.postDelayed(launchRunnable, 500)
                }
                MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                    //录像到最大时间而提前结束也会触发， 防止重复调用
                    if (isPressRecord) {
                        isPressRecord= false
                        Log.e(TAG, "initTouchListener: stop2")
                        unPressRecord()
                    }
                }
                else -> {
                }
            }
            true
        }
    }

    private fun unPressRecord() {
        backgroundHandler?.removeCallbacks(launchRunnable)
        when (state.get()) {
            STATE_PREVIEW -> {
                if (CameraConfig.IS_ALLOW_PHOTO) {
                    //手指松开还未开始录像 进行拍照
                    takePhoto()
                }
            }
            STATE_RECORDING -> {
                //正在录像 停止录像
                binding.mBtnRecord.stop()
                stopRecord()
            }
        }
    }

    /**
     * ***********************************************************take photo
     */
    private fun takePhoto() {
        if (cameraDevice == null || !binding.mTextureView.isAvailable) {
            return
        }
        try {
            val captureBuilder = cameraDevice?.createCaptureRequest(
                    CameraDevice.TEMPLATE_STILL_CAPTURE)?.apply {
                if (::previewImageReaderSurface.isInitialized) {
                    addTarget(previewImageReaderSurface)
                }
                set(CaptureRequest.JPEG_ORIENTATION,
                        CameraUtil.getJpegOrientation(CameraConfig.getCurrentCameraCameraCharacteristics(), sensorOrientation))
                set(CaptureRequest.CONTROL_AF_MODE,
                        CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)
                //锁定焦点
                set(CaptureRequest.CONTROL_AF_TRIGGER,
                        CameraMetadata.CONTROL_AF_TRIGGER_START)
            }

            val captureCallback = object : CameraCaptureSession.CaptureCallback() {

                override fun onCaptureCompleted(session: CameraCaptureSession,
                                                request: CaptureRequest,
                                                result: TotalCaptureResult) {
                    //closeCamera()
                    state.set(STATE_PICTURE_TAKEN)
                    showBtnLayout()
                }
            }
            previewSession?.apply {
                stopRepeating()
                //abortCaptures()
                //拍照
                capture(captureBuilder?.build()!!, captureCallback, null)
            }
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

    /**
     * ***********************************************************record
     */
    var startTime: Long = 0
    private fun startRecord() {
        sessionOpenCloseLock.acquire()
        if (state.get() != STATE_RECORDING) {
            state.set(STATE_RECORDING)
            try {
                //记录拍摄时 手机方向
                recordOrientation = sensorOrientation
                record()
                sessionOpenCloseLock.release()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@CameraActivity, getString(R.string.open_camera_error_tip), Toast.LENGTH_SHORT).show()
                this@CameraActivity.finish()
            }
        }
    }

    protected fun releaseVideoRecorder() {
        mediaRecorder?.let {
            it.setOnErrorListener(null)
            it.setOnInfoListener(null)
            it.setPreviewDisplay(null)
            try {
                if(recordeStart) {
                    val stopTime = System.currentTimeMillis() - startTime
                    Log.e(TAG, "releaseVideoRecorder: 录制时长${stopTime}")
                    if (stopTime < 1100) {
                        Thread.sleep(1100 - stopTime)
                    }
                    it.stop()
                }else {

                }
            } catch (ignore: IllegalStateException) {
                //ignore.printStackTrace()
            } catch (ignore: RuntimeException) {
                val writer = StringWriter()
                val printWriter = PrintWriter(writer)
                ignore.printStackTrace(printWriter)
                val stack_trace: String = writer.buffer.toString()
                Log.e(TAG, "stopRecord: ${stack_trace}")
            } catch (ignore: java.lang.Exception) {

            } finally {
                it.reset()
                it.release()
                backgroundHandler?.removeCallbacks(startRecordRunnable)
                mediaRecorder = null
            }
        }
    }

    private fun stopRecord() {
        sessionOpenCloseLock.acquire()
        if (state.get() == STATE_RECORDING) {
            if (recordeStart) {
                mediaPlayer.reset()
                //临界状态
                state.set(STATE_RECORD_PROCESS)
                releaseVideoRecorder()
            }else{
                Log.e(TAG, "stopRecord: 清理mediaRecorder")
                Toast.makeText(this@CameraActivity, getString(R.string.record_time_short), Toast.LENGTH_SHORT).show()
                mediaRecorder?.let {
                    it.reset()
                    it.release()
                }
                backgroundHandler?.removeCallbacks(startRecordRunnable)
                mediaRecorder = null
                state.set(STATE_PREVIEW)
                recordeStart=false
                sessionOpenCloseLock.release()
                return
            }
            try {
                if(recordeStart) {
                    closeCamera()
                    initMp4Composer()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                val writer = StringWriter()
                val printWriter = PrintWriter(writer)
                e.printStackTrace(printWriter)
                val stack_trace: String = writer.buffer.toString()
                Log.e(TAG, "stopRecord: ${stack_trace}")
                Toast.makeText(this@CameraActivity, getString(R.string.record_time_short), Toast.LENGTH_SHORT).show()
                File(recordPath).delete()
                previewSession?.apply {
                    stopRepeating()
                    abortCaptures()
                }
                unlockFocus()
            } finally {
                recordeStart=false
                sessionOpenCloseLock.release()
            }
        } else {
            //已经被操作了 直接释放信号
            sessionOpenCloseLock.release()
        }
    }
    private fun initMp4Composer(){
        if (TextUtils.isEmpty(recordPath)||TextUtils.isEmpty(comRecordPath)) return
        val isBackCamera = CameraConfig.last_camera_id == CameraConfig.BACK_CAMERA_ID

        binding.circleProgressbar.post {
            binding.circleProgressbar.setProgress(0)
            binding.circleProgressbar.visibility = View.VISIBLE
        }
        //解决镜像问题
        mp4Composer = Mp4Composer(recordPath, comRecordPath)
                .rotation(correctRecord())
                .flipHorizontal(!isBackCamera)
                .listener(object : Mp4Composer.Listener {
                    override fun onFailed(exception: Exception?) {
                        exception?.printStackTrace()
                    }

                    override fun onProgress(progress: Double) {
                        Log.d("CameraActivity", "onProgress(CameraActivity:888): ${progress}")
                        binding.circleProgressbar.setProgress(progress = (progress*100).toInt())
                    }

                    override fun onCurrentWrittenVideoTime(timeUs: Long) {
                    }

                    override fun onCanceled() {
                    }

                    override fun onCompleted() {
                        binding.circleProgressbar.setProgress(progress = 100)
                        binding.circleProgressbar.postDelayed( {
                            binding.circleProgressbar.visibility = View.GONE
                        },200)
                        //任务被取消
                        if (state.get() != STATE_RECORD_PROCESS) {
                            return
                        }
                        state.set(STATE_RECORD_TAKEN)
                        showBtnLayout()
                        videoPlayer()
                    }
                }).start()
    }
    private fun videoPlayer(){
        if (TextUtils.isEmpty(recordPath)||TextUtils.isEmpty(comRecordPath)) return
        val uri: Uri
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(this@CameraActivity, CameraProvider.getFileProviderName(this@CameraActivity), File(comRecordPath))
        } else {
            uri = Uri.fromFile(File(comRecordPath))
        }
        //还没开始预览便退出 mediaPlayer处于end状态则清除数据
        try {
            mediaPlayer.setDataSource(this@CameraActivity, uri)
            //AudioAttributes是一个封装音频各种属性的类
            val attrBuilder = AudioAttributes.Builder()
            //************************************* 横向拍摄需要修改preview显示方向
            if (recordOrientation == 90 || recordOrientation == 270) {
                setHorizontalPreview()
            }
            //*************************************
            //设置音频流的合适属性
            attrBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC)
            mediaPlayer.setAudioAttributes(attrBuilder.build())
            mediaPlayer.setSurface(Surface(binding.mTextureView.surfaceTexture))
            mediaPlayer.setOnPreparedListener {
                mediaPlayer.isLooping = true
                mediaPlayer.start()
                //移除原视频
                File(recordPath).delete()
            }
            mediaPlayer.prepare()

        } catch (e: Exception) {
            //移除临时文件
            File(comRecordPath).delete()
            File(recordPath).delete()
        }
    }


    /**
     * 配置MediaRecorder
     */
    private fun setUpMediaRecorder(): Boolean {
        //创建MediaPlayer用于播放
        if (!::mediaPlayer.isInitialized) {
            mediaPlayer = MediaPlayer()
        }
        try {
            //创建MediaRecorder用于录像
            mediaRecorder = MediaRecorder()
            Log.e(TAG, "setUpMediaRecorder: mediaRecorder create")
            val timeStamp = getDate()
            //删除上一个临时视频
            if (recordPath.isEmpty()) {
                File(recordPath).delete()
            }
            val recordFile = File(CameraConfig.getSaveDir(this) + File.separator + "mov_${timeStamp}.mp4")
            val comRecordFile = File(CameraConfig.getSaveDir(this) + File.separator + "mov_${timeStamp}comp.mp4")
            recordPath = recordFile.absolutePath
            comRecordPath = comRecordFile.absolutePath
            Log.e(TAG, "setUpMediaRecorder: ${recordPath}")
            if (!isPressRecord) return false
            mediaRecorder?.apply {
                reset()
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setVideoSource(MediaRecorder.VideoSource.SURFACE)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setOutputFile(recordPath)
                setVideoEncodingBitRate(1024 * 1024 * CameraConfig.RECORD_QUALITY)
                setVideoFrameRate(30)
                setMaxDuration(CameraConfig.MAX_RECORD_TIME * 1000)
                setVideoSize(recordSize.width, recordSize.height)
                setVideoEncoder(MediaRecorder.VideoEncoder.H264)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setAudioChannels(2)
                setAudioSamplingRate(4800)
                if (!isPressRecord) return false
                prepare()
            }
            Log.e(TAG, "setUpMediaRecorder: 创建成功${isPressRecord}:::${null==cameraDevice}")
            return isPressRecord
        } catch (e: java.lang.Exception) {

        }
        return false
    }

    private fun setHorizontalPreview() {
        val aspectRatio = previewSize.width.toFloat() / previewSize.height.toFloat()
        val newWidth: Int
        if (previewSize.height > (previewSize.height * aspectRatio).toInt()) {
            newWidth = previewSize.width
        } else {
            newWidth = (previewSize.height / aspectRatio).toInt()
        }
        val xoff = (previewSize.width - newWidth) / 2
        val matrix = Matrix()
        binding.mTextureView.getTransform(matrix)
        matrix.setScale(1f, newWidth.toFloat() / previewSize.width.toFloat())
        matrix.postTranslate(0f, xoff.toFloat())
        binding.mTextureView.setTransform(matrix)
    }

    private fun correctRecord(): Rotation {
        return when (CameraUtil.getJpegOrientation(CameraConfig.getCurrentCameraCameraCharacteristics(), recordOrientation)) {
            0 -> Rotation.NORMAL
            90 -> Rotation.ROTATION_90
            180 -> Rotation.ROTATION_180
            270 -> Rotation.ROTATION_270
            else -> Rotation.NORMAL
        }
    }

    private inner class OnJpegImageAvailableListener : ImageReader.OnImageAvailableListener {
        override fun onImageAvailable(reader: ImageReader) {
            captureImage?.let {
                it.close()
                captureImage = null
            }
            captureImage = reader.acquireLatestImage()
        }
    }

    companion object {
        /**
         * 捕获按钮动画
         */
        private val leftAction: TranslateAnimation = TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.5f, Animation.RELATIVE_TO_SELF,
                0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f)
        private val rightAction: TranslateAnimation = TranslateAnimation(Animation.RELATIVE_TO_SELF, -1.5f, Animation.RELATIVE_TO_SELF,
                0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f)

        init {
            leftAction.duration = 200
            rightAction.duration = 200
        }

        private const val TAG = "CameraActivityTAG"

        /**
         * Camera state: 预览相机
         */
        const val STATE_PREVIEW = 0x00000000

        /**
         * Camera state: 拍照捕获图片
         */
        const val STATE_PICTURE_TAKEN = 0x00000001

        /**
         * Camera state: 正在录像
         */
        const val STATE_RECORDING = 0x00000002

        /**
         * Camera state: 处理录像
         */
        const val STATE_RECORD_PROCESS = 0x00000003

        /**
         * Camera state: 捕获录像
         */
        const val STATE_RECORD_TAKEN = 0x00000004


        private fun getDate(): String {
            val ca = Calendar.getInstance()
            val year = ca.get(Calendar.YEAR)           // 获取年份
            val month = ca.get(Calendar.MONTH)         // 获取月份
            val day = ca.get(Calendar.DATE)            // 获取日
            val minute = ca.get(Calendar.MINUTE)       // 分
            val hour = ca.get(Calendar.HOUR)           // 小时
            val second = ca.get(Calendar.SECOND)       // 秒
            return "" + year + (month + 1) + day + hour + minute + second
        }
    }

    internal class CompareSizesByArea : Comparator<Size> {
        // We cast here to ensure the multiplications won't overflow
        override fun compare(lhs: Size, rhs: Size) =
                signum(lhs.width.toLong() * lhs.height - rhs.width.toLong() * rhs.height)
    }
}