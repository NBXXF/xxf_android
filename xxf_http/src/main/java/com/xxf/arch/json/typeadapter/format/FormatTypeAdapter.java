package com.xxf.arch.json.typeadapter.format;

import androidx.annotation.NonNull;

/**
 * @Description: 格式化约束
 * @Author: XGod
 * @CreateDate: 2020/11/6 10:12
 * 文档参考 https://docs.oracle.com/javase/7/docs/api/java/text/DecimalFormat.html
 */
public interface FormatTypeAdapter<T> {
    /**
     * 原始数据
     * 文档参考 https://docs.oracle.com/javase/7/docs/api/java/text/DecimalFormat.html
     * 建议使用 避免国际化问题
     * com.xxf.arch.utils.NumberUtils#format();
     * <p>
     * NumberFormat.getNumberInstance(Locale.CHINA);
     * NumberFormat.getPercentInstance(Locale.CHINA);
     * NumberFormat.getCurrencyInstance(Locale.CHINA);
     * android.text.format.DateFormat.format(DEFAULT_FORMAT, origin).toString();
     *
     * @param origin
     * @return
     */
    String format(@NonNull T origin) throws Exception;

    /**
     *
     符号	位置	本地化？	含义
     0	数	是	数字
     #	数	是	数字，零表示缺席
     .	数	是	小数点分隔符或货币小数点分隔符
     -	数	是	减号
     ,	数	是	分组分隔符
     E	数	是	用科学计数法分隔尾数和指数。 无需在前缀或后缀中引用。
     ;	子图案边界	是	分离正负子模式
     %	字首或字尾	是	乘以100并显示为百分比
     \u2030	字首或字尾	是	乘以1000并按千值显示
     ¤（\u00A4）	字首或字尾	没有	货币符号，由货币符号代替。如果加倍，则替换为国际货币符号。如果出现在模式中，则使用货币十进制分隔符代替十进制分隔符。
     '	字首或字尾	没有	用于在前缀或后缀中引用特殊字符，例如，"'#'#"格式为123到 "#123"。要自己创建一个单引号，请连续使用两个："# o''clock"。
     */

    /**
     * DecimalFormat
     * 方法教程:
     * void	applyLocalizedPattern(String pattern)
     * 将给定的模式应用于此Format对象。
     * void	applyPattern(String pattern)
     * 将给定的模式应用于此Format对象。
     * Object	clone()
     * 标准优先级；语义没有变化。
     * boolean	equals(Object obj)
     * 覆盖等于
     * StringBuffer	format(double number, StringBuffer result, FieldPosition fieldPosition)
     * 格式化双精度以产生字符串。
     * StringBuffer	format(long number, StringBuffer result, FieldPosition fieldPosition)
     * 格式化long以产生字符串。
     * StringBuffer	format(Object number, StringBuffer toAppendTo, FieldPosition pos)
     * 格式化数字并将结果文本附加到给定的字符串缓冲区。
     * AttributedCharacterIterator	formatToCharacterIterator(Object obj)
     * 格式化对象以产生AttributedCharacterIterator。
     * Currency	getCurrency()
     * 获取格式化货币值时此十进制格式使用的货币。
     * DecimalFormatSymbols	getDecimalFormatSymbols()
     * 返回十进制格式符号的副本，程序员或用户通常不会更改。
     * int	getGroupingSize()
     * 返回分组大小。
     * int	getMaximumFractionDigits()
     * 获取数字的小数部分所允许的最大位数。
     * int	getMaximumIntegerDigits()
     * 获取数字整数部分中允许的最大位数。
     * int	getMinimumFractionDigits()
     * 获取数字的小数部分所允许的最小位数。
     * int	getMinimumIntegerDigits()
     * 获取数字整数部分中允许的最小位数。
     * int	getMultiplier()
     * 获取乘数，以百分比，每千毫米和类似格式使用。
     * String	getNegativePrefix()
     * 获取否定前缀。
     * String	getNegativeSuffix()
     * 获取负后缀。
     * String	getPositivePrefix()
     * 获取正前缀。
     * String	getPositiveSuffix()
     * 得到正后缀。
     * RoundingMode	getRoundingMode()
     * 获取RoundingMode此DecimalFormat中使用的。
     * int	hashCode()
     * 覆盖hashCode
     * boolean	isDecimalSeparatorAlwaysShown()
     * 允许您获取带整数的小数点分隔符的行为。
     * boolean	isParseBigDecimal()
     * 返回parse(java.lang.String, java.text.ParsePosition) 方法是否返回BigDecimal。
     * Number	parse(String text, ParsePosition pos)
     * 解析字符串中的文本以产生Number。
     * void	setCurrency(Currency currency)
     * 设置格式化货币值时此数字格式使用的货币。
     * void	setDecimalFormatSymbols(DecimalFormatSymbols newSymbols)
     * 设置十进制格式的符号，程序员或用户通常不更改。
     * void	setDecimalSeparatorAlwaysShown(boolean newValue)
     * 允许您使用整数设置小数点分隔符的行为。
     * void	setGroupingSize(int newValue)
     * 设置分组大小。
     * void	setMaximumFractionDigits(int newValue)
     * 设置数字的小数部分中允许的最大位数。
     * void	setMaximumIntegerDigits(int newValue)
     * 设置数字的整数部分中允许的最大位数。
     * void	setMinimumFractionDigits(int newValue)
     * 设置数字的小数部分中允许的最小位数。
     * void	setMinimumIntegerDigits(int newValue)
     * 设置数字的整数部分中允许的最小位数。
     * void	setMultiplier(int newValue)
     * 设置乘数，以百分比，每千毫米和类似格式使用。
     * void	setNegativePrefix(String newValue)
     * 设置负前缀。
     * void	setNegativeSuffix(String newValue)
     * 设置负后缀。
     * void	setParseBigDecimal(boolean newValue)
     * 设置parse(java.lang.String, java.text.ParsePosition) 方法是否返回BigDecimal。
     * void	setPositivePrefix(String newValue)
     * 设置正前缀。
     * void	setPositiveSuffix(String newValue)
     * 设置正后缀。
     * void	setRoundingMode(RoundingMode roundingMode)
     * 设置RoundingMode此DecimalFormat中使用的。
     * String	toLocalizedPattern()
     * 合成表示此Format对象当前状态的本地化模式字符串。
     * String	toPattern()
     * 合成一个表示此Format对象当前状态的模式字符串。
     */
}
