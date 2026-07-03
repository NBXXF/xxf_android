package com.xxf.view.ration.inner

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080
 * Date: 2/4/21 2:45 PM
 * Description:
 */
enum class RatioDatumMode(val mode: Int) {
    /**
     * 自动
     */
    DATUM_AUTO(0),

    /**
     * 以宽度为基准
     */
    DATUM_WIDTH(1),

    /**
     * 以高度为基准
     */
    DATUM_HEIGHT(2);

    companion object {
        @JvmStatic
        fun valueOf(mode: Int): RatioDatumMode {
            if (mode == DATUM_WIDTH.mode) {
                return DATUM_WIDTH
            }
            return if (mode == DATUM_HEIGHT.mode) {
                DATUM_HEIGHT
            } else DATUM_AUTO
        }
    }
}