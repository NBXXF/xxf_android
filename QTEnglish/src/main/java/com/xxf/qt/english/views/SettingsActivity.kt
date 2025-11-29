package com.xxf.qt.english.views

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.xxf.arch.bindErrorNotice
import com.xxf.ktx.doOnDebouncingClick
import com.xxf.qt.english.models.WordInfo
import com.xxf.qt.english.services.WordDbService
import com.xxf.room.demo.R
import com.xxf.room.demo.databinding.ActivitySettingsBinding
import com.xxf.rxjava.bindLifecycle
import com.xxf.rxjava.observeOnIO
import com.xxf.rxjava.observeOnMain
import com.xxf.toast.showToast
import com.xxf.utils.UriUtils
import com.xxf.utils.relaunchApp
import com.xxf.view.titlebar.OnTitleBarListener
import com.xxf.view.utils.SystemUtils
import com.xxf.viewbinding.viewBinding
import io.reactivex.rxjava3.core.Observable
import java.io.File
import java.util.concurrent.TimeUnit


class SettingsActivity : AppCompatActivity(R.layout.activity_settings) {
    private val binding by viewBinding(ActivitySettingsBinding::bind)


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
    }


    private fun initView() {
        binding.titleBar.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(v: com.xxf.view.titlebar.TitleBar?) {
                onBackPressed()
            }
        })
        with(binding) {
            btnImport.doOnDebouncingClick {
                SystemUtils.selectFileUri(this@SettingsActivity, arrayOf("*/*"))
                    .observeOnIO()
                    .map { it ->
                        val path = UriUtils.getPath(applicationContext, it)
                        val text = File(path).readText()
                        val words = text.lines().mapNotNull {
                            if (it.isNotEmpty()) {
                                return@mapNotNull WordInfo(word = it)
                            } else {
                                return@mapNotNull null
                            }
                        }
                        WordDbService.insert(words)
                    }
                    .observeOnMain()
                    .doOnNext {
                        com.xxf.toast.showToast("导入成功")
                    }
                    .delay(1,TimeUnit.SECONDS)
                    .bindErrorNotice()
                    .bindLifecycle(this@SettingsActivity)
                    .subscribe {
                        application.relaunchApp()
                    }
            }
        }
        binding.btnClear.doOnDebouncingClick {
            Observable
                .fromAction<Unit> {
                    WordDbService.delete { it }
                    Unit
                }.observeOnMain()
                .bindErrorNotice()
                .bindLifecycle(this@SettingsActivity)
                .subscribe {
                    com.xxf.toast.showToast("删除成功")
                    application.relaunchApp()
                }
        }
    }
}