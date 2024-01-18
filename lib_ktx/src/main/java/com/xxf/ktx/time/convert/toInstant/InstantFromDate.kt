package com.xxf.ktx.time.convert.toInstant

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.util.Date

@RequiresApi(Build.VERSION_CODES.O)
fun Date.toInstant(): Instant =
    Instant.ofEpochMilli(this.time)