package com.xxf.view.ration.inner;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080
 * Date: 2/4/21 2:45 PM
 * Description:
 */
public enum RatioDatumMode {
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

    final int mode;

    RatioDatumMode(int mode) {
        this.mode = mode;
    }

    public static RatioDatumMode valueOf(int mode) {
        if (mode == DATUM_WIDTH.mode) {
            return DATUM_WIDTH;
        }
        if (mode == DATUM_HEIGHT.mode) {
            return DATUM_HEIGHT;
        }
        return DATUM_AUTO;
    }
}
