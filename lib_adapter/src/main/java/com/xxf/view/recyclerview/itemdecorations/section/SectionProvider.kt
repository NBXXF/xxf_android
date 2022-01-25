package com.xxf.view.recyclerview.itemdecorations.section

import java.util.*

interface SectionProvider {
    /**
     * key 位置 相对于adapter
     * value 分组标题
     */
    fun onProvideSection(): TreeMap<Integer, String>
}