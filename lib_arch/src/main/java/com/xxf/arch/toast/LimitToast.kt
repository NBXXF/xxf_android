package com.xxf.arch.toast

import android.content.Context
import android.widget.Toast
import java.lang.ref.SoftReference

/**
 * 数量限制的toast 避免栈内挤压过多toast  也能批量取消toast
 */
class LimitToast(context: Context) : Toast(context) {
    companion object {
        internal const val MAX_TOAST = 5
        internal val referenceArrayList = ArrayList<SoftReference<Toast>>()

        /**
         * 取消所有Toast
         */
        fun cancelAll() {
            try {
                //队列里面太多
                referenceArrayList.removeAll {
                    it.get()?.cancel()
                    true
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun show() {
        try {
            //队列里面太多, 取消前面部分
            while (referenceArrayList.size >= MAX_TOAST) {
                referenceArrayList.removeFirstOrNull()?.let {
                    it.get()?.cancel()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            referenceArrayList.removeAll {
                (it.get() == this)
            }
            referenceArrayList.add(SoftReference(this))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        super.show()
    }

    override fun cancel() {
        try {
            referenceArrayList.removeAll {
                (it.get() == this)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        super.cancel()
    }

}