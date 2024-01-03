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
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.piasy.audioprocessor.AudioProcessor
import com.xxf.media.audio.StreamAudioPlayer
import com.xxf.media.audio.StreamAudioRecorder
import com.xxf.media.audio.StreamAudioRecorder.AudioDataCallback
import com.xxf.permission.isGrantedPermission
import com.xxf.permission.requestPermission
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

class StreamActivity() : AppCompatActivity() {
    var mBtnStart: Button? = null
    var mRatioBar: SeekBar? = null
    var mRatioValue: TextView? = null
    private var mStreamAudioRecorder: StreamAudioRecorder? = null
    private var mStreamAudioPlayer: StreamAudioPlayer? = null
    private var mAudioProcessor: AudioProcessor? = null
    private var mFileOutputStream: FileOutputStream? = null
    private var mOutputFile: File? = null
    private var mBuffer: ByteArray= ByteArray(BUFFER_SIZE)
    private var mIsRecording = false
    private var mRatio = 1f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stream)
        mBtnStart = findViewById(R.id.mBtnStart)
        mBtnStart?.setOnClickListener(View.OnClickListener { start() })
        findViewById<View>(R.id.mBtnPlay).setOnClickListener(
            View.OnClickListener { play() })
        findViewById<View>(R.id.mBtnPlayChanged).setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                playChanged()
            }
        })
        mRatioBar = findViewById(R.id.mRatio)
        mRatioValue = findViewById(R.id.mRatioValue)
        mBtnStart = findViewById(R.id.mBtnStart)
        mRatioBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                mRatio = progress.toFloat() / 100
                mRatioValue?.setText(mRatio.toString())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
        mStreamAudioRecorder = StreamAudioRecorder.getInstance()
        mStreamAudioPlayer = StreamAudioPlayer.getInstance()
        mAudioProcessor = AudioProcessor(BUFFER_SIZE)
        mBuffer = ByteArray(BUFFER_SIZE)
    }

    @SuppressLint("CheckResult")
    fun start() {
        if (mIsRecording) {
            stopRecord()
            mBtnStart!!.text = "Start"
            mIsRecording = false
        } else {
            val isPermissionsGranted =
                (isGrantedPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        && isGrantedPermission(Manifest.permission.RECORD_AUDIO))
            if (!isPermissionsGranted) {
                this.requestPermission(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO
                    )
                    .subscribe { granted ->
                        // not record first time to request permission
                        if (granted) {
                            Toast.makeText(
                                applicationContext, "Permission granted",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "Permission not granted", Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                startRecord()
                mBtnStart!!.text = "Stop"
                mIsRecording = true
            }
        }
    }

    fun startRecord() {
        try {
            mOutputFile = File(
                (Environment.getExternalStorageDirectory().absolutePath +
                        File.separator + System.nanoTime() + ".stream.m4a")
            )
            mOutputFile!!.createNewFile()
            mFileOutputStream = FileOutputStream(mOutputFile)
            mStreamAudioRecorder!!.start(object : AudioDataCallback {
                override fun onAudioData(data: ByteArray, size: Int) {
                    if (mFileOutputStream != null) {
                        try {
                            Log.d("AMP", "amp " + calcAmp(data, size))
                            mFileOutputStream!!.write(data, 0, size)
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onError() {
                    mBtnStart!!.post({
                        Toast.makeText(
                            getApplicationContext(), "Record fail",
                            Toast.LENGTH_SHORT
                        ).show()
                        mBtnStart!!.setText("Start")
                        mIsRecording = false
                    })
                }
            })
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun calcAmp(data: ByteArray, size: Int): Int {
        var amplitude = 0
        var i = 0
        while (i + 1 < size) {
            val value =
                (((data[i + 1].toInt() and 0x000000FF) shl 8) + (data[i + 1].toInt() and 0x000000FF)).toShort()
            amplitude += Math.abs(value.toInt())
            i += 2
        }
        amplitude /= size / 2
        return amplitude / 2048
    }

    private fun stopRecord() {
        mStreamAudioRecorder!!.stop()
        try {
            mFileOutputStream!!.close()
            mFileOutputStream = null
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun play() {
        Observable.just(mOutputFile)
            .subscribeOn(Schedulers.io())
            .doOnError(object : Consumer<Throwable> {
                @Throws(Exception::class)
                override fun accept(throwable: Throwable) {
                    Handler(Looper.getMainLooper())
                        .post(object : Runnable {
                            override fun run() {
                                Toast.makeText(
                                    this@StreamActivity,
                                    "ex:$throwable",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        })
                    println("=========>ex:$throwable")
                }
            })
            .subscribe({ file: File? ->
                try {
                    mStreamAudioPlayer!!.init()
                    val inputStream: FileInputStream = FileInputStream(file)
                    var read: Int
                    while ((inputStream.read(mBuffer).also { read = it }) > 0) {
                        mStreamAudioPlayer!!.play(mBuffer, read)
                    }
                    inputStream.close()
                    mStreamAudioPlayer!!.release()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }, { obj: Throwable -> obj.printStackTrace() })
    }

    fun playChanged() {
        Observable.just(mOutputFile)
            .subscribeOn(Schedulers.io())
            .subscribe({ file: File? ->
                try {
                    mStreamAudioPlayer!!.init()
                    val inputStream: FileInputStream = FileInputStream(file)
                    var read: Int
                    while ((inputStream.read(mBuffer).also { read = it }) > 0) {
                        mStreamAudioPlayer!!.play(
                            if (mRatio == 1f) mBuffer else mAudioProcessor!!.process(
                                mRatio, mBuffer,
                                StreamAudioRecorder.DEFAULT_SAMPLE_RATE
                            ),
                            read
                        )
                    }
                    inputStream.close()
                    mStreamAudioPlayer!!.release()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }, { obj: Throwable -> obj.printStackTrace() })
    }



    companion object {
        val BUFFER_SIZE = 2048
    }
}