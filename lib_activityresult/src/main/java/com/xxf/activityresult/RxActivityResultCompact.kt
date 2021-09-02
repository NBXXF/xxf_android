package com.xxf.activityresult

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import io.reactivex.rxjava3.core.Observable

object RxActivityResultCompact {
    private val FRAGMENT_TAG = RxActivityResultCompact::class.java.name
    fun startActivityForResult(
            activity: FragmentActivity, intent: Intent, requestCode: Int): Observable<ActivityResult> {
        return startActivityForResult(activity.supportFragmentManager, intent, requestCode, null)
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    fun startActivityForResult(
            activity: FragmentActivity, intent: Intent, requestCode: Int, options: Bundle?): Observable<ActivityResult> {
        return startActivityForResult(activity.supportFragmentManager, intent, requestCode, options)
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    fun startActivityForResult(
            fragment: Fragment, intent: Intent, requestCode: Int): Observable<ActivityResult> {
        return startActivityForResult(fragment.childFragmentManager, intent, requestCode, null)
    }

    fun startActivityForResult(
            fragment: Fragment, intent: Intent, requestCode: Int, options: Bundle?): Observable<ActivityResult> {
        return startActivityForResult(fragment.childFragmentManager, intent, requestCode, options)
    }

    private fun startActivityForResult(
            fragmentManager: FragmentManager, intent: Intent, requestCode: Int, options: Bundle?): Observable<ActivityResult> {
        var _fragment = fragmentManager.findFragmentByTag(FRAGMENT_TAG) as ResultHandleV4Fragment?
        if (_fragment == null) {
            _fragment = ResultHandleV4Fragment()
            val transaction = fragmentManager.beginTransaction()
            transaction.add(_fragment, FRAGMENT_TAG)
            transaction.commit()
        } else if (_fragment.isDetached) {
            val transaction = fragmentManager.beginTransaction()
            transaction.attach(_fragment)
            transaction.commit()
        }
        val fragment: ResultHandleV4Fragment = _fragment
        return fragment.isAttachedBehavior
                .filter { isAttached -> isAttached }
                .flatMap {
                    fragment.startActivityForResult(intent, requestCode, options)
                    fragment.resultPublisher
                }
                .filter { result -> result.requestCode == requestCode }.take(1)
    }
}