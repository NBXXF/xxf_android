package com.xxf.qt.english.views

import android.os.Bundle
import android.view.View
import com.xxf.arch.fragment.XXFFragment
import com.xxf.ktx.argumentBinding
import com.xxf.ktx.doOnDebouncingClick
import com.xxf.ktx.putExtras
import com.xxf.qt.english.models.WordInfo
import com.xxf.qt.english.services.TTSService
import com.xxf.room.demo.R
import com.xxf.room.demo.databinding.FragmentWordBinding
import com.xxf.viewbinding.viewBinding
import java.util.Locale

class WordFragment: XXFFragment<Unit>(R.layout.fragment_word) {
    companion object{
        fun newInstance(wordInfo: WordInfo): WordFragment{
            val fragment = WordFragment().apply {
                putExtras("wordInfo" to wordInfo)
            }
            return fragment
        }
    }

    private val binding by viewBinding(FragmentWordBinding::bind)
    private val wordInfo:WordInfo by argumentBinding("wordInfo", default = WordInfo())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }
    private fun initView(){
        with(binding){
            wordTv.text = wordInfo.word.orEmpty()
            btnPlayUk.doOnDebouncingClick {
                TTSService.setLanguage(locale = Locale.UK)
                TTSService.speakWord(word = wordInfo.word.orEmpty())
            }
            btnPlayUs.doOnDebouncingClick {
                TTSService.setLanguage(locale = Locale.US)
                TTSService.speakWord(word = wordInfo.word.orEmpty())
            }
        }
    }

}