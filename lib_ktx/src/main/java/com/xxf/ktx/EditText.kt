package com.xxf.ktx

import android.widget.EditText

/**
 * 焦点位置
 */
fun <T : EditText> T.selectFirst() {
    this.setSelection(0)
}

/**
 * 焦点位置
 */
fun <T : EditText> T.selectLast() {
    this.setSelection(this.textString.length)
}