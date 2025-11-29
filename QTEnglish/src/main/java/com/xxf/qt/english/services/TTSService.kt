package com.xxf.qt.english.services

import android.speech.tts.TextToSpeech
import com.xxf.application.applicationContext
import com.xxf.log.LogUtils.logD
import com.xxf.log.logE
import java.util.Locale

object TTSService : TextToSpeech.OnInitListener {

    private val tts: TextToSpeech =  TextToSpeech(applicationContext,this);

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // 设置语言，这里是美式英语
            val result = tts.setLanguage(Locale.UK);

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                logD { "==================>此设备不支持该语言包，可能需要引导用户下载" };
            }
        } else {
            logE { "===============>TTS初始化失败" };
        }
    }

    fun speakWord(word: String) {
        tts.speak(word, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    fun setLanguage(locale:Locale){
        if(tts.language.toLanguageTag()!=locale.toLanguageTag()) {
            tts.setLanguage(locale)
        }
    }

    fun stop(){
        if(tts.isSpeaking) {
            tts.stop()
        }
    }

}