package com.xxf.ktx

import android.app.Activity
import android.view.View
import android.view.Window
import androidx.fragment.app.Fragment
import androidx.fragment.app.who
import java.util.UUID

/**
 * 对于系统全局唯一,不局限于某个页面  获取view 的唯一标识
 * 避免hashId 或者id 有持久化冲突的Bug,也避免不同activity里面的组件 android 本身的id相同
 */
val View.identityId: String
    get() {
        val tag = this.getTag(R.id.tag_view_identity_id)
        return if (tag is String) {
            tag.ifBlank {
                return UUID.randomUUID().toString().also {
                    setTag(R.id.tag_view_identity_id, it)
                }
            }
        } else {
            return UUID.randomUUID().toString().also {
                setTag(R.id.tag_view_identity_id, it)
            }
        }
    }

/**
 * 整個系統唯一標識
 */
val Window.identityId: String get() = this.decorView.identityId

/**
 * 整個系統唯一標識
 */
val Activity.identityId: String get() = this.window.identityId

/**
 * 整個系統唯一標識
 */
val Fragment.identityId: String get() = this.who