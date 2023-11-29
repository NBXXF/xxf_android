package com.xxf.demo

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import com.xxf.demo.databinding.ActivityMainBinding
import com.xxf.demo.model.result
import com.xxf.demo.parser.parseAction
import java.util.*
/**
 * @version 2.3.1
 * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @Description
 * @date createTime：2023/11/29
 */
class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        with(binding) {
            testBtn.setOnClickListener {
                if (TextUtils.isEmpty(inputTv.text)) {
                    return@setOnClickListener
                }
                try {
                    resultTv.text = "运行结果:${inputTv.text.toString().parseAction()?.result?.toString() ?: "无法计算"}"
                } catch (e: Throwable) {
                    resultTv.text = "异常$e"
                }
            }
        }
    }
}