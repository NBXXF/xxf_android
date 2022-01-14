package com.xxf.arch.rxjava.transformer.filter

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
 * 不过滤
 */
object ErrorNoFilter : Predicate<Throwable> {
    override fun test(t: Throwable?): Boolean {
        return true
    }
}