package com.xxf.download.demo

import com.xxf.hash.toMurmurHash32
import junit.framework.TestCase.assertEquals
import org.junit.Test


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val url="https://test-streams.mux.dev/x36xhzz/url_0/url_525/193039199_mp4_h264_aac_hd_7.ts"
        val toMurmurHash32 = url.toMurmurHash32()
        assertEquals(404611821, toMurmurHash32)
    }
}