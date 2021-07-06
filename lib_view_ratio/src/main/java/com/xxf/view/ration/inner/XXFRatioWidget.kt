package com.xxf.view.ration.inner;

/**
 * @Author: XGod  xuanyouwu@163.com  17611639080
 * Date: 2/4/21 2:45 PM
 * Description: 支持宽高比例
 * {@link R.styleable.xxf_ratio_styleable}
 * <p>
 * <declare-styleable name="xxf_ratio_styleable" tools:ignore="ResourceName">
 * <!-- 宽度比例系数 -->
 * <attr name="widthRatio" format="float" />
 * <!-- 高度比例系数 -->
 * <attr name="heightRatio" format="float" />
 * <!-- 宽高比 -->
 * <attr name="aspectRatio" format="float" />
 * <!-- 测量模式 -->
 * <attr name="datumRatio">
 * <!-- 自动 -->
 * <enum name="datumAuto" value="0" />
 * <!-- 以宽度为基准 -->
 * <enum name="datumWidth" value="1" />
 * <!-- 以高度为基准 -->
 * <enum name="datumHeight" value="2" />
 * </attr>
 * </declare-styleable>
 */

public interface XXFRatioWidget {

    void setRatio(RatioDatumMode mode, float widthRatio, float heightRatio);

    void setAspectRatio(float aspectRatio);
}
