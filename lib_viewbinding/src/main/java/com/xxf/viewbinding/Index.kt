package com.xxf.viewbinding

/**
 * 教程
 * class ProfileFragment : Fragment(R.layout.profile) {
 **     // no reflection API is used under the hood
 *     private val viewBinding by viewBinding(ProfileBinding::bind)
 * }
 * class ProfileActivity : AppCompatActivity(R.layout.profile) {
 *
 *     // no reflection API is used under the hood
 *     private val viewBinding by viewBinding(ProfileBinding::bind, R.id.container)
 * }
 */