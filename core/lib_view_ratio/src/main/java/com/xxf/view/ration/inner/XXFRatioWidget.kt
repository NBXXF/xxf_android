package com.xxf.view.ration.inner

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080
 * Date: 2/4/21 2:45 PM
 * Description: 支持宽高比例
 * [R.styleable.xxf_ratio_styleable]
 *
 *
 * <declare-styleable name="xxf_ratio_styleable" tools:ignore="ResourceName">
 *
 * <attr name="widthRatio" format="float"></attr>
 *
 * <attr name="heightRatio" format="float"></attr>
 *
 * <attr name="aspectRatio" format="float"></attr>
 *
 * <attr name="datumRatio">
 *
 * <enum name="datumAuto" value="0"></enum>
 *
 * <enum name="datumWidth" value="1"></enum>
 *
 * <enum name="datumHeight" value="2"></enum>
</attr> *
</declare-styleable> *
 */
interface XXFRatioWidget {
    fun setRatio(mode: RatioDatumMode?, widthRatio: Float, heightRatio: Float)
    fun setAspectRatio(aspectRatio: Float)
}