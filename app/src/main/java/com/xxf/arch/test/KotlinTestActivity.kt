package com.xxf.arch.test

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.xxf.application.activity.allActivity
import com.xxf.application.activity.topActivity
import com.xxf.arch.bindErrorNotice
import com.xxf.arch.utils.ToastUtils
import io.reactivex.rxjava3.core.Observable
import java.lang.RuntimeException

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * date createTime：2021/9/3
 * Description ://TODO
 */
class KotlinTestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        allActivity.forEach {
            System.out.println("=======>act:$it")
        }
//        ToastUtils.showToast("t:$topActivity")
//

    }

    override fun onResume() {
        super.onResume()
//        setResult(Activity.RESULT_OK);
//        finish()


        Observable
            .concatDelayError(listOf(Observable.fromCallable {
                throw RuntimeException("xxx")
            }, Observable.fromCallable {
                true
            })).bindErrorNotice()
            .subscribe {
                Log.d("===========>", "xxx:" + it)
            }

        Observable.fromCallable<Boolean> {
            throw RuntimeException("xxx76463465")
        }.bindErrorNotice()
            .subscribe {
                Log.d("===========>", "xxx22:" + it)
            }
    }
}