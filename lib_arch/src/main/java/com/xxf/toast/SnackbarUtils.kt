package com.xxf.toast

import android.app.Activity
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.xxf.application.activityList
import com.xxf.snackbar.SnackBarFragment

object SnackbarUtils {

    /**
     * 可能会被遮挡 采用dialogFragment的方式提示
     *
     * @param notice
     * @param type
     */
    @JvmStatic
    fun showSnackBar(notice: CharSequence, type: ToastType) {
        //找到合适的activity 弹出来
        val topActivity = activityList.reversed().firstOrNull {
            !it.isUnavailable()
        }
        (topActivity as? FragmentActivity)?.let {
            try {
                //去除snackbar
                val snackBarFragment = it.supportFragmentManager
                    .findFragmentByTag(SnackBarFragment::class.java.name) as SnackBarFragment?
                snackBarFragment?.dismissAllowingStateLoss()
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }

        topActivity?.run {
            if (this is FragmentActivity) {
                var snackBarFragment = this.supportFragmentManager
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
                    this.supportFragmentManager,
                    SnackBarFragment::class.java.name
                )
            } else {
                showSnackBar(topActivity.window.decorView, notice, type)
            }
        }
    }

    private fun Activity?.isUnavailable(): Boolean {
        return this?.isDestroyed ?: true || this?.isFinishing ?: true
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