package com.xxf.ktx.time.convert.toDuration

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Duration
import java.time.Period


@RequiresApi(Build.VERSION_CODES.O)
fun Period.toDuration(): Duration = Duration.from(this)
