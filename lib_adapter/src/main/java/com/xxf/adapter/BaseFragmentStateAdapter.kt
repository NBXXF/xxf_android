package com.xxf.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * 支持ViewPager2
 */
class BaseFragmentStateAdapter : FragmentStateAdapter {
    private val fragmentsList: MutableList<Fragment> = ArrayList()
    fun getFragmentsList(): List<Fragment> {
        return fragmentsList
    }

    fun bindData(isRefresh: Boolean, fragments: List<Fragment>) {
        if (fragments.isEmpty()) {
            return
        }
        if (isRefresh) {
            fragmentsList.clear()
        }
        fragmentsList.addAll(fragments)
        notifyDataSetChanged()
    }

    constructor(fragmentActivity: FragmentActivity) : super(fragmentActivity)
    constructor(fragment: Fragment) : super(fragment)
    constructor(fragmentManager: FragmentManager, lifecycle: Lifecycle) : super(
        fragmentManager,
        lifecycle
    )


    override fun getItemCount(): Int {
        return fragmentsList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentsList[position]
    }
}