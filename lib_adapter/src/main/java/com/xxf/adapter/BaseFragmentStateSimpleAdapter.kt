package com.xxf.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle

/**
 * 支持ViewPager2
 */
class BaseFragmentStateSimpleAdapter : BaseFragmentStateAdapter<Fragment> {
    constructor(fragmentActivity: FragmentActivity) : super(fragmentActivity)
    constructor(fragment: Fragment) : super(fragment)
    constructor(fragmentManager: FragmentManager, lifecycle: Lifecycle) : super(
        fragmentManager,
        lifecycle
    )

    override fun createFragmentItem(
        position: Int,
        item: Fragment
    ): Fragment {
        return item
    }

}