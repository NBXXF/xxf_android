package com.xxf.arch.rxjava.transformer.filter

import android.system.ErrnoException
import io.reactivex.rxjava3.functions.Predicate
import okhttp3.internal.connection.RouteException
import okhttp3.internal.http2.ConnectionShutdownException
import org.apache.http.conn.ConnectTimeoutException
import java.io.InterruptedIOException
import java.net.*
import javax.net.ssl.SSLException
import javax.net.ssl.SSLHandshakeException
import javax.net.ssl.SSLPeerUnverifiedException
import javax.net.ssl.SSLProtocolException

/**
 * 参考来源:https://blog.csdn.net/wangzhongshun/article/details/98783264
 *
 * 忽略网络错误
 */
object ErrorIgnoreNetFilter : Predicate<Throwable> {
    override fun test(t: Throwable?): Boolean {
        if (t is UnknownHostException) {
            return false
        }
        if (t is ConnectTimeoutException) {
            return false
        }
        if (t is SocketTimeoutException) {
            return false
        }
        if (t is NoRouteToHostException) {
            return false
        }
        if (t is SSLException) {
            return false
        }
        if (t is SSLHandshakeException) {
            return false
        }
        if (t is SSLProtocolException) {
            return false
        }
        if (t is InterruptedIOException) {
            return false
        }
        if (t is SocketException) {
            return false
        }
        if (t is ConnectException) {
            return false
        }
        if (t is ConnectionShutdownException) {
            return false
        }
        if (t is RouteException) {
            return false
        }
        if (t is ProtocolException) {
            return false
        }
        if (t is SSLPeerUnverifiedException) {
            return false
        }
        /**
         * #8002 android.system.ErrnoException
        connect failed: ENETUNREACH (Network is unreachable)
        #9008 android.system.ErrnoException
        isConnected failed: ECONNREFUSED (Connection refused)
         */
        if (t is ErrnoException) {
            return false
        }
        return true
    }
}