package com.xxf.arch.toast

import android.app.Activity
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.xxf.arch.XXF
import com.xxf.snackbar.SnackBarFragment

object SnackbarUtils {
    private val topActivity: Activity?
        private get() = XXF.getActivityStackProvider().topActivity

    /**
     * 可能会被遮挡 采用dialogFragment的方式提示
     *
     * @param notice
     * @param type
     */
    @JvmStatic
    fun showSnackBar(notice: CharSequence, type: ToastType) {
        try {
            //去除snackbar
            val topActivity = topActivity
            val snackBarFragment = (topActivity as FragmentActivity?)!!.supportFragmentManager
                .findFragmentByTag(SnackBarFragment::class.java.name) as SnackBarFragment?
            snackBarFragment?.dismissAllowingStateLoss()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        val topActivity = XXF.getActivityStackProvider().topActivity
        if (topActivity != null && !topActivity.isDestroyed && !topActivity.isFinishing) {
            if (topActivity is FragmentActivity) {
                var snackBarFragment = topActivity.supportFragmentManager
                    .findFragmentByTag(SnackBarFragment::class.java.name) as SnackBarFragment?
                snackBarFragment?.dismissAllowingStateLoss()
                snackBarFragment = SnackBarFragment()
                val finalSnackBarFragment = snackBarFragment
                snackBarFragment.lifecycle.addObserver(object : DefaultLifecycleObserver {
                    override fun onStart(owner: LifecycleOwner) {
                        super<DefaultLifecycleObserver>.onStart(owner)
                        if (finalSnackBarFragment != null) {
                            showSnackBar(finalSnackBarFragment.getDecorView()!!, notice, type)
                            finalSnackBarFragment.lifecycle.removeObserver(this)
                        }
                    }
                })
                snackBarFragment.show(
                    topActivity.supportFragmentManager,
                    SnackBarFragment::class.java.name
                )
            } else {
                showSnackBar(topActivity.window.decorView, notice, type)
            }
        }
    }

    /**
     * @param rootView
     * @param notice
     * @param type
     */
    private fun showSnackBar(rootView: View, notice: CharSequence, type: ToastType) {
        try {
            ToastUtils.toastFactory.createSnackbar(rootView, notice, type, rootView.context, 0)
                .show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}