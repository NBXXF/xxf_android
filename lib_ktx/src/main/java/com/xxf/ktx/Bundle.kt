

package com.xxf.ktx

import android.os.Bundle

@Suppress("UNCHECKED_CAST")
operator fun <T> Bundle?.get(key: String): T? = this?.get(key) as? T
