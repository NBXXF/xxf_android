/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Piasy
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.piasy.rxandroidaudio.example

import android.Manifest
import android.annotation.SuppressLint
import android.media.AudioManager
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.WorkerThread
import androidx.appcompat.app.AppCompatActivity
import com.xxf.media.audio.AudioRecorder
import com.xxf.media.audio.PlayConfig
import com.xxf.media.audio.RxAmplitude
import com.xxf.media.audio.RxAudioPlayer
import com.xxf.permission.isGrantedPermission
import com.xxf.permission.requestPermission
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.internal.functions.Functions
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.File
import java.util.LinkedList
import java.util.Queue

@Suppress("unused")
@SuppressLint("ClickableViewAccessibility")
class FileActivity() : AppCompatActivity(), AudioRecorder.OnErrorListener {
    var mFlIndicator: FrameLayout? = null
    var mTvPressToSay: TextView? = null
    var mTvLog: TextView? = null
    var mTvRecordingHint: TextView? = null
    private var mIvVoiceIndicators: MutableList<ImageView> = mutableListOf()
    private var mAudioRecorder: AudioRecorder? = null
    private var mRxAudioPlayer: RxAudioPlayer? = null
    private var mAudioFile: File? = null
    private var mRecordDisposable: Disposable? = null
    private val mAudioFiles: Queue<File?> = LinkedList()
    private val compositeDisposable = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file)
        mFlIndicator = findViewById(R.id.mFlIndicator)
        mTvPressToSay = findViewById(R.id.mTvPressToSay)
        mTvLog = findViewById(R.id.mTvLog)
        mTvPressToSay = findViewById(R.id.mTvPressToSay)
        mTvRecordingHint = findViewById(R.id.mTvRecordingHint)
        findViewById<View>(R.id.mBtnPlay).setOnClickListener(
            View.OnClickListener { startPlay() })
        mIvVoiceIndicators = ArrayList()
        mIvVoiceIndicators.add(findViewById<View>(R.id.mIvVoiceIndicator1) as ImageView)
        mIvVoiceIndicators.add(findViewById<View>(R.id.mIvVoiceIndicator2) as ImageView)
        mIvVoiceIndicators.add(findViewById<View>(R.id.mIvVoiceIndicator3) as ImageView)
        mIvVoiceIndicators.add(findViewById<View>(R.id.mIvVoiceIndicator4) as ImageView)
        mIvVoiceIndicators.add(findViewById<View>(R.id.mIvVoiceIndicator5) as ImageView)
        mIvVoiceIndicators.add(findViewById<View>(R.id.mIvVoiceIndicator6) as ImageView)
        mIvVoiceIndicators.add(findViewById<View>(R.id.mIvVoiceIndicator7) as ImageView)
        mAudioRecorder = AudioRecorder.getInstance()
        mRxAudioPlayer = RxAudioPlayer.getInstance()
        mAudioRecorder?.setOnErrorListener(this)
        mTvPressToSay?.setOnTouchListener(View.OnTouchListener { v: View?, event: MotionEvent ->
            when (event.getAction()) {
                MotionEvent.ACTION_DOWN -> press2Record()
                MotionEvent.ACTION_UP -> release2Send()
                MotionEvent.ACTION_CANCEL -> release2Send()
                else -> {}
            }
            true
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mRxAudioPlayer != null) {
            mRxAudioPlayer!!.stopPlay()
        }
        compositeDisposable.dispose()
    }

    private fun press2Record() {
        mTvPressToSay!!.setBackgroundResource(R.drawable.button_press_to_say_pressed_bg)
        mTvRecordingHint!!.setText(R.string.voice_msg_input_hint_speaking)
        val isPermissionsGranted =
            (isGrantedPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    && isGrantedPermission(Manifest.permission.RECORD_AUDIO))
        if (!isPermissionsGranted) {
            compositeDisposable.add(
                this.requestPermission(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO
                )
                .subscribe{ granted ->
                    // not record first time to request permission
                    if (granted) {
                        Toast.makeText(
                            applicationContext, "Permission granted",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            applicationContext, "Permission not granted",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )
        } else {
            recordAfterPermissionGranted()
        }
    }

    private fun recordAfterPermissionGranted() {
        mRecordDisposable = Observable
            .fromCallable({
                mAudioFile = File(
                    (Environment.getExternalStorageDirectory().getAbsolutePath()
                            + File.separator + System.nanoTime() + ".file.m4a")
                )
                Log.d(TAG, "to prepare record")
                mAudioRecorder!!.prepareRecord(
                    MediaRecorder.AudioSource.MIC,
                    MediaRecorder.OutputFormat.MPEG_4, MediaRecorder.AudioEncoder.AAC,
                    192000, 192000, mAudioFile
                )
            })
            .flatMap({ b: Boolean? ->
                Log.d(TAG, "prepareRecord success")
                Log.d(TAG, "to play audio_record_ready: " + R.raw.audio_record_ready)
                mRxAudioPlayer!!.play(
                    PlayConfig.res(getApplicationContext(), R.raw.audio_record_ready)
                        .build()
                )
            })
            .doOnComplete({
                Log.d(TAG, "audio_record_ready play finished")
                mFlIndicator!!.post(Runnable { mFlIndicator!!.setVisibility(View.VISIBLE) })
                mAudioRecorder!!.startRecord()
            })
            .doOnNext({ b: Boolean? -> Log.d(TAG, "startRecord success") })
            .flatMap({ o: Boolean? ->
                RxAmplitude.from(
                    (mAudioRecorder)!!
                )
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ level: Int ->
                val progress: Int = mAudioRecorder!!.progress()
                Log.d(TAG, "amplitude: " + level + ", progress: " + progress)
                refreshAudioAmplitudeView(level)
                if (progress >= 12) {
                    mTvRecordingHint!!.setText(
                        String.format(
                            getString(R.string.voice_msg_input_hint_time_limited_formatter),
                            15 - progress
                        )
                    )
                    if (progress == 15) {
                        release2Send()
                    }
                }
            }, { obj: Throwable -> obj.printStackTrace() })
    }

    private fun release2Send() {
        mTvPressToSay!!.setBackgroundResource(R.drawable.button_press_to_say_bg)
        mFlIndicator!!.visibility = View.GONE
        if (mRecordDisposable != null && !mRecordDisposable!!.isDisposed) {
            mRecordDisposable!!.dispose()
            mRecordDisposable = null
        }
        compositeDisposable.add(
            Observable
                .fromCallable({
                    val seconds: Int = mAudioRecorder!!.stopRecord()
                    Log.d(TAG, "stopRecord: " + seconds)
                    if (seconds >= MIN_AUDIO_LENGTH_SECONDS) {
                        mAudioFiles.offer(mAudioFile)
                        return@fromCallable true
                    }
                    false
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ added: Boolean ->
                    if (added) {
                        mTvLog!!.setText(
                            (mTvLog!!.getText().toString() + "\n"
                                    + "audio file " + mAudioFile!!.getName() + " added")
                        )
                    }
                }, { obj: Throwable -> obj.printStackTrace() })
        )
    }

    private fun refreshAudioAmplitudeView(level: Int) {
        val end = if (level < mIvVoiceIndicators!!.size) level else mIvVoiceIndicators!!.size
        val imageViews: List<ImageView> = mIvVoiceIndicators!!.subList(0, end)
        for (item: ImageView in imageViews) {
            item.visibility = View.VISIBLE
        }
        val imageViews1: List<ImageView> =
            mIvVoiceIndicators!!.subList(end, mIvVoiceIndicators!!.size)
        for (item: ImageView in imageViews1) {
            item.visibility = View.INVISIBLE
        }
    }

    fun startPlay() {
        mTvLog!!.text = ""
        if (!mAudioFiles.isEmpty()) {
            val audioFile = mAudioFiles.poll()
            compositeDisposable.add(
                mRxAudioPlayer!!.play(
                    PlayConfig.file(audioFile)
                        .streamType(AudioManager.STREAM_VOICE_CALL)
                        .build()
                )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        Functions.emptyConsumer(),
                        { obj: Throwable -> obj.printStackTrace() },
                        { startPlay() })
            )
        }
    }

    @WorkerThread
    override fun onError(error: Int) {
        runOnUiThread(
            {
                Toast.makeText(this@FileActivity, "Error code: " + error, Toast.LENGTH_SHORT)
                    .show()
            })
    }

    companion object {
        val MIN_AUDIO_LENGTH_SECONDS = 2
        private val TAG = "FileActivity"
    }
}