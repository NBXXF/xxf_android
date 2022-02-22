package com.xxf.arch.test.navigationdemo

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.xxf.arch.fragment.XXFFragment
import com.xxf.arch.fragment.navigation.findNavController
import com.xxf.arch.test.R
import com.xxf.arch.test.databinding.FragmentFirstBinding

class FirstFragment : XXFFragment<Unit>(R.layout.fragment_first) {
    val binding by viewBinding(FragmentFirstBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.jump.setOnClickListener {
            //跳转到下一个fragment中
            findNavController().navigation(SecondFragment())
        }
    }
}