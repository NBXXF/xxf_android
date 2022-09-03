package com.xxf.bus.demo

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.net.nsd.NsdManager
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.util.AndroidException
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.xxf.bus.postEvent
import com.xxf.bus.subscribeEvent
import io.reactivex.rxjava3.core.Observable
import java.lang.RuntimeException

class MainActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val textView = findViewById<EditText>(R.id.test);
        val spannableStringBuilder = SpannableStringBuilder("xxxxxfhgghdfgfdghjfghfdhgfgfghfd")
        spannableStringBuilder.setSpan(
            ForegroundColorSpan(Color.RED),
            0,
            spannableStringBuilder.length,
            Spanned.SPAN_INCLUSIVE_INCLUSIVE
        )
        textView.setText(spannableStringBuilder)

        textView.requestFocus()
        textView.setSelection(textView.text.length)


        val spannableStringBuilder1 = SpannableStringBuilder("指定日期");
        spannableStringBuilder1.setSpan(
            DateSpan("指定日期"),
            0,
            4,
            Spanned.SPAN_INCLUSIVE_INCLUSIVE
        )
        textView.editableText.append(spannableStringBuilder1)

        textView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                System.out.println("================>test:" + s)
            }

        })
        textView.setOnKeyListener { v, keyCode, event ->
            System.out.println("================>test2:" + keyCode + "  " + event)
            true
        }

    }

    override fun onResume() {
        super.onResume()

        Observable.just(1)
            .map {
                if(it>0) {
                    throw RuntimeException("x mast <=0")
                }
                it
            }.onErrorResumeNext {
                Observable.just(100)
            }.subscribe {
                System.out.println("================>test xx::"+it);
            }
//        TestEvent::class.java.subscribeEvent()
//                .subscribe {
//            System.out.println("=====>"+"收到"+it);
//            runOnUiThread {
//                        Toast.makeText(this, "收到" + it, Toast.LENGTH_SHORT).show();
//                    }
//        }
//        String.javaClass.subscribeEvent()
//            .subscribe {
//
//            }
//        TestEvent("测试").postEvent();
//        "测试".postEvent();
    }
}