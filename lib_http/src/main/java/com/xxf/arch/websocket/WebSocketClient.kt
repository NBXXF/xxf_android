package com.xxf.arch.websocket

import android.content.Context
import android.net.ConnectivityManager
import android.os.Handler
import android.os.Looper
import androidx.annotation.CallSuper
import com.xxf.application.applicationContext
import com.xxf.arch.http.OkHttpClientBuilder
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject
import okhttp3.*
import okio.ByteString
import java.io.Closeable
import java.io.IOException
import java.util.concurrent.TimeUnit


/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：8/25/19
 * Description ://socket 封装
 * 1. 支持重试
 * 2. 支持取消
 * 3. 支持rxjava
 */
interface IWebSocketClient : Closeable {
    /**
     * 发送消息
     */
    fun send(text: String): Boolean

    /**
     * 发送二进制消息
     */
    fun send(bytes: ByteString): Boolean

    /**
     * 订阅消息回调
     */
    fun subTextMessage(): Observable<String>

    /**
     * 订阅二进制消息回调
     */
    fun subByteMessage(): Observable<ByteString>
}

class WebSocketClient : IWebSocketClient, WebSocketListener {
    private val bus: Subject<Any> by lazy {
        PublishSubject.create<Any>().toSerialized();
    }
    private val okHttpClient: OkHttpClient
    private val request: Request
    private val retryStep: Long
    private val retryDelayTime: Long//延迟重试时间  最终时间 retryCount*retryStep*retryTime
    private var retryCount: Long = 0
    private val handler: Handler = Handler(Looper.getMainLooper())
    private var currentWebSocket: WebSocket? = null;

    constructor(url: String) : this(OkHttpClientBuilder().build(), Request.Builder().url(url).build())

    constructor(okHttpClient: OkHttpClient, request: Request) : this(okHttpClient, request, 1, TimeUnit.SECONDS.toMillis(10))

    constructor(okHttpClient: OkHttpClient, request: Request, retryStep: Long, retryDelayTime: Long) : super() {
        this.okHttpClient = okHttpClient
        this.request = request
        this.retryStep = retryStep
        this.retryDelayTime = retryDelayTime
        currentWebSocket = okHttpClient.newWebSocket(request, this);
    }

    @CallSuper
    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        super.onMessage(webSocket, bytes)
        bus.onNext(bytes);
    }

    @CallSuper
    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        bus.onNext(text);
    }

    @CallSuper
    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        if (retryDelayTime > 0) {
            if(isNetworkConnected()) {
                reconnect(retryDelayTime * retryStep * (++retryCount), TimeUnit.MINUTES.toMillis(1), request.newBuilder().build());
            }else {
                //没有网络 500ms 尝试
                reconnect(TimeUnit.MILLISECONDS.toMillis(500), TimeUnit.MINUTES.toMillis(1), request.newBuilder().build());
            }
        }
    }

    /**
     * @param delay 重试延迟时间
     * @param max 重试最大延迟时间
     * @param request 重试的request请求
     */
    protected fun reconnect(delay: Long, max: Long, request: Request) {
        try {
            close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        handler.postDelayed(Runnable {
            currentWebSocket = okHttpClient.newWebSocket(request, this);
        }, if (delay > max) max else delay)
    }

    /**
     * 判断网络是否链接
     */
    fun isNetworkConnected(): Boolean {
        val mConnectivityManager = applicationContext
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val mNetworkInfo = mConnectivityManager
                .activeNetworkInfo
        if (mNetworkInfo != null) {
            return mNetworkInfo.isAvailable
        }
        return false;
    }

    @Throws(IOException::class)
    override fun close() {
        handler.removeCallbacksAndMessages(null)
        currentWebSocket?.cancel()
    }

    override fun send(text: String): Boolean {
        return currentWebSocket!!.send(text);
    }

    override fun send(bytes: ByteString): Boolean {
        return currentWebSocket!!.send(bytes);
    }

    override fun subTextMessage(): Observable<String> {
        return bus.ofType(String::class.java)
    }

    override fun subByteMessage(): Observable<ByteString> {
        return bus.ofType(ByteString::class.java)
    }
}