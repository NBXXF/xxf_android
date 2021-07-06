package com.xxf.activityresult

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject

class ResultHandleV4Fragment : Fragment() {
    val resultPublisher = PublishSubject.create<ActivityResult>()
    val isAttachedBehavior = BehaviorSubject.createDefault(false)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onAttachToContext(context)
    }

    private fun onAttachToContext(context: Context) {
        isAttachedBehavior.onNext(true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        resultPublisher.onNext(ActivityResult(requestCode, resultCode, data!!))
    }
}